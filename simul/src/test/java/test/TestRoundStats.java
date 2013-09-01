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
public class TestRoundStats
{

    
    private static Logger log = LoggerFactory.getLogger(TestRoundStats.class);
   

    @Test
    public void testDonkContLimped1() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testStats1");
        
        assertEquals(1, results.listHandInfo.size());
        
        FlopTurnRiverState[] handStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Manfred"); 
       
        DonkContLimped dcl = (DonkContLimped) pStats.stats.get("dcl1");
        String s = dcl.toString();
        
        assertEquals(1, dcl.actions[DonkContLimped.LIMPED][DonkContLimped.CALL]);
        assertEquals(1, dcl.actionPossible[DonkContLimped.LIMPED][DonkContLimped.CALL]);
        
        assertEquals(0, dcl.actions[DonkContLimped.LIMPED][DonkContLimped.RAISE]);
        assertEquals(1, dcl.actionPossible[DonkContLimped.LIMPED][DonkContLimped.RAISE]);
        
        
        dcl = (DonkContLimped) pStats.stats.get("dcl2");
        
        assertEquals(1, dcl.actions[DonkContLimped.NOT_AGGRES][DonkContLimped.BET]);
        assertEquals(1, dcl.actionPossible[DonkContLimped.NOT_AGGRES][DonkContLimped.BET]);
        
        assertEquals(0, dcl.actions[DonkContLimped.NOT_AGGRES][DonkContLimped.RAISE]);
        assertEquals(0, dcl.actionPossible[DonkContLimped.NOT_AGGRES][DonkContLimped.RAISE]);
        
        
        s = dcl.toString();
        
    }
    
    

    @Test
    public void testFoldToZeroBet() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testFoldToZeroBet");
        
        assertEquals(2, results.listHandInfo.size());
        
        FlopTurnRiverState[] handStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Dhimiter");
        
        DonkContLimped dcl = (DonkContLimped) pStats.stats.get("dcl1");
        int type = DonkContLimped.LIMPED;
        

        assertEquals(0, dcl.actions[type][DonkContLimped.FOLD] );
        assertEquals(0, dcl.actionPossible[type][DonkContLimped.FOLD] );
          
    }
    
    @Test
    public void testRoundStats() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testRoundStats");
        
        assertEquals(6, results.listHandInfo.size());
                                 
        StatsSession stats = Parser.computeStats(results);
        
        //Eric  -   1        -  2 
        //Morris  - 1     4  -    3
        //*       -   2       - 
        //Billy   - 1 2 3   5 - 
        //Anto    -     3     -    4
        
        StatsSessionPlayer ericStats  =
                stats.playerSessionStats.get("Eric");
        
        
        //assertEquals(1+0+1+1+1+0, ericStats.vpipNumerator );
        //assertEquals(1+0+1+1+1+1, ericStats.vpipDenom );
        assertEquals("Vpip : 80% (4/5)", ericStats.getStatValue("vpip"));
        assertEquals(6, ericStats.totalHands );
        
                
        DonkContLimped dcl = (DonkContLimped) ericStats.stats.get("dcl1");
        int type = DonkContLimped.LIMPED;
        
        assertEquals(1+0+0+0+0+0, dcl.actions[type][DonkContLimped.CALL]);
        
        
        assertEquals(0+0+0+0+0+0, dcl.actions[type][DonkContLimped.FOLD] );
        
        assertEquals(1, dcl.actions[type][DonkContLimped.BET] );
        assertEquals(2, dcl.actionPossible[type][DonkContLimped.BET] );
        assertEquals(2, dcl.count[type] );
        
        type  = DonkContLimped.IS_AGGRES;
        
        assertEquals(0, dcl.actions[type][DonkContLimped.FOLD_RAISE] );
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.FOLD_RAISE] );
        
        dcl = (DonkContLimped) ericStats.stats.get("dcl2");
        type = DonkContLimped.NOT_AGGRES;
        
        assertEquals(1, dcl.actions[type][DonkContLimped.RERAISE] );
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.RERAISE] );
        
        
        
        
        
    }
    
    @Test
    public void testReraise1() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testReraise1");
        
        assertEquals(2, results.listHandInfo.size());
                                 
        StatsSession stats = Parser.computeStats(results);
        
        //Eric  -   1        -  2 
        //Morris  - 1     4  -    3
        //*       -   2       - 
        //Billy   - 1 2 3   5 - 
        //Anto    -     3     -    4
        
        StatsSessionPlayer pStats  =
                stats.playerSessionStats.get("Eric");
        
        DonkContLimped dcl = (DonkContLimped) pStats.stats.get("dcl1");
        int type = DonkContLimped.NOT_AGGRES;
        assertEquals(1, dcl.count[type]);
        assertEquals(1, dcl.actions[type][DonkContLimped.BET]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.BET]);
        
        assertEquals(0, dcl.actions[type][DonkContLimped.FOLD_RAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.FOLD_RAISE]);
        
        type = DonkContLimped.LIMPED;
        
        assertEquals(1, dcl.actions[type][DonkContLimped.FOLD_RAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.FOLD_RAISE]);
        
        assertEquals(1, dcl.actions[type][DonkContLimped.RAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.RAISE]);
        
        dcl = (DonkContLimped) pStats.stats.get("dcl2");
        type = DonkContLimped.NOT_AGGRES;
        assertEquals(1, dcl.count[type]);
        
        assertEquals(1, dcl.actions[type][DonkContLimped.CALL]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.CALL]);

        assertEquals(1, dcl.actions[type][DonkContLimped.ALL_IN]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.ALL_IN]);
        
        
        dcl = (DonkContLimped) pStats.stats.get("dcl3");
        
        assertEquals(0, dcl.count[DonkContLimped.NOT_AGGRES]);
        assertEquals(0, dcl.count[DonkContLimped.IS_AGGRES]);
        assertEquals(0, dcl.count[DonkContLimped.LIMPED]);
        
        pStats  =
                stats.playerSessionStats.get("Ivana");
        
        dcl = (DonkContLimped) pStats.stats.get("dcl1");
        
        assertEquals(1, dcl.count[DonkContLimped.IS_AGGRES]);
        
        assertEquals(1, dcl.actions[DonkContLimped.IS_AGGRES][DonkContLimped.CHECK_RAISE]);
        assertEquals(1, dcl.actionPossible[DonkContLimped.IS_AGGRES][DonkContLimped.CHECK_RAISE]);

        assertEquals(1, dcl.actions[DonkContLimped.IS_AGGRES][DonkContLimped.RAISE]);
        assertEquals(1, dcl.actionPossible[DonkContLimped.IS_AGGRES][DonkContLimped.RAISE]);

        
        log.debug(dcl.toString());
        
        dcl = (DonkContLimped) pStats.stats.get("dcl2");
        
        assertEquals(1, dcl.count[DonkContLimped.IS_AGGRES]);
        
        assertEquals(0, dcl.actions[DonkContLimped.IS_AGGRES][DonkContLimped.CHECK_RAISE]);
        assertEquals(0, dcl.actionPossible[DonkContLimped.IS_AGGRES][DonkContLimped.CHECK_RAISE]);

        assertEquals(0, dcl.actions[DonkContLimped.IS_AGGRES][DonkContLimped.RAISE]);
        assertEquals(0, dcl.actionPossible[DonkContLimped.IS_AGGRES][DonkContLimped.RAISE]);

        assertEquals(1, dcl.actions[DonkContLimped.IS_AGGRES][DonkContLimped.BET]);
        assertEquals(1, dcl.actionPossible[DonkContLimped.IS_AGGRES][DonkContLimped.BET]);

        
        dcl = (DonkContLimped) pStats.stats.get("dcl3");
        
        assertEquals(1, dcl.count[DonkContLimped.IS_AGGRES]);
        
        assertEquals(0, dcl.actions[DonkContLimped.IS_AGGRES][DonkContLimped.CHECK_RAISE]);
        assertEquals(0, dcl.actionPossible[DonkContLimped.IS_AGGRES][DonkContLimped.CHECK_RAISE]);

        assertEquals(1, dcl.actions[DonkContLimped.IS_AGGRES][DonkContLimped.RAISE]);
        assertEquals(1, dcl.actionPossible[DonkContLimped.IS_AGGRES][DonkContLimped.RAISE]);

        assertEquals(0, dcl.actions[DonkContLimped.IS_AGGRES][DonkContLimped.BET]);
        assertEquals(0, dcl.actionPossible[DonkContLimped.IS_AGGRES][DonkContLimped.BET]);

        assertEquals(0, dcl.actions[DonkContLimped.IS_AGGRES][DonkContLimped.RERAISE]);
        assertEquals(0, dcl.actionPossible[DonkContLimped.IS_AGGRES][DonkContLimped.RERAISE]);

        
        pStats  =
                stats.playerSessionStats.get("Manfred");
        
        dcl = (DonkContLimped) pStats.stats.get("dcl1");
        
        type = DonkContLimped.NOT_AGGRES;
        assertEquals(1, dcl.count[type]);
        
        assertEquals(0, dcl.actions[DonkContLimped.NOT_AGGRES][DonkContLimped.BET]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.BET]);
        
        assertEquals(0, dcl.actions[DonkContLimped.NOT_AGGRES][DonkContLimped.CHECK_RAISE]);
        assertEquals(1, dcl.actionPossible[DonkContLimped.NOT_AGGRES][DonkContLimped.CHECK_RAISE]);
        
        assertEquals(0, dcl.actions[DonkContLimped.NOT_AGGRES][DonkContLimped.FOLD]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.FOLD]);

        assertEquals(1, dcl.actions[type][DonkContLimped.CALL]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.CALL]);
        
        assertEquals(0, dcl.actions[type][DonkContLimped.FOLD_RAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.FOLD_RAISE]);
        
        dcl = (DonkContLimped) pStats.stats.get("dcl2");
        
        type = DonkContLimped.NOT_AGGRES;
        assertEquals(1, dcl.count[type]);
        
        assertEquals(0, dcl.actions[DonkContLimped.NOT_AGGRES][DonkContLimped.BET]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.BET]);
        
        
        assertEquals(1, dcl.actions[type][DonkContLimped.CALL]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.CALL]);
        
        
        dcl = (DonkContLimped) pStats.stats.get("dcl3");
        
        type = DonkContLimped.NOT_AGGRES;
        assertEquals(1, dcl.count[type]);
        
        assertEquals(1, dcl.actions[DonkContLimped.NOT_AGGRES][DonkContLimped.BET]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.BET]);
        
        
        assertEquals(0, dcl.actions[type][DonkContLimped.CALL]);
        assertEquals(0, dcl.actionPossible[type][DonkContLimped.CALL]);
        
        assertEquals(0, dcl.actions[type][DonkContLimped.FOLD_RAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.FOLD_RAISE]);
        
        assertEquals(1, dcl.actions[type][DonkContLimped.RERAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.RERAISE]);
        
        assertEquals(1, dcl.actions[type][DonkContLimped.ALL_IN]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.ALL_IN]);
        
        
        pStats  =
                stats.playerSessionStats.get("Vhia");
        
        dcl = (DonkContLimped) pStats.stats.get("dcl1");
        type = DonkContLimped.LIMPED;
        
        assertEquals(1, dcl.actions[type][DonkContLimped.BET]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.BET]);
        
        assertEquals(0, dcl.actions[type][DonkContLimped.FOLD_RAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.FOLD_RAISE]);
        
        assertEquals(1, dcl.actions[type][DonkContLimped.RERAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.RERAISE]);
        
    }
    
    @Test
    public void testReraise2() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testReraise2");
        
        assertEquals(1, results.listHandInfo.size());
                                 
        StatsSession stats = Parser.computeStats(results);
        
        //Eric  -   1        -  2 
        //Morris  - 1     4  -    3
        //*       -   2       - 
        //Billy   - 1 2 3   5 - 
        //Anto    -     3     -    4
        
        StatsSessionPlayer pStats  =
                stats.playerSessionStats.get("永佳");
        
        DonkContLimped dcl = (DonkContLimped) pStats.stats.get("dcl1");
        int type = DonkContLimped.NOT_AGGRES;
        
        assertEquals(1, dcl.actions[type][DonkContLimped.RAISE]);
        assertEquals(1, dcl.actionPossible[type][DonkContLimped.RAISE]);
    }
    
    @Test
    public void testNonAgg1() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testNonAgg1");
        
        assertEquals(2, results.listHandInfo.size());
        
        FlopTurnRiverState[] handStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Nasrullah"); 
        
        DonkContLimped dcl = (DonkContLimped) pStats.stats.get("dcl1");
        String s = dcl.toString();
        
        assertEquals(2, dcl.count[DonkContLimped.NOT_AGGRES]);
        
        assertEquals(0, dcl.actions[DonkContLimped.NOT_AGGRES][DonkContLimped.CALL]);
        assertEquals(1, dcl.actionPossible[DonkContLimped.NOT_AGGRES][DonkContLimped.CALL]);
        
        assertEquals(1, dcl.actions[DonkContLimped.NOT_AGGRES][DonkContLimped.FOLD_RAISE]);
        assertEquals(1, dcl.actionPossible[DonkContLimped.NOT_AGGRES][DonkContLimped.FOLD_RAISE]);
        
        
        dcl = (DonkContLimped) pStats.stats.get("dcl2");
        
        
        
        s = dcl.toString();
        
    }
    
    @Test
    public void testRaise1() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testRaise1");
        
        assertEquals(6, results.listHandInfo.size());
        
        FlopTurnRiverState[] handStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Shelly"); 
        
        
       
        
        int type = DonkContLimped.LIMPED;
        DonkContLimped dcl = (DonkContLimped) pStats.stats.get("dcl2");
        
        assertEquals(5, dcl.count[type]);
        
        assertEquals(5, dcl.actions[type][DonkContLimped.RAISE]);
        assertEquals(5, dcl.actionPossible[type][DonkContLimped.RAISE]);
        
        String s = dcl.toString();
        //dcl = (DonkContLimped) pStats.stats.get("dcl2");
        
        

    }
    
    
}
