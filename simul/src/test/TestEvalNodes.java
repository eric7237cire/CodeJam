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
import pkr.Evaluation;
import pkr.Flop;
import pkr.HandLevel;
import pkr.HoleCards;
import pkr.possTree.EvaluationNode.EvaluationCategory;
import pkr.possTree.FlopTextureNode.TextureCategory;


import static pkr.Evaluation.*;

public class TestEvalNodes
{

    public TestEvalNodes() {
        // TODO Auto-generated constructor stub
    }

    
    @Test
    public void testTopPair() {
        
        HoleCards h1 = new HoleCards(Card.parseCards("Ks Qc"));                
        HoleCards h2 = new HoleCards(Card.parseCards("Qh Jd"));
                               
        Flop f = new Flop(Card.parseCards("Qs 8s 5h"));
        
        Evaluation[] evals = EvalHands.evaluate(new HoleCards[] {h1, h2},
                f, Card.parseCard("Ac"), Card.parseCard("Qd"));
        
        int handNum = 0;
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.QUEEN);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[1] == CardRank.KING);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[2] == CardRank.EIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[3] == CardRank.FIVE);
        
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.TOP_PAIR));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.LOSING));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.WINNING));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_2));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_1));
        
        assertTrue( evals[handNum].getRoundTexture(ROUND_FLOP).hasFlag(TextureCategory.SAME_SUIT_2));
        assertFalse( evals[handNum].getRoundTexture(ROUND_FLOP).hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.QUEEN);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[1] == CardRank.JACK);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[2] == CardRank.EIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[3] == CardRank.FIVE);
        
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.TOP_PAIR));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.LOSING));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.WINNING));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_2));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_1));

        
        assertTrue( evals[0].getRoundTexture(ROUND_FLOP).equals(evals[1].getRoundTexture(ROUND_FLOP)) );
       
        
        
    }
    
    //Test for top pair on a paired board
    
    
    //Test tie for 1st and 2nd place (5 players)
    @Test
    public void testMultipleTiesWithThirdPlace() {
        
        HoleCards h1 = new HoleCards(Card.parseCards("2s 6c"));  //2               
        HoleCards h2 = new HoleCards(Card.parseCards("6d 7h"));  //1
        HoleCards h3 = new HoleCards(Card.parseCards("6h 7d")); //1
        HoleCards h4 = new HoleCards(Card.parseCards("2h 6s")); //2
        HoleCards h5 = new HoleCards(Card.parseCards("2c Ad")); //3
                               
        Flop f = new Flop(Card.parseCards("5d 3s 4h"));
        
        Evaluation[] evals = EvalHands.evaluate(new HoleCards[] {h1, h2, h3, h4, h5},
                f, Card.parseCard("Ac"), Card.parseCard("Qd"));
        
        assertTrue( evals[0].getRoundTexture(ROUND_FLOP).equals(evals[1].getRoundTexture(ROUND_FLOP)) );
        assertTrue( evals[0].getRoundTexture(ROUND_FLOP).equals(evals[2].getRoundTexture(ROUND_FLOP)) );
        assertTrue( evals[0].getRoundTexture(ROUND_FLOP).equals(evals[3].getRoundTexture(ROUND_FLOP)) );
        assertTrue( evals[0].getRoundTexture(ROUND_FLOP).equals(evals[4].getRoundTexture(ROUND_FLOP)) );
        assertFalse( evals[0].getRoundTexture(ROUND_FLOP).hasFlag(TextureCategory.SAME_SUIT_2));
        assertFalse( evals[0].getRoundTexture(ROUND_FLOP).hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        assertTrue( evals[0].getRoundTexture(ROUND_FLOP).hasFlag(TextureCategory.UNSUITED));
        
        int handNum = 0;
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SIX);
        
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.TOP_PAIR));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.LOSING));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.WINNING));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_1));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_2));
        
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SEVEN);
        
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.TOP_PAIR));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.LOSING));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.WINNING));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_1));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_2));
        
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SEVEN);
        
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.TOP_PAIR));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.LOSING));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.WINNING));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_1));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_2));
        
       
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SIX);
        
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.TOP_PAIR));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.LOSING));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.WINNING));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_1));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_2));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.SECOND_BEST_HAND));
       
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.FIVE);
        
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.TOP_PAIR));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.LOSING));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.WINNING));
        assertTrue( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_1));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.BY_KICKER_2));
        assertFalse( evals[handNum].getRoundEval(ROUND_FLOP).hasFlag( EvaluationCategory.SECOND_BEST_HAND));
        
    }
    
    //Test for 3 way tie (no second place)
    
    //Test for tie for 1st and 2nd but no 3rd place (5 players)
}
