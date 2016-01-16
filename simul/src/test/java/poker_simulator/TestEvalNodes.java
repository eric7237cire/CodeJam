package poker_simulator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pkr.CompleteEvaluation.ROUND_FLOP;
import static pkr.CompleteEvaluation.ROUND_RIVER;
import static pkr.CompleteEvaluation.ROUND_TURN;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import pkr.Card;
import pkr.CardRank;
import pkr.CompleteEvaluation;
import pkr.EvalHands;
import pkr.HandLevel;
import pkr.HoleCards;
import pkr.TextureInfo;
import pkr.possTree.PossibilityNode.TextureCategory;
import poker_simulator.flags.*;

@RunWith(JUnit4.class)
public class TestEvalNodes
{

    public TestEvalNodes() {
        
    }

    
    @Test
    public void testTopPair() {
        
        HoleCards h1 = new HoleCards(Card.parseCards("Ks Qc"));                
        HoleCards h2 = new HoleCards(Card.parseCards("Qh Jd"));
                               
        CompleteEvaluation[] evals = EvalHands.evaluate(false, new HoleCards[] {h1, h2},
                Card.parseCards("Qs 8s 5h Ac Qd"));
        
        int handNum = 0;
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.QUEEN);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[1] == CardRank.KING);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[2] == CardRank.EIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[3] == CardRank.FIVE);
        
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.PAIR_OVERCARDS_0));
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
        
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.PAIR_OVERCARDS_0));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));

        
        assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).equals(evals[1].getPossibilityNode(ROUND_FLOP,0)) );
       
        
        
    }
    
    
    @Test
    public void testFlush() {
        
        HoleCards h1 = new HoleCards(Card.parseCards("Kh Qh"));                
        HoleCards h2 = new HoleCards(Card.parseCards("Ah Jd"));
                               
        CompleteEvaluation[] evals = EvalHands.evaluate(false, new HoleCards[] {h1, h2},
                Card.parseCards("Th 8h 5h Ac 9h"));
        
        int handNum = 0;
        
        int round = ROUND_FLOP;
        
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.FLUSH);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[0] == CardRank.KING);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[1] == CardRank.QUEEN);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[2] == CardRank.TEN);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[3] == CardRank.EIGHT);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[4] == CardRank.FIVE);
        
        assertTrue( evals[handNum].hasFlag(round, HandCategory.FLUSH));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.FLUSH_DRAW));
        assertFalse( evals[handNum].hasFlag(round, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(round, WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].hasFlag(round, HandSubCategory.BY_HAND_CATEGORY));
        assertFalse( evals[handNum].hasFlag(round, HandSubCategory.BY_KICKER_HAND));
        
        assertTrue( evals[handNum].getPossibilityNode(round,0).hasFlag(TextureCategory.SAME_SUIT_3));
        assertFalse( evals[handNum].getPossibilityNode(round,0).hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.HIGH_CARD);
         
        assertFalse( evals[handNum].hasFlag(round, HandCategory.PAIR_OVERCARDS_0));
        assertTrue( evals[handNum].hasFlag(round, HandCategory.FLUSH_DRAW));
        assertTrue( evals[handNum].hasFlag(round, WinningLosingCategory.SECOND_BEST_HAND));
        assertFalse( evals[handNum].hasFlag(round, WinningLosingCategory.WINNING));
        assertFalse( evals[handNum].hasFlag(round, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(round, HandSubCategory.BY_HAND_CATEGORY));
        assertFalse( evals[handNum].hasFlag(round, HandSubCategory.BY_KICKER_HAND));

        handNum=1;
        round = ROUND_TURN;
        
        assertTrue( evals[handNum].hasFlag(round, HandCategory.FLUSH_DRAW));
        
        handNum=0;
        round = ROUND_RIVER;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.FLUSH);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[0] == CardRank.KING);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[1] == CardRank.QUEEN);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[2] == CardRank.TEN);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[3] == CardRank.NINE);
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[4] == CardRank.EIGHT);
        
        assertTrue( evals[handNum].hasFlag(round, HandCategory.FLUSH));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.FLUSH_DRAW));
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
                               
        
        CompleteEvaluation[] evals = EvalHands.evaluate(false, new HoleCards[] {h1, h2, h3, h4, h5},
                Card.parseCards("5d 3s 4h Ac Qd"));
        
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
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.PAIR_OVERCARDS_0));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SEVEN);
        
        //1st
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.PAIR_OVERCARDS_0));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SEVEN);
        
        //1st
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.PAIR_OVERCARDS_0));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        
       
        ++handNum;
        
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.SIX);
        
        //2nd
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.PAIR_OVERCARDS_0));
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
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.PAIR_OVERCARDS_0));
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
                                       
        CompleteEvaluation[] evals = EvalHands.evaluate(false, 
                new HoleCards[] {h1, h2, h3},
                Card.parseCards("7d 8s 4h Kc Jd"));
        
        
        //assertFalse( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.SAME_SUIT_2));
        //assertFalse( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        //assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.UNSUITED));
        
        int handNum = 0;
        //flop
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getRoundScore(ROUND_FLOP).getKickers()[0] == CardRank.JACK);
        
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.PAIR_OVERCARDS_0));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandCategory.HIDDEN_PAIR));
        
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.WINNING));        
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.LOSING));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_HAND_CATEGORY));
        assertFalse( evals[handNum].hasFlag(ROUND_FLOP, HandSubCategory.BY_KICKER_1));
        
        //turn
        int round  = ROUND_TURN;
        assertTrue(evals[handNum].getRoundScore(ROUND_TURN).getHandLevel() == HandLevel.PAIR);
        assertTrue(evals[handNum].getRoundScore(ROUND_TURN).getKickers()[0] == CardRank.JACK);
        
        assertTrue( evals[handNum].hasFlag(round, HandCategory.PAIR_OVERCARDS_1));
        assertTrue( evals[handNum].hasFlag(round, HandCategory.HIDDEN_PAIR));
        
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.WINNING));        
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.SECOND_BEST_HAND));
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, HandSubCategory.BY_HAND_CATEGORY));
        assertTrue( evals[handNum].hasFlag(ROUND_TURN, HandSubCategory.BY_KICKER_HAND));
        
      //river
        assertTrue(evals[handNum].getRoundScore(ROUND_RIVER).getHandLevel() == HandLevel.TRIPS);
        assertTrue(evals[handNum].getRoundScore(ROUND_RIVER).getKickers()[0] == CardRank.JACK);
        
        assertFalse( evals[handNum].hasFlag(ROUND_RIVER, HandCategory.PAIR_OVERCARDS_1));
        assertFalse( evals[handNum].hasFlag(ROUND_RIVER, HandCategory.HIDDEN_PAIR));
        
        assertTrue( evals[handNum].hasFlag(ROUND_RIVER, HandCategory.SET_USING_BOTH));
        
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.WINNING));        
        assertFalse( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.LOSING));
        assertTrue( evals[handNum].hasFlag(ROUND_TURN, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue( evals[handNum].hasFlag(ROUND_RIVER, HandSubCategory.BY_HAND_CATEGORY));
        assertFalse( evals[handNum].hasFlag(ROUND_RIVER, HandSubCategory.BY_KICKER_1));
        
        handNum = 2;
        assertTrue(evals[handNum].getRoundScore(ROUND_RIVER).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_RIVER).getKickers()[0] == CardRank.JACK);
        
    }
    
    @Test
    public void testStraightDrawsInTextureInfo()
    {
        TextureInfo ti = new TextureInfo();
        ti.addCards(Card.parseCards("As 2c 4d 3s Th"));
        ti.calculate();
        
        assertEquals(1, ti.getStraightDrawCount());
        
        ti = new TextureInfo();
        ti.addCards(Card.parseCards("As 5h 4d 3s Th"));
        ti.calculate();
        
        assertEquals(1, ti.getStraightDrawCount());
    }
    @Test
    public void testStraightDraws()
    {
        HoleCards h1 = new HoleCards(Card.parseCards("As 2c"));               
        HoleCards h2 = new HoleCards(Card.parseCards("Ad 5h"));  
        HoleCards h3 = new HoleCards(Card.parseCards("6c 5d"));  
                                       
        CompleteEvaluation[] evals = EvalHands.evaluate(false, 
                new HoleCards[] {h1, h2, h3},
                Card.parseCards("4d 3s Th 2d 7c"));
        
        
        int handNum = 0;
        int round  = ROUND_FLOP;
        
        //flop
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.HIGH_CARD);
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        
        handNum = 1;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.HIGH_CARD);
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        
        handNum  = 2;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.HIGH_CARD);
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_2));
        
        //turn
        round  = ROUND_TURN;
        handNum = 0;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.PAIR);
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        
        handNum = 1;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.STRAIGHT);
        //Can make 2345 [6]
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_2));
        
        handNum  = 2;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        
     
        
        handNum = 2;
        assertTrue(evals[handNum].getRoundScore(ROUND_RIVER).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue(evals[handNum].getRoundScore(ROUND_RIVER).getKickers()[0] == CardRank.SEVEN);
    }
    
    @Test
    public void testStraightDraws2()
    {
        HoleCards h1 = new HoleCards(Card.parseCards("7s 8c"));               
        HoleCards h2 = new HoleCards(Card.parseCards("8d Ah"));  
        HoleCards h3 = new HoleCards(Card.parseCards("Ac Kd"));  
                            
        
        CompleteEvaluation[] evals = EvalHands.evaluate(false, 
                new HoleCards[] {h1, h2, h3},
                Card.parseCards("9d Ts Jh Qd Kc"));
        
        
        //assertFalse( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.SAME_SUIT_2));
        //assertFalse( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.HAS_AT_LEAST_ONE_ACE));
        //assertTrue( evals[0].getPossibilityNode(ROUND_FLOP,0).hasFlag(TextureCategory.UNSUITED));
        
        int handNum = 0;
        int round  = ROUND_FLOP;
        
        //flop
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[0] == CardRank.JACK);
        
        handNum = 1;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.HIGH_CARD);
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_2));
        
        handNum  = 2;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.HIGH_CARD);
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        
        //turn
        round  = ROUND_TURN;
        handNum = 0;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_2));
        assertTrue( evals[handNum].hasFlag(round, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[0] == CardRank.QUEEN);
        
        handNum = 1;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1)); //king makes a better straight
        assertFalse( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_2));
        assertTrue( evals[handNum].hasFlag(round, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[0] == CardRank.QUEEN);
        
        handNum  = 2;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue( evals[handNum].hasFlag(round, WinningLosingCategory.WINNING));
        assertFalse( evals[handNum].hasFlag(round, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[0] == CardRank.ACE);
        
        assertFalse( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_2));
        
       
     
        round = ROUND_RIVER;
        handNum = 0;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.STRAIGHT);
        assertTrue( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_2));
        assertTrue( evals[handNum].hasFlag(round, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[0] == CardRank.KING);
        
        handNum = 1;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.STRAIGHT);
        assertFalse( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_2));
        assertTrue( evals[handNum].hasFlag(round, WinningLosingCategory.WINNING));
        assertFalse( evals[handNum].hasFlag(round, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[0] == CardRank.ACE);
        
        handNum  = 2;
        assertTrue(evals[handNum].getRoundScore(round).getHandLevel() == HandLevel.STRAIGHT);
        assertFalse( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_1));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.STRAIGHT_DRAW_2));
        assertTrue( evals[handNum].hasFlag(round, WinningLosingCategory.WINNING));
        assertFalse( evals[handNum].hasFlag(round, WinningLosingCategory.SECOND_BEST_HAND));
        assertTrue(evals[handNum].getRoundScore(round).getKickers()[0] == CardRank.ACE);
    }
    
    
    @Test
    public void testBoardFull()
    {
        HoleCards h1 = new HoleCards(Card.parseCards("2s 2c"));               
        HoleCards h2 = new HoleCards(Card.parseCards("3d Ah"));  
        HoleCards h3 = new HoleCards(Card.parseCards("Ks 4d"));  
                                       
        CompleteEvaluation[] evals = EvalHands.evaluate(false, 
                new HoleCards[] {h1, h2, h3},
                Card.parseCards("Kd As Kh Ad Kc"));
        
        
        int handNum = 0;
        int round  = ROUND_FLOP;
        
        //flop
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum = 1;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum  = 2;
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        assertTrue( evals[handNum].hasFlag(round, HandCategory.SET_USING_ONE));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));        
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        //turn
        round  = ROUND_TURN;
        handNum = 0;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_NONE));
        
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum = 1;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.FULL_HOUSE));
        
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum  = 2;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.FULL_HOUSE));
        
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
     
        
        round  = ROUND_RIVER;
        handNum = 0;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.FULL_HOUSE));
        
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum = 1;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.FULL_HOUSE));
        
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum  = 2;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.QUADS));
        
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
    }
    
    @Test
    public void testTopPairTwoPair()
    {
        HoleCards h1 = new HoleCards(Card.parseCards("Ac Kc"));               
        HoleCards h2 = new HoleCards(Card.parseCards("4h Ah"));  
        HoleCards h3 = new HoleCards(Card.parseCards("Qs Ad"));  
                                       
        CompleteEvaluation[] evals = EvalHands.evaluate(false, 
                new HoleCards[] {h1, h2, h3},
                Card.parseCards("7d As 7h 4d Qc"));
        
        
        int handNum = 0;
        int round  = ROUND_FLOP;
        
        //flop
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_NONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_BOTH));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum = 1;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_NONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_BOTH));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum  = 2;
        
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_NONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_BOTH));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        //turn
        round  = ROUND_TURN;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_NONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_BOTH));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum = 1;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_NONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_BOTH));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum  = 2;
        
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_NONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_BOTH));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
     
        
        round  = ROUND_RIVER;
        handNum = 0;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_NONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_BOTH));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum = 1;
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_NONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_BOTH));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
        
        handNum  = 2;
        
        
        assertTrue( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_BOTH));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_NONE));
        assertFalse( evals[handNum].hasFlag(round, HandCategory.TWO_PAIR_USING_ONE));
        
        assertTrue( evals[handNum].hasFlag(round, TextureCategory.PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.UNPAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.TWO_PAIRED_BOARD));
        assertFalse( evals[handNum].hasFlag(round, TextureCategory.FULL_BOARD));
    }
}
