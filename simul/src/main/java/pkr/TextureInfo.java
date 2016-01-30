package pkr;

import static pkr.Card.NUM_RANKS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/*
 * 
 * Given a set of cards, functions about cards
 */
public class TextureInfo {
    
    public List<Card> cards;
    public List<Card> sortedCards;
    
    int rankBitmask;
    public int[] freqCard = new int[NUM_RANKS];
    public int[] freqSuit = new int[4];
    
    public int fourKind = -1;
    public int threeKind = -1;
    public int firstPair = -1;
    public int secondPair = -1;
    public int straightRank = 0;
    
    //any straight is possible
    public boolean straightPossible;
    
    public boolean flush;
    public Suit flushSuit;
    
    // blank A (1) 2 (2) ... K (13) A (14) blank (15)
    //int[] conseqCardsLeft = new int[16];
   // int[] conseqCardsRight = new int[16];
    
    
    //Cards needed to complete straight ; index is straight rank 5 to ace
    public int[] cardsNeededForStraight = new int[NUM_RANKS];
    
    //index is  rank
    public int[] cardMakesStraight = new int[NUM_RANKS];
    
    //A -- 0 2 -- 1  K -- 12 A -- 13
    boolean[] straightRanks = new boolean[14];
    
    public int highestFreqSuit = 0;
    
    
    
    public TextureInfo() {
        super();
        this.cards = new ArrayList<>(7);
        this.sortedCards = new ArrayList<>(7);
    }



    public CardRank getHighestRank() 
    {
        return sortedCards.get(sortedCards.size()-1).getRank();
    }
    
    public int getRanksAbove(CardRank rank) 
    {
        int ranksAbove = 0;
        for( int i = sortedCards.size() - 1; i >= 0; --i)
        {
            CardRank boardRank = sortedCards.get(i).getRank();
            if (boardRank.getIndex() <= rank.getIndex()) {
                return ranksAbove;
            }
            
            ranksAbove++;
        }
        
        return ranksAbove;
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
    
    private void calculateStraightInfo()
    {
        int missingRank = -1;
        int missing = 0;
        
        
        for(int strRank = 0; strRank <= 13; ++strRank)
        {
            //card no longer in the straight is no longer missing
            if (strRank >= 5 && !straightRanks[strRank-5]) {
                missing --;
            }
            
            if(!straightRanks[strRank]) {
                missing++;
                missingRank = strRank;
            }
            
            if (strRank < 4) 
                continue;
            
            cardsNeededForStraight[strRank-1] = missing;
            
            if (missing == 1) {
                if (missingRank == 0) {
                    cardMakesStraight[CardRank.ACE.getIndex()] = strRank - 1;
                } else {
                    cardMakesStraight[missingRank-1] = strRank - 1;
                }
            } else if (missing == 0) {
                straightRank = strRank - 1;
            } else if (missing == 2) {
                straightPossible = true;
            }
        }
    }
    
    public void calculate() {
        
        firstPair = secondPair = threeKind = fourKind = -1;

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
            
            /*
            if (freqCard[r] > 0 ) {
                conseqCardsRight[r+2] = conseqCardsRight[r+3] + 1;
            }*/
        }
        /*
        if (freqCard[CardRank.ACE.getIndex()] > 0) {
            conseqCardsLeft[1] = 1;
        }
        
        // A-1  K-13 A-14 
        for (int r = 0; r < NUM_RANKS; ++r) {
            if (freqCard[r] > 0 ) {
                conseqCardsLeft[r+2] = 
                         1 + conseqCardsLeft[r+1];
            }
        }
        
        if (freqCard[CardRank.ACE.getIndex()] > 0) {
            conseqCardsRight[1] = conseqCardsRight[1] + 1;
        }*/

        /*
        for (int sbmIdx = straightBitMasks.length - 1; sbmIdx >= 0 ; --sbmIdx) {
            if ( (rankBitmask & straightBitMasks[sbmIdx]) == straightBitMasks[sbmIdx] ) {
                straightRank = sbmIdx + 3;
                break;
            }
        }*/
        
        calculateStraightInfo();
        
        Collections.sort(sortedCards);
    }
    
    public int getStraightDrawCount() {
        
        int straightDraws = 0;
                
        for(int cardRank = 0; cardRank <= 12; ++cardRank)
        {
            if (cardMakesStraight[cardRank] != 0 && cardMakesStraight[cardRank] > straightRank)
            {
                ++straightDraws;
            }
        }
        
        return straightDraws;
    }
    
    public boolean hasStraight() 
    {
        return straightRank > 0;
    }
    
    public void addCard(Card card) {
        if (card == null)
            return;
        rankBitmask |= (1 << card.getRank().getIndex());
        freqCard[card.getRank().getIndex()]++;
        freqSuit[card.getSuit().ordinal()]++;
        straightRanks[1+card.getRank().getIndex()] = true;
        
        if (card.getRank() == CardRank.ACE) {
            straightRanks[0] = true;
        }
        
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
    
    public boolean hasTwoPair() {
        return firstPair >= 0 && secondPair >= 0;
    }
    
    public boolean hasFullHouse() {
        return threeKind >= 0 && firstPair >= 0;
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

    public void addCards(Card[] cards, int num) 
    {
        for(int i = 0; i < num; ++i) {
            addCard(cards[i]);
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
