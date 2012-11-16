package com.eric.codejam;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    @Test
    public void testTree() {
        Node.LETTER_MAX = 4;
        
        Node n = Node.createFirstNode();
        
        assertEquals(4, n.getCount());
        
        n.connectSingleNode(2, true);
        
        assertEquals(10, n.getCount());
        
        n.connectSingleNode(3, false);
        
        assertEquals(30, n.getCount());
        
    }
   

}
