package euler;

import java.math.RoundingMode;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.utils.Prime;

import com.google.common.collect.Lists;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Prob1 {

    final static Logger log = LoggerFactory.getLogger(Prob1.class);
    
    public static void main(String args[]) throws Exception {
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
    }

}
