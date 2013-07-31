package pkr.history;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        
        this.currentPlayer = players.size() - 1;
        
        hasFolded = Maps.newHashMap();
        playerBets = Maps.newHashMap();
        
        if (round == 0) {
            logOutput.debug("\n***********************************************");
            logOutput.debug("Starting new Hand");
        }
        log.debug("Nouveau round.  Players {} pot {}", players.size(), pot);
        
        logOutput.debug("\n------------------------------");
        logOutput.debug("\nStarting round {} with {} players.  Pot is ${}\n",
                round == 0 ? "PREFLOP" :
                    round == 1 ? "FLOP" :
                    (round == 2 ? "TURN" : "RIVER"),
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
    
    private ParserListener getNextStateAfterPreflop(boolean replayline) {
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
        
        FlopTurnRiverState ftrState = new FlopTurnRiverState(playersInOrder, pot, replayline, 1);
        
        return ftrState;
    }
    
    private ParserListener getNextState(boolean replayLine) {
        log.debug("Tous les joueurs pris en compte");
        
        if (round == 0) {
            return getNextStateAfterPreflop(replayLine);
        } 
        
        if (round == 3) {
            return null;
        }
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

    /*
     * Position currentPlayer au joeur courant
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
            ++loopCheck;
            
            if (loopCheck > 15) {
                Preconditions.checkState(false, "Loop all players folded");
            }
            ++currentPlayer;
            if (currentPlayer == players.size()) {
                currentPlayer = 0;
            }
            
            String loopPlayerName = players.get(currentPlayer);
            if (BooleanUtils.isNotTrue(hasFolded.get(loopPlayerName))) {
                Preconditions.checkState(loopPlayerName.equals(playerName), "Player name " + playerName + " cur " + currentPlayer + " " + loopPlayerName);
                
                return true;
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
        
        logOutput.debug("** Player {} position {} Action [< {} >]", 
                players.get(currentPlayer), 1+currentPlayer, action);
        
        Integer playerBet = playerBets.get(players.get(currentPlayer)); 
        if (playerBet == null)
            playerBet = 0;
        
        if (round >= 0 && amtToCall > playerBet)
        {
            int diff = amtToCall - playerBet;
            double perc = 100.0 * diff / (pot + diff);
            double ratio = pot * 1.0 / diff; 
            
            double outsOne = perc / 2;
            
           // double betSizeToPot = 1.0 * diff / pot;
            //% must be ahead
           // double callBluff = 100*betSizeToPot / (1+betSizeToPot);
            
            
            logOutput.debug("Amount to call ${} for pot ${}.\n  Pot ratio : {}%  | 1 to {} | {} outs", 
                    moneyFormat.format(diff), moneyFormat.format(pot),
                    df2.format(perc), df2.format(ratio), df2.format(outsOne));
          //  logOutput.debug("Must be ahead {}% of the time to call a bluff", 
              //      df2.format(callBluff));
        }
        
        if (round >= 0 && raiseAmt > playerBet)
        {
            int diff = raiseAmt - amtToCall;
            double betSizeToPot = 1.0 * diff / pot;
            double bluff = 100.0*(betSizeToPot) / (1+betSizeToPot);
            
            logOutput.debug("Raise amt ${} | %{} of pot " +
            		"\nbluff % chance everyone must fold {}%",
                    moneyFormat.format(diff),
                    df2.format(100*betSizeToPot),
                     df2.format(bluff));
        }
        
        logOutput.debug("\n");
    }

    @Override
    public ParserListener handleSuivi(String playerName, int betAmt)
    {
        log.debug("Suivi player {} bet {} ; pot {}", playerName, betAmt, pot);
        
        if (round == 0 && amtToCall == 0) 
        {
            amtToCall = betAmt;
            log.debug("Table mise à {}", amtToCall);
        } else {
        
        Preconditions.checkState(amtToCall > 0); 
        Preconditions.checkState(betAmt == amtToCall);
        }
        
        boolean seenPlayer = incrementPlayer(playerName);
        printHandHistory("Call $" + moneyFormat.format(betAmt));
        
        
        
        pot = updatePlayerBet(playerBets, playerName, betAmt, pot);
        
        Preconditions.checkState(players.contains(playerName));
        
        
        if (seenPlayer && potsGood() && round < 3) 
        {
            return getNextState(false);
        }
    
        return this;
    }

    @Override
    public ParserListener handleRelance(String playerName, int betAmt)
    {
      //Le prochain round a commencé
        if (round == 0 && 
                players.contains(playerName) && 
                potsGood())
        {
            return getNextState(true);
        }
        
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
        //Preconditions.checkState(players.contains(playerName));
        
        
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
        /*
        if (playerBets.containsKey(playerName)) 
        {
            log.warn("Next round appears to have started; maybe a player left?");
            return null;
        }*/
        //Preconditions.checkState(!playerBets.containsKey(playerName));
        
        
        
        
        //Le prochain round a commencé
        if (round == 0 && players.contains(playerName) && potsGood())
        {
            return getNextState(true);
        }
        
      //  boolean seenPlayer = 
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
        logOutput.debug("{} wins showdown with pot ${}", playerName, 
                moneyFormat.format(finalPot));
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
