package codejam.y2009;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.test.TesterBase;
import codejam.y2009.alphabetomials.InputData;
import codejam.y2009.alphabetomials.Main;

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


    @Test
    public void testCount() {
        Main m = new Main();
        InputData input = new InputData(1);
        input.polynomial = "a+e";
        int count = m.evalP("ejoe",input);
        assertEquals(2, count);

        input.polynomial = "aber+aab+c";
        count = m.evalP("abracadabra edgar",input);
        assertEquals(109, count);
    }

}
