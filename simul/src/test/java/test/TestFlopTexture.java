package test;


import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pkr.Card;
import pkr.CardRank;
import pkr.Suit;
import pkr.TextureInfo;

@RunWith(JUnit4.class)

public class TestFlopTexture {

    @Test
    public void testFlopText() {
        
        TextureInfo ti = new TextureInfo();
        ti.addCard(Card.getCard(Suit.CLUBS, CardRank.ACE));
        ti.addCard(Card.getCard(Suit.CLUBS, CardRank.THREE));
        ti.addCard(Card.getCard(Suit.HEARTS, CardRank.FOUR));
        
        ti.calculate();
        
        assertEquals(2, ti.freqSuit[Suit.CLUBS.ordinal()]);
        assertEquals(1, ti.freqSuit[Suit.HEARTS.ordinal()]);
        
        assertEquals(1, ti.freqCard[CardRank.ACE.getIndex()]);
        assertEquals(1, ti.freqCard[CardRank.THREE.getIndex()]);
        assertEquals(1, ti.freqCard[CardRank.FOUR.getIndex()]);
        
        assertEquals(-1, ti.firstPair);
        
        
        //
        ti.addCard(Card.getCard(Suit.HEARTS, CardRank.ACE));
        ti.calculate();
        
        assertEquals(2, ti.freqSuit[Suit.CLUBS.ordinal()]);
        assertEquals(2, ti.freqSuit[Suit.HEARTS.ordinal()]);
        
        assertEquals(2, ti.freqCard[CardRank.ACE.getIndex()]);
        assertEquals(1, ti.freqCard[CardRank.THREE.getIndex()]);
        assertEquals(1, ti.freqCard[CardRank.FOUR.getIndex()]);
        
        assertEquals(CardRank.ACE.getIndex(), ti.firstPair);
        
    }
}
