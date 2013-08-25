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
import pkr.history.HandInfoCollector;
import pkr.history.Parser;
import pkr.history.StatsSession;
import pkr.history.StatsSessionPlayer;

@RunWith(JUnit4.class)
public class TestPreflopStats
{

    
    
    public static HandInfoCollector getList(String testFile) throws URISyntaxException, IOException
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
        HandInfoCollector results = getList("testTapisSuivi1");
        
        assertEquals(5, results.masterList.size());
        
        FlopTurnRiverState[] handStates = results.masterList.get(0);
        
        StatsSession stats = Parser.computeStats(results);
    }
    
    @SuppressWarnings("unused")
    @Test
    public void testTurnTapis() throws Exception
    {
        HandInfoCollector results = getList("testNonPreflopTapis1");
        
        assertEquals(4, results.masterList.size());
        
        FlopTurnRiverState[] handStates = results.masterList.get(0);
        
        StatsSession stats = Parser.computeStats(results);
    }
    
    @SuppressWarnings("unused")
    @Test
    public void testVPIP1() throws Exception
    {
        HandInfoCollector results = getList("testVPIP1");
        
        assertEquals(5, results.masterList.size());
        
        FlopTurnRiverState[] handStates = results.masterList.get(0);
        
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
        
        assertEquals("Vpip : 25% (1/4)", pStats.getStatValue("vpip"));
        assertEquals(5, players.get(playerNum).totalHands );
        
        playerNum = 1;
        pStats =  stats.playerSessionStats.get("Morris");
        assertEquals("Vpip : 50% (2/4)", pStats.getStatValue("vpip"));
        assertEquals(5, players.get(playerNum).totalHands );
        
        playerNum = 2;
        pStats =  stats.playerSessionStats.get("מוריס");
        assertEquals(3, players.get(playerNum).totalHands );
        assertEquals("Vpip : 33.3% (1/3)", pStats.getStatValue("vpip"));
        
        playerNum = 3;
        pStats =  stats.playerSessionStats.get("Billy");
        assertEquals(5, players.get(playerNum).totalHands );
        assertEquals("Vpip : 80% (4/5)", pStats.getStatValue("vpip"));
        
        playerNum = 4;
        pStats =  stats.playerSessionStats.get("Anto");
        assertEquals("Vpip : 50% (1/2)", pStats.getStatValue("vpip"));
        assertEquals(3, players.get(playerNum).totalHands );
    }
    
    /**
     * Test raised pots
     * @throws Exception
     */
    @Test
    public void testVPIP2() throws Exception
    {
        HandInfoCollector results = getList("testVPIP2");
        
        assertEquals(4, results.masterList.size());
                                 
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
        assertEquals("Vpip : 100% (4/4)", pStats.getStatValue("vpip"));
        assertEquals(4, players.get(playerNum).totalHands );
        
        
        playerNum = 1;
        pStats =  players.get(playerNum);
        
        assertEquals("Vpip : 100% (4/4)", pStats.getStatValue("vpip"));
        assertEquals(4, players.get(playerNum).totalHands );
        
        
        playerNum = 2;
        pStats =  players.get(playerNum); 
        
        assertEquals("Vpip : 75% (3/4)", pStats.getStatValue("vpip"));
        assertEquals(4,  players.get(playerNum).totalHands );
        
    }
    
    @Test
    public void testPFR1() throws Exception
    {
        HandInfoCollector results = getList("testPFR1");
        
        assertEquals(6, results.masterList.size());
                                 
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
        StatsSessionPlayer pStats =  players.get(playerNum);
        assertEquals("Vpip : 16.7% (1/6)", pStats.getStatValue("vpip"));
        assertEquals(6, players.get(playerNum).totalHands );
        
        
        playerNum = 1;
        pStats =  players.get(playerNum);
        
        assertEquals("Vpip : 66.7% (2/3)", pStats.getStatValue("vpip"));
        assertEquals(3, players.get(playerNum).totalHands );
        
        playerNum = 2;
        pStats =  players.get(playerNum);
        assertEquals("Vpip : 66.7% (2/3)", pStats.getStatValue("vpip"));
        assertEquals(3, players.get(playerNum).totalHands );
        
        //Eric
        playerNum = 3;
        pStats =  players.get(playerNum);
        
        assertEquals("Vpip : 33.3% (2/6)", pStats.getStatValue("vpip"));
        assertEquals(6, players.get(playerNum).totalHands );
        
        //Amed
        playerNum = 4;
        pStats =  players.get(playerNum);
        
        assertEquals("Vpip : 20% (1/5)", pStats.getStatValue("vpip"));
        
        assertEquals(6, players.get(playerNum).totalHands );
        
        playerNum = 5;
        pStats =  players.get(playerNum);
        
        assertEquals("Vpip : 100% (3/3)", pStats.getStatValue("vpip"));
        assertEquals(3, players.get(playerNum).totalHands );
        
        playerNum = 6;
        pStats =  players.get(playerNum);
        assertEquals("Vpip : 33.3% (1/3)", pStats.getStatValue("vpip"));
        assertEquals(3, players.get(playerNum).totalHands );
    }
    
    @Test
    public void test3bet1() throws Exception
    {
        HandInfoCollector results = getList("test3bet1");
        
        assertEquals(4, results.masterList.size());
                                 
        StatsSession stats = Parser.computeStats(results);
        
        //Eric  -   1        -  2 
        //Morris  - 1     4  -    3
        //*       -   2       - 
        //Billy   - 1 2 3   5 - 
        //Anto    -     3     -    4
        
        StatsSessionPlayer pStat = 
                stats.playerSessionStats.get("Eric");
        
        //String s = pStat.getStatValue("3bet");
        assertEquals("3bet : 100% (1/1) Avg amt : $1 000 000 Call : 100% (3/3)", pStat.getStatValue("3bet") );
        
        
        pStat = 
                stats.playerSessionStats.get("Joe");
        
        assertEquals("3bet : 66.7% (2/3) Avg amt : $1 100 000 Call : 100% (2/2)", pStat.getStatValue("3bet") );
        
        
        pStat = 
                stats.playerSessionStats.get("Bill");
        
        assertEquals("3bet : 100% (1/1) Avg amt : $600 000 Call : 66.7% (2/3)", pStat.getStatValue("3bet") );
        
        
        pStat = 
                stats.playerSessionStats.get("Manfred");
        
        assertEquals("3bet : 0% (0/3) Avg amt : $0 Call : 0% (0/1)", pStat.getStatValue("3bet") );
        
        
        
    }
    
    @Test
    public void testPFR2() throws Exception
    {
        HandInfoCollector results = getList("testPFR2");
        
        assertEquals(1, results.masterList.size());
                                 
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Idan");
        
        assertEquals("Pfr : 100% (1/1) Avg amt : $1 600 000 Tapis : 0", pStats.getStatValue("pfr"));
        
        
    }
}
