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
    static Logger log = LoggerFactory.getLogger(FlopTurnRiverState.class);
    
    
    public HandInfo handInfo;
    
    //La liste complète d'actions faites dans la main
    public List<PlayerAction> actions;    
    public List<List<Integer>> playerPosToActions;
    
    //Qui a fait la dernière action aggressive dans la tournée précédente
    public String prevTourneeAgresseur;
    
    //La liste de joeurs dans l'ordre à parler 
    public List<String> players;
    int pot;
    
    final public static int MAX_PLAYERS = 5;
    
    //Full amount needed to stay in
    int amtToCall = 0;
    
    //public Map<String , Boolean> hasFolded;
    public boolean[] hasFolded;
    public int[] allInMinimum;
    
    String lastTapisPlayer;
    int lastTapisPlayerPos;
    int ambigTapisLineNumber;
    
    public int[] playerBets;
    
    
    public String roundInitialBetter;
    public int roundInitialBetterPos;
    
    
    int sbPos;
    int  bbPos;
    public int tableStakes;
    private final static int DEFAULT_TABLE_STAKES = 200000;
    
    int currentPlayer = 0;
    
    //-1 means replay last line
    int replayLine = -10;
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
    public FlopTurnRiverState(List<String> players, int pot, 
    		int round, 
            HandInfo handInfo) 
    {
        super();
        Preconditions.checkNotNull(handInfo);
        
        this.players = Lists.newArrayList();
        this.pot = pot;
        this.round = round;
        this.handInfo = handInfo;
        
        
        actions = Lists.newArrayList();
        playerPosToActions = Lists.newArrayList();
        

        hasFolded = new boolean[MAX_PLAYERS];
        playerBets = new int[MAX_PLAYERS];
        allInMinimum = new int[MAX_PLAYERS];
        
        this.currentPlayer = -1;
        
        for (int i  = 0; i < players.size(); ++i)
        {
        	addPlayer(players.get(i));
                     
        }
        
        Preconditions.checkState(this.currentPlayer == players.size() - 1);
        
        roundInitialBetterPos = -1;
        ambigTapisLineNumber = -1;
        replayLine = -10; 
         
        
        Preconditions.checkState(handInfo.roundStates.length == 4);
        Preconditions.checkState(handInfo.roundStates[round] == null);
        handInfo.roundStates[round] = this;
        
        log.debug("Nouveau round.  Players {} pot {}", players.size(), pot);
                        
        
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
    
    
    
    
    
    private ParserListener getNextStateAfterPreflop(boolean replayline) {
        log.debug("Preflop fini : Tous les joueurs pris en compte");
        List<String> playersInOrder = Lists.newArrayList();
        
        //add sb
        sbPos = players.size() - 2;
        bbPos = players.size() - 1;
        
        String playerSB = players.get(sbPos);
        String playerBB = players.get(bbPos);
        
        
        if (!hasFolded[sbPos] &&  allInMinimum[sbPos] < 0) {
            log.debug("Adding small blind {}", playerSB);
            playersInOrder.add(playerSB);
        } 
        
        if (!hasFolded[bbPos] &&  allInMinimum[bbPos] < 0) {
            log.debug("Adding big blind {}", playerBB);
            playersInOrder.add(playerBB);
        } 
        
        for(int i = 0; i < players.size() - 2; ++i) {
            String playerName = players.get(i);
            if (!hasFolded[i]
                    &&  allInMinimum[i] < 0
                    ) {
                log.debug("Adding player {}", playerName);
                playersInOrder.add(playerName);
            }
        }
        
        FlopTurnRiverState ftrState = new FlopTurnRiverState(playersInOrder,
                pot, 1, handInfo);
        
        ftrState.replayLine = replayline ? -1 : -10;
        ftrState.ambigTapisLineNumber = ambigTapisLineNumber;
        
        return ftrState;
    }
    
    private ParserListener getNextState(boolean replayLine) {
        log.debug("Tous les joueurs pris en compte");
        
        if (round == 0) {
        	if (!correction)
        	{
        		correctionPotBlinds();
        	}
            return getNextStateAfterPreflop(replayLine);
        } 
        
        if (round == 3) {
            return null;
        }
        List<String> playersInOrder = Lists.newArrayList();
        
        for(int i = 0; i < players.size() ; ++i) {
            String playerName = players.get(i);
            if (!hasFolded[i]
                    && allInMinimum[i] < 0
                    
                    ) {
                log.debug("Adding player {}", playerName);
                playersInOrder.add(playerName);
            }
        }
        
        FlopTurnRiverState ftrState = new FlopTurnRiverState(playersInOrder, 
                pot, 1+round, handInfo);
        
        ftrState.replayLine = replayLine ? -1 : -10;
        ftrState.ambigTapisLineNumber = ambigTapisLineNumber;
        
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
        
        log.debug("Pot is good");
        return true;
    }

    boolean correction = false;
    private void correctionPotBlinds()
    {

    	correction = true;
        //
        sbPos = players.size() - 2;
        bbPos = players.size() - 1;
        
        PlayerAction sbAction = actions.get(actions.size()-2);
        PlayerAction bbAction = actions.get(actions.size()-1);
        
        Preconditions.checkState(sbAction.playerPosition == sbPos);
        Preconditions.checkState(bbAction.playerPosition == bbPos);
        
        int potAdj = 0;
        if (sbAction.action != Action.FOLD)
        {
        	potAdj -= tableStakes;
        	
        }
        sbAction.playerAmtPutInPotThisRound = tableStakes;
        
        bbAction.pot += potAdj;
        bbAction.playerAmtPutInPotThisRound = 2 * tableStakes;
        
        if (bbAction.action != Action.FOLD)
        {
        	potAdj -= 2*tableStakes;
        }
        
        pot += potAdj;
    }
    
    private void addPlayer(String playerName)
    {
    	players.add(playerName);
    	currentPlayer = players.size() - 1;
        
        playerPosToActions.add( new ArrayList<Integer>() );
        playerBets[currentPlayer] = -1;
        allInMinimum[currentPlayer] = -1;
    
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
            addPlayer(playerName);
            
            if (players.size() > MAX_PLAYERS)
            {
            	log.error("Too many players hand {} line {}", handInfo.handIndex, handInfo.startingLine);
            }
            
            
            
            return false;
        }
        
        //Tous les joeurs sont pris en comptes, adjuste le pot
        if (!correction && round == 0)
        {
        	
        	correctionPotBlinds();
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
                    && allInMinimum[currentPlayer] < 0
                    ) 
            {
            	if (!loopPlayerName.equals(playerName))
            	{
            		log.debug("Expected {} but was {} maybe {} left",
            				playerName, loopPlayerName, loopPlayerName);
            		hasFolded[currentPlayer] = true;
            		continue;
            	}
                Preconditions.checkState(loopPlayerName.equals(playerName), "Player name " + playerName + " cur " + currentPlayer + " " + loopPlayerName);
                
                return true;
            }
        }
    }
  
    /**
     * Après avoir trouver les montants exact de la tapisAction,
     * on corrige les actions qui suivaient éventuellement
     * @param fromActionIndex
     * @param actions
     */
    private static void correctActions(int fromActionIndex, List<PlayerAction> actions)
    {
        PlayerAction tapisAction = actions.get(fromActionIndex); 
        int pot = tapisAction.pot;
        if (tapisAction.action == Action.CALL_ALL_IN)
        {
            pot += tapisAction.incomingBetOrRaise;
        } else if (tapisAction.action == Action.RAISE_ALL_IN)
        {
            pot += tapisAction.amountRaised;
        }
        for(int idx = fromActionIndex+1; idx < actions.size(); ++idx)
        {
            PlayerAction action = actions.get(idx);
            
            if (action.action == Action.FOLD) {
                action.pot = pot;
                action.incomingBetOrRaise = tapisAction.action == Action.RAISE_ALL_IN ? 
                        tapisAction.amountRaised : tapisAction.incomingBetOrRaise;
            } else {
                log.debug("Not correction action {}", action);
            }
        }
    }

    private void changeLastTapisAction(PlayerAction.Action pAction, Integer tapisAmt,
            boolean clearLastTapisPlayer,
            boolean isNotAmbig
            )
    {
        int tapisPos = players.indexOf(lastTapisPlayer);
        List<Integer> actionIndexes = playerPosToActions.get(tapisPos);
        int tapisActionIndex = actionIndexes.get(actionIndexes.size()-1);
        PlayerAction tapisAction = actions.get(tapisActionIndex);
        
        Preconditions.checkState(tapisAction.action == Action.ALL_IN || tapisAction.action == Action.RAISE_ALL_IN);
        
        tapisAction.action = pAction;
        
        if (tapisAmt != null && tapisAction.action == Action.RAISE_ALL_IN)
        {
            tapisAction.amountRaised = tapisAmt;
            pot = updatePlayerBet(playerBets, lastTapisPlayerPos, lastTapisPlayer, tapisAmt, pot );
            correctActions(tapisActionIndex, actions);
        } else if (tapisAction.action == Action.CALL_ALL_IN)
        {
            tapisAction.amountRaised = 0;
            pot = updatePlayerBet(playerBets, lastTapisPlayerPos, lastTapisPlayer, tapisAction.incomingBetOrRaise, pot );
            correctActions(tapisActionIndex, actions);
        }
        
        if (clearLastTapisPlayer)
        {
            lastTapisPlayer = null;
            lastTapisPlayerPos = -1;
        }
        
        if (isNotAmbig)
        {
            ambigTapisLineNumber = -1;
        }
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
            tableStakes = amtToCall / 2;
            
            //Ajoute les blindes
            pot = 3 * tableStakes;
             
        } else {
            
            //Check if there is an unknown tapis we can deduce
            if (lastTapisPlayer != null)
            {
                if (betAmt < amtToCall)
                {
                    log.debug("Last tapis was a call");
                    changeLastTapisAction( Action.CALL_ALL_IN, null, true, true );
                    amtToCall = betAmt;
                } else {
                    int diff = betAmt - amtToCall;
                    
                    int tapisGuess = allInMinimum[lastTapisPlayerPos];
                    
                    Preconditions.checkArgument(tapisGuess >= 0);
                    
                    //+1 was added in handleTapis to make it act like a raise
                    amtToCall = tapisGuess + diff + 1;
                    log.debug("Adjusting tapis guess to {}", tapisGuess + diff);
                    
                    //pot = updatePlayerBet(playerBets, lastTapisPlayerPos, lastTapisPlayer, amtToCall, pot);

                    
                    allInMinimum[lastTapisPlayerPos] = amtToCall;
                    
                    
                    
                    changeLastTapisAction( Action.RAISE_ALL_IN, amtToCall, true, true );
                }
                
                
                
            }
            
            //Check internal state
            Preconditions.checkState(amtToCall > 0); 
            Preconditions.checkState(betAmt == amtToCall, "Bet amount %s does not equal incoming amount to call %s", betAmt, amtToCall);
        }
        
        
        boolean seenPlayer = incrementPlayer(playerName);
        
        int prevBet = playerBets[currentPlayer];
        if (prevBet < 0)
            prevBet = 0;
        PlayerAction action = PlayerAction.createCall(currentPlayer, playerName, prevBet, betAmt, pot);
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
            
            log.debug("Player %{} bet %{} of pot", playerName);
            
            
        } 
        
        if (round == 0 && tableStakes <= 0)
        {
            //we have to guess what the stakes were
            tableStakes = DEFAULT_TABLE_STAKES;
            pot = 3 * DEFAULT_TABLE_STAKES;
            
        }
        
        //Check if there is an unknown tapis we can only guess their bet is less than the current amount
        if (lastTapisPlayer != null)
        {
            int tapisGuess = allInMinimum[lastTapisPlayerPos];
            
            log.debug("Reraise after tapis, no real guess possible {}", tapisGuess );
            
            //It is also possible that the next round started, impossible to tell...see testTapis2
            //maybe if it is seen that the river is empty...
            
            changeLastTapisAction(Action.CALL_ALL_IN, tapisGuess, true, false);
           
            
        }
        
        incrementPlayer(playerName);
        
        PlayerAction action = PlayerAction.createReraise(currentPlayer, playerName, amtToCall,
                betAmt, playerBets[currentPlayer], pot);
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
        
        //Stats
        final int playerBet = playerBets[currentPlayer];
        int raiseAmt = amtToCall - playerBet;
        
        log.debug("Fold raiseAmt {} amtToCall {} player bet {}", raiseAmt, amtToCall, playerBet);
        
        if ( 1 == raiseAmt && lastTapisPlayer != null)
        {
            log.debug("{} se couche une relance, alors le dernier tapis était une relance.");
            
            changeLastTapisAction( Action.RAISE_ALL_IN, null, false, false );
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
            
            
            changeLastTapisAction(Action.CALL_ALL_IN, null, true, true);
            
            return getNextState(true);
        }
        
        Preconditions.checkState(amtToCall == 0);
        
        
        incrementPlayer(playerName);
      
        
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
    public ParserListener handleTapis(String playerName, int lineNumber)
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
            tableStakes = DEFAULT_TABLE_STAKES;
            pot = 3 * DEFAULT_TABLE_STAKES;
            
        }
                
        if (actions.size() > 0 && playerName.equals(actions.get(actions.size()-1).playerName))
        {
        	log.debug("Deuxieme tapis d'affiler, ne pas pris en compte");
        	return this;
        }
        
        boolean seenPlayer = incrementPlayer(playerName);
        
        if (amtToCall == 0)
        {
            log.debug("All was a bet, no previous bets");
            addAction(PlayerAction.createBetAllin(currentPlayer, playerName, amtToCall, pot));
                        
        } else if (ambigTapisLineNumber == lineNumber)
        {
            //Trying to make it a call
            int prevBet = playerBets[currentPlayer];
            if (prevBet < 0)
                prevBet = 0;
            
            
            addAction(PlayerAction.createAllin(currentPlayer, playerName, amtToCall,prevBet, pot));
            
            lastTapisPlayer = playerName;
            lastTapisPlayerPos = currentPlayer;

            allInMinimum[currentPlayer] = amtToCall;
            changeLastTapisAction(PlayerAction.Action.CALL_ALL_IN, amtToCall, true, true);
            ambigTapisLineNumber = -2;
            
            if (seenPlayer && potsGood() && round < 3) 
            {
                return getNextState(false);
            }
            return this;
        } else {
            log.debug("All in, unknown, incoming to call was >0 {}", amtToCall);
            int prevBet = playerBets[currentPlayer];
            if (prevBet < 0)
                prevBet = 0;
            addAction(PlayerAction.createAllin(currentPlayer, playerName, amtToCall,prevBet, pot));
            
            
        }
        
        allInMinimum[currentPlayer] = amtToCall;
        
      //Assume it was  a raise
        amtToCall += 1;
        
        //Guess it was a raise
        //amtToCall = 1+getAllInBet(playerName);
        
        pot = updatePlayerBet(playerBets, currentPlayer, playerName, amtToCall, pot);
        lastTapisPlayer = playerName;
        lastTapisPlayerPos = currentPlayer;
        if (ambigTapisLineNumber == -1)
        {
            ambigTapisLineNumber = lineNumber;
        } else if (ambigTapisLineNumber == lineNumber) {
            ambigTapisLineNumber = -2;
        }
        
       
        
        return this;
    }

    @Override
    public ParserListener handleShowdown(String playerName, int finalPot, String winDesc)
    {
        
    	if (!correction && round == 0)
        {        	
        	correctionPotBlinds();
        }
    	
    	if (lastTapisPlayer != null)
    	{
    		log.debug("Last tapis was a call");
    		changeLastTapisAction( Action.CALL_ALL_IN, null, true, true );
    	}
    	
        if(players.size() >= 2 && actions.size() == 0)
        {
            log.debug("Showdown tandis que aucun jouer n'a parlé");
            if (ambigTapisLineNumber > -1) {
                FlopTurnRiverState restart = new FlopTurnRiverState(new ArrayList<String>(), 0,  0, 
                        new HandInfo(handInfo.startingLine, handInfo.handIndex));
                restart.replayLine = handInfo.startingLine + 1; 
                restart.ambigTapisLineNumber = ambigTapisLineNumber;
                return restart;
            }
        }
        
        handInfo.wonPot += finalPot;
        
        handInfo.winDesc = winDesc;
        handInfo.winRound = round;
        handInfo.winnerPlayerName[0] = playerName;
        
        
        return this;
    }

    @Override
    public ParserListener handleGagne(String playerName)
    {
        log.debug("{} gagne", playerName);
        
        //To make sure the player list is accurate
        if (!players.contains(playerName))
        {
            log.debug("Player [{}] ajouté avec index {} ", playerName, players.size());
            addPlayer(playerName);            
        }
        
       // int playerPos = players.indexOf(playerName);
        
        handInfo.wonPot = pot;
        handInfo.winRound = actions.size() == 0 ? round - 1 : round;
        handInfo.winnerPlayerName[0] = playerName;
        handInfo.winDesc = playerName + " Round "  +
        Statistics.roundToStr(handInfo.winRound) + "  No showdown";
                
        return this;
    }

    @Override
    public int replayLine()
    {
        int ret = replayLine;
        
         replayLine = -10; 
        
        return ret;
    }
}
