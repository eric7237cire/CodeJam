package codejam.y2008.round_amer.code_sequence;

import java.util.List;

import com.google.common.base.Preconditions;

public class Decoder {

    
    static int posMod(int i, int mod) {
        int m = i % mod;
        if (m < 0)
            m += mod;
        
        return m;
    }

    
    public static int calculateNextAssumingStart0(int mod, List<Integer> sequence) {
        
        //Impossible to determine next element
        
        /*
         * 000 
         * 001 
         * 010  
         * 011  
         * 100  
         * 101  
         * 110  
         * 111  
         * 000  --0
         * 001  --1
         * 010  --2
         * 011  --3
         * 100  --4
         * 101  --5*
         */

        //int k1 = posMod(sequence.get(2) - sequence.get(0));
        int kBig = sequence.get(0);
        int k0 = posMod(sequence.get(1) - kBig, mod);
        int k1 = posMod(sequence.get(2) - kBig, mod);
        int k2 = posMod(sequence.get(4) - kBig, mod);
        
        int s3 = posMod(kBig+k0+k1,mod);
        Preconditions.checkState(s3 == sequence.get(3));
        
        int s4 = posMod(kBig + k2, mod);
        Preconditions.checkState(s4 == sequence.get(4));
                
        int s5 = posMod(kBig + k0+k2, mod);
        return s5;
    }

    public static int calculateNextAssumingStart7(int mod, List<Integer> sequence) {
        
        //Impossible to determine next element
        
        /*
         * 000 
         * 001 
         * 010  
         * 011  
         * 100  
         * 101  
         * 110  
         * 111  --0
         * 000  --1
         * 001  --2
         * 010  --3
         * 011  --4
         * 100  --5*
         */

        //int k1 = posMod(sequence.get(2) - sequence.get(0));
        int kBig = sequence.get(1);
        int k0 = posMod(sequence.get(2) - kBig, mod);
        int k1 = posMod(sequence.get(3) - kBig, mod);
        int kbig = sequence.get(2);
        
        int s4 = posMod(kbig + k0 + k1, mod);
        Preconditions.checkState(s4 == sequence.get(4));
                
        return -1;
    }
    
    


    public static int calculateNextAssumingStart6(int mod, List<Integer> sequence) {
        
        //Impossible to determine next element
        
        /*
         * 000 
         * 001 
         * 010  
         * 011  
         * 100  
         * 101  
         * 110  --0
         * 111  --1
         * 000  --2
         * 001  --3
         * 010  --4
         * 011  --5*
         */

        //int k1 = posMod(sequence.get(2) - sequence.get(0));
        int k0 = posMod(sequence.get(1) - sequence.get(0), mod);
        int k1 = posMod(sequence.get(4) - sequence.get(2), mod);        
        int kbig = sequence.get(2);
        
        int s3 = posMod(kbig + k0, mod);;
        Preconditions.checkState(s3 == sequence.get(3));
        
        int s4 = posMod(kbig + k1, mod); 
        Preconditions.checkState(s4 == sequence.get(4));
        
        int s5 = posMod(kbig + k1+k0, mod);
        
        return s5;
    }
    
    public static int calculateNextAssumingStart5(int mod, List<Integer> sequence) {
        
        //Impossible to determine next element
        
        /*
         * 000 
         * 001 
         * 010  
         * 011  
         * 100  
         * 101  --0
         * 110  --1
         * 111  --2
         * 000  --3
         * 001  --4
         * 010  --5*
         */

        //int k1 = posMod(sequence.get(2) - sequence.get(0));
        int k0 = posMod(sequence.get(2) - sequence.get(1), mod);
        int k1 = posMod(sequence.get(2) - sequence.get(0), mod);        
        int kbig = sequence.get(3);
        
        int s4 = posMod(kbig + k0, mod); 
        Preconditions.checkState(s4 == sequence.get(4));
        int s5 = posMod(kbig + k1, mod);
        
        return s5;
    }

    public static int calculateNextAssumingStart4(int mod, List<Integer> sequence) {
        
        //Impossible to determine next element
        
        /*
         * 000 
         * 001 
         * 010  
         * 011  
         * 100  --0
         * 101  --1
         * 110  --2
         * 111  --3
         * 000  --4
         * 001  --5*
         */

        //int k1 = posMod(sequence.get(2) - sequence.get(0));
        int k0 = posMod(sequence.get(1) - sequence.get(0), mod);
        int k1 = posMod(sequence.get(2) - sequence.get(0), mod);        
        
        int s5 = posMod(sequence.get(4)+k0, mod);
        
        return s5;
    }


    public static int calculateNextAssumingStart3(int mod, List<Integer> sequence) {
        
        //Impossible to determine next element
        
        /*
         * 000 
         * 001 
         * 010  
         * 011  --0
         * 100  --1
         * 101  --2
         * 110  --3
         * 111  --4
         * 000  --5*
         */

        //int k1 = posMod(sequence.get(2) - sequence.get(0));
        int k0 = posMod(sequence.get(2) - sequence.get(1), mod);
        int k2 = posMod(sequence.get(4) - sequence.get(0), mod);
        int k1 = posMod(sequence.get(3) - sequence.get(1), mod);        
        int kBig = posMod(sequence.get(1) - k2, mod);
        
        int s0 = posMod(kBig + k0 + k1, mod);
        Preconditions.checkState(s0==sequence.get(0));
        int s1 = posMod(kBig + k2, mod);
        Preconditions.checkState(s1==sequence.get(1));
        int s2 = posMod(kBig + k0 + k2, mod);
        Preconditions.checkState(s2==sequence.get(2));
        int s3 = posMod(kBig + k2 + k1, mod);
        Preconditions.checkState(s3==sequence.get(3));
        int s4 = posMod(kBig + k0+k1+k2, mod);
        Preconditions.checkState(s4==sequence.get(4));
        
        return -1;
    }
    
    public static int calculateNextAssumingStart2(int mod, List<Integer> sequence) {
        /*
         * 001 
         * 010  --0
         * 011  --1
         * 100  --2
         * 101  --3
         * 110  --4
         * 111  --5*next
         */

        //int k1 = posMod(sequence.get(2) - sequence.get(0));
        int k0 = posMod(sequence.get(1) - sequence.get(0), mod);
        int k1 = posMod(sequence.get(4) - sequence.get(2), mod);        
        int kBig = posMod(sequence.get(0) - k1, mod);
        int k2 = posMod(sequence.get(2) - kBig, mod);
        
        int s1 = posMod(kBig + k0 + k1, mod);
        Preconditions.checkState(s1==sequence.get(1));
        
        int s4 = posMod(kBig + k1+k2, mod);
        Preconditions.checkState(s4==sequence.get(4));
        
        int s5 = posMod(k1+k2+k0+kBig, mod);
        
        return s5;
    }
    public static int calculateNextAssumingStart1(int mod, List<Integer> sequence) {
        /*
         * 001 --**
         * 010
         * 011
         * 100
         * 101
         * 110
         * 111
         */
        int k1 = posMod(sequence.get(2) - sequence.get(0), mod);
        int k0 = posMod(sequence.get(2) - sequence.get(1), mod);
        int kBig = posMod(sequence.get(0) - k0, mod);
        int kBig2 = posMod(sequence.get(1) - k1, mod);
        Preconditions.checkState(kBig == kBig2);
        
        int k2 = posMod(sequence.get(3)-kBig, mod);
        
        int s4 = posMod(k0+k2+kBig, mod);
        Preconditions.checkState(s4==sequence.get(4));
        
        int s5 = posMod(k1+k2+kBig, mod);
        
        return s5;
    }
}
