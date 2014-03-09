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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

public class LaunchUCI {

    static Logger log = LoggerFactory.getLogger(LaunchUCI.class);

    static final IConfiguration config
     //= new StockfishConfig();
       // = new HoudiniConfig();
    = new Houdini4Config();

    
    static final int waitTime = 1 * 1000;
//5r1k/5Bp1/1p3b2/3p4/P7/2q3P1/5P1P/1Q2R1K1 w - - 0 36

     String startPos = 
    "r3kb1r/p2np2p/2p3p1/q4p1b/1p1P4/3B1N1P/PPP2PP1/R1BQR1K1 w kq - 0 14 moves";

    String endPos = startPos + " c1d2";


    // The most we can lose going from start position to end position
    final static int isBlunderThreshold = -100;

    // After score Beyond which nothing is a bludner
    final static int maximumScoreNotABlunder = 600;

    final static int maximumScoreIsBetter = 300; 
    final static int isBetterThreshold = -50;
    final static boolean isBetterCheck = true;


    final static boolean isDebug = true;
    final static boolean isPrintAllOutput = true; //isDebug;
    
    public final Lock lock = new ReentrantLock();
    
    public final Condition readyOK = lock.newCondition();
    
    public final Condition bestMoveCond = lock.newCondition();

    boolean ready = false;


    Map<String, PositionEval> cache;

    // ........
    // ........
    // ..k.....
    // ..pp....
    // ....N...
    // ........
    // ..K.....
    // ........
    // "8/8/2k5/2pp4/4N3/8/2K5/8 w - - 0 55";
    
    // ........
    // k.......
    // .qq.....
    // ........
    // ........
    // .RR.....
    // K.......
    // ........
    // "8/k7/1qq5/8/8/1QQ5/K7/8 b - - 0 55";



    PositionEval posEval = new PositionEval();

    OutputStreamWriter osw;
    Process p = null;

    @SuppressWarnings("unchecked")
    void loadCache() throws FileNotFoundException, IOException,
            ClassNotFoundException {
        cache = Maps.newHashMap();

        try {
            FileInputStream fis = new FileInputStream(config.getCacheFilename());
            ObjectInputStream ois = new ObjectInputStream(fis);

            int timeUsed = ois.readInt();

            if (timeUsed == LaunchUCI.waitTime) {
               //cache = (Map<String, PositionEval>) ois.readObject();
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

           // os.writeInt(waitTime);
           // os.writeObject(cache);

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
        
        
        
        log.debug("Done init");
    }
    
    PositionEval evalPosition(String position, int timeToCalcMs) throws IOException, InterruptedException
    {
        this.posEval = new PositionEval();
        
        PositionEval cacheScore = cache.get(position);
        
        if (cacheScore != null) {
            return cacheScore;
        }
        
        sendCommand("ucinewgame");
        sendCommand("position fen " + position);
        
        checkEngineIsReady();
        
        ready = false;
        lock.lock();
        sendCommand("go movetime " + timeToCalcMs);
        
        while(!ready) {
            log.debug("Waiting for ready {}", ready);
            bestMoveCond.await();
        }
        
        cache.put(position, posEval);
        
        
        return posEval;
        
    }
    
    void checkEngineIsReady() {
        ready = false;
        lock.lock();
        try {
        sendCommand("isready");
        
        while(!ready) {
            log.debug("Waiting for ready {}", ready);
            readyOK.await();
        }
        
        } catch (IOException ex) {
            log.error("ex", ex);
        } catch (InterruptedException ex) {
            log.error("ex", ex);
        }
        
        log.debug("Done waiting for ready");
    }

    void sendCommand(String cmd) throws IOException {
        log.debug("Sending {}", cmd);
        osw.write(cmd);
        osw.write("\n");
        osw.flush();
    }

    

    void printResults(int beforeMoveScore, int afterMoveScore) throws IOException {
        int change = afterMoveScore - beforeMoveScore;
        if (LaunchUCI.isDebug)
            log.debug("before {} After {} change {}", beforeMoveScore, afterMoveScore, change);

        if (afterMoveScore <= maximumScoreNotABlunder
                && change < isBlunderThreshold) {
            log.info("Its a blunder!");
        } else {
            log.info("Its OK!");

            if (isBetterCheck && afterMoveScore <= maximumScoreIsBetter && change < isBetterThreshold) {
                log.info("There is a better move probably");
            }
        }


    }

    public void go() {
        

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
            
            checkEngineIsReady();

            
        } catch (IOException ex) {
            log.error("ex", ex);
            p.destroy();
        } catch (Exception ex) {
            log.error("ex", ex);
            p.destroy();
        }
    }
    
    public void evalStartEndPos() {
        int startScore;
        try {
            startScore = evalPosition(startPos, waitTime).scoreCp;
        
        int endScore = evalPosition(endPos, waitTime).scoreCp;            
        endScore = -endScore;
        
        printResults(startScore, endScore);
        
        } catch (IOException ex) {
            log.error("ex",ex);
        } catch (InterruptedException ex) {
            log.error("ex",ex);
        }
        
    }
    
    public void quit() {
        
        try {
            sendCommand("quit");
            p.waitFor();
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

        writeCache();
    }

    public static void main(String[] args) throws IOException {
        LaunchUCI lau = new LaunchUCI();

        String startPos = Files.readFirstLine(new File("C:\\codejam\\CodeJam\\cjava\\startpos.txt"), Charsets.UTF_8);
        startPos = startPos.replaceAll("[^\\x00-\\x7F]", "");
        log.info("StartPos {}", startPos);
        
        String endPos = Files.readFirstLine(new File("C:\\codejam\\CodeJam\\cjava\\endpos.txt"), Charsets.UTF_8);
        endPos = endPos.replaceAll("[^\\x00-\\x7F]", "");
        log.info("EndPos {}", endPos);
        
        lau.startPos = startPos;
        lau.endPos = endPos;
        
        Board b = new Board(startPos);
        log.info("Starting Pos\n{}", b.toString());
        
        b = new Board(endPos);
        log.info("Ending Pos\n{}", b.toString());
        lau.go();
        
        lau.evalStartEndPos();
        
        lau.quit();

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
                    LaunchUCI.log.debug(line);

                // Quand le moteur est prêt, lancer le calcul
                if ("readyok".equals(line)) {
                    LaunchUCI.log.debug("readyok recieved");
                    launchUCI.lock.lock();
                    try {
                    launchUCI.ready = true;
                    launchUCI.readyOK.signal();
                    } finally {
                        launchUCI.lock.unlock();
                    }
                    

                }

                // Le moteur est fini
                Matcher bestMove = Parser.bestMove.matcher(line);

                if (bestMove.matches()) {
                    /*
                    if (!launchUCI.isPos2)
                        launchUCI.launchPosition2();
                    else if (launchUCI.isPos2)
                        launchUCI.printResults();
                        */
                    launchUCI.posEval.bestMove = bestMove.group(1);
                    launchUCI.lock.lock();
                    
                    launchUCI.ready = true;
                    launchUCI.bestMoveCond.signal();
                    
                    launchUCI.lock.unlock();
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

                    LaunchUCI.log.debug("depth {} seldepth {} time {} ", depth, seldepth, time);

                    if (score != null) {
                        if (LaunchUCI.isDebug)
                            LaunchUCI.log.debug("Latest score is {}", score);
                        
                        launchUCI.posEval.scoreCp = score;
                        
                    }
                }
            }

        } catch (IOException ioe) {
            LaunchUCI.log.error("ex", ioe);
        }
    }
}
