package pkr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.possTree.PossibilityNode.HandCategory;
import pkr.possTree.PossibilityNode.TextureCategory;
import pkr.possTree.PossibilityNode.WinningLosingCategory;
import pkr.possTree.Tree;

import com.google.common.collect.Lists;

public class Simulator {
    
    private static Logger log = LoggerFactory.getLogger(Simulator.class);
    private static Logger logOutput = LoggerFactory.getLogger("mainOutput");
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        List<String> playerHoleCards = Lists.newArrayList();
        
       // playerHoleCards.add("AA, KK, QQ, JJ, TT, 99, 88, 77, 66, 55, 44, 33, 22, AK, AQ, AJ");
        //playerHoleCards.add("AA, AKs, 27, 93, 44, 99");
      //playerHoleCards.add("KTs");
      
    //  playerHoleCards.add("22");
    //  playerHoleCards.add("AKs");
       // playerHoleCards.add("T8");
    //  playerHoleCards.add("Q2s+, J2+, T2+, 32+");
     // playerHoleCards.add("A2o, A3o, A4o, A5o");
        
        //playerHoleCards.add("K8o, K9o, KTo");
        
        //playerHoleCards.add("AA");
        //playerHoleCards.add("KJo");
      //  playerHoleCards.add("AKo");
      //  playerHoleCards.add("JTs");
       // playerHoleCards.add("A4o");
        
       // playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+, 22+");
       // playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+, 22+");
        


      int NUM_RANDOM =0;
        int NUM_LOOSE_CALLS = 0;
        int NUM_OK_CALLS = 0;
        int NUM_GOOD_HANDS = 0;
        int NUM_RAISING_HANDS = 1;
        
        //offsuit A2 K7 Q8 J8 T8 T9 98(limit)
        //suited A2 K2 Q4 J6 T6 96 86?
        //22+
        
        //random
        for(int i = 0; i < NUM_RANDOM; ++i) {
            playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+,22+");
        }
        
        
        
        final String LESS_LOOSE = "22+, A2s+, K2s+, Q2s+, J3s+, T6s+, 96s+, 86s+, 75s+," +
                " 63s+, 53s+, 43s, 32s, " +
                "A2o+, K4o+, Q6o+, J8o+, T8o+, 98o+, 87o+, 75o+, 65o+, 54o";
        
        
        //loose call, top 50% of hands
        for(int i = 0; i < NUM_LOOSE_CALLS; ++i) {
            //playerHoleCards.add("22+, A2s+, K2s+, Q2s+, J4s+, T6s+, 96s+, 86s+, 75s+, 65s, 54s, A2o+, K2o+, Q5o+, J7o+, T7o+, 98o, 87o, 76o, 65o");
           // playerHoleCards.add(SUPER_LOOSE);
            //playerHoleCards.add(LOOSE);
            playerHoleCards.add(LESS_LOOSE);
        }
        
        final String MY_LIST = "22+, A2s+, K2s+, Q4s+, J6s+, T6s+, " +
        		"96s+, 86s+, 76s, 65s, 54s, " +
        		" A2o+, K7o+, Q8o+, J8o+, T8o+, 98o";
        
        
        final String LOOSE_ALL_IN = "22+, A2+, K9+, Q9s+, QTo+, JT+ " 
        		;
        
        //playerHoleCards.add(LOOSE_ALL_IN);
        
        //top 36%
        for(int i = 0; i < NUM_OK_CALLS; ++i) {
       // playerHoleCards.add("22+, A2s+, K7s+, Q7s+, J7s+, T7s+, 98s, 87s, 76s, A2o+, K7o+, Q8o+, J9o+, T9o, 98o");
            playerHoleCards.add(MY_LIST);
        }
        
        //top 18%
        for(int i = 0; i < NUM_GOOD_HANDS; ++i) {
        playerHoleCards.add("55+, A8s+, K9s+, QTs+, JTs, T9s, 98s, 87s, A8o+, KJo+, QJo, JTo");
        }
        
        for(int i = 0; i < NUM_RAISING_HANDS; ++i) {
            playerHoleCards.add("TT+, ATs+, KJs+, QTs+, JTs, T9s, 98s, AJo+, KQo+");
            }
        
       // 
        //playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+");
        //playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+");
        
        //vs crazy all ins (ie around 40% of the time)
        final String ALL_IN_DEFENSE = "A7o, KJo, 55+, QJs, KTs+, A6s+";
        
                
        String fileName = "C:\\codejam\\CodeJam\\simul\\out.xml";
        
        Tree tree = simulate(60001, playerHoleCards);
        tree.output(fileName);

        
        
    }
    
    public static interface iCriteria
    {
        public boolean matches(CompleteEvaluation[] evals);
        public boolean isApplicable(CompleteEvaluation eval);
    }
        
   
    
    
    
    
    public static Tree simulate(int TOTAL_SIMULATIONS, List<String> playerHoleCards) {
        
        Tree tree = new Tree();
        
        //Setup the ranges
        HoleCardsRange[] listRanges = new HoleCardsRange[playerHoleCards.size()];
        
        for(int i = 0; i < playerHoleCards.size(); ++i) {
            listRanges[i] = new HoleCardsRange(playerHoleCards.get(i));
        }
        
        int actualRounds = 0;
        
        final int numPlayers = playerHoleCards.size();
        
        double[] equity = new double[numPlayers];
        
        List<Criteria> criteres = Lists.newArrayList();
        
        List<Criteria> pairedBoardCriteres = Lists.newArrayList();
        List<Criteria> unPairedBoardCriteres = Lists.newArrayList();
        List<Criteria> allBoardCriteres = Lists.newArrayList();
        
        for(int round = 0; round < 3; ++round)
        {
            String roundStr =  
                round == 0 ? "FLOP " :
                (round == 1 ? "TURN " : "RIVER");
        
                        
            Criteria.addUnpairedBoardCriteria(round, roundStr, unPairedBoardCriteres);
            //Criteria.addPairedBoardCriteria(round, roundStr, pairedBoardCriteres);
            //addAnyBoardCriteria(round, roundStr, allBoardCriteres);
          //  Criteria.addAnyBoardCriteria(round, roundStr, allBoardCriteres);
           
            
        }
        
        criteres.addAll(pairedBoardCriteres);
        criteres.addAll(unPairedBoardCriteres);
       criteres.addAll(allBoardCriteres);
                
        long startTime = System.currentTimeMillis();
        
        for(int simulNum = 0; simulNum < TOTAL_SIMULATIONS; ++simulNum) {
            CompleteEvaluation[] evals = simulateOneRound(false, listRanges);
            
          //Does not match the ranges
            if (evals == null)
            {
                continue;
            }
            
            for(Criteria c : criteres) {
                log.debug("Calculate criteria {}", c.desc);
                c.calculate(evals);
            }
            
            tree.addCompleteEvaluation(evals[0]);
            
            ++actualRounds;
            
            
            
                        
            for(int p = 0; p < numPlayers; ++p) {
                equity[p] += evals[p].realEquity;
                
                
                //log.debug("eq {} player {}", evals[p].realEquity, p);
            }
                        
            if (simulNum >= 0 && simulNum % 5000 == 0) {
                logOutput.debug("# of simulations {} of {}", simulNum, TOTAL_SIMULATIONS );
                                
                logOutput.debug("\nPaired board criteria\n");
                for(Criteria c : pairedBoardCriteres) {
                    c.printMsg();
                }
                logOutput.debug("\nUn paired board criteria\n");
                for(Criteria c : unPairedBoardCriteres) {
                    c.printMsg();
                }
                logOutput.debug("\nAll board criteria\n");
                for(Criteria c : allBoardCriteres) {
                    c.printMsg();
                }
                
                logOutput.info("{} valid rounds", actualRounds);
                for(int p = 0; p < numPlayers; ++p) {
                                       
                    logOutput.info("Players {} {}", p, (equity[p] /actualRounds) * 100.0);
                }
            }
        }
        
        if (actualRounds == 0) {
            log.warn("none valid");
        }
        
        log.info("{} valid rounds", actualRounds);
        
        long endTime = System.currentTimeMillis();
        
        for(int p = 0; p < numPlayers; ++p) {
            equity[p] /= actualRounds;
            
            log.info("Players {} range {} =  {}", p, listRanges[p], equity[p] * 100);
        }
        
        logOutput.info("Seconds passed {}", (endTime-startTime) / 1000.0);
        
        return tree;
        
    }
    
    private static int[] availableCards = new int[52];
    private static Card[] flopTurnRiver = new Card[52];
    private static int[] availableHoleCards = new int[1326];    
    
    /**
     * Choisir au hasard deux cartes dans un éventail
     * 
     * @param usedCards si les cartes ont été utilisées  
     * @param range l'eventail de cartes possible
     * @return null si rien trouvé
     */
    private static HoleCards chooseValidAvailableCard(boolean[] usedCards, HoleCardsRange range) 
    {
        int numAvail = 0;
        log.debug("Choosing range {} ", range);
        
        for(int i = 0; i < range.getCardsList().size(); ++i)
        {
            HoleCards hc = range.getCardsList().get(i);
            if (!usedCards[hc.getCards()[0].index]
                   && !usedCards[hc.getCards()[1].index]) 
            {
                availableHoleCards[numAvail++] = i;
               // log.debug("{} available in {} ", hc, range);
            } else {
               // log.debug("{} not available in {} ", hc, range);
            }
            
        }
        
        if (numAvail == 0) 
            return null;
        
        int choice = (int) (Math.random() * numAvail);
        
        return range.getCardsList().get( availableHoleCards[choice] );
        
    }
    
    
    /**
     * Choiser le flop, turn, et river parmi les cartes disponibles
     * @param usedCards
     * @return
     */
    private static void chooseRemainingCards(boolean[] usedCards) {
        
        int numAvail = 0;
        
        for(int i = 0; i < 52; ++i) 
        {
            if (!usedCards[i] ) {
                availableCards[numAvail++] = i;
            }
        }
        
        
        for (int deckIdx = 0; deckIdx < 5; ++deckIdx) {
            // int from remainder of deck
            int r = deckIdx + (int) (Math.random() * (numAvail - deckIdx));
            int swap = availableCards[r];
            availableCards[r] = availableCards[deckIdx];
            availableCards[deckIdx] = swap;
            
            flopTurnRiver[deckIdx] = Card.listByIndex[availableCards[deckIdx]];
            
        }
        
        
    }
            
    
    //private static final int NUM_CARDS = 52;
    
    private static CompleteEvaluation[] simulateOneRound(boolean heroOnly, HoleCardsRange[] listRanges) {
        
        //final int numPlayers = listRanges.length;
        
        
        
        //See if the shuffle matches our ranges
        HoleCards[] holeCards = new HoleCards[listRanges.length];
        
        boolean[] usedCards = new boolean[52]; 
        
        
        
        
        for(int i = 0; i < listRanges.length; ++i) {
            
            HoleCardsRange range = listRanges[i];
            
            //"Shuffle" only what is needed, the next 2 cards
            
            //0  0 1
            //1  2 3
            //2  4 5
            //3  6 7
            //x  2x 2x+1
            
            /*
             * List of holeCards, need to choose among those still left
             */
            
            HoleCards hc = chooseValidAvailableCard(usedCards, range);
            
            if (hc == null) {
                log.debug("No hole cards found for range {}  " +
                		"prev holeCards {}", range, holeCards );
                return null;
            }
            
            usedCards[ hc.getCards()[0].index ] = true;
            usedCards[ hc.getCards()[1].index ] = true;
            holeCards[i] = hc;
            
            //log.debug("Hole cards ({}) {} idx 1 {} idx 2 {}", i, holeCards[i], card1Index, card2Index);
        }
        
        //Everything matches evaluate
        
        //Pick the 5 remaining cards for the flop, turn, and river
        chooseRemainingCards(usedCards);
        
        CompleteEvaluation[] evals = EvalHands.evaluate(heroOnly, holeCards, flopTurnRiver); 
                
        
        /*
        if (evals[1].won == true) {
            
            log.debug("\nh1 {} eval: {}  \n h2  {} eval: {}  \n Flop {} turn {} river {}", 
                    holeCards[0], evals[0].toString(),
                    holeCards[1], evals[1].toString(),
                    flop, 
                    Card.listByIndex[ flopTurnRiver[3] ], Card.listByIndex[ flopTurnRiver[4] ]);
        }*/
        return evals;
    }
}

