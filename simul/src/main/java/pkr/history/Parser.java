package pkr.history;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

public class Parser {
    
    static Logger log = LoggerFactory.getLogger(Parser.class);
    
  
    
    
    private final static Pattern XP_LINE = 
            Pattern.compile("\\+ \\d+ XP pour avoir (?:joué|gagné) une main.");
    
    private final static Pattern REALISATION_LINE = 
            Pattern.compile(".* a atteint la réalisation suivante : .* !");
    
    private final static Pattern ADJUSTEMENT =
            Pattern.compile(".* a fait un.* automatique.");
    
    static boolean isIgnoreLine(String line) 
    {
        if (line.trim().equals(""))
            return true;
        
        if (XP_LINE.matcher(line).matches())
            return true;
        
        if (REALISATION_LINE.matcher(line).matches())
            return true;
        
        if (ADJUSTEMENT.matcher(line).matches()) 
            return true;
        
        return false;
    }
    
    
    
    private static Pattern patSuivi = Pattern.compile("(.*) a suivi de ([\\d ]+)\\.");
    private static Pattern patParle = Pattern.compile("(.*) a parlé.");    
    private static Pattern patRelance = Pattern.compile("(.*) a relancé de ([\\d ]+).");
    private static Pattern patCouche = Pattern.compile("(.*) se couche.");
    private static Pattern patTapis = Pattern.compile("(.*) a fait tapis."); 
    private static Pattern patShowdown = Pattern.compile("(.*) remporte le pot \\(([\\d ]+) \\$\\) .*\\.");
    private static Pattern patGagne = Pattern.compile("(.*) gagne.");
    private static Pattern patHandBoundary = Pattern.compile("_*");
    
    public static void main(String[] args) throws IOException {
        String fileName = "C:\\codejam\\CodeJam\\simul\\hands.txt";
        File file = new File(fileName);
        List<String> lines = Files.readLines(file, Charsets.ISO_8859_1);
        
        ParserListener curState = null;
        
        for(int i = 0; i < lines.size(); ++i)
        {
            if (curState != null && curState.replayLine()) {
                log.debug("Replaying line");
                --i;
            }
            
            String line = lines.get(i);
            
            log.debug("\nProcessing line {}\n", line);
            if (isIgnoreLine(line))
                continue;
            
            Matcher match = null;
            
            if (curState == null)
            {
                match = patHandBoundary.matcher(line);
                if (match.matches()) {
                    curState = new DiscoveryPreflopState();                    
                }
                continue;
            }
            
            try{
            
            match = patSuivi.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
                String betAmtStr = match.group(2);
                
                int betAmt = Integer.parseInt(betAmtStr.replace(" ", ""), 10);
                log.debug("Suivi player {} bet {} = {} ", playerName, betAmtStr, betAmt);
                curState = curState.handleSuivi(playerName, betAmt);
                continue;
            }
            
            match = patParle.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
             
                curState = curState.handleParole(playerName);
                continue;
            }
            
            match = patCouche.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
             
                curState = curState.handleCoucher(playerName);
                continue;
            }
            
            match = patTapis.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
             
                curState = curState.handleTapis(playerName);
                continue;
            }
            
            match = patGagne.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
             
                curState = curState.handleGagne(playerName);
                continue;
            }
            
            match = patRelance.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
                String betAmtStr = match.group(2);
                
                int betAmt = Integer.parseInt(betAmtStr.replace(" ", ""), 10);
                log.debug("Relance player {} bet {} = {} ", playerName, betAmtStr, betAmt);
                curState = curState.handleRelance(playerName, betAmt);
                continue;
            }
            
            match = patShowdown.matcher(line);
            
            if (match.matches()) 
            {
                String playerName = match.group(1);
                String betAmtStr = match.group(2);
                
                int betAmt = Integer.parseInt(betAmtStr.replace(" ", ""), 10);
                //log.debug("Relance player {} bet {} = {} ", playerName, betAmtStr, betAmt);
                curState = curState.handleShowdown(playerName, betAmt);
                continue;
            }
            
            } catch (IllegalStateException ex) {
                log.warn("state", ex);
                curState = null;
                continue;
            }
            
            Preconditions.checkState(false, line);
            //log.debug("Line {}", line);
        }
        
    }
}
