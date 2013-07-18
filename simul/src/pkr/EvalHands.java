package pkr;

import static pkr.Card.NUM_RANKS;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.possTree.PossibilityNode;
import pkr.possTree.PossibilityNode.HandCategory;
import pkr.possTree.PossibilityNode.HandSubCategory;
import pkr.possTree.PossibilityNode.TextureCategory;
import pkr.possTree.PossibilityNode.WinningLosingCategory;

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

        if (cards!=null)
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
    
    public static CompleteEvaluation[] evaluate(boolean heroOnly, HoleCards[] cards, Flop flop, Card turn, Card river) {

        
        
        final int numPlayers = cards.length;
        
        //Add all cards to same list
        List<Card> allCards = getAllCards(cards, flop, turn, river);
                
        //Check uniqueness
        checkUniqueness(allCards);

        CompleteEvaluation[] evals = new CompleteEvaluation[cards.length];
        
       
        TextureInfo communityCardsFlop = new TextureInfo();
        communityCardsFlop.addCards(flop.getCards());
        communityCardsFlop.calculate();
        
        TextureInfo communityCardsTurn = new TextureInfo();
        communityCardsTurn.addCards(flop.getCards());
        communityCardsTurn.addCard(turn);
        communityCardsTurn.calculate();
        
        TextureInfo communityCardsRiver = new TextureInfo();
        communityCardsRiver.addCards(flop.getCards());
        communityCardsRiver.addCard(turn);
        communityCardsRiver.addCard(river);
        communityCardsRiver.calculate();
        
        for(int i = 0; i < numPlayers; ++i) {
            
            HoleCards holeCards = cards[i];
            TextureInfo texInfo = new TextureInfo();
            texInfo.addCards(holeCards.getCards());
            texInfo.addCards(flop.getCards());        
            texInfo.calculate();
            
            TextureInfo texInfoTurn = new TextureInfo();
            texInfoTurn.addCards(holeCards.getCards());
            texInfoTurn.addCards(flop.getCards());
            texInfoTurn.addCard(turn);
            texInfoTurn.calculate();
            
            TextureInfo texInfoRiver = new TextureInfo();
            texInfoRiver.addCards(holeCards.getCards());
            texInfoRiver.addCards(flop.getCards());
            texInfoRiver.addCard(turn);
            texInfoRiver.addCard(river);
            texInfoRiver.calculate();
            
            evals[i] = new CompleteEvaluation();

            evals[i].setHoleCards(holeCards);
                       
            
            evals[i].setRoundScore(0, scoreSingleHand(texInfo));
            evals[i].setRoundScore(1, scoreSingleHand(texInfoTurn));
            evals[i].setRoundScore(2, scoreSingleHand(texInfoRiver));
            
            
           evals[i].setPosition(i);
        
           if (i == 0 || !heroOnly) {
            evaluateNodeSingleHand(
                   evals[i], CompleteEvaluation.ROUND_FLOP,  texInfo, communityCardsFlop,
                   evals[i].getRoundScore(CompleteEvaluation.ROUND_FLOP), flop, null, null );
            evaluateNodeSingleHand(
                    evals[i], CompleteEvaluation.ROUND_TURN, texInfoTurn, communityCardsTurn,
                    evals[i].getRoundScore(CompleteEvaluation.ROUND_TURN), flop, turn, null );
           evaluateNodeSingleHand(
                   evals[i], CompleteEvaluation.ROUND_RIVER, texInfoRiver, communityCardsRiver,
                   evals[i].getRoundScore( CompleteEvaluation.ROUND_RIVER), flop, turn, river );
           }
        }
        populateFlopTexture(heroOnly, evals, communityCardsFlop, 0, flop, null, null);
        populateFlopTexture(heroOnly, evals, communityCardsTurn, 1, flop, turn, null);
        populateFlopTexture(heroOnly, evals, communityCardsRiver, 2, flop, turn, river);
        
                    
        populateEvalutionNodeWithRelativeHandRankings(evals);
        
        
        
        return evals;
    }
    
    private static void populateFlopTexture(boolean heroOnly, CompleteEvaluation[] evals, TextureInfo communityCards, int round, Flop flop, Card turn, Card river) 
    {
        int[] freqCard = new int[NUM_RANKS];
        int[] freqSuit = new int[4];

        List<Card> allCards = getAllCards(null, flop, turn, river);
        Card[] allCardsArr = allCards.toArray(new Card[allCards.size()]);
        
       // PossibilityNode flopTexture = new PossibilityNode( PossibilityNode.TextureCategory.values() );

        for (Card card : allCardsArr) {
           // suits |= 1 << card.getSuit().ordinal();            
            freqCard[card.getRank().getIndex()]++;
            freqSuit[card.getSuit().ordinal()]++;
        }
        
        
       
        switch(communityCards.highestFreqSuit )
        {
        case 1:
            evals[0].setFlag(round, TextureCategory.UNSUITED);
            break;
        case 2:
            evals[0].setFlag(round, PossibilityNode.TextureCategory.SAME_SUIT_2);
            break;
        case 3:
            evals[0].setFlag(round, PossibilityNode.TextureCategory.SAME_SUIT_3);
            break;
        case 4:
            evals[0].setFlag(round, PossibilityNode.TextureCategory.SAME_SUIT_4);
            break;
        case 5:
            evals[0].setFlag(round, PossibilityNode.TextureCategory.SAME_SUIT_5);
            break;
        }
        
        if (communityCards.noPairedCards()) {
            evals[0].setFlag(round, TextureCategory.UNPAIRED_BOARD);
        } else {
            evals[0].setFlag(round, TextureCategory.PAIRED_BOARD);
        }
        
        if (heroOnly)
            return;
        
        for(CompleteEvaluation eval : evals) {
            eval.setPossibilityNode(round, PossibilityNode.Levels.TEXTURE.ordinal(),
                    evals[0].getPossibilityNode(round, PossibilityNode.Levels.TEXTURE.ordinal()));
        }
        
    }

    
    
    

   /**
    * Sets non relative flags on hand strength / draws 
    * @param eval
    * @param round
    * @param allCardsTexInfo
    * @param communityCards
    * @param score
    * @param flop
    * @param turn
    * @param river
    */
    
    public static void evaluateNodeSingleHand(CompleteEvaluation eval, 
            int round,
            TextureInfo allCardsTexInfo, TextureInfo communityCards, Score score, Flop flop,  Card turn, Card river)
    {
        int straightDrawCount = allCardsTexInfo.getStraightDrawCount(eval.getHoleCards());
        
        if(eval.getHoleCards().isSuited())
            {
            if (communityCards.freqSuit[eval.getHoleCards().getCards()[0].getSuit().ordinal()] == 2)
            {
                  eval.setFlag(round, HandCategory.FLUSH_DRAW);
            }
        } else if (communityCards.freqSuit[eval.getHoleCards().getCards()[0].getSuit().ordinal()] == 3)
        {
            eval.setFlag(round, HandCategory.FLUSH_DRAW);
        }
        else if (communityCards.freqSuit[eval.getHoleCards().getCards()[1].getSuit().ordinal()] == 3)
        {
            eval.setFlag(round, HandCategory.FLUSH_DRAW);
        }   
        
        
        switch(straightDrawCount) {
        case 0:
            break;
        case 1:
            eval.setFlag(round, HandCategory.STRAIGHT_DRAW_1);
            break;
        case 2:
            eval.setFlag(round, HandCategory.STRAIGHT_DRAW_2);
            break;
            default:
                log.warn("3 straight draws?");
        }
        
        //Top pair?
        if (score.handLevel == HandLevel.PAIR)
        {
            if (communityCards.noPairedCards()) {
                int ranksAbove = communityCards.getRanksAbove(score.kickers[0]);
                switch(ranksAbove) 
                {
                case 0:
                    eval.setFlag(round, HandCategory.PAIR_OVERCARDS_0);
                    break;
                case 1:
                    eval.setFlag(round, HandCategory.PAIR_OVERCARDS_1);
                    break;
                case 2:
                    eval.setFlag(round, HandCategory.PAIR_OVERCARDS_2);
                    break;
                case 3:
                    default:
                    eval.setFlag(round, HandCategory.PAIR_OVERCARDS_3);
                    break;
                }
                
                if (eval.getHoleCards().getCards()[0].getRank() == eval.getHoleCards().getCards()[1].getRank()) {
                    eval.setFlag(round, HandCategory.HIDDEN_PAIR);
                }
                
            } else {
                eval.setFlag(round, HandCategory.PAIR_ON_PAIRED_BOARD);
            }
            
            
        } else if (score.handLevel == HandLevel.TRIPS) 
        {
            if (communityCards.noPairedCards()) 
            {
                eval.setFlag(round, HandCategory.HIDDEN_SET);
            } else {
                eval.setFlag(round, HandCategory.VISIBLE_SET);
            }
        } else if (score.handLevel == HandLevel.TWO_PAIR) 
        {
            if (communityCards.firstPair == -1) {
                eval.setFlag(round, HandCategory.HIDDEN_TWO_PAIR);
            } else {
                eval.setFlag(round, HandCategory.TWO_PAIR);
            }
        } else if (score.handLevel == HandLevel.FULL_HOUSE) 
        {
            eval.setFlag(round, HandCategory.FULL_HOUSE);
        } else if (score.handLevel == HandLevel.HIGH_CARD) {
            eval.setFlag(round, HandCategory.HIGH_CARD);
        } else if (score.handLevel == HandLevel.QUADS) {
            eval.setFlag(round, HandCategory.QUADS);
        } else if (score.handLevel == HandLevel.FLUSH) {
            eval.setFlag(round, HandCategory.FLUSH);
        } else if (score.handLevel == HandLevel.STRAIGHT) {
            eval.setFlag(round, HandCategory.STRAIGHT);
        }
    }
    
    private static class CompareByRoundScore implements Comparator<CompleteEvaluation>
    {
        final int round;
        
        private CompareByRoundScore(int round) {
            super();
            this.round = round;
        }

        @Override
        public int compare(CompleteEvaluation o1, CompleteEvaluation o2)
        {
            return o1.getRoundScore(round).compareTo(o2.getRoundScore(round));
        }
    }
    
    /**
     * 
     * side effet -- sets winning flag
     * @param round
     * @param resultsSortedByScore
     * @return -1 for all way tie
     */
    private static int getNextBestHandIndex(int start, int round, CompleteEvaluation[] resultsSortedByScore)
    {
        int sortedEvalIndex = start;
        
        final Score bestHandScore =  resultsSortedByScore[sortedEvalIndex].getRoundScore(round);
        
        for(; sortedEvalIndex >= 0; --sortedEvalIndex)
        {
            CompleteEvaluation curEval = 
                    resultsSortedByScore[sortedEvalIndex]; 
            
            if (!bestHandScore
                    .equals(curEval.getRoundScore(round)))            
                break;            
        }
        
        return sortedEvalIndex;
    }
    
    private static void setWinningFlags(int round, CompleteEvaluation[] resultsSortedByScore, int secondBestHandIndex)
    {
        int numTiedForFirst = resultsSortedByScore.length-1 - secondBestHandIndex;
        
        for(int winningEvalIndex = resultsSortedByScore.length-1; winningEvalIndex > secondBestHandIndex; --winningEvalIndex)
        {
            CompleteEvaluation winEval = resultsSortedByScore[winningEvalIndex];            
            winEval.setRealEquity(1.0 / numTiedForFirst);
            winEval.setFlag(round, WinningLosingCategory.WINNING);
        }
    }
    
    private static void setVillianHands(int round, CompleteEvaluation hero, Score bestHandScore )
    {
        if (hero.hasFlag(round, WinningLosingCategory.WINNING))
            return;
        
        if (bestHandScore.handLevel == HandLevel.HIGH_CARD) {
            hero.setFlag(round, HandSubCategory.VILLAIN_HIGH_CARD);
        } else if (bestHandScore.handLevel == HandLevel.PAIR) {
            hero.setFlag(round, HandSubCategory.VILLAIN_PAIR);
        } else if (bestHandScore.handLevel == HandLevel.TWO_PAIR) {
            hero.setFlag(round, HandSubCategory.VILLAIN_TWO_PAIR);
        } else if (bestHandScore.handLevel == HandLevel.TRIPS) {
            hero.setFlag(round, HandSubCategory.VILLAIN_TRIPS);
        } else if (bestHandScore.handLevel == HandLevel.STRAIGHT) {
            hero.setFlag(round, HandSubCategory.VILLAIN_STRAIGHT);
        } else if (bestHandScore.handLevel == HandLevel.FLUSH) {
            hero.setFlag(round, HandSubCategory.VILLAIN_FLUSH);
        } else {
            hero.setFlag(round, HandSubCategory.VILLAIN_OTHER);
        }
    
    }
    
    private static HandSubCategory getWonByFlag(Score bestHandScore, Score curScore)
    {
        HandSubCategory cat = null;
        if (bestHandScore.getHandLevel() != curScore.getHandLevel()) {
            cat = HandSubCategory.BY_HAND_CATEGORY;
        } else if (bestHandScore.getKickers()[0] != curScore.getKickers()[0]) {
            cat = HandSubCategory.BY_KICKER_HAND;
        } else if (bestHandScore.getKickers()[1] != curScore.getKickers()[1]) {
            cat = HandSubCategory.BY_KICKER_1;
        } else if (bestHandScore.getKickers()[2] != curScore.getKickers()[2]) {
            cat = HandSubCategory.BY_KICKER_2_PLUS;
        } else if (bestHandScore.getKickers()[3] != curScore.getKickers()[3]) {
            cat = HandSubCategory.BY_KICKER_2_PLUS;
        } else if (bestHandScore.getKickers()[4] != curScore.getKickers()[4]) {
            cat = HandSubCategory.BY_KICKER_2_PLUS;
        }
        
        Preconditions.checkNotNull(cat);


        return cat;
    }
    
    public static void populateEvalutionNodeWithRelativeHandRankings( CompleteEvaluation[] eval ) {
     
        CompleteEvaluation[][] resultsSortedByRoundScore = new CompleteEvaluation[3][eval.length];
        
        for(int i = 0; i < eval.length; ++i) {
            resultsSortedByRoundScore[0][i] = eval[i];
            resultsSortedByRoundScore[1][i] = eval[i];
            resultsSortedByRoundScore[2][i] = eval[i];
        }
        
        for(int round = 0; round < 3; ++round) 
        {
            Arrays.sort(resultsSortedByRoundScore[round], new CompareByRoundScore(round));
            
            int sortedEvalIndex = eval.length - 1;
            final int bestHandIndex = eval.length - 1;
            
            final Score bestHandScore =  resultsSortedByRoundScore[round][bestHandIndex].getRoundScore(round);
                                    
            final int secondBestHandIndex = getNextBestHandIndex(bestHandIndex, round, resultsSortedByRoundScore[round]);
            
            setWinningFlags(round, resultsSortedByRoundScore[round], secondBestHandIndex);
            
            
            if (secondBestHandIndex >= 0) {
                
                /**
                 * Nous voulons que si le héro a gagné, on ajoute la deuxième
                 * meilleur main à la liste
                 */
                eval[0].addSecondBestHand(round,
                        resultsSortedByRoundScore[round][secondBestHandIndex]
                                .getHoleCards());
                eval[0].addBestHand(round,
                        resultsSortedByRoundScore[round][bestHandIndex]
                                .getHoleCards());

                setVillianHands(round, eval[0], bestHandScore);

                // Second best hand exists

                //either the 3rd best or -1 if no 3rd place
                int thirdBestHand = getNextBestHandIndex(secondBestHandIndex, round, resultsSortedByRoundScore[round]);
                
                //Set flags for all losing hands
                for(sortedEvalIndex = secondBestHandIndex; sortedEvalIndex >= 0; --sortedEvalIndex) 
                {
                    CompleteEvaluation curEval = resultsSortedByRoundScore[round][sortedEvalIndex];
                    Score curScore = curEval.getRoundScore(round);
                
                    HandSubCategory cat = getWonByFlag(bestHandScore, curScore);
                    
                    curEval.setFlag(round, cat);
                    curEval.setRealEquity(0);
                    
                    if (sortedEvalIndex > thirdBestHand) {
                        curEval.setFlag(round, WinningLosingCategory.SECOND_BEST_HAND);
                    } else {
                        curEval.setFlag(round, WinningLosingCategory.LOSING);
                    }
                    
                    /*
                     * Set flags for best hand
                     */
                    if (sortedEvalIndex == secondBestHandIndex) {
                        //we can just used the flag for the 2nd best hand in the best hand as they won for the same reason the 2nd best hand lost
                        for(int winningEvalIndex = bestHandIndex; winningEvalIndex > secondBestHandIndex; --winningEvalIndex)
                        {
                            CompleteEvaluation winEval = resultsSortedByRoundScore[round][winningEvalIndex];
                        
                            winEval.setFlag(round, cat);
                        }
                    }
                }
                
                
                
            } else {
                //Second best hand does not exist; all way tie, just say that the all way tie won by a hand
                for(sortedEvalIndex = bestHandIndex; sortedEvalIndex >= 0; --sortedEvalIndex) {
                    CompleteEvaluation curEval = resultsSortedByRoundScore[round][sortedEvalIndex];
                    curEval.setFlag(round, HandSubCategory.BY_HAND_CATEGORY);
                }
            }
        }
        
    }
    
    
    
    public static Score scoreSingleHand(TextureInfo texInfo) {
        
        Score score = new Score();
        
        // straight flush
        if (texInfo.straightRank >= 0 && texInfo.flush) {
            score.setHandLevel(HandLevel.STRAIGHT_FLUSH);
            score.setKickers(
                    new CardRank[] { CardRank.ranks[texInfo.straightRank]
                             });
            return score;
        }

        // 4 kind
        if (texInfo.fourKind >= 0) {
            score.setHandLevel(HandLevel.QUADS);

            CardRank kicker = texInfo.getHighestRank(CardRank.ranks[texInfo.fourKind],null);
            
            score.setKickers(
                    new CardRank[] { 
                            CardRank.ranks[texInfo.fourKind],
                            kicker });
    
            return score;
        }

        // full house
        if (texInfo.threeKind >= 0 && texInfo.firstPair >= 0) {

            score.setHandLevel(HandLevel.FULL_HOUSE);
            
            score
            .setKickers(
                    new CardRank[] { 
                            CardRank.ranks[texInfo.threeKind],
                            CardRank.ranks[texInfo.firstPair] });
            return score;            
        }

        if (texInfo.flush) {
           
            score.setHandLevel(HandLevel.FLUSH);

            score.setKickers(new CardRank[5]);

            // ace to two
            int kickerIndex = 0;
            for (int cardIdx = texInfo.sortedCards.size() - 1; cardIdx >= 0; --cardIdx) {
                Card card = texInfo.sortedCards.get(cardIdx);

                if (card.getSuit() != texInfo.flushSuit)
                    continue;

                score.getKickers()[kickerIndex++] = card.getRank();

                if (kickerIndex == 5)
                    break;
            }
            return score;
        }

        // straight
        if (texInfo.straightRank >= 0) {
            score.setHandLevel(HandLevel.STRAIGHT);
            score.setKickers(
                    new CardRank[] { CardRank.ranks[texInfo.straightRank] });
            return score;
        }

        // 3 kind
        if (texInfo.threeKind >= 0) {
            score.setHandLevel(HandLevel.TRIPS);

            score.setKickers(new CardRank[3]);

            score.getKickers()[0] = CardRank.ranks[texInfo.threeKind];
            score.getKickers()[1] = texInfo.getHighestRank(score.getKickers()[0], null);            
            score.getKickers()[2] = texInfo.getHighestRank(score.getKickers()[0], score.getKickers()[1]);
            
            return score;
        }

        // 2 pair
        if (texInfo.firstPair >= 0 && texInfo.secondPair >= 0) {
            score.setHandLevel(HandLevel.TWO_PAIR);

            score.setKickers(new CardRank[3]);

            score.getKickers()[0] = CardRank.ranks[texInfo.firstPair];
            score.getKickers()[1] = CardRank.ranks[texInfo.secondPair];
            
            score.getKickers()[2] = texInfo.getHighestRank(score.getKickers()[0], score.getKickers()[1]);

            return score;

        }

        // Pair
        if (texInfo.firstPair >= 0) {
            score.setHandLevel(HandLevel.PAIR);

            int kickers = 1;
            score.setKickers(new CardRank[4]);

            score.getKickers()[0] = CardRank
                    .getFromZeroBasedValue(texInfo.firstPair);

            for (int cardIdx = texInfo.sortedCards.size() - 1; cardIdx >= 0; --cardIdx) {
                Card card = texInfo.sortedCards.get(cardIdx);
                
                if (card.getRank() == score.getKickers()[0])
                    continue;

                Preconditions.checkState(texInfo.freqCard[card.getRank().getIndex()] == 1);
                
                score.getKickers()[kickers++] = card.getRank();

                if (kickers == 4)
                    break;
            }
            
            Preconditions.checkState(kickers == 4);
            return score;

        }

        // High card
        score.setHandLevel(HandLevel.HIGH_CARD);

        int kickers = 0;
        score.setKickers(new CardRank[5]);
        
        for (int cardIdx = texInfo.sortedCards.size() - 1; cardIdx >= 0; --cardIdx) {
            Card card = texInfo.sortedCards.get(cardIdx);

            Preconditions.checkState(texInfo.freqCard[card.getRank().getIndex()] == 1);
            
            score.getKickers()[kickers++] = card.getRank();

            if (kickers == 5)
                break;
        }
        
        Preconditions.checkState(kickers == 5);
        return score;
        

    }
}