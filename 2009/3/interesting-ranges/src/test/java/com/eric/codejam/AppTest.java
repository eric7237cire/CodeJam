package com.eric.codejam;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
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

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;

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
	public void testMainMethod() {
	    //Interval i1 = BruteForce.createInterval(1, 1);
	    Interval i2 = BruteForce.createInterval(1, 20);
	    Interval i5 = BruteForce.createInterval(21, 22);
	    Interval i3 = Interval.combin(i2, i5);
	    
	    Interval i4 = Main.calc(new BigInteger("22"));
	    for(int i = 1; i < 200; ++i) {
	        log.debug("i is {}", i);
	        BigInteger check = BruteForce.countTotal(1, i, true);
	        Interval intv = Main.calc(BigInteger.valueOf(i));
	        assertEquals("Failed at " + i, check, intv.totalEven);
	    }
	}
	
	@Test
	public void testNumBetween() {
	    assertEquals(new BigInteger("10999"), PalinSpace.calcNumBetween(7, 0));
	    assertEquals(new BigInteger("1099"), PalinSpace.calcNumBetween(7, 1));
	    assertEquals(new BigInteger("109"), PalinSpace.calcNumBetween(7, 2));
	    
	}

	@Test
	public void testPalinSpace() {
	    Object o2 = Main.palinSpace.segments.get(3);
	    
	    Interval i = Main.palinSpace.segments.get(3).get(new BigInteger("990"));
	    Interval check = Main.calcEvenPairRanges(new BigInteger("4005"), new BigInteger("4994"));
	    assertEquals(check, i);
	    
	    i = Main.palinSpace.segments.get(6).get(new BigInteger("800080"));
        check = Main.calcEvenPairRanges(new BigInteger("5000006"), new BigInteger("5800085"));
        assertEquals(check, i);
        
        i = Main.palinSpace.segments.get(7).get(new BigInteger("77000"));
        check = Main.calcEvenPairRanges(new BigInteger("60000007"), new BigInteger("60077006"));
        assertEquals(check, i);
        
        i = Main.palinSpace.segments.get(7).get(new BigInteger("200200"));
        check = Main.calcEvenPairRanges(new BigInteger("60000007"), new BigInteger("60200206"));
        assertEquals(check, i);
        
        i = Main.palinSpace.segments.get(7).get(new BigInteger("7000070"));
        check = Main.calcEvenPairRanges(new BigInteger("60000007"), new BigInteger("67000076"));
        assertEquals(check, i);
        
        i = Main.palinSpace.segments.get(7).get(new BigInteger("1000010"));
        check = Main.calcEvenPairRanges(new BigInteger("60000007"), new BigInteger("61000016"));
        assertEquals(check, i);
        
        i = Main.palinSpace.segments.get(7).get(new BigInteger("11000"));
        check = Main.calcEvenPairRanges(new BigInteger("60000007"), new BigInteger("60011006"));
        assertEquals(check, i);
        
        Object o = Main.palinSpace.segments.get(6);
        
	    assertEquals(check, i);
	}
	@Test
	public void testFullRangeSmall() {
	    int exp = 0;
        //1 - 2 until 1 - 10
        for(int num = 1; num <= 10; ++num) {
            Interval intv = Main.getFullRange(num, exp);
            Interval check = BruteForce.createInterval(1, num);
            assertEquals("Num " + num, check, intv);
        }
        
        exp = 1;
        //1 - 20 until 1 - 100
        for(int num = 1; num <= 10; ++num) {
            Interval intv = Main.getFullRange(num, exp);
            Interval check = BruteForce.createInterval(1, 10*num);
            assertEquals("\nGood\n" + check + "\nBad\n" + intv + " num " + num + " exp " + exp, check, intv);
        }
        
        
	}
	
	@Test 
    public void testRangeSliceSmall() {
	    int exp = 0;
	    //1 - 2 until 1 - 10
	    for(int num = 2; num <= 10; ++num) {
	        Interval intv = Main.getRangeSlice(num, exp);
	        Interval check = BruteForce.createInterval(1, num);
	        assertEquals("Num " + num, check, intv);
	    }
	    
	    exp = 1;
        //11 - 20 until 11 - 100
        for(int num = 2; num <= 10; ++num) {
            Interval intv = Main.getRangeSlice(num, exp);
            Interval check = BruteForce.createInterval(11, 10*num);
            assertEquals("\nGood\n" + check + "\nBad\n" + intv + " num " + num + " exp " + exp, check, intv);
        }
        
        
        
	}
	
	
	@Test 
	public void testCountRangeSmall() {
	    
	    BigInteger c = BruteForce.countTotal(10, 99,true);
	    
	    Interval beforeFirstPalin = new Interval(10);
	    
	    Interval palin = new Interval(1);
	    Interval empties = Interval.createEmpty(10);
	    
	    Interval total = beforeFirstPalin;
	    assertEquals(BruteForce.countTotal(10, 10,true), total.totalEven);
	    for(int i = 0; i < 8; ++i) {
	        total = Interval.combin(total, palin);
	        total = Interval.combin(total, empties);
	        
	        if (i==0) {
	            assertEquals(BruteForce.countTotal(10, 21,true), total.totalEven);
	        }
	    }
	    
	    total = Interval.combin(total, palin);
	    
	    assertEquals(c, total.totalEven);
	    
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
            Interval check = BruteForce.createInterval(i + 1, 100);

            // log.debug("{}", c);
            assertEquals("Fail " + i + " small \n " + small + " big \n " + big
                    + "\n good \n" + check + "\n bad\n" + diff, check, diff);
        }

    }

    @Test
    public void testSubtract2() {

        int TEST_UPPER = 20;

        Interval[] cacheInt = new Interval[TEST_UPPER + 1];
        for (int i = 1; i <= TEST_UPPER; ++i) {
            Interval small = BruteForce.createInterval(1, i);
            cacheInt[i] = small;
        }

        for (int lb = 2; lb <= TEST_UPPER / 2; ++lb) {
            for (int up = lb + 1; up <= TEST_UPPER; ++up) {
                Interval small = cacheInt[lb - 1];
                Interval big = cacheInt[up];

                Interval sub = Interval.subtract(small, big);
                // Interval check = BruteForce.createInterval(lb, up);
                BigInteger checkCount = BruteForce.countTotal(lb, up, true);

                // log.debug("{}", c);
                assertEquals("Fail " + lb + " small \n " + small + " big \n "
                        + big + "\n" + "\n bad\n" + sub, checkCount,
                        sub.totalEven);
            }

        }

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
