package test;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestEvalNodes.class, TestOutputTree.class, TestRanges.class, TestScore.class})
public class TestSuite {

}
