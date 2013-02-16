package codejam.y2008.round_3.endless_knight;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.fraction.Fraction;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.LargeNumberUtils;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>
{

    public Main() {
        super("D", 1, 1, 1);
        //((ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }

    /*
     * To calculate change of basis
     * Array2DRowRealMatrix m = new Array2DRowRealMatrix(
                new double[][] {
                        {2d, 1d},
                        {1d, 2d},
                });
        
        LUDecomposition lu = new LUDecomposition(m);
        RealMatrix inv = lu.getSolver().getInverse();
        
        log.debug("Inv {}", inv);
        
        for(int x = 0; x <= 5; ++x)
        {
            for(int y = 0; y <= 5; ++y) 
            {
        
                Array2DRowRealMatrix p1 = 
                        new Array2DRowRealMatrix(
                                new double[][] {
                                        {(double)x},
                                        {(double)y},
                                });
            
                RealMatrix r = inv.multiply(p1);
                log.debug("Result {},{} = {}", x,y ,r);
            }
        }
     */

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);

        in.H = scanner.nextInt();
        in.W = scanner.nextInt();
        in.R = scanner.nextInt();

        in.rocksOrig = new PointInt[in.R];

        for (int i = 0; i < in.R; ++i)
        {
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            in.rocksOrig[i] = new PointInt(col, row);

        }
        return in;
    }

    /*
     * example in solution
     * 
     * 11 height
     * 13 width
     * 
     * going by diags
     * 1,1  3,2  5,3  7,4   9,5   11,6
     * 2,3  4,4  6,5  8,6   10,7  12,8
     * 3,5  5,6  7,7  9,8   11,9  13,10
     * 4,7  6,8  8,9  10,10 12,11 14,12
     * 
     * these coords will be transformed to
     * 0,0  1,0   2,0  3,0   4,0  5,0
     * 0,1  1,1   2,1  3,1   4,1  5,1
     * 0,2  1,2   2,2  3,2   4,2  5,2
     * 0,3  1,3   2,3  3,3   4,3  5,3
     */
    void convertBasis(InputData in)
    {
        /*
        Fraction[][] basisMatrix = new Fraction[][] {
                {2d/3, -1d/3},
                {-1d/3, 2d/3}
        };*/
        Fraction a = new Fraction(2, 3);
        Fraction b = new Fraction(-1, 3);

        in.rocks = Lists.newArrayList();
        
        for (PointInt rock : in.rocksOrig)
        {
            Fraction x = a.multiply(rock.x()-1).add(b.multiply(rock.y()-1));
            Fraction y = b.multiply(rock.x()-1).add(a.multiply(rock.y()-1));

            if (x.getDenominator() == 1 && y.getDenominator() == 1)
            {
                PointInt pt = new PointInt(x.intValue(), y.intValue());
                if (pt.x() >= 0 && pt.y() >= 0)
                    in.rocks.add(pt);
            }
        }

        Fraction x = a.multiply(in.W-1).add(b.multiply(in.H-1));
        Fraction y = b.multiply(in.W-1).add(a.multiply(in.H-1));
        
        if (x.getDenominator() == 1 && 
                y.getDenominator() == 1 && x.intValue() >= 0 && y.intValue() >= 0)
        {
            in.finalCorner = new PointInt(x.intValue(),y.intValue());
        }

    }
    
    private static class RockSorter implements Comparator<PointInt>
    {

        @Override
        public int compare(PointInt o1, PointInt o2)
        {
            return ComparisonChain.start().compare(o1.x(),o2.x()).compare(o1.y(), o2.y()).result();
        }
    }
    
    static int[][] preCal = null;

    @Override
    public String handleCase(InputData in)
    {

        convertBasis(in);
        
        log.debug("Rocks {}", in.rocks);
        log.debug("Final {}", in.finalCorner);
        
        //Final corner not reachable
        if (in.finalCorner == null) {
            return String.format("Case #%d: 0", in.testCase);
        }
        
        final int mod = 10007;
        if (preCal == null)
            preCal = LargeNumberUtils.generateModedCombin(mod-1, mod);
        
        int subsets = 1 << in.rocks.size();
        
        Collections.sort(in.rocks, new RockSorter());
        
        int totalWays = 0;
        
        for(int perm = 0; perm < subsets; ++perm)
        {
            /**
             * Calculate ways to go from start hitting all the rocks in subset
             * to the end
             */
            List<PointInt> subSet = Lists.newArrayList();
            subSet.add(new PointInt(0,0));
            
            //Build up the subset list
            for(int r = 0; r < in.rocks.size(); ++r) {
                if ( (perm & 1 << r) != 0) {
                    subSet.add(in.rocks.get(r));
                }
            }
            
            subSet.add(in.finalCorner);
            
            int ways = 1;
            
            for(int subSetIdx = 1; subSetIdx < subSet.size(); ++subSetIdx )
            {
                PointInt last = subSet.get(subSetIdx-1);
                PointInt cur = subSet.get(subSetIdx);
                
                int m = cur.y() - last.y();
                int n = cur.x() - last.x();
                //Can only move in a positive direction +(1,0) or +(0,1)
                if ( m < 0 || n < 0 ) {
                    ways = 0;
                    break;
                }
                //Lattice paths 
                ways *= LargeNumberUtils.combin(m+n, n, preCal, mod);
                ways %= mod;
            }
            
            int mult = subSet.size() % 2 == 0 ? 1 : -1;
            totalWays += mod + mult * ways;
            totalWays %= mod;
            
        }

        return String.format("Case #%d: %d", in.testCase, totalWays);

    }
}