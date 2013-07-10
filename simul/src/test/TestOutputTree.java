package test;

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
import pkr.possTree.Tree;
import pkr.possTree.FlopTextureNode.TextureCategory;



@RunWith(JUnit4.class)
public class TestOutputTree {
    @Test
    public void testSimpleCase() 
    {
        HoleCards h1 = new HoleCards(Card.parseCards("Ks Jc"));               
        HoleCards h2 = new HoleCards(Card.parseCards("Ad Qh"));  
        HoleCards h3 = new HoleCards(Card.parseCards("Th Td")); 
                                       
        Flop f = new Flop(Card.parseCards("5c 3s Kc"));
        
        CompleteEvaluation[] evals = EvalHands.evaluate(new HoleCards[] {h1, h2, h3},
                f, Card.parseCard("Qc"), Card.parseCard("Qd"));
        
        assertTrue(evals[0].getRoundTexture(1).hasFlag(TextureCategory.SAME_SUIT_3));
        assertTrue(evals[0].getRoundTexture(2).hasFlag(TextureCategory.SAME_SUIT_3));
        
        Tree tree = new Tree();
        tree.addCompleteEvaluation(evals[0]);
        
        String fileName = "C:\\codejam\\CodeJam\\simul\\out.xml";
        tree.output(fileName);
    }

}
