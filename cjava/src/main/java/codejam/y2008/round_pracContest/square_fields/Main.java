package codejam.y2008.round_pracContest.square_fields;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
       //super();
        super("C", 1,1);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.F = scanner.nextLong();
        in.D = scanner.nextLong();
        in.B = scanner.nextLong();
        
        return in;
    }
    
    static long[][] memo;
    
    /**
     * Given bDlimit[ b ] = first D value such that # of floors is above 2 ^ 32
     */
    static long[] bDlimit;
    
    static long findFMax(long D, long B) {
        
        if (D == 0 || B == 0)
            return 0;
        
        if (D < memoDSize && B < memoBSize && memo[(int)D][(int)B] != 0)
            return memo[(int)D][(int)B];
    
        
      //  log.debug("Calc fmax d {} b {}", D, B);
        
        long ans;
        
        if (B == 1) {
            /**
             * If we can break only 1 egg, then we can test D levels,
             * starting at the first floor and going up
             */
            ans = D;
            memoAns(D, B, ans);
            return ans;
        }
        if (B == 2) {
            /**
             * We make a partition like D-1, D-2, D-3, D-4 to 1
             * Say D is 6, so F(6, 2)
             *   F(6, 2) = F(5,1) + 1 + F(5,2)  == 5 + 1 + 15 == 21
             *   F(5, 2) = F(4,1) + 1 + F(4,2)  == 4 + 1 + 10 == 15
             *   F(4, 2) = F(3,1) + 1 + F(3,2)  == 3 + 1 + 6 == 10
             *   F(3, 2) = F(2,1) + 1 + F(2,2)  == 2 + 1 + 3 == 6 
             *   F(2, 2) = F(1,1) + 1 + F(1,2)  == 3
             *   F(1, 1) =                      == 1
             *   
             *   which is the sum formula
             */
            ans = D*(D+1)/2; 
            memoAns(D, B, ans);
            return ans;
        }
        
        /**
         * If we can break the same or more than we can drop, it is
         * a binary search
         */
        if (B >= D) {
            //Can do a binary search
            if (D > 32) {
                ans = -1;
                memoAns(D, B, ans);
                return ans;
            }
            
            ans = (1L << D) - 1;
            memoAns(D, B, ans);
            return ans;
        }
        
        if (B >= memoBSize ) //&& D >= 1000) {
            return -1;
        
        
        Preconditions.checkState(B < memoBSize);
        
        if (bDlimit[(int)B] != 0 && D >= bDlimit[(int)B]) {
            
            ans = -1;
            memoAns(D, B, ans);            
            return -1;
        }
        
        /**
         * Try the egg at some floor N,
         * if it breaks, we can check F(D-1, B-1) floors below N
         * if it does not break, we can check F(D-1, B) floors above N
         */
        long bottomPartition = findFMax(D - 1, B - 1);
        long topPartition = findFMax(D - 1, B);
                
        if (bottomPartition == -1 || topPartition == -1) {
            ans = -1;
        } else {
                  
            ans = 1 + bottomPartition + topPartition;
        
            if (ans >= maxF) {
                ans = -1;
            }
        }
        
        memoAns(D, B, ans);
        
        return ans;
    }
    
    private static void memoAns(long D, long B, long ans) {
        if (D < memoDSize && B < memoBSize )
            memo[(int)D][(int)B] = ans;
    }
    
    final static long maxF = 4294967296L; 
    
    /**
     * Start at 1 and keep going until fMax is higher than the given F
     * 
     */
    long findDMin(long F, long B)
    {
        Preconditions.checkArgument(F >= 0);
        Preconditions.checkArgument(B >= 0);
        long last = -2;
        
        int d = 1;
        
        while(true) {
            long f = findFMax(d, B); 
            
            if (f >= F || f == -1) {
                return d;
            }
            
            ++d;
            Preconditions.checkState(f > last);
            last = f;
        }
        
    }
    
    /**
     * Same idea as Dmin
     */
    long findBMin(long F, long D) {
        Preconditions.checkArgument(F >= 0);
        Preconditions.checkArgument(D >= 0);
        long last = -2;
        
        int b = 1;
        
        while(true) {
            long f = findFMax(D, b); 
            
            if (f >= F || f == -1) {
                return b;
            }
            
            ++b;
            Preconditions.checkState(f > last);
            last = f;
        }
    }
        
    /**
     * Figured out experimentally.  After around D = 3000, B = 3 will be > 2 ^ 32
     * 
     * As B gets higher, the number of eggs available to drop is less.  
     */
    final static int memoDSize = 3000;
    final static int memoBSize = 200;
    
    static {
        memo = new long[memoDSize][memoBSize];
        
        bDlimit = new long[memoBSize];
        
        /**
         * Found out smallest D value such that fMax >= 2^32
         */
        for( int b = 3; b < memoBSize; ++b ) {
            long d = 1;
            
            long f = findFMax(d, b);
            
            while(true) {
                if (f == -1) {
                    bDlimit[b] = d;
                    break;
                }
                
                ++d;
                f = findFMax(d, b);
            }
        }
    }
    @Override
    public String handleCase(InputData in)
    {
        if (memo == null) {
            
        }
        long Fmax = findFMax(in.D,in.B);
        long Dmin = findDMin(in.F,in.B);
        long Bmin = findBMin(in.F,in.D);
        
        return  String.format("Case #%d: %d %d %d", in.testCase, Fmax, Dmin,Bmin);
        
    }
    

}