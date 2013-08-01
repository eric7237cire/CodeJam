package pkr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.possTree.PossibilityNode.HandCategory;
import pkr.possTree.PossibilityNode.TextureCategory;
import pkr.possTree.Tree;

import com.google.common.collect.Lists;

public class Simulator {
    
    private static Logger log = LoggerFactory.getLogger(Simulator.class);
    private static Logger logOutput = LoggerFactory.getLogger("mainOutput");
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        List<String> playerHoleCards = Lists.newArrayList();
        
       // playerHoleCards.add("AA, KK, QQ, JJ, TT, 99, 88, 77, 66, 55, 44, 33, 22, AK, AQ, AJ");
        //playerHoleCards.add("AA, AKs, 27, 93, 44, 99");
      //playerHoleCards.add("KTs");
      
     // playerHoleCards.add("99");
    //  playerHoleCards.add("AKs");
      //  playerHoleCards.add("KJ");
    //  playerHoleCards.add("Q2s+, J2+, T2+, 32+");
     // playerHoleCards.add("A2o, A3o, A4o, A5o");
        
        //playerHoleCards.add("K8o, K9o, KTo");
        
        //playerHoleCards.add("AA");
        //playerHoleCards.add("KJo");
      //  playerHoleCards.add("AKo");
      //  playerHoleCards.add("JTs");
        playerHoleCards.add("A8o");
        
       // playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+, 22+");
       // playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+, 22+");
        
      int NUM_RANDOM = 1;
        int NUM_LOOSE_CALLS = 1;
        int NUM_OK_CALLS = 1;
        int NUM_GOOD_HANDS = 0;
        
        //offsuit A2 K7 Q8 J8 T8 T9 98(limit)
        //suited A2 K2 Q4 J6 T6 96 86?
        //22+
        
        //random
        for(int i = 0; i < NUM_RANDOM; ++i) {
            playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+,22+");
        }
        
        final String SUPER_LOOSE = "22+, A2s+, K2s+, Q2s+, J2s+, T2s+, 92s+, 82s+, 72s+," +
                " 62s+, 52s+, 42s+, 32s," +
                "A2o+, K2o+, Q2o+, J2o+, T2o+, 95o+, 85o+, 75o+, 64o+, 54o, 43o, 32o";
        
        final String LOOSE = "22+, A2s+, K2s+, Q2s+, J2s+, T2s+, 92s+, 85s+, 74s+," +
                " 63s+, 53s+, 42s+, 32s," +
                "A2o+, K2o+, Q2o+, J4o+, T5o+, 95o+, 85o+, 75o+, 64o+, 54o";
        
        //loose call, top 50% of hands
        for(int i = 0; i < NUM_LOOSE_CALLS; ++i) {
            //playerHoleCards.add("22+, A2s+, K2s+, Q2s+, J4s+, T6s+, 96s+, 86s+, 75s+, 65s, 54s, A2o+, K2o+, Q5o+, J7o+, T7o+, 98o, 87o, 76o, 65o");
           // playerHoleCards.add(SUPER_LOOSE);
            playerHoleCards.add(LOOSE);
        }
        
        final String MY_LIST = "22+, A2s+, K2s+, Q4s+, J6s+, T6s+, " +
        		"96s+, 86s+, 76s, 65s, 54s," +
        		" A2o+, K8o+, Q8o+, J8o+, T8o+, 98o";
        //playerHoleCards.add(MY_LIST);
        
        final String LOOSE_ALL_IN = "22+, A2+, K9+, Q9s+, QTo+, JT+ " 
        		;
        
        //playerHoleCards.add(LOOSE_ALL_IN);
        
        //top 36%
        for(int i = 0; i < NUM_OK_CALLS; ++i) {
        playerHoleCards.add("22+, A2s+, K7s+, Q7s+, J7s+, T7s+, 98s, 87s, 76s, A2o+, K7o+, Q8o+, J9o+, T9o, 98o");
        }
        
        //top 18%
        for(int i = 0; i < NUM_GOOD_HANDS; ++i) {
        playerHoleCards.add("55+, A8s+, K9s+, QTs+, JTs, T9s, 98s, 87s, A8o+, KJo+, QJo, JTo");
        }
       // 
        //playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+");
        //playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+");
        
                
        String fileName = "C:\\codejam\\CodeJam\\simul\\out.xml";
        
        Tree tree = simulate(60001, playerHoleCards);
        tree.output(fileName);

        
        
    }
    
    public static interface iCriteria
    {
        public boolean matches(CompleteEvaluation[] evals);
        public boolean isApplicable(CompleteEvaluation eval);
    }
        
    public static class Criteria implements iCriteria
    {
        int round;
        String desc;
        boolean heroOnly;
        boolean allMustMatch;
                
        //Any match will do
        List<HandCategory> matchHandCat;
        List<HandCategory> matchNegHandCat;
        
        //For applicable
        List<TextureCategory> mustHave;
        List<TextureCategory> mustNotHave;
        
        int applicableCount = 0;
        int matches = 0;
        
        private Criteria(int round, String desc) {
            super();
            this.round = round;
            this.desc = desc;
            
            mustHave = Lists.newArrayList();
            mustNotHave = Lists.newArrayList();
            
            matchHandCat = Lists.newArrayList();
            matchNegHandCat = Lists.newArrayList();
        }
        
        public void printMsg() {
            if (applicableCount == 0) {
                logOutput.debug("{}.  No applicable cases", desc);
                return;
            }
            logOutput.debug("{}.  {} / {} =  %{} ", desc, matches, applicableCount,
                    100.0 * matches / applicableCount);
        }

        /* (non-Javadoc)
         * @see pkr.Simulator.iCriteria#matches(pkr.CompleteEvaluation[])
         */
        @Override
        public boolean matches(CompleteEvaluation[] evals) {
            if (heroOnly)
            {
                for(HandCategory cat : matchHandCat)
                {
                    if (evals[0].hasFlag(round, cat))
                        return true;
                }
                 
            } else if (allMustMatch) 
            {
                for (CompleteEvaluation eval : evals) {
                    boolean ok = false;
                    for(HandCategory cat : matchHandCat)
                    {
                        if (eval.hasFlag(round, cat))
                        {
                            ok = true;
                            break;
                        }
                    }
                    
                    if (!ok)
                        return false;
                    
                    for(HandCategory cat : matchNegHandCat)
                    {
                        if (eval.hasFlag(round, cat))
                        {
                            return false;
                        }
                    }
                    
                }
                
                return true;
            } else {
                for (CompleteEvaluation eval : evals) {
                    for(HandCategory cat : matchHandCat)
                    {
                        if (eval.hasFlag(round, cat))
                        {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        
        public void calculate(CompleteEvaluation[] evals)
        {
            if (!isApplicable(evals[0]))
                return;
            
            ++applicableCount;
            
            if (matches(evals)) 
                ++matches;
        }

        /* (non-Javadoc)
         * @see pkr.Simulator.iCriteria#isApplicable(pkr.CompleteEvaluation)
         */
        @Override
        public boolean isApplicable(CompleteEvaluation eval) {
            for(TextureCategory cat : mustHave)
            {
                if (!eval.hasFlag(round, cat))
                    return false;
            }
            
            for(TextureCategory cat : mustNotHave)
            {
                if (eval.hasFlag(round, cat))
                    return false;
            }
            return true;
        }
    }
    
    public static Tree simulate(int TOTAL_SIMULATIONS, List<String> playerHoleCards) {
        
        Tree tree = new Tree();
        
        //Setup the ranges
        HoleCardsRange[] listRanges = new HoleCardsRange[playerHoleCards.size()];
        
        for(int i = 0; i < playerHoleCards.size(); ++i) {
            listRanges[i] = new HoleCardsRange(playerHoleCards.get(i));
        }
        
        int actualRounds = 0;
        
        final int numPlayers = playerHoleCards.size();
        
        double[] equity = new double[numPlayers];
        
        List<Criteria> criteres = Lists.newArrayList();
        List<Criteria> pairedBoardCriteres = Lists.newArrayList();
        List<Criteria> unPairedBoardCriteres = Lists.newArrayList();
        List<Criteria> allBoardCriteres = Lists.newArrayList();
        
        
        for(int round = 0; round < 3; ++round)
        {
            String roundStr =  
                round == 0 ? "FLOP " :
                (round == 1 ? "TURN " : "RIVER");
            
            Criteria anythingOnPairedFlop = new Criteria(round, roundStr + " Anyone on paired board");
            anythingOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);
            anythingOnPairedFlop.matchHandCat.add( HandCategory.TWO_PAIR_USING_ONE );
            anythingOnPairedFlop.matchHandCat.add( HandCategory.SET_USING_ONE );
            anythingOnPairedFlop.matchHandCat.add( HandCategory.FULL_HOUSE );
            anythingOnPairedFlop.matchHandCat.add( HandCategory.QUADS );
            
            Criteria tripsOnPairedFlop = new Criteria(round, roundStr + " Anyone trips on paired board");
            tripsOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
            tripsOnPairedFlop.matchHandCat.add( HandCategory.SET_USING_ONE );
            
            Criteria twoPairOnPairedFlop = new Criteria(round, roundStr + " 2 pair on paired board");
            twoPairOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
            twoPairOnPairedFlop.matchHandCat.add( HandCategory.TWO_PAIR_USING_ONE );
            
            Criteria nothingOnPairedFlop = new Criteria(round, roundStr + " nothing on paired board");
            nothingOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
            nothingOnPairedFlop.matchHandCat.add( HandCategory.PAIR_ON_PAIRED_BOARD );
            nothingOnPairedFlop.allMustMatch = true;
            
            pairedBoardCriteres.add(anythingOnPairedFlop);
            pairedBoardCriteres.add(tripsOnPairedFlop);
            pairedBoardCriteres.add(twoPairOnPairedFlop);
            pairedBoardCriteres.add(nothingOnPairedFlop);
            
            
            Criteria flushDrawCrit = new Criteria(round, roundStr + " flush draw");
            //nothingOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
            flushDrawCrit.matchHandCat.add( HandCategory.FLUSH_DRAW );
            
            Criteria straightDrawCrit = new Criteria(round, roundStr + " straight draw (inc gutshots)");
            //nothingOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
            straightDrawCrit.matchHandCat.add( HandCategory.STRAIGHT_DRAW_2 );
            straightDrawCrit.matchHandCat.add( HandCategory.STRAIGHT_DRAW_1 );

            Criteria straight2DrawCrit = new Criteria(round, roundStr + " straight draw");
            //nothingOnPairedFlop.mustHave.add(TextureCategory.PAIRED_BOARD);            
            straight2DrawCrit.matchHandCat.add( HandCategory.STRAIGHT_DRAW_2 );
            

            allBoardCriteres.add(flushDrawCrit);
            allBoardCriteres.add(straightDrawCrit);
            allBoardCriteres.add(straight2DrawCrit);
            
            
            
            Criteria highCardOnly  = new Criteria(round, roundStr + " nothing on unpaired board");
            highCardOnly.allMustMatch = true;
            highCardOnly.matchHandCat.add(HandCategory.HIGH_CARD);
            highCardOnly.mustHave.add(TextureCategory.UNPAIRED_BOARD);
            
            unPairedBoardCriteres.add(highCardOnly);
            
            Criteria reallyNothing = new Criteria(round, roundStr + " no pairs no draws on unpaired board");
            reallyNothing.allMustMatch = true;
            reallyNothing.matchHandCat.add(HandCategory.HIGH_CARD);
            reallyNothing.matchNegHandCat.add(HandCategory.FLUSH_DRAW);
            reallyNothing.matchNegHandCat.add(HandCategory.STRAIGHT_DRAW_2);
            reallyNothing.mustHave.add(TextureCategory.UNPAIRED_BOARD);
            unPairedBoardCriteres.add(reallyNothing);
            
        Criteria meTwoPairFlop = new Criteria(round, roundStr + " 2 pair");
        meTwoPairFlop.mustHave.add(TextureCategory.UNPAIRED_BOARD);
        meTwoPairFlop.matchHandCat.add(HandCategory.TWO_PAIR_USING_BOTH);
        meTwoPairFlop.heroOnly = true;
        
        Criteria anyTwoPairFlop = new Criteria(round, roundStr +" Anyone 2 pair on unpaired board");
        anyTwoPairFlop.mustHave.add(TextureCategory.UNPAIRED_BOARD);
        anyTwoPairFlop.matchHandCat.add( HandCategory.TWO_PAIR_USING_BOTH );
        
        unPairedBoardCriteres.add(meTwoPairFlop);
        unPairedBoardCriteres.add(anyTwoPairFlop);
        
        Criteria any0PairCrit = new Criteria(round, roundStr + " Anyone top/over pair");
        any0PairCrit.mustHave.add(TextureCategory.UNPAIRED_BOARD);
        any0PairCrit.mustNotHave.add(TextureCategory.SAME_SUIT_5);
        any0PairCrit.mustNotHave.add(TextureCategory.SAME_SUIT_4);
        any0PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        
        Criteria any1PairCrit = new Criteria(round, roundStr + " Anyone pair 1 overcard");
        any1PairCrit.mustHave.add(TextureCategory.UNPAIRED_BOARD);
        any1PairCrit.mustNotHave.add(TextureCategory.SAME_SUIT_5);
        any1PairCrit.mustNotHave.add(TextureCategory.SAME_SUIT_4);
        any1PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        any1PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        
        Criteria any2PairCrit = new Criteria(round, roundStr +" Anyone pair 2 overcards");
        any2PairCrit.mustHave.add(TextureCategory.UNPAIRED_BOARD);
        any2PairCrit.mustNotHave.add(TextureCategory.SAME_SUIT_5);
        any2PairCrit.mustNotHave.add(TextureCategory.SAME_SUIT_4);
        any2PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        any2PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        any2PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        
        Criteria any3PairCrit = new Criteria(round, roundStr + " Anyone pair 3 overcards");
        any3PairCrit.mustHave.add(TextureCategory.UNPAIRED_BOARD);
        any3PairCrit.mustNotHave.add(TextureCategory.SAME_SUIT_5);
        any3PairCrit.mustNotHave.add(TextureCategory.SAME_SUIT_4);
        any3PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_0);
        any3PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_1);
        any3PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_2);
        any3PairCrit.matchHandCat.add( HandCategory.PAIR_OVERCARDS_3);
        
        unPairedBoardCriteres.add(any0PairCrit);
        unPairedBoardCriteres.add(any1PairCrit);
        unPairedBoardCriteres.add(any2PairCrit);
        unPairedBoardCriteres.add(any3PairCrit);
        
        Criteria tripsCrit = new Criteria(round, roundStr + " Anyone Trips ");

        tripsCrit.matchHandCat.add( HandCategory.SET_USING_BOTH );
        tripsCrit.matchHandCat.add( HandCategory.SET_USING_ONE );
        
        allBoardCriteres.add(tripsCrit);
        
        Criteria straightCrit = new Criteria(round, roundStr + " Anyone Straight");

        straightCrit.matchHandCat.add( HandCategory.STRAIGHT );
        
        allBoardCriteres.add(straightCrit);
        
        Criteria flushCrit = new Criteria(round, roundStr + " Anyone Flush");

        flushCrit.matchHandCat.add( HandCategory.FLUSH );
        
        allBoardCriteres.add(flushCrit);
        
        Criteria fullHouseOrBetter = new Criteria(round, roundStr + " Anyone Full house or better");

        fullHouseOrBetter.matchHandCat.add( HandCategory.FULL_HOUSE );
        fullHouseOrBetter.matchHandCat.add( HandCategory.QUADS );
        fullHouseOrBetter.matchHandCat.add( HandCategory.STRAIGHT_FLUSH);
        
        allBoardCriteres.add(fullHouseOrBetter);
        
        criteres.addAll(pairedBoardCriteres);
        criteres.addAll(unPairedBoardCriteres);
        criteres.addAll(allBoardCriteres);
        
        }
        
                
        long startTime = System.currentTimeMillis();
        
        for(int simulNum = 0; simulNum < TOTAL_SIMULATIONS; ++simulNum) {
            CompleteEvaluation[] evals = simulateOneRound(false, listRanges);
            
          //Does not match the ranges
            if (evals == null)
            {
                continue;
            }
            
            for(Criteria c : criteres) {
                c.calculate(evals);
            }
            
            tree.addCompleteEvaluation(evals[0]);
            
            ++actualRounds;
            
            
            
                        
            for(int p = 0; p < numPlayers; ++p) {
                equity[p] += evals[p].realEquity;
                
                
                //log.debug("eq {} player {}", evals[p].realEquity, p);
            }
                        
            if (simulNum >= 0 && simulNum % 5000 == 0) {
                logOutput.debug("# of simulations {} of {}", simulNum, TOTAL_SIMULATIONS );
                                
                logOutput.debug("\nPaired board criteria\n");
                for(Criteria c : pairedBoardCriteres) {
                    c.printMsg();
                }
                logOutput.debug("\nUn paired board criteria\n");
                for(Criteria c : unPairedBoardCriteres) {
                    c.printMsg();
                }
                logOutput.debug("\nAll board criteria\n");
                for(Criteria c : allBoardCriteres) {
                    c.printMsg();
                }
                
                logOutput.info("{} valid rounds", actualRounds);
                for(int p = 0; p < numPlayers; ++p) {
                                       
                    logOutput.info("Players {} {}", p, (equity[p] /actualRounds) * 100.0);
                }
            }
        }
        
        if (actualRounds == 0) {
            log.warn("none valid");
        }
        
        log.info("{} valid rounds", actualRounds);
        
        long endTime = System.currentTimeMillis();
        
        for(int p = 0; p < numPlayers; ++p) {
            equity[p] /= actualRounds;
            
            log.info("Players {} range {} =  {}", p, listRanges[p], equity[p] * 100);
        }
        
        logOutput.info("Seconds passed {}", (endTime-startTime) / 1000.0);
        
        return tree;
        
    }
    
    private static int[] availableCards = new int[52];
    private static Card[] flopTurnRiver = new Card[52];
    private static int[] availableHoleCards = new int[1326];    
    
    /**
     * Choisir au hasard deux cartes dans un éventail
     * 
     * @param usedCards si les cartes ont été utilisées  
     * @param range l'eventail de cartes possible
     * @return null si rien trouvé
     */
    private static HoleCards chooseValidAvailableCard(boolean[] usedCards, HoleCardsRange range) 
    {
        int numAvail = 0;
        log.debug("Choosing range {} ", range);
        
        for(int i = 0; i < range.getCardsList().size(); ++i)
        {
            HoleCards hc = range.getCardsList().get(i);
            if (!usedCards[hc.getCards()[0].index]
                   && !usedCards[hc.getCards()[1].index]) 
            {
                availableHoleCards[numAvail++] = i;
               // log.debug("{} available in {} ", hc, range);
            } else {
               // log.debug("{} not available in {} ", hc, range);
            }
            
        }
        
        if (numAvail == 0) 
            return null;
        
        int choice = (int) (Math.random() * numAvail);
        
        return range.getCardsList().get( availableHoleCards[choice] );
        
    }
    
    
    /**
     * Choiser le flop, turn, et river parmi les cartes disponibles
     * @param usedCards
     * @return
     */
    private static void chooseRemainingCards(boolean[] usedCards) {
        
        int numAvail = 0;
        
        for(int i = 0; i < 52; ++i) 
        {
            if (!usedCards[i] ) {
                availableCards[numAvail++] = i;
            }
        }
        
        
        for (int deckIdx = 0; deckIdx < 5; ++deckIdx) {
            // int from remainder of deck
            int r = deckIdx + (int) (Math.random() * (numAvail - deckIdx));
            int swap = availableCards[r];
            availableCards[r] = availableCards[deckIdx];
            availableCards[deckIdx] = swap;
            
            flopTurnRiver[deckIdx] = Card.listByIndex[availableCards[deckIdx]];
            
        }
        
        
    }
            
    
    //private static final int NUM_CARDS = 52;
    
    private static CompleteEvaluation[] simulateOneRound(boolean heroOnly, HoleCardsRange[] listRanges) {
        
        //final int numPlayers = listRanges.length;
        
        
        
        //See if the shuffle matches our ranges
        HoleCards[] holeCards = new HoleCards[listRanges.length];
        
        boolean[] usedCards = new boolean[52]; 
        
        
        
        
        for(int i = 0; i < listRanges.length; ++i) {
            
            HoleCardsRange range = listRanges[i];
            
            //"Shuffle" only what is needed, the next 2 cards
            
            //0  0 1
            //1  2 3
            //2  4 5
            //3  6 7
            //x  2x 2x+1
            
            /*
             * List of holeCards, need to choose among those still left
             */
            
            HoleCards hc = chooseValidAvailableCard(usedCards, range);
            
            if (hc == null) {
                log.debug("No hole cards found for range {}  " +
                		"prev holeCards {}", range, holeCards );
                return null;
            }
            
            usedCards[ hc.getCards()[0].index ] = true;
            usedCards[ hc.getCards()[1].index ] = true;
            holeCards[i] = hc;
            
            //log.debug("Hole cards ({}) {} idx 1 {} idx 2 {}", i, holeCards[i], card1Index, card2Index);
        }
        
        //Everything matches evaluate
        
        //Pick the 5 remaining cards for the flop, turn, and river
        chooseRemainingCards(usedCards);
        
        CompleteEvaluation[] evals = EvalHands.evaluate(heroOnly, holeCards, flopTurnRiver); 
                
        
        /*
        if (evals[1].won == true) {
            
            log.debug("\nh1 {} eval: {}  \n h2  {} eval: {}  \n Flop {} turn {} river {}", 
                    holeCards[0], evals[0].toString(),
                    holeCards[1], evals[1].toString(),
                    flop, 
                    Card.listByIndex[ flopTurnRiver[3] ], Card.listByIndex[ flopTurnRiver[4] ]);
        }*/
        return evals;
    }
}

