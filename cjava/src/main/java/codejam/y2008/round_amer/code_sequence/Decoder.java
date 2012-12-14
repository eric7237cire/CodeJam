package codejam.y2008.round_amer.code_sequence;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

public class Decoder {

    
    static int posMod(int i, int mod) {
        int m = i % mod;
        if (m < 0)
            m += mod;
        
        return m;
    }
    
    public static final int IMPOSSIBLE = -2;
    public static final int UNKNOWN = -1;
    public static final int NON_INIT = -3;

    
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
        
        int s3 = posMod(sequence.get(0) + k0 + k1, mod);
        Preconditions.checkState(sequence.get(3) == s3);
        
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
    
    public static int calculateNextAssumingStart0_size4(int mod, List<Integer> sequence) {
    
        int k1_a = posMod(sequence.get(2) - sequence.get(0),mod);
        int k1_b = posMod(sequence.get(3) - sequence.get(1),mod);
        
        if (k1_a != k1_b) {
            return IMPOSSIBLE;
        }
        
        int k0_a = posMod(sequence.get(1) - sequence.get(0), mod);
        int k0_b = posMod(sequence.get(3) - sequence.get(2), mod);
        
        if (k0_a != k0_b) {
            return IMPOSSIBLE;
        }
        
        return UNKNOWN;
    }
    
    public static int calculateNextAssumingStart1_size4(int mod, List<Integer> sequence) {
        
        int k1 = posMod(sequence.get(2) - sequence.get(0),mod);
        
        int k0 = posMod(sequence.get(2) - sequence.get(1), mod);
        
        int kBig = posMod(sequence.get(0) - k0,mod);
        
        int s0 = posMod(kBig + k0,mod);
        int s1 = posMod(kBig + k1,mod);
        int s2 = posMod(kBig + k0 + k1,mod);
        
        if (s0 != sequence.get(0))
            return IMPOSSIBLE;
        
        if (s1 != sequence.get(1))
            return IMPOSSIBLE;
        
        if (s2 != sequence.get(2))
            return IMPOSSIBLE;
        
        int s3 = posMod(sequence.get(3) + k0, mod);
        return s3;
    }
    
    public static int calculateNextAssumingStart2_size4(int mod, List<Integer> sequence) {
        
        int k0_a = posMod(sequence.get(1) - sequence.get(0), mod);
        int k0_b = posMod(sequence.get(3) - sequence.get(2), mod);
        
        if (k0_a != k0_b) {
            return IMPOSSIBLE;
        }
        
        return UNKNOWN;
    }
    
    public static int calculateNextAssumingStart3_size4(int mod, List<Integer> sequence) {
        
        int k0 = posMod(sequence.get(2) - sequence.get(1), mod);
        
        int s4 = posMod(sequence.get(3)+k0,mod);
        return s4;
    }
    
    public static class OffsetData {
        public int k;
        public int offset;
        public int next;
        public OffsetData(int k, int offset, int next) {
            super();
            this.k = k;
            this.offset = offset;
            this.next = next;
        }
        public OffsetData() {
            
        }
    }
    
    
    public static List<OffsetData> getPossibleOffset_kn(List<OffsetData> kPrevODList, int keyIndex, int mod, List<Integer> sequence) {
        List<OffsetData> r = new ArrayList<>();
        
        if (kPrevODList == null && keyIndex == 0) {
            kPrevODList = new ArrayList<>();
            kPrevODList.add(new OffsetData(0,0,UNKNOWN));
        }
        
        int keyDiff = 1 << keyIndex;
        int keyCycle = 2*keyDiff;
        
        int prevKeyCycle = keyDiff;
        
        for (OffsetData kPrevOD : kPrevODList) {
            if (kPrevOD.k == IMPOSSIBLE)
                continue;
            
            for (int possibleOffset = 0; possibleOffset <= keyCycle; ++possibleOffset) {

                //offset must be compatablie with previous level
                if (kPrevOD.offset != (possibleOffset % prevKeyCycle))
                    continue;

                OffsetData od = new OffsetData();
                od.offset = possibleOffset;

                int k = NON_INIT;
                for (int n = keyDiff; n < sequence.size(); ++n) {
                    int prevN = (possibleOffset + n - keyDiff) % keyCycle;
                    int currentN = (possibleOffset + n) % keyCycle;

                    if (currentN - prevN == keyDiff) {
                        int calculatedK = posMod(
                                sequence.get(n) - sequence.get(n - keyDiff),
                                mod);
                        if (k == NON_INIT) {
                            k = calculatedK;
                        } else if (k != calculatedK) {
                            //found an inconsistency
                            k = IMPOSSIBLE;
                            break;
                        }
                    }
                }

                od.k = k;
                r.add(od);
            }
        }
        return r;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
