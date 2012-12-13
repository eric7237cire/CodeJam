package codejam.y2008;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import codejam.y2008.round_amer.code_sequence.Decoder;
import codejam.y2008.round_amer.code_sequence.Generator;

public class CodeSequenceTest {
    final static Logger log = LoggerFactory.getLogger(CodeSequenceTest.class);
    
    @Test
    public void testCodeSeq() {
        
        //int n = 424225488;
        int n = 0;
 
        int mod = 97;
        
        Generator g = new Generator(n, mod, 0);
        
        for(int k = 29; k >= 0; --k) {
            log.debug("K{}: {}", k, g.keys[k]);
        }
        
        LinkedList<Integer> buf = new LinkedList<Integer>();
        
        
        
        for(int i = 0; i < 5; ++i) {
            buf.add(g.next());            
        }
        
        Integer next0 = null;
        Integer next1 = null;
        Integer next2 = null;
        Integer next4 = null;
        Integer next5 = null;
        Integer next6 = null;
        Integer next7 = null;
        for(int i = 0; i < 50000; ++i) {
            if (i % 8 == 1) {
                Preconditions.checkState(next1 == buf.getLast());
                next2 = Decoder.calculateNextAssumingStart2(mod,buf);
            }
            if (i % 8 == 2) {
                Preconditions.checkState(next2 == buf.getLast());
                Decoder.calculateNextAssumingStart3(mod,buf);
            }
            if (i % 8 == 3) {
                next4 = Decoder.calculateNextAssumingStart4(mod,buf);
            }
            if (i % 8 == 4) {
                Preconditions.checkState(next4 == buf.getLast());
                next5 = Decoder.calculateNextAssumingStart5(mod,buf);
            }
            if (i % 8 == 5) {
                Preconditions.checkState(next5 == buf.getLast());
                next6 = Decoder.calculateNextAssumingStart6(mod,buf);
            }
            if (i % 8 == 6) {
                Preconditions.checkState(next6 == buf.getLast());
            }
            if (i % 8 == 7) {
                next0 = Decoder.calculateNextAssumingStart0(mod,buf);
            }
            if (i % 8 == 0 ) {                
                next1 = Decoder.calculateNextAssumingStart1(mod,buf);
                
                Preconditions.checkState(next0 == null || next0 == buf.getLast());
            }
            
            buf.add(g.next());
            buf.removeFirst();
            
        }
        
    }
}
