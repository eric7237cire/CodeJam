package codejam.y2009;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.test.TesterBase;
import codejam.y2009.round_2.digging_problem.InputData;
import codejam.y2009.round_2.digging_problem.Main;

/**
 * Unit test for simple App.
 */
public class DiggingProblemTest extends TesterBase<InputData> {

	final static Logger log = LoggerFactory.getLogger(DiggingProblemTest.class);

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public DiggingProblemTest() {
		super(new Main());
	}

    
    
    @BeforeClass
    public static void getTestData() {
        initTestData(InterestingRangesTest.class.getResourceAsStream(
                    "digging-problem.xml"));
            
    }

	
	

	@Test
	public void testBaseCaseNo() {
		String testCase = "2 2 1 \n" + ".#\n" + "##";

		String output = getOutput(testCase);

		assertEquals("Case #1: No", output);
	}

	@Test
	public void testDigRightTwo() {
		String testCase = "3 3 1 " + "...\n" + "###\n" + ".##";

		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 2", output);
	}

	@Test
	public void testDigFallTwo() {
		String testCase = "3 3 2 " + "...\n" + "###\n" + ".##";

		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 1", output);
	}

	

	@Test
	public void testWalkRight() {
		String testCase = "4 5 1\n" + ".....\n" + "##..#\n" + "###..\n"
				+ "####.\n";

		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 0", output);
	}

	@Test
	public void testDigThenWalk1() {
		String testCase = "4 5 1\n" + ".....\n" + "#####\n" + "..###\n"
				+ ".#...\n";

		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 2", output);
	}

	@Test
	public void testCantWalk1() {
		String testCase = "3 6 1\n" + "..####\n" + "##.#.#\n" + "###.##\n";
		String output = getOutput(testCase);

		assertEquals("Case #1: Yes 2", output);

	}

	@Test
	public void testCantWalk2() {
		String testCase = "3 5 2\n" + "...##\n" + "#.#.#\n" + "##.##\n";
		String output = getOutput(testCase);

		assertEquals("Case #1: No", output);

	}

	@Test
	public void testFindRange() {
		String testCase = "4 5 1\n" + ".....\n" + "#####\n" + // 1
				"..###\n" + ".#...\n";

		Scanner sc = new Scanner(testCase);


		Main m = new Main();
		InputData input;
        
            input = m.readInput(sc,1);
        

		int[] range = m.findWalkableRange(1, // row
				2, // col
				1, 2,input); // dug

		assertTrue(ArrayUtils.isEquals(new int[] { 2, 2 }, range));

		range = m.findWalkableRange(1, 2, 2, 4,input);

		assertTrue(ArrayUtils.isEquals(new int[] { 2, 4 }, range));

		range = m.findWalkableRange(0, 3,input);

		assertTrue(ArrayUtils.isEquals(new int[] { 0, 4 }, range));

	}
}
