package com.eric.codejam;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;
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
    public void testBaseCase() {
        String testCase = "2 2 1 \n" + 
                "..\n" + 
                "##";
        
        String output = getOutput(testCase);

        assertEquals("Case #1: Yes 1", output);
    }
    
    public void testBaseCaseNo() {
        String testCase = "2 2 1 \n" + 
                ".#\n" + 
                "##";
        
        String output = getOutput(testCase);

        assertEquals("Case #1: No", output);
    }
    
    public void testDigRightTwo()  {
        String testCase = "3 3 1 " + 
                "...\n" + 
                "###\n" +
                ".##";
        
        String output = getOutput(testCase);

        assertEquals("Case #1: Yes 2", output);
    }
    
    public void testDigFallTwo()  {
        String testCase = "3 3 2 " + 
                "...\n" + 
                "###\n" +
                ".##";
        
        String output = getOutput(testCase);

        assertEquals("Case #1: Yes 1", output);
    }
    
    public void testMultistep() {
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
    
    public void testWalkRight() {
        String testCase = 
                "4 5 1\n" +
        ".....\n" +
        "##..#\n" +
        "###..\n" +
        "####.\n";

        String output = getOutput(testCase);

        assertEquals("Case #1: Yes 0", output);
    }
   
    
    public void testDigThenWalk1() {
        String testCase = 
                "4 5 1\n" +
        ".....\n" +
        "#####\n" +
        "..###\n" +
        ".#...\n";

        String output = getOutput(testCase);

        assertEquals("Case #1: Yes 2", output);
    }
    
    public void testCantWalk1() {
   	 String testCase = 
   			 "3 6 1\n" +
   	 "..####\n" +
   	 "##.#.#\n" +
   	 "###.##\n";
        String output = getOutput(testCase);

        assertEquals("Case #1: Yes 2", output);

   }
    
    public void testCantWalk2() {
   	 String testCase = 
   			 "3 5 2\n" +
   	 "...##\n" +
   	 "#.#.#\n" +
   	 "##.##\n";
        String output = getOutput(testCase);

        assertEquals("Case #1: No", output);

   }
    
    public void testSmall14() {
    	 String testCase = 
    			 "6 6 2\n" +
    	 "......\n" +
    	 "######\n" +
    	 "######\n" +
    	 "######\n" +
    	 "##.#.#\n" +
    	 "###.##\n";
         String output = getOutput(testCase);

         //assertEquals("Case #1: Yes 2", output);

    }
    
    public void testFindRange() {
        String testCase = 
                "4 5 1\n" +
        ".....\n" +
        "#####\n" + //1
        "..###\n" +
        ".#...\n";
        
        Scanner sc = new Scanner(testCase);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream pos = new PrintStream(os);

        Main m = Main.buildMain(sc);
        
        int[] range = m.findWalkableRange(1, //row
                2, //col
                1, 2); //dug
        
        assertTrue( ArrayUtils.isEquals(new int[] {2, 2}, range) );
        
        range = m.findWalkableRange(1, 2, 2, 4);
        
        assertTrue( ArrayUtils.isEquals(new int[] {2, 4}, range) );
        
        range = m.findWalkableRange(0, 3);
        
        assertTrue( ArrayUtils.isEquals(new int[] {0, 4}, range) );
        
    }
}
