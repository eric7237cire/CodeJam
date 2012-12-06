package codejam.y2009;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.test.TesterBase;
import codejam.y2009.sokoban.InputData;
import codejam.y2009.sokoban.Main;

/**
 * Unit test for simple App.
 */
public class SokobanTest extends TesterBase<InputData> {

	final static Logger log = LoggerFactory.getLogger(SokobanTest.class);

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public SokobanTest() {
	    super(new Main());
	}

	@BeforeClass
	public static void getTestData() {
		initTestData(SokobanTest.class.getResourceAsStream(
					"sokoban.xml"));
			
	}

	
	
}
