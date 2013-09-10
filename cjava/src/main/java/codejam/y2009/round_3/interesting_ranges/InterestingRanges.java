package codejam.y2009.round_3.interesting_ranges;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.SortedMap;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;

public class InterestingRanges extends InputFilesHandler 
implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {
    
    
    
    public InterestingRanges()
    {
        super("D", 1, 1);
        //setLogInfo();
        
    }

    public static PalinSpace palinSpace = new PalinSpace();

    /**
     * 1 to num*10^exp f(3, 4) = 1 - 30000
     * 
     * Example sum 10001-30000 1001-10000 101 - 1000 11 - 100 1 - 10
     * 
     * @param num
     * @param exp
     * @return
     */
    public static Interval getFullRange(final int num, final int exp) {
        
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
    public static Interval getPartialRange(final int num, final int exp, BigInteger target) {
        
        Preconditions.checkArgument(exp >= 0);
        Preconditions.checkArgument(target.compareTo(BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp))) >= 0 );
        Preconditions.checkArgument(target.compareTo(BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp+1))) <= 0 );
        
       
        Interval total = new Interval();

        total = new Interval(BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp)).add(BigInteger.ONE));
                
        if (exp == 0) {
            total.left = BigInteger.ONE;
            total.right = BigInteger.ONE;
        } else {
            total.left = BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp)).add(BigInteger.ONE);
            total.right = total.left;
        }
        
        if (total.right.compareTo(target) == 0) {
            return total;
        } 

        Interval palin = new Interval(1);

        
        if (num == 1) {

            Preconditions.checkState(total.left.compareTo(target) <= 0);
            
            //Si l'intervalle total contient la cible désirée, nous sommes fini
            if (total.right.compareTo(target) == 0) {
                return total;
            }
        } else {
            if (num > 2) {
                // Get to first palin 1001 / 3003 / 9009 etc
                
                //si num est 9 alors la taille de l'intervalle est égale à 8 (1-8)
                //mais la taille ne peut pas surpasser le cible
                // target - num*10 ^ exp + 1
                total = Interval.combin(
                        total,
                        Interval.createEmpty(target.subtract(total.right).min(
                                BigInteger.valueOf(num - 2))));

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
                BigInteger chunk = map.lastKey();
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
    public static Interval getRangeSlice(int num, final int exp) {
       
        if (num == 1 && exp > 0) {
            return getRangeSlice(10, exp - 1);
        }
        
        Interval total = getPartialRange(1, exp, BigInteger.valueOf(num).multiply(BigInteger.TEN.pow(exp)));

        return total;

    }

    public static Interval calc(BigInteger numInt) 
    {
        Preconditions.checkArgument(numInt.compareTo(BigInteger.ZERO) > 0);
        String num = numInt.toString();

        final int firstLeftDigit = Character.digit(num.charAt(0), 10);
        final int exp = num.length() - 1;

        // 4367

        // 1 - 4000
        Interval regularInterval = InterestingRanges.getFullRange(
                firstLeftDigit, exp);

        BigInteger leftOverInt = numInt.subtract(BigInteger.valueOf(firstLeftDigit)
                .multiply(BigInteger.TEN.pow(exp)));

        Preconditions.checkState(leftOverInt.compareTo(BigInteger.ZERO) >= 0);

        if (leftOverInt.compareTo(BigInteger.ZERO) > 0) {
            Interval leftOver = getPartialRange(
                    firstLeftDigit, exp,
                    numInt);

            Interval total = Interval.combin(regularInterval, leftOver);

            return total;
        } else {
            return regularInterval;
        }
    }
    
    public static Interval calcEvenPairRanges(BigInteger L, BigInteger R) {
        Interval ans = null;
        
        //Calculate number of ranges with even palindromes
        Interval ri = calc(R);
                
        if (L.compareTo(BigInteger.ONE) > 0) {
            //Subtract number of ranges between 1 and L-1
            Interval li = calc(L.subtract(BigInteger.ONE));        
            ans = Interval.subtract(li, ri);
        } else {
            ans = ri;
        }
        
        return ans;
    }

   

   

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.L = scanner.nextBigInteger();
        input.R = scanner.nextBigInteger();
        return input;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData input) {
        log.info("Starting case {}", input.testCase);

        
        
        Interval ans = calcEvenPairRanges(input.L, input.R);
        
        return("Case #" + input.testCase + ": "
                + ans.totalEven.mod(BigInteger.valueOf(1000000007)));

    }
}