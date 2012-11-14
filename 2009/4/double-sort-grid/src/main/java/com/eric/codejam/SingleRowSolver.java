package com.eric.codejam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.utils.Grid;
import com.google.common.base.Preconditions;

public class SingleRowSolver {
    
    final static Logger log = LoggerFactory.getLogger(SingleRowSolver.class);
    
    int[][] count;
    int lenMax;
    Grid<Integer> grid;
    
    public SingleRowSolver(Grid<Integer> grid) {
        Preconditions.checkArgument(grid.getRows() == 1);
        this.lenMax = grid.getCols();
        this.grid = grid;
        count = new int[lenMax][26];
        
        for(int i = 0; i < lenMax; ++i) {
            for(int j = 0; j < 26; ++j) {
                count[i][j] = -1;
            }
        }
        
        
    }
    
    public static int solveGrid(Grid<Integer> grid) {
        SingleRowSolver ss = new SingleRowSolver(grid);
        
        if (grid.getEntry(0, 0) == 0) {
            return ss.solve(0, 1); // a
        } else {
            return ss.solve(0, grid.getEntry(0));
        }
    }
    
    /*
     * Returns sum @ index given the rest must be >= letter
     */
    private int solve(int index, int letter) {
        int letterIndex = letter - 1;
        if (count[index][letterIndex] >= 0) {
            return count[index][letterIndex];
        }
        log.debug("Solve {} letter {}", index, letter);
        int letterInGrid = grid.getEntry(0, index);
        int sum = 0;
        if (index == lenMax - 1) {

            if (0 == letterInGrid) {
                //any letter
                sum = 26-letter+1;
            } else if (letterInGrid >= letter) {
                sum = 1;
            }

        } else {
            if (0 == letterInGrid ) {

                for (int let = 26; let >= letter; --let) {
                    sum += solve(index + 1, let);
                    log.debug("Sum let counter {}  letter {} index {}", let, letter, index);
                }
                

            } else if (letterInGrid >= letter){
                sum = solve(index + 1, letterInGrid);
            } else {
                sum = 0;
            }
        }
        log.debug("Solve {} letter {} SUM {}", index, letter, sum);
        count[index][letterIndex] = sum;
        return sum;
    }
}
