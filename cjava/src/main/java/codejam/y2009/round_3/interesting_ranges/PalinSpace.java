package codejam.y2009.round_3.interesting_ranges;

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


    /*
     * Index of list is the exponent of 10.
     * 
     * so 0 goes 1-9
     * 1 --> 12-99
     * 2 --> 102-99
     * 3 --> 1002 - 99
     * 
     * The first palin is not counted in the map, neither is the last.
     * The main code handles the boundaries.
     * 
     *  The map contains all major chunks.  Ie for 
     *  7 --> 10 000 000  (total 999 palindromes)
     *    map (1...9 ==> their intervals
     *        (10..90, 99) ==> their intervals
     *        (100..900, 999) ==> their intervals
     * 
     * 
     */
    public List<SortedMap<BigInteger,Interval>> segments;

    PalinSpace() {
        segments = new ArrayList<>();
        for (int i = 0; i < 102; ++i) {
            segments.add(buildSegment(i));
        }

    }

    /*
     * Space between palindromes
     * exp is the 'level' 10^exp.  stepExp  10^stepExp.
     * 
     * calcNumBetween 7, 3 means the space between  the 1000th palindrome in n*10000000+n
     */
    public static BigInteger calcNumBetween(int exp, int stepExp) {
        if (stepExp == 0) {
            return exp % 2 == 0 ? new BigInteger(

            StringUtils.leftPad(StringUtils.repeat('9', exp / 2), 1, '0'), 10)
                    : new BigInteger("10" + StringUtils.repeat('9', exp / 2));
        }

        /*
1-9              (exp == 0)   -->   9
10-99            (exp == 1)   -->   10
100-999          (exp == 2)   -->   9 
1000-9999        (exp == 3)   -->   109
10000-99999      (exp == 4)   -->   99
100000-99..      (exp == 5)   -->   1099  
1000000-999      (exp == 6)   -->   999  
10000000-99      (exp == 7)   -->   10999 
100000000-99      (exp == 8)   -->  9999  
....          
*/
        
        int repeatCount = (exp - (2 * stepExp - 2)) / 2 - 1;

        Preconditions.checkState(repeatCount >= 0);

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
        BigInteger totalPalinCountPerLevel = BigInteger.valueOf(9).multiply(BigInteger.TEN.pow(exponent / 2)).subtract(BigInteger.ONE);

        BigInteger t = BigInteger.ZERO;
        int stepExp = 0;
        BigInteger empty = null;
        Interval emptyInt = null;
        Interval step = null;
        
        //First palin already 'added' (ie 1001 (exp 3), 50005 (exp 4)
        //total = palin;
        t = BigInteger.ZERO;
        empty = calcNumBetween(exponent, stepExp);
        emptyInt = Interval.createEmpty(empty);
   
        step =  emptyInt;
        step = Interval.combin(step, palin);
        
        while (t.compareTo(totalPalinCountPerLevel) < 0) {
        
            // t=1 ; 10; 100 ; etc. basically the first 'jump of 10'. T is the
            // palin count
            // Interval step = list.get(list.size() - 1);

            int upperBound = 8;
            
            //Very last one, we only do 7.  Last step is always 10
            if (emptyInt.size.equals(BigInteger.TEN) && exponent >= 2) {
                upperBound = 7;
            }
            for (int i = 0; i < upperBound; ++i) {
                
                currentInt = Interval.combin(currentInt, step);
                t = t.add(BigInteger.TEN.pow(stepExp));
                Preconditions.checkState(t.equals(currentInt.palinsCovered));
                sizeToInterval.put(currentInt.size, currentInt);        
                
                palinCountToInterval.put(currentInt.palinsCovered, currentInt);
                
            }
            
            if (t.compareTo(totalPalinCountPerLevel) == 0) {
                break;
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
                
                sizeToInterval.put(currentInt.size, currentInt);
                
                Preconditions.checkState(t.equals(currentInt.palinsCovered));
            }
            
            if (t.compareTo(totalPalinCountPerLevel) == 0) {
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
        }

        return sizeToInterval;

    }
}
