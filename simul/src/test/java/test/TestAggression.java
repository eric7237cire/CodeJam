package test;

import static org.junit.Assert.assertEquals;
import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfoCollector;
import pkr.history.Parser;
import pkr.history.StatsSession;
import pkr.history.StatsSessionPlayer;

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
import pkr.history.stats.Aggression;
import pkr.history.stats.DonkContLimped;

@SuppressWarnings("unused")
@RunWith(JUnit4.class)
public class TestAggression
{

    public TestAggression() {
        // TODO Auto-generated constructor stub
    }

    @Test
    public void testAgg1() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testAgg1");
        
        assertEquals(1, results.listHandInfo.size());
        
        FlopTurnRiverState[] roundStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Adrian");
        
        Aggression agg = (Aggression) pStats.stats.get("agg");
        
        assertEquals(2, agg.betsOrRaises);
        assertEquals(0, agg.folds);
        assertEquals(1, agg.calls);
        
        
    }
}
