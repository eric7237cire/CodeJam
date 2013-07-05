package pkr;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.possTree.EvaluationNode;
import pkr.possTree.EvaluationNode.EvaluationCategory;
import pkr.possTree.FlopTextureNode;
import pkr.possTree.FlopTextureNode.TextureCategory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class EvalHands {
    
    private static Logger log = LoggerFactory.getLogger(EvalHands.class);
    
    private static void checkUniqueness(List<Card> allCards)
    {
        boolean[] seenCard = new boolean[52];
        
        for (Card card : allCards) {
            
            
            if (seenCard[card.toInt()]) {
                log.warn("Cards {} ", allCards);
            }
            
            Preconditions.checkState(!seenCard[card.toInt()], card.toString());

            seenCard[card.toInt()] = true;
        }
    }
    
    private static List<Card> getAllCards(HoleCards[] cards, Flop flop, Card turn, Card river)
    {
        List<Card> allCards = Lists.newArrayList();

        
        for (HoleCards hCards : cards) {
            allCards.addAll(Arrays.asList(hCards.getCards()));            
        }
        
        allCards.addAll(Arrays.asList(flop.getCards()));
        
        if (turn != null)
            allCards.add(turn);
        
        if (river != null)
            allCards.add(river);
        
        return allCards;
    }
    
    public static Evaluation[] evaluate(HoleCards[] cards, Flop flop, Card turn, Card river) {

        
        
        final int numPlayers = cards.length;
        
        //Add all cards to same list
        List<Card> allCards = getAllCards(cards, flop, turn, river);
                
        //Check uniqueness
        checkUniqueness(allCards);

        Evaluation[] evals = new Evaluation[cards.length];
        
        for(int i = 0; i < numPlayers; ++i) {
            evals[i] = evaluateSingleHand(cards[i], flop, turn, river);
            
            //Evaluation flopEval = evaluateSingleHand(cards[i], flop, null, null);
            
           evals[i].setPosition(i);
        }
        
        populateFlopTexture(evals, flop, turn, river);
        
        Evaluation[] resultsSortedByScore = new Evaluation[numPlayers];
        
        for(int i = 0; i < numPlayers; ++i) {
            resultsSortedByScore[i] = evals[i];
        }
        
        Arrays.sort(resultsSortedByScore);
        
        resultsSortedByScore[numPlayers - 1].setWon(true);
        resultsSortedByScore[numPlayers - 1].setRealEquity(1.0);
        
        int secondBestHandIndex = numPlayers - 2;
        
        if (resultsSortedByScore[numPlayers - 1].getScore().compareTo(
                resultsSortedByScore[numPlayers - 2].getScore())==0) {
            resultsSortedByScore[numPlayers - 1].setTied(true);
            int numTied = 1;
            
            
            
            for(; secondBestHandIndex >= 0; --secondBestHandIndex) {
                if (resultsSortedByScore[numPlayers - 1].getScore().compareTo(
                        resultsSortedByScore[secondBestHandIndex].getScore())==0) {
                    resultsSortedByScore[secondBestHandIndex].setTied(true);
                    ++numTied;
                } else {
                    break;
                }
            }
            
            secondBestHandIndex += 1;
            
            for(int j = numPlayers - 1; j >= secondBestHandIndex; --j) {
                resultsSortedByScore[j].setRealEquity(1.0 / numTied);
            }
            
        }
        
            
        
        
        
        return evals;
    }
    
    private static void populateFlopTexture(Evaluation[] evals, Flop flop, Card turn, Card river) 
    {
        int[] freqCard = new int[NUM_RANKS];
        int[] freqSuit = new int[4];

        FlopTextureNode flopTexture = new FlopTextureNode(flop.getCards());

        for (Card card : flop.getCards()) {
           // suits |= 1 << card.getSuit().ordinal();            
            freqCard[card.getRank().getIndex()]++;
            freqSuit[card.getSuit().ordinal()]++;
        }
        
        for(int s = 0; s < 4; ++s)
        {
            if (freqSuit[s] == 2) {
                flopTexture.setFlag(TextureCategory.SAME_SUIT_2);
            } else if (freqSuit[s] == 3) {
                flopTexture.setFlag(TextureCategory.SAME_SUIT_3);
            }
        }
        
        for(Evaluation eval : evals) {
            eval.setFlopTexture(flopTexture);
        }
        
    }

    private static int[] straightBitMasks = {
            15 + (1 << 12), // a + 2 to 4
            31, 31 << 1, 31 << 2, 31 << 3, 31 << 4, 31 << 5, 31 << 6, 31 << 7,
            31 << 8 };
    
    private static final int NUM_RANKS = 13;

    public static Evaluation evaluateSingleHand(HoleCards cards, Flop flop,  Card turn, Card river)
    {
        Evaluation eval = new Evaluation();

        eval.setHoleCards(cards);
        
        eval.setFlopScore(scoreSingleHand(cards, flop, null, null));
        eval.setTurnScore(scoreSingleHand(cards, flop, turn, null));
        eval.setRiverScore(scoreSingleHand(cards, flop, turn, river));
        
        eval.setFlopEval(evaluateNodeSingleHand(cards, eval.getFlopScore(), flop, null, null ));
        eval.setTurnEval(evaluateNodeSingleHand(cards, eval.getTurnScore(), flop, turn, null ));
        eval.setRiverEval(evaluateNodeSingleHand(cards, eval.getRiverScore(), flop, turn, river ));
        return eval;
    }
    
    public static EvaluationNode evaluateNodeSingleHand(HoleCards cards, Score score, Flop flop,  Card turn, Card river)
    {
        EvaluationNode evalNode = new EvaluationNode();
        
        //Top pair?
        if (score.handLevel == HandLevel.PAIR)
        {
            //exclude cases like 72  flop TT4
            
            if (!flop.isPaired && score.kickers[0] == flop.getSortedCards()[2].getRank()) 
            {
                evalNode.setFlag(EvaluationCategory.TOP_PAIR);
            }
        }
        
        return evalNode;
    }
    
    public static void populateEvalutionNodeWithRelativeHandRankings( Evaluation[] eval ) {
        
    }
    
    public static Score scoreSingleHand(HoleCards cards, Flop flop,  Card turn, Card river) {
        
        Score score = new Score();
        
        List<Card> hand = Lists.newArrayList();

        hand.addAll(Arrays.asList(cards.getCards()));
        hand.addAll(Arrays.asList(flop.getCards()));
        if (turn != null)
            hand.add(turn);
        
        if (river != null)
            hand.add(river);
        Collections.sort(hand);

        
        int ranks = 0;

        int[] freqCard = new int[NUM_RANKS];
        int[] freqSuit = new int[4];

        boolean flush = false;
        Suit flushSuit = null;

        for (Card card : hand) {
           // suits |= 1 << card.getSuit().ordinal();
            ranks |= (1 << card.getRank().getIndex());
            freqCard[card.getRank().getIndex()]++;
            freqSuit[card.getSuit().ordinal()]++;

            if (freqSuit[card.getSuit().ordinal()] >= 5) {
                flush = true;
                flushSuit = card.getSuit();
            }
        }

        int straightRank = -1;

        for (int sbmIdx = straightBitMasks.length - 1; sbmIdx >= 0 ; --sbmIdx) {
            if ( (ranks & straightBitMasks[sbmIdx]) == straightBitMasks[sbmIdx] ) {
                straightRank = sbmIdx + 3;
                break;
            }
        }

        int fourKind = -1;
        int threeKind = -1;
        int firstPair = -1;
        int secondPair = -1;
        List<Integer> singleCard = Lists.newArrayList();

        for (int r = NUM_RANKS-1; r >= 0; --r) {
            if (freqCard[r] == 4) {
                fourKind = r;
            } else if (threeKind == -1 && freqCard[r] == 3) {
                threeKind = r;
                
            } else if ( (freqCard[r] == 2 || freqCard[r] == 3)  && firstPair == -1) {
                //first pair could be trips, ie 333222 really has a pair of 2's
                firstPair = r;
            } else if (freqCard[r] == 2 && secondPair == -1) {
                secondPair = r;
            } else if (freqCard[r] == 1) {
                singleCard.add(r);
            }
        }


        // straight flush
        if (straightRank >= 0 && flush) {
            score.setHandLevel(HandLevel.STRAIGHT_FLUSH);
            score.setKickers(
                    new CardRank[] { CardRank
                            .getFromZeroBasedValue(straightRank ) });
            return score;
        }

        // 4 kind
        if (fourKind >= 0) {
            score.setHandLevel(HandLevel.QUADS);

            // Find first non quad kicker
            for (int r = NUM_RANKS-1; r >= 0; --r) {
                if (freqCard[r] < 4 && freqCard[r] > 0) {
                    score
                            .setKickers(
                                    new CardRank[] { 
                                            CardRank.getFromZeroBasedValue(fourKind),
                                            CardRank
                                            .getFromZeroBasedValue(r) });
                    break;
                }
            }
            return score;
        }

        // full house
        if (threeKind >= 0 && firstPair >= 0) {

            score.setHandLevel(HandLevel.FULL_HOUSE);
            
            score
            .setKickers(
                    new CardRank[] { 
                            CardRank.getFromZeroBasedValue(threeKind)
                            ,CardRank.getFromZeroBasedValue(firstPair) });
            return score;
    
            
            
        }

        if (flush) {
           
            score.setHandLevel(HandLevel.FLUSH);

            score.setKickers(new CardRank[5]);

            // ace to two
            int kickerIndex = 0;
            for (int cardIdx = hand.size() - 1; cardIdx >= 0; --cardIdx) {
                Card card = hand.get(cardIdx);

                if (card.getSuit() != flushSuit)
                    continue;

                score.getKickers()[kickerIndex++] = card.getRank();

                if (kickerIndex == 5)
                    break;
            }
            return score;
        }

        // straight
        if (straightRank >= 0) {
            score.setHandLevel(HandLevel.STRAIGHT);
            score.setKickers(
                    new CardRank[] { CardRank
                            .getFromZeroBasedValue(straightRank ) });
            return score;
        }

        // 3 kind
        if (threeKind >= 0) {
            score.setHandLevel(HandLevel.TRIPS);

            int kickers = 1;

            score.setKickers(new CardRank[3]);

            score.getKickers()[0] = CardRank
                    .getFromZeroBasedValue(threeKind);

            for (int r = NUM_RANKS-1; r >= 0; --r) {
                if (freqCard[r] == 1) {
                    score.getKickers()[kickers++] = CardRank
                            .getFromZeroBasedValue(r);

                }

                if (kickers == 3)
                    break;
            }
            return score;
        }

        // 2 pair
        if (firstPair >= 0 && secondPair >= 0) {
            score.setHandLevel(HandLevel.TWO_PAIR);

            score.setKickers(new CardRank[3]);

            score.getKickers()[0] = CardRank
                    .getFromZeroBasedValue(firstPair);
            score.getKickers()[1] = CardRank
                    .getFromZeroBasedValue(secondPair);

            for (int r = NUM_RANKS-1; r >= 0; --r) {
                if (freqCard[r] >= 1 && r != firstPair && r != secondPair) {
                    score.getKickers()[2] = 
                            CardRank.getFromZeroBasedValue(r);
                    break;
                }

                
            }
            return score;

        }

        // Pair
        if (firstPair >= 0) {
            score.setHandLevel(HandLevel.PAIR);

            int kickers = 1;
            score.setKickers(new CardRank[4]);

            score.getKickers()[0] = CardRank
                    .getFromZeroBasedValue(firstPair);

            for (int r = NUM_RANKS-1; r >= 0; --r) {
                if (freqCard[r] == 1) {
                    score.getKickers()[kickers++] = CardRank
                            .getFromZeroBasedValue(r);

                }

                if (kickers == 4)
                    break;
            }
            return score;

        }

        // High card
        score.setHandLevel(HandLevel.HIGH_CARD);

        int kickers = 0;
        score.setKickers(new CardRank[5]);

        
        for (int r = NUM_RANKS-1; r >= 0; --r) {
            if (freqCard[r] == 1) {
                score.getKickers()[kickers++] = CardRank
                        .getFromZeroBasedValue(r);

            }

            if (kickers == 5)
                break;
        }
        return score;

    }
}
