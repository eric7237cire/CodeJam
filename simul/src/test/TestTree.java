package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
import pkr.possTree.Tree;
import pkr.possTree.PossibilityNode.TextureCategory;
import pkr.possTree.PossibilityNode.WinningLosingCategory;
import pkr.possTree.PossibilityNode.HandSubCategory;
import pkr.possTree.PossibilityNode.HandCategory;


@RunWith(JUnit4.class)
public class TestTree {
    
    public void testTree() 
    {
        
        Tree tree = new Tree();
        
        HoleCards h1 = new HoleCards(Card.parseCards("Qs Qc"));                
        HoleCards h2 = new HoleCards(Card.parseCards("Ac 7d"));
        HoleCards h3 = new HoleCards(Card.parseCards("As 5d"));
        HoleCards h4 = new HoleCards(Card.parseCards("Kc 8d"));
                               
        Flop f = new Flop(Card.parseCards("Js 8s 5h"));
        
        CompleteEvaluation[] evals = EvalHands.evaluate(true, new HoleCards[] {h1, h2, h3, h4},
                f, Card.parseCard("2c"), Card.parseCard("2d"));
        
        tree.addCompleteEvaluation(evals[0]);
        
        h1 = new HoleCards(Card.parseCards("Qs Qc"));                
        h2 = new HoleCards(Card.parseCards("Ac 7d"));
        h3 = new HoleCards(Card.parseCards("As 5d"));
        h4 = new HoleCards(Card.parseCards("Kc 8d"));
                               
        f = new Flop(Card.parseCards("Js 7s 5h"));
        
        evals = EvalHands.evaluate(true, new HoleCards[] {h1, h2, h3, h4},
                f, Card.parseCard("2c"), Card.parseCard("2d"));
        tree.addCompleteEvaluation(evals[0]);
        
        h1 = new HoleCards(Card.parseCards("Qs Qc"));                
        h2 = new HoleCards(Card.parseCards("Ac 7d"));
        h3 = new HoleCards(Card.parseCards("As 5d"));
        h4 = new HoleCards(Card.parseCards("Kc 8d"));
                               
        f = new Flop(Card.parseCards("Js 8s 5h"));
        
        evals = EvalHands.evaluate(true, new HoleCards[] {h1, h2, h3, h4},
                f, Card.parseCard("2c"), Card.parseCard("2d"));
        tree.addCompleteEvaluation(evals[0]);
        
        
        String fileName = "C:\\codejam\\CodeJam\\simul\\out.xml";
        
        tree.output(fileName);
    }
}
