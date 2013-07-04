package pkr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Simulator {
    
    private static Logger log = LoggerFactory.getLogger("main");
    
    public static void main(String[] args) {
        List<String> playerHoleCards = Lists.newArrayList();
        
       // playerHoleCards.add("AA, KK, QQ, JJ, TT, 99, 88, 77, 66, 55, 44, 33, 22, AK, AQ, AJ");
        //playerHoleCards.add("AA, AKs, 27, 93, 44, 99");
      playerHoleCards.add("AA");
        //playerHoleCards.add("JJ");
       // playerHoleCards.add("27o, 38s");
      playerHoleCards.add("27o");
         
        
        
        simulate(playerHoleCards);
    }
    public static void simulate(List<String> playerHoleCards) {
        //Setup the ranges
        HoleCardsRange[] listRanges = new HoleCardsRange[playerHoleCards.size()];
        
        for(int i = 0; i < playerHoleCards.size(); ++i) {
            listRanges[i] = new HoleCardsRange(playerHoleCards.get(i));
        }
        
        int actualRounds = 0;
        
        final int numPlayers = playerHoleCards.size();
        
        double[] equity = new double[numPlayers];
        
        final int TOTAL_SIMULATIONS = 1000000;
        for(int simulNum = 0; simulNum < TOTAL_SIMULATIONS; ++simulNum) {
            Evaluation[] evals = simulateOneRound(listRanges);
            
            if (simulNum % 50000 == 0) {
                log.debug("# of simulations {} of {}", simulNum, TOTAL_SIMULATIONS );
                
                
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
            
            log.info("Players {} {}", p, equity[p] * 100);
        }
        
    }
    
    private static int[] availableCards = new int[52];
    
    private static int chooseValidAvailableCard(boolean[] usedCards, boolean[] validCards) {
       
        int numAvail = 0;
        
        for(int i = 0; i < 52; ++i) 
        {
            if (!usedCards[i] && validCards[i]) {
                availableCards[numAvail++] = i;
            }
        }
        
        if (numAvail == 0)
            return -1;
        
        int choice = (int) (Math.random() * numAvail);
        
        return availableCards[choice];
        
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
            
    
    private static final int NUM_CARDS = 52;
    
    private static Evaluation[] simulateOneRound(HoleCardsRange[] listRanges) {
        
        final int numPlayers = listRanges.length;
        
        //int deck[] = new int[NUM_CARDS];
        /*
        for (int i = 0; i < NUM_CARDS; i++)
            deck[i] = i;
        */
        
        //See if the shuffle matches our ranges
        HoleCards[] holeCards = new HoleCards[listRanges.length];
        
        boolean[] usedCards = new boolean[52]; 
        
        
        
        
        for(int i = 0; i < listRanges.length; ++i) {
            
            HoleCardsRange range = listRanges[i];
            
            //"Shuffle" only what is needed, the next 2 cards
            /*
            for (int deckIdx = 2*i; deckIdx <= 2*i + 1; ++deckIdx) {
                // int from remainder of deck
                int r = deckIdx + (int) (Math.random() * (NUM_CARDS - deckIdx));
                int swap = deck[r];
                deck[r] = deck[deckIdx];
                deck[deckIdx] = swap;
            }*/
            
            //0  0 1
            //1  2 3
            //2  4 5
            //3  6 7
            //x  2x 2x+1
            int card1Index = chooseValidAvailableCard(usedCards, range.inRangeCard1 );
            
            if (card1Index == -1)
                return null;
            
            usedCards[card1Index] = true;
            
            
            int card2Index = chooseValidAvailableCard(usedCards, range.mask[card1Index]
                    );
            
            usedCards[card2Index] = true;
            if (card2Index == -1)
                return null;
            
            if (!listRanges[i].inRange(card1Index, card2Index))
                return null;
            
            holeCards[i] = HoleCards.getByIndices(card1Index, card2Index);
            
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
        Evaluation[] evals = EvalHands.evaluate(holeCards, 
                flop,  Card.listByIndex[ flopTurnRiver[3] ], 
        Card.listByIndex[ flopTurnRiver[4] ]);
        
        
        if (evals[1].won = true) {
            //log.debug("Flop {} turn {} river {}", flop, Card.listByIndex[ flopTurnRiver[3] ], Card.listByIndex[ flopTurnRiver[4] ]);
        }
        return evals;
    }
}

