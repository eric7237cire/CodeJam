package pkr;

import java.util.Arrays;

import com.google.common.base.Preconditions;

public class HoleCards {
    private Card[] cards;

    public HoleCards(Card card1, Card card2) {
        super();
        this.cards = new Card[] {card1, card2 };
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
        
        if (rhs.cards[0] == cards[1] && rhs.cards[1] == cards[0])
            return true;
        
        return false;
    }
    
    
    
}
