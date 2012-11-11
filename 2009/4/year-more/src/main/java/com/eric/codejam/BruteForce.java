package com.eric.codejam;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.Main.Tournament;
import com.google.common.base.Preconditions;

public class BruteForce {
    
    static boolean debug = false;
    static int count = 0;
    final static Logger log = LoggerFactory.getLogger(BruteForce.class);
    
    static int[][] counts = new int[4][4];
    
    static public int bruteForceHappiness(List<Tournament> list, int blockSize, int[] numExistingRounds) {
        if (list.size() == 0) {
            int h = 0;
            for (int idx = 0; idx < blockSize; ++idx) {
                int r = numExistingRounds[idx];
                if (debug)
                    counts[idx][r]++;
                h += r * r;
            }
            if (debug) {
                ++count;
                //++counts[r];
                log.debug("Calc {} = {}.  Count {} counts {}",
                        numExistingRounds, h, count, counts);
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
            
            if (debug) {
                log.debug("start {} num existing\n{}{}",
                        start,
                        StringUtils.repeat("    ", list.size()),
                       (Object) rounds);
            }
            
            happiness += bruteForceHappiness(list.subList(1, list.size()), blockSize, rounds);
            
        }
        
        return happiness;
        
    }
}
