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
public class TestCheckRaise
{

    
    private static Logger log = LoggerFactory.getLogger(TestCheckRaise.class);
   

    @Test
    public void testCheckRaise1() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testCheckRaise1");
        
        assertEquals(1, results.listHandInfo.size());
        
        FlopTurnRiverState[] roundStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Eric"); 
       
        DonkContLimped dcl = (DonkContLimped) pStats.stats.get("dcl1");
        
        int type = DonkContLimped.IS_AGGRES;
        assertEquals(1, dcl.actions[type][DonkContLimped.FOLD_RAISE]);
        assertEquals(2, dcl.actionPossible[type][DonkContLimped.FOLD_RAISE]);
        
        
        

        pStats = stats.playerSessionStats.get("Bena");
        
        dcl = (DonkContLimped) pStats.stats.get("dcl1");
        type = DonkContLimped.NOT_AGGRES;
        
        assertEquals(1, dcl.actions[type][DonkContLimped.CHECK_RAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.CHECK_RAISE]);
        
        assertEquals(1, dcl.actions[type][DonkContLimped.RAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.RAISE]);
        
        assertEquals(1, dcl.actions[type][DonkContLimped.RERAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.RERAISE]);
        
        assertEquals(0, dcl.actions[type][DonkContLimped.FOLD_RAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.FOLD_RAISE]);
        
        assertEquals(0, dcl.actions[type][DonkContLimped.FOLD]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.FOLD]);
        
        pStats = stats.playerSessionStats.get("Ahmed");
        dcl = (DonkContLimped) pStats.stats.get("dcl1");
        type = DonkContLimped.NOT_AGGRES;
        
        assertEquals(1, dcl.actions[type][DonkContLimped.CHECK_RAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.CHECK_RAISE]);
        
        assertEquals(0, dcl.actions[type][DonkContLimped.RAISE]);
        assertEquals(0, dcl.actionPossible[type][DonkContLimped.RAISE]);
        
        assertEquals(1, dcl.actions[type][DonkContLimped.RERAISE]);
        assertEquals(2, dcl.actionPossible[type][DonkContLimped.RERAISE]);
        
        assertEquals(0, dcl.actions[type][DonkContLimped.FOLD_RAISE]);
        assertEquals(2, dcl.actionPossible[type][DonkContLimped.FOLD_RAISE]);
        
    }
}