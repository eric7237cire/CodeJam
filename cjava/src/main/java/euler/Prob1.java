package euler;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.Fraction;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.utils.Direction;
import codejam.utils.utils.DoubleFormat;
import codejam.utils.utils.Grid;
import codejam.utils.utils.PermutationWithRepetition;
import codejam.utils.utils.Permutations;
import codejam.utils.utils.Prime;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.common.math.BigIntegerMath;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Prob1 {

    final static Logger log = LoggerFactory.getLogger(Prob1.class);
    
    public static void main(String args[]) throws Exception {
        long start = System.currentTimeMillis();
        problem70();
        //test();
        long end = System.currentTimeMillis();
        
        log.info("Elapsed time {} ms", end - start);
        
    }
    
    static int getUsedDigits(int num)
    {
        int ret = 0;
        while(num > 0)
        {
            int digit = num % 10;
            ret |= 1 << digit;

            num /= 10;
        }

        return ret;
    }
    
    static boolean isPerm(int num, int num2) {
        int[] digitCounts = new int[10] ;
        
        while(num > 0)
        {
            int digit = num % 10;
            digitCounts[ digit ] ++;

            num /= 10;
        }
        
        while(num2 > 0)
        {
            int digit = num2 % 10;
            digitCounts[ digit ] --;

            num2 /= 10;
        }
        
        for(int d = 0; d <= 9; ++d) {
            if (digitCounts[d] != 0)
                return false;
        }

        return true;
    }
    
    static void problem70() {

        List<Integer> primes = Prime.generatePrimes((int) (2 * Math.sqrt(10000000)));

        double ratioMin = Double.MAX_VALUE;
        int minN = -1;
        int minPhi = 0;

        for (int p1Idx = 0; p1Idx < primes.size(); ++p1Idx) {
            for (int p2Idx = p1Idx + 1; p2Idx < primes.size(); ++p2Idx) {
                int p1 = primes.get(p1Idx);
                int p2 = primes.get(p2Idx);

                int num = p1 * p2;
                if (num > 10000000)
                    break;

                int phi = (p1 - 1) * (p2 - 1);

                double ratio = (double) num / phi;

                if (ratio > ratioMin)
                    continue;

                if (!isPerm(phi, num))
                    continue;

                if (ratio < ratioMin) {
                    ratioMin = ratio;
                    minN = num;
                    minPhi = phi;
                    // log.debug("n {} Phi {} rat {}", num, result, DoubleFormat.df3.format(ratio));
                    log.debug("Current min is {} num {} phi {}", ratioMin, minN, minPhi);
                }

            }
        }

        log.debug("Max is {} num {} phi {}", ratioMin, minN, minPhi);
    }
static void problem70_slow() {
        
        List<Integer> primes = Prime.generatePrimes(1000000);
        
        double ratioMin = Double.MAX_VALUE;
        int minN = -1;
        int minPhi = 0;
        
        for(int num = 1000000; num <= 10000000; ++num) {
            
            int upperLimit = IntMath.sqrt(num, RoundingMode.DOWN);
            
            //List<Integer> primeFactors = Lists.newArrayList();
            
            int numToFactor = num;
            int phi = num;
            
            //Prime factorization
            for(int prime : primes  )
            {
                if (prime > upperLimit || prime > numToFactor)
                    break;
                
                if (numToFactor % prime == 0) {
              //      primeFactors.add(prime);
                    
                    phi -= phi / prime;
                    
                    while(numToFactor % prime == 0) 
                        numToFactor /= prime;
                    
                }
            }
            
            //If num is prime
            if (numToFactor > 1) {
                phi -= phi / numToFactor;
            }
            
            double ratio = (double)num / phi;
            
            if (ratio > ratioMin)
                continue;
            
            if (!isPerm(phi, num))
                continue;
            
            
          
            if (ratio < ratioMin) {
                ratioMin = ratio;
                minN = num;
                minPhi = phi;
            //  log.debug("n {} Phi {} rat {}", num, result, DoubleFormat.df3.format(ratio));
                log.debug("Current min is {} num {} phi {}", ratioMin, minN, minPhi);
            }
            
            
        }
        
        log.debug("Max is {} num {} phi {}", ratioMin, minN, minPhi);
    }

    static void problem69() {
        
        List<Integer> primes = Prime.generatePrimes(1000000);
        
        double ratioMax = 0;
        int maxN = -1;
        
        for(int num = 1; num <= 1000000; ++num) {
            
            //int upperLimit = IntMath.sqrt(num, RoundingMode.DOWN);
            
            //List<Integer> primeFactors = Lists.newArrayList();
            
            int numToFactor = num;
            int result = num;
            
            //Prime factorization
            for(int prime : primes  )
            {
                if (prime > numToFactor)
                    break;
                
                if (numToFactor % prime == 0) {
              //      primeFactors.add(prime);
                    
                    result -= result / prime;
                    
                    while(numToFactor % prime == 0) 
                        numToFactor /= prime;
                    
                }
            }
            double ratio = (double)num / result;
          //  log.debug("n {} Phi {} rat {}", num, result, DoubleFormat.df3.format(ratio));
            if (ratio > ratioMax) {
                ratioMax = ratio;
                maxN = num;
            }
            
            
        }
        
        log.debug("Max is {} num {}", ratioMax, maxN);
    }
    
    static void test() {
        int N = 165;
        int K = 65;

        long[][] state = new long[K + 1][N + 1];

        /*
         * Initial condition.
         */
        state[1][1] = 1;

        for (int length = 2; length <= K; ++length) {
            for (int num = 3; num <= N; num += 2) {

                int rightLength = length - 1;

                for (int rightNum = num - 2; rightNum >= 1; rightNum -= 2) {

                    /*
                     * If there are no valid right subtrees with this state then continue
                     */
                    if (state[rightLength][rightNum] == 0)
                        continue;

                    long rightState = state[rightLength][rightNum];

                    int leftNum = num - (rightNum + 1);

                    for (int leftLength = 1; leftLength <= rightLength; ++leftLength) {

                        long leftState = state[leftLength][leftNum];

                        /*
                         * If there is no valid left hand state continue to next left hand state.
                         */
                        if (leftState == 0)
                            continue;

                        /*
                         * If leftLength == rightLength our loop will consider both mirror images of the state so only count it once.
                         * 
                         * If leftNum == rightNum then both sub trees are identical. The loop only considers this (symmetric) state once which is correct
                         */
                        if (leftLength == rightLength) {
                            state[length][num] += (leftState * rightState);
                        } else {
                            state[length][num] += (leftState * rightState * 2);
                        }

                        state[length][num] %= 9901;

                    }
                }
            }
        }

        System.out.println(state[K][N]);
    }
    
    static void testMergeSort()
    {
        int[] toSort = new int[] {2,
        2,
        1,
        3,
        3,
        3,
        2,
        3,
        1};
        
        int iCount = invCount(toSort);
        
    }
    
    static int merge(int[] arr, int[] left, int[] right) {
        int i = 0, j = 0, count = 0;
        while (i < left.length || j < right.length) {
            if (i == left.length) {
                arr[i+j] = right[j];
                j++;
            } else if (j == right.length) {
                arr[i+j] = left[i];
                i++;
            } else if (left[i] <= right[j]) {
                arr[i+j] = left[i];
                i++;                
            } else {
                arr[i+j] = right[j];
                count += left.length-i;
                j++;
            }
        }
        log.debug("merge {} ; {} ; {} returning count {}", arr, left, right, count);
        return count;
    }

    static int invCount(int[] arr) {
        if (arr.length < 2)
            return 0;

        int m = (arr.length + 1) / 2;
        int left[] = Arrays.copyOfRange(arr, 0, m);
        int right[] = Arrays.copyOfRange(arr, m, arr.length);

        int invCountLeft = invCount(left);
        int invCountRight = invCount(right);
        int mergeCount = merge(arr, left, right);
        int ret = invCountLeft+invCountRight+mergeCount;
        log.debug("Returning {} + {} + {} = {}", invCountLeft, invCountRight, mergeCount, ret);
        return ret;
    }
    
    static boolean isSquare(long n) {
        long sr = (long) Math.sqrt(n);
        return sr * sr == n;
    }
    
    static void problem66() 
    {
        Map<Long, Integer> squares = new HashMap<>();
        for(int sq = 1; sq < 1000; ++sq) {
            squares.put(LongMath.checkedMultiply(sq,sq), sq);
        }
        BigInteger maxX = BigInteger.ZERO;
        int maxD = -1;
        
        for(int D = 1; D <= 1000; ++D) {
            if (isSquare(D))
                continue;
            
            List<Integer> contFrac = findConFrac(D);
            
            List<BigInteger> p = Lists.newArrayList();
            List<BigInteger> q = Lists.newArrayList();
            
            int indexWanted = contFrac.size() % 2 == 1 ?
                    contFrac.size() - 2 :
                        2 * (contFrac.size() - 2) + 1
                    ;
            
            calculatePandQ(contFrac, p, q, indexWanted+1);
            boolean found = false;
            
            BigInteger xBI = p.get(indexWanted);
            BigInteger yBI = q.get(indexWanted);
            
            //log.debug("D = {}.  Maybe {} ^ 2 - {} * {} ^ 2 = 1", D, xBI, D, yBI);
            
            if (xBI.compareTo(maxX) > 0) {
                maxD = D;
                maxX = xBI;
            }
            
            
            
        }
        
        log.debug("Max x {} when D = {}", maxX, maxD);
    }
    
    /*
     * convergents of a continued fraction, p/q is the decimal value approx
     */
    static void calculatePandQ(List<Integer> repFrac,
            List<BigInteger> p,
        List<BigInteger> q, int upTo) 
    {
        List<Integer> a = Lists.newArrayList();
        
        List<Integer> repeating = repFrac.subList(1, repFrac.size());
        
        
        a.add(repFrac.get(0));
        
        int offset = 0;
        for(int n = 1; n < upTo; ++n) {
            a.add(repeating.get(offset));
            ++offset;
            if (offset == repeating.size())
                offset = 0;
                        
        }
        
        p.add(BigInteger.valueOf(a.get(0)));
        q.add(BigInteger.valueOf(1));
        
        p.add(BigInteger.valueOf(a.get(1)).multiply(p.get(0)).add(BigInteger.ONE));
        q.add(BigInteger.valueOf(a.get(1)));
        
        for(int n = 2; n < upTo; ++n) {
            p.add(BigInteger.valueOf(a.get(n)).multiply(p.get(n-1)).add(p.get(n-2)));
        }
        
        for(int n = 2; n < upTo; ++n) {
            q.add(BigInteger.valueOf(a.get(n)).multiply(q.get(n-1)).add(q.get(n-2)));
        }
        
        
    }
    
    static void problem65() 
    {
        List<Integer> a = Lists.newArrayList();
        
        List<Integer> repeating = Arrays.asList(2, 1, 3, 1, 2, 8);
        
        List<BigInteger> p = Lists.newArrayList();
        List<BigInteger> q = Lists.newArrayList(); 
        
        
        a.add(2);
        
        int offset = 0;
        for(int n = 1; n < 100; ++n) {
            //for e
            if (n % 3 == 2) {
                a.add( (n/3 + 1) * 2 );
            } else {
                a.add( 1 );
            }
            //a.add(repeating.get(offset));
            ++offset;
            if (offset == repeating.size())
                offset = 0;
                        
        }
        
        p.add(BigInteger.valueOf(a.get(0)));
        q.add(BigInteger.valueOf(1));
        
        p.add(BigInteger.valueOf(a.get(1)).multiply(p.get(0)).add(BigInteger.ONE));
        q.add(BigInteger.valueOf(a.get(1)));
        
        for(int n = 2; n < 100; ++n) {
            p.add(BigInteger.valueOf(a.get(n)).multiply(p.get(n-1)).add(p.get(n-2)));
        }
        
        for(int n = 2; n < 100; ++n) {
            q.add(BigInteger.valueOf(a.get(n)).multiply(q.get(n-1)).add(q.get(n-2)));
        }
        
        for(int n = 0; n < 100; ++n) {
            double d = p.get(n).doubleValue() / q.get(n).doubleValue();
            log.debug("n {} = {}.  {} / {} Sum numerator : {} ", n, d,  p.get(n), q.get(n), findSumDigits(p.get(n)));
        }
        
    }
    
    static int findSumDigits(BigInteger bi) {
        BigInteger sum = BigInteger.ZERO;
        
        while(bi.compareTo(BigInteger.ZERO) > 0) {
            BigInteger digit = bi.mod(BigInteger.TEN);
            sum = sum.add(digit);
            
            bi = bi.divide(BigInteger.TEN);
        }
        
        return sum.intValue();
        
    }
    
    /*
     * The steps in the algorithm for √n are:
Step 1:
Find the nearest square number less than n, let's call it m2, so that m2<n and n<(m+1)2. 
For example, if n=14 and we are trying to find the CF for √14, then 9 is the nearest square below 14, so m is 3 and n lies between m2=9 and (m+1)2=16.
The whole number part starts off your list of numbers for the continued fraction.
The easy way to find the largest square number below n is to use your calculator:
Find √n and just ignore the part after the decimal point! The number showing is m.

Now, √n = m + 1/x

where n and m are whole numbers.

Step 2:
Rearrange the equation of Step 1 into the form of x equals an expression involving the square root which will appear as the denominator of a fraction: x = 1 / (√n - m)
Step 3:
We now have a fraction with a square-root in the denominator. Use the method above to convert it into a fraction with whole numbers in the denominator. 
In this case, multiply top and bottom by (√ n + m) and simplify.
either Step 4A:
stop if this expression is the original square root plus an integer.
or Step 4B:
start again from Step 1 but using the expression at the end of Step 3


     */
   static int findM(int rad, int num, int denom) {
        double v = (Math.sqrt(rad) + num) / denom;
        int m = (int) v;
        
        return m;
    }
   
  static List<Integer> findConFrac(int rad) {
       int prevStep1Numerator = 0;
       int prevStep1Denom = 1;
       
       List<Integer> xList = Lists.newArrayList();
       
       for(int i = 0; i < 50000; ++i) {
           int xi = findM(rad, prevStep1Numerator, prevStep1Denom);
           
           
           xList.add(xi);
           //Prefect square
           if (i==0 && xi * xi == rad) {
               return xList;
           }
           
           /**
            * The radical is not represented, so
            * 5 / ( Sqrt(14) - 2) is just 5 / -2
            */
           int step2Numerator = prevStep1Denom;
           int step2Denom = prevStep1Numerator - xi * prevStep1Denom;
           
           //Again radical is missing
           int step3Numerator = -step2Denom;
           
           int step3Denom = (rad - step2Denom*step2Denom) / step2Numerator;
           
           if (step3Denom == 1) {
               xList.add(step3Numerator+xList.get(0));
               break;
           }
           
           prevStep1Denom = step3Denom;
           prevStep1Numerator = step3Numerator;
           
           //log.debug("x{}={} frac={}", i, xi);
       }
       
       return xList;
   }
    
  //See tex
    static void problem63() {
        
        //4, 2,1,3,1,2,8
        findConFrac(19);
        
        int count = 0;
       for(int rad = 2; rad <= 10000; ++rad) {
        List<Integer> xList = findConFrac(rad);
        int period = xList.size() - 1;
        if (period % 2 == 1)
            ++count;
       // log.debug("Rad {}  list {}", rad, xList);
       }
       
       log.debug(" count is {}", count);
    }
    
    static void problem59() {
        Scanner scanner = new Scanner(Prob1.class.getResourceAsStream("cipher1.txt"));
        
        //for(int i = 1; i <= 127; ++i) {
        int i = 32;
            for(int j = 1; j <= 127; ++j) {
                log.debug("{} xor {} = {}", i, j, i ^ j);
            }
        //}
        List<Integer> o1 = Lists.newArrayList();
        List<Integer> o2 = Lists.newArrayList();
        List<Integer> o3 = Lists.newArrayList();
        
        List<List<Integer>> chars = Lists.newArrayList();
        List<Integer> allChars = Lists.newArrayList();
        chars.add(o1);
        chars.add(o2);
        chars.add(o3);
        
        List<Multiset<Integer>> freq = Lists.newArrayList();
        freq.add(HashMultiset.<Integer>create());
        freq.add(HashMultiset.<Integer>create());
        freq.add(HashMultiset.<Integer>create());
        
        int[] maxChar = {0,0,0};
        int[] maxCount = {0, 0, 0};
        
        int offset = 0;
        for(String str : Splitter.on(',').split(scanner.next())) {
            Integer anInt = Integer.parseInt(str);
            allChars.add(anInt);
            chars.get(offset).add(anInt);
            
            freq.get(offset).add(anInt);
            
            if (freq.get(offset).count(anInt) > maxCount[offset]) {
                maxCount[offset] = freq.get(offset).count(anInt);
                maxChar[offset] = anInt;
            }
            
            ++offset;
            
            if (offset == 3)
                offset = 0;
            
            
        }
        
        
        int[] key = {103, 111, 100}; 
        
        offset = 0;
        
        StringBuffer sb = new StringBuffer();

        int count = 0;
        
        for (Integer anInt : allChars) {
            sb.append(Character.toString((char) (anInt ^ key[offset])));
            count += anInt ^ key[offset];
            ++offset;

            

            if (offset == 3)
                offset = 0;
        }


        log.debug("Message {}\nCount {}", sb.toString(), count);
    }
    
    static void problem58() {
        
        findPrimeRatioInSpiral(30009);
        
    }
    
    static boolean findPrimeRatioInSpiral(int size) {
        
        
        //Initial direction
        Direction dir = Direction.EAST;
        
        int max = size * size;
        
        int movements = 1;
        int movementsLeft = 1;
        
        int total = 0;
        int primeCount = 0;
        int curSize = -1;
        
        for(int i = 1; i <= max; ++i) {
            
            if (movementsLeft == movements && dir != Direction.NORTH &&
                    Prime.isPrime(i)) {
                ++primeCount;                
            } 

            --movementsLeft;
            
            if (movementsLeft == 0) {
                
                
                //Turn counter clockwise
                dir = dir.turn(-2);
                
                if (dir == Direction.NORTH) {

                    curSize += 2;
                    total = 2 * curSize - 1;
                    
                    double ratio = (double) primeCount / total;

                    if (ratio < .1d && i > 1) {
                        log.info("Found it {}", curSize);
                        return true;
                    }
                }
                
                //Each time we are going again east or west, the spiral has grownn
                if (dir == Direction.WEST || dir == Direction.EAST) {
                    ++movements;
                }
                
                movementsLeft = movements;
            }
        }
        
            
        return false;
     
    }
    
    static void problem57() {
        BigFraction denom = new BigFraction(2);

        int count = 0;

        for (int i = 2; i <= 1000; ++i) {
            denom = new BigFraction(1).divide(denom).add(2);
            BigFraction frac = new BigFraction(1).divide(denom).add(1);

            if (frac.getNumerator().toString().length() > frac.getDenominator().toString().length()) {
                ++count;
                log.debug("Fraction {} = {}", i, frac);
            }
        }

        log.debug("Count is {}", count);

    }
    
    static void problem56() {
        int max =0;
        for(int a = 1; a < 100; ++a) {
            for(int b = 1; b < 100; ++b) {
                String s = BigInteger.valueOf(a).pow(b).toString();
                
                int sum = 0;
                for(int i = 0; i < s.length(); ++i) {
                    sum += Character.digit(s.charAt(i),10);
                }
                
                if (sum > max) {
                    log.debug("New max a {} b {} max {} ", a, b, sum);
                    max = sum;
                }
            }
        }

        
        
    }
    
    static void problem55() {
        int count = 0;
        
        for (int i = 1; i < 10000; ++i) {
            BigInteger num = BigInteger.valueOf(i);
            boolean isLychrel = true;
            for (int tri = 0; tri < 50; ++tri) {
                num = num.add(new BigInteger(StringUtils.reverse(num.toString())));
                String numStr = num.toString();
                String numStrRev = StringUtils.reverse(numStr);
                if (numStr.equals(numStrRev)) {
                    isLychrel = false;
                    break;
                }
            }
            
            if (isLychrel) {
                log.debug("Is Lychrel {}", i);
                ++count;
            }
        }
        
        log.debug("Count is {}", count);
    }

    static Pair<Integer, Integer> strToCard(String str) {
        Integer card = -1;
        Integer suit = -1;
        
        Character cardChar = str.charAt(0);
        Character suitChar = str.charAt(1);
        
        switch(cardChar) {
        case 'A': card = 14; break;
        case 'T': card = 10; break;
        case 'J': card = 11; break;
        case 'Q': card = 12; break;
        case 'K': card = 13; break;
        default: card = Character.digit(cardChar, 10);
        }
        
        switch(suitChar) {
        case 'D': suit = 2; break;
        case 'S': suit = 4; break;
        case 'H': suit = 8; break;
        case 'C': suit = 16; break;
        }
        
        Preconditions.checkState(card >= 0);
        Preconditions.checkState(suit >= 0);
        
        return new ImmutablePair<Integer, Integer>(card, suit);
    }
    
    final static int PAIR = 1000000;
    final static int STRAIGHT_FLUSH = 8*PAIR;
    final static int FOUR_KIND = 7*PAIR;
    final static int FULL_HOUSE = 6*PAIR;
    final static int FLUSH = 5*PAIR;
    final static int STRAIGHT = 4*PAIR;
    final static int THREE_KIND = 3*PAIR;
    final static int TWO_PAIR = 2*PAIR;
    
        
    static int[] kickerLevels = {50675, 3375, 225, 15, 1};
    static int[] straightBitMasks = {4111, 31, 31 << 1, 31 << 2, 31 << 3, 31 << 4, 31 << 5, 31 << 6, 31 << 7, 31 << 8};
    public  static int evalHand(List<Pair<Integer, Integer>> hand) {
        
        Collections.sort(hand);
        
        int suits = 0;
        int ranks = 0;
        
        int[] freqCard = new int[15];
        
        for(Pair<Integer,Integer> card : hand) {
            suits |= card.getRight();
            ranks |= (1 << card.getLeft() - 2);
            freqCard[card.getLeft()]++;
        }
        
        boolean flush = IntMath.isPowerOfTwo(suits);
        
        int straight = -1;
        
        for(int sbmIdx = 0; sbmIdx < straightBitMasks.length; ++sbmIdx) {
            if (ranks == straightBitMasks[sbmIdx]) {
                straight = sbmIdx;
            }
        }
                
        int fourKind = -1;
        int threeKind = -1;
        int firstPair = -1;
        int secondPair = -1;
        List<Integer> singleCard = Lists.newArrayList();
        
        
        for(int r = 14; r >= 0; --r) {
            if (freqCard[r] == 4) {
                fourKind = r;
            } else if (freqCard[r] == 3) {
                threeKind = r;
            } else if (freqCard[r] == 2 && firstPair == -1) {
                firstPair = r;
            } else if (freqCard[r] == 2 ) {
                secondPair = r;
            } else if (freqCard[r] == 1) {
                singleCard.add(r);
            }
        }
        
        int score = 0;
        int kickerLevel = 0;
        
        //straight flush
        if (straight >= 0 && flush) {
            return STRAIGHT_FLUSH + kickerLevels[0] * straight;
        }
        
                
        //4 kind
        if (fourKind >= 0) {
            return FOUR_KIND + kickerLevels[0] * fourKind + kickerLevels[1] * singleCard.get(0);
        }
        
        //full house
        if (threeKind >= 0 && firstPair >= 0) {
            return FULL_HOUSE + kickerLevels[0] * threeKind + kickerLevels[1] * firstPair;
        }
        
        if(flush) {
            kickerLevel = 0;
            score = FLUSH;
            for(int cardIdx = 4; cardIdx >= 0; --cardIdx) {
                Pair<Integer,Integer> card = hand.get(cardIdx);
                
                score += kickerLevels[kickerLevel] * card.getLeft();
                ++kickerLevel;
            }
            return score;
        }
        
        //straight
        if (straight >= 0) {
            return STRAIGHT + kickerLevels[0] * straight;
        }
        
        
        //3 kind
        if (threeKind >= 0) {
            return THREE_KIND + threeKind * kickerLevels[0] + singleCard.get(0) * kickerLevels[1] + singleCard.get(1) * kickerLevels[2];
        }
        
        //2 pair
        if (firstPair >= 0 && secondPair >= 0) {
            return score = TWO_PAIR + firstPair * kickerLevels[0] + secondPair * kickerLevels[1] +  singleCard.get(0) * kickerLevels[2] ;
        }
        
        //Pair
        if (firstPair >= 0) {
            return PAIR + firstPair * kickerLevels[0] +  singleCard.get(0) * kickerLevels[1] +
                    singleCard.get(1) * kickerLevels[2] +  singleCard.get(2) * kickerLevels[3];
                   
        }
        
        //High card        
        kickerLevel = 0;
        score = 0;
        for(int cardIdx = 4; cardIdx >= 0; --cardIdx) {
            Pair<Integer,Integer> card = hand.get(cardIdx);
            
            score += kickerLevels[kickerLevel] * card.getLeft();
            ++kickerLevel;
        }
        
        return score;
        
    }
    
    public static List<Pair<Integer, Integer>> stringToHand(String str) {
        Scanner scanner = new Scanner(str);
        List<Pair<Integer, Integer>> hand = Lists.newArrayList();
        
        for(int j = 0; j < 5; ++j) {
            hand.add(strToCard(scanner.next()));
        }
        
        return hand;
    }
    
    public static void problem54() {
        Scanner scanner = new Scanner(Prob1.class.getResourceAsStream("poker.txt"));
        int count = 0;
        
        for(int i = 0; i < 1000; ++i) {
            List<Pair<Integer, Integer>> player1 = Lists.newArrayList();
            List<Pair<Integer, Integer>> player2 = Lists.newArrayList();
            
            for(int j = 0; j < 5; ++j) {
                player1.add(strToCard(scanner.next()));
            }
            
            for(int j = 0; j < 5; ++j) {
                player2.add(strToCard(scanner.next()));
            }
            
            if (evalHand(player1) > evalHand(player2)) {
                ++count;
            }
        }
        
        log.debug("Count is {}", count);
        
        
    }
    
    public static void problem48() {
        BigInteger sum  = BigInteger.ZERO;
        for(int i = 1; i <= 1000; ++i) {
            sum  = sum.add(BigInteger.valueOf(i).pow(i));
        }
        
        sum = sum.mod(BigInteger.valueOf(10000000000L));
        
        log.debug("Sum {} ", sum);
    }
    
    public static void problem43() {
        Integer[] digits = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        
        Integer[] out = new Integer[digits.length];
        
        Permutations<Integer> p = Permutations.create(digits,out);
        
        List<Integer> primes = Prime.generatePrimes(17);
        
        long sum = 0;
        
        while(p.next()) {
            boolean special = true;
            
            for(int i = 0; i < 7; ++i) {
                int start = i + 1;
                
                int num = 0;
                
                for(int d = start; d < start + 3; ++d) {
                    num *= 10;
                    num += out[d];
                }
            
                if (num % primes.get(i) != 0) {
                    special = false;
                    break;
                }
                
            }
            
            if (special) {
                
                long num = 0;
                
                for(int d = 0; d < out.length; ++d) {
                    num *= 10;
                    num += out[d];
                }
                
                sum += num;
                log.debug("Num {} or {} has the property", (Object)out, num);
            }
        }
        
        log.debug("Sum is {}", sum);
    }
    
    public static void problem42() {
        Set<Integer> triangleNums = Sets.newHashSet();
        
        int tNum = 0;
        
        int max = 26 * 26;
        
        int next = 1;
        
        while(tNum < max) {
            tNum += next;
            triangleNums.add(tNum);
            next++;
        }
        
        Scanner scanner = new Scanner(Prob1.class.getResourceAsStream("prob42.txt"));
        
        String[] s = scanner.next().replace("\"", "").split(",");
        
        int count = 0;
        
        for (String str : s) {
            //log.debug(str);
            int num = 0;
            for(int c = 0; c < str.length(); ++c) {
                num += str.charAt(c) - 'A' + 1;
            }
            
            if (triangleNums.contains(num)) {
                log.debug("Found triangle word {}", str);
                ++count;
            }
        }
        
        log.debug("Count is {}", count);
        
    }
    
    public static void problem41() {
        List<Integer> knownPrimes = Prime.generatePrimes(1000000);
        
        for(int digits = 2; digits <= 9; ++digits) {
            Integer[] digitsArray = new Integer[digits];
            
            for(int d = 1; d <= digits; ++d) {
                digitsArray[d-1] = d;
            }
            
            Integer[] out = new Integer[digits];
            
            Permutations<Integer> p = Permutations.create(digitsArray, out);
            
            while(p.next()) {
                
                int num = 0;
                
                for(int d = 0; d < digits; ++d) {
                    num *= 10;
                    num += out[d];
                }
        
                if (Prime.isPrime(num, knownPrimes)) {
                    log.debug("pandigital prime {}", num);
                }
                
            }
        }
    }
    
    public static void problem40() {
        StringBuffer sb = new StringBuffer();
        
        int i = 1;
        
        while(sb.length() < 1000000) {
            sb.append(Integer.toString(i));
            ++i;
        }
        
        int digit = 1;
        int product = 1;
        for(i = 0; i < 7; ++i) {
            log.debug("Taking {} digit", digit);
            product *= Character.digit(sb.charAt(digit - 1), 10);
            digit *= 10;
            
        }
        
        log.debug("Product p {}", product);
    }
    
    public static void problem39() {
        int max = 0;
        for(int p = 3; p <= 1000; ++p) {
            int num = 0;
            
            for(int a = 1; a < p; ++a) {
                for(int b = a ; b < p; ++b) {
                    int c = p - a - b;
                    int ab = a*a + b*b;
                    int cc = c*c;
                    if (ab == cc) {
                        //log.debug("Found {} {} {} with p {}", a,b,c,p);
                        num ++;
                    }
                
                }
            }
            
            if (num > max) {
                max = num;
                log.debug("New max {} for p {}", max, p);
            }
        }
    }
    
    public static void problem38() {
      
        int max = 0;
        
        outer:
        for(int num = 1; num < 10000; ++num) {
            int i = 1;
            String s = "";
            
            while (s.length() < 9) {
                s = s + i * num;
                ++i;
            }
            
            if (s.length() > 9)
                continue;
            
            for(int d = 1; d <= 9; ++d) {
                if (!s.contains(Integer.toString(d))) {
                    continue outer;
                }
            }
            
            int pan = Integer.parseInt(s);
            
            if (pan > max) {
                max = pan;
            log.debug("Found {} makes {}", num, s);
            }
        }
        /*
        Integer[] digits = new Integer[9];
        for(int i = 1; i <= 9; ++i) {
            digits[i-1] = i;
        }
        
        Integer[] perm = new Integer[9];
        
        int max = 0;
        Permutations<Integer> p = Permutations.create(digits, perm);
        
        while(p.next()) {
            int panNum  = 0;
            for(int i = 0; i < 9; ++i) {
                panNum *= 10;
                panNum += perm[i];                
            }
            
            for(int n = 12; n >= 2; --n) {
                int trial = panNum;
                boolean ok = true;
                for(int i = n; i >= 1; --i) {
                    if (trial % i != 0) {
                        ok = false;
                        break;
                    }
                    
                    trial /= i;
                        
                }
                
                if (ok && panNum > max) {
                    max = panNum;
                    log.debug("Found pan {} from {} multiplying 1 to {}", panNum, trial, n);
                }
            }
        }*/
    }
    
    public static void problem37() {
        List<Integer> primes = Prime.generatePrimes(1000000);

        Set<Integer> primeSet = Sets.newHashSet(primes);
        
        int[] powTen = new int[8];
        
        powTen[0] = 10;
        
        for(int i = 1; i < powTen.length; ++i) {
            powTen[i] = 10 * powTen[i-1];
        }
        
        int sum = 0;
        for(int p : primes) {
            if (p < 10)
                continue;
            
            boolean isTruncable = true;
            int truncP = p;
            
            for(int lastDigit = powTen.length - 1; lastDigit >= 0; --lastDigit) {
                truncP %= powTen[lastDigit];
                
                if (!primeSet.contains(truncP)) {
                    isTruncable = false;
                    break;
                }
            }
            
            truncP = p;
            
            while(truncP > 0 && isTruncable) {
                if (!primeSet.contains(truncP)) {
                    isTruncable = false;
                    break;
                }
                
                truncP /= 10;
            }
        
            
            if (isTruncable) {
                log.debug("{} is truncable", p);
                sum += p;
            }
        }
        
        log.debug("Sum is {}", sum);
    }
    
    public static void main36() {
        int sum = 0;
        for(int i = 1; i < 1000000; ++i) {
            String s10 = Integer.toString(i, 10);
            String s2 = Integer.toString(i, 2);
            
            //String chk = s2.substring(s2.length() - s2.length() / 2);
            //s10 len 4
            //0 1 2 3  with beg
            //len 5
            //4 5
            if ((s10.startsWith(StringUtils.reverse(s10.substring(s10.length() - s10.length() / 2)))) &&
            s2.startsWith(StringUtils.reverse(s2.substring(s2.length() - s2.length() / 2)))) {
                    log.debug("Found {} {} ", s10, s2);
                    sum += i;
            }
        }
        
        log.debug("Sum {}", sum);
    }
    
    public static void main35(String args[]) throws Exception {
        //10987654321

        long[] powTen;

        int powers = 8;
        powTen = new long[powers];
        long pt = 1;
        for (int i = 0; i < powers; ++i) {
            powTen[i] = pt;
            pt *= 10;
        }

        List<Integer> primes = Prime.generatePrimes(1000000);

        Set<Integer> primeSet = Sets.newHashSet();
        
        primeSet.addAll(primes);
        
        int count = 0;
        
        int numDigits = 1;
        
        for(int prime : primes) {
            if (prime >= powTen[numDigits]) {
                ++numDigits;
            }
            
            boolean ok = true;
            
            for(int shift = 0; shift < numDigits; ++shift) {
                int digit = prime % 10;
                prime /= 10;
                prime += digit * powTen[numDigits - 1];
                if (!primeSet.contains(prime)) {
                    ok = false;
                    break;
                }
            }
            
            if (ok) {
                log.debug("Circular prime {}  count {}", prime, count);
                ++count;
            }
            
        }
                        

        log.debug("Count is {}", count);
    }
    
    public static void main34_faster(String args[]) throws Exception {
        // 10987654321

        int[] factorials = new int[10];

        factorials[0] = 1;
        for (int i = 1; i <= 9; ++i) {
            factorials[i] = i * factorials[i-1];
        }

        int sum = 0;
        for (int num = 10; num < 10000000; ++num) {

            int digits = num;
            int fac = 0;
            
            while(digits > 0) {
                int digit = digits % 10;
                fac += factorials[digit];
                digits /= 10;
            }                

            if (fac == num) {
                log.debug("Found {}", num);
                sum += num;
            }
        

        }

        log.debug("Sum is {}", sum);
    }
    
    public static void main34(String args[]) throws Exception {
                 //10987654321
        
        long[] powTen;
        
        int powers = 15;
        powTen = new long[powers];
        long pt = 1;
        for(int i = 0; i < powers; ++i) {
            powTen[i] = pt;
            pt *= 10;
        }
    
        
        int[] factorials = new int[10];
        
        for(int i = 0; i <= 9; ++i) {
            factorials[i] = IntMath.factorial(i);
        }
        
        long sum = 0;
        for (int numDigits = 2; numDigits <= 7; ++numDigits) {
            
            for(long num = powTen[numDigits-1]; num < powTen[numDigits]; ++num) {
                long fac = 0;
                for(int d = 1; d <= numDigits; ++d) {
                    fac += factorials[getDigit(num, d, powTen)];
                }
                
                if (fac == num) {
                    log.debug("Found {}", num);
                    sum += num;
                }
            }
            
        }
        
        log.debug("Sum is {}", sum);
    }
    
    
    
    static int getDigit(long num, int digit, long[] powTen) {
    
        num %= powTen[digit];
        num /= powTen[digit - 1];
        
        return Ints.checkedCast(num);
    }
    
    public static void main33(String args[]) throws Exception {
        
        Fraction product = new Fraction(1);
        
        for (int num = 10; num <= 99; ++num) {
            for(int denom = 11; denom <= 99; ++denom) {
                int numSimp = num / 10;
                int denomSimp = denom % 10;
                
                int numElim = num % 10;
                int denomElim = denom / 10;

                if (denomSimp == 0)
                    continue;

                if (numElim != denomElim)
                    continue;

                Fraction f = new Fraction(num, denom);
                Fraction f2 = new Fraction(numSimp, denomSimp);
                
                if (f2.getDenominator() == 1)
                    continue;
                
                if (f.equals(f2)) {
                    log.debug("Fractions {} / {} == {}", num, denom, f2);
                    product = product.multiply(f);
                }
            }
        }
        
        log.debug("Product {}", product);
    }
    
    public static void main32(String args[]) throws Exception {
        //Iterator<Long> it = new CombinationIterator(n, k);
        
        int[] powTen = new int[] { 1, 10, 100, 1000, 
            10000, 
            100000, 
            1000000, 
            10000000, 
            100000000 };
        
        Set<Integer> panDigital = Sets.newHashSet();
        
        Integer choice[] = new Integer[] { 0, 1, 2 };
        Integer output[] = new Integer[9];
        
        PermutationWithRepetition<Integer> perm = PermutationWithRepetition.create(choice, output);
        
        while(perm.hasNext()) {
            perm.next();
            
            List<Integer> aDigits = Lists.newArrayList();
            List<Integer> bDigits = Lists.newArrayList();
            List<Integer> pDigits = Lists.newArrayList();
            
            for(int d = 1; d <= 9; ++d) {
                if (output[d-1] == 0) {
                    aDigits.add(d);
                } else if (output[d-1] == 1) {
                    bDigits.add(d);
                } else if (output[d-1] == 2) {
                    pDigits.add(d);
                } else {
                    Preconditions.checkState(false);
                }
            }
            
            if (pDigits.size() < 3 || pDigits.size() > 6)
                continue;
            
            if (aDigits.isEmpty() || bDigits.isEmpty())
                continue;
            
            Integer[] aDigitsArray = new Integer[aDigits.size()];
            aDigitsArray = aDigits.toArray(aDigitsArray);
            Integer[] bDigitsArray = new Integer[bDigits.size()];
            bDigitsArray = bDigits.toArray(bDigitsArray);
            
            Integer[] aDigitsOut = new Integer[aDigitsArray.length];
            Integer[] bDigitsOut = new Integer[bDigitsArray.length];
            
            Permutations<Integer> permA = Permutations.create(aDigitsArray, aDigitsOut);
            
            while(permA.next()) {
                
                
                int a = 0;
                for(int i = 0; i < aDigitsOut.length; ++i) {
                    a+=aDigitsOut[i] * powTen[i];
                }
                
                Permutations<Integer> permB = Permutations.create(bDigitsArray, bDigitsOut);
                
                while(permB.next()) {
                int b = 0;
                for(int i = 0; i < bDigitsOut.length; ++i) {
                    b+=bDigitsOut[i] * powTen[i];
                }
                
                int p = a * b;
                
                String pStr = Integer.toString(p);
                
                if (pStr.length() != pDigits.size())
                    continue;
                
                boolean match = true;
                for(int pDigit : pDigits) {
                    if (!pStr.contains(Integer.toString(pDigit))) {
                        match = false;
                        break;
                    }
                }
                
                if (match) {
                    log.debug("Unusual number {} * {} = {}", a, b, p);
                    panDigital.add(p);
                }
                
                }
            }
            
            
            
            //log.debug("output {}", (Object) output);
        }
        
        int sum = 0;
        for(int pan : panDigital) {
            sum += pan;
        }
        
        log.debug("Sum is {}", sum);
    }
    
    
    public static void main31(String args[]) throws Exception
    {
      List<Integer> values = Arrays.asList(1, 2, 5, 10, 20, 50, 100, 200);
        //List<Integer> values = Arrays.asList(1, 5, 10, 25, 50, 100);
        int total = 200;
        /*
         * 1  -- (1) 1
         * 2  -- (2) 1,1 ; 2
         * 3  -- (2) 1,1,1 ; 2,1 
         * 4  -- (3) 1x4 ; 2,1,1 ; 2,2
         * 5  -- (4)  ; 5
         * 6  -- (5)  ; 2,2,2
         * 7          ; 5,2
         * 8          ; 2,2,2,2
         * A new way every time the sum % coin == 0
         */
                
        log.debug("Ways {}", countWays(values, values.size() - 1, total));
        
        int[] ways = new int[total+1];
        ways[0] = 1;
        for(int coinIndex = 0; coinIndex < values.size(); ++coinIndex) {
            int coin = values.get(coinIndex);
            for(int j = coin; j <= total; ++j) {
                ways[j] = ways[j] + ways[j - coin];
            }
        }
        
        log.debug("Ways {}", ways[total]);
        
    }
    
    static int countWays(List<Integer> values, int maxCoin, int sum) {
        
        int value = values.get(maxCoin);
        
        if (maxCoin == 0) {
            if (sum % value == 0) 
                return 1;
            else 
                return 0;
        }
        
        int max = sum / value;
        
        int ways = 0;
        for(int coinsUsed = 0; coinsUsed <= max; ++coinsUsed) {
            ways += countWays(values, maxCoin - 1, sum - coinsUsed * value);
        }
        
        return ways;
    }
    
    
    
    public static void main30(String args[]) throws Exception {
        int pow = 5;
                
        int total = 0;
        
        for(int i = 2; i < 2000000; ++i) {
            String s = Integer.toString(i);
            
            int sum = 0;
            
            for(int c = 0; c < s.length(); ++c) {
                sum += IntMath.pow(Character.digit(s.charAt(c), 10), pow);
            }
            
            if (sum == i) {
                log.debug("Num {}", i);
                total += i;
            }
        }
        
        log.debug("Sum {}", total);
    }
    
    public static void main29(String args[]) throws Exception {
        Set<BigInteger> set = Sets.newHashSet();
        
        int max = 100;
        for(int a = 2; a <= max; ++a) {
            for(int b = 2; b <= max; ++b) {
                set.add(BigInteger.valueOf(a).pow(b));
            }
        }
        
        log.debug("Set size {}", set.size());
    }
    public static void main28(String args[]) throws Exception {
        int size = 1001;
        
        Grid<Integer> grid = Grid.buildEmptyGrid(size, size, 0);
        
        Integer index = grid.getIndex(size / 2, size / 2);
        
        Direction dir = Direction.EAST;
        
        int max = size * size;
        
        int movements = 1;
        int movementsLeft = 1;
        
        for(int i = 1; i <= max; ++i) {
            grid.setEntry(index, i);
            
            if (i == max)
                break;
            
            index = grid.getIndex(index, dir);
            
            --movementsLeft;
            
            if (movementsLeft == 0) {
                dir = dir.turn(2);
                
                if (dir == Direction.WEST || dir == Direction.EAST) {
                    ++movements;
                }
                
                movementsLeft = movements;
            }
        }
        
        index = 0;
        
        int sum = 0;
        for(int i = 0; i < size; ++i) {
            sum += grid.getEntry(index);
            
            index = grid.getIndex(index, Direction.SOUTH_EAST);
        }
        
        index = grid.getIndex(0, size - 1);
        for(int i = 0; i < size; ++i) {
            sum += grid.getEntry(index);
            
            index = grid.getIndex(index, Direction.SOUTH_WEST);
        }
        
        sum -= grid.getEntry(size / 2, size / 2);
        
        log.debug("Sum {}", sum);
    }
    public static void main27(String args[]) throws Exception {
        List<Integer> primes = Prime.generatePrimes(1000*1000 * 2);
        
        int max = 0;
        
        List<Integer> bPrimes = Prime.generatePrimes(1000);
        
        
        for(int a = -100; a <= 100; ++a) {
            //log.debug("A is {}", a);
            for(int b : bPrimes) {
                Preconditions.checkState(primes.contains(Math.abs(b)));
                
                int n = 0;
                for(n = 0; n < 100; ++n) {
                    int val = n * n + a * n  + b;
                    if (!primes.contains(val)) {
                        break;
                    }
                }
                if (n > max) {
                    max = n;
                    log.debug("a {} b {} num primes {}.  product {}", a,b, n, a*b);
                }
            }
        }
    }
    
    /*
     * dividend / divisor
     */
    @SuppressWarnings("unused")
    static int getRepetitionLength(final int divisor, int dividend) {
        double ans = 0;

        int origDividend = dividend;
        int factor = 1;

        List<Integer> remainders = Lists.newArrayList();
        while (true) {

            while (dividend < divisor) {
                factor *= 10;
                dividend *= 10;
            }
            
            int index = remainders.indexOf(dividend);
            
            if (index != -1) {
                
                /*
                log.debug("Ans {} rep len {} for {} / {}", ans, 
                        remainders.size() - index,
                        origDividend, divisor);
                        */
                        
                return remainders.size() - index;
            }
            
            remainders.add(dividend);
            
            int d = dividend / divisor;
            
            //Too inaccurate
            ans += (double) d / factor;
            int remainer = dividend - d * divisor;
            
            dividend = remainer;

            if (dividend == 0) {
                return 0;
            }
        }
    }
    
    public static void main26(String args[]) throws Exception {
        
        int max = 0;
        
        for(int i = 2; i < 1000; ++i) {
            int rl = getRepetitionLength(i, 1);
            if (rl > max) {
                max = rl;
                log.debug("New max {} for num {}", max, i);
            }
        }
        
        
        
        
    }
    public static void main25(String args[]) throws Exception {
        BigInteger x = BigInteger.ONE;
        BigInteger y = BigInteger.ONE;
        
        int digits = 1000;
        
        BigInteger min = BigInteger.TEN.pow(digits-1);
        
        int term = 2;
        while(y.compareTo(min) < 0) {
            ++term;
            BigInteger next = x.add(y);
            x = y;
            y = next;
        }
        
        log.debug("Prob 25.  {}", term);
    }
    public static void main24(String args[]) throws Exception {
        Integer[] arr = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        
        Integer[] out = new Integer[10];
        
        Permutations<Integer> p = Permutations.create(arr, out);
        
        int i = 0;
        while(p.next()) {
            ++i;
            if (i==1000000) {
            log.debug("out {}", (Object)out);
            break;
            }
        }
    }
    
    public static void main23(String args[]) throws Exception {
        
        long l = System.currentTimeMillis();
        
        int maxNum = 28123;
        
        boolean[] abundant = new boolean[1+maxNum];
        
        for(int num = 1; num <= maxNum; ++num) {
            
            int sum = 0;
            
            int upperLimit = IntMath.sqrt(num, RoundingMode.DOWN);
            
            for(int factor = 1; factor <= upperLimit; ++factor) {
                if (num % factor == 0) {
                    sum += factor;
                    int otherFactor = num / factor;
                    
                    if (otherFactor != factor && otherFactor != num)
                        sum += otherFactor;
                }
            }
            
            
            if (sum > num) {
                abundant[num] = true;
            }
        }
        
        int sum = 0;
        
        num :
        for(int num = 1; num <= maxNum; ++num) {
            
            for(int smaller = 1; smaller <= num / 2; ++smaller) {
                int larger = num - smaller;
                if (abundant[smaller] && abundant[larger]) {
                    continue num;
                }
            }
            
            //log.debug("Num {} cannot be expressed as abundant", num);
            sum += num;
        }
        
        long elapsed = System.currentTimeMillis() - l ;
        log.debug("Prob 23.  Elapsed ms {} Sum {}", elapsed, sum);
    }
    
    
    public static void main1_22(String args[]) throws Exception {
        ///////////////////////////////////////
        //1
        int sum = 0;
        for(int i = 1; i < 1000; ++i ) {
            if (i%3 ==0 || i % 5 == 0)
                sum +=i;
        }
        log.info("Prob 1 Sum {}", sum);
        
        ///////////////////////////////////////
        //2
        
        sum = 2;
        int fibBack2 = 1;
        int fibBack1 = 2;
        
        while(fibBack1 <= 4000000) {
            int fib = fibBack1 + fibBack2;
            fibBack2 = fibBack1;
            fibBack1 = fib;
            if (fib % 2 == 0) {
                sum += fib;
            }
        }
        
        log.info("Prob 2 sum {}", sum);
        
        //////////////////////////////////////
        //3
        long num = 600851475143L;
        
        int sqRootNum = Ints.checkedCast( LongMath.sqrt(num, RoundingMode.UP) );
        
        List<Integer> primes = Prime.generatePrimes(sqRootNum);
        
        for(int i = primes.size() - 1; i >= 0; --i) {
            if (num % primes.get(i) == 0) {
                log.info("Prob 3 Prime is {}", primes.get(i));
                break;
            }
        }
        ///////////////////////////////////////
        //4
        
        int largest = 0;
        
        for(int i = 100; i <= 999; ++i) {
            for(int j = 100; j <= 999; ++j) {
                sum = i * j;
                
                int toRev = sum;
                int rev = 0;
                
                while(toRev != 0) {
                    rev *= 10;
                    rev += toRev % 10;
                    toRev /= 10;
                }
                if (rev == sum && largest < rev) {
                    largest = sum;
                }
            }
        }
        log.info("Prob 4.  Largest palin {}", largest);
        
        primes = Prime.generatePrimes(20);
        
        Multiset<Integer> primeFactorizationOfN = HashMultiset.create();
        
        for(int prime : primes) {
            primeFactorizationOfN.add(prime);
        }
        
        for(int i = 2; i <= 20; ++i) {
            Multiset<Integer> primeFactorizationOfDivisor = HashMultiset.create();
            //get prime factorization
            int divisor = i;
            
            while(divisor > 1) {
                for(int prime : primes) {
                    if (divisor % prime == 0) {
                        divisor /= prime;
                        primeFactorizationOfDivisor.add(prime);
                    }
                }
            }
            
            for(Integer pf : primeFactorizationOfDivisor) {
                if (primeFactorizationOfDivisor.count(pf) > primeFactorizationOfN.count(pf)) {
                    primeFactorizationOfN.setCount(pf, primeFactorizationOfDivisor.count(pf));
                }
            }
            
        }
        
        long n = 1;
        
        for(Integer pf : primeFactorizationOfN) {
            n *= pf;
        }
        
        log.info("Prob 5.  Divisible by 1-20 : {}", n);
        
//        for(long i = 1; i < 1000000000; ++i) {
//            boolean found = true;
//            
//            for(int j = 2; j <= 20; ++j) {
//                if (i % j != 0) {
//                    found = false;
//                    break;
//                }                    
//            }
//            if (found) {
//            log.info("Prob 5.  Divisible by 1-20 : {}", i);
//            break;
//            }
//        }
        
        
        n = 100;
        
        long sumSq = n * (n+1) * (2*n+1) /  6;
        long sqOfSum = n * (n+1) / 2;
        sqOfSum *= sqOfSum;
        
        log.info("Prob 6.   (1+2+...)^2 - (1^2 + 2^2 + ..)  {}", sqOfSum - sumSq);
        
        
        primes = Prime.generatePrimes(120000);
        log.info("Prob 7. primes 1st {} 10001 - {}", primes.get(0), primes.get(10000));

        String s = 
        "73167176531330624919225119674426574742355349194934" +
        "96983520312774506326239578318016984801869478851843" +
        "85861560789112949495459501737958331952853208805511" +
        "12540698747158523863050715693290963295227443043557" +
        "66896648950445244523161731856403098711121722383113" +
        "62229893423380308135336276614282806444486645238749" +
        "30358907296290491560440772390713810515859307960866" +
        "70172427121883998797908792274921901699720888093776" +
        "65727333001053367881220235421809751254540594752243" +
        "52584907711670556013604839586446706324415722155397" +
        "53697817977846174064955149290862569321978468622482" +
        "83972241375657056057490261407972968652414535100474" +
        "82166370484403199890008895243450658541227588666881" +
        "16427171479924442928230863465674813919123162824586" +
        "17866458359124566529476545682848912883142607690042" +
        "24219022671055626321111109370544217506941658960408" +
        "07198403850962455444362981230987879927244284909188" +
        "84580156166097919133875499200524063689912560717606" +
        "05886116467109405077541002256983155200055935729725" +
        "71636269561882670428252483600823257530420752963450";
        
        int max = 0;
        for(int i = 0; i < s.length() - 5; ++i) {
            String sub = s.substring(i, i+5);
            int prod = 1;
            for(int j=0; j<5; ++j) {
                prod *= Character.digit( sub.charAt(j), 10 );
            }
            max = Math.max(max, prod);
        }
        
        log.info("Prob 8 {}", max);
        
        outer:
        for(int i = 1; i <= 1000; ++i) {
            for(int j = i + 1; j <= 1000; ++j) {
                int k = 1000 - i - j;
                if (k <= j)
                    continue;
                
                if (k*k == i*i + j*j) {
                    log.info("Prob 9 {} {} {} --- prod {}", i,j,k,i*j*k);
                    break outer;
                }
            
            }
        }
        
        primes = Prime.generatePrimes(2000000);
        
        long sumL = 0;
        for(int p : primes) {
            sumL += p;
        }
        
        log.info("Prob 10 : {}", sumL);
        
        int[][] gridNum = new int[][] { 
              { 8, 02, 22, 97, 38, 15, 00, 40, 00, 75, 04, 05, 07, 78, 52, 12, 50, 77, 91, 8 },     
              { 49, 49, 99, 40, 17, 81, 18, 57, 60, 87, 17, 40, 98, 43, 69, 48, 04, 56, 62, 00 },
              { 81, 49, 31, 73, 55, 79, 14, 29, 93, 71, 40, 67, 53, 88, 30, 03, 49, 13, 36, 65 },
              { 52, 70, 95, 23, 04, 60, 11, 42, 69, 24, 68, 56, 01, 32, 56, 71, 37, 02, 36, 91 },
              { 22, 31, 16, 71, 51, 67, 63, 89, 41, 92, 36, 54, 22, 40, 40, 28, 66, 33, 13, 80 },
              { 24, 47, 32, 60, 99, 03, 45, 02, 44, 75, 33, 53, 78, 36, 84, 20, 35, 17, 12, 50 },
              { 32, 98, 81, 28, 64, 23, 67, 10, 26, 38, 40, 67, 59, 54, 70, 66, 18, 38, 64, 70 },
              { 67, 26, 20, 68, 02, 62, 12, 20, 95, 63, 94, 39, 63, 8, 40, 91, 66, 49, 94, 21 },
              { 24, 55, 58, 05, 66, 73, 99, 26, 97, 17, 78, 78, 96, 83, 14, 88, 34, 89, 63, 72 },
              { 21, 36, 23, 9, 75, 00, 76, 44, 20, 45, 35, 14, 00, 61, 33, 97, 34, 31, 33, 95 },
              { 78, 17, 53, 28, 22, 75, 31, 67, 15, 94, 03, 80, 04, 62, 16, 14, 9, 53, 56, 92 },
              { 16, 39, 05, 42, 96, 35, 31, 47, 55, 58, 88, 24, 00, 17, 54, 24, 36, 29, 85, 57 },
              { 86, 56, 00, 48, 35, 71, 89, 07, 05, 44, 44, 37, 44, 60, 21, 58, 51, 54, 17, 58 },
              { 19, 80, 81, 68, 05, 94, 47, 69, 28, 73, 92, 13, 86, 52, 17, 77, 04, 89, 55, 40 },
              { 04, 52, 8, 83, 97, 35, 99, 16, 07, 97, 57, 32, 16, 26, 26, 79, 33, 27, 98, 66 },
              { 88, 36, 68, 87, 57, 62, 20, 72, 03, 46, 33, 67, 46, 55, 12, 32, 63, 93, 53, 69 },
              { 04, 42, 16, 73, 38, 25, 39, 11, 24, 94, 72, 18, 8, 46, 29, 32, 40, 62, 76, 36 },
              { 20, 69, 36, 41, 72, 30, 23, 88, 34, 62, 99, 69, 82, 67, 59, 85, 74, 04, 36, 16 },
              { 20, 73, 35, 29, 78, 31, 90, 01, 74, 31, 49, 71, 48, 86, 81, 16, 23, 57, 05, 54 },
              { 01, 70, 54, 71, 83, 51, 54, 69, 16, 92, 33, 48, 61, 43, 52, 01, 89, 19, 67, 48 }
        };
        
        max = 0;
        for(int r = 0; r < 20; ++r) {
            for(int c = 0; c < 20; ++c) {
                //Vertical
                if (r <= 16) {
                    int prod = 1;
                    for(int deltaR = 0; deltaR < 4; ++deltaR) {
                        prod *= gridNum[r+deltaR][c];
                    }
                    max = Math.max(max, prod);
                }
                
                //Horizontal
                if (c <= 16) {
                    int prod = 1;
                    for(int deltaC = 0; deltaC < 4; ++deltaC) {
                        prod *= gridNum[r][c+deltaC];
                    }
                    max = Math.max(max, prod);
                }
                
                //Diag
                if (r <= 16 && c <= 16) {
                    int prod = 1;
                    for(int delta = 0; delta < 4; ++delta) {
                        prod *= gridNum[r+delta][c+delta];
                    }
                    max = Math.max(max, prod);
                }
                
                //Other diag
                if (r <= 16 && c >= 3) {
                    int prod = 1;
                    for(int delta = 0; delta < 4; ++delta) {
                        prod *= gridNum[r+delta][c-delta];
                    }
                    max = Math.max(max, prod);
                }
            }
        }
        
        log.info("Prob 11.  Sum {}", max);
        
        /*
        
        for(n = 1; n < 10000000; ++n) {
            long triangle = n * (n+1) / 2;
            
            Set<Long> factors = Sets.newHashSet();
            
            long upperLimit = LongMath.sqrt(triangle,RoundingMode.UP);
            
            for(long factor = 1; factor <= upperLimit; ++factor) {
                if (triangle % factor == 0) {
                    factors.add(factor);
                    factors.add(triangle / factor);
                }
            }
            
            if (factors.size() > 500 ) {
                log.info("Prob 12.  Triangle num {}", triangle);
                break;
            }
            
        }*/
    
        
        ///
        Scanner scanner = new Scanner(Prob1.class.getResourceAsStream("prob12.txt"));
        
        BigInteger sumBI = BigInteger.ZERO;
        
        for(int i = 0; i < 100; ++i) {
            BigInteger next = scanner.nextBigInteger();
            sumBI = sumBI.add(next);
        }
        
        log.info("Prob 13. Sum {}", sumBI.toString().substring(0, 10));
        scanner.close();
        
        /*
        int maxCount = 0;
        
        for(long start = 1; start < 1000000; ++start ) {
            int count = 0;
            long seq = start;
            while(seq != 1) {
                seq = seq % 2 == 0 ? seq / 2 : 3 * seq + 1;
                ++count;
            }
            if (count > maxCount) {
                maxCount = count;
               // log.info("Prob 14.  Seq length {}  start {}", count, start);
            }
        }*/
        
        s = BigInteger.valueOf(2).pow(1000).toString();
        
        sum = 0;
        for(int i = 0; i < s.length(); ++i) {
            sum += Character.digit(s.charAt(i),10);
        }
        
        log.info("Prob 16  sum {}", sum);

        //1 to 10
        int wordCounts[] = {3, 3, 5, 4, 4, 3, 5, 5, 4, 3, 6, "twelve".length(), "thirteen".length(),
                8, 7, 7, 9, "eighteen".length(), 8};
        int tenCounts[] = {"twenty".length(), "thirty".length(), "forty".length(), "fifty".length(),
                "sixty".length(), "seventy".length(), "eighty".length(), "ninety".length() };
        int hundred = "hundred".length();
        int thousand = "thousand".length();
        
        sum = 0;
        for(int i = 1; i <= 1000; ++i) {            
            if (i >= 1000) {
                sum += wordCounts[0] + thousand;
                continue;
            }
            
            int rest = i;
            if (i >= 100) {
                int hundredsDigit = wordCounts[i / 100 - 1] + hundred;
                sum += hundredsDigit;
                rest = i % 100;
            }
            
            if (rest == 0)
                continue;
            
            if (i >= 100) {
                sum += 3; //and
            }
            
            if (rest < 20) {
                sum += wordCounts[rest-1];
                continue;
            }
            
            int tensDigit = rest / 10;
            sum += tenCounts[tensDigit - 2];
            
            int onesDigit = rest % 10;
            
            if (onesDigit > 0) {
                sum += wordCounts[onesDigit-1];
            }
            
            
        }
        
        log.info("Prob 17 : sum {}", sum);
        
        //scanner = new Scanner(Prob1.class.getResourceAsStream("prob18.txt"));
        scanner = new Scanner(Prob1.class.getResourceAsStream("prob67.txt"));
        
        int nodeNum = 1;
        
        List<Integer> maximumPath  = Lists.newArrayList();
        maximumPath.add(scanner.nextInt());
        final int maxRow = 100;
        int globalMaxVal = 0;
        for(int r = 2; r <= maxRow; ++r) {
            for(int c = 1; c <= r; ++c) {
                ++nodeNum;
                int val = scanner.nextInt();
                
                int maxValue = 0;
                //subtract length of previous row
                if (c > 1) {
                    maxValue = Math.max(maxValue, val+maximumPath.get( nodeNum-r-1));
                }
                if (c < r) {
                    maxValue = Math.max(maxValue, val+maximumPath.get( nodeNum-r));
                }
                maximumPath.add(maxValue);
                globalMaxVal = Math.max(globalMaxVal,maxValue);
            }
        }
        
        log.info("Prob 18.  max sum {}", globalMaxVal);
        
        sum = 0;
        for (int y = 1901; y <= 2000; ++y) {
            for(int m = 1; m <= 12; ++m) {
                LocalDate dt = new LocalDate(y, m, 1);
                if (dt.getDayOfWeek() == DateTimeConstants.SUNDAY) {
                    sum++;
                }
                //log.info("Date time {}", dt.getDayOfWeek());
            }
        }
        
        log.info("Prob 19 {}", sum);
        
        
        String hugeFactorial = BigIntegerMath.factorial(100).toString();
        
        sum = 0;
        
        for(int i = 0; i < hugeFactorial.length(); ++i) {
            sum += Character.digit(hugeFactorial.charAt(i),10);
        }
        
        log.info("Prob 20 {}", sum);
        
        int amiable = 0;
        int[] sums = new int[10000];
        for(int i = 1; i < 10000; ++i) {
            
            int upperLimit = IntMath.sqrt(i,RoundingMode.UP);
            
            sum = 0;
            for(int factor = 1; factor <= upperLimit; ++factor) {
                if (i % factor == 0) {
                    sum += factor;
                    sum += i / factor;
                }
            }
            
            //Only proper factors
            sum -= i;
            
            sums[i-1] = sum;
            if (sum < i && sum > 0 && sums[sum-1] == i ) {
                log.info("Found amiable pair n {} : {} and {} : {}", i, sum, sum, sums[sum-1]);
                amiable+=i;
                amiable += sum;
            }
            
        }
        
        log.info("prob 21 : {}", amiable);
        
        
        scanner = new Scanner(Prob1.class.getResourceAsStream("prob22.txt"));
        
        Iterable<String> names = Splitter.on(",").split(scanner.next().replace("\"", ""));
        
        List<String> nameList = Lists.newArrayList(names);
        Collections.sort(nameList);
        
        sum = 0;
        for(int i = 0; i < nameList.size(); ++i) {
            String name = nameList.get(i);
            int alpha = 0;
           for(int c = 0; c < name.length(); ++c) {
               alpha += 1 + name.charAt(c) - 'A';
           }
           sum += (i+1) * alpha;
        }
        
        log.info("Prob 22 sum {}", sum);
        
        
    }

}
