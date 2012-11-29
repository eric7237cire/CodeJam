package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.test.TesterBase;
import com.google.common.math.LongMath;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest extends TesterBase {

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

    @Override
    protected String getOutput(String testCaseData) throws IOException {

        Main m = new Main();

        InputData input = m.readInput(new BufferedReader(new StringReader(
                testCaseData)), 1);

        String output = m.handleCase(1, input);

        log.info(output);
        return output.trim();
    }

    @Test
    public void testEx() {
        Main.solve(52,72, 40);
        
        Main.solve(1,99, LongMath.pow(10, 10));
    }
    
    @Test
    public void testSolveIt() {
        Main m = new Main();
        m.solve_iter(100, new int[] {23, 51, 100});
        assertEquals(2, m.memo[46]);
    }
    
    @Test
    public void test43() {
        Main m = new Main();
        Main.solve(88925, 88926,25898618514778333L);
        
    }

    
}
