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
        public List<Integer> keys;
        public int offset;
        public int next;
        public OffsetData(int k, List<Integer> prevKeys, int offset, int next) {
            super();
            this.keys = new ArrayList<>(prevKeys);
            keys.add(k);
            this.offset = offset;
            this.next = next;
        }
        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "OffsetData [keys=" + keys + ", offset=" + offset
                    + ", next=" + next + "]";
        }
        
    }
    
    /**
     * 
     * @param kPrevODList
     * @param keyIndex  the index of the generator keys.  0 = smallest bit
     * @param mod
     * @param sequence list of sequences we are trying to find the next value
     * @return
     */
    public static List<OffsetData> getPossibleOffset_kn(List<OffsetData> kPrevODList, int keyIndex, int mod, List<Integer> sequence) {
        List<OffsetData> r = new ArrayList<>();
        
        //Seed the first list
        if (kPrevODList == null && keyIndex == 0) {
            kPrevODList = new ArrayList<>();
            kPrevODList.add(new OffsetData(0,new ArrayList<Integer>(),0,UNKNOWN));
            kPrevODList.get(0).keys.clear();
        }
        
        //Decimal value of the key index 
        int keyDiff = 1 << keyIndex;
        
        //How long a complete cycle
        int keyCycle = 2*keyDiff;
        
        int prevKeyCycle = keyDiff;
        
        for (OffsetData kPrevOD : kPrevODList) {            
            
            /*
             * The goal is to guess where in the cycle the sequence starts
             */
            for (int possibleOffset = 0; possibleOffset < keyCycle; ++possibleOffset) {

                /**
                 * offset must be compatible with previous level.  Since the previous cycle
                 * will be smaller than this cyle, mod works.  For example
                 * if this cyle length is 16, and we have a previous cyle of length 4 with
                 * offset 3.  Then valid offsets are now 3, 7, 11, and 15  
                 */
                if (kPrevOD.offset != (possibleOffset % prevKeyCycle))
                    continue;

                OffsetData od = new OffsetData(NON_INIT,kPrevOD.keys,possibleOffset,NON_INIT);
                
                int k = NON_INIT;
                
                //Go through the sequence.  We start at keyDiff because it is the minimum we advace
                //in order to calculate a potential key value.  And we need to know the previous value
                for (int n = keyDiff; n < sequence.size(); ++n) {
                    int prevPosInCycle = (possibleOffset + n - keyDiff) % keyCycle;
                    int currentPosInCycle = (possibleOffset + n) % keyCycle;

                    /**
                     * The only way to calculate a key value is to not have reset to 0's as then
                     * we do not know which greater keys were triggered on or off.                    
                     * 
                     * For example, lets say the keyDiff is 100 (4 base 10).
                     * 
                     * We could attempt to calculate the key if we go from 101 0 11 to 101 1 11
                     * 
                     * 
                     */
                    if (currentPosInCycle - prevPosInCycle == keyDiff) {
                        int calculatedK = posMod(
                                sequence.get(n) - sequence.get(n - keyDiff),
                                mod);
                        if (k == NON_INIT) {
                            k = calculatedK;
                        } else if (k != calculatedK) {
                            //found an inconsistency.  We now know this offset is not possible
                            k = IMPOSSIBLE;
                            break;
                        }
                    }
                }
                
                //Here we see if a guess is possible
                int lastPosInCycle = (sequence.size() - 1+possibleOffset) % keyCycle;
                
                //Looking for something like ...11111
                //                             100000
                // Example for k5.  Subtract k4 to k0 then add k5 to get the next value
                if (lastPosInCycle == keyDiff - 1 && k >= 0) {
                    int prev = sequence.get(sequence.size()-1);
                    
                    //Subtract all the previous keys
                    for(int prevK : kPrevOD.keys) {
                        prev = posMod(prev - prevK, mod);
                    }
                    od.next =  posMod(prev + k,mod); 
                } else if (k==IMPOSSIBLE){
                    od.next = IMPOSSIBLE;
                    continue;
                } else {
                    od.next = UNKNOWN;
                }

                od.keys.set(od.keys.size()-1, k);
                r.add(od);
            }
        }
        return r;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
