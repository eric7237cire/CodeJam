package codejam.y2008.round_amer.code_sequence;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.y2008.CodeSequenceTest;

import com.google.common.base.Joiner;

public class Generator {
    int n;
    public int[] keys = new int[30];
    final int MOD;
    
    final static Logger log = LoggerFactory.getLogger(CodeSequenceTest.class);
    
    public Generator(int n, int mod, int seed) {
        this.n = n;
        MOD = mod;
        
        Random r = new Random(seed);
        for(int k = 0; k < keys.length; ++k) {
            keys[k] = r.nextInt(MOD);
        }
    }
    
    public int next() {
        ++n;
        BitSet bs = BitSet.valueOf(new long[] {n});
        
        StringBuffer sb = new StringBuffer();
        
        List<Integer> keysUsed = new ArrayList<>();
        
        int sum = 0;
        for(int i = 0; i <= 29; ++i) {
            if (bs.get(i)) {
                sum += keys[i];
                keysUsed.add(i);
            }
        }
        
        sb.append("Seq ").append(sum % MOD)
        .append(" Sum ").append(n).append(" ");
        sb.append("Keys used: ").append(Joiner.on(", ").join(keysUsed));
        log.debug(sb.toString());
        return sum % MOD;
    }
}
