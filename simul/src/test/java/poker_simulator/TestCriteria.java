package poker_simulator;

import org.junit.Test;

import pkr.Card;
import pkr.CompleteEvaluation;
import pkr.EvalHands;
import pkr.HoleCards;
import poker_simulator.criteria.Criteria;
import poker_simulator.flags.HandCategory;

import static org.junit.Assert.*;

public class TestCriteria {
	@Test
    public void testFlush() 
    {
		Criteria flushDrawCrit = new Criteria(0, " flush draw");                
        flushDrawCrit.matchHandCat.add( HandCategory.FLUSH_DRAW );
        
        boolean heroOnly = false;
        
        HoleCards h1 = new HoleCards(Card.parseCards("Kh Qh"));                
        HoleCards h2 = new HoleCards(Card.parseCards("Ah Jd"));
        HoleCards h3 = new HoleCards(Card.parseCards("As Js"));
        
        HoleCards[] holeCards = new HoleCards[] {h1, h2, h3};
        
        Card[] flopTurnRiver = Card.parseCards("2h 8h 5s Ac 9h"); 
        
        CompleteEvaluation[] evals = EvalHands.evaluate(heroOnly, holeCards, flopTurnRiver);
        
        flushDrawCrit.calculate(evals);
        
        assertEquals(1,  flushDrawCrit.getApplicableCount());
        assertEquals(1,  flushDrawCrit.getMatches());
        
        //Now player 2 has the flush draw, not hero
        flopTurnRiver = Card.parseCards("2s 8h 5s Ac 9h");        
        evals = EvalHands.evaluate(heroOnly, holeCards, flopTurnRiver);
        
        flushDrawCrit.calculate(evals);
        
        assertEquals(2,  flushDrawCrit.getApplicableCount());
        assertEquals(1,  flushDrawCrit.getMatches());
        
    }
}
