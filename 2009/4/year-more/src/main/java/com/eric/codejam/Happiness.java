package com.eric.codejam;

import java.math.BigInteger;
import java.util.List;

import com.eric.codejam.Main.Tournament;

public class Happiness {
    
    /*
     * p[block][#] = numerator of probability
     */
    BigInteger probability[][];
    BigInteger denom;
    
    int currentTournNum = 0;
    int blockSize;
    int maxTournamentCount = 0;
    int maxTournamentSize;
    
    public Happiness(int blockSize, int maxTournamentCount, int maxTournamentSize) {
        this.blockSize = blockSize;
        this.maxTournamentCount = maxTournamentCount;
        this.maxTournamentSize = maxTournamentSize;
        
        if (blockSize < maxTournamentSize) {
            this.maxTournamentSize = blockSize;
        }
        
        /*
         * Max # of rounds on single day == maxTournamentCount
         * 
         * Only store max tournament size because every day after that the probababilities are the same
         */
        probability = new BigInteger[maxTournamentSize][maxTournamentCount+1];  
        for(int i = 0; i < maxTournamentSize; ++i) {
            for(int j = 0; j <= maxTournamentCount; ++j) {
                probability[i][j] = BigInteger.ZERO;
            }
        }
    }
    
    public static Happiness create(int blockSize, int maxTournamentCount, int maxTournamentSize, List<Tournament> list) {
        Happiness h = new Happiness(blockSize, maxTournamentCount, maxTournamentSize);
        
        for(Tournament t : list) {
            h.addTournament(t);
        }
        
        return h;
    }
    
    public void addTournament(Tournament t) {
        //First calc probabilities of tournament
        int[][] tournProb =  new int[maxTournamentSize][2];        
        
        for (int start = 0; start < maxTournamentSize; ++start) {
            for (int round : t.roundDays) {
                if (start + round >= maxTournamentSize) {
                    break;
                }

                tournProb[round + start][1]++;
            }
        }
        
        for(int i = 0; i < maxTournamentSize; ++i) {
            tournProb[i][0] = blockSize - tournProb[i][1];
        }
        
        if (currentTournNum == 0) {
            for(int i = 0; i < maxTournamentSize; ++i) {
                probability[i][0] = BigInteger.valueOf( tournProb[i][0] );
                probability[i][1] = BigInteger.valueOf(tournProb[i][1] );
            }
            denom = BigInteger.valueOf(blockSize);
            currentTournNum ++;
            return;
        }
        
        denom = denom.multiply(BigInteger.valueOf(blockSize));
        currentTournNum ++;
      
        BigInteger[][] newProbability = new BigInteger[maxTournamentSize][maxTournamentCount+1];
        
        for(int i = 0; i < maxTournamentSize; ++i) {
            for(int j = 0; j <= maxTournamentCount; ++j) {
                newProbability[i][j] = BigInteger.ZERO;
            }
        }
        
        for(int i = 0; i < maxTournamentSize; ++i) {
            for(int simulRoundCount = 0; simulRoundCount <= currentTournNum; ++simulRoundCount) {
                if (simulRoundCount == 0) {
                    newProbability[i][0] = probability[i][0].multiply(BigInteger.valueOf(tournProb[i][0]));
                } else 
                if (simulRoundCount == currentTournNum) {
                    newProbability[i][simulRoundCount] = probability[i][simulRoundCount-1].multiply(BigInteger.valueOf(tournProb[i][1])) ;
                } else {
                    newProbability[i][simulRoundCount] = probability[i][simulRoundCount].multiply(BigInteger.valueOf(tournProb[i][0])).add(
                             probability[i][simulRoundCount-1].multiply(BigInteger.valueOf( tournProb[i][1])));
                }                
            }
        }
        
        probability = newProbability;
        
        
        
    }
    
    public BigInteger getNumerator() {
        BigInteger sum = BigInteger.ZERO;
        
        for(int i = 0; i < maxTournamentSize; ++i) {
            for(int roundCount = 1; roundCount <= maxTournamentCount; ++roundCount) {
                sum = sum.add(probability[i][roundCount].multiply(BigInteger.valueOf( roundCount*roundCount )));
            }
        }
        
        int rest = blockSize - maxTournamentSize;
        if (rest > 0) {
            for(int roundCount = 1; roundCount <= maxTournamentCount; ++roundCount) {
                sum = sum.add(BigInteger.valueOf(rest).multiply
                        ( probability[maxTournamentSize-1][roundCount].multiply(BigInteger.valueOf( roundCount*roundCount ))));
            }
        }
            
        
        
        return sum;
    }
}
