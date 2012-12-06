package codejam.y2009;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.test.TesterBase;
import codejam.y2009.sokoban.InputData;
import codejam.y2009.sokoban.Main;

/**
 * Unit test for simple App.
 */
public class SokobanTest extends TesterBase {

	final static Logger log = LoggerFactory.getLogger(SokobanTest.class);

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public SokobanTest() {
		super();
	}

	protected String getOutput(String testCase) {
		Scanner sc = new Scanner(testCase);
		Main m = new Main();
		InputData input = m.readInput(sc,1);
		String output = m.handleCase(input);
		return output;
	}
	
	

	@BeforeClass
	public static void getTestData() {
		initTestData(SokobanTest.class.getResourceAsStream(
					"sokoban.xml"));
			
	}

	
	
}
