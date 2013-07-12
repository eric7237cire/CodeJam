package test;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

import pkr.Card;
import pkr.CardRank;
import pkr.EvalHands;
import pkr.CompleteEvaluation;
import pkr.Flop;
import pkr.HandLevel;
import pkr.HoleCards;
import pkr.HoleCardsRange;



@RunWith(JUnit4.class)
public class TestRanges {
    
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
        HoleCardsRange range1  = new HoleCardsRange("77+, J4s+, 93o+, 27, 82s");
        
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
    
}
