package codejam.y2012.round_final.xeno_archaeology;

import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.math.DoubleMath;

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
        bruteForce(in);
        
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

                List<Tile> constraintDiffXGTEDiffY = Lists.newArrayList();
                List<Tile> constraintDiffXLTEDiffY = Lists.newArrayList();
                
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
                            constraintDiffXGTEDiffY.add( tile );
                        } else if ( (tile.isRed && yDiffOdd) || (!tile.isRed && !yDiffOdd) ){
                            constraintDiffXLTEDiffY.add( tile );
                        }
                    }

                }
                
                
                Collections.sort(negSlopeYIntercepts);
                Collections.sort(posSlopeYIntercepts);
                
                for(int p2Idx = 1; p2Idx < posSlopeYIntercepts.size(); ++p2Idx)
                {
                    nextRectangle:
                    for(int n2Idx = 1; n2Idx < negSlopeYIntercepts.size(); ++n2Idx)
                    {
                        /**
                         * Rectangle bounded by y = x + p1 ; y = x + p2 
                         *                      y = -x + n1 ; y = -x + n1
                         */
                        
                        
                        long p1 = posSlopeYIntercepts.get(p2Idx - 1);
                        long p2 = posSlopeYIntercepts.get(p2Idx);
                        
                        long n1 = negSlopeYIntercepts.get(n2Idx - 1);
                        long n2 = negSlopeYIntercepts.get(n2Idx);
                        
                        log.debug("Rectangle bounded by y = x + {}, {} and y = -x + {}, {}", p1,p2,n1,n2);
                        
                        Line posSlope = new Line(1, p1);
                        Line negSlope = new Line(-1, n1);
                        
                        Point intersection = posSlope.getIntersection(negSlope);
                        
                        long cX = DoubleMath.roundToLong(intersection.getX(), RoundingMode.HALF_DOWN);
                        long cY = 1 + DoubleMath.roundToLong(intersection.getY(), RoundingMode.HALF_DOWN);
                        
                        for( Tile tile : constraintDiffXGTEDiffY ) {
                            long diffX = Math.abs( cX - tile.getX() );
                            long diffY = Math.abs( cY - tile.getY() );
                            
                            if (diffX < diffY) {
                                continue nextRectangle;
                            }
                        }
                        
                        for( Tile tile : constraintDiffXLTEDiffY ) {
                            long diffX = Math.abs( cX - tile.getX() );
                            long diffY = Math.abs( cY - tile.getY() );
                            
                            if (diffX > diffY) {
                                continue nextRectangle;
                            }
                        }
                        
                        log.debug("Found a rectangle.  center x odd? {} center y odd? {} ", centerXOdd, centerYOdd);
                    }
                }
                
            }
        }
        
       
        
        
            
        return bruteForce(in);
        
    }
    
    /**
     * 
     * A <= x + y <= B (negative slope lines)
     * C <= x - y <= D (positive slope lines
     */
    Tile findBestPointInRectangle(long A, long B, long C, long D, boolean centerXOdd, boolean centerYOdd) {
       long gAB = g(A,B);
       long gCD = g(C,D);
       
       if (gAB == 0 && gCD == 0)
           return findBestPointInRectangleContainingZero(A,B,C,D,centerXOdd, centerYOdd);
       
       //Look at graph on desmos to see why
       long M = Math.max(g(A,B), g(C,D)) ;
       
       //Find the line that limits, and add one
       Preconditions.checkState(Math.abs(A) <= Math.abs(B));
       Preconditions.checkState(Math.abs(C) <= Math.abs(D));
       
       List<Point> pointsToTest = Lists.newArrayList();
       
       if (M == Math.abs(A)) {
           Line mLine = null;
           
           
           if (A >= 0) {
               //Line is x+y = A+1
               mLine = new Line( new Point(0, M+1), new Point(M+1, 0));
               pointsToTest.add( new Point(0, M+1) );
               pointsToTest.add( new Point(M+1, 0) );
           } else {
               //Line is x+y = A - 1
               mLine = new Line( new Point(0, -M-1), new Point(-M-1, 0));
               pointsToTest.add( new Point(0, -M-1) );
               pointsToTest.add( new Point(-M-1, 0) );
           }
           
           //Intersect with line C and D
           Line cLine = new Line(1, -C);
           Line dLine = new Line(1, -D);
           
           Point cm = cLine.getIntersection(mLine);
           
           Point dm = dLine.getIntersection(mLine);
           
           pointsToTest.add(cm);
           pointsToTest.add(dm);
       } else {
           Preconditions.checkState(M == Math.abs(C));
           Line mLine = null;
           if (C <= 0) {
               //Line is x-y = C - 1
               mLine = new Line( new Point(-M-1, 0), new Point(0, M+1));

               pointsToTest.add( mLine.getP1() );
               pointsToTest.add( mLine.getP2() );
           } else {
               //Line is x+y = A - 1
               mLine = new Line( new Point(0, -M-1), new Point(M+1, 0));
               pointsToTest.add( mLine.getP1() );
               pointsToTest.add( mLine.getP2() );
           }
       
           //Intersect with line A and B
           Line aLine = new Line(-1, A);
           Line bLine = new Line(-1, B);
           
           Point am = aLine.getIntersection(mLine);           
           Point bm = bLine.getIntersection(mLine);
       
           pointsToTest.add(am);
           pointsToTest.add(bm);
       }
       
       Collections.sort(pointsToTest, new Comparator<Point>() {

        @Override
        public int compare(Point o1, Point o2)
        {
            return ComparisonChain.start().compare(o2.getX(), o1.getX()).compare(o2.getY(), o1.getY()).result();
        }
           
       });
       
       for( Point point : pointsToTest)
       {
           long x = DoubleMath.roundToLong(point.getX(), RoundingMode.HALF_EVEN);
           long y = DoubleMath.roundToLong(point.getY(), RoundingMode.HALF_EVEN);
           boolean feasible = (A <= x+y && x+y <= B) && (C <= x-y && x-y <= D);
           
           if (feasible)
               return new Tile(x,y,false);
       
       }
       
       return null;
    }
    
    Tile findBestPointInRectangleContainingZero(long A, long B, long C, long D, 
            boolean centerXOdd, boolean centerYOdd) {
        if (!centerXOdd && !centerYOdd)
            return new Tile(0, 0, false);
        
        List<Pair<Long, Long>> pointsToTry = Lists.newArrayList();
        if (centerXOdd && centerYOdd) {
            pointsToTry.add( new ImmutablePair<>(1l,1l) );
            pointsToTry.add( new ImmutablePair<>(1l,-1l) );
            pointsToTry.add( new ImmutablePair<>(-1l,1l) );
            pointsToTry.add( new ImmutablePair<>(-1l,-1l) );            
        }
        
        if (centerXOdd && !centerYOdd) {
            pointsToTry.add( new ImmutablePair<>(1l,0l) );
            pointsToTry.add( new ImmutablePair<>(-1l,-1l) );
            pointsToTry.add( new ImmutablePair<>(3l,0l) );
            pointsToTry.add( new ImmutablePair<>(1l,2l) );
            pointsToTry.add( new ImmutablePair<>(1l,-2l) );
            pointsToTry.add( new ImmutablePair<>(-1l,2l) );
            pointsToTry.add( new ImmutablePair<>(-1l,-2l) );
            pointsToTry.add( new ImmutablePair<>(-3l,0l) );
        }
        
        if (!centerXOdd && centerYOdd) {
            pointsToTry.add( new ImmutablePair<>(0l,1l) );
            pointsToTry.add( new ImmutablePair<>(-1l,-1l) );
            
            pointsToTry.add( new ImmutablePair<>(2l,1l) );
            pointsToTry.add( new ImmutablePair<>(2l,-1l) );
            
            pointsToTry.add( new ImmutablePair<>(0l,3l) );
            pointsToTry.add( new ImmutablePair<>(0l,-3l) );
            
            pointsToTry.add( new ImmutablePair<>(-2l,1l) );            
            pointsToTry.add( new ImmutablePair<>(-2l,-1l) );
            
        }
        
        
        //TODO how can 3, 0 work when 1, 0 did not ??
        
        
        for(Pair<Long,Long> point : pointsToTry)
        {
            long x = point.getLeft();
            long y = point.getRight();
            boolean feasible = (A <= x+y && x+y <= B) && (C <= x-y && x-y <= D);
            
            if (feasible)
                return new Tile(x,y,false);
        
        }
      //None are feasible, rectangle does not contain any feasible points
        return null;
        
    }
    long g(long k, long l) {
        if ( (k >= 0 && l >= 0) || ( k < 0 && l < 0) )
            return Math.min(Math.abs(k), Math.abs(l));
        
        return 0;
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
