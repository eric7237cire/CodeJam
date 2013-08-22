package test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.Parser;
import pkr.history.Statistics;
import pkr.history.StatsSession;
import pkr.history.StatsSessionPlayer;
import pkr.history.StatsSessionPlayer.RoundStats;
import pkr.history.stats.DonkContLimped;
import static org.junit.Assert.*;

@SuppressWarnings("unused")
@RunWith(JUnit4.class)
public class TestRoundStats
{

    
    private static Logger log = LoggerFactory.getLogger(TestRoundStats.class);
   

    @Test
    public void testDonkContLimped1() throws Exception
    {
        List<FlopTurnRiverState[]> results = TestPreflopStats.getList("testStats1");
        
        assertEquals(1, results.size());
        
        FlopTurnRiverState[] handStates = results.get(0);
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Manfred"); 
        assertEquals(1, pStats.roundStats[0].calls);
        assertEquals(1, pStats.roundStats[1].betAllIn);
        assertEquals(0, pStats.roundStats[1].raiseCallAllIn);
        
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
    public void testStats2() throws Exception
    {
        List<FlopTurnRiverState[]> results = TestPreflopStats.getList("testStats2");
        
        assertEquals(1, results.size());
        
        FlopTurnRiverState[] handStates = results.get(0);
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Gilles");
        RoundStats rs = pStats.roundStats[0];
        
        //Unopened stats
        assertEquals(0, rs.bets);
        assertEquals(0, rs.checksUnopened);
        assertEquals(0, rs.betAllIn);
        assertEquals(0, rs.callReraise);
        assertEquals(0, rs.betFold);
        
        //Opened stats
        assertEquals(1, rs.calls);        
        assertEquals(0, rs.checkRaises);
        assertEquals(1, rs.checksOpened);
        assertEquals(0, rs.folded);
        assertEquals(0, rs.raiseCallAllIn);
        assertEquals(0, rs.reRaiseOpened);
        
        //Counters
        assertEquals(1, rs.openedBySomeoneElse);
        assertEquals(0, rs.checkedThrough);
        assertEquals(1, rs.seen);
        assertEquals(0, rs.unopened);
        
        rs = pStats.roundStats[1];
        
      //Unopened stats
        assertEquals(0, rs.bets);
        assertEquals(0, rs.checksUnopened);
        assertEquals(0, rs.betAllIn);
        assertEquals(0, rs.callReraise);
        assertEquals(0, rs.betFold);
        
        //Opened stats
        assertEquals(0, rs.calls);        
        assertEquals(0, rs.checkRaises);
        assertEquals(1, rs.checksOpened);
        assertEquals(1, rs.folded);
        assertEquals(0, rs.raiseCallAllIn);
        assertEquals(0, rs.reRaiseOpened);
        
        //Counters
        assertEquals(1, rs.openedBySomeoneElse);
        assertEquals(0, rs.checkedThrough);
        assertEquals(1, rs.seen);
        assertEquals(0, rs.unopened);
        
        rs = pStats.roundStats[2];
      //Unopened stats
        assertEquals(0, rs.bets);
        assertEquals(0, rs.checksUnopened);
        assertEquals(0, rs.betAllIn);
        assertEquals(0, rs.callReraise);
        assertEquals(0, rs.betFold);
        
        //Opened stats
        assertEquals(0, rs.calls);        
        assertEquals(0, rs.checkRaises);
        assertEquals(0, rs.checksOpened);
        assertEquals(0, rs.folded);
        assertEquals(0, rs.raiseCallAllIn);
        assertEquals(0, rs.reRaiseOpened);
        
        //Counters
        assertEquals(0, rs.openedBySomeoneElse);
        assertEquals(0, rs.checkedThrough);
        assertEquals(0, rs.seen);
        assertEquals(0, rs.unopened);
        
        ////////////////////////////////
        
        pStats = stats.playerSessionStats.get("Sakis");
        rs = pStats.roundStats[0];
        
        //Unopened stats
        assertEquals(0, rs.bets);
        assertEquals(0, rs.checksUnopened);
        assertEquals(0, rs.betAllIn);
        assertEquals(0, rs.callReraise);
        assertEquals(0, rs.betFold);
        
        //Opened stats
        assertEquals(1, rs.calls);        
        assertEquals(0, rs.checkRaises);
        assertEquals(1, rs.checksOpened);
        assertEquals(0, rs.folded);
        assertEquals(0, rs.raiseCallAllIn);
        assertEquals(0, rs.reRaiseOpened);
        
        //Counters
        assertEquals(1, rs.openedBySomeoneElse);
        assertEquals(0, rs.checkedThrough);
        assertEquals(1, rs.seen);
        assertEquals(0, rs.unopened);
        
        rs = pStats.roundStats[1];
        
      //Unopened stats
        assertEquals(0, rs.bets);
        assertEquals(0, rs.checksUnopened);
        assertEquals(0, rs.betAllIn);
        assertEquals(0, rs.callReraise);
        assertEquals(0, rs.betFold);
        
        //Opened stats
        assertEquals(0, rs.calls);        
        assertEquals(1, rs.checkRaises);
        assertEquals(1, rs.checksOpened);
        assertEquals(0, rs.folded);
        assertEquals(1, rs.raiseCallAllIn);
        assertEquals(1, rs.reRaiseOpened);
        
        //Counters
        assertEquals(1, rs.openedBySomeoneElse);
        assertEquals(0, rs.checkedThrough);
        assertEquals(1, rs.seen);
        assertEquals(0, rs.unopened);
        
        rs = pStats.roundStats[2];
      //Unopened stats
        assertEquals(0, rs.bets);
        assertEquals(0, rs.checksUnopened);
        assertEquals(0, rs.betAllIn);
        assertEquals(0, rs.callReraise);
        assertEquals(0, rs.betFold);
        
        //Opened stats
        assertEquals(0, rs.calls);        
        assertEquals(0, rs.checkRaises);
        assertEquals(0, rs.checksOpened);
        assertEquals(0, rs.folded);
        assertEquals(0, rs.raiseCallAllIn);
        assertEquals(0, rs.reRaiseOpened);
        
        //Counters
        assertEquals(0, rs.openedBySomeoneElse);
        assertEquals(0, rs.checkedThrough);
        assertEquals(0, rs.seen);
        assertEquals(0, rs.unopened);
        
        ////////////////////////////////
      
        pStats = stats.playerSessionStats.get("Francis");
        rs = pStats.roundStats[0];
        
        //Unopened stats
        assertEquals(1, rs.bets);
        assertEquals(0, rs.checksUnopened);
        assertEquals(0, rs.betAllIn);
        assertEquals(0, rs.callReraise);
        assertEquals(0, rs.betFold);
        
        //Opened stats
        assertEquals(0, rs.calls);        
        assertEquals(0, rs.checkRaises);
        assertEquals(0, rs.checksOpened);
        assertEquals(0, rs.folded);
        assertEquals(0, rs.raiseCallAllIn);
        assertEquals(0, rs.reRaiseOpened);
        
        //Counters
        assertEquals(0, rs.openedBySomeoneElse);
        assertEquals(0, rs.checkedThrough);
        assertEquals(1, rs.seen);
        assertEquals(1, rs.unopened);
        
        rs = pStats.roundStats[1];
        
      //Unopened stats
        assertEquals(1, rs.bets);
        assertEquals(0, rs.checksUnopened);
        assertEquals(0, rs.betAllIn);
        assertEquals(1, rs.callReraise);
        assertEquals(0, rs.betFold);
        
        //Opened stats
        assertEquals(0, rs.calls);        
        assertEquals(0, rs.checkRaises);
        assertEquals(0, rs.checksOpened);
        assertEquals(0, rs.folded);
        assertEquals(0, rs.raiseCallAllIn);
        assertEquals(0, rs.reRaiseOpened);
        
        //Counters
        assertEquals(0, rs.openedBySomeoneElse);
        assertEquals(0, rs.checkedThrough);
        assertEquals(1, rs.seen);
        assertEquals(1, rs.unopened);
        
        rs = pStats.roundStats[2];
      //Unopened stats
        assertEquals(0, rs.bets);
        assertEquals(0, rs.checksUnopened);
        assertEquals(0, rs.betAllIn);
        assertEquals(0, rs.callReraise);
        assertEquals(0, rs.betFold);
        
        //Opened stats
        assertEquals(0, rs.calls);        
        assertEquals(0, rs.checkRaises);
        assertEquals(0, rs.checksOpened);
        assertEquals(0, rs.folded);
        assertEquals(0, rs.raiseCallAllIn);
        assertEquals(0, rs.reRaiseOpened);
        
        //Counters
        assertEquals(0, rs.openedBySomeoneElse);
        assertEquals(0, rs.checkedThrough);
        assertEquals(0, rs.seen);
        assertEquals(0, rs.unopened);
        
        ////////////////////////////////
    }

    @Test
    public void testStats3() throws Exception
    {
        List<FlopTurnRiverState[]> results = TestPreflopStats.getList("testStats3");
        
        assertEquals(2, results.size());
        
        FlopTurnRiverState[] handStates = results.get(0);
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Dhimiter");
        RoundStats rs = pStats.roundStats[0];
        
        rs = pStats.roundStats[0];
        //Unopened stats
          assertEquals(1, rs.bets);
          assertEquals(0, rs.checksUnopened);
          assertEquals(0, rs.betAllIn);
          assertEquals(0, rs.callReraise);
          assertEquals(0, rs.betFold);
          
          //Opened stats
          assertEquals(0, rs.calls);        
          assertEquals(0, rs.checkRaises);
          assertEquals(0, rs.checksOpened);
          assertEquals(1, rs.folded);
          assertEquals(0, rs.raiseCallAllIn);
          assertEquals(0, rs.reRaiseOpened);
          
          //Counters
          assertEquals(1, rs.openedBySomeoneElse);
          assertEquals(0, rs.checkedThrough);
          assertEquals(2, rs.seen);
          assertEquals(1, rs.unopened);
          
          rs = pStats.roundStats[1];
          
          //Unopened stats
          assertEquals(1, rs.bets);
          assertEquals(0, rs.checksUnopened);
          assertEquals(0, rs.betAllIn);
          assertEquals(0, rs.callReraise);
          assertEquals(1, rs.betFold);
          
          //Opened stats
          assertEquals(0, rs.calls);        
          assertEquals(0, rs.checkRaises);
          assertEquals(0, rs.checksOpened);
          assertEquals(0, rs.folded);
          assertEquals(0, rs.raiseCallAllIn);
          assertEquals(0, rs.reRaiseOpened);
          
          //Counters
          assertEquals(0, rs.openedBySomeoneElse);
          assertEquals(0, rs.checkedThrough);
          assertEquals(1, rs.seen);
          assertEquals(1, rs.unopened);
    }
    
    @Test
    public void testRoundStats() throws Exception
    {
        List<FlopTurnRiverState[]> results = TestPreflopStats.getList("testRoundStats");
        
        assertEquals(6, results.size());
                                 
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
        assertEquals(0+0+1+1+1+0, ericStats.preFlopRaises );
        assertEquals(0+0+0+0+0+0, ericStats.notFoldRaisedPreflop );
        assertEquals(0+0+0+0+0+0, ericStats.raisedPreflopDenom );
        
        assertEquals(1+0+0+0+0+0, ericStats.flopStats.calls );
        assertEquals(0+0+0+0+1+0, ericStats.flopStats.callReraise );
        assertEquals(0+0+0+0+0+0, ericStats.flopStats.folded );
        assertEquals(0+1+0+0+1+0, ericStats.flopStats.bets );
        assertEquals(0+0+1+0+0+0, ericStats.flopStats.reRaiseOpened );
        assertEquals(1+1+1+1+1+0, ericStats.flopStats.seen );
        assertEquals(0+0+0+0+0+0, ericStats.flopStats.checkRaises );
        
        assertEquals(0+0+1+0+0+0, ericStats.turnStats.calls );
        assertEquals(0+0+0+0+1+0, ericStats.turnStats.folded );
        assertEquals(1+1+0+0+0+0, ericStats.turnStats.bets );
        assertEquals(1+0+0+0+0+0, ericStats.turnStats.reRaiseUnopened );
        assertEquals(1+1+1+1+1+0, ericStats.turnStats.seen );
        assertEquals(0+0+0+0+0+0, ericStats.turnStats.checkRaises );
        
        
        assertEquals(0+1+0+0+0+0, ericStats.riverStats.callReraise );
        assertEquals(0+0+0+0+0+0, ericStats.riverStats.folded );
        assertEquals(1+1+0+0+0+0, ericStats.riverStats.bets );
        assertEquals(0+0+0+0+0+0, ericStats.riverStats.reRaiseOpened );
        assertEquals(1+1+0+1+0+0, ericStats.riverStats.seen );
        assertEquals(0+0+0+0+0+0, ericStats.riverStats.checkRaises );
        
        
        
    }
    
    @Test
    public void testReraiseAndAllin() throws Exception
    {
        List<FlopTurnRiverState[]> results = TestPreflopStats.getList("testReraise1");
        
        assertEquals(1, results.size());
                                 
        StatsSession stats = Parser.computeStats(results);
        
        //Eric  -   1        -  2 
        //Morris  - 1     4  -    3
        //*       -   2       - 
        //Billy   - 1 2 3   5 - 
        //Anto    -     3     -    4
        
        StatsSessionPlayer pStats  =
                stats.playerSessionStats.get("Eric");
        
        DonkContLimped dcl = (DonkContLimped) pStats.stats.get("dcl1");
        dcl = (DonkContLimped) pStats.stats.get("dcl2");
        dcl = (DonkContLimped) pStats.stats.get("dcl3");
        
        pStats  =
                stats.playerSessionStats.get("Ivana");
        
        dcl = (DonkContLimped) pStats.stats.get("dcl1");
        
        log.debug(dcl.toString());
        
        dcl = (DonkContLimped) pStats.stats.get("dcl2");
        dcl = (DonkContLimped) pStats.stats.get("dcl3");
    }
}
