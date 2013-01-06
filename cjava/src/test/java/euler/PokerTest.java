package euler;

import static org.junit.Assert.*;

import org.junit.Test;

public class PokerTest {

    @Test
    public void testPoker() {
      /*
       * High Card: Highest value card.
One Pair: Two cards of the same value.
Two Pairs: Two different pairs.
Three of a Kind: Three cards of the same value.
Straight: All cards are consecutive values.
Flush: All cards of the same suit.
Full House: Three of a kind and a pair.
Four of a Kind: Four cards of the same value.
Straight Flush: All cards are consecutive values of same suit.
Royal Flush: Ten, Jack, Queen, King, Ace, in same suit.

D S C H
       */
        
        //Straight flush beats better straight flush
        assertTrue(Prob1.evalHand(Prob1.stringToHand("2C 3C 4C 5C 6C")) 
                > Prob1.evalHand(Prob1.stringToHand("2C 3C 4C 5C AC")));
        
      //Full house beat flush
        assertTrue(Prob1.evalHand(Prob1.stringToHand("2C 2D 3C 2H 3D")) 
                > Prob1.evalHand(Prob1.stringToHand("AC KC QC JC 9C")));
        
        //3 kind beat 2 pair
        assertTrue(Prob1.evalHand(Prob1.stringToHand("2C 2H 4S 3H 2D")) 
                > Prob1.evalHand(Prob1.stringToHand("AC AH QS KH KC")));
        
        //St8 beat 3 kind
        assertTrue(Prob1.evalHand(Prob1.stringToHand("2C AH 3S 4H 5D")) 
                > Prob1.evalHand(Prob1.stringToHand("AC AH QS AD KC")));
        
        //Flush beat straigh
        assertTrue(Prob1.evalHand(Prob1.stringToHand("2C 3C 4C 6C 7C")) 
                > Prob1.evalHand(Prob1.stringToHand("AC KH QS JD TC")));
        
        
        
        //Flush beats higher flush
        assertTrue(Prob1.evalHand(Prob1.stringToHand("2C 3C 4C 5C 8C")) 
                > Prob1.evalHand(Prob1.stringToHand("2C 3C 4C 5C 7C")));
        
        //Straigh flush beats 4 kind
        assertTrue(Prob1.evalHand(Prob1.stringToHand("2C 3C 4C 5C AC")) 
                > Prob1.evalHand(Prob1.stringToHand("2C 2H 2D 2S 3C")));
        
       
                
      //High card vs high card
        assertTrue(Prob1.evalHand(Prob1.stringToHand("3C TH AS KH JC")) 
                > Prob1.evalHand(Prob1.stringToHand("2C TH AS KH JC")));
        
        //Low pair
        assertTrue(Prob1.evalHand(Prob1.stringToHand("2C 2H 3S 4H 5C")) 
                > Prob1.evalHand(Prob1.stringToHand("2C TH AS KH JC")));
        
        //High card
        assertTrue(Prob1.evalHand(Prob1.stringToHand("2C TH AS KH JC")) 
                > Prob1.evalHand(Prob1.stringToHand("2C TH 7S KH JC")));
        
                
        //Pair 3rd kicker
        assertTrue(Prob1.evalHand(Prob1.stringToHand("7C 7H 4S 5H 6C")) 
                > Prob1.evalHand(Prob1.stringToHand("7C 7H 6S 5H 3C")));
        
    }

}
