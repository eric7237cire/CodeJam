package pkr;

import java.util.Arrays;

import com.google.common.base.Preconditions;

public class Flop {
    private Card[] cards;
    private Card[] sortedCards;
    
    public final boolean isPaired;
    public final boolean isTriped;
    public final boolean isRainbow;
    public final boolean is2Suit;
    public final boolean is3Suit;
    
    public CardRank pairRank;
    
    public Flop(Card card1, Card card2, Card card3) {
        super();
        this.cards = new Card[] {card1, card2, card3 };
        
        this.sortedCards = new Card[] {card1, card2, card3 };
        Arrays.sort(this.sortedCards);
        
        if (card1.getRank() == card2.getRank())
        {
            if (card1.getRank() == card3.getRank())
            {
                isPaired = false;
                isTriped = true;
            } else {
                isPaired = true;
                pairRank = card1.getRank();
                isTriped = false;
            }
        } else if (card1.getRank() == card3.getRank()) 
        {
            isPaired = true;
            pairRank = card1.getRank();
            isTriped = false;
        } else if (card2.getRank() == card3.getRank()) 
        {
            pairRank = card2.getRank();
            isPaired = true;
            isTriped = false;
        } else {
            isPaired = false;
            isTriped = false;
        }
        
        if (card1.getSuit() == card2.getSuit())
        {
            isRainbow = false;
            if (card1.getSuit() == card3.getSuit())
            {                
                is2Suit = false;
                is3Suit = true;
            } else {
                is2Suit = true;
                is3Suit = false;
            }
        } else if (card1.getSuit() == card3.getSuit()) 
        {
            isRainbow = false;
            is2Suit = true;
            is3Suit = false;
        } else if (card2.getSuit() == card3.getSuit()) 
        {
            isRainbow = false;
            is2Suit = true;
            is3Suit = false;
        } else {
            isRainbow = true;
            is2Suit = false;
            is3Suit = false;
        }
    }
    
    public Flop(Card[] cards) {
        this(cards[0], cards[1], cards[2]);
        
    }

    /**
     * @return the cards
     */
    public Card[] getCards() {
        return cards;
    }

    @Override
    public String toString()
    {
        return "[ " + cards[0] + " " + cards[1] + " " + cards[2] + "]";
    }

    public Card[] getSortedCards()
    {
        return sortedCards;
    }

    public void setSortedCards(Card[] sortedCards)
    {
        this.sortedCards = sortedCards;
    }
    
    
}
