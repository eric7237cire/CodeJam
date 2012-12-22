package codejam.y2009.round_4.year_more;

import java.util.List;

import org.apache.commons.math3.fraction.BigFraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.y2009.round_4.year_more.Main.Tournament;

import com.google.common.math.LongMath;

public class RealSolution {
 final static Logger log = LoggerFactory.getLogger(Happiness.class);
    
    
    List<Tournament> tournaments;
    
    
    BigFraction expectedValue;
    
    int N;
    int maxTournamentCount = 0;
    int maxTournamentSize;
    
    int [][] D;
    /*
     * Yj is an indicator 1 if a round is on that day, 0 if not
     * 
     *   Ε(X2) = Ε((∑1≤j≤T Yj)2) = ∑1≤j≤T Ε(Yj2) + 2 ∑1≤j<k≤T Ε(YjYk).
     *   
     *   Yj^2 is P(j)
     */
    public RealSolution(int blockSize, int maxTournamentCount, int maxTournamentSize) {
        this.N = blockSize;
        this.maxTournamentCount = maxTournamentCount;
        this.maxTournamentSize = maxTournamentSize;
        
        if (blockSize < maxTournamentSize) {
            this.maxTournamentSize = blockSize;
        }
        
    }
    
    public static RealSolution create(int blockSize, int maxTournamentCount, int maxTournamentSize, List<Tournament> list) {
        RealSolution h = new RealSolution(blockSize, maxTournamentCount, maxTournamentSize);
        
        h.tournaments = list;
        h.calculate();
        return h;
    }
    
    private void calculate() {
        
        D = new int[tournaments.size()][maxTournamentSize];
        
        
        /**
         * In the stared formula, we need to sum expected value that tournament Y
         * has a contest on day i.  The denominator is N.
         * 
         * D(i, j)
         */
        long[][] S1 = new long[tournaments.size()][maxTournamentSize];
        
        //b*a + c (a + b) + d ( a+b+c )
        /**
         * Used to calculate D(i,j) * D(i, k) for day i and tournaments j and k : 0<=j<k
         * 
         *   Lets say we have D(i, k) values
         *   t0: 1 2 3 3
         *   t1: 1 1 2 3
         *   t2: 1 2 3 4
         *   
         *   the D(i,j) tallys (S1) are then
         *   
         *   t0: 1 2 3 3
         *   t1: 2 3 5 6
         *   t2: 3 5 8 10
         *   
         *   Thus for day 0, the expected value ∑1≤j≤T Ε(Yj2) =  3 / N  (Yj2 is just Prob tourn j has round on day 0)
         *   
         *   to compute this second part, first observe that for t0, it is always 0, since there is no k
         *   
         *   t0: 0 0  0  0
         *   t1: 1 2  6  9      = S1(i, t0) * D(i, t1)
         *   t2: 2 6 15 24      = S1(i, t1) * D(i, t2)  =  ( D(i, t0) + D(i, t1) ) * D(i, t2)  = D(i, t0)*D(i,t2) + D(i, t1)*D(i,t2)
         *   
         *    which is what we want, for all other tournaments, the probability that both tournament j and
         *    tournament k has a round on day i for 0 <= j < k < T
         */
        long[][] runningRoundCountMult = new long[tournaments.size()][maxTournamentSize];
        
        long term2 = 0;
        
        //In order to add from maxTournamentSize to blocksize
        long lastDayTerm2 = 0;
        
        for (int tNum = 0; tNum < tournaments.size(); ++tNum) {

            Tournament t = tournaments.get(tNum);

            // First calc probabilities of tournament
            for (int startDay = 0; startDay < maxTournamentSize; ++startDay) {
                for (int round : t.roundDays) {
                    if (startDay + round >= maxTournamentSize) {
                        break;
                    }

                    D[tNum][round + startDay]++;
                }

                if (tNum == 0) {
                    runningRoundCountMult[tNum][startDay] = 0;
                    S1[tNum][startDay] = D[tNum][startDay];
                } else {
                    runningRoundCountMult[tNum][startDay] = S1[tNum - 1][startDay]
                            * D[tNum][startDay];
                    S1[tNum][startDay] = S1[tNum - 1][startDay]
                            + D[tNum][startDay];

                    term2 = LongMath.checkedAdd(term2, LongMath
                            .checkedMultiply(2,
                                    runningRoundCountMult[tNum][startDay]));

                    if (startDay == maxTournamentSize - 1) {
                        lastDayTerm2 = LongMath.checkedAdd(lastDayTerm2,
                                LongMath.checkedMultiply(2,
                                        runningRoundCountMult[tNum][startDay]));
                    }
                }
            }            
            
        }
        
        long term1 = 0;  
               
        for(int day = 0; day < maxTournamentSize; ++day) {
            term1 = LongMath.checkedAdd(term1, 
            S1[tournaments.size()-1][day]);                  
        }
                
        int rest = N - maxTournamentSize;
        
        term1 = LongMath.checkedAdd(term1,
                LongMath.checkedMultiply(rest, 
                        S1[tournaments.size()-1][maxTournamentSize-1])); 
                
        BigFraction ret = new BigFraction(term1, (long)N);
        
                
        term2 = LongMath.checkedAdd(term2, LongMath.checkedMultiply(rest, lastDayTerm2));

        ret = ret.add(new BigFraction(term2, LongMath.checkedMultiply(N, N)));
    
        expectedValue = ret;
    }
    
    public BigFraction getEv() {
        return expectedValue;
    }
}
