package codejam.utils.datastructures;

import java.math.RoundingMode;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

public class BitSetInt {
    private int bits;
    
    public int getBits() {
        return bits;
    }
    
    public static BitSetInt fromIntArray(int[] array) {
        Preconditions.checkArgument(array.length <= 31);
        
        BitSetInt bs = new BitSetInt();
        
        for(int i = 0; i < array.length; ++i) {
            if (array[i] == 1) 
                bs.set(i);
            
        }
        
        return bs;
    }
    
    public int[] toIntArray(int length) {
        int[] array = new int[length];
        for(int i = 0; i < array.length; ++i) {
            if (isSet(i)) {
                array[i] = 1;
            }
        }
        
        return array;
    }

    public void setAllBits(int bits) {
        this.bits = bits;
    }

    public BitSetInt(int bits) {
        this.bits = bits;
    }
    
    public BitSetInt(BitSetInt bits) {
        this.bits = bits.getBits();
    }
    
    public BitSetInt() {
        
    }
    
    public static BitSetInt createWithBitsSet(int lower, int upper) {
        BitSetInt r = new BitSetInt();
        for(int i = lower; i <= upper; ++i) {
            r.set(i);
        }
        return r;
    }
    
    public void set(int pos) {
        bits |= 1 << pos;
    }
    
    public BitSetInt and(BitSetInt rhs) {
        return new BitSetInt(bits & rhs.getBits());
    }
    
    public int populationCount() {
        return Integer.bitCount(bits);
    }
    
    public void unset(int pos) {
        bits &= ~(1 << pos);
    }
    
    public void toggleBit(int pos) {
        bits ^= 1 << pos;
    }
    
    public void turnOffLeaseSignificantBit() {
        bits &= bits - 1;
    }
    
    /*
     * Returns value with that bit set
     */
    public int isolateLeastSignificantBit() {
        return bits & -bits;
    }
    
    public int getLeastSignificantBitIndex() {
        return IntMath.log2(bits & -bits, RoundingMode.UNNECESSARY);
    }
    
    public int getMostSigBitIndex() {
        return IntMath.log2(bits, RoundingMode.DOWN);
    }
    
    public int isolateLeastSigUnsetBit() {
        return ~bits & (bits + 1);
    }
    
    public void unsetLeastSignificantSetBit() {
        bits &= bits - 1;
    }
    
    public void setLeastSignificantUnSetBit() {
        bits |= bits + 1;
    }
    
    public int getTrailingZeros() {
         
        int r;           // result goes here
        int MultiplyDeBruijnBitPosition[] = 
        {
          0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 
          31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
        };
        r = MultiplyDeBruijnBitPosition[((bits & -bits) * 0x077CB531) >> 27];
        return r;
    }
    
    public boolean isSet(int pos) {
        return (bits & 1 << pos) != 0;
    }
}
