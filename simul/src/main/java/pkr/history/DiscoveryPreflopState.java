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
    
    List<String> players;
    int pot;
    
    final static int MAX_PLAYERS = 5;
    
    int amtToEnter = 0;
    
    Map<String , Boolean> hasFolded;
    Map<String, Integer> playerBets;
    
    DiscoveryPreflopState() 
    {
        players = Lists.newArrayList();
        pot = 0;
        
        hasFolded = Maps.newHashMap();
        playerBets = Maps.newHashMap();
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
        
        FlopTurnRiverState ftrState = new FlopTurnRiverState(playersInOrder, pot, replayline);
        
        return ftrState;
    }
    
    
      

    @Override
    public ParserListener handleSuivi(String playerName, int betAmt)
    {
        if (amtToEnter == 0) 
        {
            amtToEnter = betAmt;
            log.debug("Table mise à {}", amtToEnter);
        } else {
            Preconditions.checkState(betAmt == amtToEnter);
        }
        
        log.debug("Player entering / staying in.  player {} bet {}", playerName,  betAmt);
        
        pot = FlopTurnRiverState.updatePlayerBet(playerBets, playerName, betAmt, pot);
        
        if (players.contains(playerName)) 
        {
            if (potsGood()) 
                return getNextState(false);
        } else {
            log.debug("Player [{}] ajouté avec index {} ", playerName, players.size());
            players.add(playerName);                
        }
        
        return this;
    }

    @Override
    public ParserListener handleRelance(String playerName, int betAmt)
    {
        Preconditions.checkState(betAmt > amtToEnter);
        
        amtToEnter = betAmt;
        
        pot = FlopTurnRiverState.updatePlayerBet(playerBets, playerName, betAmt, pot);
        
        if (!players.contains(playerName)) 
        {
            log.debug("Player [{}] ajouté avec index {} ", playerName, players.size());
            players.add(playerName);
            playerBets.put(playerName, betAmt);
        }
        
        return this;
    }

    @Override
    public ParserListener handleCoucher(String playerName)
    {
        hasFolded.put(playerName, true);
        
        if (players.contains(playerName)) 
        {
            if (potsGood())
                return getNextState(false);
        } else {
            log.debug("couché Player [{}] ajouté avec index {} ", playerName, players.size());
            players.add(playerName);
            
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

    

}