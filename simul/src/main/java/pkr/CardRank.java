package pkr;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public enum CardRank  
{
    
    ACE("A"), KING("K"), 
    QUEEN("Q"), JACK("J"), TEN("T"), NINE("9"),
    EIGHT("8"),
    SEVEN("7"), SIX("6"),
    FIVE("5"), FOUR("4"), THREE("3"), TWO("2");
    
    private static BiMap<Integer, CardRank> cardMap = HashBiMap.create(13);
    
    //2 to Ace
    public static CardRank[] ranks;
    
    private char theChar;
    
    static {
        ranks = new CardRank[13];
        
        for (CardRank cardRank : CardRank.values()) {
            cardMap.put(12 - cardRank.ordinal(), cardRank);
            ranks[12 - cardRank.ordinal()] = cardRank;
        }
    }
    private CardRank(String str) {
      theChar = str.charAt(0);
    }
    
    
    
    public static CardRank getFromZeroBasedValue(int value) {
        return cardMap.get(value);
    }
    
    /**
     * 14 or 1 for ace
     * @param value
     * @return
     */
    private static CardRank getFromFaceValue(int value) {
        if (value == 1 || value == 14)
            return CardRank.ACE;
        
        return cardMap.get(value - 2);
    }
    
    /*
     * 12 for ace, 0 for two
     */
    public int getIndex() {
        return 12 - ordinal();
    }


    public static int compare(CardRank lhs, CardRank rhs) {
        return Integer.compare(lhs.getIndex(), rhs.getIndex());
    }

    /**
     * @return the theChar
     */
    public char toChar() {
        return theChar;
    }
    
    public static CardRank fromChar(char rankChar) {
        CardRank rank;
        
        switch(rankChar) {
        case 'a': case 'A':
            rank = CardRank.ACE;
            break;
        case 'k': case 'K':
            rank = CardRank.KING;
            break;
        case 'q': case 'Q':
            rank = CardRank.QUEEN;
            break;
        case 'j': case 'J':
            rank = CardRank.JACK;
            break;
        case 't': case 'T':
            rank = CardRank.TEN;
            break;
            default:
                rank = CardRank.getFromFaceValue(Character.digit(rankChar,10));
        }
        
        return rank;
    }
    
    
}
