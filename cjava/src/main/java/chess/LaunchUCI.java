package chess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class LaunchUCI {

    static Logger log = LoggerFactory.getLogger(LaunchUCI.class);

    static final IConfiguration config = new StockfishConfig();

    
    static final int waitTime = 3 * 1000;

    final static String startPos = 
            "3r2k1/1pp2ppp/p2rb3/8/4N3/1P2R3/1PP2PPP/4R1K1 b - - 8 22";

    final static String endPos = startPos + " moves " +
  //  "d6d1"
            "d6d5"
            //"d6d5";
            //"d6c6"
            ;
            //" moves h7h6 ";
    // + " b1a3 ";

    // The most we can lose going from start position to end position
    final static int isBlunderThreshold = -60;

    // After score Beyond which nothing is a bludner
    final static int maximumScoreNotABlunder = 350;

    final static int maximumScoreIsBetter = 250; 
    final static int isBetterThreshold = -30;
    final static boolean isBetterCheck = true;

    final static boolean isDebug = false;
    final static boolean isPrintAllOutput = false;

    Map<String, Integer> cache;

    // ........
    // ........
    // ..k.....
    // ..pp....
    // ....N...
    // ........
    // ..K.....
    // ........
    // "8/8/2k5/2pp4/4N3/8/2K5/8 w - - 0 55";

    boolean isPos2 = false;

    int beforeMoveScore = -9999999;

    int afterMoveScore = -9999999;

    OutputStreamWriter osw;
    

    @SuppressWarnings("unchecked")
    void loadCache() throws FileNotFoundException, IOException,
            ClassNotFoundException {
        cache = Maps.newHashMap();

        try {
            FileInputStream fis = new FileInputStream(config.getCacheFilename());
            ObjectInputStream ois = new ObjectInputStream(fis);

            int timeUsed = ois.readInt();

            if (timeUsed == LaunchUCI.waitTime) {
                cache = (Map<String, Integer>) ois.readObject();
            }

            ois.close();
        } catch (Exception ex) {
            log.error("ex", ex);
        }
    }

    void writeCache() {
        try {
            FileOutputStream fs = new FileOutputStream(config.getCacheFilename(), false);
            ObjectOutputStream os = new ObjectOutputStream(fs);

            os.writeInt(waitTime);
            os.writeObject(cache);

        } catch (Exception ex) {
            log.error("ex", ex);
        }
    }

    void init() throws IOException {
        log.debug("init");

        sendCommand("uci");
        for (String option : config.getOptions()) {
            sendCommand("setoption " + option);
        }
        
        Integer score = cache.get(LaunchUCI.startPos);

        if (score != null) {
            log.info("Using cache for start position");

            if (isDebug) {
                log.debug("Setting score to {}", score);
            }
            this.beforeMoveScore = score;
            launchPosition2();
            return;
        }

        
        sendCommand("position fen " + startPos);
        sendCommand("isready");
    }

    void sendCommand(String cmd) throws IOException {
        System.out.println("Sending " + cmd);
        osw.write(cmd);
        osw.write("\n");
        osw.flush();
    }

    void launchPosition2() throws IOException {
        isPos2 = true;

        Integer score = cache.get(LaunchUCI.endPos);

        if (score != null) {
            log.info("Using cache for end position");

            if (isDebug) {
                log.debug("Setting score to {}", score);
            }
            this.afterMoveScore = score;
            printResults();
            return;
        }

        sendCommand("ucinewgame");
        sendCommand("position fen " + endPos);
        sendCommand("isready");

    }

    void printResults() throws IOException {
        int change = afterMoveScore - beforeMoveScore;
        if (LaunchUCI.isDebug)
            System.out.println("before " + beforeMoveScore + " After "
                    + afterMoveScore + " change " + change);

        if (afterMoveScore <= maximumScoreNotABlunder
                && change < isBlunderThreshold) {
            System.out.println("Its a blunder!");
        } else {
            System.out.println("Its OK!");

            if (isBetterCheck && afterMoveScore <= maximumScoreIsBetter && change < isBetterThreshold) {
                System.out.println("There is a better move probably");
            }
        }

        sendCommand("quit");

    }

    public void go() {
        Process p = null;

        try {
            loadCache();

            ProcessBuilder pb = new ProcessBuilder(config.getDirectory()
                    + File.separator + config.getExecName());
         //   Map<String, String> env = pb.environment();
            pb.directory(new File(config.getDirectory()));
           
            
            p = pb.start();

            StreamGobbler sg1 = new StreamGobbler(p.getErrorStream(), this);
            StreamGobbler sg2 = new StreamGobbler(p.getInputStream(), this);
            sg1.start();
            sg2.start();

            OutputStream os = p.getOutputStream();

            osw = new OutputStreamWriter(os);

            init();

            p.waitFor();

            writeCache();

        } catch (IOException ex) {
            log.error("ex", ex);
            p.destroy();
        } catch (InterruptedException ex) {
            log.error("ex", ex);
            p.destroy();
        } catch (Exception ex) {
            log.error("ex", ex);
            p.destroy();
        }
    }

    public static void main(String[] args) {
        LaunchUCI lau = new LaunchUCI();

        lau.go();

    }

    
}

class LaunchThread implements Runnable {

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

    }
}

class StreamGobbler extends Thread {
    InputStream is;
    LaunchUCI launchUCI;

    StreamGobbler(InputStream is, LaunchUCI launchUCI) {
        this.is = is;
        this.launchUCI = launchUCI;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (LaunchUCI.isPrintAllOutput)
                    System.out.println(line);

                // Quand le moteur est prêt, lancer le calcul
                if ("readyok".equals(line)) {
                    launchUCI.sendCommand("go movetime " + LaunchUCI.waitTime);

                }

                // Le moteur est fini
                Matcher bestMove = Parser.bestMove.matcher(line);

                if (bestMove.matches()) {
                    if (!launchUCI.isPos2)
                        launchUCI.launchPosition2();
                    else if (launchUCI.isPos2)
                        launchUCI.printResults();
                }

                if (line != null && line.startsWith("info")) {

                    Integer score = Parser.getInteger(line, Parser.score);
                    Integer mateDist = Parser.getInteger(line,
                            Parser.mateDepth);

                    if (score == null && mateDist == null)
                        continue;

                    if (score == null) {
                        Preconditions.checkState(mateDist != null);
                        
                        score = mateDist > 0 ? 40000 : -40000;
                    }
                    int depth = Parser.getInteger(line, Parser.depth);
                    int seldepth = Parser.getInteger(line,
                            Parser.seldepth);
                    int time = Parser.getInteger(line, Parser.time);

                    System.out.println("depth " + depth + " seldepth "
                            + seldepth + " time " + time);

                    if (score != null) {
                        if (LaunchUCI.isDebug)
                            System.out.println("Latest score is " + score);
                        if (launchUCI.isPos2) {
                            // Negative car c'est l'adversaire
                            launchUCI.afterMoveScore = -score;

                            launchUCI.cache.put(LaunchUCI.endPos, -score);

                        } else {
                            launchUCI.beforeMoveScore = score;

                            launchUCI.cache.put(LaunchUCI.startPos, score);
                        }
                    }
                }
            }

        } catch (IOException ioe) {
            LaunchUCI.log.error("ex", ioe);
        }
    }
}
