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
public class TestParse
{

    
    private static Logger log = LoggerFactory.getLogger(TestCheckRaise.class);
   
    @Test
    public void test()
    {
    	
    }


    public void testParse1() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testParse1");
        
        assertEquals(0, results.listHandInfo.size());
        
        FlopTurnRiverState[] roundStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Eric"); 
    }
    
   //second hand in file  has no small blind
    @Test
    public void testParse2() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testParse2");
        
        assertEquals(2, results.listHandInfo.size());
        
        FlopTurnRiverState[] roundStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Eric"); 
    }
}