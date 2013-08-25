package pkr.history;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.PlayerAction.Action;

import com.google.common.base.Preconditions;

public class StatsComputer
{

     Logger log = LoggerFactory.getLogger(StatsComputer.class);
    
    public StatsComputer(List<FlopTurnRiverState[]> hands ) {

        stats = new StatsSession();
        
        for(int hand = 0; hand < hands.size(); ++hand )
        {
            FlopTurnRiverState[] ftrStates = hands.get(hand);
            log.debug("\nStats hand : {} line # : {}", hand+1, ftrStates[0].lineNumber);
            
            for(String playerName : ftrStates[0].players)
            {
                log.debug("Player {}", playerName);
                StatsSessionPlayer playerSesStat = getSessionStats(playerName);
                
                playerSesStat.totalHands++;
                
                handlePreflopStats(ftrStates, playerSesStat, playerName);
                
                handleRoundStats(ftrStates, playerSesStat, playerName);
                
                handleStats(ftrStates, playerSesStat, playerName);
            }
        }
        
        for(int hs = hands.size() - 1; hs >= 0; --hs)
        {
            List<String> p = hands.get(hs)[0].players;
            if (p.size() > 1)
            {
                stats.currentPlayerList = p;
                break;
            }
        }
        
        for(String playerName : stats.currentPlayerList)
        {
            StatsSessionPlayer pStats = stats.playerSessionStats.get(playerName);
            

        }
    }
    
    public StatsSession stats = null;
    
    private void handlePreflopStats(FlopTurnRiverState[] ftrStates, StatsSessionPlayer playerSesStat, String preFlopPlayer)
    {
        //Checking BB does not affect stats
        if (preFlopPlayer.equals(ftrStates[0].playerBB)
                && ftrStates[0].tableStakes == ftrStates[0].getCurrentBet(preFlopPlayer)
                && ftrStates[0].roundInitialBetter == null
                )
        {
            log.debug("Player {} is an unraised big  blind", preFlopPlayer);
            return;
        }
        
        
        
        int playerBet = ftrStates[0].getCurrentBet(preFlopPlayer);
        
        final boolean playerAllin = ftrStates[0].allInMinimum.containsKey(preFlopPlayer);
        
        
        
        final boolean isPreFlopRaiser = StringUtils.equals(ftrStates[0].roundInitialBetter, preFlopPlayer);
        
        if (!isPreFlopRaiser &&
                ftrStates[0].roundInitialBetter != null &&
                BooleanUtils.isNotTrue(ftrStates[0].hasFolded.get(preFlopPlayer))
                ) 
        {
            log.debug("Player {} called an initial raise", preFlopPlayer);
            playerSesStat.raisedPreflopDenom++;
            playerSesStat.notFoldRaisedPreflop++;
        } else if (BooleanUtils.isTrue(ftrStates[0].foldedToBetOrRaise.get(preFlopPlayer))) {
            playerSesStat.raisedPreflopDenom++;
            log.debug("Folded to a preflop raise {}", preFlopPlayer);
        }
        
        
        if (isPreFlopRaiser && !playerAllin)
        {
            playerSesStat.preFlopRaises ++;
            playerSesStat.preFlopRaiseTotalAmt += playerBet;
            log.debug("Player {} pre flop raised {}", preFlopPlayer, playerBet);
        }
        
        if (playerAllin && isPreFlopRaiser)
        {
            playerSesStat.preFlopTapis++;
        }
        
       
    }
    private void handleRoundStats(FlopTurnRiverState[] ftrStates, StatsSessionPlayer player, String playerName)
    {
        Preconditions.checkNotNull(ftrStates);
        Preconditions.checkArgument(ftrStates.length == 4);
        
        for(int r = 0; r < 3; ++r)
        {
        if (ftrStates[r+1] != null && 
                ftrStates[r+1].players != null && 
                ftrStates[r+1].players.size() > 1 && 
                ftrStates[r+1].players.contains(playerName))
        {
            
            log.debug("Player {} is in round {}", playerName, Statistics.roundToStr(r + 1));
            
            final boolean isInitialBetter = StringUtils.equals(ftrStates[r+1].roundInitialBetter, playerName);
            
            
            
                        
            final boolean hasReraised = (BooleanUtils.isTrue(ftrStates[r+1].hasReraised.get(playerName)));
            
            final boolean opened = ftrStates[r+1].amtToCall > 0 && !isInitialBetter; 
            if (opened)
            {
                
                //Make sure at least 1 raise / fold / call is true
                
                int check = 0;
                if (hasReraised)
                   ++check;
            
                if (BooleanUtils.isTrue(ftrStates[r+1].foldedToBetOrRaise.get(playerName)))
                    ++check;
                
                if (BooleanUtils.isTrue(ftrStates[r+1].calledABetOrRaise.get(playerName)))
                    ++check;
                
                if (BooleanUtils.isTrue(ftrStates[r+1].allInMinimum.containsKey(playerName)))
                    ++check;
                
               if (false) Preconditions.checkState(1 <= check && check <= 3,
                        "%s Player %s has not just bet/folded/called in a raised round %s.\n " +
                        " has bet [%s] has folded [%s]  has called [%s] has all in [%s]",
                        check,
                        playerName, r,
                        hasReraised,
                        ftrStates[r+1].foldedToBetOrRaise.get(playerName),
                        ftrStates[r+1].calledABetOrRaise.get(playerName),
                        ftrStates[r+1].allInMinimum.containsKey(playerName)
                        );
                        
            }
            
            final boolean unOpened = (ftrStates[r+1].amtToCall == 0 || isInitialBetter);
            final boolean playerHasBet = BooleanUtils.isTrue(ftrStates[r+1].hasBet.get(playerName));
            final boolean playerHasReraised = BooleanUtils.isTrue(ftrStates[r+1].hasReraised.get(playerName));
            
            
            
            
            
        }
        }
    }
    
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
                
                if (action.action == Action.RAISE && !ftrStates[round-1].hasFoldedArr[action.playerPosition])
                {
                    log.debug("Player {} dans position {} était l'agresseur de la tournée précédente {}",
                            action.playerName,
                            action.playerPosition,
                            round);
                    ftrStates[round].agresseur = action.playerName;
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
    
    /**
     * Déterminer si les all-ins était des calls ou des relances
     * @param ftrStates
     */
    private void defineAllins(FlopTurnRiverState[] ftrStates)
    {
        for(int round = 0; round <= 3; ++round)
        {
            FlopTurnRiverState ftr = ftrStates[round];
            if (ftr == null)
                continue;
            
            Action lastAction = null;
            for(int actionIndex = ftr.actions.size() - 1; actionIndex >= 0; --actionIndex)
            {
                PlayerAction action = ftr.actions.get(actionIndex);
                
                if (action.action == Action.ALL_IN)
                {
                    if (lastAction == null || lastAction == Action.CHECK) {
                        action.action = Action.CALL_ALL_IN;
                    } else if ( lastAction == Action.CALL )
                    {
                        action.action = Action.RAISE_ALL_IN;
                    } else if ( lastAction == Action.RAISE )
                    {
                        log.debug("All in at index, guessing it was a call");
                        action.action = Action.CALL_ALL_IN;
                    }
                          
                } else if ( action.action == Action.CALL ||
                            action.action == Action.RAISE ||
                            action.action == Action.CHECK)
                {
                    lastAction = action.action;
                }
            }
        }
    }
    
    private void handleStats(FlopTurnRiverState[] ftrStates, StatsSessionPlayer player, String playerName)
    {
        //Precompute common traits
       // defineAllins(ftrStates);
        
        calculateGlobalRaiseCount(ftrStates);
        getAgresseur(ftrStates);
        
        
        for(Map.Entry<String, iPlayerStatistic> entries : player.stats.entrySet())
        {
            entries.getValue().calculate(ftrStates);
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
