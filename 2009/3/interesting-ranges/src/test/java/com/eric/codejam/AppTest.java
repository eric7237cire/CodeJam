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
	    Interval i = new Interval(10);
	    Interval t_i = BruteForce.createInterval(10,10);
	    
	    assertEquals(t_i, i);
	    Interval i2 = new Interval(11);
	    Interval t_ii = BruteForce.createInterval(11,11);
	    
	    assertEquals(t_ii, i2);
	    
	    Interval c = Interval.combin(i,i2);
	    Interval c_t = BruteForce.createInterval(10, 11);
	    
	    assertEquals(c_t, c);
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
