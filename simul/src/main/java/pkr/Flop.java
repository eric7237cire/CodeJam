package pkr;


public class Flop {
    private Card[] cards;    
    
    
    public Flop(Card card1, Card card2, Card card3) {
        super();
        this.cards = new Card[] {card1, card2, card3 };
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

    
    
}
