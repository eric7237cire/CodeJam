package codejam.y2010.rank_pure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.LargeNumberUtils;

import com.google.common.collect.ComparisonChain;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(InputData input) {

        int caseNumber = input.testCase;
        log.info("Starting calculating case {}", caseNumber);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);

        log.info("Done calculating answer case {}", caseNumber);
        
        int n = input.n;
        
        int maxSetSize = n - 1;
        
       // int check = showPattern(n);
        int count = 0;
        
        for(int size = 1; size <= maxSetSize; ++size) {
            //must be an element equal to size somewhere in the set
            //start it in position size - 1.  N is always at position size
            // size = 4   2 3 4 n  ;  2 4 x n ;  4 x y n
            //            
            
            count += solveSize(size, n);
            count %= MOD;
        }
        
        //n = 6;
        
       // Preconditions.checkState(check == count);

        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + count);
    }
    
    final static int MOD = 100003;
    final static int N_MAX = 500;
    static int[][] memoize = new int[N_MAX+1][N_MAX+1];
    static int[][] combin = LargeNumberUtils.generateModedCombin(500, MOD);
    
    int solveSize(int size, int n) {
        
        if (memoize[size][n] > 0) {
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
        for(int choose = 1; choose <= size - 2 && possible >= choose; ++choose) {
            // a b Size c d n
            // choose is # of vars right of size.
            // must multiply it by how many ways to make a b.  A b must also be perfect
            long mult = solveSize(size - choose - 1, size);
            count += mult * combin[possible][choose] % MOD;
           
            count %= MOD; 
        }
        }
        
        memoize[size][n] = count;
        return count;   
    }
    
    int showPattern(int n) {
        List<Integer> list = new ArrayList<Integer>(n-1);
        
        for(int i = 2; i <= n; ++i) {
            list.add(i);
        }
        /*
Operators    Precedence
postfix expr++ expr--
unary   ++expr --expr +expr -expr ~ !
multiplicative  * / %
additive    + -
shift   << >> >>>
relational  < > <= >= instanceof
equality    == !=
bitwise AND &
bitwise exclusive OR    ^
bitwise inclusive OR    |
logical AND &&
logical OR  ||
ternary ? :
assignment  = += -= *= /= %= &= ^= |= <<= >>= >>>=

         */
        
        List<List<Integer>> pureList = new ArrayList<>();
        
        int allSubSets = (1 << list.size() - 1) - 1;
        int isPureCount = 0;
        
        for(int subSet = allSubSets; subSet >= 0; --subSet) {
            List<Integer> subList = new ArrayList<Integer>(n-1);
            for(int i = 2; i < n; ++i) {
                if (( 1 << i-2 & subSet) != 0  ) {
                    subList.add(i);
                }
            }
            subList.add(n);
            
            boolean isPure = isPure(subList);
            
            if (isPure) {
                ++isPureCount;
                //log.debug("Sub set {}.  ss {}  isPure {}", subList, subSet, isPure);
                pureList.add(subList);
            }
        }
        
       // boolean r = isPure(list);
        
        Collections.sort(pureList, new Comparator<List<Integer>>() {

            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                
                ComparisonChain cc = ComparisonChain.start().compare(o1.size(), o2.size());
                
                if (cc.result() != 0) {
                    return cc.result();
                }
                
                for(int i = 0; i < o1.size(); ++i) {
                    cc = cc.compare(o1.get(i), o2.get(i));
                    }
                
                return cc.result();
            }
            
        });
        
        for(List<Integer> pl : pureList) {
            log.debug("{}", pl);
        }
        
        return isPureCount;
    }
    
    boolean isPure(List<Integer> set) {
        //int start = set.get(set.size() -1);
        
        int value = set.get( set.size() - 1);
        while(true) {
            int rank = set.indexOf(value) + 1;
            if (rank == 1) {
                return true;
            }
           
          if (rank == value) {
              return false;
          } else if (rank == 1) {
              return true;
          }
          value = rank;
        }
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData input = new InputData(testCase);
        input.n = scanner.nextInt();

        return input;

    }
    
}