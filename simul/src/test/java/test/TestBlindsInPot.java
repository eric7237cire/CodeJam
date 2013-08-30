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
public class TestBlindsInPot
{

    
    private static Logger log = LoggerFactory.getLogger(TestTapis.class);
   

    @Test
    public void testBlindsInPot1() throws Exception
    {
        HandInfoCollector results = TestPreflopStats.getList("testBlindsInPot1");
        
        assertEquals(1, results.listHandInfo.size());
        
        FlopTurnRiverState[] roundStates = results.listHandInfo.get(0).roundStates;
        
        StatsSession stats = Parser.computeStats(results);
        
       // StatsSessionPlayer pStats = stats.playerSessionStats.get("Elif Hatice");
        
        List<PlayerAction> actions = roundStates[0].actions;
        
        assertEquals(5, actions.size());
        
        PlayerAction action = actions.get(0);
        
        assertEquals("Elif Hatice",  action.playerName);
        assertEquals(0,  action.playerPosition);
        assertEquals(600000, action.pot);
        assertEquals(00000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(1200000, action.amountRaised);
        assertEquals(PlayerAction.Action.RAISE, action.action);
        assertEquals(5, action.playersLeft);
        
        action = actions.get(1);
        

        assertEquals("Fatin",  action.playerName);
        assertEquals(1,  action.playerPosition);
        assertEquals(1800000, action.pot);
        assertEquals(1200000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(5, action.playersLeft);
        
        action = actions.get(2);
        
        assertEquals("Hydz",  action.playerName);
        assertEquals(2,  action.playerPosition);
        assertEquals(3000000, action.pot);
        assertEquals(1200000, action.incomingBetOrRaise);
        assertEquals(0, action.playerAmtPutInPotThisRound);
        assertEquals(0, action.amountRaised);
        assertEquals(PlayerAction.Action.FOLD, action.action);
        assertEquals(5, action.playersLeft);
        
        action = actions.get(3);
        
        assertEquals("Eric",  action.playerName);
        assertEquals(3,  action.playerPosition);
        assertEquals(3000000, action.pot);
        assertEquals(1200000, action.incomingBetOrRaise);
        assertEquals(200000, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.FOLD, action.action);
        assertEquals(4, action.playersLeft);
        
        action = actions.get(4);
        
        assertEquals("Turgay",  action.playerName);
        assertEquals(4,  action.playerPosition);
        assertEquals(3000000, action.pot);
        assertEquals(1200000, action.incomingBetOrRaise);
        assertEquals(400000, action.playerAmtPutInPotThisRound);
        assertEquals(00000, action.amountRaised);
        assertEquals(PlayerAction.Action.CALL, action.action);
        assertEquals(3, action.playersLeft);

        
    }
}