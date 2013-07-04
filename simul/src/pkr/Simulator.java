package pkr;

import java.util.List;

import com.google.common.collect.Lists;

public class Simulator {
    void simulate(List<String> playerHoleCards) {
        //Setup the ranges
        HoleCardsRange[] listRanges = new HoleCardsRange[playerHoleCards.size()];
        
        for(int i = 0; i < playerHoleCards.size(); ++i) {
            listRanges[i] = new HoleCardsRange(playerHoleCards.get(i));
        }
        
        
    }
    
    private static final int NUM_CARDS = 52;
    
    private Evaluation[] simulateOneRound(HoleCardsRange[] listRanges) {
        
        final int numPlayers = listRanges.length;
        
        int deck[] = new int[NUM_CARDS];
        for (int i = 0; i < NUM_CARDS; i++)
            deck[i] = i;
        
        for (int i = 0; i < NUM_CARDS; i++) {
            // int from remainder of deck
            int r = i + (int) (Math.random() * (NUM_CARDS - i));
            int swap = deck[r];
            deck[r] = deck[i];
            deck[i] = swap;
        }
     
        //See if the shuffle matches our ranges
        HoleCards[] holeCards = new HoleCards[listRanges.length];
        
        for(int i = 0; i < listRanges.length; ++i) {
            
            
            //0  0 1
            //1  2 3
            //2  4 5
            //3  6 7
            //x  2x 2x+1
            int card1Index = 2*i;
            int card2Index = 2*i+1;
            
            if (!listRanges[i].inRange(card1Index, card2Index))
                return null;
            
            holeCards[i] = HoleCards.getByIndices(card1Index, card2Index);
        }
        
        //Everything matches evaluate
        
        
        Evaluation[] evals = EvalHands.evaluate(holeCards, new Flop(new Card[] { 
                new Card(2 * numPlayers),
                new Card(2 * numPlayers+1),
                new Card(2 * numPlayers+2)
        }), new Card(2 * numPlayers+3), new Card(2 * numPlayers+4));
        
        return evals;
    }
}

