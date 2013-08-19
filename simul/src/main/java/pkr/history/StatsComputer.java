package pkr.history;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.StatsSessionPlayer.RoundStats;

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
            }
        }
        
        if (hands.size() > 0)
            stats.currentPlayerList = hands.get(hands.size()-1)[0].players;
        
        for(String playerName : stats.currentPlayerList)
        {
            StatsSessionPlayer pStats = stats.playerSessionStats.get(playerName);
            
            for(int r = 0; r < 3; ++r)
            {
                pStats.roundStats[r].avgBetToPot /= pStats.roundStats[r].bets;
                pStats.roundStats[r].avgFoldToBetToPot /= pStats.roundStats[r].folded;
            }
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
        
        final boolean playerAllin = ftrStates[0].allInBet.containsKey(preFlopPlayer);
        
        playerSesStat.vpipDenom++;
        
        if ( (!preFlopPlayer.equals(ftrStates[0].playerBB) && playerBet >= ftrStates[0].tableStakes)
                ||
                (preFlopPlayer.equals(ftrStates[0].playerBB) && playerBet > ftrStates[0].tableStakes)
                ||
                //Supposons qu'un tapis est une relance
                playerAllin
                )
        {
            log.debug("Player {} entered pot for VPIP.  table stakes {}  player bet {} bb {}", preFlopPlayer,
                    ftrStates[0].tableStakes,
                    ftrStates[0].getCurrentBet(preFlopPlayer),
                    ftrStates[0].playerBB
                    );
            playerSesStat.vpipNumerator++;
        }
        
        final boolean isPreFlopRaiser = StringUtils.equals(ftrStates[0].roundInitialBetter, preFlopPlayer);
        
        if (!isPreFlopRaiser &&
                ftrStates[0].roundInitialBetter != null &&
                BooleanUtils.isNotTrue(ftrStates[0].hasFolded.get(preFlopPlayer))
                ) 
        {
            log.debug("Player {} called an initial raise", preFlopPlayer);
            playerSesStat.callOpenDenom++;
            playerSesStat.callOpenNumerator++;
        } else if (BooleanUtils.isTrue(ftrStates[0].foldedToBetOrRaise.get(preFlopPlayer))) {
            playerSesStat.callOpenDenom++;
            log.debug("Folded to a preflop raise {}", preFlopPlayer);
        }
        
        
        if (isPreFlopRaiser && !playerAllin)
        {
            playerSesStat.preFlopRaises ++;
            playerSesStat.preFlopRaiseTotalAmt += playerBet;
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
            player.roundStats[r].seen++;
            
            log.debug("Player {} is in round {}", playerName, FlopTurnRiverState.roundToStr(r + 1));
            
            final boolean isInitialBetter = StringUtils.equals(ftrStates[r+1].roundInitialBetter, playerName);
            
            RoundStats rs = player.roundStats[r];
            
            if (ftrStates[r+1].amtToCall == 0)
            {
                rs.checkedThrough++;
            }
            
            final boolean hasReraised = (BooleanUtils.isTrue(ftrStates[r+1].hasReraised.get(playerName)));
            
            final boolean opened = ftrStates[r+1].amtToCall > 0 && !isInitialBetter; 
            if (opened)
            {
                rs.openedBySomeoneElse++;
                
                //Make sure at least 1 raise / fold / call is true
                
                int check = 0;
                if (hasReraised)
                   ++check;
            
                if (BooleanUtils.isTrue(ftrStates[r+1].foldedToBetOrRaise.get(playerName)))
                    ++check;
                
                if (BooleanUtils.isTrue(ftrStates[r+1].calledABetOrRaise.get(playerName)))
                    ++check;
                
                if (BooleanUtils.isTrue(ftrStates[r+1].allInBet.containsKey(playerName)))
                    ++check;
                
                Preconditions.checkState(1 <= check && check <= 3,
                        "%s Player %s has not just bet/folded/called in a raised round %s.\n " +
                        " has bet [%s] has folded [%s]  has called [%s] has all in [%s]",
                        check,
                        playerName, r,
                        hasReraised,
                        ftrStates[r+1].foldedToBetOrRaise.get(playerName),
                        ftrStates[r+1].calledABetOrRaise.get(playerName),
                        ftrStates[r+1].allInBet.containsKey(playerName)
                        );
                        
            }
            
            final boolean unOpened = (ftrStates[r+1].amtToCall == 0 || isInitialBetter);
            final boolean playerHasBet = BooleanUtils.isTrue(ftrStates[r+1].hasBet.get(playerName));
            final boolean playerHasReraised = BooleanUtils.isTrue(ftrStates[r+1].hasReraised.get(playerName));
            
            if (unOpened)
            {
                rs.unopened++;
            }
            
            if (BooleanUtils.isTrue(ftrStates[r+1].calledABetOrRaise.get(playerName)))
            {
                if (opened)
                {
                    log.debug("Player {} called the opened pot", playerName);
                    rs.calls++;
                } else {
                    log.debug("Player {} called a reraise in an unopened pot", playerName);
                    rs.callReraise++;
                }
                
                
                //Preconditions.checkState(opened, playerName);
            }
            
            if (BooleanUtils.isTrue(ftrStates[r+1].foldedToBetOrRaise.get(playerName)))
            {
                if (unOpened)
                {
                    log.debug("Player {} folded to a reraise", playerName);
                    if (playerHasBet)
                        rs.betFold++;
                    else
                    {
                        rs.checkFold++;
                    }
                } else {
                    log.debug("Player {} folded an opened pot", playerName);
                    rs.folded++;
                }
                rs.avgFoldToBetToPot += ftrStates[r+1].foldToBetSize.get(playerName); 
            }
            
            if (playerHasBet)
            {
                rs.bets++;
                rs.avgBetToPot += ftrStates[r+1].betToPotSize.get(playerName);
            }
            
            if (BooleanUtils.isTrue(ftrStates[r+1].allInBet.containsKey(playerName)))
            {
                if (unOpened)
                {
                    log.debug("Player {} all in unopened", playerName);
                    rs.betAllIn++;
                }
                else
                    rs.raiseCallAllIn++;
            }
            
            if (playerHasReraised)
            {
                if (unOpened)
                {
                    log.debug("Player {} has reraised unopened", playerName);
                    rs.reRaiseUnopened++;
                } else {
                    rs.reRaiseOpened++;
                }
            }
            
            if (BooleanUtils.isTrue(ftrStates[r+1].hasChecked.get(playerName)))
            {
                if (unOpened)
                    rs.checksUnopened++;
                else
                {
                    log.debug("Player {} checked the opened pot", playerName);
                    rs.checksOpened++;
                }
            }
            
            if (BooleanUtils.isTrue(ftrStates[r+1].hasChecked.get(playerName)) &&
                    playerHasReraised)
            {
                rs.checkRaises++;
            }
        }
        }
    }
    
    
    StatsSessionPlayer getSessionStats(String playerName)
    {
        StatsSessionPlayer ret = stats.playerSessionStats.get(playerName);
        
        if (ret == null)
        {
            ret = new StatsSessionPlayer();
            stats.playerSessionStats.put(playerName, ret);
        }
        
        return ret;
    }

}
