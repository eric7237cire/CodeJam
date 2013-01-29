package codejam.y2010.round_1B.rank_pure;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.LargeNumberUtils;

public class Main implements TestCaseHandler<InputData>,
TestCaseInputScanner<InputData>, DefaultInputFiles {

    
    @Override
    public String[] getDefaultInputFiles() {
     //  return new String[] { "sample.in" };
     //   return new String[] { "A-small-practice.in" };
       return new String[] { "C-small-practice.in", "C-large-practice.in" };
    }
    
    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    static int[][] memoize;
    
    //bottom up
    public String handleCase(InputData in) {
      
        
        //[n][size] = count
        if (memoize == null) {
        memoize = new int[N_MAX+1][N_MAX];
        
                
        memoize[2][1] = 1;
        
        for(int n = 3; n <= N_MAX; ++n) {
            
            memoize[n][1] = 1;
            memoize[n][2] = 1;
            
            for(int size = 3; size < n; ++size) {
                
                /**
                 * Maximum Numbers between n and size ; 
                 * 
                 * example, n = 7, size = 5, there is between 0 and 1 between them
                 * 
                 * This is also the pool of numbers that can be put between 
                 * using choose n k 
                 */
                final int possible = n - size - 1;
                int count = 1;  //2 Size N is always possible
                
                /**
                 * After we pick / remove N, the next element is the rank of N
                 * which is the size of the set, ie 2,3,12 with N=12 ; next is 3
                 * 
                 * choose how many elements are between size and n
                 * 
                 * it cannot be <= size - 2 since we must have room for size and n 
                 * 
                 * The left of choose must itself be pure, the right can be any
                 * combination of the numbers between n and size
                 */
                
                for(int choose = 1; choose <= size - 2 && choose <= possible ; ++choose) {
                    // a b Size c d n
                    // choose is # of vars right of size.
                    // must multiply it by how many ways to make a b.  A b must also be perfect
                    //Can be zero legitimately
                   // Preconditions.checkState(memoize[size][size - choose - 1] != 0);
                    
                    //size becomes the new n
                    long mult = memoize[size][size - choose - 1];
                    count += mult * combin[possible][choose] % MOD;
                   
                    count %= MOD; 
                }
                
                memoize[n][size] = count;
                
            }
        }
        
        }
        
        int total = 0;
        for(int size = 1; size < in.n; ++size) {
            total += memoize[in.n][size];
            total %= MOD;
        }
        
        return ("Case #" + in.testCase + ": " + total);
        
    }
    
    public String handleCase2(InputData input) {

        int caseNumber = input.testCase;
                
        final int n = input.n;
        
        final int maxSetSize = n - 1;
        
        /*
         * 
  See BruteForce for an example of n = 12
         */
        
        int count = 0;
        
        if (memoize == null) {
         memoize = new int[N_MAX+1][N_MAX+1];
        for(int[] a : memoize) {
            Arrays.fill(a, -1);
        }
        }
        
        /*
        for(int i = 1; i <= n; ++i) {    
            memoize[1][i] = 1; 
        }
    
        
        for(int size = 2 ; size <= maxSetSize; ++size) {
            for(int i = 1; i <= size+1; ++i) {
                //int possible = n - size - 1;
                //n - size - 1 > 0
                //n > size+1
                //so n must be > 
                memoize[size][i] = 1; 
            }
        }*/
        
        for(int size = 1; size <= maxSetSize; ++size) {
            //must be an element equal to size somewhere in the set
            //start it in position size - 1.  N is always at position size
            // size = 4   2 3 4 n  ;  2 4 x n ;  4 x y n
            //            
            
            count += solveSize(size, n);
            count %= MOD;
        }
        
        return ("Case #" + caseNumber + ": " + count);
    }
    
    final static int MOD = 100003;
    final static int N_MAX = 500;
    //static int[][] memoize = new int[N_MAX+1][N_MAX+1];
    static int[][] combin = LargeNumberUtils.generateModedCombin(500, MOD);
    
    int solveSize(int size, int n) {
        
        if (memoize[size][n] >= 0) {
            return memoize[size][n];
        }
        if (size == 1) {
            return 1;
        }
        int count = 0;
        count ++;  // for 2...size n
        
        //# of elements between size and n
        int possible = n - size - 1;
        if (possible > 0) {
            
            /**
             * After we pick / remove N, the next element is the rank of N
             * which is the size of the set, ie 2,3,12 with N=12 ; next is 3
             * 
             * choose is where we put this element = size
             * 
             * The left of choose must itself be pure, the right can be any
             * combination of the numbers between n and size
             */
            
            for(int choose = 1; choose <= size - 2 && possible >= choose; ++choose) {
                // a b Size c d n
                // choose is # of vars right of size.
                // must multiply it by how many ways to make a b.  A b must also be perfect
                long mult = solveSize(size - choose - 1, size);
                //Preconditions.checkState(memoize[size - choose - 1][size] != -1);
               // long mult = memoize[size - choose - 1][size];
                count += mult * combin[possible][choose] % MOD;
               
                count %= MOD; 
            }
        }
        
        memoize[size][n] = count;
        return count;   
    }
    
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData input = new InputData(testCase);
        input.n = scanner.nextInt();

        return input;

    }
    
}