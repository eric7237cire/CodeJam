package poker_simulator;

import org.junit.Test;

import pkr.Card;
import pkr.CompleteEvaluation;
import pkr.HoleCards;
import pkr.possTree.PossibilityNode.TextureCategory;
import poker_simulator.criteria.Criteria;
import poker_simulator.criteria.CriteriaFactory;
import poker_simulator.criteria.TextureMatcher;
import poker_simulator.criteria.matcher.AllPlayerMatcher;
import poker_simulator.criteria.matcher.HeroCategoryMatcher;
import poker_simulator.evaluation.EvalHands;
import poker_simulator.flags.HandCategory;
import poker_simulator.flags.Round;

import static org.junit.Assert.*;

public class TestCriteria
{
	@Test
	public void testEveryoneHighCard()
	{
		
		
		boolean heroOnly = false;

		HoleCards h1 = new HoleCards(Card.parseCards("Kh Qh"));
		HoleCards h2 = new HoleCards(Card.parseCards("Ah Jd"));
		HoleCards h3 = new HoleCards(Card.parseCards("As Js"));

		HoleCards[] holeCards = new HoleCards[] { h1, h2, h3 };

		Card[] flopTurnRiver = Card.parseCards("2h 8h 5s 2c 9h");

		CompleteEvaluation[] evals = EvalHands.evaluate(heroOnly, holeCards, flopTurnRiver);

		Criteria c;
		c = CriteriaFactory.BuildUnpairedCriteria_AllPlayerMatch("nothing on unpaired board", Round.FLOP, HandCategory.HIGH_CARD);
		c.calculate(evals);

		assertEquals(1, c.getApplicableCount());
		assertEquals(1, c.getMatches());
		
		
		c = CriteriaFactory.BuildUnpairedCriteria_AllPlayerMatch("not applicable on turn", Round.TURN, HandCategory.HIGH_CARD);
		c.calculate(evals);

		assertEquals(0, c.getApplicableCount());
		assertEquals(0, c.getMatches());
		
	}
	@Test
	public void testFlush()
	{
		Criteria flushDrawCrit = new Criteria(" flush draw");

		HeroCategoryMatcher matcher = new HeroCategoryMatcher(Round.FLOP);
		matcher.AddMatchingHandCategories(HandCategory.FLUSH_DRAW);

		flushDrawCrit.setMatcher(matcher);

		boolean heroOnly = false;

		HoleCards h1 = new HoleCards(Card.parseCards("Kh Qh"));
		HoleCards h2 = new HoleCards(Card.parseCards("Ah Jd"));
		HoleCards h3 = new HoleCards(Card.parseCards("As Js"));

		HoleCards[] holeCards = new HoleCards[] { h1, h2, h3 };

		Card[] flopTurnRiver = Card.parseCards("2h 8h 5s Ac 9h");

		CompleteEvaluation[] evals = EvalHands.evaluate(heroOnly, holeCards, flopTurnRiver);

		flushDrawCrit.calculate(evals);

		assertEquals(1, flushDrawCrit.getApplicableCount());
		assertEquals(1, flushDrawCrit.getMatches());

		// Now player 2 has the flush draw, not hero
		flopTurnRiver = Card.parseCards("2s 8h 5s Ac 9h");
		evals = EvalHands.evaluate(heroOnly, holeCards, flopTurnRiver);

		flushDrawCrit.calculate(evals);

		assertEquals(2, flushDrawCrit.getApplicableCount());
		assertEquals(1, flushDrawCrit.getMatches());

	}
}
