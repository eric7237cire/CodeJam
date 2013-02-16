package codejam.y2009.round_final.min_perimeter;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.PointInt;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.math.DoubleMath;

public class DivideConq {
    private List<PointInt> list;
    
    static CompareX compX = new CompareX();
    static CompareY compY = new CompareY();
    
    final static Logger log = LoggerFactory.getLogger(DivideConq.class);
    
    public DivideConq(List<PointInt> list) {
        super();
        this.list = list;
        Collections.sort(this.list, new CompareX());        
    }

    public static double findMinPerimTriangle(List<PointInt> list) {
        DivideConq dq = new DivideConq(list);
        List<PointInt> listSortedY = new ArrayList<>(list);
        Collections.sort(listSortedY, compY);
        
        return dq.findMinPerim(0, list.size()-1, listSortedY);
    }
    
    public double bruteForceSmall(List<PointInt> listPointsSortedY) {
        double min = Double.MAX_VALUE;
        for(int i = 0; i < listPointsSortedY.size(); ++i) {
            PointInt pI = listPointsSortedY.get(i);
            
            for(int j = i+1; j < listPointsSortedY.size(); ++j) {
                
                PointInt pj = listPointsSortedY.get(j);
                double ij = pI.distance(pj);
                
                for(int k = j+1; k < listPointsSortedY.size(); ++k) {
                    PointInt pk = listPointsSortedY.get(k);
                    double ik = pk.distance(pI);
                    double jk = pk.distance(pj);
                    
                    double perim = ij + ik + jk;
                    min = Math.min(min, perim);
                }
            }
        }
        return min;
    }
    
    /**
     * Because we are partitioning by X and avoiding another sort,
     * leftIndex and rightIndex 
     * @param leftIndex
     * @param rightIndex
     * @param listPointsSortedY
     * @return
     */
    public double findMinPerim(int leftIndex, int rightIndex, List<PointInt> listPointsSortedY) {
        final int numberOfPoints = rightIndex - leftIndex + 1;
        Preconditions.checkArgument(listPointsSortedY.size() == numberOfPoints);
        
        if (numberOfPoints < 3) {
            return Double.MAX_VALUE;
        }
        
        if (numberOfPoints < 9) {
            return bruteForceSmall(listPointsSortedY);
        }
        
        int leftHalfEndIndex = leftIndex + numberOfPoints / 2 - 1;
        int rightHalfStartIndex = leftHalfEndIndex + 1;
        
        Preconditions.checkState(rightHalfStartIndex <= rightIndex);
        
        double min = Double.MAX_VALUE;
        
        /*
         * In order to avoid having to do another sort by y, (taking n log n), we just split using the sortedY list
         */
        List<PointInt> leftList = new ArrayList<>(leftHalfEndIndex - leftIndex + 1);
        List<PointInt> rightList = new ArrayList<>(rightIndex - rightHalfStartIndex + 1);
        
        //We must compare x and y because though we are partitioning by a vertical line, we must also be able to partition if all points have the same x
        //Because the partition is strictly less and due to rounding error from longs,
        //we use this as the division point
        PointInt midPoint = list.get(rightHalfStartIndex);
       
        
        for(PointInt pl : listPointsSortedY) {
            //Verify the points are still between left and right index
            Preconditions.checkState(list.get(leftIndex).x() <= pl.x());
            Preconditions.checkState(list.get(rightIndex).x() >= pl.x());
            
            if (compX.compare(pl, midPoint) < 0) {
                leftList.add(pl);
            } else {
                rightList.add(pl);
            }
        }
        
        Preconditions.checkState(leftList.size() == numberOfPoints / 2);
        Preconditions.checkState(leftList.size() > 0);
        Preconditions.checkState(rightList.size() > 0);
        
        Preconditions.checkState(leftHalfEndIndex != rightIndex);
        Preconditions.checkState(rightHalfStartIndex != leftIndex);
        
        Preconditions.checkState(numberOfPoints == leftList.size() + rightList.size());
        
        //Divide
        double minLeft = findMinPerim(leftIndex, leftHalfEndIndex, leftList);
        double minRight = findMinPerim(rightHalfStartIndex, rightIndex, rightList);
        min = Math.min(minLeft,minRight);
        
        /*
         * For the combine, we make a box.  
         * Left bound is min / 2 from vertical line, right bound is min / 2.
         * 
         * Reason being that any triangle covering the vertical line and with a 
         * point greater than min / 2 would have a perimeter > min.
         */
        
        int boxMargin = DoubleMath.roundToInt(  (min > Double.MAX_VALUE /2 ? Integer.MAX_VALUE : min) / 2d, RoundingMode.UP );
        Preconditions.checkState(min > Integer.MAX_VALUE || boxMargin < min);
        Preconditions.checkState(min > Integer.MAX_VALUE || boxMargin * 2 >= min);
        
        List<PointInt> boxPoints = new ArrayList<>();
        int startBox = 0;
        for(int i = 0; i < listPointsSortedY.size(); ++i) {
            PointInt point = listPointsSortedY.get(i);
            
            if (Math.abs(point.x() - midPoint.x()) > boxMargin) {
                continue;
            }
            
            /**
             * Calculate start of the box to consider.  Should be at most
             * boxMargin away from currenty point.  End of box is the last point added
             * 
             * Basically, while the box is too low, move it up
             */
            while(startBox < boxPoints.size() && point.y() - boxPoints.get(startBox).y() > boxMargin) {
                startBox++;
            }
            
            /**
             * To explain this, the box goes from -minP / 2 to + minP / 2 from the
             * dividing vertical line.  Its height is also minP / 2.
             * 
             * Because we have the minimum with respect to the left and right
             * sides, all triangles to the left and right have max perimiter <= minP.
             * 
             * The only way to cram 16 points is to have 2 points on each corner and in the middle
             * 
             *  PP-------PP--------PP
             *  
             *       PP       PP
             *       
             *  PP-------PP--------PP
             *  
             *  The perim of any triangle is >= minP.  Any other point would be 
             *  a triangle of perim < minP.  
             */
            Preconditions.checkState(boxPoints.size() - startBox <= 16);
            
            //Consider all points in box (can be proved <= 16...Similar to most 6 for closest 2 points algorithm
            for(int bi=startBox; bi<boxPoints.size(); ++bi) {
                for(int j=bi+1; j<boxPoints.size(); ++j) {
                  double perim = point.distance(boxPoints.get(bi)) +
                          point.distance(boxPoints.get(j)) +
                                   boxPoints.get(bi).distance(boxPoints.get(j));
                  
                  min = Math.min(min, perim);
                }
              }
            
            boxPoints.add(point);
        }
        
        return min;
        
    }

    private static class CompareY implements Comparator<PointInt> {

        @Override
        public int compare(PointInt o1, PointInt o2) {
            return ComparisonChain.start().compare(o1.y(), o2.y())
                    .compare(o1.x(), o2.x()).result();
        }

    };

    private static class CompareX implements Comparator<PointInt> {

        @Override
        public int compare(PointInt o1, PointInt o2) {
            return ComparisonChain.start().compare(o1.x(), o2.x())
                    .compare(o1.y(), o2.y()).result();
        }

    };
}
