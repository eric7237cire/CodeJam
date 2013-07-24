package pkr.history;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.Parser.State;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DiscoveryPreflopState implements State
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
    
    private State getNextState(String line) {
        log.debug("Tous les joueurs pris en compte");
        List<String> playersInOrder = Lists.newArrayList();
        
        //add sb
        String playerSB = players.get(players.size()-2);
        String playerBB = players.get(players.size()-1);
        
        if (BooleanUtils.isNotFalse(hasFolded.get(playerSB))) {
            log.debug("Adding small blind {}", playerSB);
            playersInOrder.add(playerSB);
        }
        
        if (BooleanUtils.isNotFalse(hasFolded.get(playerBB))) {
            log.debug("Adding big blind {}", playerBB);
            playersInOrder.add(playerBB);
        }
        
        for(int i = 0; i < players.size() - 2; ++i) {
            String playerName = players.get(i);
            if (BooleanUtils.isNotFalse(hasFolded.get(playerName))) {
                log.debug("Adding player {}", playerName);
                playersInOrder.add(playerName);
            }
        }
        
        FlopTurnRiverState ftrState = new FlopTurnRiverState(playersInOrder, pot);
        ftrState.handleLine(line);
        return ftrState;
    }
    
    @Override
    public State handleLine(String line) {
        if (Parser.isIgnoreLine(line))
            return this;
        
        log.debug("\nDiscoveryPreflopState line [{}]\n ", line);
        String regexSuivi = "(.*) a suivi de ([\\d ]+)\\.";
        
        Pattern patSuivi = Pattern.compile(regexSuivi);
        
        Matcher matSuivi = patSuivi.matcher(line);
        
        if (matSuivi.matches()) {
            
            String playerName = matSuivi.group(1);
            String betAmtStr = matSuivi.group(2);
            
            int betAmt = Integer.parseInt(betAmtStr.replace(" ", ""), 10);
            log.debug("Correspondance player {} bet {} = {}", playerName, betAmtStr, betAmt);
            
            if (amtToEnter == 0) 
            {
                amtToEnter = betAmt;
                log.debug("Table mise à {}", amtToEnter);
            } else {
                Preconditions.checkState(betAmt == amtToEnter);
            }
            
            playerBets.put(playerName, betAmt);
            
            if (players.contains(playerName)) 
            {
                if (potsGood()) 
                    return getNextState("");
            } else {
                log.debug("Player [{}] ajouté avec index {} ", playerName, players.size());
                players.add(playerName);                
            }
            
            return this;
        }
        
        String regexParle = "(.*) a parlé.";
        Pattern patParle = Pattern.compile(regexParle);
        Matcher matParle = patParle.matcher(line);
        
        if (matParle.matches()) 
        {
            /*
            String playerName = matParle.group(1);
            
            //Should be first player to act
            int playerIndex = players.indexOf(playerName);
            */
            Preconditions.checkState(potsGood());
            
            return getNextState(line);
        }
        
        String regexRelance = "(.*) a relancé de ([\\d ]+).";
        Pattern patRelance = Pattern.compile(regexRelance);
        Matcher matRelance = patRelance.matcher(line);
        
        if (matRelance.matches())
        {
            String playerName = matRelance.group(1);
            String betAmtStr = matRelance.group(2);
            
            int betAmt = Integer.parseInt(betAmtStr.replace(" ", ""), 10);
            log.debug("Relance player {} bet {} = {}", playerName, betAmtStr, betAmt);
            
            Preconditions.checkState(amtToEnter > 0);
            Preconditions.checkState(betAmt > amtToEnter);
            
            amtToEnter = betAmt;
            
            if (players.contains(playerName)) 
            {
                //TODO pas sûr c'est correct
                return getNextState(line);
            } else {
                log.debug("Player [{}] ajouté avec index {} ", playerName, players.size());
                players.add(playerName);
                playerBets.put(playerName, betAmt);
            }
            
            return this;
        }
        
        String regexCouche = "(.*) se couche.";
        Pattern patCouche = Pattern.compile(regexCouche);
        Matcher matCouche = patCouche.matcher(line);
        
        if (matCouche.matches())
        {
            String playerName = matCouche.group(1);
            
            if (players.contains(playerName)) 
            {
                if (potsGood())
                    return getNextState(line);
            } else {
                log.debug("Player [{}] ajouté avec index {} ", playerName, players.size());
                players.add(playerName);
                hasFolded.put(playerName, true);
            }
            
            return this;
        }
        
        String regexGagne = "(.*) gagne.";
        Pattern patGagne = Pattern.compile(regexGagne);
        Matcher matGagne = patGagne.matcher(line);
        
        if (matGagne.matches())
        {
            String playerName = matGagne.group(1);
            log.debug("{} gagne", playerName);
            return new UnitializedState();
        }
        
        String regexTapis = "(.*) a fait tapis.";
        Pattern patTapis = Pattern.compile(regexTapis);
        Matcher matTapis = patTapis.matcher(line);
        
        if (matTapis.matches())
        {
            String playerName = matTapis.group(1);
            log.debug("{} Tapis", playerName);
            return new UnitializedState();
        }
        
        Preconditions.checkState(false, line);
        return this;
    }

}