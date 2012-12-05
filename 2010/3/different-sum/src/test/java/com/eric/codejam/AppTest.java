package com.eric.codejam;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.primitives.Ints;

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
    
   

}
