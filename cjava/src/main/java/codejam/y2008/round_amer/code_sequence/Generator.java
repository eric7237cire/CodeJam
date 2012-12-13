package codejam.y2008.round_amer.code_sequence;

import java.util.BitSet;
import java.util.Random;

public class Generator {
    int n;
    public int[] keys = new int[29];
    final int MOD;
    
    public Generator(int n, int mod, int seed) {
        this.n = n;
        MOD = mod;
        
        Random r = new Random(seed);
        for(int k = 0; k < keys.length; ++k) {
            keys[k] = r.nextInt(MOD);
        }
    }
    
    int next() {
        ++n;
        BitSet bs = BitSet.valueOf(new long[] {n});
        
        int sum = 0;
        for(int i = 0; i < 29; ++i) {
            if (bs.get(0)) {
                sum += keys[i];
            }
        }
        
        return sum % MOD;
    }
}
