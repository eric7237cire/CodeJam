package codejam.y2011;

import java.util.BitSet;
import static org.junit.Assert.*;

import org.junit.Test;

public class HouseKittensTest {
    @Test
    public void testBitSet() {
        BitSet c = new BitSet(3);
        
        assertEquals(0,c.nextClearBit(0));
        
        c.set(0);
        c.set(1);
        
        assertEquals(2, c.nextClearBit(0));
        
        c.set(2);
        
        assertEquals(3, c.nextClearBit(0));
        
        c.set(3);
        
        assertEquals(4, c.nextClearBit(0));
        
    }
}
