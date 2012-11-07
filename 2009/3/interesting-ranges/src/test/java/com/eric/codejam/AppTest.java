package com.eric.codejam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Unit test for simple App.
 */
public class AppTest {

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
	
	private static String extractAns(String str) {
		Pattern p = Pattern.compile("Case #\\d: (.*)");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			return m.group(1);
		}
		
		return "Error";
	}

	private static Map<String, String> testData;

	@BeforeClass
	public static void getTestData() {
		testData = new HashMap<>();

		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(ClassLoader
					.getSystemResourceAsStream("test-data.xml"));
			doc.getDocumentElement().normalize();

			NodeList nl = doc.getElementsByTagName("test");
			for (int i = 0; i < nl.getLength(); ++i) {
				Node n = nl.item(i);
				String value = n.getTextContent();

				String name = n.getAttributes().getNamedItem("name")
						.getNodeValue();

				testData.put(name, value);
			}
		} catch (Exception ex) {

		}

	}

	@Test
	public void testIntervalSingle() {
	    Interval i1 = new Interval(10);
	    Interval t_i = BruteForce.createInterval(10,10);
	    
	    assertEquals(t_i, i1);
	    Interval i2 = new Interval(11);
	    Interval t_ii = BruteForce.createInterval(11,11);
	    
	    assertEquals(t_ii, i2);
	    
	    Interval c = Interval.combin(i1,i2);
	    Interval c_t = BruteForce.createInterval(10, 11);
	    
	    assertEquals(c_t, c);
	    
	    for (int i = 12; i <= 62; ++i) {
	        c = Interval.combin(c,new Interval(i));
            c_t = BruteForce.createInterval(10, i);
            
           // log.debug("{}", c);
            assertEquals("Fail " + i + " good \n" + c_t + "\n bad\n" + c,c_t, c);
	    }
        
        
	}
	
	@Test
    public void testSubtract() {
        
        
	    Interval big = BruteForce.createInterval(1, 100);
	    
        for (int i = 1; i <= 99; ++i) {
            
            Interval small = BruteForce.createInterval(1, i);
            
            Interval diff = Interval.subtract(small, big);
            Interval check = BruteForce.createInterval(i+1, 100);
            
           // log.debug("{}", c);
            assertEquals("Fail " + i + " small \n " + small + " big \n " + big + "\n good \n" + check
                    + "\n bad\n" + diff,check, diff);
        }
        
        
    }
	
	@Test
    public void testSubtract2() {
        
	    int TEST_UPPER = 200;
	    
	    Interval[] cacheInt = new Interval[TEST_UPPER+1];
        for(int i = 1; i <= TEST_UPPER; ++i) {
            Interval small = BruteForce.createInterval(1, i);
            cacheInt[i] = small;
        }
        
        for (int lb = 2; lb <= 99; ++lb) {
            for(int up = lb+1; up <= 200; ++up) {
                Interval small = cacheInt[lb-1];
                Interval big = cacheInt[up];
                
                Interval sub = Interval.subtract(small, big);
               // Interval check = BruteForce.createInterval(lb, up);
                int checkCount = BruteForce.countTotal(lb,up,true);
                
               // log.debug("{}", c);
                assertEquals("Fail " + lb + " small \n " +
               small + " big \n " + big + "\n"  
                        + "\n bad\n" + sub,checkCount, sub.totalEven);    
            }
            
            
        }
        
        
    }
	
	
	public void testFixedRanges() {
	     int size = 10;
	    Interval[] intsUnit = new Interval[size];
	    for(int i = 0; i < size; i++) {
	        intsUnit[i] = BruteForce.createInterval(i, i);
	        log.debug(intsUnit[i].toString());
	    }
	    
	    Interval[] ints = new Interval[size];
	    Interval[] intsNext = new Interval[size];
	    
	    ints = intsUnit;
	    
	    for(int i = 1; i < size; ++i) {
	        Interval ii = ints[i];
	        for(int j = 0; j< size; ++j) {
	            ii = Interval.combin(ii, intsUnit[j]);
	        }
	        intsNext[i] = ii;
	    }
	    
	    log.debug(BruteForce.createInterval(1, 19).toString());
	    
	    log.debug(BruteForce.createInterval(1, 49).toString());
	    
	    log.debug(BruteForce.createInterval(20, 49).toString());
	    
	    log.debug(BruteForce.createInterval(990, 998).toString());
	    
	    for(int i = 100; i <= 100; ++i) {
	        log.debug(BruteForce.createInterval(100, i).toString());
	    }
	    
	    BruteForce.countPalin(1, 1000);
	    assertEquals(intsUnit[0], intsUnit[3]);
	}

	@Test
	public void testS1() {
	    String testCase = testData.get("s1");

        int output = Integer.parseInt(extractAns(getOutput(testCase)));

        assertEquals(1, output);
	}

	   @Test
	    public void testS2() {
	        String testCase = testData.get("s2");

	        int output = Integer.parseInt(extractAns(getOutput(testCase)));

	        assertEquals(12, output);
	    }
	   
	    @Test
	    public void testS3() {
	        String testCase = testData.get("s3");

	        int output = Integer.parseInt(extractAns(getOutput(testCase)));

	        assertEquals(2466, output);
	    }
	
}
