package pkr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

public class Card implements Comparable<Card>
{
    private static Logger log = LoggerFactory.getLogger(Card.class);
    
    private Suit suit;
    
    private CardRank rank;
    
    public final int index;
    
    public static final int NUM_RANKS = 13;
    
    public  static Card[] listByIndex;

    static {
        listByIndex = new Card[52];
        for(int i = 0; i < 52; ++i) {
            listByIndex[i] = new Card(i);
        }
    }
    /*
     * 0 - 2
     * 1 - 2
     * 2 - 2
     * 3 - 2
     * 4 - 3
     * 5 - 3 
     * etc
     */
    
    public static Card getCard(Suit suit, CardRank rank) {
        
        return listByIndex[rank.getIndex() * 4 + suit.ordinal()];
       
    }
    
    
    
    private Card(int deckNumber) {
        int rankInt = deckNumber / 4;
        int suitInt = deckNumber % 4;
        index = deckNumber;
        
        Suit suit = Suit.getFromValue(suitInt);
        
        CardRank rank = CardRank.getFromZeroBasedValue(rankInt);
        
        this.suit = suit;
        this.rank = rank;
    }
    
    public static Card parseCard(String cardStr) {
        Preconditions.checkArgument(cardStr.length() == 2);
        
        CardRank rank = CardRank.fromChar(cardStr.charAt(0));
        
        Preconditions.checkArgument(rank != null, cardStr);
        
        Suit suit = null;
        
        switch(cardStr.charAt(1)) {
        case 's': case 'S':
            suit = Suit.SPADES;
            break;
        case 'c': case 'C':
            suit = Suit.CLUBS;
            break;
        case 'h': case 'H':
            suit = Suit.HEARTS;
            break;
        case 'd': case 'D':
            suit = Suit.DIAMONDS;
            break;
        
        }
        
        Preconditions.checkArgument(suit != null, cardStr);
        
        return  getCard(suit, rank);
    }
    
    /**
     * white space not needed if 2 cards
     * @param allCardsStr can be AcKs or Ac Ks Qj 2d Ad 
     * @return
     */
    public static Card[] parseCards(String allCardsStr) {
        String[] tokens = null;
        
        if (allCardsStr == null) {
            return new Card[0];
        }
        
        if (allCardsStr.length() == 4) {
            tokens = new String[] { allCardsStr.substring(0, 2),
                    allCardsStr.substring(2, 4) };
            
        } else {
            tokens = allCardsStr.toUpperCase().split("\\s+");
        }
        
        List<Card> cards = Lists.newArrayList();
        for(int i = 0; i < tokens.length; ++i) 
        {
            
            String cardStr = tokens[i];
                        
            try {
                Card card = parseCard(cardStr);
                cards.add(card);
            } catch (IllegalArgumentException ex) {
                
            } catch (IllegalStateException ex) {
                log.warn(cardStr, ex);
            }
        }
        
        Card[] cardsArr = new Card[cards.size()];
        
        cardsArr = cards.toArray(cardsArr);
        
        return cardsArr;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Card rhs) {
        return ComparisonChain.start().compare(index, rhs.index).result();
    }

    /**
     * @return the suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * @return the rank
     */
    public CardRank getRank() {
        return rank;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        
        return "" + rank.toChar() + suit.toChar();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        return result;
    }

    

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
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
        Card other = (Card) obj;
        if (index != other.index)
            return false;
        return true;
    }

    
    
}
