package com.eric.codejam;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.geometry.PointLong;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.math.DoubleMath;

public class DivideConq {
    List<PointLong> list;
    SortedSet<PointLong> setX;
    SortedSet<PointLong> setY;
    
    final static Logger log = LoggerFactory.getLogger(DivideConq.class);
    
    public DivideConq(List<PointLong> list) {
        super();
        this.list = list;
        Collections.sort(this.list, new CompareX());
        setX = new TreeSet<PointLong>(new CompareX());
        setY = new TreeSet<PointLong>(new CompareY());
        
        setX.addAll(list);
        setY.addAll(list);
    }

    public static double findMinPerimTriangle(List<PointLong> list) {
        DivideConq dq = new DivideConq(list);
        List<PointLong> listSortedY = new ArrayList<>(list);
        Collections.sort(listSortedY, new CompareY());
        
        return dq.findMinPerim(0, list.size()-1, listSortedY);
    }
    
    public double findMinPerim(int leftIndex, int rightIndex, List<PointLong> listPointsSortedY) {
        int numberOfPoints = rightIndex - leftIndex + 1;
        Preconditions.checkArgument(listPointsSortedY.size() == numberOfPoints);
        
        if (numberOfPoints < 3) {
            return Double.MAX_VALUE;
        }
        
        int leftHalfEndIndex = leftIndex + numberOfPoints / 2 - 1;
        int rightHalfStartIndex = leftHalfEndIndex + 1;
        
        Preconditions.checkState(rightHalfStartIndex <= rightIndex);
        
        double min = Double.MAX_VALUE;
        
        /*
         * In order to avoid having to do another sort by y, (taking n log n), we just split using the sortedY list
         */
        List<PointLong> leftList = new ArrayList<>();
        List<PointLong> rightList = new ArrayList<>();
        
        //We must compare x and y because though we are partitioning by a vertical line, we must also be able to partition if all points have the same x
        //PointLong midPoint = PointLong.midPoint(list.get(leftHalfEndIndex), list.get(rightHalfStartIndex));
        PointLong midPoint = list.get(rightHalfStartIndex);
       
        
        for(PointLong pl : listPointsSortedY) {
            //Verify the points are still between left and right index
            Preconditions.checkState(list.get(leftIndex).getX() <= pl.getX());
            Preconditions.checkState(list.get(rightIndex).getX() >= pl.getX());
            
            if (new CompareX().compare(pl, midPoint) < 0) {
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
        
        //Correct the indexes
        //leftHalfEndIndex = leftIndex + leftList.size() - 1;
       // rightHalfStartIndex = leftHalfEndIndex + 1;
        Preconditions.checkState(numberOfPoints == leftList.size() + rightList.size());

        if (midPoint.getX() >= 4000 && midPoint.getX() < 5000) {
            log.debug("Mid point is {}", midPoint);
        }
        
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
        
        List<PointLong> boxPoints = new ArrayList<>();
        int startBox = 0;
        for(int i = 0; i < listPointsSortedY.size(); ++i) {
            PointLong point = listPointsSortedY.get(i);
            
            if (point.getX() == 4274 ) {
                log.debug("p {}", point);
            }
            if (Math.abs(point.getX() - midPoint.getX()) > boxMargin) {
                continue;
            }
            
            //Calculate start of the box to consider.  Should be at most
            //boxMargin away from currenty point.  End of box is the last point added
            while(startBox < boxPoints.size() && point.getY() - boxPoints.get(startBox).getY() > boxMargin) {
                startBox++;
            }
            
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
    
    private static class CompareY implements Comparator<PointLong> {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object,
         * java.lang.Object)
         */
        @Override
        public int compare(PointLong o1, PointLong o2) {
            return ComparisonChain.start().compare(o1.getY(), o2.getY())
                    .compare(o1.getX(), o2.getX()).result();
        }

    };
    
    private static class CompareX implements Comparator<PointLong> {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object,
         * java.lang.Object)
         */
        @Override
        public int compare(PointLong o1, PointLong o2) {
            return ComparisonChain.start().compare(o1.getX(), o2.getX())
                    .compare(o1.getY(), o2.getY()).result();
        }

    };
}
