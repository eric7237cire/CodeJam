package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import poker_simulator.TestEvalNodes;
import poker_simulator.TestFlopTexture;
import poker_simulator.TestRanges;
import poker_simulator.TestScore;

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
