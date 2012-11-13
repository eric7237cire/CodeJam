package com.eric.codejam;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.geometry.PointLong;
import com.eric.codejam.utils.CombinationIterator;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;
import com.google.common.math.DoubleMath;

public class BruteForce {
    final static Logger log = LoggerFactory.getLogger(BruteForce.class);

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

    static double minPerimUsingSort(List<PointLong> list) {

        SortedSet<PointLong> set = new TreeSet<>();

        
        TreeSet<PointLong> setY = new TreeSet<PointLong>(new CompareY());

        for (PointLong p : list) {
            set.add(p);
            setY.add(p);
        }

        double minPerimeter = 2l * Integer.MAX_VALUE;

        int count = 0;
        for (PointLong pointX : set) {
            long maxDist = 1 + DoubleMath.roundToLong(minPerimeter / 2d,
                    RoundingMode.DOWN);

            ++count;

            // SortedSet<PointLong> subSet = set.headSet(x).tailSet(new
            // PointLong(IntMath.checkedSubtract(x.getX(),maxDist), 0));
            SortedSet<PointLong> subSetX = set.headSet(pointX).tailSet(
                    new PointLong(pointX.getX() - maxDist, 0));

            /*
             * for(PointLong pointY : subSetX) { SortedSet<PointLong> subSetY =
             * setY.headSet(pointY).tailSet(new PointLong(0,
             * pointY.getY()-maxDist));
             * 
             * Set<PointLong> inter = Sets.intersection(subSetX, subSetY);
             * 
             * for(PointLong point3 : inter) { double perim =
             * pointX.distance(pointY) + pointX.distance(point3) +
             * pointY.distance(point3);
             * 
             * minPerimeter = Math.min(perim, minPerimeter); } }
             */

            SortedSet<PointLong> subSetY = setY.headSet(
                    new PointLong(0, pointX.getY() + maxDist)).tailSet(
                    new PointLong(0, pointX.getY() - maxDist));

            Set<PointLong> inter = Sets.intersection(subSetX, subSetY);
            // inter = subSet;

            if (inter.size() < 2) {
                continue;
            }

            List<PointLong> l = new ArrayList<PointLong>(inter);

            if (count % 1000 == 0)
                log.debug(
                        "x = {} MinX = {} minP {}.  Last x {} subset size {} count {}",
                        pointX, maxDist, minPerimeter, set.last(),
                        inter.size(), count);

            // log.debug("Checking set of size {}", l.size());
            double minP = minPerim(pointX, l, minPerimeter);
            if (minP < minPerimeter) {
                log.debug("New minimum {}", minP);
                minPerimeter = minP;
            }

            /*
             * if (count % 1000 == 0) log.debug(
             * "x = {} MinX = {} minP {}.  Last x {} subset size {} count {}",
             * point, maxDist, minPerimeter, set.last(), subSet.size(),count);
             */

            Preconditions.checkArgument(!Double.isNaN(minPerimeter));
        }

        return minPerimeter;
    }

   

    static double minPerim(PointLong p, List<PointLong> list, double min) {

        int count = 0;

        Collections.sort(list, new CompareY());
        
        double[] distP = new double[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            distP[i] = list.get(i).distance(p);
        }

        for (int i = 0; i < list.size(); ++i) {
            double oneSides = distP[i];
            
            if (oneSides * 2 >= min) {
                continue;
            }
            
            PointLong lowY = list.get(i);
            
            for (int j = i + 1; j < list.size(); ++j) {
                ++count;
                
                PointLong highY = list.get(j);
                
                Preconditions.checkState(highY.getY() >= lowY.getY());
                
                if (highY.getY() - lowY.getY() >= min / 2) {
                    continue;
                }

                double perim = oneSides + distP[j]
                        + lowY.distance(highY);

                if (perim < min) {
                    log.debug("Min {}", perim);
                    min = Math.min(perim, min);
                }

            }
        }

        return min;
    }

    static double minPerim(List<PointLong> list) {
        Preconditions.checkArgument(list.size() <= 62);

        Iterator<Long> comIt = new CombinationIterator(list.size(), 3);

        double min = Double.MAX_VALUE;

        int count = 0;

        while (comIt.hasNext()) {
            ++count;
            if (count % 1000000 == 0)
                log.debug("Count {}", count);
            Long com = comIt.next();

            PointLong[] arr = new PointLong[3];
            for (int i = 0; i < 3; ++i) {
                long bit = Long.highestOneBit(com);
                int index = Long.numberOfTrailingZeros(bit);
                PointLong p = list.get(index);
                com = com & ~bit;
                arr[i] = p;
            }

            double area = arr[0].distance(arr[1]) + arr[0].distance(arr[2])
                    + arr[1].distance(arr[2]);
            min = Math.min(area, min);
        }

        return min;
    }

}
