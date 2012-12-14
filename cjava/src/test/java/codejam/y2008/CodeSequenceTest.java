package codejam.y2008;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.y2008.round_amer.code_sequence.Decoder;
import codejam.y2008.round_amer.code_sequence.Decoder.OffsetData;
import codejam.y2008.round_amer.code_sequence.Generator;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class CodeSequenceTest {
    final static Logger log = LoggerFactory.getLogger(CodeSequenceTest.class);
    
    
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
    
    
    @Test
    public void testCodeSeq4() {
        
        //int n = 424225488;
        int n = 0;
 
        int mod = 17;
        
        Generator g = new Generator(n, mod, 0);
        
        for(int k = 29; k >= 0; --k) {
            log.debug("K{}: {}", k, g.keys[k]);
        }
        
        LinkedList<Integer> buf = new LinkedList<Integer>();
        
        
        
        for(int i = 0; i < 4; ++i) {
            buf.add(g.next());            
        }
        
        Function<Integer,String> formatStr = new Function<Integer,String>() {
            public String apply(Integer input) {
                return formatN(input);
            }
        };
        
        
        for(int i = 0; i < 12; ++i) {
            log.debug("\n\nStarting n={}  %2={}  %4={}", n, n%2, n%4);
            int n0 = Decoder.calculateNextAssumingStart0_size4(mod,buf);
            int n1 = Decoder.calculateNextAssumingStart1_size4(mod,buf);
            int n2 = Decoder.calculateNextAssumingStart2_size4(mod,buf);
            int n3 = Decoder.calculateNextAssumingStart3_size4(mod,buf);
            
            List<OffsetData> k0_od = Decoder.getPossibleOffset_kn(null, 0, mod, buf);
            List<OffsetData> od2 = Decoder.getPossibleOffset_kn(k0_od, 1,mod,buf);
            
            
            int realNext = g.next();
            buf.add(realNext);
            buf.removeFirst();
            ++n;
            
            log.debug("Assuming starting at n % 4 == 0,1,2,3\n [{}] {}  {}   {}   {}", realNext, formatN(n0),formatN(n1),formatN(n2),formatN(n3));
            for(OffsetData od : k0_od) {
                log.debug("Offset data k0 off={} keys={} next={}", od.offset, Joiner.on(", ").join(Lists.transform(od.keys,formatStr)), formatN(od.next));
            }
            for(OffsetData od : od2) {
            log.debug("Offset data k1 off={} keys={} next={}", od.offset, Joiner.on(", ").join(Lists.transform(od.keys,formatStr)), formatN(od.next));
            }
        }
        
        List<Integer> l = Arrays.asList(1, 10, 11, 200);
        
        mod = 10007;
        int n0 = Decoder.calculateNextAssumingStart0_size4(mod,l);
        int n1 = Decoder.calculateNextAssumingStart1_size4(mod,l);
        int n2 = Decoder.calculateNextAssumingStart2_size4(mod,l);
        int n3 = Decoder.calculateNextAssumingStart3_size4(mod,l);
        log.debug("Assuming starting at n % 4 == 0,1,2,3\n  {}  {}   {}   {}", formatN(n0),formatN(n1),formatN(n2),formatN(n3));
        l = Arrays.asList(1000, 1520, 7520, 7521);
        n0 = Decoder.calculateNextAssumingStart0_size4(mod,l);
        n1 = Decoder.calculateNextAssumingStart1_size4(mod,l);
        n2 = Decoder.calculateNextAssumingStart2_size4(mod,l);
        n3 = Decoder.calculateNextAssumingStart3_size4(mod,l);        
        log.debug("Assuming starting at n % 4 == 0,1,2,3\n {}  {}   {}   {}", formatN(n0),formatN(n1),formatN(n2),formatN(n3));
        
    }
    
    String formatN(int n) {
        if (n==Decoder.IMPOSSIBLE)
            return "I";
        
        if (n==Decoder.UNKNOWN)
            return "?";
        
        return Integer.toString(n);
    }
}
