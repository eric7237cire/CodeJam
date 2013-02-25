package codejam.y2009;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.test.TesterBase;
import codejam.y2009.round_final.min_perimeter.InputData;
import codejam.y2009.round_final.min_perimeter.MinPerimeter;

/**
 * Unit test for simple App.
 */
public class MinPerimeterTest extends TesterBase<InputData> {

    final static Logger log = LoggerFactory.getLogger(MinPerimeterTest.class);

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public MinPerimeterTest() {
        super(new MinPerimeter());
    }

  

    @BeforeClass
    public static void getTestData() {
        initTestData(InterestingRangesTest.class.getResourceAsStream(
                    "min-perimeter.xml"));
            
    }

}
