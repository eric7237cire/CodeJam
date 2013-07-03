package pkr;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public enum Suit {
    HEARTS('h'), CLUBS('c'), DIAMONDS('d'), SPADES('s');

    private static BiMap<Integer, Suit> sMap = HashBiMap.create(13);

    private char theChar;
    
    private Suit(char theChar) {
        this.theChar = theChar;
    }

    static {

        for (Suit s : Suit.values()) {
            sMap.put(s.ordinal(), s);
        }
    }

    public static Suit getFromValue(int value) {
        return sMap.get(value);
    }

    /*
     * 0 to 3
     */
    public int getValue() {
        return ordinal();
    }
    
    public char toChar() {
        return theChar;
    }
}
