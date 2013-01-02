package codejam.y2011.round_2.spinning_blade;

import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Grid;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.math.DoubleMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
         return new String[] {"sample.in"};
      //  return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {



        /*
         * The first line of the input gives the number of test cases, T. 
         * T test cases follow. Each one starts with a line containing 
         * 3 integers: R, C and D — the dimensions of the grid and the 
         * mass you expected each cell to have.
         *  The next R lines each contain C digits wij each,
         *   giving the differences between the actual and 
         *   the expected mass of the grid cells. Each cell
         *    has a uniform density, but could have an integer
         *     mass between D + 0 and D + 9, inclusive.
         */
        InputData in = new InputData(testCase);
        in.R = scanner.nextInt();        
        in.C = scanner.nextInt();
        in.D = scanner.nextInt();
        
        Pattern delim = scanner.delimiter();
        scanner.useDelimiter("");
        
        in.cells = Grid.buildFromScanner(scanner, in.R, in.C,
                new Grid.FromScanner<Integer>() {
                    @Override
                    public Integer getFromScanner(Scanner scanner) {
                        String s = null;
                        while(StringUtils.trimToNull(s) == null) {
                            s = scanner.next();
                        }
                        return Integer.parseInt(s);
                    }

                }, -1);
        
        scanner.useDelimiter(delim);

        return in;
    }

    int solveBruteForce(InputData in) {
        int largestSize = Math.min(in.R, in.C);
        
        for(int size = largestSize; size >= 3; --size) {
            
            for(int topRow = 0; topRow <= in.R - size; ++topRow ) {
                for(int leftCol = 0; leftCol <= in.C - size; ++leftCol) {
                    int bottomRow = topRow + size - 1;
                    int rightCol = leftCol + size - 1;
                    
                    double centerCol = (rightCol + leftCol) / 2d;
                    double centerRow = (topRow + bottomRow) / 2d;
                                        
                    double sumCol = 0;
                    double sumRow = 0;
                    for(int r = topRow; r <= bottomRow; ++r) {
                        for(int c = leftCol; c <= rightCol; ++c) {
                            int mass = in.D + in.cells.getEntry(r,c);
                            
                            if ( (r == topRow || r == bottomRow) &&
                                    (c == leftCol || c == rightCol) ) {
                                //dont count corners
                                continue;
                            }
                            
                            sumCol += (centerCol - c) * mass;
                            sumRow += (centerRow - r) * mass;
                        }
                    }
                    
                    
                    
                    if (DoubleMath.fuzzyCompare(sumCol, 0, 1e-5) == 0 &&
                            DoubleMath.fuzzyCompare(sumRow, 0, 1e-5) == 0 
                            ) {
                        return size;
                    }
                }
            }
        }
        
        return 0;
    }
    
    static class Blade {
        double centerX;
        double centerY;
        double mass;
        int size;
        
        double centerMassX;
        double centerMassY;
        
        public int getIndex() {
            return getIndex(centerX, centerY, size);
        }
        
        static int getIndex(double centerX, double centerY, int size) {
            int centerXInt = DoubleMath.roundToInt(2 * centerX, RoundingMode.HALF_UP);
            int centerYInt = DoubleMath.roundToInt(2 * centerY, RoundingMode.HALF_UP);
            
            return centerYInt * size + centerXInt;
        }
    }
    
    static class Square {
        long totalMass;
        
        long colMassSum;
        long rowMassSum;
        
        int topRow;
        int leftCol;
        int size;
        
        
        public Square() {
            
        }
        
        static Square from1d(int totalMass, int row, int col) {
            Square square = new Square();
            square.totalMass = totalMass;
            square.colMassSum = col * totalMass;
            square.rowMassSum = row * totalMass;
            square.topRow = row;
            square.leftCol = col;
            square.size = 1;
            
            return square;
        }
        
        static Square from2d(Square[][] squares_1, int row, int col) {
            Square square = new Square();
            
            square.topRow = row;
            square.leftCol = col;
            square.size = 2;
            
            for(int deltaRow = 0; deltaRow <= 1; ++deltaRow) {
                for(int deltaCol = 0; deltaCol <= 1; ++deltaCol) {
                    square.totalMass += squares_1[row+deltaRow][col+deltaCol].totalMass;
                    square.colMassSum += squares_1[row+deltaRow][col+deltaCol].colMassSum;
                    square.rowMassSum += squares_1[row+deltaRow][col+deltaCol].rowMassSum;                    
                }
            }
            
            
            return square;
        }
        
        static Square fromXd(Square centerSquare, Square upperRight, Square lowerLeft
                , Square[][] squares_1,
                 int row, int col) {
            Square square = new Square();
            
            square.topRow = row;
            square.leftCol = col;
            square.size = centerSquare.size + 2;
            
            square.totalMass = upperRight.totalMass + lowerLeft.totalMass - centerSquare.totalMass
                    + squares_1[row][col].totalMass + squares_1[row+square.size - 1][col+square.size - 1].totalMass;
            square.rowMassSum = upperRight.rowMassSum + lowerLeft.rowMassSum - centerSquare.rowMassSum
                    + squares_1[row][col].rowMassSum + squares_1[row+square.size - 1][col+square.size - 1].rowMassSum;

            square.colMassSum = upperRight.colMassSum + lowerLeft.colMassSum - centerSquare.colMassSum
                    + squares_1[row][col].colMassSum + squares_1[row+square.size - 1][col+square.size - 1].colMassSum;

            return square;
            
        }
    }
    
    int solve(InputData in) {

        int largestSize = Math.min(in.R, in.C);
        
        
        int maxSize = 0;
        
        
        //indexed top left
        List<Square[][]> squares = Lists.newArrayList();
        
        Square[][] squares_1 = new Square[in.R][in.C];
        for(int r = 0; r < in.R; r++) {
            for(int c = 0; c < in.C; ++c) {
                squares_1[r][c] = Square.from1d(in.cells.getEntry(r,c) + in.D, r, c);
            }
        }
        
        Square[][] squares_2 = new Square[in.R - 1][in.C - 1];
        
        for(int r = 0; r < in.R - 1; r++) {
            for(int c = 0; c < in.C - 1; ++c) {
                squares_2[r][c] = Square.from2d(squares_1, r, c);
            }
        }
        
        squares.add(squares_1);
        squares.add(squares_2);
        
        for(int size = 3; size <= largestSize; ++size) {
            
            Square[][] centerSquares = squares.get(squares.size() - 2);
            Square[][] largeSquares = squares.get(squares.size() - 1);
            Square[][] newSquares = new Square[largeSquares.length - 1][largeSquares[0].length - 1];
            
            int sizeCenterSquare = size - 2;
        
            //A square is the center
            for(int topRow = 1; topRow <= in.R - sizeCenterSquare - 1; ++topRow ) {
                for(int leftCol = 1; leftCol <= in.C - sizeCenterSquare - 1; ++leftCol) {
                    Square centerSquare = centerSquares[topRow][leftCol];
                    Square upperRight = largeSquares[topRow - 1][leftCol];
                    Square lowerLeft = largeSquares[topRow][leftCol - 1];
                    Square newSquare = Square.fromXd(centerSquare,
                            upperRight, lowerLeft, squares_1, topRow-1, leftCol-1);
                    
                    newSquares[topRow - 1][leftCol - 1] = newSquare;
                    
                    int newSquareTopRow = topRow - 1;
                    int newSquareLeftCol = leftCol - 1;
                    
                    int centerX = (leftCol - 1 + leftCol - 1 + size - 1) ; // /2d
                    int centerY = (topRow - 1 + topRow - 1 + size - 1) ;   // / 2d
                    
                    //calculate center mass
                    long centerColMassSum = newSquare.colMassSum 
                            - squares_1[newSquareTopRow][newSquareLeftCol].colMassSum
                            - squares_1[newSquareTopRow+newSquare.size-1][newSquareLeftCol].colMassSum
                            - squares_1[newSquareTopRow][newSquareLeftCol+newSquare.size-1].colMassSum
                            - squares_1[newSquareTopRow+newSquare.size-1][newSquareLeftCol+newSquare.size-1].colMassSum;
                    
                    long centerRowMassSum = newSquare.rowMassSum 
                            - squares_1[newSquareTopRow][newSquareLeftCol].rowMassSum
                            - squares_1[newSquareTopRow+newSquare.size-1][newSquareLeftCol].rowMassSum
                            - squares_1[newSquareTopRow][newSquareLeftCol+newSquare.size-1].rowMassSum
                            - squares_1[newSquareTopRow+newSquare.size-1][newSquareLeftCol+newSquare.size-1].rowMassSum;
                    
                    long massBlade = (newSquare.totalMass 
                            - squares_1[newSquareTopRow][newSquareLeftCol].totalMass
                            - squares_1[newSquareTopRow+newSquare.size-1][newSquareLeftCol].totalMass
                            - squares_1[newSquareTopRow][newSquareLeftCol+newSquare.size-1].totalMass
                            - squares_1[newSquareTopRow+newSquare.size-1][newSquareLeftCol+newSquare.size-1].totalMass);

                    //To get rid of .5
                    centerColMassSum *= 2;
                    centerRowMassSum *= 2;
                    
                    if (centerColMassSum % massBlade != 0 || centerRowMassSum % massBlade != 0)
                        continue;
                    
                    long centerMassX = centerColMassSum / massBlade;
                    long centerMassY = centerRowMassSum / massBlade;
                    
                    if (centerX == centerMassX &&
                            centerY == centerMassY 
                            ) {
                        maxSize = size;
                    }
                }
            }
            
            squares.set(squares.size() - 2, null);
            squares.add(newSquares);
        }
    

        return maxSize;
    }
    
    public String handleCase(InputData in) {

        //int bf = solveBruteForce(in);
        
        int max = solve(in);
    
        if (max == 0) {
            //return String.format("Case #%d: IMPOSSIBLE %d", in.testCase, bf);
            return String.format("Case #%d: IMPOSSIBLE", in.testCase);
        } else {
            //return String.format("Case #%d: %d %d", in.testCase, max, bf);
            return String.format("Case #%d: %d", in.testCase, max);
        }
    }

}
