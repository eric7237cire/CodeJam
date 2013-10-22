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
    
    static final String dir = "C:\\Documents and Settings\\Groning-E\\Mes documents\\Downloads\\stockfish-4-win\\stockfish-4-win\\Windows";
    //C:\Documents and Settings\Groning-E\Mes documents\Downloads\stockfish-4-win\stockfish-4-win\Windows
    static final String execName = "stockfish_4_32bit.exe";
    
    static final int waitTime = 1000;
    static final int bufferTime = 200;
    
    static int score;
    
    static Pattern cp = Pattern.compile("info .* score cp (-?\\d+) .*");
    
    static String startPos = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    //static String endPos = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/1NBQKBNR w KQkq - 0 1";
    static String endPos = "rnbqkbnr/pppppppp/8/8/8/8/1PPPPPPP/RNBQKBNR w KQkq - 0 1";
    
    //The most we can lose going from start position to end position
    static int threshold = -80;
    
    static int isBetterThreshold = -40;
    static boolean isBetterCheck = false;
    
    //After score Beyond which nothing is a bludner
    static int maximumScoreNotABlunder = 200;
    
    static boolean isDebug = false;
    
    static void sendCommand(String cmd, OutputStreamWriter osw) throws IOException
    {
        System.out.println("Sending " + cmd);
        osw.write(cmd);
        osw.write("\n");
        osw.flush();
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
        
        Thread.sleep(300);
        
        OutputStream os = p.getOutputStream();
        
        OutputStreamWriter osw = new OutputStreamWriter(os);
        
        sendCommand("uci", osw);
        sendCommand("setoption name Hash value 1024", osw);
        sendCommand("position fen " + startPos, osw);
        
        sendCommand("go movetime " + waitTime, osw);
        
        Thread.sleep(waitTime + bufferTime);
        
        int beforeMoveScore = score;
        
        sendCommand("ucinewgame", osw);
        sendCommand("position fen " + endPos, osw);
        
        sendCommand("go movetime " + waitTime, osw);
        
        Thread.sleep(waitTime + bufferTime);
        
        int afterMoveScore = score;
        
        
        
        int change = afterMoveScore - beforeMoveScore;
        if (LaunchUCI.isDebug)
            System.out.println("before " + beforeMoveScore +  " After " + afterMoveScore + " change " + change);
        
        if (afterMoveScore <= maximumScoreNotABlunder && change < threshold) {
            System.out.println("Its a blunder!");
        } else {
            System.out.println("Its OK!");
        }
        
        if (isBetterCheck && change < isBetterThreshold) 
        {
            System.out.print("There is a better move probably");
        }
        
        p.destroy();
        
        
        
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
                    if (LaunchUCI.isDebug)
                        System.out.println(type + ">" + line);  
                
                    
                    
                    Matcher m = LaunchUCI.cp.matcher(line);
                    
                    if (line != null && m.matches())
                    {
                        int score = Integer.parseInt(m.group(1));
                        if (LaunchUCI.isDebug) System.out.println("Latest score is " + score);
                        LaunchUCI.score = score;
                    }
                }
                
                } catch (IOException ioe)
                  {
                    ioe.printStackTrace();  
                  }
        }
    }


