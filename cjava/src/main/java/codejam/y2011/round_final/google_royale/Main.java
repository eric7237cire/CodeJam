package codejam.y2011.round_final.google_royale;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main() {
        super("E", 0, 0);
    }
    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        in.A = scanner.nextLong();
        in.M = scanner.nextLong();
        in.V = scanner.nextLong();
        
        return in;
    }
    
    /**
     * We just keep double or nothing starting with 
     * the initial bet
     */
    public void testObservation2() {
        //Say A = 95, V = 100, M = 50 and we bet 5
        
        int A = 50;
        int V = 100;
        int M = 100;
        int initialBet = 10;
        
        int simulations = 10000;
        
        int hitVCount = 0;
        int loseCount = 0;
        int moneyIfLost = 0;
        int totalRounds = 0;
        
        Random r = new Random();
        
        
        for(int simul = 0; simul < simulations; ++simul)
        {
            int bet = initialBet;
            int money = A;
        
            ++totalRounds;
            
            while(money >= initialBet)
            {
                while(bet <= M )
                {
                    boolean won = r.nextBoolean();
                    if (won) {
                        money += bet;
                        break;
                    } else {
                        money -= bet;
                    }
                    
                    bet*=2;                
                }
                
                if (money >= V) {
//                    log.debug("Won simul {} money {}", simul, money);
                    break;
                }
  //              log.debug("Simul {} money {}", simul, money);
                bet = initialBet;
            }
            
            if (money >= V) {
                ++hitVCount;
            } else {
                moneyIfLost += money;
                ++loseCount;
            }
        }
        
        double expectedMoneyIfLose = moneyIfLost * 1.0d / loseCount; 
        log.debug("Won {}, lost {}, expected lose money {}  expected value {}",
                100.0 * hitVCount / totalRounds,
                100.0 * loseCount / totalRounds,
                moneyIfLost * 1.0d / loseCount,
                1.0d * hitVCount / totalRounds * V +
                1.0d * loseCount / totalRounds * expectedMoneyIfLose
                );
    }
    
    /**
     * Bet 1 until we win or have only y dollars left
     */
    public void testObservation3_case2() {
        //Say A = 95, V = 100, M = 50 and we bet 5
        
        int A = 50;
        int V = 100;
        int y = 10;
        int M = 100;
        
        int simulations = 10000;
        
        //"Strategy is to bet 5 and keep doubling
        int hitVCount = 0;
        int loseCount = 0;
        int moneyIfLost = 0;
        int totalRounds = 0;
        
        Random r = new Random();
        
       
        for(int simul = 0; simul < simulations; ++simul)
        {
            int money = A;
                        
            ++totalRounds;
            
            
            while(money > 0 && money < V)
            {
                boolean won = r.nextBoolean();
                if (won) {
                    money += 1;
                } else {
                    money -= 1;
                }
                
                if (money == V) {
                   //++hitVCount;
                    break;
                }
                
                if (money == y) {
                                        
                    int bet = y;
                    while(bet <= M )
                    {
                        won = r.nextBoolean();
                        if (won) {
                            money += bet;
                            break;
                        }
                        
                        money -= bet;
                        bet*=2;                        
                    }
                    
                }
                //Mabye check for money == v ?
            }
            
            if (money >= V) {
                ++hitVCount;
            } else {
                moneyIfLost += money;
                ++loseCount;
            }
        }
        
        double expectedMoneyIfLose = moneyIfLost * 1.0d / loseCount; 
        log.debug("Won {}, lost {}, expected lose money {}  expected value {}",
                100.0 * hitVCount / totalRounds,
                100.0 * loseCount / totalRounds,
                moneyIfLost * 1.0d / loseCount,
                1.0d * hitVCount / totalRounds * V +
                1.0d * loseCount / totalRounds * expectedMoneyIfLose
                );
    }
    
    public void testOptimalBetting(InputData in) {
        //Say A = 95, V = 100, M = 50 and we bet 5
        
        long A = in.A;
        long V = in.V;
        
        long M = in.M;
        
        int simulations = 10000;
        
        List<AllInPoint> allInPoints = calculateStrictAllInPoints(in.M);
        
        //"Strategy is to bet 5 and keep doubling
        int hitVCount = 0;
        int loseCount = 0;
        
        int moneyIfLost = 0;
        int totalRounds = 0;
        
        Random r = new Random();
               
        for(int simul = 0; simul < simulations; ++simul)
        {
            long money = A;
                        
            ++totalRounds;
                        
            while(money > 0 && money < V)
            {
                
                
                //First either all in until lose or bet 1
                
                boolean allIn = false;
                final long searchMoney = money;
                if (Iterables.any(allInPoints, new Predicate<AllInPoint>() {

                    @Override
                    public boolean apply(AllInPoint input)
                    {
                        return searchMoney == input.moneyAmt;
                    }
                }))
                {
                    allIn = true;
                }
                
                if (!allIn) {
                    boolean won = r.nextBoolean();
                    if (won) {
                        money += 1;
                    } else {
                        money -= 1;
                    }
                } else {
                                                        
                    long bet = money;
                    while(bet <= M )
                    {
                        boolean won = r.nextBoolean();
                        if (won) {
                            money += bet;
                            break;
                        }
                        
                        money -= bet;
                        bet*=2;                        
                    }                    
                }

            }
            
            if (money >= V) {
                ++hitVCount;
            } else {
                moneyIfLost += money;
                ++loseCount;
            }
        }
        
        double expectedMoneyIfLose = moneyIfLost * 1.0d / loseCount; 
        log.debug("Won {}, lost {}, expected lose money {}  expected value {}",
                100.0 * hitVCount / totalRounds,
                100.0 * loseCount / totalRounds,
                moneyIfLost * 1.0d / loseCount,
                1.0d * hitVCount / totalRounds * V +
                1.0d * loseCount / totalRounds * expectedMoneyIfLose
                );
    }
    
    class AllInPoint
    {
        long moneyAmt;
        
        /**
         * How much money will be left if the all in does not work.
         * 
         * Equal to 
         * moneyAmt - moneyAmt - 2 * moneyAmt - 4 * moneyAmt...
         * 
         */
        long L;
        
        /**
         * Probability that the all in will succeed
         */
        double pWinBettingRound;
        
        /**
         * Probability of winning / getting to V
         */
        double P;

        public AllInPoint(long moneyAmt, long l) {
            super();
            this.moneyAmt = moneyAmt;
            L = l;
            
            /**
             * Derived from Observation 2, but instead of V we use 2x the money (what we have when we win)
             */
            this.pWinBettingRound = 1.0d * (moneyAmt - L) / (2*moneyAmt - L);
        }

        @Override
        public String toString()
        {
            return "AllInPoint [moneyAmt=" + moneyAmt + ", L=" + L + ", p=" + pWinBettingRound + ", pWinToV=" + P + "]";
        }
        
        
    }
    public List<AllInPoint> calculateStrictAllInPoints(long M) {
        
        long lowestLYet = Long.MAX_VALUE;
        List<AllInPoint> allInPoints = Lists.newArrayList();
        
        for(int i = 55; i >= 0; --i) {
            
            //2 ^ i
            long p2 = 1L << i;
            
            if (p2 > M)
                continue;
            
            /**
             * The inflection point -- observation 4
             */
            //Greatest Mi such that Mi* 2^i <= M
            //  Mi <= M / 2 ^ i
            long Mi = M / p2;
            
            
            long L = 0;
            
            if (i >= 1) {
                L = -2 * Mi * ( (p2 >> 0L) - 1);
            }
            
            if (L < lowestLYet) {
                lowestLYet = L;
                allInPoints.add(new AllInPoint(Mi, L));
            }
            
            log.debug("Mi {} * 2^i:{} ({}) <= {} ; L {}", Mi, i, p2, M, L);
        }
        
        return allInPoints;
    }

    public void calculateAllInPointProbability(List<AllInPoint> allInPoints, InputData in) {
        
        /**
         * Start off with final point
         */
        double lastApProb = 1.0;
        long lastApMoneyAmt = in.V;
        
        for(int apIdx = allInPoints.size() - 1; apIdx >= 0; --apIdx) {
            AllInPoint ap = allInPoints.get(apIdx);
            
            //Set up variables, see tex doc
            
            double Pk = lastApProb;
            long j = 2 * ap.moneyAmt;
            long i = ap.moneyAmt;
            long k = lastApMoneyAmt;
            double p = ap.pWinBettingRound;
            
            double Pj = Pk * (j-i) / ( (k-i) + p * (j - k));
            double Pi = p * Pj;
            
            ap.P = Pi;
            lastApMoneyAmt = ap.moneyAmt;
            lastApProb = Pi;
        }
    }
    
    public String handleCase(InputData in) {

        List<AllInPoint> allInPoints = calculateStrictAllInPoints(in.M);
        
        calculateAllInPointProbability(allInPoints, in);
        
        for(AllInPoint ap : allInPoints) {
            log.debug("ALl in point {}", ap);
        }
        testOptimalBetting(in);
//        testObservation2();
        
      //  testObservation3_case2();
        
      //  calculateStrictAllInPoints(100);
        
        return String.format("Case #%d: ", in.testCase);        
    }

}
