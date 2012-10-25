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
	public void testIntersectingPoint() {
	    Circle c1 = null;
	    Point p = null;
	    
	    c1 = new Circle(3, 3, 1);
        p = new Point(0, 0);
        
        Line l = new Line(p, c1.getCenter());
        
        Point[] pts1 = c1.getPointsIntersectingLineOriginatingAtP(p);
        
        log.debug("{} {}", pts1[0], pts1[1]);
        
        assertTrue( l.onLine(pts1[0]));
        assertTrue( l.onLine(pts1[1]));
        assertTrue( c1.onCircle(pts1[0]));
        assertTrue( c1.onCircle(pts1[0]));
        assertFalse(pts1[0].equals(pts1[1]));
        
        
        Point[] pts2 = c1.getPointsIntersectingLineOriginatingAtP_second(p);
        
        log.debug("{} {}", pts2[0], pts2[1]);
        
        assertTrue( l.onLine(pts2[0]));
        assertTrue( l.onLine(pts2[1]));
        assertTrue( c1.onCircle(pts2[0]));
        assertTrue( c1.onCircle(pts2[0]));
        assertFalse(pts1[0].equals(pts2[1]));
	    
	}
	
	@Test
    public void testIntersectingPoint2() {
        Circle c1 = null;
        Point p = null;
        
        c1 = new Circle(7, 2, 5);
        p = new Point(-3, 4);
        
        Line l = new Line(p, c1.getCenter());
        
        Point[] pts1 = c1.getPointsIntersectingLineOriginatingAtP(p);
        
        log.debug("{} {}", pts1[0], pts1[1]);
        
        Point[] pts2 = c1.getPointsIntersectingLineOriginatingAtP_second(p);
        
        log.debug("{} {}", pts2[0], pts2[1]);
        
        assertTrue( l.onLine(pts1[0]));
        assertTrue( l.onLine(pts1[1]));
        assertTrue( c1.onCircle(pts1[0]));
        assertTrue( c1.onCircle(pts1[0]));
        assertFalse(pts1[0].equals(pts1[1]));
        
        
        
        
        assertTrue( l.onLine(pts2[0]));
        assertTrue( l.onLine(pts2[1]));
        assertTrue( c1.onCircle(pts2[0]));
        assertTrue( c1.onCircle(pts2[0]));
        assertFalse(pts1[0].equals(pts2[1]));
        
    }
	
	@Test
    public void testIntersectingPointVertical() {
        Circle c1 = null;
        Point p = null;
        
        c1 = new Circle(-3, -3, 7);
        p = new Point(-3, -14);
        
        Line l = new Line(p, c1.getCenter());
        
        Point closePoint = new Point(-3, -10);
        Point farPoint = new Point(-3, 4);
        
        Point[] pts1 = c1.getPointsIntersectingLineOriginatingAtP(p);
        
        assertEquals(pts1[0], closePoint);
        assertEquals(pts1[1], farPoint);
        
        log.debug("{} {}", pts1[0], pts1[1]);
        
        assertTrue( l.onLine(pts1[0]));
        assertTrue( l.onLine(pts1[1]));
        assertTrue( c1.onCircle(pts1[0]));
        assertTrue( c1.onCircle(pts1[0]));
        assertFalse(pts1[0].equals(pts1[1]));
                
        Point[] pts2 = c1.getPointsIntersectingLineOriginatingAtP_second(p);
        
        log.debug("{} {}", pts2[0], pts2[1]);
        
        assertTrue( l.onLine(pts2[0]));
        assertTrue( l.onLine(pts2[1]));
        assertTrue( c1.onCircle(pts2[0]));
        assertTrue( c1.onCircle(pts2[0]));
        assertFalse(pts1[0].equals(pts2[1]));
        
        assertEquals(pts1[0], pts2[0]);
        assertEquals(pts1[1], pts2[1]);
        
    }
	
	@Test
    public void testIntersectingPointHorizontal() {
	    Circle c1 = null;
        Point p = null;
        
        c1 = new Circle(5, -3, 2);
        p = new Point(15, -3);
        
        Line l = new Line(p, c1.getCenter());
        
        Point closePoint = new Point(7, -3);
        Point farPoint = new Point(3, -3);
        
        Point[] pts1 = c1.getPointsIntersectingLineOriginatingAtP(p);
        
        assertEquals(pts1[0], closePoint);
        assertEquals(pts1[1], farPoint);
        
        log.debug("{} {}", pts1[0], pts1[1]);
        
        assertTrue( l.onLine(pts1[0]));
        assertTrue( l.onLine(pts1[1]));
        assertTrue( c1.onCircle(pts1[0]));
        assertTrue( c1.onCircle(pts1[0]));
        assertFalse(pts1[0].equals(pts1[1]));
                
        Point[] pts2 = c1.getPointsIntersectingLineOriginatingAtP_second(p);
        
        log.debug("{} {}", pts2[0], pts2[1]);
        
        assertTrue( l.onLine(pts2[0]));
        assertTrue( l.onLine(pts2[1]));
        assertTrue( c1.onCircle(pts2[0]));
        assertTrue( c1.onCircle(pts2[0]));
        assertFalse(pts1[0].equals(pts2[1]));
        
        assertEquals(pts1[0], pts2[0]);
        assertEquals(pts1[1], pts2[1]);
    }
	
	@Test
	public void testCircle3Points() {
		Circle a = new Circle(2, 3, 3);
		
		Circle b = new Circle(4.4142135623731, 1.5857864376269, 2);
		
		Circle c = new Circle(3, 6, 1);
		
		Circle ans = Circle.getCircleContaining(a, b, c);
		
		assertEquals(3, ans.getX(), DoubleComparator.TOLERANCE);
		assertEquals(3, ans.getY(), DoubleComparator.TOLERANCE);
		assertEquals(4, ans.getR(), DoubleComparator.TOLERANCE);
		
		 b = new Circle(5, 3, 2);
		//Circle b = new Circle(4.4142135623731, 1.5857864376269, 2);
		
		
		ans = Circle.getCircleContaining(a, b, c);
		
		Display d = new Display();
		d.addCircle(a);
		d.addCircle(b);
		d.addCircle(c);
		d.addCircle(ans);
		d.setBounds(0, 0, 400,  400);
		//d.setVisible(true);
		
		
		assertEquals(3, ans.getX(), DoubleComparator.TOLERANCE);
		assertEquals(3, ans.getY(), DoubleComparator.TOLERANCE);
		assertEquals(4, ans.getR(), DoubleComparator.TOLERANCE);
	}
	
	@Test
	public void testCircle2Points() {
		Circle a = new Circle(10, 7, 5);
		
		Circle b = new Circle(90, 7, 15);
		
		Circle ans = Circle.getCircleContaining(a, b);
		
		assertEquals(55, ans.getX(), DoubleComparator.TOLERANCE);
		assertEquals(7, ans.getY(), DoubleComparator.TOLERANCE);
		assertEquals(50, ans.getR(), DoubleComparator.TOLERANCE);
		
	}
	
	
	@Test
	public void testInvalidCircle3Points() {
		Circle a = new Circle(3, 5, 4);
		
		Circle b = new Circle(2, 1, 5);
		
		Circle c = new Circle(4, 3, 1);
		
		Circle ans = Circle.getCircleContaining(a, b, c);
		
		Display d = new Display();
		d.addCircle(a);
		d.addCircle(b);
		d.addCircle(c);
		d.addCircle(ans);
		d.setBounds(0, 0, 400,  400);
	//	d.setVisible(true);
		
		
		
		//assertEquals(3, ans.getX(), DoubleComparator.TOLERANCE);
		//assertEquals(3, ans.getY(), DoubleComparator.TOLERANCE);
		//assertEquals(4, ans.getR(), DoubleComparator.TOLERANCE);
	}
	
}
