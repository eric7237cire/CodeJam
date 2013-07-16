package pkr;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.possTree.PossibilityNode.HandCategory;
import pkr.possTree.Tree;

import com.google.common.collect.Lists;

public class Simulator {
    
    private static Logger log = LoggerFactory.getLogger(Simulator.class);
    private static Logger logOutput = LoggerFactory.getLogger("mainOutput");
    
    public static void main(String[] args) {
        List<String> playerHoleCards = Lists.newArrayList();
        
       // playerHoleCards.add("AA, KK, QQ, JJ, TT, 99, 88, 77, 66, 55, 44, 33, 22, AK, AQ, AJ");
        //playerHoleCards.add("AA, AKs, 27, 93, 44, 99");
      //playerHoleCards.add("KTs");
      
    //  playerHoleCards.add("72o, 32");
     // playerHoleCards.add("27s");
    //  playerHoleCards.add("Q2s+, J2+, T2+, 32+");
      //playerHoleCards.add("Q2s+, J2+, T2+, 32+");
        
        playerHoleCards.add("QQ");
        
        //random
        playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+, 22+");
        
        //3 loose all in calls
        for(int i = 0; i < 1; ++i) {
            playerHoleCards.add("A2+, K2s+, K7o+, Q2s+, Q7o+, J2s+, " +
            		"J7o+, T2s+, T7o+, 92s+, 97o+, 82s+, 85o+, 72s+, " +
            		"76o, 62s+, 65o, 52s+, 54o, 42s+, 43o, 22+");
        }
        
        //2 semi reasonable calls
        for(int i = 0; i < 1; ++i) {
        playerHoleCards.add("A2+, K2s+, K9o+, Q8s+, Q9o+, J7s+, " +
                "J8o+, T8s+, T9o+, 97s+, 98o+, 85s+, 87o+, 75s+, " +
                "76o, 64s+, 65o, 53s+, 54o, 42s+, 43o, 22+");
        }
       // 
        //playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+");
        //playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+");
        
        
        //BUG with this range
        //playerHoleCards.add("JJ, KJ");
       // playerHoleCards.add(HoleCardsRange.SUITED_ACES);
        
        String fileName = "C:\\codejam\\CodeJam\\simul\\out.xml";
        
        Tree tree = simulate(playerHoleCards);
        tree.output(fileName);

       // playerHoleCards.add("JJ, KJo");
       // playerHoleCards.add("27o");
        
       // playerHoleCards.clear(); 
       // playerHoleCards.add("AA");
     //  playerHoleCards.add("AQs");
      // playerHoleCards.add("KJo");
        
        
    }
    public static Tree simulate(List<String> playerHoleCards) {
        
        Tree tree = new Tree();
        
        //Setup the ranges
        HoleCardsRange[] listRanges = new HoleCardsRange[playerHoleCards.size()];
        
        for(int i = 0; i < playerHoleCards.size(); ++i) {
            listRanges[i] = new HoleCardsRange(playerHoleCards.get(i));
        }
        
        int actualRounds = 0;
        
        final int numPlayers = playerHoleCards.size();
        
        double[] equity = new double[numPlayers];
        
        final int TOTAL_SIMULATIONS = 100001;
        int quadCount = 0;
        int lotsOFSets = 0;
        int royals = 0;
        int fullHouse = 0;
        int flops2Pair = 0;
        
        for(int simulNum = 0; simulNum < TOTAL_SIMULATIONS; ++simulNum) {
            CompleteEvaluation[] evals = simulateOneRound(listRanges);
            
          //Does not match the ranges
            if (evals == null)
            {
                continue;
            }
            tree.addCompleteEvaluation(evals[0]);
            
           
            
            
            
            ++actualRounds;
            
            int sets = 0;
            
            for(int p = 0; p < numPlayers; ++p) {
                equity[p] += evals[p].realEquity;
                
                if (p==0) {
                if (evals[p].quads) {
                    ++quadCount;
                }
                
                if (evals[p].getScore().handLevel == HandLevel.TRIPS) {
                    //++sets;
                }
                if (evals[p].getScore().handLevel == HandLevel.STRAIGHT_FLUSH && evals[p].getScore().getKickers()[0] == CardRank.ACE) {
                    ++royals;
                }
                if (evals[p].getScore().handLevel == HandLevel.FULL_HOUSE) {
                    ++fullHouse;
                }
                if (evals[p].hasFlag(CompleteEvaluation.ROUND_FLOP, HandCategory.HIDDEN_TWO_PAIR)) {
                    ++flops2Pair;
                }
                if (evals[p].hasFlag(CompleteEvaluation.ROUND_FLOP, HandCategory.HIDDEN_SET)) {
                    ++sets;
                }
                
                }
                //log.debug("eq {} player {}", evals[p].realEquity, p);
            }
            
            if (sets >= 1)
            {
                lotsOFSets ++;
            }
            
            if (simulNum > 0 && simulNum % 1000 == 0) {
                logOutput.debug("# of simulations {} of {}", simulNum, TOTAL_SIMULATIONS );
                logOutput.debug("# of quads {}.  %{} ", quadCount, 100.0 * quadCount / simulNum);
                logOutput.debug("# of str8 flushes {}.  %{} ", royals, 100.0 * royals / simulNum);
                logOutput.debug("# of flopped sets {}.  %{} ", lotsOFSets, 100.0 * lotsOFSets / simulNum);
                logOutput.debug("# of fullhouse {}.  %{} ", fullHouse, 100.0 * fullHouse / simulNum);
                logOutput.debug("# of flopping 2 pair {}.  %{} ", flops2Pair, 100.0 * flops2Pair / simulNum);
                
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
        
        for(int p = 0; p < numPlayers; ++p) {
            equity[p] /= actualRounds;
            
            log.info("Players {} range {} =  {}", p, listRanges[p], equity[p] * 100);
        }
        
        return tree;
        
    }
    
    private static int[] availableCards = new int[52];
    private static int[] availableHoleCards = new int[1326];    
    
    private static HoleCards chooseValidAvailableCard(boolean[] usedCards, HoleCardsRange range) 
    {
        int numAvail = 0;
        log.debug("Choosing range {} ", range);
        
        for(int i = 0; i < range.getCardsList().size(); ++i)
        {
            HoleCards hc = range.getCardsList().get(i);
            if (!usedCards[hc.getCards()[0].toInt()]
                   && !usedCards[hc.getCards()[1].toInt()]) 
            {
                availableHoleCards[numAvail++] = i;
                log.debug("{} available in {} ", hc, range);
            } else {
                log.debug("{} not available in {} ", hc, range);
            }
            
        }
        
        if (numAvail == 0) 
            return null;
        
        int choice = (int) (Math.random() * numAvail);
        
        return range.getCardsList().get( availableHoleCards[choice] );
        
    }
    
    
    
    private static int[] chooseRemainingCards(boolean[] usedCards) {
        
        int numAvail = 0;
        
        for(int i = 0; i < 52; ++i) 
        {
            if (!usedCards[i] ) {
                availableCards[numAvail++] = i;
            }
        }
        
        int[] remCards = new int[5];
        
        for (int deckIdx = 0; deckIdx < 5; ++deckIdx) {
            // int from remainder of deck
            int r = deckIdx + (int) (Math.random() * (numAvail - deckIdx));
            int swap = availableCards[r];
            availableCards[r] = availableCards[deckIdx];
            availableCards[deckIdx] = swap;
            
            remCards[deckIdx] = availableCards[deckIdx];
        }
        
        return remCards;
        
    }
            
    
    //private static final int NUM_CARDS = 52;
    
    private static CompleteEvaluation[] simulateOneRound(HoleCardsRange[] listRanges) {
        
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
            
            usedCards[ hc.getCards()[0].toInt() ] = true;
            usedCards[ hc.getCards()[1].toInt() ] = true;
            holeCards[i] = hc;
            
            //log.debug("Hole cards ({}) {} idx 1 {} idx 2 {}", i, holeCards[i], card1Index, card2Index);
        }
        
        //Everything matches evaluate
        
        //Pick the 5 remaining cards for the flop, turn, and river
        int[] flopTurnRiver = chooseRemainingCards(usedCards);
        
        Flop flop = new Flop(new Card[] { 
                Card.listByIndex[ flopTurnRiver[0] ],
                Card.listByIndex[ flopTurnRiver[1] ],
                Card.listByIndex[ flopTurnRiver[2] ]
        });
        CompleteEvaluation[] evals = EvalHands.evaluate(true, holeCards, 
                flop,  Card.listByIndex[ flopTurnRiver[3] ], 
        Card.listByIndex[ flopTurnRiver[4] ]);
        
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

