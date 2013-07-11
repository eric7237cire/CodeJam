package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pkr.CompleteEvaluation.ROUND_FLOP;

import org.junit.Test;

import pkr.Card;
import pkr.CardRank;
import pkr.CompleteEvaluation;
import pkr.EvalHands;
import pkr.Flop;
import pkr.HandLevel;
import pkr.HoleCards;
import pkr.possTree.PossibilityNode.TextureCategory;
import pkr.possTree.PossibilityNode.WinningLosingCategory;
import pkr.possTree.PossibilityNode.WinningLosingSubCategory;

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
        
        CompleteEvaluation[] evals = EvalHands.evaluate(new HoleCards[] {h1, h2},
                f, Card.parseCard("Ac"), Card.parseCard("Qd"));
        
        int handNum = 0;
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.QUEEN);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[1] == CardRank.KING);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[2] == CardRank.EIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[3] == CardRank.FIVE);
        
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.TOP_PAIR));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_2));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_1));
        
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.SAME_SUIT_2));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.QUEEN);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[1] == CardRank.JACK);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[2] == CardRank.EIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[3] == CardRank.FIVE);
        
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.TOP_PAIR));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.LOSING));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_2));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_1));

        
        assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).equals(evals[1].getPossibilityNode(ROUND_FLOP,0)) );
       
        
        
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
        
        CompleteEvaluation[] evals = EvalHands.evaluate(new HoleCards[] {h1, h2, h3, h4, h5},
                f, Card.parseCard("Ac"), Card.parseCard("Qd"));
        
        assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).equals(evals[1].getPossibilityNode(ROUND_FLOP,0)) );
        assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).equals(evals[2].getPossibilityNode(ROUND_FLOP,0)) );
        assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).equals(evals[3].getPossibilityNode(ROUND_FLOP,0)) );
        assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).equals(evals[4].getPossibilityNode(ROUND_FLOP,0)) );
        assertFalse( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.SAME_SUIT_2));
        assertFalse( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.UNSUITED));
        
        int handNum = 0;
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SIX);
        
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.TOP_PAIR));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.LOSING));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_1));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_2));
        
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SEVEN);
        
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.TOP_PAIR));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_1));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_2));
        
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SEVEN);
        
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.TOP_PAIR));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_1));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_2));
        
       
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SIX);
        
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.TOP_PAIR));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.LOSING));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_1));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_2));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.SECOND_BEST_HAND));
       
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.FIVE);
        
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.TOP_PAIR));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.LOSING));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_1));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,2).hasFlag( WinningLosingSubCategory.BY_KICKER_2));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,1).hasFlag( WinningLosingCategory.SECOND_BEST_HAND));
        
    }
    
    //Test for 3 way tie (no second place)
    
    //Test for tie for 1st and 2nd but no 3rd place (5 players)
}
