package codejam.y2009;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.test.TesterBase;
import codejam.y2009.round_3.alphabetomials.InputData;
import codejam.y2009.round_3.alphabetomials.Main;

/**
 * Unit test for simple App.
 */
public class AlphabetomialsTest extends TesterBase<InputData> {

    final static Logger log = LoggerFactory.getLogger(AlphabetomialsTest.class);

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public AlphabetomialsTest() {
        super(new Main());
    }

    @BeforeClass
    public static void getTestData() {
        initTestData(AlphabetomialsTest.class.getResourceAsStream(
                    "alphabetomials.xml"));
            
    }
    
    

}
