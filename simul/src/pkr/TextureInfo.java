package pkr;

import static pkr.Card.NUM_RANKS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TextureInfo {
    
    List<Card> cards;
    List<Card> sortedCards;
    
    int rankBitmask;
    int[] freqCard = new int[NUM_RANKS];
    int[] freqSuit = new int[4];
    
    int fourKind = -1;
    int threeKind = -1;
    int firstPair = -1;
    int secondPair = -1;
    int straightRank = -1;
    
    boolean flush;
    Suit flushSuit;
    
    int highestFreqSuit = 0;
    
    public TextureInfo() {
        super();
        this.cards = new ArrayList<>(7);
        this.sortedCards = new ArrayList<>(7);
    }



    private static int[] straightBitMasks = {
        15 + (1 << 12), // a + 2 to 4
        31, 31 << 1, 31 << 2, 31 << 3, 31 << 4, 31 << 5, 31 << 6, 31 << 7,
        31 << 8 };

    public CardRank getHighestRank() 
    {
        return sortedCards.get(sortedCards.size()-1).getRank();
    }
    public CardRank getHighestRank(CardRank ignore1, CardRank ignore2)
    {
        int rank = rankBitmask;
        if (ignore1 != null)
            rank ^= (1 << ignore1.getIndex());
        
        if (ignore2 != null)
            rank ^= (1 << ignore2.getIndex());
        
        //19 -> 12
        //20 -> 11
        //21 -> 10
        int bestRank = 12 - (Integer.numberOfLeadingZeros(rank) - 19);
        
        return CardRank.ranks[bestRank];
    }
    
    public void calculate() {
        
        

        for (int r = NUM_RANKS-1; r >= 0; --r) {
            if (freqCard[r] == 4) {
                fourKind = r;
            } else if (threeKind == -1 && freqCard[r] == 3) {
                threeKind = r;
                
            } else if ( (freqCard[r] == 2 || freqCard[r] == 3)  && firstPair == -1) {
                //first pair could be trips, ie 333222 really has a pair of 2's
                firstPair = r;
            } else if (freqCard[r] == 2 && secondPair == -1) {
                secondPair = r;
            } 
        }
        
        

        for (int sbmIdx = straightBitMasks.length - 1; sbmIdx >= 0 ; --sbmIdx) {
            if ( (rankBitmask & straightBitMasks[sbmIdx]) == straightBitMasks[sbmIdx] ) {
                straightRank = sbmIdx + 3;
                break;
            }
        }
        
        Collections.sort(sortedCards);
    }
    
    public void addCard(Card card) {
        if (card == null)
            return;
        rankBitmask |= (1 << card.getRank().getIndex());
        freqCard[card.getRank().getIndex()]++;
        freqSuit[card.getSuit().ordinal()]++;
        
        if (freqSuit[card.getSuit().ordinal()] > highestFreqSuit) {
            highestFreqSuit = freqSuit[card.getSuit().ordinal()];
            
            if (highestFreqSuit >= 5) {
                flush = true;
                flushSuit = card.getSuit();
            }
        }
        
        
        
        this.cards.add(card);
        this.sortedCards.add(card);
    }
    
    public boolean noPairedCards() {
        return firstPair == -1;
    }
    
    public void addCards(Collection<Card> cards) 
    {
        for(Card card : cards) {
            addCard(card);
        }
    }
    
    public void addCards(Card[] cards) 
    {
        for(Card card : cards) {
            addCard(card);
        }
    }



    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TextureInfo [cards=" + cards + "]";
    }
}
