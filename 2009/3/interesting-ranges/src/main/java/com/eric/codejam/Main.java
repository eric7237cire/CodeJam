package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    static PalinSpace palinSpace = new PalinSpace();

    /**
     * 1 to num*10^exp f(3, 4) = 1 - 30000
     * 
     * Example sum 10001-30000 1001-10000 101 - 1000 11 - 100 1 - 10
     * 
     * @param num
     * @param exp
     * @return
     */
    static Interval getFullRange(int num, final int exp) {
        
        if (num == 1 && exp ==0 ){
            return new Interval(1);
        }
        Interval total = num > 1 ?
                getPartialRange(1, exp, BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp))) 
                : new Interval();

        for (int i = exp - 1; i >= 0; --i) {
            Interval next 
            = getPartialRange(1, i, BigInteger.valueOf(10).multiply(BigInteger.TEN.pow(i)));
            total = Interval.combin(next, total);
        }

        return total;
    }
    
    /**
     * num*10 ^ exp + 1 to target = 10001 - ????
     * @param num
     * @param exp
     * @return
     */
    static Interval getPartialRange(int num, int exp, BigInteger target) {
        
        Preconditions.checkArgument(exp >= 0);
        Preconditions.checkArgument(target.compareTo(BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp))) >= 0 );
        Preconditions.checkArgument(target.compareTo(BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp+1))) <= 0 );
        
        

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
....             (exp == 9)   -->   109999
                 (exp == 10)   -->  99999
                (exp == 11)   -->   1099999  
  
100000-999999  ; 900   ; 1099 ; 109
1000000-999999 ; 9000  ; 999  ; 1099
10000000       ; 9000  ; 10999 ; 1099
100000000      ; 90000 ; 9999  ; 10999
1000000000      ; 90000
10000000000     ; 900000
100000000000   ; 900000
1000000000000  ; 9000000
10000000000000  ; 9000000
*/
        

        Interval total = new Interval();

        total = new Interval(BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp)).add(BigInteger.ONE));
        total.left = BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp)).add(BigInteger.ONE);
        total.right = total.left;
        
        if (exp == 0) {
            total.left = BigInteger.ONE;
            total.right = BigInteger.ONE;
        }
        
        if (total.right.compareTo(target) == 0) {
            return total;
        } 

        Interval palin = new Interval(1);

        int n = num;

        if (n == 1) {

            Preconditions.checkState(total.left.compareTo(target) <= 0);
            if (total.right.compareTo(target) == 0) {
                return total;
            }
        } else {
            if (n > 2) {
                // Get to first palin 1001 / 3003 / 9009 etc
                total = Interval.combin(
                        total,
                        Interval.createEmpty(target.subtract(total.right).min(
                                BigInteger.valueOf(n - 2))));

                if (total.right.compareTo(target) == 0) {
                    return total;
                }
            }

            // Add first palin (palin 0)
            total = Interval.combin(total, palin);

            if (total.right.compareTo(target) == 0) {
                return total;
            }
        }

        BigInteger leftToGo = target.subtract(total.right);
        BigInteger t = BigInteger.ONE; // already added 1
        while (leftToGo.compareTo(BigInteger.ZERO) > 0) {

            SortedMap<BigInteger, Interval> map = palinSpace.segments.get(exp)
                    .headMap(leftToGo.add(BigInteger.ONE));

            if (!map.isEmpty()) {
                BigInteger chunk = palinSpace.segments.get(exp)
                        .headMap(leftToGo.add(BigInteger.ONE)).lastKey();
                Interval chunkInterval = palinSpace.segments.get(exp)
                        .get(chunk);

                total = Interval.combin(total, chunkInterval);
                Preconditions.checkState(BruteForce.isPalin(total.right));
                t = t.add(chunkInterval.palinsCovered);
                Preconditions.checkState(t.compareTo(BigInteger.ZERO) >= 0);
                Preconditions.checkState(t.compareTo(total.palinsCovered) == 0);

                // Handle edge case...if palincsCovered < 10, then we are done.
                // Sometimes the diff
                // between palin 9 and 10 is larger than palin 0 and 1
                if (chunkInterval.palinsCovered.compareTo(BigInteger.TEN) < 0) {
                    total = Interval.combin(total,
                            Interval.createEmpty(target.subtract(total.right)));
                    break;
                }
            } else {
                total = Interval.combin(total,
                        Interval.createEmpty(target.subtract(total.right)));
                break;
            }

            Preconditions.checkState(BruteForce.isPalin(total.right));

            if (total.right.compareTo(target) == 0) {
                return total;
            }
            
            leftToGo = target.subtract(total.right);
        }

        Preconditions.checkState(total.right.compareTo(target) == 0);

        return total;
    }

    /**
     * 10 ^ exp + 1 to num*10^exp f(3, 4) = 10001 - 30000
     * 
     * @param num
     * @param exp
     * @return
     */
    static Interval getRangeSlice(int num, final int exp) {
       
        if (num == 1 && exp > 0) {
            return getRangeSlice(10, exp - 1);
        }
        
        Interval total = getPartialRange(1, exp, BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp)));

        return total;

    }

    public static Interval calc(BigInteger numInt) {
        Preconditions.checkArgument(numInt.compareTo(BigInteger.ZERO) > 0);
        String num = numInt.toString();

        int digit = Character.digit(num.charAt(0), 10);
        int exp = num.length() - 1;

        // 4367

        // 1 - 4000
        Interval regularInterval = Main.getFullRange(
                Character.digit(num.charAt(0), 10), num.length() - 1);

        BigInteger leftOverInt = numInt.subtract(BigInteger.valueOf(digit)
                .multiply(BigInteger.TEN.pow(exp)));

        Preconditions.checkState(leftOverInt.compareTo(BigInteger.ZERO) >= 0);

        if (leftOverInt.compareTo(BigInteger.ZERO) > 0) {
            Interval leftOver = getPartialRange(
                    Character.digit(num.charAt(0), 10), num.length() - 1,
                    numInt);

            Interval total = Interval.combin(regularInterval, leftOver);

            return total;
        } else {
            return regularInterval;
        }
    }
    
    public static Interval calcEvenPairRanges(BigInteger L, BigInteger R) {
        Interval ans = null;
        Interval ri = calc(R);
                
        if (L.compareTo(BigInteger.ONE) > 0) {
            Interval li = calc(L.subtract(BigInteger.ONE));        
            ans = Interval.subtract(li, ri);
        } else {
            ans = ri;
        }
        
        return ans;
    }

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        log.info("Starting case {}", caseNumber);

        BigInteger L = scanner.nextBigInteger();
        BigInteger R = scanner.nextBigInteger();
        
        Interval ans = calcEvenPairRanges(L, R);
        
        os.println("Case #" + caseNumber + ": "
                + ans.totalEven.mod(BigInteger.valueOf(1000000007)));

       
    }

    public Main() {

        // TODO Add test case vars
        super();
    }

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
           // args = new String[] { "D-small-practice.in" };
        }
        log.info("Input file {}", args[0]);

        Scanner scanner = new Scanner(new File(args[0]));

        int t = scanner.nextInt();

        for (int i = 1; i <= t; ++i) {

            handleCase(i, scanner, System.out);

        }

        scanner.close();
    }
}