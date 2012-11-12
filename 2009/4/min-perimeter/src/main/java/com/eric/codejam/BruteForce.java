package com.eric.codejam;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.utils.CombinationIterator;
import com.google.common.base.Preconditions;


public class BruteForce {
    final static Logger log = LoggerFactory.getLogger(BruteForce.class);
    
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
            if (counter % 100 == 0) {
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
    
    static double  minPerim(List<PointInt> list) {
        Preconditions.checkArgument(list.size() <= 200);
        
        Iterator<Long> comIt = new CombinationIterator(list.size(), 3);
        
        double min = Double.MAX_VALUE;
        
        while(comIt.hasNext()) {
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
