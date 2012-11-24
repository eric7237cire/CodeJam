package com.eric.codejam;

import org.junit.Test;

import com.eric.codejam.geometry.Rectangle;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(  )
    {
        super(  );
    }

    @Test
    public void testRectTouching()
    {
        Rectangle r1 = new Rectangle(5, 5, 7, 7);
        Rectangle r2 = new Rectangle(3, 3, 9, 9);
        
        assertTrue(r1.touches(r2));
        
        r1 = new Rectangle(2, 5, 2, 7);
        r2 = new Rectangle(3, 1, 3, 5);
        
        assertTrue(r1.touches(r2));
        
        r1 = new Rectangle(2, 6, 2, 7);
        r2 = new Rectangle(3, 1, 3, 5);
        
        assertFalse(r1.touches(r2));
        
        r1 = new Rectangle(2, 6, 2, 7);
        r2 = new Rectangle(2, 1, 2, 5);
        
        assertTrue(r1.touches(r2));
        
        r1 = new Rectangle(7, 14, 7, 17);
        r2 = new Rectangle(3, 1, 7, 14);
        
        assertTrue(r1.touches(r2));
    }
}
