package chess.parsing;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class PgnParser {
    
    private static final Logger log = LoggerFactory.getLogger(PgnParser.class);
    
    //[PlyCount "153"]
    private final static Pattern tagRegex = Pattern.compile("\\s*\\["
            + "\\s*" 
            + "([\\S]+)"
            + "\\s+"
            + "\\\"(.*?)\\\"" 
            + "]", Pattern.MULTILINE);
    
    
    //private final static Pattern moveRegex =
          
    private final static Pattern startTag = Pattern.compile("\\s*" + Pattern.quote("[") + ".*", Pattern.DOTALL);
    
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
    
    public Game parseGame() {
        Game game = new Game();
        log.debug("Starting parse");
        int loopCheck = 0;
        while(hasTag())
        {
            if (loopCheck++ > 20)
            {
                log.error("Too many tags");
                System.exit(1);
            }
            CharSequence curBlock = input.getCurrentBlock();
            Matcher m = tagRegex.matcher(curBlock);
            
            if (!m.lookingAt()) {
                log.error("No tag!\n{}\n", curBlock);
                System.exit(1);
            }
            
            Pair<TagName, String> tag = parseTag(m);
            game.tags.add(tag);
            
            int endMarker = m.end();
            
            log.debug("Found tag {}.  marker {}", tag, endMarker);
            
            input.setCurMarker( input.getCurMarker() + endMarker );
        }
        
        game.moves = parseMoveList(false);
        
        return game;
        
    }
    
    private List<Move> parseMoveList(boolean isVariation) 
    {
        List<Move> ret = Lists.newArrayList();
        
        Move move = null;
        while( true ) 
        {
            boolean isEnd = parseMoveListEnd(isVariation);
            if (isEnd)
                break;
            move = parseMove();
            
            Preconditions.checkState(move != null);
            
            log.debug("Found move {}", move);
            ret.add(move);
        }
        
        return ret;
    }
    
    private boolean parseMoveListEnd(boolean isVariation)
    {
        CharSequence curBlock = input.getCurrentBlock();
        
        Pattern[] ends;
        
        if (isVariation)
        {
               ends =  new Pattern[] {
               
                Pattern.compile(Pattern.quote(")")) };
        } else {
            ends =  new Pattern[] {
                    Pattern.compile(Pattern.quote("1-0")),
                    Pattern.compile(Pattern.quote("0-1")),
                    Pattern.compile(Pattern.quote("1/2-1/2")),
                    Pattern.compile(Pattern.quote("*"))}; 
        }
        
        for(Pattern p : ends) {
            Matcher m = p.matcher(curBlock);
            if (m.lookingAt()) {
                input.setCurMarker( input.getCurMarker() + m.end() );
                return true;
            }
        }
        
        return false;
    }
    
    public Move parseMove()
    {
        log.debug("parseMove\n{}\n", input.getCurrentBlock());
        
        
        Move move = new Move();
        
        Integer moveNumber = parseMoveNumber();
        if (moveNumber == null) {
            return null;
        }
        move.moveNumber = moveNumber; 
        
        //Cas spécial; une variation commençant par un black move
        boolean skipWhiteMove = parseSkipWhiteMove();
        
        if (skipWhiteMove) {
            move.whiteMove = null;
        } else {
            move.whiteMove = parsePly();
            move.whiteMove.isWhiteMove = true;
        
            parseVariations(move.whiteMove);
        
            if (move.whiteMove.variations.size() > 0) {
                int bmn = parseBlackMoveNumber();
                Preconditions.checkState(bmn == moveNumber);
            }
       
            move.whiteMove.moveNumber = moveNumber;
        }
        
        move.blackMove = parsePly();
       
        parseVariations(move.blackMove);
        
        
        move.blackMove.moveNumber = moveNumber;
        
        return move;
    }
    
    private void parseVariations(Ply ply)
    {
        List<List<Move>> variations = Lists.newArrayList();
        ply.variations = variations;
        
        while(true) {
            List<Move> variation = parseVariation();
            
            if (variation == null) {
                return;
            }
            
            variations.add(variation);
        }
        
        
    }
    
    private List<Move> parseVariation()
    {
        CharSequence curBlock = input.getCurrentBlock();
        
        Pattern comment = Pattern.compile("\\s*\\(");
        
        Matcher m = comment.matcher(curBlock);
        
        if (!m.lookingAt()) {
            return null;
        }
        
        input.setCurMarker( input.getCurMarker() + m.end() );
        
        List<Move> variation = parseMoveList(true);
        
       /// curBlock = input.getCurrentBlock();
       // Preconditions.checkState(curBlock.charAt(0) == ')');
        
       // input.setCurMarker( input.getCurMarker() + 1 );
        
        return variation;
        
        
    }
    
    private Ply parsePly()
    {
        Ply ret = new Ply();
        
        ret.sanTxt = parseSanMove();
        
        parseNagCode(ret);
        
        ret.comment = parseComment();
        
        return ret;
    }
    
    private void parseNagCode(Ply ply)
    {
        Map<Integer, String> codes = Maps.newHashMap();
        
        codes.put(1, "!");
        codes.put(2, "?");
        codes.put(3, "!!");
        codes.put(4, "??");
        codes.put(5, "!?");
        codes.put(6, "?!");
        
        codes.put(10, "=");
        codes.put(11, "= (quiet)");
        
        //slight advantage
        codes.put(14, "+=");
        codes.put(15, "=+");
        
        codes.put(16, "+-"); //plus over minus
        codes.put(17, "-+");
        
        codes.put(18, "+/-");
        codes.put(19, "-/+");
        
        
        
        CharSequence curBlock = input.getCurrentBlock();
        
        Pattern comment = Pattern.compile("\\s*\\$(\\d+)\\s*");
        
        Matcher m = comment.matcher(curBlock);
        
        if (!m.lookingAt()) {
            //log.debug("Pas trouvé comment.  curBlock {}", curBlock);
            return;
        }
        
        input.setCurMarker( input.getCurMarker() + m.end() );
        
        ply.nagValue = Integer.parseInt(m.group(1));
        
        ply.nagComment = codes.get(ply.nagValue);
        
        Preconditions.checkState(ply.nagComment != null);
        
        
    }
    
    private String parseComment() 
    {
        CharSequence curBlock = input.getCurrentBlock();
        
        Pattern comment = Pattern.compile("\\{(.*?)\\}\\s*", Pattern.DOTALL);
        
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
                "[QKRBN]?" + //piece
                 "[abcdefgh]?" + //disambiguation file (precedence)
                 "[12345678]?" + //disambiguation file 
                 "x?" +
                "[abcdefgh]" + //target file
                "[12345678]" + //target rank 
                
                        "[+#]?"  //Check or mate
                + ")\\s*") ;
        
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
    
    private boolean parseSkipWhiteMove() 
    {
        CharSequence curBlock = input.getCurrentBlock();
        
        Pattern moveNumber = Pattern.compile("\\s*\\.{2}\\s*");
        
        Matcher m = moveNumber.matcher(curBlock);
        
        if (!m.lookingAt()) {
            return false;
        }
        
        input.setCurMarker( input.getCurMarker() + m.end() );
        
        return true;
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
