package codejam.y2012.round_final.xeno_archaeology;

import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
      //  return new String[] { "sample.in" };
       //  return new String[] { "C-small-practice.in" };
         return new String[] { "C-large-practice.in" };
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
    
    
    
    
    
    final long MAX_COORD = 1000000000000000l; 
    final long MAX_INTERCEPT = 2 *MAX_COORD + 10;
    

    /**
     * Follow given solution
     */
    public String handleCase(InputData in)
    {
        //String bf = bruteForce(in);
       // log.debug("Brute force {}", bf);
        
        /**
         * Center coordinate Cx, Cy can
         * either have an odd x or even ; similiar for y
         */
        
        
        List<Tile> bestPoints = Lists.newArrayList();
        
        

        for (int centerYOdd = 0; centerYOdd <= 1; ++centerYOdd)
        {
            centerXLoop:
            for (int centerXOdd = 0; centerXOdd <= 1; ++centerXOdd)
            {
                
                List<Long> posSlopeYIntercepts = Lists.newArrayList();
                List<Long> negSlopeYIntercepts = Lists.newArrayList();
                
                posSlopeYIntercepts.add(MAX_INTERCEPT);
                posSlopeYIntercepts.add(-MAX_INTERCEPT);
                negSlopeYIntercepts.add(MAX_INTERCEPT);
                negSlopeYIntercepts.add(-MAX_INTERCEPT);


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
                        continue centerXLoop;
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
                
                
                /**
                 * Now we go rectangle by rectangle, finding the best points
                 */
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
                         *                      
                         *                      x-y = -p1 (C or D)  ;  x-y = -p2 (C or D)
                         *                      x+y = n1 (A or B)   ;  x+y = n2 (A or B)
                         */
                        
                        
                        long p1 = posSlopeYIntercepts.get(p2Idx - 1);
                        long p2 = posSlopeYIntercepts.get(p2Idx);
                        
                        long n1 = negSlopeYIntercepts.get(n2Idx - 1);
                        long n2 = negSlopeYIntercepts.get(n2Idx);
                        
                        //log.debug("Rectangle bounded by y = x + {}, {} and y = -x + {}, {}", p1,p2,n1,n2);
                        
                        Line posSlope = new Line(1, p1);
                        Line negSlope = new Line(-1, n1);
                        
                        /**
                         * Find just any point in the rectangle in order to test if
                         * the interior of the rectangle is a valid solution. 
                         */
                        Point intersection = posSlope.getIntersection(negSlope);
                        
                        long cX = DoubleMath.roundToLong(intersection.x(), RoundingMode.HALF_DOWN);
                        long cY = 1 + DoubleMath.roundToLong(intersection.y(), RoundingMode.HALF_DOWN);
                        
                        long A, B, C, D;
                        
                        if ( Math.abs(n1) <= Math.abs(n2) ) {
                            A =  n1;
                            B = n2;
                        } else {
                            A = n2;
                            B = n1;
                        }
                        
                        if ( Math.abs(-p1) <= Math.abs(-p2) ) {
                            C = -p1;
                            D = -p2;                                    
                        } else {
                            C = -p2;
                            D = -p1;
                        }
                        
                        
                        
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
                        
                      // log.debug("Found a rectangle.  center x odd? {} center y odd? {} ", centerXOdd, centerYOdd);
                        
                       
                        
                        
                        Tile center = findBestPointInRectangle( A,B,C,D,
                                centerXOdd != 0,
                                centerYOdd != 0);
                                
                        if (center != null) {
                            bestPoints.add(center);
                        }
                    }
                }
                
            }
        }
        
       

        Collections.sort(bestPoints);
        
        if (bestPoints.size() > 0) {
            return String.format("Case #%d: %d %d", in.testCase,
                    bestPoints.get(0).getX(), bestPoints.get(0).getY());
            
        } else {
            return String.format("Case #%d: Too damaged", in.testCase);
            
        }
            
       // return bruteForce(in);
        
    }
    
    /**
     * 
     * A/B <= x + y <= A/B (negative slope lines)
     * C/D <= x - y <= C/D (positive slope lines
     * 
     * abs(A) <= abs(B)
     * abs(C) <= abs(D)
     */
    Tile findBestPointInRectangle(long A, long B, long C, long D, boolean centerXOdd, boolean centerYOdd) {
       long gAB = g(A,B);
       long gCD = g(C,D);
       
       
       //If Both A and B ; C and D switch signs that 0,0 is in the rectangle
       if (gAB == 0 && gCD == 0)
           return findBestPointInRectangleContainingZero(A,B,C,D,centerXOdd, centerYOdd);
       
       //Look at graph on desmos to see why
       long M = Math.max(g(A,B), g(C,D)) ;
       
       //Find the line that limits, and add one
       Preconditions.checkState(Math.abs(A) <= Math.abs(B));
       Preconditions.checkState(Math.abs(C) <= Math.abs(D));
       
       List<Tile> pointsToTest = Lists.newArrayList();
           
       /**
        * Here we see which constraint is the limiting factor.
        * We add 1 more since the boundary of the rectangle does not
        * have the good parity (it will either be
        * odd/odd ; even/even alternating or odd/even ; even/odd)
        */
       if (M == Math.abs(A)) {
           Line mLine = null;
           long A_plus1 = 0;
           
           if (A >= 0) {
               //Line is x+y = A+1 ( M+1)
               mLine = new Line( new Point(0, M+1), new Point(M+1, 0));
               pointsToTest.add( new Tile(0, M+1,false) );
               pointsToTest.add( new Tile(M+1, 0, false) );
               
               //In case x must be odd we add these to check
               pointsToTest.add( new Tile(1, M,false) );
               pointsToTest.add( new Tile(M, 1, false) );               
               
               A_plus1 = M+1;
           } else {
               //Line is x+y = -M - 1 (A - 1)
               mLine = new Line( new Point(0, -M-1), new Point(-M-1, 0));
               pointsToTest.add( new Tile(0, -M-1, false) );
               pointsToTest.add( new Tile(-1, -M, false) );
               pointsToTest.add( new Tile(-M-1, 0, false) );
               
               A_plus1 = -M-1;
           }
           
           //Intersect with line C and D
           Line cLine = new Line(1, -C);
           Line dLine = new Line(1, -D);
           
           Point cm = cLine.getIntersection(mLine);
           
           Point dm = dLine.getIntersection(mLine);
           
           double minX = Math.min(cm.x(), dm.x());
           double maxX = Math.max(cm.x(), dm.x());
           
           long intMinX = DoubleMath.roundToLong(minX, RoundingMode.UP);
           long intMaxX = DoubleMath.roundToLong(maxX,maxX >= 0 ? RoundingMode.DOWN : RoundingMode.UP);
           
           //Because the x's alternate (odd, even) the closest integer to the boundary
           //might need to be pulled back to make the point valid
           if ( (intMaxX % 2 == 0 && centerXOdd) ||
                   (intMaxX % 2 != 0 && !centerXOdd) ) {
                  intMaxX --;
              }
           
           pointsToTest.add( new Tile(intMinX, A_plus1-intMinX, false) );
           pointsToTest.add( new Tile(intMaxX, A_plus1-intMaxX, false) );
       } else {
           Preconditions.checkState(M == Math.abs(C));
           Line mLine = null;
           long C_plus1 = 0;
           if (C <= 0) {
               //Line is x-y = C - 1 (-M-1)
               mLine = new Line( new Point(-M-1, 0), new Point(0, M+1));

               pointsToTest.add( new Tile(-M-1, 0, false ) );
               pointsToTest.add( new Tile(0, M+1, false ) );
               pointsToTest.add( new Tile(-1, M, false ) );
               
               C_plus1 = -M - 1;
           } else {
               //Line is x-y = C + 1 (M+1)
               mLine = new Line( new Point(0, -M-1), new Point(M+1, 0));
               pointsToTest.add( new Tile(0, -M-1, false) );
               
               //M+1 may not match odd/even requirements for center
               pointsToTest.add( new Tile(M+1, 0, false) );
               pointsToTest.add( new Tile(M, -1, false) );
               
               C_plus1 = M + 1;
           }
           
           /**
            * x-y = C_plus1 is the line representing the manhattan distance
            * M+1
            * 
            * Intersect it with the edges of the rectangle to find
            * the point with the greatest X
            */
           
           //A line with positive slope alternates between pos,pos ; neg, neg or pos,neg ; neg, pos
           //  2, 3 ; 3, 4 ; 4 , 5 or  4, 8 ; 5, 9 ; 6, 10
       
           //Intersect with line A and B
           Line aLine = new Line(-1, A);
           Line bLine = new Line(-1, B);
           
           Point am = aLine.getIntersection(mLine);           
           Point bm = bLine.getIntersection(mLine);
       
           double minX = Math.min(am.x(), bm.x());
           double maxX = Math.max(am.x(), bm.x());
           
           long intMinX = DoubleMath.roundToLong(minX, RoundingMode.UP);
           long intMaxX =  DoubleMath.roundToLong(maxX, maxX >= 0 ? RoundingMode.DOWN : RoundingMode.UP)  ;
           
           if ( (intMaxX % 2 == 0 && centerXOdd) ||
                (intMaxX % 2 != 0 && !centerXOdd) ) {
               intMaxX --;
           }
                              
           pointsToTest.add( new Tile(intMinX, -C_plus1+intMinX, false) );
           pointsToTest.add( new Tile(intMaxX, -C_plus1+intMaxX, false) );
           
       }
       
       Collections.sort(pointsToTest);
       
       for( Tile point : pointsToTest)
       {
           long x = point.getX();
           long y = point.getY();
           
           //It is possible that some of the points don't match the odd/even requirements
           //Should be enough to test X
           
           if (x % 2 == 0 && centerXOdd)
               continue;
           
           if (x % 2 != 0 && !centerXOdd)
               continue;
           
           if (pointInRectangle(x,y,A,B,C,D))
               return point;       
       }
       
       return null;
    }
    
    private static boolean pointInRectangle(long x, long y, long A, long B, long C, long D) {
        boolean betAB = (A < B) ?
                (A <= x+y && x+y <= B) :
                    (B <= x+y && x+y <= A)
                    ;
                
        boolean betCD = (C < D) ?
                (C <= x-y && x-y <= D) :
                    (D <= x-y && x-y <= C);
                
         
        boolean feasible = betAB && betCD;
        
        if (feasible)
            return true;
        
        return false;
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
            pointsToTry.add( new ImmutablePair<>(-1l,0l) );
            pointsToTry.add( new ImmutablePair<>(1l,2l) );
            pointsToTry.add( new ImmutablePair<>(1l,-2l) );
            pointsToTry.add( new ImmutablePair<>(-1l,2l) );
            pointsToTry.add( new ImmutablePair<>(-1l,-2l) );
        }
        
        if (!centerXOdd && centerYOdd) {
            pointsToTry.add( new ImmutablePair<>(0l,1l) );
            pointsToTry.add( new ImmutablePair<>(0l,-1l) );
            
            pointsToTry.add( new ImmutablePair<>(2l,1l) );
            pointsToTry.add( new ImmutablePair<>(2l,-1l) );
            
            pointsToTry.add( new ImmutablePair<>(-2l,1l) );            
            pointsToTry.add( new ImmutablePair<>(-2l,-1l) );            
        }
        
        
        
        for(Pair<Long,Long> point : pointsToTry)
        {
            long x = point.getLeft();
            long y = point.getRight();
            if (pointInRectangle(x,y, A,B,C,D))
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
    
    
   

}
