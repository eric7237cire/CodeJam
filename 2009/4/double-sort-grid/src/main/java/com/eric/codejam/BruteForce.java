package com.eric.codejam;

import com.eric.codejam.utils.Direction;
import com.eric.codejam.utils.Grid;
import com.google.common.collect.Ordering;

public class BruteForce {
  //Bruteforce
    public static int count(Grid<Integer> grid, Grid<Integer> gridOrig) {
        //log.info("Count grid {}", grid);
        Ordering<Integer> o = Ordering.natural().nullsFirst();
        int count = 0;
        for(int i = 0; i < grid.getSize(); ++i) {
            
            Integer current = grid.getEntry(i);
            Integer top = grid.getEntry(i, Direction.NORTH);
            Integer left = grid.getEntry(i, Direction.WEST);
            
            //A flexible spot, compute all possibilities
            if (current == 0) {
                for(int j = DynamicProgrammingSmall.LETTER_MAX; j >= 1; --j) {
                    //Must be non decreasing
                    if (o.compare(j, top) < 0) {
                        break;
                    }
                    
                    if (o.compare(j, left) < 0) {
                        break;
                    }
                    Grid<Integer> copy = new Grid<Integer>(grid);
                    copy.setEntry(i, j);
                    count += count(copy, gridOrig);
                    
                    count %= 10007;
                }
                return count;
            }
            
            //Can not decrease
            if (o.compare(current, top) < 0) {
                return 0;
            }
            
            if (o.compare(current, left) < 0) {
                return 0;
            }
        }
        
        /*
        for(int idx = 0; idx < 9; ++idx) {
            for(int  idx2 = 0; idx2 < 9; ++idx2) {
                specialCountr_c[idx][idx2][grid.getEntry(idx) - 1][grid.getEntry(idx2) - 1] ++;
            }
        }*/
        //No variable squares
        return 1;
    }
    
    
}
