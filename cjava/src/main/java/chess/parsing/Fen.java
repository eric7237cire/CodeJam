package chess.parsing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chess.Board;

import com.google.common.base.Preconditions;

public class Fen {
    
    static Logger log = LoggerFactory.getLogger(Fen.class);
    
    public static void main(String[] args) {
        Board b = parseFen("8/8/2k5/2pp4/4N3/8/2K5/8 w - - 0 55");
        log.info("\n{}", b.toString());
    }
    
    public static Board parseFen(String fenStr) {
        //
        Board b = new Board();
        
        int fenIdx = 0;
        for(int cIdx = 0; cIdx < fenStr.length(); ++cIdx)
        {
            if (fenIdx >= 64)
                break;
            int rank = 7 - fenIdx / 8;
            int file =  fenIdx % 8;
            
            char c = fenStr.charAt(cIdx);
            
           // log.debug("Char {} fenIdx {} rank {} file {}", c,
            //        fenIdx, rank+1, (char) ('a' + file));
                    
            if (c == '/')
            {
                Preconditions.checkState(file == 0);
                continue;
            }
            
            if (Character.isDigit(c)) 
            {
                fenIdx += Character.digit(c, 10);
                continue;
            }
            
            b.grid.setEntry(rank, file, c);
            ++fenIdx;
            
            //log.debug("\n" + b.toString());
        }
        
        return b;
    }
}
