package com.eric.codejam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.utils.Grid;
import com.google.common.base.Preconditions;

public class DoubleRowSolver {
        
        final static Logger log = LoggerFactory.getLogger(DoubleRowSolver.class);
        
        int[][][] count;
                
        Grid<Integer> grid;
        int rows;
        int cols;
        
        public static final  int LETTER_MAX = 4;
        
        public DoubleRowSolver(Grid<Integer> grid) {
            //Preconditions.checkArgument(grid.getRows() == 1);
            this.grid = grid;
            this.rows = grid.getRows();
            this.cols = grid.getCols();
            count = new int[grid.getRows()][grid.getCols()][LETTER_MAX];
            
            for(int r = 0; r < rows; ++r) {
                for(int c=0; c < cols; ++c) {
                for(int j = 0; j < LETTER_MAX; ++j) {
                    count[r][c][j] = -1;
                }
            }
            }
            
            
        }
        
        public static int solveGrid(Grid<Integer> grid, int row) {
            
            DoubleRowSolver ss = new DoubleRowSolver(grid);
            
            if (grid.getEntry(0, 0) == 0) {
                return ss.solve(0, 0, 1); // a
            } else {
                return ss.solve(0, 0, grid.getEntry(0));
            }
        }
        
        /*
         * Returns sum @ index given the rest must be >= letter
         */
         int solve(int rowIndex, int colIndex, int letter) {
            int letterIndex = letter - 1;
            
            if (count[rowIndex][colIndex][letterIndex] >= 0) {
                log.debug("Returning (Already calc) row={}, col={} with letter >= {} SUM = {}", rowIndex, colIndex, letter, count[rowIndex][colIndex][letterIndex]);
                
                return count[rowIndex][colIndex][letterIndex];
            }
            log.debug("Solve r={},c={} with letter >= {}", rowIndex, colIndex, letter);
            int letterInGrid = grid.getEntry(rowIndex, colIndex);
            int sum = 0;
            //Corner
            if (colIndex == cols - 1 && rowIndex == rows - 1) {

                if (0 == letterInGrid) {
                    //any letter
                    sum = LETTER_MAX-letter+1;
                } else if (letterInGrid >= letter) {
                    sum = 1;
                }

            } else if (rowIndex == rows - 1){
                if (0 == letterInGrid ) {

                    for (int let = LETTER_MAX; let >= letter; --let) {
                        sum += solve(rowIndex, colIndex + 1, let);
                        log.debug("Sum only cols let counter {}  letter {} index {}", let, letter, colIndex);
                    }
                    

                } else if (letterInGrid >= letter){
                    sum = solve(rowIndex, colIndex + 1, letterInGrid);
                } else {
                    sum = 0;
                }
            } else if (colIndex == cols - 1) {
                if (0 == letterInGrid ) {

                    for (int let = LETTER_MAX; let >= letter; --let) {
                        sum += solve(rowIndex+1, colIndex, let);
                        log.debug("Sum only rows let counter {}  letter {} index {}", let, letter, colIndex);
                    }
                    

                } else if (letterInGrid >= letter){
                    sum = solve(rowIndex, colIndex + 1, letterInGrid);
                } else {
                    sum = 0;
                }

            } else {
                if (0 == letterInGrid ) {
                    for (int hereLet = LETTER_MAX; hereLet >= letter; --hereLet) {
                        log.debug("here {}", hereLet);
                for (int rightLetter = LETTER_MAX; rightLetter >= hereLet; --rightLetter) {
                    for (int bottomLetter = LETTER_MAX; bottomLetter >= hereLet; --bottomLetter) {
                        
                        int rightSum = solve(rowIndex, colIndex + 1, rightLetter);
                        
                        //We want exact values, not >=
                        if (rightLetter < LETTER_MAX) {
                            rightSum -= solve(rowIndex, colIndex + 1, rightLetter + 1);
                        }

                        int downSum = solve(rowIndex + 1, colIndex, bottomLetter);
                        
                        if (bottomLetter < LETTER_MAX) {
                            downSum -= solve(rowIndex+1, colIndex, bottomLetter + 1);
                        }

                        int minLetter = Math.min(rightLetter, bottomLetter);

                        int redundantSum = solve(rowIndex + 1, colIndex + 1, minLetter);
                        
                        log.debug("Sum only rows let counter {}  letter {} index {}",rightLetter, letter, colIndex);

                        int localSumTotal = rightSum + downSum - redundantSum;

                        sum += localSumTotal;
                        
                        if (rowIndex == 0 && colIndex == 0) {
                        Grid<Integer> checkGrid = new Grid<Integer>(grid);
                        checkGrid.setEntry(0, 0, hereLet);
                        checkGrid.setEntry(0, 1, rightLetter);
                        checkGrid.setEntry(1, 0, bottomLetter);
                        int check = Main.count(checkGrid, new Grid<Integer>(checkGrid));
                        Preconditions.checkState(check == localSumTotal);
                        }
                    }
                }
                    }

                } else if (letterInGrid >= letter){
                    sum = solve(rowIndex, colIndex + 1, letterInGrid);
                    sum += solve(rowIndex, colIndex+1, letterInGrid);
                    
                    sum += solve(rowIndex+1, colIndex, letterInGrid);
                    
                    sum -= solve(rowIndex+1, colIndex+1, letterInGrid);
                } else {
                    sum = 0;
                }                
            }
            log.debug("Returning row={}, col={} with letter >= {} SUM = {}", rowIndex, colIndex, letter, sum);
            count[rowIndex][colIndex][letterIndex] = sum;
            return sum;
        }
    }


