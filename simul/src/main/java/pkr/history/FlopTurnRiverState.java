package pkr.history;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.text.NumberFormatter;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class FlopTurnRiverState implements ParserListener
{
    static Logger log = LoggerFactory.getLogger(Parser.class);
    static Logger logOutput = LoggerFactory.getLogger("handOutput");
    
    List<String> players;
    int pot;
    
    final static int MAX_PLAYERS = 5;
    
    int amtToCall = 0;
    
    Map<String , Boolean> hasFolded;
    Map<String, Integer> playerBets;
    
    
    int currentPlayer = 0;
    
    boolean replayLine = false;
    int round;
    
    
    public FlopTurnRiverState(List<String> players, int pot, boolean replayLine, int round) {
        super();
        this.players = players;
        this.pot = pot;
        this.replayLine = replayLine;
        this.round = round;
        
        hasFolded = Maps.newHashMap();
        playerBets = Maps.newHashMap();
        
        log.debug("Nouveau round.  Players {} pot {}", players.size(), pot);
        
       // logOutput.debug("\n***********************************************");
        logOutput.debug("\nStarting round {} with {} players.  Pot is ${}\n",
                round == 0 ? "FLOP" : (round == 1 ? "TURN" : "RIVER"),
                        players.size(),
                        moneyFormat.format(pot));
                        
        
    }
    
    public static int updatePlayerBet(Map<String, Integer> playerBets, String playerName, int playerBet, int pot) 
    {
        if (playerBets.containsKey(playerName)) {
           // log.debug("Player suit une relance");
            pot -= playerBets.get(playerName);
        }
        
        pot += playerBet;
        
        playerBets.put(playerName, playerBet);
        
        log.debug("After {} {} pot = {}",  playerName, playerBet, pot);
        
        return pot;
    }
    
    private ParserListener getNextState(boolean replayLine) {
        log.debug("Tous les joueurs pris en compte");
        List<String> playersInOrder = Lists.newArrayList();
        
        
        
        for(int i = 0; i < players.size() ; ++i) {
            String playerName = players.get(i);
            if (BooleanUtils.isNotTrue(hasFolded.get(playerName))) {
                log.debug("Adding player {}", playerName);
                playersInOrder.add(playerName);
            }
        }
        
        FlopTurnRiverState ftrState = new FlopTurnRiverState(playersInOrder, pot, replayLine, 1+round);
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

    private void incrementPlayer(String playerName)
    {
        Preconditions.checkState(players.get(currentPlayer).equals(playerName));
        
        while(true)
        {
            ++currentPlayer;
            if (currentPlayer == players.size()) {
                currentPlayer = 0;
            }
            
            if (BooleanUtils.isNotTrue(hasFolded.get(playerName))) {
                return;
            }
        }
    }
    
    public final static DecimalFormat df2;
    public final static DecimalFormat moneyFormat;
    
    static {
        
        df2 = new DecimalFormat("0.##");
        df2.setRoundingMode(RoundingMode.HALF_UP);
        df2.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        moneyFormat = new DecimalFormat("###,###", symbols);
    }
    
    private void printHandHistory(String action)
    {
        printHandHistory(action, 0);
    }
    private void printHandHistory(String action, int raiseAmt)
    {
        
        logOutput.debug("** Player {} position {} Action {}", 
                players.get(currentPlayer), 1+currentPlayer, action);
        
        Integer playerBet = playerBets.get(players.get(currentPlayer)); 
        if (playerBet == null)
            playerBet = 0;
        
        if (amtToCall > playerBet)
        {
            int diff = amtToCall - playerBet;
            double perc = 100.0 * diff / (pot + diff);
            double ratio = pot * 1.0 / diff; 
            
            double outsOne = perc / 2;
            
            double betSizeToPot = 1.0 * diff / pot;
            //% must be ahead
            double callBluff = 100*betSizeToPot / (1+betSizeToPot);
            
            
            logOutput.debug("Amount to call {} for pot {}.  Pot ratio : {}%  or 1 to {}  or {} outs", 
                    moneyFormat.format(amtToCall), moneyFormat.format(pot),
                    df2.format(perc), df2.format(ratio), df2.format(outsOne));
            logOutput.debug("Calling a bluff % chance must be ahead {}%", 
                    df2.format(callBluff));
        }
        
        if (raiseAmt > playerBet)
        {
            int diff = raiseAmt - playerBet;
            double betSizeToPot = 1.0 * diff / pot;
            double bluff = 100.0*(betSizeToPot) / (1+betSizeToPot);
            
            logOutput.debug("bluff % chance everyone must fold {}%", 
                     df2.format(bluff));
        }
    }

    @Override
    public ParserListener handleSuivi(String playerName, int betAmt)
    {
        log.debug("Suivi player {} bet {} ; pot {}", playerName, betAmt, pot);
        
        Preconditions.checkState(amtToCall > 0); 
        Preconditions.checkState(betAmt == amtToCall);
        
        incrementPlayer(playerName);
        printHandHistory("Call");
        
        pot = updatePlayerBet(playerBets, playerName, betAmt, pot);
        
        Preconditions.checkState(players.contains(playerName));
        
        
        if (potsGood()) 
        {
            return getNextState(false);
        }
    
        return this;
    }

    @Override
    public ParserListener handleRelance(String playerName, int betAmt)
    {
        Preconditions.checkState(betAmt > amtToCall);
        
        incrementPlayer(playerName);
        printHandHistory("Raise $" + moneyFormat.format(betAmt), betAmt);
        
        amtToCall = betAmt;
        
        pot = updatePlayerBet(playerBets, playerName, betAmt, pot);
        
        return this;
    }

    @Override
    public ParserListener handleCoucher(String playerName)
    {
        Preconditions.checkState(players.contains(playerName));
        
        printHandHistory("Fold");
        incrementPlayer(playerName);
        
        hasFolded.put(playerName, true);
        
        if (potsGood()) 
        {
            return getNextState(false);
        }
        
        return this;
    }

    @Override
    public ParserListener handleParole(String playerName)
    {
        if (playerBets.containsKey(playerName)) 
        {
            log.warn("Next round appears to have started; maybe a player left?");
            return null;
        }
        //Preconditions.checkState(!playerBets.containsKey(playerName));
        
        incrementPlayer(playerName);
        printHandHistory("Check");
        
        playerBets.put(playerName, 0);
        
        if (potsGood())
        {
            //La ligne actuel était prise en compte
            return getNextState(false);
        }
        
        return this;
    }

    @Override
    public ParserListener handleTapis(String playerName)
    {
      //Tapis est un cas diffil car on ne sais pas si c'est un relancement ou pas ; aussi on ne sais plus le pot
        log.debug("{} Tapis", playerName);
        return null;
    }

    @Override
    public ParserListener handleShowdown(String playerName, int finalPot)
    {
        if (finalPot != pot ) {
            log.warn("Final pot calculated as {} but is {}", pot, finalPot);
        }
        log.debug("{} Showdown pot {}", playerName, finalPot);
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
        if (replayLine) {
            replayLine = false;
            return true;
        }
        return false;
    }
}
