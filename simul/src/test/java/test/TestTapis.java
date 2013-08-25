package test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.Parser;
import pkr.history.StatsSession;
import pkr.history.StatsSessionPlayer;
import pkr.history.stats.DonkContLimped;

@SuppressWarnings("unused")
@RunWith(JUnit4.class)
public class TestTapis
{

    
    private static Logger log = LoggerFactory.getLogger(TestTapis.class);
   

    @Test
    public void testTapis1() throws Exception
    {
        List<FlopTurnRiverState[]> results = TestPreflopStats.getList("testTapis1");
        
        assertEquals(1, results.size());
        
        FlopTurnRiverState[] handStates = results.get(0);
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Dennis"); 
       
        DonkContLimped dcl = (DonkContLimped) pStats.stats.get("dcl1");
        String s = dcl.toString();
        
        pStats = stats.playerSessionStats.get("Roy"); 
        
        dcl = (DonkContLimped) pStats.stats.get("dcl1");
        
    }
}