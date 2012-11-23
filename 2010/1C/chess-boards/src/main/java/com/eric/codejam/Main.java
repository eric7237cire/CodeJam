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
        Set<Integer> indexSameColumn;
        Set<Integer> indexSameRow;
        SortedSet<Integer> indexNorthWest;
        Grid<Integer> grid;
        Square square;
        int idx;
        
        public PotentialSquares(int idx, Grid<Integer> grid) {
            indexSameColumn = Sets.newHashSet(idx);
            indexSameRow = Sets.newHashSet(idx);
            indexNorthWest = Sets.newTreeSet();
            indexNorthWest.add(idx);
            this.idx = idx;
            this.grid = grid;
        }
        public void calcSquare(Grid<PotentialSquares> gridPot) {
            square = null;
            int[] rowCol = grid.getRowCol(idx);
            for(Integer nwIdx : indexNorthWest) {
                if (null == gridPot.getEntry(nwIdx)) {
                    //TODO supprime index NW
                    continue;
                }
                int[] nwRowCol = grid.getRowCol(nwIdx);
                int rowDiff =  rowCol[0] - nwRowCol[0];
                int colDiff = rowCol[1] - nwRowCol[1] ;
                if (rowDiff == colDiff) {
                    Square sq = new Square(rowDiff+1, nwRowCol[0], nwRowCol[1]);
                    if (square == null || sq.compareTo(square) > 0) {
                        square = sq;
                    }
                }
            }
            
            Preconditions.checkState(square != null);
        }
        public String toString() {
            return Integer.toString(square.size);
        }
    }
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        log.debug("Grid {}", input.grid);

        log.info("Done calculating answer case {}", caseNumber);
        
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
                PotentialSquares westPs = potGrid.getEntry(idx, Direction.WEST);
                
                PotentialSquares ps = new PotentialSquares(idx, grid);
                
                if (val == northVal) {
                    ps.indexSameColumn.addAll( topPs.indexSameColumn );
                }
                if (val == westVal) {
                    ps.indexSameRow.addAll( westPs.indexSameRow );
                }
                
                if (val == northVal && val == westVal) {
                    ps.indexNorthWest.addAll (
                            Sets.intersection( Sets.union(topPs.indexSameRow, topPs.indexNorthWest) //to cover 2x2
                                    , Sets.union(westPs.indexSameColumn, westPs.indexNorthWest) ) );
                }
                
                potGrid.setEntry(idx, ps);
                ps.calcSquare(potGrid);
                squares.add(ps.square);
                
            }
        }
        
        log.debug("Potential {}", potGrid);
        
        
        
        TreeMultiset<Integer> counts =  TreeMultiset.create(Ordering.natural().reverse());
        
        while(!squares.isEmpty()) {
            Square square = squares.last();
            
            log.debug("Square {}.  Pot Grid {}", square, potGrid );
            
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
            
            squares.clear();
            
            //TODO efficace
            for(int m = 0; m < input.M; ++m) {
                for(int n = 0; n < input.N; ++n) {
                    PotentialSquares ps = potGrid.getEntry(m, n);
                    if (ps != null) {
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
bitwise exclusive OR    ^
bitwise inclusive OR    |
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
            int row = Integer.parseInt(br.readLine(), 16);
            //log.debug("Row {}", StringUtils.leftPad(Integer.toBinaryString(row), input.N,'0'));
            for(int n = 0; n < input.N; ++n) {
                int value = (1 << input.N - n - 1 & row) != 0 ? 1 : 0;
                
                parity ^= 1;
                //log.debug("r {} c {} value {} parity {}", m,n,value,parity);
                input.grid.setEntry(m, n, value == parity ? 1 : 0);
            }
        }
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
           // args = new String[] { "B-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}