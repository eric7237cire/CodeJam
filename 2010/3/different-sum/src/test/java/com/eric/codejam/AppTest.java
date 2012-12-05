package com.eric.codejam;

import java.util.ArrayList;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.Main.SingleColumnCounts;
import com.google.common.primitives.Ints;

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
    public void testUberArray() {
        Main m = new Main();
        
        OldLargeSolution ols = new OldLargeSolution(m);
        
        
        
        long n = 268465676376382L;
        int base = 10;
        
        int check = Ints.checkedCast( ols.solve(n,ols.singleColCounts,base) % Main.MOD);
        
        int c = Ints.checkedCast(m.solve(n,base) % Main.MOD);
        
        assertEquals(check,c);
        
        for(n = 1; n < 9001; ++n) {
            log.debug("Checking {}", n);
            
            check = Ints.checkedCast( ols.solve(n,ols.singleColCounts,base)  % Main.MOD );
            
        c = Ints.checkedCast(m.solve(n,base)  % Main.MOD);
        
        assertEquals(check,c);
        
        }
        
        
    }
    
    public int getCount(int n, int base, int[] checks) {
        Main m = new Main();
        String s = Long.toString(n,base);
    boolean[][] fd = new boolean[s.length()][base];
    
    int[] counts = m.count(n,n, base,fd, new ArrayList<String>(), checks, n);
    
    int termCount = counts[1];
    int sumCount = counts[0];
    
    return sumCount;
    }

}
