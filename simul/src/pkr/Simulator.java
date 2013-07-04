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
        playerHoleCards.add("JJ");
         
        
        
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
        
        final int TOTAL_SIMULATIONS = 50000000;
        for(int simulNum = 0; simulNum < TOTAL_SIMULATIONS; ++simulNum) {
            Evaluation[] evals = simulateOneRound(listRanges);
            
            if (simulNum % 5000000 == 0) {
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
    
    private static final int NUM_CARDS = 52;
    
    private static Evaluation[] simulateOneRound(HoleCardsRange[] listRanges) {
        
        final int numPlayers = listRanges.length;
        
        int deck[] = new int[NUM_CARDS];
        for (int i = 0; i < NUM_CARDS; i++)
            deck[i] = i;
        
        //See if the shuffle matches our ranges
        HoleCards[] holeCards = new HoleCards[listRanges.length];
        
        for(int i = 0; i < listRanges.length; ++i) {
            
            //"Shuffle" only what is needed, the next 2 cards
            for (int deckIdx = 2*i; deckIdx <= 2*i + 1; ++deckIdx) {
                // int from remainder of deck
                int r = deckIdx + (int) (Math.random() * (NUM_CARDS - deckIdx));
                int swap = deck[r];
                deck[r] = deck[deckIdx];
                deck[deckIdx] = swap;
            }
            
            //0  0 1
            //1  2 3
            //2  4 5
            //3  6 7
            //x  2x 2x+1
            int card1Index = deck[ 2*i ];
            int card2Index = deck[ 2*i+1 ];
            
            if (!listRanges[i].inRange(card1Index, card2Index))
                return null;
            
            holeCards[i] = HoleCards.getByIndices(card1Index, card2Index);
            
            //log.debug("Hole cards ({}) {} idx 1 {} idx 2 {}", i, holeCards[i], card1Index, card2Index);
        }
        
        //Everything matches evaluate
        
        //Pick the 5 remaining cards for the flop, turn, and river
        for (int deckIdx = 2*numPlayers; deckIdx <= 2*numPlayers + 4; ++deckIdx) {
            // int from remainder of deck
            int r = deckIdx + (int) (Math.random() * (NUM_CARDS - deckIdx));
            int swap = deck[r];
            deck[r] = deck[deckIdx];
            deck[deckIdx] = swap;
        }
        
        Evaluation[] evals = EvalHands.evaluate(holeCards, new Flop(new Card[] { 
                Card.listByIndex[ deck[ 2 * numPlayers ] ],
                Card.listByIndex[ deck[ 2 * numPlayers + 1] ],
                Card.listByIndex[ deck[ 2 * numPlayers + 2] ]
                
        }),  Card.listByIndex[ deck[ 2 * numPlayers + 3] ], 
        Card.listByIndex[ deck[ 2 * numPlayers + 4] ]);
        
        return evals;
    }
}

