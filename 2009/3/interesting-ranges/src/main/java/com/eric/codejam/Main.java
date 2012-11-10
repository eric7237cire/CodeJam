package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Scanner;
import java.util.SortedMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    static PalinSpace palinSpace = new PalinSpace();

    static BigInteger roundUp(BigInteger someInt) {
        String s = someInt.subtract(BigInteger.ONE).toString();
        int n = Character.digit(s.charAt(0), 10);
        ++n;
        
        int exp = s.length() - 1;
        
        return BigInteger.valueOf(n).multiply(BigInteger.TEN.pow(exp));
    }
    
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
        if (num == 1) {
            if (exp == 0) {
                return BruteForce.createInterval(1, 1);
            }
            return getFullRange(10, exp-1);
        }
        Interval total = getRangeSlice(num, exp);

        for (int i = exp - 1; i >= 0; --i) {
            Interval next = getRangeSlice(10, i);
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
        /*if (num == 1) {
            exp --;
            num = 10;
        }*/
        Preconditions.checkArgument(exp >= 0);
        Preconditions.checkArgument(target.compareTo(BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp))) > 0 );
        Preconditions.checkArgument(target.compareTo(BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp+1))) <= 0 );
        /*if (exp < 1) {
            Preconditions.checkArgument(BigInteger.TEN.compareTo(target) > 0);
            return BruteForce.createInterval(1, target.intValue());
        }*/
        int totalPalinExp = exp / 2; // 9 * 10 ^ totalPalin

        BigInteger totalPerNum = BigInteger.TEN.pow(totalPalinExp);

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
        
                //numBetween 10  = 10 
                //1 0
                //2 -2
                //3 -4
                //4 -6
                //5 -8
                
                
       

        Interval total = new Interval();

        total = new Interval(BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp)).add(BigInteger.ONE));
        total.left = BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp)).add(BigInteger.ONE);
        total.right = total.left;
        
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
                    total = Interval.combin(total, Interval.createEmpty(target
                            .subtract(total.right).min(
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

            BigInteger t = BigInteger.ONE; // already added 1
            while (t.compareTo(totalPerNum) < 0) {
                
                BigInteger leftToGo = target.subtract(total.right);
                
                SortedMap<BigInteger, Interval> map = palinSpace.segments.get(exp).headMap(leftToGo.add(BigInteger.ONE));
                
                if (!map.isEmpty()) {
                    BigInteger chunk = palinSpace.segments.get(exp).headMap(leftToGo.add(BigInteger.ONE)).lastKey();
                    Interval chunkInterval =  palinSpace.segments.get(exp).get(chunk);
                
                    total = Interval.combin(total, chunkInterval);
                    Preconditions.checkState(BruteForce.isPalin(total.right));
                    t = t.add(chunkInterval.palinsCovered);
                    Preconditions.checkState(t.compareTo(BigInteger.ZERO) >= 0);
                    Preconditions.checkState(t.compareTo(total.palinsCovered) == 0);
                    
                    //Handle edge case...if palincsCovered < 10, then we are done.  Sometimes the diff
                    //between palin 9 and 10 is larger than palin 0 and 1
                    if (chunkInterval.palinsCovered.compareTo(BigInteger.TEN) < 0) {
                        total = Interval.combin(total, Interval.createEmpty(target.subtract(total.right)));
                        break;
                    }
                } else {
                    total = Interval.combin(total, Interval.createEmpty(target.subtract(total.right)));
                    break;
                }
                
                Preconditions.checkState(BruteForce.isPalin(total.right));
                /*
                
                if (t >= 100000 && t % 100000 == 0) {                   
                    total = Interval.combin(total, Interval.createEmpty(target.subtract(total.right).min(BigInteger.valueOf(numBetween_100000))));
                } else if (t >= 10000 && t % 10000 == 0) {
                    
                    total = Interval.combin(total, Interval.createEmpty(target.subtract(total.right).min(BigInteger.valueOf(numBetween_10000))));
                } else if (t >= 1000 && t % 1000 == 0) {
                    total = Interval.combin(total, Interval.createEmpty(target.subtract(total.right).min(BigInteger.valueOf(numBetween_1000))));
                    
 
                } else if (t >= 100 && t % 100 == 0) {
                    total = Interval.combin(total,
                            Interval.createEmpty(target.subtract(total.right).min(BigInteger.valueOf(numBetween_100))));
                } else if (t >= 10 && (t) % 10 == 0) { 
                                                       
                    total = Interval.combin(total, Interval.createEmpty( target.subtract(total.right).min(BigInteger.valueOf(numBetween_10))));
                    
                } else {
                    total = Interval.combin(total, Interval.createEmpty( target.subtract(total.right).min(BigInteger.valueOf(numBetween))));
                }
                if (total.right.compareTo(target) == 0) {
                    return total;
                }
                
                ++t;
                total = Interval.combin(total, palin);
                */
                
                
                if (total.right.compareTo(target) == 0) {
                    return total;
                }
            }

            // Add the empty space to get to next round #

            if (total.right.compareTo(target) == 0) {
                return total;
            } 
            
            BigInteger spaceNeeded = BigInteger.valueOf(n).add(BigInteger.ONE)
                    .multiply(BigInteger.TEN.pow(exp));
            spaceNeeded = spaceNeeded.subtract(total.right);

            total = Interval.combin(total, 
                    Interval.createEmpty(Math.min(spaceNeeded.intValue(),target.subtract(total.right).intValue())));

            if (total.right.compareTo(target) == 0) {
                return total;
            } 
        

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
        
        Interval total = new Interval();
        
        if (exp < 1) {
            for(int i = 1; i <= num && i <= 9; ++i) {
                total = Interval.combin(total, new Interval(1));
            }
            if (num == 10) {
                total = Interval.combin(total, new Interval(10));
            }
            return total;
          //  return BruteForce.createInterval(1, num);
        }
   

        

        for (int n = 1; n < num; ++n) {
            
            Interval partial = getPartialRange(n, exp, BigInteger.TEN.pow(exp).multiply(BigInteger.valueOf(n+1)));
            total = Interval.combin(total,
                    partial);
            
        }

        return total;

    }

    public static Interval calc(BigInteger numInt) {
        Preconditions.checkArgument(numInt.compareTo(BigInteger.ZERO) > 0);
        String num = numInt.toString();
        
        //BigInteger upBound = roundUp(numInt);
        //String upBoundStr = upBound.toString();
        int digit =  Character.digit(num.charAt(0), 10);
        int exp = num.length()-1;
        
        //4367
        
        //1 - 4000
        Interval regularInterval = Main.getFullRange(
                Character.digit(num.charAt(0), 10), num.length()-1);

        BigInteger leftOverInt = numInt.subtract(BigInteger.valueOf(digit).multiply(BigInteger.TEN.pow(exp)));
        
        Preconditions.checkState(leftOverInt.compareTo(BigInteger.ZERO) >= 0);
        
        if (leftOverInt.compareTo(BigInteger.ZERO) > 0) {
        Interval leftOver = getPartialRange(Character.digit(num.charAt(0), 10), num.length()-1, numInt);
        
        //
        
        //Preconditions.checkState(leftOver.compareTo(BigInteger.ZERO) >= 0);
        
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
        
        // BigInteger r = BruteForce.countTotal(m.L, m.R, true);
        os.println("Case #" + caseNumber + ": "
                + ans.totalEven.mod(BigInteger.valueOf(1000000007)));

        /*
         * os.println("1-9 " + BruteForce.countPalin(1, 9));
         * os.println(BruteForce.countPalin(10, 99)); os.println("100-999 " +
         * BruteForce.countPalin(100, 999));
         * os.println(BruteForce.countPalin(1000, 9999));
         * 
         * os.println(BruteForce.countPalin(100, 9999));
         * 
         * os.println(BruteForce.countPalin(10000, 99999));
         * os.println("100,000-999999 " + BruteForce.countPalin(100000,
         * 999999));
         * 
         * os.println(BruteForce.countPalin(1000000, 9999999));
         */
    }

    public Main() {

        // TODO Add test case vars
        super();
    }

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           // args = new String[] { "sample.txt" };
            args = new String[] { "D-small-practice.in" };
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