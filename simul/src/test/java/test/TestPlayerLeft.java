package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfoCollector;
import pkr.history.Parser;
import pkr.history.StatsSession;
import pkr.history.StatsSessionPlayer;
import pkr.history.stats.DonkContLimped;

@SuppressWarnings("unused")
@RunWith(JUnit4.class)
public class TestPlayerLeft
{

    
    private static Logger log = LoggerFactory.getLogger(TestRoundStats.class);
   
    @Test
    public void testPlayerLeft() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testPlayerLeft");
        
        assertEquals(2, results.listHandInfo.size());
        
        Parser.outputHands(results);
    }
}