package com.eric.codejam;

import com.eric.codejam.Main.Tournament;

public class Happiness {
    
    /*
     * p[block][#] = numerator of probability
     */
    int probability[][];
    int denom;
    
    int currentTournNum = 0;
    int blockSize;
    int maxTournamentCount = 0;
    
    public Happiness(int blockSize, int maxTournamentCount) {
        this.blockSize = blockSize;
        this.maxTournamentCount = maxTournamentCount;
        
        //Max # of rounds on single day == maxTournamentCount
        probability = new int[blockSize][maxTournamentCount+1];        
    }
    
    public void addTournament(Tournament t) {
        //First calc probabilities of tournament
        int[][] tournProb =  new int[blockSize][2];        
        
        for (int start = 0; start < blockSize; ++start) {
            for (int round : t.roundDays) {
                if (start + round >= blockSize) {
                    break;
                }

                tournProb[round + start][1]++;
            }
        }
        
        for(int i = 0; i < blockSize; ++i) {
            tournProb[i][0] = blockSize - tournProb[i][1];
        }
        
        if (currentTournNum == 0) {
            for(int i = 0; i < blockSize; ++i) {
                probability[i][0] = tournProb[i][0];
                probability[i][1] = tournProb[i][1];
            }
            denom = blockSize;
            currentTournNum ++;
            return;
        }
        
        currentTournNum ++;
      
        int[][] newProbability = new int[blockSize][maxTournamentCount+1];
        
        for(int i = 0; i < blockSize; ++i) {
            for(int simulRoundCount = 0; simulRoundCount <= currentTournNum; ++simulRoundCount) {
                if (simulRoundCount == 0) {
                    newProbability[i][0] = tournProb[i][0] * probability[i][0];
                } else 
                if (simulRoundCount == currentTournNum) {
                    newProbability[i][simulRoundCount] = tournProb[i][1] * probability[i][simulRoundCount-1];
                } else {
                    newProbability[i][simulRoundCount] = tournProb[i][0] * probability[i][simulRoundCount] + tournProb[i][1] * probability[i][simulRoundCount-1];
                }                
            }
        }
        
        probability = newProbability;
        
        
        
    }
    
    public int getNumerator() {
        int sum = 0;
        
        for(int i = 0; i < blockSize; ++i) {
            for(int j = 1; j <= maxTournamentCount; ++j) {
                sum += probability[i][j] * j*j;
            }
        }
        
        return sum;
    }
}
