package test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.FlopTurnRiverState;
import pkr.history.HandInfoCollector;
import pkr.history.Parser;
import pkr.history.PlayerAction;
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
        HandInfoCollector results = TestPreflopStats.getList("testTapis1");
        
        assertEquals(1, results.listHandInfo.size());
        
        FlopTurnRiverState[] roundStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Dennis"); 
       
        DonkContLimped dcl = (DonkContLimped) pStats.stats.get("dcl1");
        String s = dcl.toString();
        
        pStats = stats.playerSessionStats.get("Roy"); 
        
        dcl = (DonkContLimped) pStats.stats.get("dcl1");
        
    }
    
    @Test
    public void testTapis2() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testTapis2");
        
        assertEquals(1, results.listHandInfo.size());
        
        FlopTurnRiverState[] roundStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        Parser.outputHands(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Dennis");
        
        List<PlayerAction> actions = roundStates[1].actions;
        
        assertEquals(3, actions.size());
        
        PlayerAction action = actions.get(0);
        
        assertEquals("Eric",  action.playerName);
        assertEquals(0,  action.playerPosition);
       // assertEquals(0, action.pot);
        assertEquals(000000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(3200000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(3, action.playersLeft);
        
        action = actions.get(2);
        
        assertEquals("Pierre",  action.playerName);
        assertEquals(2,  action.playerPosition);
       // assertEquals(400000, action.pot);
        assertEquals(3200000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL_ALL_IN, action.action);
        assertEquals(3, action.playersLeft);
        
        actions = roundStates[2].actions;
        
        assertEquals(2, actions.size());
        
        action = actions.get(0);
        
        assertEquals("Eric",  action.playerName);
        assertEquals(0,  action.playerPosition);
       // assertEquals(0, action.pot);
        assertEquals(000000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(5900000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(2, action.playersLeft);
        
    
        actions = roundStates[3].actions;
        action = actions.get(0);
        
        assertEquals(2, actions.size());
        
        assertEquals("Eric",  action.playerName);
        assertEquals(0,  action.playerPosition);
       // assertEquals(0, action.pot);
        assertEquals(000000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(4200000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(2, action.playersLeft);
        
        
    }
    
    @Test
    public void testTapis3() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testTapis3");
        
        assertEquals(1, results.listHandInfo.size());
        
        FlopTurnRiverState[] roundStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        Parser.outputHands(results);
        
        //StatsSessionPlayer pStats = stats.playerSessionStats.get("Dennis");
        
        List<PlayerAction> actions = roundStates[0].actions;
        
        assertEquals(6, actions.size());
        
        PlayerAction action = actions.get(0);
        
        assertEquals("Fatin",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(600000, action.pot);
        assertEquals(400000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(5, action.playersLeft);
        
        action = actions.get(1);
        
        assertEquals("Hydz",  action.playerName);
        assertEquals(1,  action.playerPosition);
        assertEquals(1000000, action.pot);
        assertEquals(400000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(3200000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE_ALL_IN, action.action);
        assertEquals(5, action.playersLeft);
        
        action = actions.get(2);
        
        assertEquals("Eric",  action.playerName);
        assertEquals(2,  action.playerPosition);
        assertEquals(4200000, action.pot);
        assertEquals(3200000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.FOLD, action.action);
        assertEquals(4, action.playersLeft);
        
        action = actions.get(3);
        
        assertEquals("Turgay",  action.playerName);
        assertEquals(3,  action.playerPosition);
        assertEquals(4200000, action.pot);
        assertEquals(3200000, action.incomingBetOrRaise);
        assertEquals(200000, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(3, action.playersLeft);
 

        action = actions.get(4);
        
        assertEquals("Elif Hatice",  action.playerName);
        assertEquals(4,  action.playerPosition);
        assertEquals(7200000, action.pot);
        assertEquals(3200000, action.incomingBetOrRaise);
        assertEquals(400000, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(3, action.playersLeft);
        
        action = actions.get(5);
        
        assertEquals("Fatin",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(10000000, action.pot);
        assertEquals(3200000, action.incomingBetOrRaise);
        assertEquals(400000, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(3, action.playersLeft);
        
        actions = roundStates[1].actions;
        
        assertEquals(4, actions.size());
        
        action = actions.get(0);
        
        assertEquals("Turgay",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(12800000, action.pot);
        assertEquals(00000, action.incomingBetOrRaise);
        assertEquals(00000, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.CHECK, action.action);
        assertEquals(3, action.playersLeft);
        
        action = actions.get(1);
        
        assertEquals("Elif Hatice",  action.playerName);
        assertEquals(1,  action.playerPosition);
        assertEquals(12800000, action.pot);
        assertEquals(00000, action.incomingBetOrRaise);
        assertEquals(00000, action.playerAmtPutInPotThisRound);
        assertEquals(5300000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(3, action.playersLeft);
        
        action = actions.get(2);
        
        assertEquals("Fatin",  action.playerName);
        assertEquals(2,  action.playerPosition);
        assertEquals(18100000, action.pot);
        assertEquals(5300000, action.incomingBetOrRaise);
        assertEquals(00000, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(3, action.playersLeft);
        
       action = actions.get(3);
        
        assertEquals("Turgay",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(23400000, action.pot);
        assertEquals(5300000, action.incomingBetOrRaise);
        assertEquals(00000, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.FOLD, action.action);
        assertEquals(3, action.playersLeft);
        
        actions = roundStates[2].actions;
        
        assertEquals(2, actions.size());
        
        action = actions.get(0);
        
        assertEquals("Elif Hatice",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(23400000, action.pot);
        assertEquals(00000, action.incomingBetOrRaise);
        assertEquals(00000, action.playerAmtPutInPotThisRound);
        assertEquals(12645464, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE_ALL_IN, action.action);
        assertEquals(2, action.playersLeft);
        
       action = actions.get(1);
        
        assertEquals("Fatin",  action.playerName);
        assertEquals(1,  action.playerPosition);
        assertEquals(36045464, action.pot);
        assertEquals(12645464, action.incomingBetOrRaise);
        assertEquals(00000, action.playerAmtPutInPotThisRound);
        assertEquals(0, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(1, action.playersLeft);
 
 
 
    }
}