package codejam.y2008;

import org.junit.BeforeClass;

import codejam.utils.test.TesterBase;
import codejam.y2008.round_amer.mixing.InputData;
import codejam.y2008.round_amer.mixing.Main;

public class MixingBowlTest extends TesterBase<InputData> {
    public MixingBowlTest() {
        super(new Main());
    }

    @BeforeClass
    public static void getTestData() {
        initTestData(MixingBowlTest.class.getResourceAsStream(
                    "mixing_bowls.xml"));
            
    }
}
