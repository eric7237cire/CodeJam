package com.eric.codejam;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

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
	public void testSample1() {
		String testCase = testData.get("sample1");

		String output = getOutput(testCase);

		//assertEquals("Case #1: 2", output);
	}
	
	private final static double DOUBLE_THRESHOLD = 0.000001d;
	
	@Test
	public void testSlopeYInt() {
	    Point p = new Point(2, 7);
	    Point p2 = new Point(4, 11);
	    Point p3 = new Point(-2, -1);
	    double[] mb = p.getSlopeAndYIntercept(p2);
	    
	    assertEquals(2d, mb[0], DOUBLE_THRESHOLD);
	    assertEquals(3d, mb[1], DOUBLE_THRESHOLD);
	    
	    mb = p2.getSlopeAndYIntercept(p);
        
        assertEquals(2d, mb[0], DOUBLE_THRESHOLD);
        assertEquals(3d, mb[1], DOUBLE_THRESHOLD);
        
        mb = p3.getSlopeAndYIntercept(p);
        
        assertEquals(2d, mb[0], DOUBLE_THRESHOLD);
        assertEquals(3d, mb[1], DOUBLE_THRESHOLD);
	}

	@Test
	public void testFurthestPoint() {
	    Circle c1 = null;
	    Point p = null;
	    
	    c1 = new Circle(3, 3, 1);
        p = new Point(0, 0);
        
        Line l = new Line(p, c1.getCenter());
        
        Point[] pts1 = c1.getPointsIntersectingLineOriginatingAtP(p);
        
        assertTrue( l.onLine(pts1[0]));
        assertTrue( l.onLine(pts1[1]));
        assertTrue( c1.onCircle(pts1[0]));
        assertTrue( c1.onCircle(pts1[0]));
        assertFalse(pts1[0].equals(pts1[1]));
        
        Point[] pts2 = c1.getPointsIntersectingLineOriginatingAtP_second(p);
        
        assertTrue( l.onLine(pts2[0]));
        assertTrue( l.onLine(pts2[1]));
        assertTrue( c1.onCircle(pts2[0]));
        assertTrue( c1.onCircle(pts2[0]));
        assertFalse(pts1[0].equals(pts2[1]));
	    
	    //Vertical / Horizontal line
		 c1 = new Circle(4, 4, 2);
		p = new Point(4, 1);
		
		//Point f = c1.getFurthestPointFrom(p);
		//Inside circle
		//assertEquals(new Point(4, 6), f);
	}
	
	
	
}
