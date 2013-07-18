package pkr;

import java.util.Arrays;

import com.google.common.base.Preconditions;

/**
 * 
 * 2nd card is always > in index
 *
 */
public class HoleCards {
    private Card[] cards;

    @Override
    public String toString()
    {
        return "[" + cards[0] + " " + cards[1] + "]";
    }

    private static HoleCards[] mapHoleCards;
    private static  int[][] indexes;
    
    
    static {
        
        indexes = new int[52][52];
        mapHoleCards = new HoleCards[52 * 26];
        
        int index = 0;
        for(int i = 0; i < 52; ++i) 
        {
            for(int j = i+1; j < 52; ++j) 
            {
                indexes[i][j] = index;
                indexes[j][i] = index;
                mapHoleCards[index++] =  new HoleCards(new Card(i), new Card(j));
            }
        }
    }
    
    boolean isSuited() 
    {
        return cards[0].getSuit() == cards[1].getSuit();
    }
    
    //TODO make this faster, auto mapping to integer based on card indexes
    public String toStartingHandString() {
        if (cards[0].getRank() == cards[1].getRank()) {
            return "" + cards[0].getRank().toChar() + cards[0].getRank().toChar();
        } 
        
        
        if (cards[0].getRank().getIndex() > cards[1].getRank().getIndex()) {
            return "" + cards[0].getRank().toChar() + cards[1].getRank().toChar() + (isSuited() ? 's' : 'o');
        }
        
        return "" + cards[1].getRank().toChar() 
                + cards[0].getRank().toChar() + (isSuited() ? 's' : 'o');
    }
    
    /**
     * Order does not matter of card1 / card 2
     * @param card1
     * @param card2
     * @return
     */
    public static HoleCards getByIndices(int card1, int card2) {
        return mapHoleCards[ indexes[card1][card2] ];
    }
    
    
    
    public HoleCards(Card card1, Card card2) {
        super();
        if (card1.toInt() > card2.toInt()) {
            this.cards = new Card[] {card2, card1 };
        } else {
            this.cards = new Card[] {card1, card2 };
        }
        
        Preconditions.checkState(CardRank.compare(cards[0].getRank(),
                cards[1].getRank()) <= 0);
    }
    
    public HoleCards(Card[] cards) {
        super();
        Preconditions.checkArgument(cards.length == 2);
        this.cards = cards;
        
        if (cards[0].toInt() > cards[1].toInt()) {
            Card tmp = cards[0];
            cards[0] = cards[1];
            cards[1] = tmp;
        }
        
        Preconditions.checkState(CardRank.compare(cards[0].getRank(),
                cards[1].getRank()) <= 0);
    }
    
    public CardRank getHigherRank() {
        return cards[1].getRank();
    }
    
    public CardRank getLowerRank() {
        return cards[0].getRank();
    }

    /**
     * @return the cards
     */
    public Card[] getCards() {
        return cards;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(cards);
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HoleCards rhs = (HoleCards) obj;
        
        if (rhs.cards[0].toInt() == cards[0].toInt() &&
                rhs.cards[1].toInt() == cards[1].toInt())
            return true;
        
        //if (rhs.cards[0] == cards[1] && rhs.cards[1] == cards[0])
          //  return true;
        
        return false;
    }
    
    
    
}
