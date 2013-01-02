package codejam.y2011.round_2.spinning_blade;

import java.math.RoundingMode;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Grid;

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
        
        static int getIndex(double centerX, double centerY, int size) {
            int centerXInt = DoubleMath.roundToInt(2 * centerX, RoundingMode.HALF_UP);
            int centerYInt = DoubleMath.roundToInt(2 * centerY, RoundingMode.HALF_UP);
            
            return centerYInt * size + centerXInt;
        }
    }
    
    int solve(InputData in) {

        int largestSize = Math.min(in.R, in.C);
        
        int size = 3;
        
        Map< Integer, Blade > centers = Maps.newHashMap();  
            
        for(int topRow = 0; topRow <= in.R - size; ++topRow ) {
            for(int leftCol = 0; leftCol <= in.C - size; ++leftCol) {
                int bottomRow = topRow + size - 1;
                int rightCol = leftCol + size - 1;
                
                double centerCol = (rightCol + leftCol) / 2d;
                double centerRow = (topRow + bottomRow) / 2d;
                
                                    
                double sumCol = 0;
                double sumRow = 0;
                int totalMass = 0;
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
                        totalMass += mass;
                    }
                }
                
                Blade blade = new Blade();
                blade.mass = totalMass;
                blade.centerX = centerCol;
                blade.centerY = centerRow;
                blade.centerMassX = blade.centerX + sumCol / blade.mass;
                blade.centerMassY = blade.centerY + sumRow / blade.mass;
                
                Pair<Double, Double> center = new ImmutablePair<>(centerRow, centerCol);
                 
                
                
                if (DoubleMath.fuzzyCompare(sumCol, 0, 1e-5) == 0 &&
                        DoubleMath.fuzzyCompare(sumRow, 0, 1e-5) == 0 
                        ) {
                    return size;
                }
            }
        }
    

    }
    
    public String handleCase(InputData in) {

        int bf = solveBruteForce(in);
    
        if (bf == 0) {
            return String.format("Case #%d: IMPOSSIBLE", in.testCase);
        } else {
            return String.format("Case #%d: %d", in.testCase, bf);
        }
    }

}
