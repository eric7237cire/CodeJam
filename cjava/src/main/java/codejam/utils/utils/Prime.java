package codejam.utils.utils;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.google.common.math.DoubleMath;

public class Prime {
    /**
     * Generate all primes between 1 and n inclusive
     * @param n
     * @return the list of primes
     */
    public static List<Integer> generatePrimes(int n) {
        BitSet isPrime = new BitSet();
        isPrime.flip(0, n+1);
        isPrime.set(0, 2, false);
        
        List<Integer> primes = new ArrayList<Integer>();

        //Since we are eliminating via prime factors, a factor is at most sqrt(n)
        int upperLimit = DoubleMath.roundToInt(Math.sqrt(n), RoundingMode.UP);
        for(int i = 2; i <= upperLimit; ++i) {
            if (!isPrime.get(i)) {
                continue;
            }
            
            //Loop through all multiples of the prime factor i.  Start with i*i, because the rest
            //were already covered by previous factors.  Ex, i == 7, we start at 49 because 7*2 through 7*6 
            //we already covered by previous prime factors.
            for(int j = i * i; j <= n; j += i) {
                isPrime.set(j, false);
            }
        }
        
        //Use BitSet suggestion for looping through set bits
        for (int i = isPrime.nextSetBit(0); i >= 0; i = isPrime.nextSetBit(i+1)) {
            primes.add(i);
        }
        
        
        return primes;
    }
    
    public static boolean isPrime(int n, List<Integer> knownPrimes) {
        int upperLimit = DoubleMath.roundToInt(Math.sqrt(n), RoundingMode.DOWN);
        
        for(int prime : knownPrimes) {
            if (prime > upperLimit) 
                return true;
            
            if (n % prime == 0)
                return false;
        }
        
        return true;
    }
}
