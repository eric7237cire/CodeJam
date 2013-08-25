package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestEvalNodes.class,
     TestRanges.class, 
    TestScore.class, TestPreproc.class, 
    TestPreflopStats.class,
    TestRoundStats.class
})
public class TestSuite {

}
