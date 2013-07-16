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
import pkr.possTree.PossibilityNode.TextureCategory;
import pkr.possTree.PossibilityNode.WinningLosingCategory;
import pkr.possTree.PossibilityNode.HandSubCategory;
import pkr.possTree.PossibilityNode.HandCategory;

@RunWith(JUnit4.class)
public class TestEvalNodes
{

    public TestEvalNodes() {
        
    }

    
    @Test
    public void testTopPair() {
        
        HoleCards h1 = new HoleCards(Card.parseCards("Ks Qc"));                
        HoleCards h2 = new HoleCards(Card.parseCards("Qh Jd"));
                               
        Flop f = new Flop(Card.parseCards("Qs 8s 5h"));
        
        CompleteEvaluation[] evals = EvalHands.evaluate(false, new HoleCards[] {h1, h2},
                f, Card.parseCard("Ac"), Card.parseCard("Qd"));
        
        int handNum = 0;
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.QUEEN);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[1] == CardRank.KING);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[2] == CardRank.EIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[3] == CardRank.FIVE);
        
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.TOP_PAIR));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));
        
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.SAME_SUIT_2));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.QUEEN);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[1] == CardRank.JACK);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[2] == CardRank.EIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[3] == CardRank.FIVE);
        
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.TOP_PAIR));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));

        
        assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).equals(evals[1].getPossibilityNode(ROUND_FLOP,0)) );
       
        
        
    }
    
    @Test
    public void testFiveWayEvaluation() {
        
        HoleCards h1 = new HoleCards(Card.parseCards("Kh Ah"));                
        HoleCards h2 = new HoleCards(Card.parseCards("Jh Qs"));
        HoleCards h3 = new HoleCards(Card.parseCards("2h 7h"));                
        HoleCards h4 = new HoleCards(Card.parseCards("7d Kd"));
        HoleCards h5 = new HoleCards(Card.parseCards("Js Qc"));
        
Flop f = new Flop(Card.parseCards("4s 9s Ks"));
        
        CompleteEvaluation[] evals = EvalHands.evaluate(true, new HoleCards[] {h1, h2,h3,h4,h5},
                f, Card.parseCard("6s"), Card.parseCard("Kc"));
    }
    @Test
    public void testFlush() {
        
        HoleCards h1 = new HoleCards(Card.parseCards("Kh Qh"));                
        HoleCards h2 = new HoleCards(Card.parseCards("Ah Jd"));
                               
        Flop f = new Flop(Card.parseCards("Th 8h 5h"));
        
        CompleteEvaluation[] evals = EvalHands.evaluate(false, new HoleCards[] {h1, h2},
                f, Card.parseCard("Ac"), Card.parseCard("9h"));
        
        int handNum = 0;
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.FLUSH);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.KING);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[1] == CardRank.QUEEN);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[2] == CardRank.TEN);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[3] == CardRank.EIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[4] == CardRank.FIVE);
        
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.FLUSH));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_HAND_CATEGORY));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));
        
        assertTrue( evals[handNum].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.SAME_SUIT_3));
        assertFalse( evals[handNum].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.HIGH_CARD);
         
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.TOP_PAIR));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_HAND_CATEGORY));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));

        handNum=0;
        int round = ROUND_RIVER;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.FLUSH);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[0] == CardRank.KING);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[1] == CardRank.QUEEN);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[2] == CardRank.TEN);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[3] == CardRank.NINE);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[4] == CardRank.EIGHT);
        
        assertTrue( evals[handNum].hasFlag(round, HandCategory.FLUSH));
        assertFalse( evals[handNum].hasFlag(round, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(round, WinningLosingCategory.SECOND_BEST_HAND));
        assertFalse( evals[handNum].hasFlag(round, HandSubCategory.BY_HAND_CATEGORY));
        assertTrue( evals[handNum].hasFlag(round, HandSubCategory.BY_KICKER_HAND));
        
        assertTrue( evals[handNum].getPossibilityNode(round,0).hasFlag(TextureCategory.SAME_SUIT_4));
        assertFalse( evals[handNum].getPossibilityNode(round,0).hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.FLUSH);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[0] == CardRank.ACE);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[1] == CardRank.TEN);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[2] == CardRank.NINE);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[3] == CardRank.EIGHT);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[4] == CardRank.FIVE);
        
        assertTrue( evals[handNum].hasFlag(round, HandCategory.FLUSH));
        assertFalse( evals[handNum].hasFlag(round, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(round, WinningLosingCategory.WINNING));
        assertFalse( evals[handNum].hasFlag(round, HandSubCategory.BY_HAND_CATEGORY));
        assertTrue( evals[handNum].hasFlag(round, HandSubCategory.BY_KICKER_HAND));
        
        //assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).equals(evals[1].getPossibilityNode(ROUND_FLOP,0)) );
       
        
        
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
        
        CompleteEvaluation[] evals = EvalHands.evaluate(false, new HoleCards[] {h1, h2, h3, h4, h5},
                f, Card.parseCard("Ac"), Card.parseCard("Qd"));
        
        //all flop nodes the same
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
        //2nd
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.TOP_PAIR));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SEVEN);
        
        //1st
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.TOP_PAIR));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SEVEN);
        
        //1st
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.TOP_PAIR));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        
       
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SIX);
        
        //2nd
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.TOP_PAIR));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
       
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.FIVE);
        
        //3rd
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.TOP_PAIR));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        
    }
    
    //Test for 3 way tie (no second place)
    
    //Test for tie for 1st and 2nd but no 3rd place (5 players)
    
    @Test
    public void testOverPair()
    {
        HoleCards h1 = new HoleCards(Card.parseCards("Js Jc"));               
        HoleCards h2 = new HoleCards(Card.parseCards("Kd 5h"));  
        HoleCards h3 = new HoleCards(Card.parseCards("9d Td"));  
                               
        Flop f = new Flop(Card.parseCards("7d 8s 4h"));
        
        CompleteEvaluation[] evals = EvalHands.evaluate(false, 
                new HoleCards[] {h1, h2, h3},
                f, Card.parseCard("Kc"), Card.parseCard("Jd"));
        
        
        //assertFalse( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.SAME_SUIT_2));
        //assertFalse( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        //assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.UNSUITED));
        
        int handNum = 0;
        //flop
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.JACK);
        
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.TOP_PAIR));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.OVER_PAIR));
        
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));        
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_HAND_CATEGORY));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        
        //turn
        assertTrue(evals[handNum].getRoundScore(ROUND_TURN).getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getRoundScore(ROUND_TURN).getKickers()[0] == CardRank.JACK);
        
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, HandCategory.TOP_PAIR));
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, HandCategory.OVER_PAIR));
        
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.WINNING));        
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.SECOND_BEST_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, HandSubCategory.BY_HAND_CATEGORY));
        assertTrue( evals[handNum].hasFlag(ROUND_TURN, HandSubCategory.BY_KICKER_HAND));
        
      //river
        assertTrue(evals[handNum].getRoundScore(ROUND_RIVER).getHandLevel() == HandLevel.TRIPS);
        assertTrue(evals[handNum].getRoundScore(ROUND_RIVER).getKickers()[0] == CardRank.JACK);
        
        assertFalse( evals[handNum].hasFlag(ROUND_RIVER, HandCategory.TOP_PAIR));
        assertFalse( evals[handNum].hasFlag(ROUND_RIVER, HandCategory.OVER_PAIR));
        assertTrue( evals[handNum].hasFlag(ROUND_RIVER, HandCategory.HIDDEN_SET));
        
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.WINNING));        
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue( evals[handNum].hasFlag(ROUND_RIVER, HandSubCategory.BY_HAND_CATEGORY));
        assertFalse( evals[handNum].hasFlag(ROUND_RIVER, HandSubCategory.BY_KICKER_1));
        
        handNum = 2;
        assertTrue(evals[handNum].getRoundScore(ROUND_RIVER).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_RIVER).getKickers()[0] == CardRank.JACK);
        
    }
}
