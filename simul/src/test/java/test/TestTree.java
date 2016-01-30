package test;

import pkr.Card;
import pkr.CompleteEvaluation;
import pkr.HoleCards;
import pkr.possTree.Tree;
import poker_simulator.evaluation.EvalHands;


//@RunWith(JUnit4.class)
public class TestTree {
    
    public void testTree() 
    {
        
        Tree tree = new Tree();
        
        HoleCards h1 = new HoleCards(Card.parseCards("Qs Qc"));                
        HoleCards h2 = new HoleCards(Card.parseCards("Ac 7d"));
        HoleCards h3 = new HoleCards(Card.parseCards("As 5d"));
        HoleCards h4 = new HoleCards(Card.parseCards("Kc 8d"));
                               
        
        CompleteEvaluation[] evals = EvalHands.evaluate(true, new HoleCards[] {h1, h2, h3, h4},
                Card.parseCards("Js 8s 5h 2c 2d"));
        
        tree.addCompleteEvaluation(evals[0]);
        
        h1 = new HoleCards(Card.parseCards("Qs Qc"));                
        h2 = new HoleCards(Card.parseCards("Ac 7d"));
        h3 = new HoleCards(Card.parseCards("As 5d"));
        h4 = new HoleCards(Card.parseCards("Kc 8d"));
                               
        
        evals = EvalHands.evaluate(true, new HoleCards[] {h1, h2, h3, h4},
                Card.parseCards("Js 7s 5h 2c 2d"));
        tree.addCompleteEvaluation(evals[0]);
        
        h1 = new HoleCards(Card.parseCards("Qs Qc"));                
        h2 = new HoleCards(Card.parseCards("Ac 7d"));
        h3 = new HoleCards(Card.parseCards("As 5d"));
        h4 = new HoleCards(Card.parseCards("Kc 8d"));
                               
       
        
        evals = EvalHands.evaluate(true, new HoleCards[] {h1, h2, h3, h4},
                Card.parseCards("Js 8s 5h 2c 2d"));
        tree.addCompleteEvaluation(evals[0]);
        
        
        String fileName = "C:\\codejam\\CodeJam\\simul\\out.xml";
        
        tree.output(fileName);
    }
}
