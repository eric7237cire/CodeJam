package codejam.utils.datastructures;

public class BitSetInt {
    private int bits;
    
    public int getBits() {
        return bits;
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
    
    public boolean isSet(int pos) {
        return (bits & 1 << pos) != 0;
    }
}
