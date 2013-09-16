package pkr;

import generated.Simul;
import generated.Simul.Scenarios.Scenario;
import generated.Simul.Scenarios.Scenario.Player;
import generated.Simul.Sets.Set;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pkr.possTree.Tree;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

public class Simulator {
    
    private static Logger log = LoggerFactory.getLogger(Simulator.class);
    private static Logger logOutput = LoggerFactory.getLogger("mainOutput");
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        
        
       // playerHoleCards.add("AA, KK, QQ, JJ, TT, 99, 88, 77, 66, 55, 44, 33, 22, AK, AQ, AJ");
        //playerHoleCards.add("AA, AKs, 27, 93, 44, 99");
      //playerHoleCards.add("KTs");
      
    //  playerHoleCards.add("22");
    //  playerHoleCards.add("AKs");
       // playerHoleCards.add("T8");
    //  playerHoleCards.add("Q2s+, J2+, T2+, 32+");
     // playerHoleCards.add("A2o, A3o, A4o, A5o");
        
        //playerHoleCards.add("K8o, K9o, KTo");
        
        //playerHoleCards.add("AA");
        //playerHoleCards.add("KJo");
      //  playerHoleCards.add("AKo");
      //  playerHoleCards.add("JTs");
       // playerHoleCards.add("A4o");
        
       // playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+, 22+");
       // playerHoleCards.add("A2+, K2+, Q2+, J2+, T2+, 92+, 82+, 72+, 62+, 52+, 42+, 32+, 22+");
        
        List<String> groups = Lists.newArrayList();

        Simul config = null;
        try {
            // setup object mapper using the AppConfig class
            JAXBContext context = JAXBContext.newInstance(Simul.class);
            // parse the XML and return an instance of the AppConfig class
            config = (Simul) context.createUnmarshaller().unmarshal(
                    Resources.getResource("input.xml"));
          } catch(JAXBException e) {
            // if things went wrong...
            log.error("error parsing xml: ", e);
            System.exit(1);
          }
        
        Map<String, String> sets = Maps.newHashMap();
        for ( Set set : config.getSets().getSet())
        {
            log.debug("Adding {} {}", set.getName(), set.getValue().trim());
            sets.put(set.getName(), set.getValue().trim());
        }
        
        for( Scenario scenario : config.getScenarios().getScenario())
        {
            List<String> playerHoleCards = Lists.newArrayList();
            
            String fileName = "C:\\codejam\\CodeJam\\simul\\output\\" + scenario.getName() + ".xml";
            
            for(Player player : scenario.getPlayer())
            {
                int count = player.getCount().intValue();
                
                String range = null;
                String setName = player.getSet();
                if (setName != null && sets.containsKey(setName))
                {
                    range = sets.get(setName);
                } else {
                    range = player.getValue().trim();
                    Preconditions.checkState(range.length() > 0);
                }
                
                for(int i = 0; i < count; ++i)
                {
                    playerHoleCards.add(range);
                }
            }
            
            int iters = scenario.getIterCount().intValue();
            
            logOutput.debug("\n\n************************************************************************");
            logOutput.debug("*  {}  ** rounds : {}", scenario.getName(), iters);
            logOutput.debug("************************************************************************");
            Tree tree = simulate(iters, scenario.getFlop(), playerHoleCards);
            tree.output(fileName);
        }

        
        
    }
    
    public static interface iCriteria
    {
        public boolean matches(CompleteEvaluation[] evals);
        public boolean isApplicable(CompleteEvaluation eval);
    }
        
   
    
    
    
    
    public static Tree simulate(int TOTAL_SIMULATIONS, String preChosenFlop, List<String> playerHoleCards) {
        
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
        
                        
            Criteria.addUnpairedBoardCriteria(round, roundStr, unPairedBoardCriteres);
            Criteria.addPairedBoardCriteria(round, roundStr, pairedBoardCriteres);

            Criteria.addAnyBoardCriteria(round, roundStr, allBoardCriteres);

           
            
        }
        
        criteres.addAll(pairedBoardCriteres);
        criteres.addAll(unPairedBoardCriteres);
       criteres.addAll(allBoardCriteres);
                
        long startTime = System.currentTimeMillis();
        
        for(int simulNum = 0; simulNum < TOTAL_SIMULATIONS; ++simulNum) {
            CompleteEvaluation[] evals = simulateOneRound(false, preChosenFlop, listRanges);
            
          //Does not match the ranges
            if (evals == null)
            {
                continue;
            }
            
            for(Criteria c : criteres) {
                log.debug("Calculate criteria {}", c.desc);
                c.calculate(evals);
            }
            
            tree.addCompleteEvaluation(evals[0]);
            
            ++actualRounds;
            
            
            
                        
            for(int p = 0; p < numPlayers; ++p) {
                equity[p] += evals[p].realEquity;
                
                
                //log.debug("eq {} player {}", evals[p].realEquity, p);
            }
                        
            if (simulNum >= 0 && simulNum % 5000 == 0) {
                
            }
        }
        
        if (actualRounds == 0) {
            log.warn("none valid");
        }
        
      //  logOutput.debug("# of simulations {} of {}", simulNum, TOTAL_SIMULATIONS );
        
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
     * et mettres les résultats dans flopTurnRiver
     * @param usedCards
     * @return
     */
    private static void chooseRemainingCards(int nAlreadyChosen, int nToChoose, boolean[] usedCards) {
        
        int numAvail = 0;
        
        for(int i = 0; i < 52; ++i) 
        {
            if (!usedCards[i] ) {
                availableCards[numAvail++] = i;
            }
        }
        
        
        for (int deckIdx = 0; deckIdx < (nToChoose-nAlreadyChosen); ++deckIdx) {
            // int from remainder of deck
            int r = deckIdx + (int) (Math.random() * (numAvail - deckIdx));
            int swap = availableCards[r];
            availableCards[r] = availableCards[deckIdx];
            availableCards[deckIdx] = swap;
            
            flopTurnRiver[deckIdx+nAlreadyChosen] = Card.listByIndex[availableCards[deckIdx]];
            
        }
        
        
    }
            
    
    //private static final int NUM_CARDS = 52;
    
    private static CompleteEvaluation[] simulateOneRound(boolean heroOnly, String flop, HoleCardsRange[] listRanges) 
    {
        
        //See if the shuffle matches our ranges
        HoleCards[] holeCards = new HoleCards[listRanges.length];
        
        boolean[] usedCards = new boolean[52]; 
        
        Card[] flopPreChosen = Card.parseCards(flop);
        Preconditions.checkState(flopPreChosen.length <= 5);
        for(int i = 0; i < flopPreChosen.length; ++i)
        {
            usedCards[flopPreChosen[i].getIndex()]=true;
            flopTurnRiver[i] = flopPreChosen[i];
        }
        
        
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
        chooseRemainingCards(flopPreChosen.length, 5, usedCards);
        
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

