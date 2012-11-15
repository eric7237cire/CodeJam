package com.eric.codejam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.utils.Grid;
import com.google.common.base.Preconditions;

public class SingleRowSolver {
    
    final static Logger log = LoggerFactory.getLogger(SingleRowSolver.class);
    
    int[][] count;
    int lenMax;
    int row;
    Grid<Integer> grid;
    
    int LETTER_MAX = 4;
    
    public SingleRowSolver(Grid<Integer> grid, int row) {
        //Preconditions.checkArgument(grid.getRows() == 1);
        this.lenMax = grid.getCols();
        this.grid = grid;
        this.row = row;
        count = new int[lenMax][LETTER_MAX];
        
        for(int i = 0; i < lenMax; ++i) {
            for(int j = 0; j < LETTER_MAX; ++j) {
                count[i][j] = -1;
            }
        }
        
        
    }
    
    public static int solveGrid(Grid<Integer> grid, int row) {
        
        SingleRowSolver ss = new SingleRowSolver(grid, row);
        
        if (grid.getEntry(0, 0) == 0) {
            return ss.solve(0, 1); // a
        } else {
            return ss.solve(0, grid.getEntry(0));
        }
    }
    
    /*
     * Returns sum @ index given the rest must be >= letter
     */
     int solve(int colIndex, int letter) {
        int letterIndex = letter - 1;
        if (count[colIndex][letterIndex] >= 0) {
            return count[colIndex][letterIndex];
        }
        log.debug("Solve {} letter {}", colIndex, letter);
        int letterInGrid = grid.getEntry(row, colIndex);
        int sum = 0;
        if (colIndex == lenMax - 1) {

            if (0 == letterInGrid) {
                //any letter
                sum = LETTER_MAX-letter+1;
            } else if (letterInGrid >= letter) {
                sum = 1;
            }

        } else {
            if (0 == letterInGrid ) {

                for (int let = LETTER_MAX; let >= letter; --let) {
                    sum += solve(colIndex + 1, let);
                    log.debug("Sum let counter {}  letter {} index {}", let, letter, colIndex);
                }
                

            } else if (letterInGrid >= letter){
                sum = solve(colIndex + 1, letterInGrid);
            } else {
                sum = 0;
            }
        }
        log.debug("Solve {} letter {} SUM {}", colIndex, letter, sum);
        count[colIndex][letterIndex] = sum;
        return sum;
    }
}
