package test;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestEvalNodes.class,
     TestRanges.class, 
    TestScore.class, TestPreproc.class})
public class TestSuite {

}
