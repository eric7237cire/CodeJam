package codejam.y2009.round_4.year_more;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.y2009.round_4.year_more.Main.Tournament;

import com.google.common.math.LongMath;

public class RealSolution {
 final static Logger log = LoggerFactory.getLogger(Happiness.class);
    
    long denom;
    long numerator;
    
    List<Tournament> tournaments;
    
    
    int wholeNumber = 0;
    
    int blockSize;
    int maxTournamentCount = 0;
    int maxTournamentSize;
    
    int [][] tournamentRoundCountsPerDay;
    /*
     * Yj is an indicator 1 if a round is on that day, 0 if not
     * 
     *   Ε(X2) = Ε((∑1≤j≤T Yj)2) = ∑1≤j≤T Ε(Yj2) + 2 ∑1≤j<k≤T Ε(YjYk).
     *   
     *   Yj^2 is P(j)
     */
    public RealSolution(int blockSize, int maxTournamentCount, int maxTournamentSize) {
        this.blockSize = blockSize;
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
        log.info("Adding t ");
        
        tournamentRoundCountsPerDay = new int[tournaments.size()][maxTournamentSize];
        
        
        //a + b + c + d
        long[][] runningRoundCount = new long[tournaments.size()][maxTournamentSize];
        
        //b*a + c (a + b) + d ( a+b+c )
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

                    tournamentRoundCountsPerDay[tNum][round + startDay]++;
                }

                if (tNum == 0) {
                    runningRoundCountMult[tNum][startDay] = 0;
                    runningRoundCount[tNum][startDay] = tournamentRoundCountsPerDay[tNum][startDay];
                } else {
                    runningRoundCountMult[tNum][startDay] = runningRoundCount[tNum - 1][startDay]
                            * tournamentRoundCountsPerDay[tNum][startDay];
                    runningRoundCount[tNum][startDay] = runningRoundCount[tNum - 1][startDay]
                            + tournamentRoundCountsPerDay[tNum][startDay];

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
        
     //   long[] runningSum = new long[maxTournamentSize];
        long term1 = 0; //denom is maxTournSize 
       
        
        for(int day = 0; day < maxTournamentSize; ++day) {
            term1 = LongMath.checkedAdd(term1, 
            runningRoundCount[tournaments.size()-1][day]);                  
        }
                
        int rest = blockSize - maxTournamentSize;
        
        term1 = LongMath.checkedAdd(term1,
                LongMath.checkedMultiply(rest, 
                        runningRoundCount[tournaments.size()-1][maxTournamentSize-1])); 
                
        long wn = term1 / blockSize;
        term1 -= LongMath.checkedMultiply(wn, blockSize);
        
        wholeNumber += wn;
                
        term2 = LongMath.checkedAdd(term2, LongMath.checkedMultiply(rest, lastDayTerm2));

        denom = LongMath.checkedMultiply(blockSize, blockSize);
       
        numerator = LongMath.checkedAdd(LongMath.checkedMultiply(term1, blockSize), term2);
    }
    
    public long getNumerator() {
        return numerator;
    }
}
