package codejam.utils.datastructures;

public class BitSetInt {
    public int bits;
    
    public void setBit(int pos) {
        bits |= 1 << pos;
    }
    
    public boolean isSet(int pos) {
        return bits >> pos != 0;
    }
}
