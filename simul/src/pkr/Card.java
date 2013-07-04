package pkr;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public class Card implements Comparable<Card>{
    private Suit suit;
    
    private CardRank rank;
    
    public  static Card[] listByIndex;

    static {
        listByIndex = new Card[52];
        for(int i = 0; i < 52; ++i) {
            listByIndex[i] = new Card(i);
        }
    }
    
    public Card(Suit suit, CardRank rank) {
        
        super();
        this.suit = suit;
        this.rank = rank;
        Preconditions.checkNotNull(suit);
        Preconditions.checkNotNull(rank);
    }
    
    public int toInt() {
        return suit.ordinal() * 13 + rank.getIndex();
    }
    
    public Card(int deckNumber) {
        int suitInt = deckNumber / 13;
        int rankInt = deckNumber % 13;
        
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
        
        return  new Card(suit, rank);
    }
    public static Card[] parseCards(String allCardsStr) {
        String[] tokens = allCardsStr.toUpperCase().split("\\s+");
        Card[] cards = new Card[tokens.length];
        
        for(int i = 0; i < tokens.length; ++i) {
            
            String cardStr = tokens[i];
                        
            cards[i] = parseCard(cardStr);
        }
        
        return cards;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Card rhs) {
        return ComparisonChain.start().compare(toInt(), rhs.toInt()).result();
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

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rank == null) ? 0 : rank.hashCode());
        result = prime * result + ((suit == null) ? 0 : suit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        Preconditions.checkState(false);
        return false;
    }
    
    
}
