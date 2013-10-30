package chess.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Input {
    
    static Logger log = LoggerFactory.getLogger(Input.class);
    
    CharSequence curBlock;
    private int curMarker;
    
    StringBuffer sb = new StringBuffer();
    
   // private InputStream is;
    BufferedReader br;
    
    private final int minBlockLength = 255;
    
    boolean doneReading = false;
    
    public Input(InputStream is) {
        super();
        //this.is = is;
        
        br = new BufferedReader(new InputStreamReader(is));
    }
    
    public Input(String is) {
        super();
        
        
        br = new BufferedReader( new StringReader(is) );
    }



    public CharSequence getCurrentBlock()
    {
        if (doneReading) {
            return sb.subSequence(curMarker, sb.length());
        }
        while( ( (sb.length() - curMarker) <  minBlockLength) )
        {
            try {
                String line = br.readLine();
                
                if (line == null) 
                {
                    doneReading = true;
                    break;
                }
                
                sb.append(line);
                sb.append('\n');
            } catch (IOException ex) {
                log.error("ex",ex);
                doneReading = true;
            }
        }
        
        return sb.subSequence(curMarker, sb.length());
    }



    /**
     * @return the curMarker
     */
    public int getCurMarker() {
        return curMarker;
    }



    /**
     * @param curMarker the curMarker to set
     */
    public void setCurMarker(int curMarker) {
        this.curMarker = curMarker;
    }
}
