package pkr.history;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import pkr.history.PlayerAction.Action;

public class StatsComputer
{

     Logger log = LoggerFactory.getLogger(StatsComputer.class);
    
    public StatsComputer(HandInfoCollector hands ) {

        stats = new StatsSession();
        
        for(int hand = 0; hand < hands.listHandInfo.size(); ++hand )
        {
            FlopTurnRiverState[] ftrStates = hands.listHandInfo.get(hand).roundStates;
            log.debug("\nStats hand : {} line # : {}", hand+1, hands.listHandInfo.get(hand).startingLine);
            
            for(String playerName : ftrStates[0].players)
            {
                log.debug("Player {}", playerName);
                StatsSessionPlayer playerSesStat = getSessionStats(playerName);
                
                playerSesStat.totalHands++;
                                                
                handleStats(hands.listHandInfo.get(hand), playerSesStat, playerName);
            }
        }
        
        for(int hs = hands.listHandInfo.size() - 1; hs >= 0; --hs)
        {
            List<String> p = hands.listHandInfo.get(hs).roundStates[0].players;
            if (p.size() > 1)
            {
                stats.currentPlayerList = p;
                break;
            }
        }
        
       
    }
    
    public StatsSession stats = null;
    
    
    
    
    void getAgresseur(FlopTurnRiverState[] ftrStates)
    {
        nextRound:
        for(int round = 1; round <= 3; ++round)
        {
            if (ftrStates[round] == null)
                return;
            
            List<PlayerAction> actions = ftrStates[round-1].actions;
            
            for(int actionIdx = actions.size() - 1; actionIdx >= 0; --actionIdx)
            {
                PlayerAction action = actions.get(actionIdx);
                
                if (action.action == Action.RAISE && !ftrStates[round-1].hasFolded[action.playerPosition])
                {
                    log.debug("Player {} dans position {} était l'agresseur de la tournée précédente {}",
                            action.playerName,
                            action.playerPosition,
                            round);
                    ftrStates[round].prevTourneeAgresseur = action.playerName;
                    continue nextRound;
                }
            }
            
            log.debug("Il n'y avais pas d'agresseur dans la tournée précédente {}", round);
        }
        
       
    }
    
    private void calculateGlobalRaiseCount(FlopTurnRiverState[] ftrStates)
    {
        
        
        for(int round = 0; round <= 3; ++round)
        {
            int globalRaiseCount = 0;
            
            FlopTurnRiverState ftr = ftrStates[round];
            if (ftr == null)
                continue;
            
            int playersLeft = ftr.players.size();
            int folded = 0;
            int allins = 0;
            
            for(int actionIndex = 0; actionIndex < ftr.actions.size(); ++actionIndex)
            {
                PlayerAction action = ftr.actions.get(actionIndex);
                
                //Preconditions.checkState(!action.playerName.equals(playerName));
                action.globalRaiseCount = globalRaiseCount;
                action.playersFolded = folded;
                action.playersLeft = playersLeft;
                action.playersAllIn = allins;
                
                log.debug("action idx {} player {} raise count now {}", actionIndex, action.playerName, globalRaiseCount);
                
                if (action.action == Action.RAISE || action.action == Action.RAISE_ALL_IN)
                {
                    ++globalRaiseCount;                
                }
                
                if (action.action == Action.FOLD)
                {
                    ++folded;
                    --playersLeft;
                }
                
                if (action.action == Action.ALL_IN || action.action == Action.CALL_ALL_IN || action.action == Action.RAISE_ALL_IN)
                {
                    ++allins;
                    --playersLeft;
                }
            }
        }   
    }
    
   
    private void calculateWinAction(HandInfo handInfo)
    {
        
        PlayerAction winAction = null;
        
        for(int round = 3; round >= 0; --round)
        {
           
            FlopTurnRiverState ftr = handInfo.roundStates[round];
            if (ftr == null)
                continue;
            
             if (winAction == null)
            {
                winAction = ftr.actions.get(ftr.actions.size()-1);
                Preconditions.checkState(winAction.action == Action.WON);
                handInfo.winner = winAction;
                return;
            }
            
        }   
    }
    
    private void handleStats(HandInfo handInfo, StatsSessionPlayer player, String playerName)
    {
        //Precompute common traits
       // defineAllins(ftrStates);
        
        calculateWinAction(handInfo);
        
        calculateGlobalRaiseCount(handInfo.roundStates);
        getAgresseur(handInfo.roundStates);
        
        
        for(Map.Entry<String, iPlayerStatistic> entries : player.stats.entrySet())
        {
            entries.getValue().calculate(handInfo);
        }
    }
    
    StatsSessionPlayer getSessionStats(String playerName)
    {
        StatsSessionPlayer ret = stats.playerSessionStats.get(playerName);
        
        if (ret == null)
        {
            ret = new StatsSessionPlayer(playerName);
            stats.playerSessionStats.put(playerName, ret);
        }
        
        return ret;
    }

}
