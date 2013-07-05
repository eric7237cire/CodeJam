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
        assertTrue(evals[handNum].getFlopScore().getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getFlopScore().getKickers()[0] == CardRank.QUEEN);
        assertTrue(evals[handNum].getFlopScore().getKickers()[1] == CardRank.KING);
        assertTrue(evals[handNum].getFlopScore().getKickers()[2] == CardRank.EIGHT);
        assertTrue(evals[handNum].getFlopScore().getKickers()[3] == CardRank.FIVE);
        
        assertTrue( evals[handNum].getFlopEval().hasFlag( EvaluationCategory.TOP_PAIR));
        assertFalse( evals[handNum].getFlopEval().hasFlag( EvaluationCategory.LOSING));
        assertTrue( evals[handNum].getFlopEval().hasFlag( EvaluationCategory.WINNING));
        assertTrue( evals[handNum].getFlopEval().hasFlag( EvaluationCategory.BY_KICKER_2));
        
        assertTrue( evals[handNum].getFlopTexture().hasFlag(TextureCategory.SAME_SUIT_2));
        assertFalse( evals[handNum].getFlopTexture().hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        ++handNum;
        
        assertTrue(evals[handNum].getFlopScore().getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getFlopScore().getKickers()[0] == CardRank.QUEEN);
        assertTrue(evals[handNum].getFlopScore().getKickers()[1] == CardRank.KING);
        assertTrue(evals[handNum].getFlopScore().getKickers()[2] == CardRank.EIGHT);
        assertTrue(evals[handNum].getFlopScore().getKickers()[3] == CardRank.FIVE);
        
        assertTrue( evals[handNum].getFlopEval().hasFlag( EvaluationCategory.TOP_PAIR));
        assertTrue( evals[handNum].getFlopEval().hasFlag( EvaluationCategory.LOSING));
        assertFalse( evals[handNum].getFlopEval().hasFlag( EvaluationCategory.WINNING));
        assertTrue( evals[handNum].getFlopEval().hasFlag( EvaluationCategory.BY_KICKER_2));

        
        assertTrue( evals[0].getFlopTexture().equals(evals[1].getFlopTexture()) );
       
        
        
    }
    
    //Test for top pair on a paired board
}
