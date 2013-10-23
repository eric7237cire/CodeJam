import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LaunchUCI {
    
    //static final String dir = "C:\\Documents and Settings\\Groning-E\\Mes documents\\Downloads\\stockfish-4-win\\stockfish-4-win\\Windows";
    static final String dir = "C:\\Program Files (x86)\\Common Files\\ChessBase\\Engines.uci\\Houdini 3";

    //static final String execName = "stockfish_4_32bit.exe";
    static final String execName = "Houdini_3_Pro_x64";
    
    static final int waitTime = 10 * 1000;
    //static final int bufferTime = 200;
    
    final static Pattern cp = Pattern.compile("info .* depth (\\d+) seldepth (\\d+) score cp (-?\\d+) time (\\d+) .*");
    final static Pattern bestMove = Pattern.compile("bestmove (.*)");
    
   // 
    //static String startPos = "2krr3/bpp2qpp/2p2pn1/p1P5/P2PP3/5N1P/1P1B1PP1/R2QR1K1 w - - 0 19";
    //static String endPos = "2krr3/bpp2qpp/2p2pn1/B1P5/P2PP3/5N1P/1P3PP1/R2QR1K1 b - - 0 19";
    
    static String startPos =
"8/5p1p/1p4p1/6P1/5Pb1/1N6/pK6/3k4 b - - 0 55";
    
    static String endPos =

    "8/5p1p/1p2b1p1/6P1/5P2/1N6/pK6/3k4 w - - 0 56";

    
    //The most we can lose going from start position to end position
    static int isBlunderThreshold = -60;

    //After score Beyond which nothing is a bludner
    static int maximumScoreNotABlunder = 300;
    
    static int isBetterThreshold = -30;
    static boolean isBetterCheck = true;
    
    static boolean isPos2 = false;
    
    static int beforeMoveScore ;
    
    static int afterMoveScore ;
    
    static boolean isDebug = false;
    static boolean isPrintAllOutput = false;
    static OutputStreamWriter osw;
    
    static void init() throws IOException
    {
        sendCommand("uci");
        //sendCommand("setoption name Hash value 3072");
        sendCommand("setoption name Hash value 2048");
        sendCommand("setoption name Threads value 4");
        sendCommand("setoption name NalimovPath value " + "E:\\ChessDB\\tablebase\\tablebase.sesse.net\\3-4-5" );
        sendCommand("position fen " + startPos);
        sendCommand("isready");
    }
    static void sendCommand(String cmd) throws IOException
    {
        System.out.println("Sending " + cmd);
        osw.write(cmd);
        osw.write("\n");
        osw.flush();
    }
    
    static void launchPosition2() throws IOException
    {
        sendCommand("ucinewgame");
        sendCommand("position fen " + endPos);
        sendCommand("isready");
        isPos2 = true;
        
    }
    
    static void printResults() throws IOException
    {
        int change = afterMoveScore - beforeMoveScore;
        if (LaunchUCI.isDebug)
            System.out.println("before " + beforeMoveScore +  " After " + afterMoveScore + " change " + change);
        
        if (afterMoveScore <= maximumScoreNotABlunder && change < isBlunderThreshold) {
            System.out.println("Its a blunder!");
        } else {
            System.out.println("Its OK!");
            
            if (isBetterCheck && change < isBetterThreshold) 
            {
                System.out.println("There is a better move probably");
            }
        }
        
        
        
        sendCommand("quit");
        
    }
    
    public static void main(String[] args) 
    {
        try {
        ProcessBuilder pb = new ProcessBuilder(dir + "\\" + LaunchUCI.execName);
        Map<String, String> env = pb.environment();
        pb.directory(new File(LaunchUCI.dir));
        //pb.redirectOutput(Redirect.appendTo(log));
        
        /*File f = new File(dir);
        
        System.out.print(f.exists());
        f = new File(dir + "\\" + execName);
        System.out.print(f.exists());
        */
        Process p = pb.start();
        
        StreamGobbler sg1 = new StreamGobbler(p.getErrorStream(), "ERR");
        StreamGobbler sg2 = new StreamGobbler(p.getInputStream(), "OUT");
        sg1.start();
        sg2.start();
        
        
        OutputStream os = p.getOutputStream();
        
        osw = new OutputStreamWriter(os);
        
        init();
        
        p.waitFor();
        
        } catch (IOException ex) {
            System.err.print("io ex");
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            System.err.print("int ex");
        }
        
    }
}

class LaunchThread implements Runnable 
{

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() 
    {
        
        
    }
}
    
    class StreamGobbler extends Thread
    {
        InputStream is;
        String type;
        
        StreamGobbler(InputStream is, String type)
        {
            this.is = is;
            this.type = type;
        }
        
        public void run()
        {
            try
            {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line=null;
                while ( (line = br.readLine()) != null)
                {
                    if (LaunchUCI.isPrintAllOutput)
                        System.out.println(type + ">" + line);  
                
                    if ("readyok".equals(line))
                    {
                        LaunchUCI.sendCommand("go movetime " + LaunchUCI.waitTime);
                        
                    }
                    
                    Matcher bestMove = LaunchUCI.bestMove.matcher(line);
                    
                    if (bestMove.matches())
                    {
                        if (!LaunchUCI.isPos2) 
                            LaunchUCI.launchPosition2();
                        else if (LaunchUCI.isPos2) 
                            LaunchUCI.printResults();
                        
                    }
                        
                    
                    
                    
                    Matcher m = LaunchUCI.cp.matcher(line);
                    
                    if (line != null && m.matches())
                    {
                        
                        int depth = Integer.parseInt(m.group(1));
                        int seldepth = Integer.parseInt(m.group(2));
                        int time = Integer.parseInt(m.group(4));
                        int score = Integer.parseInt(m.group(3));
                        
                        System.out.println("depth " + depth + " seldepth " + seldepth + " time " + time);
                        
                        if (LaunchUCI.isDebug) System.out.println("Latest score is " + score);
                        if (LaunchUCI.isPos2)
                        {
                            //Negative car c'est l'adversaire
                            LaunchUCI.afterMoveScore = -score;
                            
                        } else {
                            LaunchUCI.beforeMoveScore = score;
                        }
                    }
                }
                
                } catch (IOException ioe)
                  {
                    ioe.printStackTrace();  
                  }
        }
    }


