package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

import com.eric.codejam.test.TesterBase;

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

    @Test(expected=IllegalArgumentException.class)
    public void testPolar() {
        Main.comparePolar(1.57, 2.5, 1.8, 2.6);
    }
    @Test(expected=IllegalArgumentException.class)
    public void testPolar2() {
        Main.comparePolar(Math.PI, -Math.PI / 2, Math.PI + .35, -Math.PI /4);
    }
    public void testPolar3() {
        int c = Main.comparePolar(3, -3, -Math.PI - .03, Math.PI - .02);
        assertEquals(-1, c);
        
        c = Main.comparePolar(3, -3, -Math.PI - .03, Math.PI - .04);
        assertEquals(1, c);
    }
}
