package chess.parsing;

import java.util.Iterator;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chess.Board;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

public class Fen {
    
    static Logger log = LoggerFactory.getLogger(Fen.class);
    
    public static void main(String[] args) {
        Board b = new Board("8/8/2k5/2pp4/4N3/8/2K5/8 w - - 0 55");
        log.info("\n{}", b.toString());
    }
    
    
}
