package codejam.y2008.round_amer.code_sequence;

import java.util.List;

import com.google.common.base.Preconditions;

public class Decoder {
    final int MOD;
    List<Integer> sequence;
    
    Integer next;

    public Decoder(int mod, List<Integer> sequence, Integer next) {
        super();
        
        Preconditions.checkState(sequence.size() == 5);
        this.MOD = mod;
        this.sequence = sequence;
        this.next = next;
    }
    
    int posMod(int i) {
        int m = i % MOD;
        if (m < 0)
            m += MOD;
        
        return m;
    }
    private void calculateNextAssumingStart0() {
        int k1 = sequence.get(2) - sequence.get(0);
        int k0 = sequence.get(2) - sequence.get(1);
        int kBig = sequence.get(0) - k0;
        int kBig2 = sequence.get(1) - k1;
        Preconditions.checkState(kBig == kBig2);
    }
}
