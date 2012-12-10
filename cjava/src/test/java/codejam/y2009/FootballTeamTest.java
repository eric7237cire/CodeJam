package codejam.y2009;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.test.TesterBase;
import codejam.y2009.round_3.football_team.InputData;
import codejam.y2009.round_3.football_team.Main;

/**
 * Unit test for simple App.
 */
public class FootballTeamTest extends TesterBase<InputData> {

    final static Logger log = LoggerFactory.getLogger(FootballTeamTest.class);

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public FootballTeamTest() {
        super(new Main());
    }

    @BeforeClass
    public static void getTestData() {
        initTestData(FootballTeamTest.class.getResourceAsStream(
                    "football-team.xml"));
            
    }

}
