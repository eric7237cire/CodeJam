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
    
    //Test for 3 way tie (no second place)
    
    //Test for tie for 1st and 2nd but no 3rd place (5 players)
}
