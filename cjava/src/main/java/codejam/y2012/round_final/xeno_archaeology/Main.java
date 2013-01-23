package codejam.y2012.round_final.xeno_archaeology;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] { "sample.in" };
     //    return new String[] { "B-small-practice.in" };
       //  return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        in.N = scanner.nextInt();

        in.tiles = Lists.newArrayList();
        
        for (int i = 0; i < in.N; ++i) {
            
            Tile tile = new Tile(scanner.nextLong(), scanner.nextLong(), 
                    ".".equals(scanner.next()) ? false : true);
            
            in.tiles.add(tile);
            
        }
        return in;
    }
    
    public static class Tile  {

        final long y;
        final long x;
        final boolean isRed;
        
        public Tile(long x, long y, boolean isRed) {
            this.y = y;
            this.x = x;
            this.isRed = isRed;
        }

        public long getY()
        {
            return y;
        }


        public long getX()
        {
            return x;
        }
        
    }
    
    class ManhatDistance implements Comparator<PointInt>
    {

        @Override
        public int compare(PointInt o1, PointInt o2) {
            int m1 = Math.abs(o1.getX()) + Math.abs((o1.getY()));
            int m2 = Math.abs(o2.getX()) + Math.abs((o2.getY()));
            return ComparisonChain.start()
                    .compare(m1, m2)
                    .compare(o2.getX(), o1.getX())
                    .compare(o2.getY(), o1.getY())
                    .result();
        }

       
        
    }

    /**
     * Follow given solution
     */
    public String handleCase(InputData in)
    {
        /**
         * Center coordinate Cx, Cy can
         * either have an odd x or even ; similiar for y
         */
        
        List<Line> boundaryLines = Lists.newArrayList();
        
        

        for (int centerYOdd = 0; centerYOdd <= 1; ++centerYOdd)
        {
            centerXLoop:
            for (int centerXOdd = 0; centerXOdd <= 1; ++centerXOdd)
            {
                
                List<Long> posSlopeYIntercepts = Lists.newArrayList();
                List<Long> negSlopeYIntercepts = Lists.newArrayList();

                List<Long> constraintGTE = Lists.newArrayList();
                List<Long> constraintLTE = Lists.newArrayList();
                
                /**
                 * For each point, find if x - x' is odd/even
                 * and y - y'.
                 * 
                 * If the color is red, max ( abs(x - x'), abs(y - y' ) must be odd
                 * blue, must be even 
                 */
                for (Tile tile : in.tiles)
                {
                    boolean xDiffOdd = (centerXOdd == 1 && tile.getX() % 2 == 0) ||
                            (centerXOdd == 0 && tile.getX() % 2 != 0);

                    boolean yDiffOdd = (centerYOdd == 1 && tile.getY() % 2 == 0) || 
                            (centerYOdd == 0 && tile.getY() % 2 != 0);

                    if ((tile.isRed && !xDiffOdd && !yDiffOdd) || 
                            (!tile.isRed && xDiffOdd && yDiffOdd))
                    {
                        break centerXLoop;
                        /**
                         * No solution will work.  If center is 
                         * odd, odd then a red point 3,  7 will have both xDiff and yDiff be even.
                         */
                    } else if ((tile.isRed && xDiffOdd && yDiffOdd) ||
                            (!tile.isRed && !xDiffOdd && !yDiffOdd))
                    {
                        /**
                         * Any solution will work.
                         */
                    } else {
                        /**
                         * Create lines (if you check the graph, they match the abs value).
                         * 
                         * They will have slope 1 or -1.  Only the y-intercept changes
                         */
                        
                        // x + y <= / >= x' + y'
                        Line negSlope = new Line(-1, tile.getX()+tile.getY());
                        
                        // x - y >= / <= x' - y'
                        Line posSlope = new Line(1, tile.getY() - tile.getX());
                        
                        boundaryLines.add(negSlope);
                        boundaryLines.add(posSlope);
                        
                        negSlopeYIntercepts.add( tile.getX()+tile.getY());
                        
                        posSlopeYIntercepts.add( tile.getY() - tile.getX());
                        
                        if ( (tile.isRed && xDiffOdd) || (!tile.isRed && !xDiffOdd) ){
                            // abs( x - x' ) > abs ( y - y')
                            // Basically the diff of x's must become the maximum 
                            constraintGTE.add( tile.getY() );
                        } else if ( (tile.isRed && yDiffOdd) || (!tile.isRed && !yDiffOdd) ){
                            constraintLTE.add( tile.getX() );
                        }
                    }

                }
                
                
                Collections.sort(negSlopeYIntercepts);
                Collections.sort(posSlopeYIntercepts);
                
                for(int p2Idx = 1; p2Idx < posSlopeYIntercepts.size(); ++p2Idx)
                {
                    for(int n2Idx = 1; n2Idx < negSlopeYIntercepts.size(); ++n2Idx)
                    {
                        /**
                         * Rectangle bounded by y = x + p1 ; y = x + p2 
                         *                      y = -x + n1 ; y = -x + n1
                         */
                    }
                }
                
            }
        }
        
       
        
        
            
        return bruteForce(in);
        
    }
    
    
    public String bruteForce(InputData in) {

        PointInt bestCandidate = null;
        
        
        
        Ordering<PointInt> order = Ordering.from(new ManhatDistance()).nullsLast(); 
        
        List<PointInt> blueTiles = Lists.newArrayList();
        List<PointInt> redTiles = Lists.newArrayList();
        
        for(Tile tile : in.tiles) {
            if (tile.isRed) {
                redTiles.add(new PointInt( (int) tile.getX(), (int) tile.getY()));
            } else {
                blueTiles.add(new PointInt( (int) tile.getX(), (int) tile.getY()));
            }
        }
        
        for(int y = -201; y <= 201; ++y)
        {
            for(int x = -201; x <= 201; ++x)
            {
                //Suppose center is at x, y
                PointInt center = new PointInt(x,y);
                
                boolean ok = true;
                for(PointInt red : redTiles)
                {
                    int parity = center.getKingDistance(red) % 2;
                    if (parity == 0)
                    {
                        ok = false;
                        break;
                    }
                }
                
                for(PointInt blue : blueTiles)
                {
                    int parity = center.getKingDistance(blue) % 2;
                    if (parity == 1)
                    {
                        ok = false;
                        break;
                    }
                }
                
                if (ok && order.compare(center, bestCandidate) < 0)
                    bestCandidate = center;
            }
        }
                
        return String.format("Case #%d: %s", in.testCase,
                bestCandidate == null ? "Too damaged" : "" + bestCandidate.getX() + " " + bestCandidate.getY());
        
    }

}
