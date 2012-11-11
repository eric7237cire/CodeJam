package com.eric.codejam;

import java.math.BigInteger;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.Main.Tournament;
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
        
        for (int tNum = 0; tNum < tournaments.size(); ++tNum) {

            Tournament t = tournaments.get(tNum);

            // First calc probabilities of tournament
            for (int start = 0; start < maxTournamentSize; ++start) {
                for (int round : t.roundDays) {
                    if (start + round >= maxTournamentSize) {
                        break;
                    }

                    tournamentRoundCountsPerDay[tNum][round + start]++;
                }
            }
        }
        
     //   long[] runningSum = new long[maxTournamentSize];
        long term1 = 0; //denom is maxTournSize 
        long term2 = 0; //denom is maxTournSize^2
        
        for(int day = 0; day < maxTournamentSize; ++day) {
            for (int tNum = 0; tNum < tournaments.size(); ++tNum) {
                term1 = LongMath.checkedAdd(term1, tournamentRoundCountsPerDay[tNum][day]); 
            }
        }
        
        long lastDaySum = 0;
        for(int day = 0; day < maxTournamentSize; ++day) {
            for (int i = 0; i < tournaments.size(); ++i) {
                for (int j = i+1; j < tournaments.size(); ++j) {
                    term2 = 
                            LongMath.checkedAdd(term2, 
                                    LongMath.checkedMultiply(2, 
                                            LongMath.checkedMultiply(tournamentRoundCountsPerDay[i][day], tournamentRoundCountsPerDay[j][day])
                                            ));
                    
                    if (day == maxTournamentSize -1 ) {
                        lastDaySum = LongMath.checkedAdd(lastDaySum, LongMath.checkedMultiply(2, 
                                            LongMath.checkedMultiply(tournamentRoundCountsPerDay[i][day], tournamentRoundCountsPerDay[j][day])));
                    }
                }
            }
        }
        
        int rest = blockSize - maxTournamentSize;
        
        for (int tNum = 0; tNum < tournaments.size(); ++tNum) {
            term1 = LongMath.checkedAdd(term1,LongMath.checkedMultiply(rest, tournamentRoundCountsPerDay[tNum][maxTournamentSize-1])); 
        }
        
        long wn = term1 / blockSize;
        term1 -= LongMath.checkedMultiply(wn, blockSize);
        
        wholeNumber += wn;
                
        term2 = LongMath.checkedAdd(term2, LongMath.checkedMultiply(rest, lastDaySum));

        denom = LongMath.checkedMultiply(blockSize, blockSize);
       
        numerator = LongMath.checkedAdd(LongMath.checkedMultiply(term1, blockSize), term2);
    }
    
    public long getNumerator() {
        return numerator;
    }
}
