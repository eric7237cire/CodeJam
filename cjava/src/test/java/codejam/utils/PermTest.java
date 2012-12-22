package codejam.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.utils.PermutationWithRepetition;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;

public class PermTest {
    final static Logger log = LoggerFactory.getLogger(PermTest.class);

public static int sumOfProductAllPermutationsFast(Integer[] numbers, int permLength, int mod) {
        
        long firstChunk = 0;
        
        for(int n = 0; n < numbers.length; ++n) {
            firstChunk += numbers[n];
            firstChunk %= mod;
        }
        
        List<Integer> chunks = new ArrayList<Integer>();
        chunks.add(Ints.checkedCast(firstChunk));
        
        int biggestChunkIndex = IntMath.log2(permLength, RoundingMode.DOWN) ;
                
        for(int i = 1; i <= biggestChunkIndex; ++i) {
            int chunk = Ints.checkedCast((long) chunks.get(i-1) * chunks.get(i-1) % mod);
            chunks.add( chunk ); 
        }
        
        long sum = 1;
        //Now use calculated chunks to finish the job
        while(permLength > 0) {
            int chunkIndex = IntMath.log2(permLength, RoundingMode.DOWN);
            sum *= chunks.get(chunkIndex);
            sum %= mod;
            permLength -= 1 << chunkIndex;
        }
        
        Preconditions.checkState(permLength == 0);
        
        return Ints.checkedCast(sum);
    }
    
    public static int sumOfProductAllPermutations(Integer[] numbers, int permLength, int mod) {
        int[] lastRound = new int[numbers.length];
        for(int n = 0; n < numbers.length; ++n) {
            lastRound[n] = numbers[n];
        }
        
        long lastSum = 1;
        
        for(int i = 0; i < permLength; ++i) {
            long sum = 0;
            for(int n = 0; n < numbers.length; ++n) {
                sum += numbers[n] * lastSum;
                sum %= mod;
            }
            lastSum = sum;            
        }
        
        return Ints.checkedCast(lastSum);
    }
    
    public static int sumOfProductAllPermutationsBruteForce(Integer[] numbersArray, int permLength, int mod) {
        Integer[] perm = new Integer[permLength];
        
        PermutationWithRepetition<Integer> pr = PermutationWithRepetition.create(numbersArray, perm);
        
        long sum = 0;
        while(pr.hasNext())
         {
            pr.next();
            long product = 1;
            for(int i = 0; i < perm.length; ++i) {
                product *= perm[i];
                product %= mod;
            }
            sum+=product;
            sum%=mod;
        } 
        
        return Ints.checkedCast(sum);
    }
    
    @Test
    public void testPermsRepetition() {
        Integer[] possible = new Integer[] {3, 7, 12};
        
        Integer[] perm = new Integer[2];
        
        PermutationWithRepetition<Integer> pr = PermutationWithRepetition.create(possible, perm);
        
        pr.next();
        assertArrayEquals(new Integer[] {3,3}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {7,3}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {12,3}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {3,7}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {7,7}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {12,7}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {3,12}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {7,12}, perm);
        pr.next();
        assertArrayEquals(new Integer[] {12,12}, perm);
        
        assertEquals(false, pr.hasNext());
    }
    
    @Test
    public void testSumProduct() {
        Integer[] possible = new Integer[] {2,3};
        
        int mod = 1000000007;
        
        int s = sumOfProductAllPermutationsBruteForce(possible,1,mod);
        
        assertEquals(5, s);
        
        s = sumOfProductAllPermutationsBruteForce(possible,2,mod);
        assertEquals(25, s);
        
        s = sumOfProductAllPermutations(possible, 2,mod);
        assertEquals(25, s);
        
        possible = new Integer[] {4, 0, 3, 27, 14, 18, 0, 4, 99, 17, 20, 30};
        for(int i = 1; i < 5; ++i) {
            int s1 = sumOfProductAllPermutationsBruteForce(possible,i,mod);
            int s2 = sumOfProductAllPermutations(possible, i,mod);
            assertEquals(s1,s2);
        }
        
        //possible = new Integer[] {4, 2, 3, 5,0};
        possible = new Integer[] {4, 0, 3, 27, 14, 18, 0, 4, 99, 17, 20, 30};
        
        int a1 = sumOfProductAllPermutations(possible, 2,mod);
        int a2 = sumOfProductAllPermutations(possible, 4,mod);
        
        int a1_c = sumOfProductAllPermutationsBruteForce(possible,2,mod);
        int a2_c = sumOfProductAllPermutationsBruteForce(possible,4,mod);
        
        assertEquals(a1_c, a1);
        assertEquals(a2_c, a2);
        int a3 = Ints.checkedCast(((long)a1*a1) % mod);
        
        assertEquals(a2, a3);
        
        long start = System.currentTimeMillis();
        long stop = System.currentTimeMillis();
        for(int i = 3000000; i < 10000000; i += 1000000) {
            
            if (i <= 6) {
            start = System.currentTimeMillis();
            int slo1 = sumOfProductAllPermutationsBruteForce(possible,i,mod);
            stop = System.currentTimeMillis();
            log.debug("Slowest {} took {} ms", slo1, stop-start);
            }
            
            start = System.currentTimeMillis();
        int s1 = sumOfProductAllPermutations(possible, i,mod);
        stop = System.currentTimeMillis();
        log.debug("Slow {} took {} ms", s1, stop-start);
        
        
        start = System.currentTimeMillis();
        int f1 = sumOfProductAllPermutationsFast(possible, i, mod);
        stop = System.currentTimeMillis();
        log.debug("Fast {} took {} ms\n", f1, stop-start);
        
        assertEquals(s1, f1);
        }
    }
}
