package euler;

import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.math.IntMath;

public class Util {

    final static Logger log = LoggerFactory.getLogger(Prob1.class);
    
    static int calculatePhi(List<Integer> primes, int num) {
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
        
        return phi;
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
    
    /*
     * dividend / divisor
     */
    
    static int getRepetitionLength(final int divisor, int dividend) {
        double ans = 0;

        //final int numDigits = countDigits(dividend);
        
       // int origDividend = dividend;
        int factor = 1;

        List<Integer> remainders = Lists.newArrayList();
        List<Integer> remToAns = Lists.newArrayList();
        List<Integer> decDigits = Lists.newArrayList();
        StringBuffer ansSb = new StringBuffer();
        
        if (dividend >= divisor) {
            int intPart = dividend / divisor;
            dividend = dividend % divisor;
            ansSb.append(intPart);
            ansSb.append(".");
        } else {
            ansSb.append("0.");
        }
        
        int ansLen = 0;
        
        if (dividend == 0) {
            ansSb.append(0);
            log.debug("Ans: {} sb {}", ans, ansSb);
            return 0;
        }
        
        while (true) {

            
            while (dividend < divisor) {
                factor *= 10;
                dividend *= 10;
                ansLen ++;
            }
            
            int index = remainders.indexOf(dividend);
            // 0.803(571428)
            if (index != -1) {
                
                /*
                log.debug("Ans {} rep len {} for {} / {}", ans, 
                        remainders.size() - index,
                        origDividend, divisor);
                        */
                        
               
                
                
                StringBuffer ansStr = new StringBuffer( Double.toString(ans) );
                int strIdx = remToAns.get(index);
                ansSb.insert(strIdx, "(");
                ansSb.append(")");
                log.debug("Ans: {}  {}  sb {}", ans, ansStr, ansSb);
                return remainders.size() - index;
            }
            
            remToAns.add(ansSb.length());
            remainders.add(dividend);
            
            int d = dividend / divisor;
             
            ansSb.append(StringUtils.leftPad(Integer.toString(d), ansLen, "0"));
            
            ansLen = 0;
            decDigits.add(d);
            //Too inaccurate
            ans += (double) d / factor;
            
            int remainer = dividend - d * divisor;
            
            dividend = remainer;

            if (dividend == 0) {
               // ansStr.insert(numDigits, ".");
                log.debug("Ans: {} {} ",  ans, ansSb);
                
                return 0;
            }
        }
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
   public static int findM(int rad, int num, int denom) {
        double v = (Math.sqrt(rad) + num) / denom;
        int m = (int) v;
        
        return m;
    }
    public static List<Integer> findConFrac(int rad) {
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
    
    static public int countDigits(int num) {
        int digits = num;
        int count = 0;

        while (digits > 0) {
            //int digit = digits % 10;
            digits /= 10;
            ++count;
        }
        return count;
    }
    
    public static int getRandBetween(int Min, int Max, Random r)
    {
        return Min + (int)(r.nextDouble() * ((Max - Min) + 1));
    }
}
