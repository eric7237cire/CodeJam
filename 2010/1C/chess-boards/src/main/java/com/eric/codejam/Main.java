package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.eric.codejam.utils.Direction;
import com.eric.codejam.utils.Grid;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultiset;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    private static class Square implements Comparable<Square>{
        int size;
        int row;
        int col;
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
            return "Size " + size + " R " + row + " C " + col;
        }
        
    }
    
    private static class PotentialSquares {
        //Highest connected cell column, highest being all the way left;west; col 0
        int topConnectedCount;
        
        //Highest in same row
        int leftConnectedCount;
        
        //NW
        int diagConnectedCount;
        Grid<Integer> grid;
        Square square;
        int idx;
        int row;
        int col;
        
        public PotentialSquares(int idx, Grid<Integer> grid) {
            diagConnectedCount = 1;
            this.idx = idx;
            int[] rowCol = grid.getRowCol(this.idx);
            row = rowCol[0];
            col = rowCol[1];
            topConnectedCount = 1;
            leftConnectedCount = 1;            
            this.grid = grid;
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
            
            square = new Square(squareSize, row - (squareSize - 1), col - (squareSize - 1));
            
        }
        public String toString() {
            return Integer.toString(diagConnectedCount);
        }
    }
    @Override
    public String handleCase(int caseNumber, InputData input) {

         
        log.debug("Grid {}", input.grid);

        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        
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
                
                PotentialSquares topPs = potGrid.getEntry(idx, Direction.NORTH);
                PotentialSquares leftPs = potGrid.getEntry(idx, Direction.WEST);
                PotentialSquares topLeftPs = potGrid.getEntry(idx, Direction.NORTH_WEST);
                PotentialSquares ps = new PotentialSquares(idx, grid);
                
                if (val == northVal) {
                    ps.topConnectedCount = topPs.topConnectedCount+1;
                }
                if (val == westVal) {
                    ps.leftConnectedCount =  leftPs.leftConnectedCount+1 ;
                }
                
                if (val == northVal && val == westVal) {
                    
                    if (topPs.diagConnectedCount >= 2 )  {
                        
                        Preconditions.checkState(grid.getEntry(idx, Direction.NORTH_WEST) == northVal);
                        
                        int minDiag = topLeftPs.diagConnectedCount;
                        minDiag = Math.min(minDiag, topPs.diagConnectedCount);
                        minDiag = Math.min(minDiag, leftPs.diagConnectedCount);
                        
                        //basically 3 squares make a bigger square
                        ps.diagConnectedCount = minDiag + 1;
                        
                    } else if (topPs.leftConnectedCount > 1 && leftPs.topConnectedCount > 1) {
                        Preconditions.checkState(grid.getEntry(idx, Direction.NORTH_WEST) == northVal);
                        //a square
                        ps.diagConnectedCount = 2;
                    } else {
                        //nothing
                        ps.diagConnectedCount = 1;
                    }
                }
                
                potGrid.setEntry(idx, ps);
                ps.calcSquare(potGrid);
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
           // log.info("Squares size " + squares.size());
            Preconditions.checkArgument(squares.size() < lastSquareSize);
            lastSquareSize = squares.size();
            
            
            boolean allClear = true;
            for(int r = square.row; r < square.row + square.size; ++r) {
                for(int c = square.col; c < square.col + square.size; ++c) {
                    if (null == potGrid.getEntry(r, c)) {
                        allClear = false;
                        break;
                    }
                }
            }
            
            if (!allClear) {
                continue;
            }
            
            counts.add(square.size);
            
            for(int r = square.row; r < square.row + square.size; ++r) {
                for(int c = square.col; c < square.col + square.size; ++c) {
                    Preconditions.checkState(null != potGrid.getEntry(r, c)); 
                    potGrid.setEntry(r, c, null);                        
                }
            }
            
           // squares.clear();
            
            //TODO efficace
            
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
        
        
        /*
        List<String> testBoard = Arrays.asList(
        "0101",
        "1001"
        );
        
        for(String s : testBoard) {
            log.debug("Int {}", Integer.toHexString(Integer.parseInt(s,2)));
        }*/
        
        return (sb.toString());
    }
    
    /*
     * Operators    Precedence

unary   ++expr --expr +expr -expr ~ !
multiplicative  * / %
additive    + -
shift   << >> >>>
relational  < > <= >= instanceof
equality    == !=
bitwise AND &
bitwise exclusive OR    ^bitwise inclusive OR    |.
logical AND &&
logical OR  ||
ternary ? :
assignment  = += -= *= /= %= &= ^= |= <<= >>= >>>=

     */
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        
        InputData  input = new InputData(testCase);
        
        input.M = Integer.parseInt(line[0]);
        input.N = Integer.parseInt(line[1]);
        
        input.grid = Grid.buildEmptyGrid(input.M, input.N, -1);
        int parity = 1;
        
        /*
         * Define a parity.  Basically, divide the squares into 2 sets.  Any square
         * in the set builds a chessboard.  
         */
        for(int m = 0; m < input.M; ++m) {
            parity = m % 2 == 0 ? 1 : 0;
            String hex = br.readLine();
            
            for(int hexIdx = 0; hexIdx < input.N / 4; ++hexIdx) {
            int hexInt = Integer.parseInt("" + hex.charAt(hexIdx), 16);
            //log.debug("Row {}", StringUtils.leftPad(Integer.toBinaryString(row), input.N,'0'));
            for(int hexBit = 0; hexBit < 4; ++hexBit) {
                int n = hexIdx * 4 + hexBit;
                int value = (1 << 4 - hexBit - 1 & hexInt) != 0 ? 1 : 0;
                
                parity ^= 1;
                //log.debug("r {} c {} value {} parity {}", m,n,value,parity);
                input.grid.setEntry(m, n, value == parity ? 1 : 0);
            }
            }
        }
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
  //          args = new String[] { "sample.txt" };
            args = new String[] { "C-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}