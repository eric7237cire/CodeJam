package pkr.history;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DiscoveryPreflopState implements ParserListener
{
    static Logger log = LoggerFactory.getLogger(Parser.class);
    static Logger logOutput = LoggerFactory.getLogger("handOutput");
    
    List<String> players;
    int pot;
    
    final static int MAX_PLAYERS = 5;
    
    int amtToEnter = 0;
    int currentPlayer = 0;
    
    Map<String , Boolean> hasFolded;
    Map<String, Integer> playerBets;
    
    DiscoveryPreflopState() 
    {
        players = Lists.newArrayList();
        pot = 0;
        
        hasFolded = Maps.newHashMap();
        playerBets = Maps.newHashMap();
        
        logOutput.debug("\n***********************************************");
        logOutput.debug("Starting new Hand");
    }
    
    private boolean potsGood() 
    {
        for(int i = 0; i < players.size(); ++i)
        {
            String playerName = players.get(i);
            if (BooleanUtils.isTrue(hasFolded.get(playerName))) {
                continue;
            }
            
            Integer playerBet = playerBets.get(playerName);
            
            Preconditions.checkNotNull(playerBet);
            
            if (playerBet < amtToEnter)
            {
                log.debug("Pot is not good.  Player {} idx {} bet only {}",
                        playerName, i, playerBet);
                
                return false;
            }
            
            Preconditions.checkState(amtToEnter == playerBet);
        }
        
        return true;
    }
    
    /**
     * 
     * @return true if playerName has been seen
     */
    private boolean incrementPlayer(String playerName)
    {
        if (!players.contains(playerName))
        {
            log.debug("Player [{}] ajouté avec index {} ", playerName, players.size());
            players.add(playerName);
            currentPlayer = players.size() - 1;
            return false;
        }
              
        
        int loopCheck = 0;
        
        while(true)
        {
            ++currentPlayer;
            ++loopCheck;
            
            if (loopCheck > 15) {
                Preconditions.checkState(false, "Loop all players folded");
            }
            if (currentPlayer == players.size()) {
                currentPlayer = 0;
            }
            
            if (BooleanUtils.isNotTrue(hasFolded.get(playerName))) {
                
                Preconditions.checkState(players.get(currentPlayer).equals(playerName));
                
                return true;
            }
        }
        
        
    }
    
    private ParserListener getNextState(boolean replayline) {
        log.debug("Preflop fini : Tous les joueurs pris en compte");
        List<String> playersInOrder = Lists.newArrayList();
        
        //add sb
        String playerSB = players.get(players.size()-2);
        String playerBB = players.get(players.size()-1);
        
        if (BooleanUtils.isNotTrue(hasFolded.get(playerSB))) {
            log.debug("Adding small blind {}", playerSB);
            playersInOrder.add(playerSB);
        }
        
        if (BooleanUtils.isNotTrue(hasFolded.get(playerBB))) {
            log.debug("Adding big blind {}", playerBB);
            playersInOrder.add(playerBB);
        }
        
        for(int i = 0; i < players.size() - 2; ++i) {
            String playerName = players.get(i);
            if (BooleanUtils.isNotTrue(hasFolded.get(playerName))) {
                log.debug("Adding player {}", playerName);
                playersInOrder.add(playerName);
            }
        }
        
        FlopTurnRiverState ftrState = new FlopTurnRiverState(playersInOrder, pot, replayline, 0);
        
        return ftrState;
    }
    
    
      

    @Override
    public ParserListener handleSuivi(String playerName, int betAmt)
    {
        boolean seenPlayer = incrementPlayer(playerName);
        printHandHistory("Call");
        
        if (amtToEnter == 0) 
        {
            amtToEnter = betAmt;
            log.debug("Table mise à {}", amtToEnter);
        } else {
            Preconditions.checkState(betAmt == amtToEnter);
        }
        
        log.debug("Player entering / staying in.  player {} bet {}", playerName,  betAmt);
        
        pot = FlopTurnRiverState.updatePlayerBet(playerBets, playerName, betAmt, pot);
        
        if (seenPlayer && potsGood())
        {
           return getNextState(false);
        } 
        
        return this;
    }

    @Override
    public ParserListener handleRelance(String playerName, int betAmt)
    {
        boolean seenPlayer = incrementPlayer(playerName);
        printHandHistory("Raise");
        
        Preconditions.checkState(betAmt > amtToEnter);
        
        amtToEnter = betAmt;
        
        pot = FlopTurnRiverState.updatePlayerBet(playerBets, playerName, betAmt, pot);
        
        playerBets.put(playerName, betAmt);
        
        
        return this;
    }

    @Override
    public ParserListener handleCoucher(String playerName)
    {
        boolean seenPlayer = incrementPlayer(playerName);
        printHandHistory("Fold");
        
        hasFolded.put(playerName, true);
        
        if (seenPlayer && potsGood()) 
        {
            return getNextState(false);
        } 
        
        return this;
    }

    @Override
    public ParserListener handleParole(String playerName)
    {
        Preconditions.checkState(potsGood());
        
        return getNextState(true);
    }

    @Override
    public ParserListener handleTapis(String playerName)
    {
        return null;
    }

    @Override
    public ParserListener handleShowdown(String playerName, int pot)
    {
        return null;
    }

    @Override
    public ParserListener handleGagne(String playerName)
    {
        log.debug("{} gagne", playerName);
        return null;
    }

    @Override
    public boolean replayLine()
    {
        return false;
    }

    private void printHandHistory(String action)
    {
        
        logOutput.debug("Player {} position {} Action {}", 
                players.get(currentPlayer), 1+currentPlayer, action);
        
        Integer playerBet = playerBets.get(players.get(currentPlayer)); 
        if (playerBet != null && amtToEnter > playerBet)
        {
            int diff = amtToEnter - playerBet;
            double perc = 1.0 * diff / (pot + diff);
            double ratio = pot * 1.0 / diff;
            
            log.debug("Amount to call {}.  {}%   {} 1 to {}", 
                    amtToEnter, FlopTurnRiverState.df2.format(perc), FlopTurnRiverState.df2.format(ratio));
        }
    }

}