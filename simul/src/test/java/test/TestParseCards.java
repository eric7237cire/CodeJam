package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pkr.Card;
import pkr.CardRank;
import pkr.Suit;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TestParseCards 
{
    @Test
    public void testSimple()
    {
        Card[] list = Card.parseCards("Ah 2s");
        
        assertNotNull(list);
        assertEquals(2, list.length);
        
        assertEquals(list[0], Card.getCard(Suit.HEARTS, CardRank.ACE));
        assertEquals(list[1], Card.getCard(Suit.SPADES, CardRank.TWO));
        
        list = Card.parseCards("3c Kd");
        
        assertNotNull(list);
        assertEquals(2, list.length);
        
        assertEquals(list[0], Card.getCard(Suit.CLUBS, CardRank.THREE));
        assertEquals(list[1], Card.getCard(Suit.DIAMONDS, CardRank.KING));
    }
    
    @Test
    public void testEmpty()
    {
        Card[] list = Card.parseCards("  \t \n");
        
        assertNotNull(list);
        assertEquals(0, list.length);
        
        list = Card.parseCards(null);
        
        assertNotNull(list);
        assertEquals(0, list.length);
        
        list = Card.parseCards("");
        
        assertNotNull(list);
        assertEquals(0, list.length);
        
        list = Card.parseCards("f8");
        
        assertNotNull(list);
        assertEquals(0, list.length);
    }
}




