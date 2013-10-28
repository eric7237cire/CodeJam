package chess.parsing;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


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
      //   Preconditions.checkState(m.matches());
            
        
        String tagStr = m.group(1);
        String value = m.group(2);
        
        TagName tag = null; 
        
        try {
            TagName.valueOf(tagStr);
        } catch (IllegalArgumentException ex) {
            log.error("Unrecognized tag {}", tagStr);
            return null;
        }
        
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
        log.debug("Starting parse");
        int loopCheck = 0;
        while(hasTag())
        {
            if (loopCheck++ > 20)
            {
                System.exit(1);
            }
            CharSequence curBlock = input.getCurrentBlock();
            Matcher m = tagRegex.matcher(curBlock);
            
            if (!m.lookingAt()) {
                log.error("No tag BEGIN\n{}\nEND", curBlock);
                System.exit(1);
            }
            
            Pair<TagName, String> tag = parseTag(m);
            
            
            int endMarker = m.end();
            
            log.debug("Found tag {}.  marker {}", tag, endMarker);
            
            input.setCurMarker( input.getCurMarker() + endMarker );
        }
        
        parseMoveList();
        
    }
    
    private List<Move> parseMoveList() 
    {
        List<Move> ret = Lists.newArrayList();
        
        Move move = null;
        while( (move = parseMove()) != null ) 
        {
            log.debug("Found move {}", move);
            ret.add(move);
        }
        
        return ret;
    }
    
    private Move parseMove()
    {
        log.debug("parseMove");
        
        
        Move move = new Move();
        
        Integer moveNumber = parseMoveNumber();
        if (moveNumber == null) {
            return null;
        }
        move.moveNumber = moveNumber; 
        
        
        move.whiteMove = parsePly();
        move.whiteMove.isWhiteMove = true;
        
        List<Move> whiteVar = parseVariation();
        
        if (whiteVar != null) {
            int bmn = parseBlackMoveNumber();
            Preconditions.checkState(bmn == moveNumber);
        }
        
        move.blackMove = parsePly();
       
        parseVariation();
        
        move.whiteMove.moveNumber = moveNumber;
        move.blackMove.moveNumber = moveNumber;
        
        return move;
    }
    
    private List<Move> parseVariation()
    {
        CharSequence curBlock = input.getCurrentBlock();
        if (curBlock.charAt(0) != '(')
            return null;
        
        input.setCurMarker( input.getCurMarker() + 1 );
        
        List<Move> variation = parseMoveList();
        
        curBlock = input.getCurrentBlock();
        Preconditions.checkState(curBlock.charAt(0) == ')');
        
        input.setCurMarker( input.getCurMarker() + 1 );
        
        return variation;
        
        
    }
    
    private Ply parsePly()
    {
        Ply ret = new Ply();
        
        ret.sanTxt = parseSanMove();
        ret.comment = parseComment();
        
        return ret;
    }
    
    private String parseComment() 
    {
        CharSequence curBlock = input.getCurrentBlock();
        
        Pattern comment = Pattern.compile("(\\{.*?\\})\\s*");
        
        Matcher m = comment.matcher(curBlock);
        
        if (!m.lookingAt()) {
            //log.debug("Pas trouvé comment.  curBlock {}", curBlock);
            return null;
        }
        
        input.setCurMarker( input.getCurMarker() + m.end() );
        
        return m.group(1);
    }
    
    private String parseSanMove() 
    {
        CharSequence curBlock = input.getCurrentBlock();
        
        Pattern sanTxt = Pattern.compile(
                "(" +
                "[abcdefghQKRBN]?" + //pièce en déplacement
                        "x?" +
                "[a-g][0-9])\\s*" // Carré déstination
                        );
        
        Matcher m = sanTxt.matcher(curBlock);
        
        if (m.lookingAt()) {
            input.setCurMarker( input.getCurMarker() + m.end() );
            
            return m.group(1);
            
            
        }
        
        Pattern castle = Pattern.compile("(O(?:\\-O){1,2})\\s*");
        
        m = castle.matcher(curBlock);
        
        if (m.lookingAt()) {
            input.setCurMarker( input.getCurMarker() + m.end() );
            
            return m.group(1);
            
            
        }
        
        
        log.debug("Pas trouvé sanTxt.  curBlock {}", curBlock);
        return null;
    }
    
    private Integer parseBlackMoveNumber() 
    {
        CharSequence curBlock = input.getCurrentBlock();
        
        Pattern moveNumber = Pattern.compile("\\s*(\\d+)\\.{3}\\s*");
        
        Matcher m = moveNumber.matcher(curBlock);
        
        if (!m.lookingAt()) {
            log.debug("Pas trouvé black moveNumber.  curBlock :\n{}\n", curBlock);
            return null;
        }
        
        input.setCurMarker( input.getCurMarker() + m.end() );
        
        return Integer.parseInt(m.group(1));
    }
    private Integer parseMoveNumber()
    {
        CharSequence curBlock = input.getCurrentBlock();
        
        Pattern moveNumber = Pattern.compile("\\s*(\\d+)\\.\\s*");
        
        Matcher m = moveNumber.matcher(curBlock);
        
        if (!m.lookingAt()) {
            log.debug("Pas trouvé moveNumber.  curBlock :\n{}\n", curBlock);
            return null;
        }
        
        input.setCurMarker( input.getCurMarker() + m.end() );
        
        return Integer.parseInt(m.group(1));
    }
    
    
    private boolean hasTag()
    {
        CharSequence curBlock = input.getCurrentBlock();
        
        Matcher m = startTag.matcher(input.getCurrentBlock());
        log.debug("Checking currentBlock BEGIN\n{}\nEND for tags.  Matches? {}", curBlock, m.matches());
        return m.matches();
    }
}
