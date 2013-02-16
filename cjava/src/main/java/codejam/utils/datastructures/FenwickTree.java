package codejam.utils.datastructures;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Preconditions;

public class FenwickTree
{

    private static int LSOne(int S)
    {
        return (S & (-S));
    }

    public static int[] ft_create(int n)
    {
        return new int[n + 1];
    }

    public static int findLowestIndexWithFreq(int[] ft, int cumFre){
        int idx = 0; // this var is result of function
        
        int maxVal = ft.length - 1;
        
        int bitMask = Integer.highestOneBit(maxVal);
        
        int lowestFound = ft.length + 10;
                
        while ((bitMask != 0) && (idx < maxVal)){ // nobody likes overflow :)
            int tIdx = idx + bitMask; // we make midpoint of interval
            if (cumFre == ft[tIdx]) // if it is equal, we just return idx
            {
                lowestFound = Math.min(lowestFound, tIdx);
            }
            
            if (cumFre > ft[tIdx]){ 
                    // if tree frequency "can fit" into cumFre,
                    // then include it
                idx = tIdx; // update index 
                cumFre -= ft[tIdx]; // set frequency for next loop 
            }
            bitMask >>= 1; // half current interval
        }
        
        if (lowestFound <= maxVal)
            return lowestFound;
        
        
        return -1;
        
    }

    
    public static int findAnyIndexWithFreq(int[] ft, int cumFre){
        int idx = 0; // this var is result of function
        
        int maxVal = ft.length;
        
        int bitMask = Integer.highestOneBit(maxVal);
                
        while ((bitMask != 0) && (idx < maxVal)){ // nobody likes overflow :)
            int tIdx = idx + bitMask; // we make midpoint of interval
            if (cumFre == ft[tIdx]) // if it is equal, we just return idx
                return tIdx;
            else if (cumFre > ft[tIdx]){ 
                    // if tree frequency "can fit" into cumFre,
                    // then include it
                idx = tIdx; // update index 
                cumFre -= ft[tIdx]; // set frequency for next loop 
            }
            bitMask >>= 1; // half current interval
        }
        if (cumFre != 0) // maybe given cumulative frequency doesn't exist
            return -1;
        else
            return idx;
    }

    
    public static int ft_rsq(int[] ft, int b, int mod)
    { // returns RSQ(1, b)
        int sum = 0;
        for (; b > 0; b -= LSOne(b))
        {
            sum += ft[b];
            sum %= mod;
        }
        return sum;
    }

    public static int ft_rsq(int[] ft, int a, int b, int mod)
    { // returns RSQ(a, b)
        Preconditions.checkState(a <= b);
        if (a==1)
            return ft_rsq(ft, b, mod);
        
        return (mod + ft_rsq(ft, b, mod) - 
                 ft_rsq(ft, a - 1, mod)) % mod;
    }

    // adjusts value of the k-th element by v (v can be +ve/inc or -ve/dec)
    public static void ft_adjust(int[] ft, int k, int v, int mod)
    { 
        //Indexes go from 1 to N
        Preconditions.checkState(k > 0);
        
        Preconditions.checkState(k <= ft.length);
        
        // note: n = ft.size() - 1
        for (; k < ft.length; k += LSOne(k))
        {
            ft[k] += v;
            ft[k] %= mod;
        }
    }

    
    @Test
    public void test() {
        int mod = 10000007;
        int[] ft = ft_create(10);
        
        
        FenwickTree.ft_adjust(ft, 1, 3, mod);
        FenwickTree.ft_adjust(ft, 5, 2, mod);
        FenwickTree.ft_adjust(ft, 8, 9, mod);
        
        assertEquals(5, FenwickTree.ft_rsq(ft, 5, mod) );
        assertEquals(14, FenwickTree.ft_rsq(ft, 8, mod) );
        assertEquals(3, FenwickTree.ft_rsq(ft, 1, mod) );
    }
    
    public static void main(String[] args)
    {
        // idx   0 1 2 3 4 5 6 7  8 9 10, no index 0!
        int[]  ft;
        int mod = 10000007;
        ft = ft_create(10);     // ft = {-,0,0,0,0,0,0,0, 0,0,0}
        ft_adjust(ft, 2, 1,mod);    // ft = {-,0,1,0,1,0,0,0, 1,0,0}, idx 2,4,8 => +1
        ft_adjust(ft, 4, 1,mod);    // ft = {-,0,1,0,2,0,0,0, 2,0,0}, idx 4,8 => +1
        ft_adjust(ft, 5, 2,mod);    // ft = {-,0,1,0,2,2,2,0, 4,0,0}, idx 5,6,8 => +2
        ft_adjust(ft, 6, 3,mod);    // ft = {-,0,1,0,2,2,5,0, 7,0,0}, idx 6,8 => +3
        ft_adjust(ft, 7, 2,mod);    // ft = {-,0,1,0,2,2,5,2, 9,0,0}, idx 7,8 => +2
        ft_adjust(ft, 8, 1,mod);    // ft = {-,0,1,0,2,2,5,2,10,0,0}, idx 8 => +1
        ft_adjust(ft, 9, 1,mod);    // ft = {-,0,1,0,2,2,5,2,10,1,1}, idx 9,10 => +1
        System.out.printf("%d\n", ft_rsq(ft, 1, 1));  // 0 => ft[1] = 0
        System.out.printf("%d\n", ft_rsq(ft, 1, 2));  // 1 => ft[2] = 1
        System.out.printf("%d\n", ft_rsq(ft, 1, 6));  // 7 => ft[6] + ft[4] = 5 + 2 = 7
        System.out.printf("%d\n", ft_rsq(ft, 1, 10)); // 11 => ft[10] + ft[8] = 1 + 10 = 11
        System.out.printf("%d\n", ft_rsq(ft, 3, 6));  // 6 => rsq(1, 6) - rsq(1, 2) = 7 - 1

        ft_adjust(ft, 5, 2,mod); // update demo
        System.out.printf("Index: ");
        for (int i = 0; i < (int) ft.length; i++)
            System.out.printf("%d ", i);
        System.out.printf("\n");
        System.out.printf("FT   : ");
        for (int i = 0; i < (int) ft.length; i++)
            System.out.printf("%d ", ft[i]);
        System.out.printf("\n");
    }

}
