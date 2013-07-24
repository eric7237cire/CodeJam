package pkr.history;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Parser {
    
    static Logger log = LoggerFactory.getLogger(Parser.class);
    
    interface State 
    {
        State handleLine(String line);
        
    }
    
    
    private final static Pattern XP_LINE = 
            Pattern.compile("\\+ \\d+ XP pour avoir (?:joué|gagné) une main.");
    
    private final static Pattern REALISATION_LINE = 
            Pattern.compile(".* a atteint la réalisation suivante : .* !");
    
    private final static Pattern ADJUSTEMENT =
            Pattern.compile(".* a fait un ajustement automatique.");
    
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
    
    
    
    
    
    public static void main(String[] args) throws IOException {
        String fileName = "C:\\codejam\\CodeJam\\simul\\handshistory.txt";
        File file = new File(fileName);
        List<String> lines = Files.readLines(file, Charsets.ISO_8859_1);
        
        State curState = new UnitializedState();
        for(int i = 0; i < lines.size(); ++i)
        {
            String line = lines.get(i);
            curState = curState.handleLine(line);
            //log.debug("Line {}", line);
        }
        
    }
}
