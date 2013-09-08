package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

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
import pkr.history.stats.Vpip;

@SuppressWarnings("unused")
@RunWith(JUnit4.class)
public class TestVPIP
{

    
    private static Logger log = LoggerFactory.getLogger(TestVPIP.class);
   

    @Test
    public void testVPIP1() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testVPIP1");
        
        assertEquals(5, results.listHandInfo.size());
        
        FlopTurnRiverState[] handStates = results.listHandInfo.get(0).roundStates;
        
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
        
        Vpip vpip = (Vpip) pStats.stats.get("vpip");
        assertEquals(1, vpip.moneyIn);
        assertEquals(5, vpip.played);
        assertEquals(5, players.get(playerNum).totalHands );
        
        playerNum = 1;
        pStats =  stats.playerSessionStats.get("Morris");
        vpip = (Vpip) pStats.stats.get("vpip");
        assertEquals(2, vpip.moneyIn);
        assertEquals(5, vpip.played);
        assertEquals(5, players.get(playerNum).totalHands );
        
        playerNum = 2;
        pStats =  stats.playerSessionStats.get("מוריס");
        assertEquals(3, players.get(playerNum).totalHands );
        vpip = (Vpip) pStats.stats.get("vpip");
        assertEquals(1, vpip.moneyIn);
        assertEquals(3, vpip.played);
        assertEquals("Vpip : 33.3% (1/3)", pStats.getStatValue("vpip"));
        
        playerNum = 3;
        pStats =  stats.playerSessionStats.get("Billy");
        assertEquals(5, players.get(playerNum).totalHands );
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(4, vpip.moneyIn);
        assertEquals(5, vpip.played);
        
        assertEquals("Vpip : 80% (4/5)", pStats.getStatValue("vpip"));
        
        playerNum = 4;
        pStats =  stats.playerSessionStats.get("Anto");
        vpip = (Vpip) pStats.stats.get("vpip");
        assertEquals(1, vpip.moneyIn);
        assertEquals(3, vpip.played);
        assertEquals(3, players.get(playerNum).totalHands );
    }
    
    /**
     * Test raised pots
     * @throws Exception
     */
    @Test
    public void testVPIP2() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testVPIP2");
        
        assertEquals(4, results.listHandInfo.size());
                                 
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
    public void testVPIP3() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testVPIP3");
        
        assertEquals(5, results.listHandInfo.size());
        
        FlopTurnRiverState[] roundStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Eric"); 
       
        Vpip vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(2, vpip.moneyIn);
        assertEquals(5, vpip.played);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(2, vpip.posPlayed[Vpip.SB_POS]);
        

        assertEquals(1, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(3, vpip.posPlayed[Vpip.BB_POS]);
        
        pStats = stats.playerSessionStats.get("Ahmed"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(4, vpip.moneyIn);
        assertEquals(5, vpip.played);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(2, vpip.posPlayed[Vpip.BB_POS]);
        
        assertEquals(3, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(3, vpip.posPlayed[Vpip.SB_POS]);
        
        
    }
    
    @Test
    public void testVPIP4() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testVPIP4");
        
        assertEquals(3, results.listHandInfo.size());
        
       
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Eric"); 
       
        Vpip vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(2, vpip.moneyIn);
        assertEquals(3, vpip.played);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(2, vpip.posPlayed[Vpip.BB_POS]);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.BTN_POS]);
        
        pStats = stats.playerSessionStats.get("A"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(2, vpip.moneyIn);
        assertEquals(3, vpip.played);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(2, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BB_POS]);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.BTN_POS]);
        
        pStats = stats.playerSessionStats.get("B"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(2, vpip.moneyIn);
        assertEquals(3, vpip.played);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.BB_POS]);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.BTN_POS]);
        
        
        
    }
    
    @Test
    public void testVPIP5() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testVPIP5");
        
        assertEquals(2, results.listHandInfo.size());
        
       
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = null; 
       
        Vpip vpip = null;
        
        pStats = stats.playerSessionStats.get("A"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(1, vpip.moneyIn);
        assertEquals(2, vpip.played);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.MID_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.MID_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.BTN_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BB_POS]);
        
        pStats = stats.playerSessionStats.get("B"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(1, vpip.moneyIn);
        assertEquals(2, vpip.played);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.MID_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.MID_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.BTN_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BB_POS]);
        
        pStats = stats.playerSessionStats.get("C"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(2, vpip.moneyIn);
        assertEquals(2, vpip.played);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.MID_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.MID_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BTN_POS]);
        
        assertEquals(2, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(2, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BB_POS]);
        
        pStats = stats.playerSessionStats.get("D"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(1, vpip.moneyIn);
        assertEquals(2, vpip.played);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.MID_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.MID_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BTN_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(2, vpip.posPlayed[Vpip.BB_POS]);
        
        
    }
    
    @Test
    public void testVPIP6() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testVPIP6");
        
        assertEquals(1, results.listHandInfo.size());
        
       
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = null; 
       
        Vpip vpip = null;
        
        pStats = stats.playerSessionStats.get("A"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(0, vpip.moneyIn);
        assertEquals(1, vpip.played);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.EARLY_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.EARLY_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.MID_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.MID_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BTN_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BB_POS]);
        
pStats = stats.playerSessionStats.get("B"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(1, vpip.moneyIn);
        assertEquals(1, vpip.played);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.EARLY_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.EARLY_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.MID_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.MID_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BTN_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BB_POS]);
        
pStats = stats.playerSessionStats.get("C"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(1, vpip.moneyIn);
        assertEquals(1, vpip.played);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.EARLY_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.EARLY_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.MID_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.MID_POS]);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.BTN_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BB_POS]);
        
pStats = stats.playerSessionStats.get("D"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(1, vpip.moneyIn);
        assertEquals(1, vpip.played);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.EARLY_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.EARLY_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.MID_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.MID_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BTN_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(1, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.BB_POS]);
        
pStats = stats.playerSessionStats.get("E"); 
        
        vpip = (Vpip) pStats.stats.get("vpip");
        
        assertEquals(0, vpip.moneyIn);
        assertEquals(1, vpip.played);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.EARLY_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.EARLY_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.MID_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.MID_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BTN_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BTN_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.SB_POS]);
        assertEquals(1, vpip.posPlayed[Vpip.SB_POS]);
        
        assertEquals(0, vpip.posMoneyIn[Vpip.BB_POS]);
        assertEquals(0, vpip.posPlayed[Vpip.BB_POS]);
        
    }
}