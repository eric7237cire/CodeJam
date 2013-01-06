package euler;

import static org.junit.Assert.*;

import org.junit.Test;

public class PokerTest {

    @Test
    public void testPoker() {
        /*
         * from collections import Counter

def PE54():
    def hand(cards):
        rankVals = {r:v for v,r in enumerate("23456789TJQKA")}
        ranks = sorted([card[:1] for card in cards])
        rankNums = sorted([rankVals[i] for i in ranks])
        suits = sorted([card[1:] for card in cards])
        
        if len(set(suits))==1 and ranks==['A','J','K','Q','T']:
            return 10, sorted(Counter(rankNums).most_common())[::-1], "Royal Flush"
        
        if len(set(suits))==1 and "23456789AJKQT".find(''.join(ranks))>-1:
            return 9, sorted(Counter(rankNums).most_common())[::-1], "Straight Flush"
        
        if ranks.count(ranks[0])==4 or ranks.count(ranks[-1])==4:
            return 8, Counter(rankNums).most_common(), "Four of a Kind"
        
        if ranks.count(ranks[0])*ranks.count(ranks[-1])==6:
            return 7, Counter(rankNums).most_common(), "Full House"
        
        if len(set(suits))==1:
            return 6, sorted(Counter(rankNums).most_common())[::-1], "Flush"
        
        if "23456789AJKQT".find(''.join(ranks))>-1:
            return 5, sorted(Counter(rankNums).most_common())[::-1], "Straight"
        
        if ranks.count(ranks[0])==3 or ranks.count(ranks[1])==3 or ranks.count(ranks[2])==3:
            hierarchy=sorted(Counter(rankNums).most_common(1))[::-1]
            hierarchy+=sorted(Counter(rankNums).most_common()[1:])[::-1]
            return 4, hierarchy, "Three of a Kind"
        
        if len(set(ranks))==3:
            hierarchy=sorted(Counter(rankNums).most_common(2))[::-1] 
            hierarchy.append(Counter(rankNums).most_common()[2])
            return 3, hierarchy, "Two Pair"
        
        if len(set(ranks))==4:
            hierarchy=sorted(Counter(rankNums).most_common(1))[::-1]
            hierarchy+=sorted(Counter(rankNums).most_common()[1:])[::-1]
            return 2, hierarchy, "One Pair"
        
        return 1, [max(Counter(rankNums).most_common())], "High Card"
    
    c=0
    for line in open("poker.txt"):
        cards = line.replace('\n','').split(' ')
        p1,p2=hand(sorted(cards[:5])),hand(sorted(cards[5:]))
        if p1[0]>p2[0]: c+=1
        elif p1[0]==p2[0]:
            for i in range(0,len(p1[1])):
                if p1[1][i][0] > p2[1][i][0]: c+=1
                if p1[1][i][0] != p2[1][i][0]: break
    return c

         */
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
