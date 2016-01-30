package poker_simulator;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pkr.CompleteEvaluation.ROUND_RIVER;
import static pkr.CompleteEvaluation.ROUND_TURN;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pkr.Card;
import pkr.CardRank;
import pkr.CompleteEvaluation;
import pkr.HoleCards;
import pkr.Suit;
import pkr.TextureInfo;
import pkr.possTree.PossibilityNode.TextureCategory;
import poker_simulator.evaluation.EvalHands;

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
    
    @Test
    public void testTripsOnBoard()
    {
        HoleCards h1 = new HoleCards(Card.parseCards("Ac Kc"));               
        HoleCards h2 = new HoleCards(Card.parseCards("4h Ah"));  
        HoleCards h3 = new HoleCards(Card.parseCards("Qs Ad"));  
                                       
        CompleteEvaluation[] evals = EvalHands.evaluate(false, 
                new HoleCards[] {h1, h2, h3},
                Card.parseCards("7d As 7h 4d 7c"));
        
        
        int round = ROUND_TURN;
        
        assertTrue(evals[0].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse(evals[0].hasFlag(round, TextureCategory.TRIPS_BOARD));
        
        round  = ROUND_RIVER;
        
        assertFalse(evals[0].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertTrue(evals[0].hasFlag(round, TextureCategory.TRIPS_BOARD));
    }
}
