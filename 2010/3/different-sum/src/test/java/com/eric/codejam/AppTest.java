package com.eric.codejam;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest  {

    final static Logger log = LoggerFactory.getLogger(AppTest.class);

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public AppTest() {
        super();
    }

    
    @Test
    public void testSumTermCount() {
        Main m = new Main();
        
        //public int[] getSumTermCount(int maxColumn, int nextColumnDigit, int columnDigit, int maxDigit, final int base) {
        
        final int BASE = 10;
        
        int[] sumTermCount = m.getSumTermCount(1,0,1,9,BASE);
        assertEquals(1, sumTermCount[0]); //sum count
        assertEquals(1, sumTermCount[1]); //term count
        
        sumTermCount = m.getSumTermCount(1,0,2,2,BASE);
        assertEquals(1, sumTermCount[0]); //sum count
        assertEquals(1, sumTermCount[1]); //term count
        
        sumTermCount = m.getSumTermCount(1,0,3,9,BASE);
        assertEquals(2, sumTermCount[0]); //sum count
        assertEquals(3, sumTermCount[1]); //term count
        
        sumTermCount = m.getSumTermCount(1,0,4,9,BASE);
        assertEquals(2, sumTermCount[0]); //sum count
        assertEquals(3, sumTermCount[1]); //term count
        
        sumTermCount = m.getSumTermCount(1,0,5,9,BASE);
        assertEquals(3, sumTermCount[0]); //sum count
        assertEquals(5, sumTermCount[1]); //term count
        
        sumTermCount = m.getSumTermCount(1,0,6,9,BASE);
        assertEquals(4, sumTermCount[0]); //sum count
        assertEquals(8, sumTermCount[1]); //term count
        
        sumTermCount = m.getSumTermCount(1,0,7,7,BASE);
        assertEquals(5, sumTermCount[0]); //sum count
        assertEquals(10, sumTermCount[1]); //term count
        
        sumTermCount = m.getSumTermCount(1,0,8,9,BASE);
        assertEquals(6, sumTermCount[0]); //sum count
        assertEquals(13, sumTermCount[1]); //term count
        
        sumTermCount = m.getSumTermCount(1,4,5,9,BASE);
        assertEquals(1, sumTermCount[0]); //sum count
        assertEquals(9, sumTermCount[1]); //term count
    }
    

}
