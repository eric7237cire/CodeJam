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
        
        int n = 424225488;
 
        int mod = 17;
        
        Generator g = new Generator(n, mod, 0);
        
        for(int k = 29; k >= 0; --k) {
            log.debug("K{}: {}", k, g.keys[k]);
        }
        
        LinkedList<Integer> buf = new LinkedList<Integer>();
        
        
        
        for(int i = 0; i < 5; ++i) {
            buf.add(g.next());            
        }
        
        Integer next = null;
        for(int i = 0; i < 500; ++i) {
            if (i % 8 == 1) {
                Preconditions.checkState(next == buf.getLast());
            }
            if (i % 8 == 0) {                
                Decoder d = new Decoder(mod, buf);
                next = d.next;
            }
            buf.add(g.next());
            buf.removeFirst();
            
        }
        
    }
}
