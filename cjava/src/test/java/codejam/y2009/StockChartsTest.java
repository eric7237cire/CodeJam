package codejam.y2009;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.test.TesterBase;
import codejam.y2009.stock_charts.InputData;
import codejam.y2009.stock_charts.Main;

/**
 * Unit test for simple App.
 */
public class StockChartsTest extends TesterBase<InputData> {

	final static Logger log = LoggerFactory.getLogger(StockChartsTest.class);

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public StockChartsTest() {
		super(new Main());
	}

	@BeforeClass
    public static void getTestData() {
        initTestData(InterestingRangesTest.class.getResourceAsStream(
                    "stock-charts.xml"));
            
    }
	

	
	
}
