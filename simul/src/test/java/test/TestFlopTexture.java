package test;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.*;
import static pkr.CompleteEvaluation.ROUND_FLOP;
import static pkr.CompleteEvaluation.ROUND_TURN;
import static pkr.CompleteEvaluation.ROUND_RIVER;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pkr.Card;
import pkr.CardRank;
import pkr.CompleteEvaluation;
import pkr.EvalHands;
import pkr.Flop;
import pkr.HandLevel;
import pkr.HoleCards;
import pkr.Suit;
import pkr.TextureInfo;
import pkr.possTree.PossibilityNode.TextureCategory;
import pkr.possTree.PossibilityNode.WinningLosingCategory;
import pkr.possTree.PossibilityNode.HandSubCategory;
import pkr.possTree.PossibilityNode.HandCategory;

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
