package com.eric.codejam;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

public class PalinSpace {

    // First dimension = exponent of 10. 10^0 until 10^50
    // Second follows pattern for palinsCovered --
    /*
     * dist 1 to 2 ; 1 to 3;..... 1 to 9
     *  1 to 10, 20, 30, 40, 50, 60, 70, 80, 90,
     *   100,
     *   200, ... 1000, ...
     * 1e50
     * 
     * 
     * Segments start and end at palindroms so seg of exp 0 and 1 are not
     * interesting because each level only has 1 seg of exp 0 1 to 1 seg of exp
     * 1 equals 11 or 22 or 33 seg of exp 2 equals 101 - 191 or 202 - 292 etc
     * seg of exp = 3 (equals 1001 - 1991 or 2002 - 1992 etc
     */
    List<SortedMap<BigInteger,Interval>> segments;

    PalinSpace() {
        segments = new ArrayList<>();
        for (int i = 0; i < 15; ++i) {
            segments.add(buildSegment(i));
        }

    }

    public static BigInteger calcNumBetween(int exp, int stepExp) {
        if (stepExp == 0) {
            return exp % 2 == 0 ? new BigInteger(

            StringUtils.leftPad(StringUtils.repeat('9', exp / 2), 1, '0'), 10)
                    : new BigInteger("10" + StringUtils.repeat('9', exp / 2));
        }

        int repeatCount = (exp - (2 * stepExp - 2)) / 2 - 1;

        Preconditions.checkState(repeatCount > 0);

        return new BigInteger("10" + StringUtils.repeat('9', repeatCount));

    }

    private SortedMap<BigInteger, Interval> buildSegment(int exponent) {
        Map<BigInteger, Interval> palinCountToInterval = new HashMap<>();
        SortedMap<BigInteger, Interval> sizeToInterval = new TreeMap<>();
        Interval palin = new Interval(1);
        palin.left = BigInteger.ONE;
        palin.right = BigInteger.ONE;
        
        Interval currentInt = Interval.createEmpty(BigInteger.ZERO);

        //Not counting first one
        BigInteger totalPalinCount = BigInteger.TEN.pow(exponent / 2).subtract(BigInteger.ONE);

        BigInteger t = BigInteger.ZERO;
        int stepExp = 0;

        BigInteger empty = null;// calcNumBetween(exponent, stepExp);

        Interval emptyInt = null; // Interval.createEmpty(empty);
        Interval step = null;// Interval.combin(emptyInt, palin);
        
        //First palin already 'added' (ie 1001 (exp 3), 50005 (exp 4)
        //total = palin;
        t = BigInteger.ZERO;
        empty = calcNumBetween(exponent, stepExp);
        emptyInt = Interval.createEmpty(empty);
   
        step =  emptyInt;
        step = Interval.combin(step, palin);
        
        while (t.compareTo(totalPalinCount) < 0) {

        
            // t=1 ; 10; 100 ; etc. basically the first 'jump of 10'. T is the
            // palin count
            // Interval step = list.get(list.size() - 1);

            for (int i = 0; i < 8; ++i) {
                
                currentInt = Interval.combin(currentInt, step);
                t = t.add(BigInteger.TEN.pow(stepExp));
                Preconditions.checkState(t.equals(currentInt.palinsCovered));
                sizeToInterval.put(currentInt.size, currentInt);        
                
                palinCountToInterval.put(currentInt.palinsCovered, currentInt);
                
            }
            
            if (stepExp == 0) {
                //just add another normally
                currentInt = Interval.combin(currentInt, step);
                t = t.add(BigInteger.TEN.pow(stepExp));
                Preconditions.checkState(t.equals(currentInt.palinsCovered));
                sizeToInterval.put(currentInt.size, currentInt);
                
                palinCountToInterval.put(currentInt.palinsCovered, currentInt);
                
            } else {
                //add all the 1 to 9 ; 1 to 90 ; etc to get a 99999
                //stepExp == 2 means skipping 100 at a time
                //add 90 and 9
                for (int stepFactor = stepExp; stepFactor >= 1; -- stepFactor) {
                    currentInt = Interval.combin(currentInt, 
                            palinCountToInterval.get(BigInteger.TEN.pow(stepFactor-1).multiply(BigInteger.valueOf(9))));
                }
                
                t = t.add(BigInteger.TEN.pow(stepExp).subtract(BigInteger.ONE));
                
                Preconditions.checkState(t.equals(currentInt.palinsCovered));
            }
            
            if (t.compareTo(totalPalinCount) == 0) {
                break;
            }

            // Add the step change
            stepExp++;

            //Add new increased empty space
            empty = calcNumBetween(exponent, stepExp);
            emptyInt = Interval.createEmpty(empty);
            currentInt = Interval.combin(currentInt, emptyInt);
            
            //Add palin at the end
            currentInt = Interval.combin(currentInt, palin);
            t = t.add(BigInteger.ONE);
            step = currentInt;
            
            sizeToInterval.put(currentInt.size, currentInt);

            //Preconditions.checkState(t.equals(BigInteger.TEN.pow(stepExp)));

        }

        return sizeToInterval;

    }
}
