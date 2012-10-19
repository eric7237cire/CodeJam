package com.eric.codejam;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    
    final static Logger log = LoggerFactory.getLogger(AppTest.class);

    /**
     * Create the test case
     * 
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    private String getOutput(String testCase) {
        Scanner sc = new Scanner(testCase);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream pos = new PrintStream(os);

        Main.handleCase(1, sc, pos);

        try {
            String output = new String(os.toString("UTF-8"));
            log.info(output);
            return output.trim();
        } catch (UnsupportedEncodingException ex) {
            return null;
        }
    }
    /**
     * Rigourous Test :-)
     */
    public void testBaseCase() throws UnsupportedEncodingException {
        String testCase = "2 2 1 \n" + 
                "..\n" + 
                "##";
        
        String output = getOutput(testCase);

        assertEquals("Case #1: Yes 1", output);
    }
    
    public void testBaseCaseNo() throws UnsupportedEncodingException {
        String testCase = "2 2 1 \n" + 
                ".#\n" + 
                "##";
        
        String output = getOutput(testCase);

        assertEquals("Case #1: No", output);
    }
    
    public void testDigRightTwo() throws UnsupportedEncodingException {
        String testCase = "3 3 1 " + 
                "...\n" + 
                "###\n" +
                ".##";
        
        String output = getOutput(testCase);

        assertEquals("Case #1: Yes 3", output);
    }
    
    public void testDigFallTwo() throws UnsupportedEncodingException {
        String testCase = "3 3 2 " + 
                "...\n" + 
                "###\n" +
                ".##";
        
        String output = getOutput(testCase);

        assertEquals("Case #1: Yes 1", output);
    }
    
    public void testMultistep() throws UnsupportedEncodingException {
        String testCase = 
                "5 8 3 " +
"........\n" +
"########\n" +
"...#.###\n" +
"####..##\n" +
"###.##..\n" ;
        
        String output = getOutput(testCase);

        assertEquals("Case #1: Yes 2", output);
    }
}
