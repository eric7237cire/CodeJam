package pkr;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.Lists;

/**
 * 
 * 2nd card is always > in index
 *
 */
public class HoleCards {
    private Card[] cards;

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
                mapHoleCards[index++] =  new HoleCards(new Card(i), new Card(j));
            }
        }
    }
    
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
    }
    
    public HoleCards(Card[] cards) {
        super();
        Preconditions.checkArgument(cards.length == 2);
        this.cards = cards;
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
        
        if (rhs.cards[0] == cards[0] && rhs.cards[1] == cards[1])
            return true;
        
        //if (rhs.cards[0] == cards[1] && rhs.cards[1] == cards[0])
          //  return true;
        
        return false;
    }
    
    
    
}
