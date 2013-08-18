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

import pkr.history.FlopTurnRiverState;
import pkr.history.Parser;
import pkr.history.StatsSession;
import pkr.history.StatsSessionPlayer;
import pkr.history.StatsSessionPlayer.RoundStats;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TestStats
{

    public TestStats() {
        // TODO Auto-generated constructor stub
    }
    
    public List<FlopTurnRiverState[]> getList(String testFile) throws URISyntaxException, IOException
    {
        URL testInputUrl = getClass().getResource("/parser/" + testFile + ".txt");
        //URL cleanInputUrl = getClass().getResource("/parser/clean" + testFile + ".txt");
        
        String testOutputUrlStr = testInputUrl.toString().replace(testFile, 
                "clean" + testFile);
        URL cleanInputUrl = new URL(testOutputUrlStr);
        
        File testInputRaw = new File(testInputUrl.toURI());
        File testInput = new File(cleanInputUrl.toURI());
        
        return Parser.parseFile(testInputRaw, testInput);
    }

    @SuppressWarnings("unused")
    @Test
    public void testStats1() throws Exception
    {
        List<FlopTurnRiverState[]> results = getList("testStats1");
        
        assertEquals(1, results.size());
        
        FlopTurnRiverState[] handStates = results.get(0);
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Manfred"); 
        assertEquals(1, pStats.roundStats[0].calls);
        assertEquals(1, pStats.roundStats[1].betAllIn);
        assertEquals(0, pStats.roundStats[1].raiseCallAllIn);
    }
    
    @Test
    public void testStats2() throws Exception
    {
        List<FlopTurnRiverState[]> results = getList("testStats2");
        
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

}
