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


import pkr.history.Parser.State;

public class FlopTurnRiverState implements State
{
    static Logger log = LoggerFactory.getLogger(Parser.class);
    
    List<String> players;
    int pot;
    
    final static int MAX_PLAYERS = 5;
    
    int amtToCall = 0;
    
    Map<String , Boolean> hasFolded;
    Map<String, Integer> playerBets;
    
    
    
    public FlopTurnRiverState(List<String> players, int pot) {
        super();
        this.players = players;
        this.pot = pot;
        
        hasFolded = Maps.newHashMap();
        playerBets = Maps.newHashMap();
    }
    
    private State getNextState(String line) {
        log.debug("Tous les joueurs pris en compte");
        List<String> playersInOrder = Lists.newArrayList();
        
        
        
        for(int i = 0; i < players.size() ; ++i) {
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

    private boolean potsGood() 
    {
        for(int i = 0; i < players.size(); ++i)
        {
            String playerName = players.get(i);
            if (BooleanUtils.isTrue(hasFolded.get(playerName))) {
                continue;
            }
            
            Integer playerBet = playerBets.get(playerName);
            
            if (playerBet == null) 
            {
                log.debug("Pot is not good.  Player {} idx {} has not acted",
                        playerName, i);
                return false;
            }
            
            if (playerBet < amtToCall)
            {
                log.debug("Pot is not good.  Player {} idx {} bet only {}",
                        playerName, i, playerBet);
                
                return false;
            }
            
            Preconditions.checkState(amtToCall == playerBet);
        }
        
        return true;
    }


    public State handleLine(String line) {
        if (Parser.isIgnoreLine(line))
            return this;
        
        log.debug("\nFlopTurnRiverState line [{}]\n", line);
       
        String regexSuivi = "(.*) a suivi de ([\\d ]+)\\.";
        
        Pattern patSuivi = Pattern.compile(regexSuivi);
        
        Matcher matSuivi = patSuivi.matcher(line);
        
        if (matSuivi.matches()) {
            
            String playerName = matSuivi.group(1);
            String betAmtStr = matSuivi.group(2);
            
            int betAmt = Integer.parseInt(betAmtStr.replace(" ", ""), 10);
            log.debug("Correspondance player {} bet {} = {}", playerName, betAmtStr, betAmt);
            
            if (amtToCall == 0) 
            {
                amtToCall = betAmt;
                log.debug("Table mise à {}", amtToCall);
            } else {
                Preconditions.checkState(betAmt == amtToCall);
            }
            
            playerBets.put(playerName, betAmt);
            
            if (players.contains(playerName)) 
            {
                if (potsGood()) 
                    return getNextState(line);
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
            String playerName = matParle.group(1);
            /*
            
            
            //Should be first player to act
            int playerIndex = players.indexOf(playerName);
            */
            if (playerBets.containsKey(playerName)) 
            {
                log.warn("Next round appears to have started; maybe a player left?");
                return new UnitializedState();
            }
            //Preconditions.checkState(!playerBets.containsKey(playerName));
            
            
            
            playerBets.put(playerName, 0);
            
            if (potsGood())
            {
                //La ligne actuel était prise en compte
                return getNextState("");
            }
            
            return this;
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
            
            Preconditions.checkState(betAmt > amtToCall);
            
            amtToCall = betAmt;
            
            playerBets.put(playerName, betAmt);
            
            return this;
        }
        
        String regexCouche = "(.*) se couche.";
        Pattern patCouche = Pattern.compile(regexCouche);
        Matcher matCouche = patCouche.matcher(line);
        
        if (matCouche.matches())
        {
            String playerName = matCouche.group(1);
            
            Preconditions.checkState(players.contains(playerName));
            
            hasFolded.put(playerName, true);
            
            if (potsGood()) 
            {
                return getNextState("");
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
        
        Preconditions.checkState(false, line);
        return this;
        
    }
}
