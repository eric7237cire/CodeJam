package com.eric.codejam;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.utils.CombinationIterator;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;
import com.google.common.math.DoubleMath;


public class BruteForce {
    final static Logger log = LoggerFactory.getLogger(BruteForce.class);
    
    static double  minPerimUsingSort(List<PointInt> list) {
        
        SortedSet<PointInt> set = new TreeSet<>(); 
        
        Comparator<PointInt> c = new Comparator<PointInt>() {

            /* (non-Javadoc)
             * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
             */
            @Override
            public int compare(PointInt o1, PointInt o2) {
                return ComparisonChain.start().compare(o1.getY(), o2.getY()).compare(o1.getX(), o2.getX()).result();
            }
            
        };
        
        TreeSet<PointInt> setY = new TreeSet<PointInt>(c);
        
        
        for(PointInt p : list) {
            set.add(p);
            setY.add(p);
        }
        
        double minPerimeter = Integer.MAX_VALUE;
        
        int count = 0;
        for(PointInt x : set) {
            int maxDist =  1+DoubleMath.roundToInt(minPerimeter / 2d, RoundingMode.DOWN);
            
            ++count;
            
            //SortedSet<PointInt> subSet = set.headSet(x).tailSet(new PointInt(x.getX()-maxDist, 0));
            SortedSet<PointInt> subSet = set.headSet(x); //.tailSet(new PointInt(x.getX()-maxDist, 0));
            
            
            TreeSet<PointInt> subSetY_a = (TreeSet) setY.headSet(new PointInt(0, x.getY()+maxDist),true);
            SortedSet<PointInt> subSetY = subSetY_a.tailSet(new PointInt(0, x.getY()-maxDist), true);
            
            Set<PointInt> inter = Sets.intersection(subSet, subSetY);
           // inter = subSet;
            
            if (inter.size() < 2) {
                continue;
            }
            
            List<PointInt> l = new ArrayList<PointInt>(inter);
            
            if (count % 1000 == 0)
                log.debug("x = {} MinX = {} minP {}.  Last x {} subset size {} count {}", x, maxDist, minPerimeter, set.last(), subSet.size(),count);
                
                        
            //log.debug("Checking set of size {}", l.size());
            double minP =   minPerim(x, l);
            if (minP < minPerimeter) {
                log.debug("New minimum {}", minP);
                minPerimeter = minP;
            }
            
            Preconditions.checkArgument(!Double.isNaN(minPerimeter ));
        }
        
        return minPerimeter;
    }
    static double  minPerimUsingDiff(List<PointInt> list) {
        log.info("Starting");
        //x y are indices to pointx and pointy
        SortedMap<Double, PointInt> diffsMap = new TreeMap<>();
        
        for(int i = 0; i < list.size(); ++i) {
            log.info("I {} of {}", i, list.size());
            for(int j = i+1; j < list.size(); ++j) {
                PointInt x = list.get(i);
                PointInt y = list.get(j);
                double diff = x.distance(y);
                diffsMap.put(diff, new PointInt(i,j));
            }
        }
        
        double minPerim = Double.MAX_VALUE;
        
        Iterator<Map.Entry<Double,PointInt>> it = diffsMap.entrySet().iterator();
        int counter = 0;
        
        while(it.hasNext()) {
            ++counter;
            if (counter % 10000 == 0) {
                log.info("Counter is {}.  Min is {}", counter, minPerim);
            }
            Map.Entry<Double,PointInt> entry = it.next();
            if (entry.getKey() * 2 >= minPerim) {
                return minPerim;
            }
            
            int point1 = entry.getValue().getX();
            int point2 = entry.getValue().getY();
            for(int i = 0; i < list.size(); ++i) {
                if (i == entry.getValue().getX() || i == entry.getValue().getY()) {
                    continue;
                }
                
                double area = list.get(point1).distance(list.get(i)) + list.get(point2).distance(list.get(i)) + entry.getKey(); 
                minPerim = Math.min(area, minPerim);
            }
        }
        
        return minPerim;
    }
    
    static double  minPerim(PointInt p, List<PointInt> list) {
        
        
        double min = Double.MAX_VALUE;
        
        int count = 0;
        
        for(int i = 0; i < list.size(); ++i) {
            for(int j = i + 1; j < list.size(); ++j) {
                ++count;
            
                double area = list.get(i).distance(p) 
                        + list.get(j).distance(p) 
                        + list.get(i).distance(list.get(j));
                
                min = Math.min(area, min);
                
            }
        }
        
       
        return min;
    }
    
    static double  minPerim(List<PointInt> list) {
        Preconditions.checkArgument(list.size() <= 62);
        
        Iterator<Long> comIt = new CombinationIterator(list.size(), 3);
        
        double min = Double.MAX_VALUE;
        
        int count = 0;
        
        while(comIt.hasNext()) {
            ++count;
            if (count % 1000000 ==0) 
                log.debug("Count {}", count);
            Long com =  comIt.next();
            
            PointInt[] arr = new PointInt[3];
            for(int i = 0; i < 3; ++i) {
                long bit = Long.highestOneBit(com);
                int index = Long.numberOfTrailingZeros( bit );
                PointInt p = list.get(index);
                com = com & ~bit;
                arr[i] = p;
            }
            
            double area = arr[0].distance(arr[1]) + arr[0].distance(arr[2]) + arr[1].distance(arr[2]);
            min = Math.min(area, min);
        }
        
        return min;
    }
    
    
}
