package codejam.utils.datastructures;

import com.google.common.base.Preconditions;

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
                bs.setBit(i);
            
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

    public void setBits(int bits) {
        this.bits = bits;
    }

    public BitSetInt(int bits) {
        this.bits = bits;
    }
    
    public BitSetInt() {
        
    }
    
    
    public void setBit(int pos) {
        bits |= 1 << pos;
    }
    
    public void unsetBit(int pos) {
        bits &= ~(1 << pos);
    }
    
    public void toggleBit(int pos) {
        bits ^= 1 << pos;
    }
    
    public void turnOffRightmostBit() {
        bits &= bits - 1;
    }
    
    public int getRightmostBit() {
        return bits & -bits;
    }
    
    public int getRightmostUnsetBit() {
        return ~bits & (bits + 1);
    }
    
    public void setRightmostZero() {
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
