package codejam.y2009;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Circle;
import codejam.utils.test.TesterBase;
import codejam.utils.utils.DoubleComparator;
import codejam.y2009.round_2.watering_plants.InputData;
import codejam.y2009.round_2.watering_plants.Main;

/**
 * Unit test for simple App.
 */
public class WateringPlantsTest extends TesterBase<InputData> {

	final static Logger log = LoggerFactory.getLogger(WateringPlantsTest.class);

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public WateringPlantsTest() {
		super(new Main());
	}

	
	@BeforeClass
    public static void getTestData() {
        initTestData(InterestingRangesTest.class.getResourceAsStream(
                    "watering-plants.xml"));
            
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
	
	
}
