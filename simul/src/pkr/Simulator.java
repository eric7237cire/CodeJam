package pkr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.possTree.Tree;

import com.google.common.collect.Lists;

public class Simulator {
    
    private static Logger log = LoggerFactory.getLogger("main");
    
    public static void main(String[] args) {
        List<String> playerHoleCards = Lists.newArrayList();
        
       // playerHoleCards.add("AA, KK, QQ, JJ, TT, 99, 88, 77, 66, 55, 44, 33, 22, AK, AQ, AJ");
        //playerHoleCards.add("AA, AKs, 27, 93, 44, 99");
      playerHoleCards.add("KTs");
      
      playerHoleCards.add("72o, 32");
      playerHoleCards.add("27s");
    //  playerHoleCards.add("Q2s+, J2+, T2+, 32+");
      //playerHoleCards.add("Q2s+, J2+, T2+, 32+");
       // playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+");
        //playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+");
        //playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+");
        
        
        //BUG with this range
        //playerHoleCards.add("JJ, KJ");
       // playerHoleCards.add(HoleCardsRange.SUITED_ACES);
        
        String fileName = "C:\\codejam\\CodeJam\\simul\\out.xml";
        
        Tree tree = simulate(playerHoleCards);
        tree.output(fileName);

       // playerHoleCards.add("JJ, KJo");
       // playerHoleCards.add("27o");
        
       // playerHoleCards.clear(); 
       // playerHoleCards.add("AA");
     //  playerHoleCards.add("AQs");
      // playerHoleCards.add("KJo");
        
        
    }
    public static Tree simulate(List<String> playerHoleCards) {
        
        Tree tree = new Tree();
        
        //Setup the ranges
        HoleCardsRange[] listRanges = new HoleCardsRange[playerHoleCards.size()];
        
        for(int i = 0; i < playerHoleCards.size(); ++i) {
            listRanges[i] = new HoleCardsRange(playerHoleCards.get(i));
        }
        
        int actualRounds = 0;
        
        final int numPlayers = playerHoleCards.size();
        
        double[] equity = new double[numPlayers];
        
        final int TOTAL_SIMULATIONS = 10000;
        for(int simulNum = 0; simulNum < TOTAL_SIMULATIONS; ++simulNum) {
            CompleteEvaluation[] evals = simulateOneRound(listRanges);
            
            tree.addCompleteEvaluation(evals[0]);
            
            if (simulNum % 150000 == 0) {
                log.debug("# of simulations {} of {}", simulNum, TOTAL_SIMULATIONS );
                
                log.info("{} valid rounds", actualRounds);
                for(int p = 0; p < numPlayers; ++p) {
                                       
                    log.info("Players {} {}", p, (equity[p] /actualRounds) * 100.0);
                }
            }
            
            //Does not match the ranges
            if (evals == null)
                continue;
            
            ++actualRounds;
            
            for(int p = 0; p < numPlayers; ++p) {
                equity[p] += evals[p].realEquity;
                
                //log.debug("eq {} player {}", evals[p].realEquity, p);
            }
        }
        
        if (actualRounds == 0) {
            log.warn("none valid");
        }
        
        log.info("{} valid rounds", actualRounds);
        for(int p = 0; p < numPlayers; ++p) {
            equity[p] /= actualRounds;
            
            log.info("Players {} range {} =  {}", p, listRanges[p], equity[p] * 100);
        }
        
        return tree;
        
    }
    
    private static int[] availableCards = new int[52];
    private static int[] availableHoleCards = new int[1326];    
    
    private static HoleCards chooseValidAvailableCard(boolean[] usedCards, HoleCardsRange range) 
    {
        int numAvail = 0;
        
        
        for(int i = 0; i < range.getCardsList().size(); ++i)
        {
            HoleCards hc = range.getCardsList().get(i);
            if (!usedCards[hc.getCards()[0].toInt()]
                   && !usedCards[hc.getCards()[1].toInt()]) 
            {
                availableHoleCards[numAvail++] = i;
            }
            
            ++i;
        }
        
        if (numAvail == 0) 
            return null;
        
        int choice = (int) (Math.random() * numAvail);
        
        return range.getCardsList().get( availableHoleCards[choice] );
        
    }
    
    
    
    private static int[] chooseRemainingCards(boolean[] usedCards) {
        
        int numAvail = 0;
        
        for(int i = 0; i < 52; ++i) 
        {
            if (!usedCards[i] ) {
                availableCards[numAvail++] = i;
            }
        }
        
        int[] remCards = new int[5];
        
        for (int deckIdx = 0; deckIdx < 5; ++deckIdx) {
            // int from remainder of deck
            int r = deckIdx + (int) (Math.random() * (numAvail - deckIdx));
            int swap = availableCards[r];
            availableCards[r] = availableCards[deckIdx];
            availableCards[deckIdx] = swap;
            
            remCards[deckIdx] = availableCards[deckIdx];
        }
        
        return remCards;
        
    }
            
    
    //private static final int NUM_CARDS = 52;
    
    private static CompleteEvaluation[] simulateOneRound(HoleCardsRange[] listRanges) {
        
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
            
            if (hc == null)
                return null;
            
            usedCards[ hc.getCards()[0].toInt() ] = true;
            usedCards[ hc.getCards()[1].toInt() ] = true;
            holeCards[i] = hc;
            
            //log.debug("Hole cards ({}) {} idx 1 {} idx 2 {}", i, holeCards[i], card1Index, card2Index);
        }
        
        //Everything matches evaluate
        
        //Pick the 5 remaining cards for the flop, turn, and river
        int[] flopTurnRiver = chooseRemainingCards(usedCards);
        
        Flop flop = new Flop(new Card[] { 
                Card.listByIndex[ flopTurnRiver[0] ],
                Card.listByIndex[ flopTurnRiver[1] ],
                Card.listByIndex[ flopTurnRiver[2] ]
        });
        CompleteEvaluation[] evals = EvalHands.evaluate(holeCards, 
                flop,  Card.listByIndex[ flopTurnRiver[3] ], 
        Card.listByIndex[ flopTurnRiver[4] ]);
        
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

