package codejam.utils;

import static org.junit.Assert.assertEquals;

import java.util.BitSet;
import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.SegmentTreeSum;
import euler.Util;

public class SegmentTreeTest
{
    final protected static Logger log = LoggerFactory.getLogger("main");
    

    //@Test
    public void test()
    {
        SegmentTreeSum st = new SegmentTreeSum(10);
        
        st.update(3, 5, SegmentTreeSum.SET);
        
        int a = st.rangeSumQuery( 2, 4);
        assertEquals(2, a);
    }
    
    //@Test
    public void testRand()
    {
        int size = 517;
        int iterations = 15000;
                
        Random r = new Random(42);
        
        int[] array = new int[size];
        
        SegmentTreeSum st = new SegmentTreeSum(size);
        
        //Run queries on each element after each iteration
        boolean checkAll = false;
        
        //operations 1 erase, 2 set, 3 invert, 4 is query
        for(int i = 0; i < iterations; ++i)
        {
            int op = Util.getRandBetween(1, 4, r);
            int start = Util.getRandBetween(0, size-1,r);
            int stop = Util.getRandBetween(start, size-1,r);
            
            int q = -1;
            
            if (op == 1) {
                st.update(start, stop, SegmentTreeSum.SET);
            } else if (op == 2) {
                st.update(start, stop, SegmentTreeSum.ERASE);
            } else if (op == 3) {
                st.update( start, stop, SegmentTreeSum.INVERT);
            } else if (op == 4) {
                q = st.rangeSumQuery( start, stop);
            }
            
            int check = 0;
            for(int j = start; j <= stop; ++j)
            {
                if (op == 1) {
                    array[j] = 1;
                } else if (op == 2) {
                    array[j] = 0;
                } else if (op == 3) {
                    array[j] ^= 1;
                } else if (op == 4) {
                    check += array[j];
                }
            }
            
            if (op == 4) {
                log.debug("Testing query {} to {} = {}", start, stop, check);
                assertEquals(check, q);
            }
            
            if (checkAll) {
                int total = 0;
                for(int j = 0; j < size; ++j)
                {
                    total += array[j];
                    q = st.rangeSumQuery( j, j);
                    assertEquals(array[j], q);
                }
                
                q = st.rangeSumQuery( 0, size-1);
                assertEquals(total, q);
            }
            
        }
        
    }
    
    //@Test
    public void testFindFirstZero()
    {
        int[] ar = new int[] { 
                0, 1, 0, 0, 1,   //0 to 4
                1, 0, 0, 0, 1,   //5 to 9
                1, 1, 1, 0, 1,   //10 to 14
                0, 0 };          //15 16
        
        SegmentTreeSum seg = new SegmentTreeSum(ar.length, ar);
        
        assertEquals(2, seg.rangeSumQuery(0, 4));
        assertEquals(2, seg.rangeSumQuery(1, 4));
        assertEquals(1, seg.rangeSumQuery(2, 4));
        assertEquals(0, seg.rangeSumQuery(2, 3));
        
        assertEquals(0, seg.findFirstUnsetValue(0));
        assertEquals(2, seg.findFirstUnsetValue(1));
        assertEquals(2, seg.findFirstUnsetValue(2));
        
        assertEquals(13, seg.findFirstUnsetValue(9));
        
        seg.update(13,13, SegmentTreeSum.SET);
        assertEquals(15, seg.findFirstUnsetValue(10));
    }
    
    @Test
    public void findIndexWithSum()
    {
        int[] ar = new int[] { 
                0, 1, 0, 0, 1,   //0 to 4
                1, 0, 0, 0, 1,   //5 to 9
                1, 1, 1, 0, 1,   //10 to 14
                0, 0 };          //15 16
        
        SegmentTreeSum seg = new SegmentTreeSum(ar.length, ar);
        
        assertEquals(0, seg.findLowestIndexWithSum(0));
        assertEquals(1, seg.findLowestIndexWithSum(1));
        assertEquals(4, seg.findLowestIndexWithSum(2));
        assertEquals(5, seg.findLowestIndexWithSum(3));
        
        assertEquals(9, seg.findLowestIndexWithSum(4));
        assertEquals(10, seg.findLowestIndexWithSum(5));
        assertEquals(11, seg.findLowestIndexWithSum(6));
        assertEquals(12, seg.findLowestIndexWithSum(7));
        
        assertEquals(14, seg.findLowestIndexWithSum(8));
        
        seg.update(16,16, SegmentTreeSum.SET);
        
        assertEquals(16, seg.findLowestIndexWithSum(9));
        
        
        seg.update(1,3, SegmentTreeSum.SET);
        
        /*
        int[] ar = new int[] { 
                0, 1, 1, 1, 1,   //0 to 4
                1, 0, 0, 0, 1,   //5 to 9
                1, 1, 1, 0, 1,   //10 to 14
                0, 1 };          //15 16
        */
        
        assertEquals(0, seg.findLowestIndexWithSum(0));
        assertEquals(1, seg.findLowestIndexWithSum(1));
        assertEquals(2, seg.findLowestIndexWithSum(2));
        assertEquals(3, seg.findLowestIndexWithSum(3));
        
        assertEquals(4, seg.findLowestIndexWithSum(4));
        assertEquals(5, seg.findLowestIndexWithSum(5));
        assertEquals(9, seg.findLowestIndexWithSum(6));
        assertEquals(10, seg.findLowestIndexWithSum(7));
        
        assertEquals(11, seg.findLowestIndexWithSum(8));
        assertEquals(12, seg.findLowestIndexWithSum(9));
        assertEquals(14, seg.findLowestIndexWithSum(10));
        assertEquals(16, seg.findLowestIndexWithSum(11));
        
    }
}
