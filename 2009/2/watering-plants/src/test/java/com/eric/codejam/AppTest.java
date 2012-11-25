package com.eric.codejam;

import static org.junit.Assert.assertEquals;
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

import com.eric.codejam.geometry.Circle;
import com.eric.codejam.utils.DoubleComparator;

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

	
	
	
	
	
	/*
	 * Case #1: 
Case #2: 8.000000
Case #3: 26.000000
Case #4: 
Case #5: 51
	 */
	
	@Test
	public void testS1() {
		String testCase = testData.get("s1");

		double output = Double.parseDouble(extractAns(getOutput(testCase)));
		
		assertEquals(7, output, DoubleComparator.TOLERANCE);
	}
	
	@Test 
	public void testS1Plus() {
		
		
		Circle a = new Circle(20, 10, 2);
		
		Circle b = new Circle(20, 20, 2);
		
		Circle c = new Circle(40, 10, 3);
		
		//120, 115,26
		
		Circle ans = Circle.getCircleContaining(a, b, c);
		
		assertTrue(ans.contains(a));
		assertTrue(ans.contains(b));
		assertTrue(ans.contains(c));
		
		
		a = new Circle(20, 10, 3);
		
		b = new Circle(30, 10, 3);
		
		c = new Circle(40, 10, 3);
		
		//120, 115,26
		
		ans = Circle.getCircleContaining(a, b, c);
		
		assertTrue(ans.contains(a));
		assertTrue(ans.contains(b));
		assertTrue(ans.contains(c));
		/*ans = new Circle(120, 115, 26);
		
		
		
		Display d = new Display();
		d.addCircle(a);
		d.addCircle(b);
		d.addCircle(c);
		d.addCircle(ans);
		d.setBounds(0, 0, 400,  400);
		d.setVisible(true);
		
		assertTrue(true);
		*/
		/*assertEquals(120, ans.getX(), DoubleComparator.TOLERANCE);
		assertEquals(115, ans.getY(), DoubleComparator.TOLERANCE);
		assertEquals(26, ans.getR(), DoubleComparator.TOLERANCE);
		*/
	}
	
	@Test
	public void testS2() {
		String testCase = testData.get("s2");

		double output = Double.parseDouble(extractAns(getOutput(testCase)));
		
		assertEquals(8, output, DoubleComparator.TOLERANCE);
	}
	
	@Test
	public void testS3() {
		String testCase = testData.get("s3");

		double output = Double.parseDouble(extractAns(getOutput(testCase)));
		
		assertEquals(26, output, DoubleComparator.TOLERANCE);
	}
	
	@Test
	public void tests3Plus() {
		Circle a = new Circle(100, 100, 1);
		
		Circle b = new Circle(140, 100, 1);
		
		Circle c = new Circle(100, 130, 1);
		
		
		//120, 115,26
		
		Circle ans = Circle.getCircleContaining(a, b, c);
		/*ans = new Circle(120, 115, 26);
		
		Display d = new Display();
		d.addCircle(a);
		d.addCircle(b);
		d.addCircle(c);
		d.addCircle(ans);
		d.setBounds(0, 0, 400,  400);
		d.setVisible(true);
		
		assertTrue(true);
		*/
		assertEquals(120, ans.getX(), DoubleComparator.TOLERANCE);
		assertEquals(115, ans.getY(), DoubleComparator.TOLERANCE);
		assertEquals(26, ans.getR(), DoubleComparator.TOLERANCE);
	}
	
	@Test
	public void testS4() {
		String testCase = testData.get("s4");

		double output = Double.parseDouble(extractAns(getOutput(testCase)));
		
		assertEquals(8.071067, output, DoubleComparator.TOLERANCE);
	}
	
	@Test
	public void testS5() {
		String testCase = testData.get("s5");

		double output = Double.parseDouble(extractAns(getOutput(testCase)));
		
		assertEquals(51, output, DoubleComparator.TOLERANCE);
	}
}
