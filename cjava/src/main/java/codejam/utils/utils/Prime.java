package codejam.utils.utils;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

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
        
        if (n <= 1) {
            return false;
        }
        int upperLimit = DoubleMath.roundToInt(Math.sqrt(n), RoundingMode.DOWN);
        
        for(int prime : knownPrimes) {
            if (prime > upperLimit) 
                return true;
            
            if (n % prime == 0)
                return false;
        }
        
        return true;
    }
    
    public static boolean isPrime(int n) {
        return miller_rabin_32(n);
    }
    
    private static int modular_exponent_32(int base, int power, int modulus) {
        long result = 1;
        for (int i = 31; i >= 0; i--) {
            result = (result*result) % modulus;
            if ((power & (1 << i)) != 0) {
                result = (result*base) % modulus;
            }
        }
        return (int)result; // Will not truncate since modulus is an int
    }


    private static boolean miller_rabin_pass_32(int a, int n) {
        int d = n - 1;
    int s = Integer.numberOfTrailingZeros(d);
    d >>= s;
        int a_to_power = modular_exponent_32(a, d, n);
        if (a_to_power == 1) return true;
        for (int i = 0; i < s-1; i++) {
            if (a_to_power == n-1) return true;
            a_to_power = modular_exponent_32(a_to_power, 2, n);
        }
        if (a_to_power == n-1) return true;
        return false;
    }

    public static boolean miller_rabin_32(int n) {
        if (n <= 1) return false;
        else if (n == 2) return true;
        else if (miller_rabin_pass_32( 2, n) &&
            (n <= 7  || miller_rabin_pass_32( 7, n)) &&
            (n <= 61 || miller_rabin_pass_32(61, n)))
            return true;
        else
            return false;
    }
    
    private static final Random rnd = new Random();

    private static boolean miller_rabin_pass(BigInteger a, BigInteger n) {
        BigInteger n_minus_one = n.subtract(BigInteger.ONE);
        BigInteger d = n_minus_one;
    int s = d.getLowestSetBit();
    d = d.shiftRight(s);
        BigInteger a_to_power = a.modPow(d, n);
        if (a_to_power.equals(BigInteger.ONE)) return true;
        for (int i = 0; i < s-1; i++) {
            if (a_to_power.equals(n_minus_one)) return true;
            a_to_power = a_to_power.multiply(a_to_power).mod(n);
        }
        if (a_to_power.equals(n_minus_one)) return true;
        return false;
    }

    public static boolean miller_rabin(BigInteger n) {
        for (int repeat = 0; repeat < 20; repeat++) {
            BigInteger a;
            do {
                a = new BigInteger(n.bitLength(), rnd);
            } while (a.equals(BigInteger.ZERO));
            if (!miller_rabin_pass(a, n)) {
                return false;
            }
        }
        return true;
    }
}
