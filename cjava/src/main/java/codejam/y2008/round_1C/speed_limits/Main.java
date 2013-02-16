package codejam.y2008.round_1C.speed_limits;

import java.util.Arrays;
import java.util.Scanner;

import codejam.utils.datastructures.FenwickTree;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Main()
    {
        super("C", 1, 1, 0);
      //  (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        in.n = scanner.nextInt();
        
        in.m = scanner.nextInt();
        in.X = scanner.nextInt();
        in.Y = scanner.nextInt();
        in.Z = scanner.nextInt();
        
        in.A = new int[in.m];
        for(int i = 0; i < in.m; ++i)
        {
            in.A[i] = scanner.nextInt();
        }
        
        in.speedLimits = new int[in.n];
        
        for(int i = 0; i < in.n; ++i)
        {
            in.speedLimits[i] = in.A[i % in.m];
            in.A[i % in.m] = (int) ( (in.X * in.A[i % in.m] + in.Y * (i+1)) % in.Z ); 
        }
        return in;
    }

    public String handleCaseSmall(InputData in) {
       
        int[] count = new int[in.n];
        int sum = 0;
        log.debug("{}", in.speedLimits);
        int mod = 1000000007;
        
        for(int i = in.n-1; i >= 0; --i)
        {
            count[i]  = 1;
            for(int j = i + 1; j < in.n; ++j) 
            {
                if (in.speedLimits[i] < in.speedLimits[j]) {
                    count[i] = count[i] + count[j];
                    count[i] %= mod;
                }
            }
            
            sum += count[i];
            sum %= mod;
        }
        
                
        return String.format("Case #%d: %d ", in.testCase, sum);
        
    }
    
   
    @Override
    public String handleCase(InputData in) {
       
        int[] count = new int[in.n];
        int sum = 0;
        log.debug("{}", in.speedLimits);
        int mod = 1000000007;
        
        //Normalize input
        
        int[] sorted = Arrays.copyOf(in.speedLimits, in.n);
        
        Arrays.sort(sorted);
        
        int[] index = new int[in.n];
        
        for(int i = 0; i < in.n; ++i)
        {
            //Fenwick tree is 1 based so add one
            index[i] = Arrays.binarySearch(sorted, in.speedLimits[i]) + 1;
        }
        
        //Add +1 just to not have to special case queries past n
        int[] ft = FenwickTree.ft_create(in.n+1);
        /*
        log.debug("Orig {}", in.speedLimits);
        log.debug("Sorted {}", sorted);
        log.debug("n= {} Sorted indexes {}", in.n, index);
        */
        for(int i = in.n-1; i >= 0; --i)
        {
            int sortedIndex = index[i];
            
            int rq = FenwickTree.ft_rsq(ft, sortedIndex+1, in.n+1, mod);
            
            count[i] = rq + 1;
            sum += count[i];
            
            FenwickTree.ft_adjust_mod(ft, sortedIndex, count[i], mod);
            /*
            log.debug("FT {}", ft);
            for(int j = in.n; j >= 1; --j)
            {
                log.debug("FT {} = {}", j, FenwickTree.ft_rsq(ft, j, mod));
            }*/
            
            sum %= mod;
        }
        
                
        return String.format("Case #%d: %d ", in.testCase, sum);
        
    }
}