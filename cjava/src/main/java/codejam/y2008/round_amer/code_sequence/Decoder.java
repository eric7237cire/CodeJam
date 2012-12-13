package codejam.y2008.round_amer.code_sequence;

import java.util.List;

import com.google.common.base.Preconditions;

public class Decoder {
    final int MOD;
    List<Integer> sequence;
    
    public Integer next;

    public Decoder(int mod, List<Integer> sequence) {
        super();
        
        Preconditions.checkState(sequence.size() == 5);
        this.MOD = mod;
        this.sequence = sequence;
        
        next = calculateNextAssumingStart1();
    }
    
    int posMod(int i) {
        int m = i % MOD;
        if (m < 0)
            m += MOD;
        
        return m;
    }
    private int calculateNextAssumingStart1() {
        int k1 = posMod(sequence.get(2) - sequence.get(0));
        int k0 = posMod(sequence.get(2) - sequence.get(1));
        int kBig = posMod(sequence.get(0) - k0);
        int kBig2 = posMod(sequence.get(1) - k1);
        Preconditions.checkState(kBig == kBig2);
        
        int k2 = posMod(sequence.get(3)-kBig);
        
        int s4 = posMod(k0+k2+kBig);
        Preconditions.checkState(s4==sequence.get(4));
        
        int s5 = posMod(k1+k2+kBig);
        
        return s5;
    }
}
