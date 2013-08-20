package test;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pkr.history.FlopTurnRiverState;
import pkr.history.Parser;
import pkr.history.StatsSession;
import pkr.history.StatsSessionPlayer;

@RunWith(JUnit4.class)
public class TestPreflopStats
{

    
    
    public static List<FlopTurnRiverState[]> getList(String testFile) throws URISyntaxException, IOException
    {
        URL testInputUrl = TestPreflopStats.class.getResource("/parser/" + testFile + ".txt");
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
    public void testTapisSuivi1() throws Exception
    {
        List<FlopTurnRiverState[]> results = getList("testTapisSuivi1");
        
        assertEquals(5, results.size());
        
        FlopTurnRiverState[] handStates = results.get(0);
        
        StatsSession stats = Parser.computeStats(results);
    }
    
    @SuppressWarnings("unused")
    @Test
    public void testTurnTapis() throws Exception
    {
        List<FlopTurnRiverState[]> results = getList("testNonPreflopTapis1");
        
        assertEquals(4, results.size());
        
        FlopTurnRiverState[] handStates = results.get(0);
        
        StatsSession stats = Parser.computeStats(results);
    }
    
    @SuppressWarnings("unused")
    @Test
    public void testVPIP1() throws Exception
    {
        List<FlopTurnRiverState[]> results = getList("testVPIP1");
        
        assertEquals(5, results.size());
        
        FlopTurnRiverState[] handStates = results.get(0);
        
        StatsSession stats = Parser.computeStats(results);
        
        //Eric  -   1        -  2 
        //Morris  - 1     4  -    3
        //*       -   2       - 
        //Billy   - 1 2 3   5 - 
        //Anto    -     3     -    4
        
        List<StatsSessionPlayer> players = Arrays.asList(
                stats.playerSessionStats.get("Eric"),
                stats.playerSessionStats.get("Morris"),
                stats.playerSessionStats.get("מוריס"),
                stats.playerSessionStats.get("Billy"),
                stats.playerSessionStats.get("Anto"));
        
        int playerNum = 0;
        StatsSessionPlayer pStats =  stats.playerSessionStats.get("Eric");
        
        assertEquals("25%", pStats.stats.get("vpip").getValue());
        assertEquals(1, players.get(playerNum).vpipNumerator );
        assertEquals(4, players.get(playerNum).vpipDenom );
        assertEquals(5, players.get(playerNum).totalHands );
        
        playerNum = 1;
        pStats =  stats.playerSessionStats.get("Morris");
        assertEquals(2, players.get(playerNum).vpipNumerator );
        assertEquals(4, players.get(playerNum).vpipDenom );
        
        
        assertEquals("50%", pStats.stats.get("vpip").getValue());
        assertEquals(5, players.get(playerNum).totalHands );
        
        playerNum = 2;
        pStats =  stats.playerSessionStats.get("מוריס");
        assertEquals(1, players.get(playerNum).vpipNumerator );
        assertEquals(3, players.get(playerNum).vpipDenom );
        assertEquals(3, players.get(playerNum).totalHands );
        assertEquals("33.3%", pStats.stats.get("vpip").getValue());
        
        playerNum = 3;
        pStats =  stats.playerSessionStats.get("Billy");
        assertEquals(4, players.get(playerNum).vpipNumerator );
        assertEquals(5, players.get(playerNum).vpipDenom );
        assertEquals(5, players.get(playerNum).totalHands );
        assertEquals("80%", pStats.stats.get("vpip").getValue());
        
        playerNum = 4;
        pStats =  stats.playerSessionStats.get("Anto");
        assertEquals(1, players.get(playerNum).vpipNumerator );
        assertEquals(2, players.get(playerNum).vpipDenom );
        assertEquals("50%", pStats.stats.get("vpip").getValue());
        assertEquals(3, players.get(playerNum).totalHands );
    }
    
    /**
     * Test raised pots
     * @throws Exception
     */
    @Test
    public void testVPIP2() throws Exception
    {
        List<FlopTurnRiverState[]> results = getList("testVPIP2");
        
        assertEquals(4, results.size());
                                 
        StatsSession stats = Parser.computeStats(results);
        
        //Eric  -   1        -  2 
        //Morris  - 1     4  -    3
        //*       -   2       - 
        //Billy   - 1 2 3   5 - 
        //Anto    -     3     -    4
        
        List<StatsSessionPlayer> players = Arrays.asList(
                stats.playerSessionStats.get("Eric"),
                stats.playerSessionStats.get("Ahmed"),
                stats.playerSessionStats.get("Bena"));
        
        int playerNum = 0;
        StatsSessionPlayer pStats =  players.get(playerNum); 
        assertEquals(4, pStats.vpipNumerator );
        assertEquals(4, players.get(playerNum).vpipDenom );
        assertEquals(4, players.get(playerNum).totalHands );
        assertEquals(3, pStats.preFlopRaises );
        assertEquals(766666.66, pStats.preFlopRaiseTotalAmt / pStats.preFlopRaises, .1 );
        assertEquals(0, pStats.notFoldRaisedPreflop );
        assertEquals(0, pStats.raisedPreflopDenom );
        
        assertEquals(1, pStats.preFlopTapis );
        
        playerNum = 1;
        pStats =  players.get(playerNum);
        assertEquals(4, players.get(playerNum).vpipNumerator );
        assertEquals(4, players.get(playerNum).vpipDenom );
        assertEquals(4, players.get(playerNum).totalHands );
        assertEquals(4, pStats.notFoldRaisedPreflop );
        assertEquals(4, pStats.raisedPreflopDenom );
        
        
        playerNum = 2;
        pStats =  players.get(playerNum); 
        assertEquals(3, players.get(playerNum).vpipNumerator );
        assertEquals(4, players.get(playerNum).vpipDenom );
        assertEquals(4,  players.get(playerNum).totalHands );
        assertEquals(3, pStats.notFoldRaisedPreflop );
        assertEquals(4, pStats.raisedPreflopDenom );
        
    }
    
    @Test
    public void testPFR1() throws Exception
    {
        List<FlopTurnRiverState[]> results = getList("testPFR1");
        
        assertEquals(6, results.size());
                                 
        StatsSession stats = Parser.computeStats(results);
        
        //Eric  -   1        -  2 
        //Morris  - 1     4  -    3
        //*       -   2       - 
        //Billy   - 1 2 3   5 - 
        //Anto    -     3     -    4
        
        List<StatsSessionPlayer> players = Arrays.asList(
                stats.playerSessionStats.get("Serge"),
                stats.playerSessionStats.get("Marc"),
                stats.playerSessionStats.get("Thomas"),
                stats.playerSessionStats.get("Eric"),
                stats.playerSessionStats.get("Ahmed"),
                stats.playerSessionStats.get("Yara"),
                stats.playerSessionStats.get("Bena")
                );
        
        int playerNum = 0;
        assertEquals(1, players.get(playerNum).vpipNumerator );
        assertEquals(6, players.get(playerNum).vpipDenom );
        assertEquals(6, players.get(playerNum).totalHands );
        assertEquals(0, players.get(playerNum).preFlopRaises );
        assertEquals(0, players.get(playerNum).notFoldRaisedPreflop );
        assertEquals(4, players.get(playerNum).raisedPreflopDenom );
        
        
        playerNum = 1;
        assertEquals(2, players.get(playerNum).vpipNumerator );
        assertEquals(3, players.get(playerNum).vpipDenom );
        assertEquals(3, players.get(playerNum).totalHands );
        assertEquals(0, players.get(playerNum).preFlopRaises );
        assertEquals(0, players.get(playerNum).notFoldRaisedPreflop );
        assertEquals(2, players.get(playerNum).raisedPreflopDenom );
        
        playerNum = 2;
        assertEquals(2, players.get(playerNum).vpipNumerator );
        assertEquals(3, players.get(playerNum).vpipDenom );
        assertEquals(3, players.get(playerNum).totalHands );
        assertEquals(0, players.get(playerNum).preFlopRaises );
        assertEquals(2, players.get(playerNum).preFlopTapis );
        assertEquals(0, players.get(playerNum).notFoldRaisedPreflop );
        assertEquals(0, players.get(playerNum).raisedPreflopDenom );
        
        //Eric
        playerNum = 3;
        assertEquals(2, players.get(playerNum).vpipNumerator );
        assertEquals(6, players.get(playerNum).vpipDenom );
        assertEquals(6, players.get(playerNum).totalHands );
        assertEquals(0, players.get(playerNum).preFlopRaises );
        assertEquals(1, players.get(playerNum).notFoldRaisedPreflop );
        assertEquals(5, players.get(playerNum).raisedPreflopDenom );
        
        //Amed
        playerNum = 4;
        assertEquals(1, players.get(playerNum).vpipNumerator );
        assertEquals(5, players.get(playerNum).vpipDenom );
        assertEquals(6, players.get(playerNum).totalHands );
        assertEquals(0, players.get(playerNum).preFlopRaises );
        assertEquals(1, players.get(playerNum).notFoldRaisedPreflop );
        assertEquals(5, players.get(playerNum).raisedPreflopDenom );
        
        playerNum = 5;
        assertEquals(3, players.get(playerNum).vpipNumerator );
        assertEquals(3, players.get(playerNum).vpipDenom );
        assertEquals(3, players.get(playerNum).totalHands );
        assertEquals(0, players.get(playerNum).preFlopRaises );
        assertEquals(3, players.get(playerNum).preFlopTapis );
        assertEquals(0, players.get(playerNum).notFoldRaisedPreflop );
        assertEquals(0, players.get(playerNum).raisedPreflopDenom );
        
        playerNum = 6;
        assertEquals(1, players.get(playerNum).vpipNumerator );
        assertEquals(3, players.get(playerNum).vpipDenom );
        assertEquals(3, players.get(playerNum).totalHands );
        assertEquals(0, players.get(playerNum).preFlopRaises );
        assertEquals(1, players.get(playerNum).notFoldRaisedPreflop );
        assertEquals(3, players.get(playerNum).raisedPreflopDenom );
        
    }
    
    @Test
    public void test3bet1() throws Exception
    {
        List<FlopTurnRiverState[]> results = getList("test3bet1");
        
        assertEquals(6, results.size());
                                 
        StatsSession stats = Parser.computeStats(results);
        
        //Eric  -   1        -  2 
        //Morris  - 1     4  -    3
        //*       -   2       - 
        //Billy   - 1 2 3   5 - 
        //Anto    -     3     -    4
        
        StatsSessionPlayer pStat = 
                stats.playerSessionStats.get("Eric");
        
        assertEquals(4, pStat.vpipNumerator );
        assertEquals(4, pStat.vpipDenom );
        assertEquals(4, pStat.totalHands );
        assertEquals(0, pStat.preFlopRaises );
        
        assertEquals(1, pStat.notFoldRaisedPreflop );
        assertEquals(4, pStat.raisedPreflopDenom );
        
        assertEquals(1, pStat.notFoldReraisedPreflop );
        assertEquals(3, pStat.reraisedPreflopDenom );
        
        assertEquals(3, pStat.preFlopRaiseTotalAmt / pStat.preFlopRaises, 0.1  );
        assertEquals(3, pStat.preFlopTapis );
        assertEquals(3, pStat.totalHands );
        
        pStat = 
                stats.playerSessionStats.get("Joe");
        
        assertEquals(4, pStat.vpipNumerator );
        assertEquals(4, pStat.vpipDenom );
        assertEquals(4, pStat.totalHands );
        assertEquals(0, pStat.preFlopRaises );
        assertEquals(1, pStat.notFoldRaisedPreflop );
        assertEquals(1, pStat.raisedPreflopDenom );
        
        assertEquals(0, pStat.notFoldReraisedPreflop );
        assertEquals(1, pStat.reraisedPreflopDenom );
        
        assertEquals(3, pStat.preFlopRaiseTotalAmt / pStat.preFlopRaises, 0.1  );
        assertEquals(3, pStat.preFlopTapis );
        assertEquals(3, pStat.totalHands );
        
        pStat = 
                stats.playerSessionStats.get("Bill");
        
        assertEquals(4, pStat.vpipNumerator );
        assertEquals(4, pStat.vpipDenom );
        assertEquals(4, pStat.totalHands );
        assertEquals(0, pStat.preFlopRaises );
        assertEquals(1, pStat.notFoldRaisedPreflop );
        assertEquals(1, pStat.raisedPreflopDenom );
        
        assertEquals(0, pStat.notFoldReraisedPreflop );
        assertEquals(1, pStat.reraisedPreflopDenom );
        
        assertEquals(3, pStat.preFlopRaiseTotalAmt / pStat.preFlopRaises, 0.1  );
        assertEquals(3, pStat.preFlopTapis );
        assertEquals(3, pStat.totalHands );
        
        pStat = 
                stats.playerSessionStats.get("Ivana");
        
        assertEquals(4, pStat.vpipNumerator );
        assertEquals(4, pStat.vpipDenom );
        assertEquals(4, pStat.totalHands );
        assertEquals(0, pStat.preFlopRaises );
        assertEquals(1, pStat.notFoldRaisedPreflop );
        assertEquals(1, pStat.raisedPreflopDenom );
        
        assertEquals(0, pStat.notFoldReraisedPreflop );
        assertEquals(1, pStat.reraisedPreflopDenom );
        
        assertEquals(3, pStat.preFlopRaiseTotalAmt / pStat.preFlopRaises, 0.1  );
        assertEquals(3, pStat.preFlopTapis );
        assertEquals(3, pStat.totalHands );
    }
    
}
