package codejam.utils.utils;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class LargeNumberUtils {
    public static int[][] generateModedCombin(int max, int modulo) {
        
        int[][] combinations = new int[max+1][max+1];
        for(int n = 0; n <= max; ++n)
            for(int k = 0; k <= max; ++k)
        {
            if (n<k)
                combinations[n][k] =0;
            else if (n==k || k==0 )
                combinations[n][k] = 1;
            else
                combinations[n][k] = (combinations[n-1][k] + combinations[n-1][k-1])%modulo;
        }
        
        return combinations;
    }
    
    public static int[][] generateModedPerum(int max, int modulo) {
        int[][] permutations = new int[max+1][max+1];
        
        for(int n = 0; n <= max; ++n) {
            for(int k = 0; k <= max; ++k)
            {
                
                if (n < k)
                    permutations[n][k] = 0;
                
                else if (k==0)
                    permutations[n][k] = 1;
                
                else 
                    permutations[n][k] =  Ints.checkedCast( ( (long) (n-k+1) * permutations[n][k-1] ) % modulo ); 
                    
                
            }
        }
        
        return permutations;
    }
    
    public static int safeMult(int a, int b, int MOD)
    {
        return Ints.checkedCast(LongMath.checkedMultiply(a, b) % MOD);
    }
    
    /* This function calculates (a to power b)%MOD */
    public static long pow(int a, int b, int MOD) {

        long x = 1, y = a;

        while (b > 0)
        {
            if (b % 2 == 1)
            {
                x = (x * y);

                if (x > MOD)
                    x %= MOD;
            }

            y = (y * y);

            if (y > MOD)
                y %= MOD;

            b /= 2;
        }

        return x;

    }
    
     
    
    /*  Modular Multiplicative Inverse
    
        Using Euler's Theorem
    
        a^(phi(m)) = 1 (mod m)
    
        a^(-1) = a^(m-2) (mod m) */
    
    public static long InverseEuler(int n, int MOD)    
    {    
        return pow(n,MOD-2,MOD);    
    }
    
    public static int[] generateModFactorial(int n, int MOD) {
        int[] f = new int[n+1];
        f[0] = 1;
        f[1] = 1;
        
        for(int i = 2; i <= n; ++i) {
            f[i] = Ints.checkedCast( LongMath.checkedMultiply(f[i-1], i) % MOD );
        }
        
        return f;
    }
     
    /**
     * 
     * @param n
     * @param k
     * @param MOD
     * @param f  precalculated modded factorial array
     * @return  n choose r
     */
    public static int choose(int n, int k, int MOD, int[] f)    
    {
        Preconditions.checkState(k <= n);
        Preconditions.checkState(k >= 0 && n >= 0);
        
        return Ints.checkedCast( LongMath.checkedMultiply(f[n], 
                Ints.checkedCast((InverseEuler(f[k], MOD) * InverseEuler(f[n-k], MOD)) % MOD)) 
                % MOD);
        
        //return (f[n]*( (InverseEuler(f[r], MOD) * InverseEuler(f[n-r], MOD)) % MOD)) % MOD;    
    }

}
