package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestCheckRaise.class,
    TestEvalNodes.class,
      
    TestFlopTexture.class,
    TestPreflopStats.class,
    TestPreproc.class,
    TestRanges.class,
    TestRoundStats.class,
    TestScore.class,
    TestTapis.class
})
public class TestSuite {

}
