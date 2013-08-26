package pkr.history;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.history.PlayerAction.Action;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


public class FlopTurnRiverState implements ParserListener
{
    static Logger log = LoggerFactory.getLogger(Parser.class);
    
    HandInfoCollector handInfoCollector; 
        
    
    HandInfo handInfo;
    
    //La liste complète d'actions faites dans la main
    public List<PlayerAction> actions;    
    public List<List<Integer>> playerPosToActions;
    
    //Qui a fait la dernière action aggressive dans la tournée précédente
    public String agresseur;
    
    //La liste de joeurs dans l'ordre à parler 
    public List<String> players;
    int pot;
    
    final static int MAX_PLAYERS = 5;
    
    //Full amount needed to stay in
    int amtToCall = 0;
    
    //public Map<String , Boolean> hasFolded;
    public boolean[] hasFolded;
    public int[] allInMinimum;
    
    String lastTapisPlayer;
    int lastTapisPlayerPos;
    
    public int[] playerBets;
    
    
    public String roundInitialBetter;
   
    
    
    
    String playerSB;
    public String playerBB;
    public int tableStakes;
    
    
    int currentPlayer = 0;
    
    boolean replayLine = false;
    final int round;
    
    
    /**
     * Each object is a round in a hand
     * 
     * @param players
     * @param pot
     * @param replayLine
     * @param round
     * @param stats
     * @param masterList   holds for all hands in a session
     * @param roundStates  holds all objects for the hand 
     */
    public FlopTurnRiverState(List<String> players, int pot, boolean replayLine, int round, 
            HandInfoCollector handInfoCollector,  HandInfo handInfo) 
    {
        super();
        Preconditions.checkNotNull(handInfoCollector);
        Preconditions.checkNotNull(handInfo);
        
        this.players = players;
        this.pot = pot;
        this.replayLine = replayLine;
        this.round = round;
        this.handInfoCollector = handInfoCollector;
        this.handInfo = handInfo;
        
        
        actions = Lists.newArrayList();
        playerPosToActions = Lists.newArrayList();
        

        hasFolded = new boolean[MAX_PLAYERS];
        playerBets = new int[MAX_PLAYERS];
        allInMinimum = new int[MAX_PLAYERS];
        
        for (int i  = 0; i < players.size(); ++i)
        {
            playerPosToActions.add(new ArrayList<Integer>());
            
            playerBets[i] = -1;
            allInMinimum[i] = -1;
                     
        }
        
        this.currentPlayer = players.size() - 1;
        
        
        
        if (round == 0) {
            handInfo.handLog.append("\n***********************************************");
            handInfo.handLog.append("Starting Hand ")
            .append(handInfoCollector.masterList.size()+1)
            .append(" line ")
            .append(handInfo.startingLine);
            
        }
        
        Preconditions.checkState(handInfo.roundStates.length == 4);
        Preconditions.checkState(handInfo.roundStates[round] == null);
        handInfo.roundStates[round] = this;
        
        log.debug("Nouveau round.  Players {} pot {}", players.size(), pot);
        
        handInfo.handLog.append("\n------------------------------");
        handInfo.handLog.append("\nStarting round ")
        .append(Statistics.roundToStr(round))
        .append(" with ")
        .append(players.size())
        .append(" players.  Pot is $")
        .append(Statistics.moneyFormat.format(pot))
        .append("\n");
                        
        
    }
    
    
    
    public static int updatePlayerBet( int[] playerBets, int playerPos, String playerName, int playerBet, int pot) 
    {
        if (playerBets[playerPos] >= 0) {
            pot -= playerBets[playerPos];
        }
        
        pot += playerBet;
        
        playerBets[playerPos] = playerBet;
        
        log.debug("Update player {} with bet {} in pot {}",  playerName, playerBet, pot);
        
        return pot;
    }
    
    public void addAction(PlayerAction action)
    {
        actions.add(action);
        playerPosToActions.get(currentPlayer).add(actions.size()-1);
    }
    
    
    @Deprecated
    public int getCurrentBet(String playerName)
    {
        int playerPos = players.indexOf(playerName);
        
        if (playerPos == -1)
        {
            log.error("Player {} not found in list players {}", playerName, players);
        }
        Integer bet = playerBets[playerPos];
            
        if (bet == null)
            return 0;
        
        
        return bet;
    }
    
    @Deprecated
    public int getAllInBet(String playerName)
    {
        int playerPos = players.indexOf(playerName);
        
        return allInMinimum[playerPos];
        
    }
    
    
    
    private ParserListener getNextStateAfterPreflop(boolean replayline) {
        log.debug("Preflop fini : Tous les joueurs pris en compte");
        List<String> playersInOrder = Lists.newArrayList();
        
        //add sb
        final int sbPos = players.size() - 2;
        final int bbPos = players.size() - 1;
        
        playerSB = players.get(sbPos);
        playerBB = players.get(bbPos);
        
        
        if (!hasFolded[sbPos] &&  allInMinimum[sbPos] <= 0) {
            log.debug("Adding small blind {}", playerSB);
            playersInOrder.add(playerSB);
        } else {
            //Adjust pot if necessary
            
            if (playerBets[sbPos] == 0)
            {
                log.debug("Adding small blind from player {}  bet {} tablestakes {}", playerSB, tableStakes/2, tableStakes);
                pot = updatePlayerBet(playerBets, sbPos, playerSB, tableStakes / 2, pot);
            }
        }
        
        if (!hasFolded[bbPos] &&  allInMinimum[bbPos] <= 0) {
            log.debug("Adding big blind {}", playerBB);
            playersInOrder.add(playerBB);
        } else {
            if (playerBets[bbPos] == 0)
            {
                log.debug("Adding big blinds to pot");
                pot = updatePlayerBet(playerBets, bbPos, playerBB, tableStakes, pot);
            }
        }
        
        for(int i = 0; i < players.size() - 2; ++i) {
            String playerName = players.get(i);
            if (!hasFolded[i]
                    &&  allInMinimum[i] <= 0
                    ) {
                log.debug("Adding player {}", playerName);
                playersInOrder.add(playerName);
            }
        }
        
        FlopTurnRiverState ftrState = new FlopTurnRiverState(playersInOrder,
                pot, replayline, 1, handInfoCollector, handInfo);
        
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
            if (!hasFolded[i]
                    && allInMinimum[i] <= 0
                    
                    ) {
                log.debug("Adding player {}", playerName);
                playersInOrder.add(playerName);
            }
        }
        
        FlopTurnRiverState ftrState = new FlopTurnRiverState(playersInOrder, 
                pot, replayLine, 1+round, handInfoCollector, handInfo);
        
        return ftrState;
    }

    private boolean potsGood() 
    {
        for(int i = 0; i < players.size(); ++i)
        {
            String playerName = players.get(i);
            if (hasFolded[i]) {
                continue;
            }
            
            int playerBet = playerBets[i];
            
            if (playerBet < 0) 
            {
                log.debug("Pot is not good.  Player {} idx {} has not acted",
                        playerName, i);
                return false;
            }
            
            if (allInMinimum[i] < 0 && playerBet < amtToCall)
            {
                log.debug("Pot is not good.  Player {} idx {} bet only {}",
                        playerName, i, playerBet);
                
                return false;
            }
            
            Preconditions.checkState(allInMinimum[i] > 0 || amtToCall == playerBet, "Player %s amtToCall %s  bet %s", playerName, amtToCall, playerBet);
        }
        
        return true;
    }

    
    /*
     * Position currentPlayer au joeur courant
     * 
     * Post condition : currentPlayer positionné à playerName
     */
    private boolean incrementPlayer(String playerName)
    {
        if (!players.contains(playerName))
        {
            log.debug("Player [{}] ajouté avec index {} ", playerName, players.size());
            players.add(playerName);
            
            playerPosToActions.add( new ArrayList<Integer>() );
            currentPlayer = players.size() - 1;
            
            playerBets[currentPlayer] = -1;
            allInMinimum[currentPlayer] = -1;
            
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
            if (!hasFolded[currentPlayer]
                    && allInMinimum[currentPlayer] <= 0
                    ) {
                Preconditions.checkState(loopPlayerName.equals(playerName), "Player name " + playerName + " cur " + currentPlayer + " " + loopPlayerName);
                
                return true;
            }
        }
    }
    
    
    
    private void printHandHistory(String action)
    {
        printHandHistory(action, 0);
    }
    private void printHandHistory(String action, int raiseAmt)
    {
        
        handInfo.handLog.append("\n** Player ")
        .append(players.get(currentPlayer))
        .append(" position ")
        .append(1+currentPlayer)
        .append("  Action [< ")
        .append(action)
        .append(" >]\n");
                
        
        int playerBet = playerBets[currentPlayer]; 
        
        
        if (round >= 0 && amtToCall > playerBet)
        {
            int diff = amtToCall - playerBet;
            double perc = 100.0 * diff / (pot + diff);
            double ratio = pot * 1.0 / diff; 
            
            double outsOne = perc / 2;
            
           // double betSizeToPot = 1.0 * diff / pot;
            //% must be ahead
           // double callBluff = 100*betSizeToPot / (1+betSizeToPot);
            
            
            handInfo.handLog.append("Amount to call $")
            .append(Statistics.moneyFormat.format(diff))
            .append(" for pot $")
            .append(Statistics.moneyFormat.format(pot))
            .append(".\n  Pot ratio (bluff catching) : ")
            .append(Statistics.df2.format(perc))
            .append("%  | 1 to ")
            .append(Statistics.df2.format(ratio))
            .append(" | ")
            .append(Statistics.df2.format(outsOne))
            .append("\n");
          //  logOutput.debug("Must be ahead {}% of the time to call a bluff", 
              //      Statistics.df2.format(callBluff));
        }
        
        if (round >= 0 && raiseAmt > playerBet)
        {
            int diff = raiseAmt - amtToCall;
            double betSizeToPot = 1.0 * diff / pot;
            //double bluff = 100.0*(betSizeToPot) / (1+betSizeToPot);
            
            handInfo.handLog.append("Raise amt $")
            .append(Statistics.moneyFormat.format(diff))
            .append(" | %")
            .append(Statistics.formatPercent(betSizeToPot, 1))
            .append(" of pot ")
            .append("\nbluff % chance everyone must fold ")
            .append(Statistics.formatPercent(betSizeToPot, 1+betSizeToPot))
            .append("%\n");
        }
        
        handInfo.handLog.append("\n");
    }

    private void changeLastTapisAction(PlayerAction.Action pAction, Integer tapisAmt, boolean clearLastTapisPlayer)
    {
        int tapisPos = players.indexOf(lastTapisPlayer);
        List<Integer> actionIndexes = playerPosToActions.get(tapisPos);
        PlayerAction tapisAction = actions.get(actionIndexes.get(actionIndexes.size()-1));
        
        Preconditions.checkState(tapisAction.action == Action.ALL_IN || tapisAction.action == Action.RAISE_ALL_IN);
        
        tapisAction.action = pAction;
        
        if (tapisAmt != null && tapisAction.action == Action.RAISE_ALL_IN)
        {
            tapisAction.amountRaised = tapisAmt;
        }
        
        if (clearLastTapisPlayer)
            lastTapisPlayer = null;
    }
    @Override
    public ParserListener handleSuivi(String playerName, int betAmt)
    {
        log.debug("Suivi player {} bet {} ; pot {}", playerName, betAmt, pot);
        
        if (round == 0 && amtToCall == 0) 
        {
            //Set table stakes
            amtToCall = betAmt;
            log.debug("Table mise à {}", amtToCall);
            tableStakes = amtToCall;
        } else {
            
            //Check if there is an unknown tapis we can deduce
            if (lastTapisPlayer != null)
            {
                if (betAmt < amtToCall)
                {
                    log.debug("Last tapis was a call");
                    changeLastTapisAction( Action.CALL_ALL_IN, null, true );
                    amtToCall = betAmt;
                } else {
                    int diff = betAmt - amtToCall;
                    
                    int tapisGuess = allInMinimum[lastTapisPlayerPos];
                    
                    Preconditions.checkArgument(tapisGuess >= 0);
                    
                    //+1 was added in handleTapis to make it act like a raise
                    amtToCall = tapisGuess + diff + 1;
                    log.debug("Adjusting tapis guess to {}", tapisGuess + diff);
                    
                    pot = updatePlayerBet(playerBets, lastTapisPlayerPos, lastTapisPlayer, amtToCall, pot);

                    
                    allInMinimum[lastTapisPlayerPos] = amtToCall;
                    
                    
                    
                    changeLastTapisAction( Action.RAISE_ALL_IN, amtToCall, true );
                }
                
                
                
            }
            
            //Check internal state
            Preconditions.checkState(amtToCall > 0); 
            Preconditions.checkState(betAmt == amtToCall, "Bet amount %s does not equal incoming amount to call %s", betAmt, amtToCall);
        }
        
        
        boolean seenPlayer = incrementPlayer(playerName);
        printHandHistory("Call $" + Statistics.moneyFormat.format(betAmt));
        
        PlayerAction action = PlayerAction.createCall(currentPlayer, playerName, betAmt, pot);
        addAction(action);
        
        pot = updatePlayerBet(playerBets, currentPlayer, playerName, betAmt, pot);
        
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
        
        Preconditions.checkState(betAmt > amtToCall, "Player %s betAmt %s amtToCall %s", playerName, betAmt, amtToCall );
        
        if (roundInitialBetter == null) {
            roundInitialBetter = playerName;
           
            
            Preconditions.checkState(round == 0 || amtToCall == 0);
            
            int raiseAmt = betAmt - amtToCall;
            double potRatio = 1.0 * raiseAmt / pot;
            
            log.debug("Player %{} bet %{} of pot", playerName, Statistics.df2.format(potRatio));
            
            
        } 
        
        if (round == 0 && tableStakes <= 0)
        {
            //we have to guess what the stakes were
            tableStakes = 1;
        }
        
        //Check if there is an unknown tapis we can only guess their bet is less than the current amount
        if (lastTapisPlayer != null)
        {
            int tapisGuess = getAllInBet(lastTapisPlayer);
            
            log.debug("Reraise after tapis, no real guess possible {}", tapisGuess );
            
            
            int tapisPos = players.indexOf(lastTapisPlayer);
            List<Integer> actionIndexes = playerPosToActions.get(tapisPos);
            PlayerAction tapisAction = actions.get(actionIndexes.get(actionIndexes.size()-1));
            
            Preconditions.checkState(tapisAction.action == Action.ALL_IN || tapisAction.action == Action.RAISE_ALL_IN);
            if (tapisAction.action == Action.ALL_IN)
            {
                log.debug("All in guessed to be a call");
                tapisAction.action = Action.CALL_ALL_IN;
            }
            
            lastTapisPlayer = null;
            
        }
        
        incrementPlayer(playerName);
        printHandHistory("Raise $" + Statistics.moneyFormat.format(betAmt), betAmt);
        
        PlayerAction action = PlayerAction.createReraise(currentPlayer, playerName, amtToCall, betAmt, pot);
        addAction(action);
        
        amtToCall = betAmt;
        
        pot = updatePlayerBet(playerBets, currentPlayer, playerName, betAmt, pot);
        
        return this;
    }

    @Override
    public ParserListener handleCoucher(String playerName)
    {
        //Preconditions.checkState(players.contains(playerName));
        
        //Il est possible le flop a commencé avec un joeur qui se couche
        if (round == 0 && players.contains(playerName) && potsGood())
        {
            return getNextState(true);
        }
        

        boolean seenPlayer = incrementPlayer(playerName);
        printHandHistory("Fold");
        
        //Stats
        final int playerBet = playerBets[currentPlayer];
        int raiseAmt = amtToCall - playerBet;
        
        log.debug("Fold raiseAmt {} amtToCall {} player bet {}", raiseAmt, amtToCall, playerBet);
        
        if ( 1 == raiseAmt && lastTapisPlayer != null)
        {
            log.debug("{} se couche une relance, alors le dernier tapis était une relance.");
            
            changeLastTapisAction( Action.RAISE_ALL_IN, null, false );
        }
        
        double potRatio = pot > 0 ? 1.0 * raiseAmt / pot : 0;
        
        Preconditions.checkArgument(potRatio >= 0, "Amttocall %s  current bet %s", amtToCall, getCurrentBet(playerName));
        
        
        
        
        addAction(PlayerAction.createFold(currentPlayer, 
                playerName, amtToCall, pot, playerBet));
        
        hasFolded[currentPlayer] = true;
        
        //Want to process winning line
        if (round < 3 && seenPlayer && potsGood()) 
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
        
        //If there is a check after a tapis we assume the next round started
        if (lastTapisPlayer != null)
        {
            log.debug("Parole après une tapis, le dernier tapis était un suivi", playerName);
            allInMinimum[lastTapisPlayerPos] -= 1;
            
            int tapisPos = players.indexOf(lastTapisPlayer);
            List<Integer> actionIndexes = playerPosToActions.get(tapisPos);
            PlayerAction tapisAction = actions.get(actionIndexes.get(actionIndexes.size()-1));
            
            Preconditions.checkState(tapisAction.action == Action.ALL_IN);
            
            tapisAction.action = Action.CALL_ALL_IN;
            
            return getNextState(true);
        }
        
        Preconditions.checkState(amtToCall == 0);
        
        
      //  boolean seenPlayer = 
        incrementPlayer(playerName);
        printHandHistory("Check");
        
        addAction(PlayerAction.createCheck(currentPlayer, playerName, pot));
        
        playerBets[currentPlayer] = 0;
        
        //We exclude the river since we want to parse the last line showdown
        if (round < 3 && potsGood())
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
        
        //Le prochain round a commencé
        if (round == 0 && 
                players.contains(playerName) && 
                potsGood())
        {
            return getNextState(true);
        }
        
        //If the user has less than the buy in this will be wrong but that's rare
        if (roundInitialBetter == null) {
            roundInitialBetter = playerName;
        }
        
        if (round == 0 && tableStakes <= 0)
        {
            //we have to guess what the stakes were
            tableStakes = 1;
        }
                
        
        incrementPlayer(playerName);
        printHandHistory("All in for unknown amount");
        
        if (amtToCall == 0)
        {
            log.debug("All was a bet, no previous bets");
            addAction(PlayerAction.createBetAllin(currentPlayer, playerName, amtToCall, pot));
                        
        } else {
            log.debug("All in, unknown, was a previous bet");
            addAction(PlayerAction.createAllin(currentPlayer, playerName, amtToCall, pot));
            
            
        }
        
        allInMinimum[currentPlayer] = amtToCall;
        
      //Assume it was  a raise
        amtToCall += 1;
        
        //Guess it was a raise
        //amtToCall = 1+getAllInBet(playerName);
        
        pot = updatePlayerBet(playerBets, currentPlayer, playerName, amtToCall, pot);
        lastTapisPlayer = playerName;
        lastTapisPlayerPos = currentPlayer;
        
        return this;
    }

    @Override
    public ParserListener handleShowdown(String playerName, int finalPot)
    {
        if (finalPot != pot ) {
            log.warn("Final pot calculated as {} but is {}", pot, finalPot);
        }
        handInfo.handLog.append(playerName)
        .append(" wins showdown with pot $")
        .append(Statistics.moneyFormat.format(finalPot))
        .append("");
        
        this.handInfoCollector.handFinished(handInfo);
        
        return null;
    }

    @Override
    public ParserListener handleGagne(String playerName)
    {
        log.debug("{} gagne", playerName);
        
        //To make sure the player list is accurate
        if (!players.contains(playerName))
        {
            log.debug("Player [{}] ajouté avec index {} ", playerName, players.size());
            players.add(playerName);
            currentPlayer = players.size() - 1;
            
            playerPosToActions.add( new ArrayList<Integer>() );
        }
        this.handInfoCollector.handFinished(handInfo);
        
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
