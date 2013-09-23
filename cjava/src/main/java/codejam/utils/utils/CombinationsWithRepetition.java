package codejam.utils.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.math.IntMath;

public class CombinationsWithRepetition {

    final protected static Logger log = 
            LoggerFactory.getLogger("main");

    public static void main(String[] args)
    {
        //(n+k-1)!/(k!(n-1)!)
        int n = 4;
        int k = 5;
        
        int listSize = IntMath.factorial( n+k-1)
                / ( IntMath.factorial(k) * IntMath.factorial(n-1));
        
        int[] ar = new int[k];
        long test = IntMath.pow(2, k) - 1;
        for(int i = 0; i < listSize; ++i)
        {
            log.debug("Test {}", Long.toBinaryString(test));
            log.debug("i {} : {}", i+1, (Object) ar);
           // next_combo(ar, n, k);
            long[] arl = new long[] {test};
            next_combination(arl);
            test = arl[0];
        }
    }
    /*
     * This algorithm produces the combinations in lexicographic order, with the elements in each combination in increasing order.

To find the next multicombination containing k elements from a set containing n elements, begin with the multicombination containing k zeroes, then at each step:

Find the rightmost element that is less than n - 1
Increment it.
Make the elements after it the same.

Choose from 0 to n - 1
multisets of size k
     */
    public static void next_combo(int[] ar, int k, int minimum, int maximum)
    {
        int i, lowest_i = 0;

     //   for (i=lowest_i=0; i < k; ++i)
       //     lowest_i = (ar[i] < ar[lowest_i]) ? i : lowest_i;
        for(i = k-1; i >= 0; --i)
        {
            if (ar[i] != ar[k-1])
            {
                lowest_i = i + 1;
                break;
            }
        }

        ++ar[lowest_i];

        i = (ar[lowest_i] > maximum) 
            ? 0           // 0 -> all combinations have been exhausted, reset to first combination.
            : lowest_i+1; // _ -> base incremented. digits to the right of it are now zero.

        for (; i<k; ++i)
            ar[i] = minimum;  
    }
    
 // find next k-combination
    /*
     * Each 0 means add 1
     */
    static boolean next_combination( long[] xar) // assume x has form x'01^a10^b in binary
    {
        long x = xar[0];
       long u = x & -x; // extract rightmost bit 1; u =  0'00^a10^b
       long v = u + x; // set last non-trailing bit 0, and clear to the right; v=x'10^a00^b
      if (v==0) // then overflow in v, or x==0
        return false; // signal that next k-combination cannot be represented
      x = v +(((v^x)/u)>>2); // v^x = 0'11^a10^b, (v^x)/u = 0'0^b1^{a+2}, and x ← x'100^b1^a
      xar[0] = x;
      return true; // successful completion
    }
}
