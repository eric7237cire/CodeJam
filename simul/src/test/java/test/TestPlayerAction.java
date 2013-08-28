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

@SuppressWarnings("unused")
@RunWith(JUnit4.class)
public class TestPlayerAction
{

    
    private static Logger log = LoggerFactory.getLogger(TestTapis.class);
   

    @Test
    public void testPlayerAction1() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testPlayerAction1");
        
        assertEquals(1, results.listHandInfo.size());
        
        FlopTurnRiverState[] roundStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
        StatsSessionPlayer pStats = stats.playerSessionStats.get("Dennis");
        
        List<PlayerAction> actions = roundStates[0].actions;
        
        assertEquals(6, actions.size());
        
        PlayerAction action = actions.get(0);
        
        assertEquals("C",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(0, action.pot);
        assertEquals(400000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(0, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(5, action.playersLeft);
        
        action = actions.get(1);
        
        assertEquals("D",  action.playerName);
        assertEquals(1,  action.playerPosition);
        assertEquals(400000, action.pot);
        assertEquals(400000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(800000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(5, action.playersLeft);
        
        action = actions.get(2);
        
        assertEquals("E",  action.playerName);
        assertEquals(2,  action.playerPosition);
        assertEquals(1200000, action.pot);
        assertEquals(800000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(0, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(5, action.playersLeft);
        
        action = actions.get(3);
        
        assertEquals("A",  action.playerName);
        assertEquals(3,  action.playerPosition);
        assertEquals(2000000, action.pot);
        assertEquals(800000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(5, action.playersLeft);
        
        action = actions.get(4);
        
        assertEquals("B",  action.playerName);
        assertEquals(4,  action.playerPosition);
        assertEquals(2800000, action.pot);
        assertEquals(800000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(5, action.playersLeft);

        
        action = actions.get(5);
        
        assertEquals("C",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(3600000, action.pot);
        assertEquals(800000, action.incomingBetOrRaise);
        assertEquals(400000, action.playerAmtPutInPotThisRound);
        assertEquals(0, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL_ALL_IN, action.action);
        assertEquals(5, action.playersLeft);
        
        
        actions = roundStates[1].actions;
        
        assertEquals(6, actions.size());
        
        action = actions.get(0);
        
        assertEquals("A",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(4000000, action.pot);
        assertEquals(0, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(0, action.amountRaised);
        assertEquals(PlayerAction.Action.CHECK, action.action);
        assertEquals(4, action.playersLeft);
        
        action = actions.get(1);
        
        assertEquals("B",  action.playerName);
        assertEquals(1,  action.playerPosition);
        assertEquals(4000000, action.pot);
        assertEquals(0, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(800000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(4, action.playersLeft);
        
        action = actions.get(2);
        
        assertEquals("D",  action.playerName);
        assertEquals(2,  action.playerPosition);
        assertEquals(4800000, action.pot);
        assertEquals(800000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(2000000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE_ALL_IN, action.action);
        assertEquals(4, action.playersLeft);
        
        action = actions.get(3);
        
        assertEquals("E",  action.playerName);
        assertEquals(3,  action.playerPosition);
        assertEquals(6800000, action.pot);
        assertEquals(2000000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(000000, action.amountRaised);
        assertEquals(PlayerAction.Action.FOLD, action.action);
        assertEquals(3, action.playersLeft);
        
        action = actions.get(4);
        
        assertEquals("A",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(6800000, action.pot);
        assertEquals(2000000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(000000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(2, action.playersLeft);
        
        action = actions.get(5);
        
        assertEquals("B",  action.playerName);
        assertEquals(1,  action.playerPosition);
        assertEquals(8800000, action.pot);
        assertEquals(2000000, action.incomingBetOrRaise);
        assertEquals(800000, action.playerAmtPutInPotThisRound);
        assertEquals(000000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(2, action.playersLeft);
        
        actions = roundStates[2].actions;
        
        assertEquals(7, actions.size());
        
        action = actions.get(0);
        
        assertEquals("A",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(10000000, action.pot);
        assertEquals(0, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(2000000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(2, action.playersLeft);
        
        action = actions.get(1);
        
        assertEquals("B",  action.playerName);
        assertEquals(1,  action.playerPosition);
        assertEquals(12000000, action.pot);
        assertEquals(2000000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(4000000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(2, action.playersLeft);
        
        action = actions.get(2);
        
        assertEquals("A",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(16000000, action.pot);
        assertEquals(4000000, action.incomingBetOrRaise);
        assertEquals(2000000, action.playerAmtPutInPotThisRound);
        assertEquals(6000000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(2, action.playersLeft);
        
        action = actions.get(3);
        
        assertEquals("B",  action.playerName);
        assertEquals(1,  action.playerPosition);
        assertEquals(20000000, action.pot);
        assertEquals(6000000, action.incomingBetOrRaise);
        assertEquals(4000000, action.playerAmtPutInPotThisRound);
        assertEquals(8000000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(2, action.playersLeft);
        
        action = actions.get(4);
        
        assertEquals("A",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(24000000, action.pot);
        assertEquals( 8000000, action.incomingBetOrRaise);
        assertEquals( 6000000, action.playerAmtPutInPotThisRound);
        assertEquals(10000000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(2, action.playersLeft);
        
        action = actions.get(5);
        
        assertEquals("B",  action.playerName);
        assertEquals(1,  action.playerPosition);
        assertEquals(28000000, action.pot);
        assertEquals(10000000, action.incomingBetOrRaise);
        assertEquals(8000000, action.playerAmtPutInPotThisRound);
        assertEquals(12000000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(2, action.playersLeft);
        
        action = actions.get(6);
        
        assertEquals("A",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(32000000, action.pot);
        assertEquals(12000000, action.incomingBetOrRaise);
        assertEquals(10000000, action.playerAmtPutInPotThisRound);
        assertEquals(0, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(2, action.playersLeft);
        
        actions = roundStates[3].actions;
        
        assertEquals(3, actions.size());
        
        action = actions.get(0);
        
        assertEquals("A",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(34000000, action.pot);
        assertEquals(0, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(12000004, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE_ALL_IN, action.action);
        assertEquals(2, action.playersLeft);
        
        action = actions.get(1);
        
        assertEquals("B",  action.playerName);
        assertEquals(1,  action.playerPosition);
        assertEquals(46000004, action.pot);
        assertEquals(12000004, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(0, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(1, action.playersLeft);
    }
}