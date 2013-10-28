package chess.parsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


public class PgnParser {
    
    private static final Logger log = LoggerFactory.getLogger(PgnParser.class);
    
    //[PlyCount "153"]
    private final static Pattern tagRegex = Pattern.compile("\\["
            + "\\s*" 
            + "([\\S]+)"
            + "\\s+"
            + "\\\"(.*?)\\\"" 
            + "]");
    
    
    //private final static Pattern moveRegex =
          
    private final static Pattern startTag = Pattern.compile("\\s*" + Pattern.quote("[") + ".*");
    
    public static Pair<TagName, String> parseTag(String line)
    {
        MutablePair<TagName, String> ret = new MutablePair<>();
        
        Matcher m = tagRegex.matcher(line);
        
        if (!m.matches())
            return null;
        
        String tagStr = m.group(1);
        String value = m.group(2);
        
        TagName tag = TagName.valueOf(tagStr);
        
        ret.setValue(value);
        ret.setLeft(tag);
        
        return ret;
    }
    
    public static Pair<TagName, String> parseTag(Matcher m)
    {
        Preconditions.checkState(m.matches());
            
        
        String tagStr = m.group(1);
        String value = m.group(2);
        
        TagName tag = TagName.valueOf(tagStr);
        
        MutablePair<TagName, String> ret = new MutablePair<>();
        
        ret.setValue(value);
        ret.setLeft(tag);
        
        return ret;
    }
    
    
    
    Input input;
    
    public PgnParser(Input input) {
        this.input = input;
    }
    
    public void parse() {
        int loopCheck = 0;
        while(hasTag())
        {
            if (loopCheck++ > 20)
            {
                System.exit(1);
            }
            CharSequence curBlock = input.getCurrentBlock();
            Matcher m = tagRegex.matcher(curBlock);
            
            if (!m.matches()) {
                log.error("No tag BEGIN\n{}\nEND", curBlock);
                System.exit(1);
            }
            
            Pair<TagName, String> tag = parseTag(m);
            
            input.setCurMarker( m.end() );
        }
    }
    
    private boolean hasTag()
    {
        CharSequence curBlock = input.getCurrentBlock();
        
        Matcher m = startTag.matcher(input.getCurrentBlock());
        log.debug("Checking currentBlock BEGIN\n{}\nEND for tags.  Matches? {}", curBlock, m.matches());
        return m.matches();
    }
}
