package com.eric.codejam;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

public class MinMax implements PrisonSelectionAlgorithm {

    private int calculateMax(int segStart, int segEnd, int toBeFreed) {
        int length = segEnd - segStart + 1;
        
        if (toBeFreed == 0) {
            return 0;
        }
        
        Preconditions.checkState(toBeFreed <= length);
        
        //since initial is not counted
        length -= 1;
        
        //sum 1 to Length-1
        int a = length * (length + 1) / 2;
        //sum 1 to Length - 1 tobef.size 
        int b = ( length - toBeFreed )  * ( length - toBeFreed + 1 ) / 2;
        
        return a - b;
    }
    @Override
    public int findMinCost(int segStart, int segEnd, List<Integer> toBeFreed) {
       
        log.debug("findMinCost {} {} {}", segStart, segEnd, toBeFreed);
        if (segEnd <= segStart) {
            return 0;
        }

        if (toBeFreed.size() == 0) {
            return 0;
        }

        if (toBeFreed.size() == 1) {
            return segEnd - segStart;
        }

        int chosenIdx = -1;
        int chosenPosition = -1;
        int chosenMinCost = Integer.MAX_VALUE;

        for (int i = 0; i < toBeFreed.size(); ++i) {
            int position = toBeFreed.get(i);

            Preconditions.checkState(!(position > segEnd || position < segStart));

            int lhCost = 0;
            int rhCost = 0;

            if (i > 0) {
                lhCost = calculateMax(segStart, position - 1, i);
            }

            if (i < toBeFreed.size() - 1) {
                rhCost = calculateMax(position + 1, segEnd, toBeFreed.size() - 1 - i);
            }
            
            int maxCost = lhCost + rhCost;

            if (maxCost < chosenMinCost) {
                chosenIdx = i;
                chosenPosition = position;
                chosenMinCost = maxCost;
            }
            

        }
        
        int cost = segEnd - segStart;
        chosenPosition = toBeFreed.get(chosenIdx);
        
        if (chosenIdx > 0) {
            List<Integer> cpy = new ArrayList<>(toBeFreed);
            cpy = cpy.subList(0, chosenIdx);
            cost += findMinCost(segStart, chosenPosition - 1, cpy);
        }

        if (chosenIdx < toBeFreed.size() - 1) {
            List<Integer> cpy = new ArrayList<>(toBeFreed);
            cpy = cpy.subList(chosenIdx + 1, toBeFreed.size());
            cost += findMinCost(chosenPosition + 1, segEnd, cpy);
        }

        log.debug(
                "findMinCost return {} params start {}  end {} to be freed {}",
                cost, segStart, segEnd, toBeFreed);
        return cost;
    }

}
