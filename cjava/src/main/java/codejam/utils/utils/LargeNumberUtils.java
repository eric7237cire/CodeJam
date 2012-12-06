package codejam.utils.utils;

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
}
