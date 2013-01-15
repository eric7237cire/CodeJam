package euler;

import java.math.RoundingMode;
import java.util.List;

import com.google.common.math.IntMath;

public class Util {

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

}
