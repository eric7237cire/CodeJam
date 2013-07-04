package test;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import pkr.Card;
import pkr.CardRank;
import pkr.EvalHands;
import pkr.Evaluation;
import pkr.Flop;
import pkr.HandLevel;
import pkr.HoleCards;



@RunWith(JUnit4.class)
public class TestScore {
    
    @Test(expected=IllegalStateException.class)
    public void test1() {
        HoleCards h1 = new HoleCards(Card.parseCards("5c 8s"));
        HoleCards h2 = new HoleCards(Card.parseCards("6c 8s"));                
                
               
        Flop f = new Flop(Card.parseCards("2h 7h kh"));
        
        EvalHands.evaluate(new HoleCards[] {h1, h2}, f, null, null);
    }
    
    @Test
    public void testHighCardVsPair() {
        HoleCards h1 = new HoleCards(Card.parseCards("5c 8s"));
        HoleCards h2 = new HoleCards(Card.parseCards("6c Ks"));                
                               
        Flop f = new Flop(Card.parseCards("2h 7h kh"));
        
        Evaluation[] evals = EvalHands.evaluate(new HoleCards[] {h1, h2}, f, null, null);
        
        assertTrue(evals[0].getScore().getHandLevel() == HandLevel.HIGH_CARD);
        assertTrue(evals[0].getScore().getKickers()[0] == CardRank.KING);
        assertTrue(evals[0].getScore().getKickers()[1] == CardRank.EIGHT);
        assertTrue(evals[0].getScore().getKickers()[2] == CardRank.SEVEN);
        assertTrue(evals[0].getScore().getKickers()[3] == CardRank.FIVE);
        assertTrue(evals[0].getScore().getKickers()[4] == CardRank.TWO);
        
        assertTrue(evals[1].getScore().getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[1].getScore().getKickers()[0] == CardRank.KING);
        assertTrue(evals[1].getScore().getKickers()[1] == CardRank.SEVEN);
        assertTrue(evals[1].getScore().getKickers()[2] == CardRank.SIX);
        assertTrue(evals[1].getScore().getKickers()[3] == CardRank.TWO);
        
        assertTrue(evals[1].getScore().compareTo(evals[0].getScore()) > 0);
        
    }
    
    @Test
    public void testHighCardVsHighCardThirdKicker() {
        HoleCards h1 = new HoleCards(Card.parseCards("Kc 8s"));
        HoleCards h2 = new HoleCards(Card.parseCards("6c Ks"));                
                               
        Flop f = new Flop(Card.parseCards("2h 7h 4h"));
        
        Evaluation[] evals = EvalHands.evaluate(new HoleCards[] {h1, h2},
                f, Card.parseCard("Qd"), Card.parseCard("5s"));
        
        assertTrue(evals[0].getScore().getHandLevel() == HandLevel.HIGH_CARD);
        assertTrue(evals[0].getScore().getKickers()[0] == CardRank.KING);
        assertTrue(evals[0].getScore().getKickers()[1] == CardRank.QUEEN);
        assertTrue(evals[0].getScore().getKickers()[2] == CardRank.EIGHT);
        assertTrue(evals[0].getScore().getKickers()[3] == CardRank.SEVEN);
        assertTrue(evals[0].getScore().getKickers()[4] == CardRank.FIVE);
        
        assertTrue(evals[1].getScore().getHandLevel() == HandLevel.HIGH_CARD);
        assertTrue(evals[1].getScore().getKickers()[0] == CardRank.KING);
        assertTrue(evals[1].getScore().getKickers()[1] == CardRank.QUEEN);
        assertTrue(evals[1].getScore().getKickers()[2] == CardRank.SEVEN);
        assertTrue(evals[1].getScore().getKickers()[3] == CardRank.SIX);
        assertTrue(evals[1].getScore().getKickers()[4] == CardRank.FIVE);
        
        assertTrue(evals[0].compareTo(evals[1]) > 0);
        
    }
    
    @Test
    public void testStr8VsStr8() {
        HoleCards h1 = new HoleCards(Card.parseCards("Ac 8s"));
        HoleCards h2 = new HoleCards(Card.parseCards("6c 7s"));                
        HoleCards h3 = new HoleCards(Card.parseCards("6s 2s"));
                               
        Flop f = new Flop(Card.parseCards("2h 8h 4h"));
        
        Evaluation[] evals = EvalHands.evaluate(new HoleCards[] {h1, h2, h3},
                f, Card.parseCard("3d"), Card.parseCard("5s"));
        
        assertTrue(evals[0].getScore().getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[0].getScore().getKickers()[0] == CardRank.FIVE);
        
        assertTrue(evals[1].getScore().getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[1].getScore().getKickers()[0] == CardRank.EIGHT);
        
        assertTrue(evals[2].getScore().getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[2].getScore().getKickers()[0] == CardRank.SIX);
                
        assertTrue(evals[1].getScore().compareTo(evals[0].getScore()) > 0);
        assertTrue(evals[1].getScore().compareTo(evals[2].getScore()) > 0);
        assertTrue(evals[0].getScore().compareTo(evals[1].getScore()) < 0);
    }
    
    @Test
    public void testFullHouse() {
        HoleCards h1 = new HoleCards(Card.parseCards("2s 3c"));
        HoleCards h2 = new HoleCards(Card.parseCards("4h 2c"));                
        HoleCards h3 = new HoleCards(Card.parseCards("3h 4c"));
                               
        Flop f = new Flop(Card.parseCards("3s 2h 3d"));
        
        Evaluation[] evals = EvalHands.evaluate(new HoleCards[] {h1, h2, h3},
                f, Card.parseCard("4d"), Card.parseCard("4s"));
        
        int handNum = 0;
        assertTrue(evals[handNum].getScore().getHandLevel() == HandLevel.FULL_HOUSE);
        assertTrue(evals[handNum].getScore().getKickers()[0] == CardRank.THREE);
        assertTrue(evals[handNum].getScore().getKickers()[1] == CardRank.FOUR);
        assertEquals(0, evals[handNum].getRealEquity(), 0.00001);
        ++handNum;
        
        assertTrue(evals[handNum].getScore().getHandLevel() == HandLevel.FULL_HOUSE);
        assertTrue(evals[handNum].getScore().getKickers()[0] == CardRank.FOUR);
        assertTrue(evals[handNum].getScore().getKickers()[1] == CardRank.THREE);
        assertEquals(.5, evals[handNum].getRealEquity(), 0.00001);
        ++handNum;
        
        assertTrue(evals[handNum].getScore().getHandLevel() == HandLevel.FULL_HOUSE);
        assertTrue(evals[handNum].getScore().getKickers()[0] == CardRank.FOUR);
        assertTrue(evals[handNum].getScore().getKickers()[1] == CardRank.THREE);
        assertEquals(.5, evals[handNum].getRealEquity(), 0.00001);
        
        Arrays.sort(evals);
        
        assertTrue(evals[2].getScore().compareTo(evals[1].getScore()) == 0);
        //assertTrue(evals[1].getHoleCards().equals(h2));
        assertTrue(evals[0].getHoleCards().equals(h1));
        
    }
    
    @Test
    public void testThreeWayTie() {
        HoleCards h1 = new HoleCards(Card.parseCards("2s 2c"));
        HoleCards h2 = new HoleCards(Card.parseCards("3h 3c"));                
        HoleCards h3 = new HoleCards(Card.parseCards("4h 4c"));
                               
        Flop f = new Flop(Card.parseCards("5s 5h 5d"));
        
        Evaluation[] evals = EvalHands.evaluate(new HoleCards[] {h1, h2, h3},
                f, Card.parseCard("4d"), Card.parseCard("5c"));
        
        int handNum = 0;
        assertTrue(evals[handNum].getScore().getHandLevel() == HandLevel.QUADS);
        assertTrue(evals[handNum].getScore().getKickers()[0] == CardRank.FIVE);
        assertTrue(evals[handNum].getScore().getKickers()[1] == CardRank.FOUR);
        assertEquals(1.0/3, evals[handNum].getRealEquity(), 0.00001);
        
        ++handNum;
        
        assertTrue(evals[handNum].getScore().getHandLevel() == HandLevel.QUADS);
        assertTrue(evals[handNum].getScore().getKickers()[0] == CardRank.FIVE);
        assertTrue(evals[handNum].getScore().getKickers()[1] == CardRank.FOUR);
        assertEquals(1.0/3, evals[handNum].getRealEquity(), 0.00001);
        
        ++handNum;
        
        assertTrue(evals[handNum].getScore().getHandLevel() == HandLevel.QUADS);
        assertTrue(evals[handNum].getScore().getKickers()[0] == CardRank.FIVE);
        assertTrue(evals[handNum].getScore().getKickers()[1] == CardRank.FOUR);
        assertEquals(1.0/3, evals[handNum].getRealEquity(), 0.00001);
        
        Arrays.sort(evals);
        
        assertTrue(evals[2].getScore().compareTo(evals[1].getScore()) == 0);
        //assertTrue(evals[1].getHoleCards().equals(h2));
        assertTrue(evals[0].getHoleCards().equals(h1));
    }
}
