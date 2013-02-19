package codejam.y2010.round_1C.chess_boards;

import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.Grid;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultiset;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    public Main()
    {
        super("C", 1, 1);
    }
    
    private static class Square implements Comparable<Square>{
        int size;
        int row;
        int col;
        /**
         * Top left corner
         */
        public Square(int size, int row, int col) {
            super();
            this.size = size;
            this.row = row;
            this.col = col;
        }
        @Override
        public int compareTo(Square o) {
            return ComparisonChain.start().compare(size, o.size).compare(o.row, row).compare(o.col, col).result();
            
        }
        @Override
        public String toString() {
            //if (1==1) throw new RuntimeException("ex");
            return "Size " + size + " R " + row + " C " + col;
        }
        
    }
    
    private static class PotentialSquares {
        
        
        //NW
        int diagConnectedCount;
       
        Square square;
        //Grid index
        int idx;
        
        //Row / col corresponding to grid index
        int row;
        int col;
        
        public PotentialSquares(int idx, Grid<Integer> grid) {
            diagConnectedCount = 1;
            this.idx = idx;
            int[] rowCol = grid.getRowCol(this.idx);
            row = rowCol[0];
            col = rowCol[1];        
            //this.grid = grid;
        }
        public void calcSquare() {
            int squareSize = diagConnectedCount;
            square = new Square(squareSize, row - (squareSize - 1), col - (squareSize - 1));
        }
        public void calcSquare(Grid<PotentialSquares> gridPot) {
            square = null;
            
            int squareSize = 1;
            //Find biggest square still on the board
            for(squareSize = 1; squareSize <= diagConnectedCount; ++squareSize) {
                boolean valid = true;
                int checkRow =  row - (squareSize - 1);
                int checkCol = col  - (squareSize - 1); 
                for(int n = checkCol; n <= col; ++n ) {
                    if (null == gridPot.getEntry(checkRow, n)) {
                        valid = false;
                    }
                }
                for(int m = checkRow; m <= row; ++m ) {
                    if (null == gridPot.getEntry(m, checkCol)) {
                        valid = false;
                    }
                }
                
                if (!valid) {
                    break;
                }
                
            }
            
            squareSize--;
            Preconditions.checkArgument(squareSize >= 1 && squareSize <= diagConnectedCount);
            diagConnectedCount = squareSize;
            square = new Square(squareSize, row - (squareSize - 1), col - (squareSize - 1));
            
        }
        public String toString() {
            //if (1==1) throw new RuntimeException("ex");
            return Integer.toString(diagConnectedCount);
        }
    }
    @Override
    public String handleCase(InputData input) {

        int caseNumber = input.testCase;
        log.debug("Grid {}", input.grid);

        
        
        //Build another grid which stocks the top left corner of a connected rectangle
        Grid<PotentialSquares> potGrid = Grid.buildEmptyGrid(input.M, input.N, null);
        Grid<Integer> grid = input.grid;
        
        SortedSet<Square> squares = new TreeSet<>();
        
        for(int m = 0; m < input.M; ++m) {
            for(int n = 0; n < input.N; ++n) {
                int idx = input.grid.getIndex(m, n);
                
                int val = grid.getEntry(idx);
                int northVal = grid.getEntry(idx, Direction.NORTH);
                int westVal = grid.getEntry(idx, Direction.WEST);
                int northWestVal = grid.getEntry(idx, Direction.NORTH_WEST);
                
                PotentialSquares topPs = potGrid.getEntry(idx, Direction.NORTH);
                PotentialSquares leftPs = potGrid.getEntry(idx, Direction.WEST);
                PotentialSquares topLeftPs = potGrid.getEntry(idx, Direction.NORTH_WEST);
                PotentialSquares ps = new PotentialSquares(idx, grid);
                
                
                if (val == northVal && val == westVal) {
                    
                    if ( val == northWestVal )  {
                        
                        Preconditions.checkState(grid.getEntry(idx, Direction.NORTH_WEST) == northVal);
                        
                        int minDiag = topLeftPs.diagConnectedCount;
                        minDiag = Math.min(minDiag, topPs.diagConnectedCount);
                        minDiag = Math.min(minDiag, leftPs.diagConnectedCount);
                        
                        //basically 3 squares make a bigger square
                        ps.diagConnectedCount = minDiag + 1;
                        
                    } else {
                        //nothing, form a 1x1 square
                        ps.diagConnectedCount = 1;
                    }
                }
                
                potGrid.setEntry(idx, ps);
                
                //Build the square object that shares a bottom / right corner with this grid index
                ps.calcSquare();
                squares.add(ps.square);
                
            }
        }
        
        log.debug("Potential {}", potGrid);
        
        
        
        TreeMultiset<Integer> counts =  TreeMultiset.create(Ordering.natural().reverse());
        
        int lastSquareSize = squares.size();
        
        while(!squares.isEmpty()) {
            Square square = squares.last();
            squares.remove(square);
            log.debug("Square {}.  Pot Grid {}", square, potGrid );
            //log.info("Squares size " + squares.size());
            Preconditions.checkArgument(squares.size() < lastSquareSize);
            lastSquareSize = squares.size();
            
                       
            counts.add(square.size);
            
            if (square.size == 1) {
                counts.add(square.size, squares.size());
                break;
            }
            
            //Remove all squares that were inside the chosen square
            for(int r = square.row; r < square.row + square.size; ++r) {
                for(int c = square.col; c < square.col + square.size; ++c) {
                    Preconditions.checkState(null != potGrid.getEntry(r, c));
                    PotentialSquares ps = potGrid.getEntry(r, c);
                    
                    potGrid.setEntry(r, c, null);     
                    squares.remove(ps.square);
                }
            }
            
            
            /**
             * potiential squares are keyed of bottom right corners
             * square top left
             * 
             * so there may be squares remaining that depeneded on the wood
             * just removed.  so if we removed
             * xxyy
             * xxyy
             * yyyy
             * yyyy
             * 
             * we check all y  anthyngi more would mean a contradiction
             */
            for(int m = square.row; m < square.row + 2 * square.size && m < input.M; ++m) {
                for(int n = square.col; n < square.col + 2 * square.size && n < input.N; ++n) {
                    PotentialSquares ps = potGrid.getEntry(m, n);
                    if (ps != null) {
                        squares.remove(ps.square);
                        ps.calcSquare(potGrid);
                        squares.add(ps.square);
                    }
                }
            }
            
            
            
        }
        
        
        StringBuffer sb = new StringBuffer();
        
        SortedSet<Integer> sortedCounts = counts.elementSet();
        
        sb.append("Case #" + caseNumber + ": " + sortedCounts.size());
        sb.append("\n");
        for(Integer size : sortedCounts) {
            sb.append(size).append(" ").append(counts.count(size)).append("\n");
        }
        
        
        return (sb.toString());
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)  {
        
    
        InputData  input = new InputData(testCase);
        
        input.M = scanner.nextInt();
        input.N = scanner.nextInt();
        
        input.grid = Grid.buildEmptyGrid(input.M, input.N, -1);
        int parity = 1;
        
        /*
         * Define a parity.  Basically, divide the squares into 2 sets.  Any square
         * in the set builds a chessboard.  
         */
        for (int m = 0; m < input.M; ++m) {
            parity = m % 2 == 0 ? 1 : 0;
            String hex = scanner.next();

            //Input is in hex that defines each row
            for (int hexIdx = 0; hexIdx < input.N / 4; ++hexIdx) {
                int hexInt = Integer.parseInt("" + hex.charAt(hexIdx), 16);
                // log.debug("Row {}",
                // StringUtils.leftPad(Integer.toBinaryString(row),
                // input.N,'0'));
                for (int hexBit = 0; hexBit < 4; ++hexBit) {
                    int n = hexIdx * 4 + hexBit;
                    int value = (1 << 4 - hexBit - 1 & hexInt) != 0 ? 1 : 0;

                    parity ^= 1;
                    // log.debug("r {} c {} value {} parity {}",
                    // m,n,value,parity);
                    input.grid.setEntry(m, n, value == parity ? 1 : 0);
                }
            }
        }
        
        return input;
        
    }

    
    
}