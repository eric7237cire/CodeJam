package poker_simulator;

import org.junit.Test;

import pkr.Card;
import pkr.CompleteEvaluation;
import pkr.HoleCards;
import pkr.TextureInfo;
import pkr.possTree.PossibilityNode;
import pkr.possTree.PossibilityNode.Levels;
import poker_simulator.evaluation.EvalHands;
import poker_simulator.flags.HandCategory;
import poker_simulator.flags.Round;

import static org.junit.Assert.*;

public class TestHandCategories
{
	@Test
	public void TestTripsOnBoard()
	{
		HoleCards h1 = new HoleCards(Card.parseCards("Js 4s"));                
		HoleCards h2 = new HoleCards(Card.parseCards("2h 2d"));
                           
		CompleteEvaluation[] evals = EvalHands.evaluate(false, new HoleCards[] {h1, h2},
            Card.parseCards("2s Qs Qh Qc Qd"));
		
		TextureInfo allCardsTexInfo = new TextureInfo();
		
		allCardsTexInfo.addCards(evals[0].communityCards.cards);
		allCardsTexInfo.addCards(evals[0].getHoleCards().getCards());
        
		allCardsTexInfo.calculate();
		
		for(Round r : Round.values())
		{
			EvalHands.evaluateNodeSingleHand(evals[0], r.getIndex(), allCardsTexInfo, 
				evals[0].communityCards, evals[0].getRoundScore(r.getIndex()));
		}
		
		PossibilityNode flopNode = evals[0].getPossibilityNode(0, Levels.HAND_CATEGORY.ordinal());
		assertTrue(flopNode.hasFlag(HandCategory.PAIR_ON_PAIRED_BOARD));
		
		PossibilityNode turnNode = evals[0].getPossibilityNode(1, Levels.HAND_CATEGORY.ordinal());
		assertTrue(turnNode.hasFlag(HandCategory.SET_USING_NONE));
		
		PossibilityNode riverNode = evals[0].getPossibilityNode(2, Levels.HAND_CATEGORY.ordinal());
		assertTrue(riverNode.hasFlag(HandCategory.QUADS));
	}
}
