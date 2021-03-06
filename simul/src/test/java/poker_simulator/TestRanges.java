package poker_simulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pkr.Card;
import pkr.HoleCardsRange;



@RunWith(JUnit4.class)
public class TestRanges {
    
    @Test
    public void testBtn()
    {
        HoleCardsRange range1  = new HoleCardsRange("22+\n" +
        "A2s+,\n" +
        "T9s,98s,87s, 76s, 65s, 54s, 43s,\n" +
        "KQs-KTs, QJs-QTs, JTs, \n" +
        "AKo-ATo, KQo-KTo, QJo-QTo, JTo\n" +
        "J9s, T8s, 97s, 75s, 53s, Q9s, J8s, T7s, 96s, 85s, 74s, Q8s, J7s,\n" +  
        "K9s-K6s\n");
        
        List<String> suits = Arrays.asList("c", "s", "h", "d");
        for(String suitStr : suits)
        {
            assertTrue(range1.inRange(Card.parseCards("K" + suitStr + "6" + suitStr)));
            assertTrue(range1.inRange(Card.parseCards("K" + suitStr + "7" + suitStr)));
            assertTrue(range1.inRange(Card.parseCards("K" + suitStr + "8" + suitStr)));
            assertTrue(range1.inRange(Card.parseCards("K" + suitStr + "9" + suitStr)));
            assertTrue(range1.inRange(Card.parseCards("K" + suitStr + "T" + suitStr)));
            assertTrue(range1.inRange(Card.parseCards("K" + suitStr + "J" + suitStr)));
            assertTrue(range1.inRange(Card.parseCards("K" + suitStr + "Q" + suitStr)));
        }
    }
    
    @Test
    public void testRangeSuitedAndUnsuited() 
    {
        HoleCardsRange range1  = new HoleCardsRange("AJ+");
        
        assertTrue(range1.inRange(Card.parseCards("AcJs")));
        
        assertFalse(range1.inRange(Card.parseCards("AcTs")));
        
        assertTrue(range1.inRange(Card.parseCards("AsQs")));
        
        assertFalse(range1.inRange(Card.parseCards("AsAc")));
    }
    
    @Test
    public void testRangeSuitedAndUnsuited2() 
    {
        HoleCardsRange range1  = new HoleCardsRange("77+, \tJ4s+,  93o+,\n 27, \n\n\n82s");
        
        assertTrue(range1.inRange(Card.parseCards("7c7s")));
        assertTrue(range1.inRange(Card.parseCards("AsAc")));
        assertFalse(range1.inRange(Card.parseCards("6s6c")));
        
        assertFalse(range1.inRange(Card.parseCards("Js3s")));
        assertTrue(range1.inRange(Card.parseCards("4hJh")));
        assertTrue(range1.inRange(Card.parseCards("ThJh")));
        assertFalse(range1.inRange(Card.parseCards("QhJh")));
        
        assertTrue(range1.inRange(Card.parseCards("9h7c")));
        assertFalse(range1.inRange(Card.parseCards("9c7c")));
        
        assertTrue(range1.inRange(Card.parseCards("2h7c")));
        assertTrue(range1.inRange(Card.parseCards("7c2c")));
        
        assertTrue(range1.inRange(Card.parseCards("8s2s")));
        assertTrue(range1.inRange(Card.parseCards("8h2h")));
        assertTrue(range1.inRange(Card.parseCards("8c2c")));
        assertTrue(range1.inRange(Card.parseCards("8d2d")));
        assertFalse(range1.inRange(Card.parseCards("8c2s")));
        
        
    }
    
    @Test
    public void testAll()
    {
        HoleCardsRange range1 = new HoleCardsRange("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+, 22+");
        assertEquals(range1.getCardsList().size(), 1326);
        
        range1 = new HoleCardsRange("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+");
        assertEquals(range1.getCardsList().size(), 1248);
    }
    
    @Test
    public void testMinus() 
    {
        HoleCardsRange range1  = new HoleCardsRange("44-66,\t\n A9s-AQs");
        
        assertTrue(range1.inRange(Card.parseCards("AhJh")));
        
        assertTrue(range1.inRange(Card.parseCards("AdTd")));
        
        assertTrue(range1.inRange(Card.parseCards("AsQs")));
        
        assertFalse(range1.inRange(Card.parseCards("AsKs")));
        
        assertFalse(range1.inRange(Card.parseCards("Ac8c")));
        
        assertTrue(range1.inRange(Card.parseCards("4h4c")));
        
        assertTrue(range1.inRange(Card.parseCards("5d5h")));
        
        assertTrue(range1.inRange(Card.parseCards("6h6s")));
        
        assertFalse(range1.inRange(Card.parseCards("7s7h")));
        
        assertFalse(range1.inRange(Card.parseCards("3c3d")));
    }
    
}
