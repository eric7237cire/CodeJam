package codejam.utils.datastructures;

import java.math.RoundingMode;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;

public class BitSetLong {
    private long bits;
    
    public long getBits() {
        return bits;
    }
    
    public static BitSetLong fromIntArray(int[] array) {
        Preconditions.checkArgument(array.length <= 31);
        
        BitSetLong bs = new BitSetLong();
        
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

    public void setBits(long bits) {
        this.bits = bits;
    }

    public BitSetLong(long bits) {
        this.bits = bits;
    }
    
    public BitSetLong() {
        
    }
    
    public static BitSetLong createWithBitsSet(int lower, int upper) {
        BitSetLong r = new BitSetLong();
        for(int i = lower; i <= upper; ++i) {
            r.set(i);
        }
        return r;
    }
    
    public void set(int pos) {
        bits |= 1L << pos;
    }
    
    public void unset(int pos) {
        bits &= ~(1L << pos);
    }
    
    /**
     * Bits in bitsRhs are unset
     * @param bitsRhs
     */
    public void unsetMultiple(long bitsRhs) {
        bits &= ~bitsRhs;
    }
    
    public void restrictToSet(long bitsRhs) {
        bits &= bitsRhs;
    }
    
    
    public void toggleBit(int pos) {
        bits ^= 1L << pos;
    }
    
    public void turnOffLeaseSignificantBit() {
        bits &= bits - 1;
    }
    
    /*
     * Returns value with that bit set
     */
    public long isolateLeastSignificantBit() {
        return bits & -bits;
    }
    
    public int getLeastSignificantBitIndex() {
        return LongMath.log2(bits & -bits, RoundingMode.UNNECESSARY);
    }
    
    public int getMostSigBitIndex() {
        return LongMath.log2(bits, RoundingMode.DOWN);
    }
    
    public long isolateLeastSigUnsetBit() {
        return ~bits & (bits + 1);
    }
    
    public void unsetLeastSignificantSetBit() {
        bits &= bits - 1;
    }
    
    public void setLeastSignificantUnSetBit() {
        bits |= bits + 1;
    }
    
    public int getTrailingZeros() {
         
        return Long.numberOfTrailingZeros(bits);
    }
    
    public boolean isSet(int pos) {
        return (bits & 1L << pos) != 0;
    }
    
    @Override
    public String toString() {
        return Long.toBinaryString(bits);
    }
}
