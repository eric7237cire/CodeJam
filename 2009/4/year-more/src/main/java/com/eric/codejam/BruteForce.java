package com.eric.codejam;

import java.util.Arrays;
import java.util.List;

import com.eric.codejam.Main.Tournament;
import com.google.common.base.Preconditions;

public class BruteForce {
    
    
    static public int bruteForceHappiness(List<Tournament> list, int blockSize, int[] numExistingRounds) {
        if (list.size() == 0) {
            int h = 0;
            for (int r : numExistingRounds) {
                h += r * r;
            }
            return h;
        }
        
        Preconditions.checkArgument(numExistingRounds.length == blockSize);
        
        int happiness = 0;
        
        Tournament t = list.get(0);
        
        for(int start = 0; start < blockSize; ++start) {
            int[] rounds = new int[blockSize];
            rounds = Arrays.copyOf(numExistingRounds, blockSize);
            
            for(int round : t.roundDays) {
                if (start + round >= blockSize) {
                    break;
                }               
                
                rounds[round+start] ++;
            }
            
            happiness += bruteForceHappiness(list.subList(1, list.size()), blockSize, rounds);
            
        }
        
        return happiness;
        
    }
}
