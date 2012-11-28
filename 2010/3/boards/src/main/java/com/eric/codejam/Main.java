package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import mod.GCD;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    final static int MAX_BOARD_LENGTH = 100000;
    final static int MAX_N = 100;
    
    static public void solve(int a, int b, long L) {
        int gcd_ab = IntMath.gcd(a,b);
        int[] st = GCD.gcdExtended(a,b);
        Preconditions.checkState(st[0] == gcd_ab);
        int s = st[1];
        int t = st[2];
        
        if (L % gcd_ab != 0) {
            log.debug("No solutions");
            return;
        }
        
        long x_0 = s*L / gcd_ab;
        long y_0 = t *L / gcd_ab;
        
        long start = LongMath.pow(10,8); 
        for(long k = -start; k <= -start +15; ++k) {
            long x = x_0 + b*k / gcd_ab;
            long y = y_0 - a*k  / gcd_ab;
            log.debug("Solution x: [{}] y: [{}] to {}x + {}y = {}",x,y,a,b,L);
            Preconditions.checkState(a*x + b*y == L);
        }
    }
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);

        log.info("Done calculating answer case {}", caseNumber);
        
        long minSum = Long.MAX_VALUE;
        /*
        memoize_mod_board_count = new int[MAX_BOARD_LENGTH][MAX_N];
        memoize = new int[MAX_BOARD_LENGTH][MAX_N];
        for(int i = 0; i < MAX_BOARD_LENGTH; ++i) {
            for(int j = 0; j < MAX_N; ++j) {
                memoize_mod_board_count[i][j] = -1;
                memoize[i][j] = -1;
            }
        }
        solve_mod_inner = 0;
        solve_mod_outer = 0;
    */
        for(int maxBoardIndex = input.N - 1; maxBoardIndex >= input.N - 1; --maxBoardIndex) {
            
         //   log.debug("Case number {} max board index {}", caseNumber, maxBoardIndex);
            
            
            
            final long sum = input.L / input.boardLens[maxBoardIndex];
            int rest = Ints.checkedCast(input.L % input.boardLens[maxBoardIndex]);
            
            
            
            //
                        
            int gcd; 
            if (maxBoardIndex > 1) {
                gcd = IntMath.gcd(input.boardLens[maxBoardIndex - 1],
                        input.boardLens[maxBoardIndex - 2]);
            } else {
                gcd = input.boardLens[0]; 
            }
            
            for (int i = maxBoardIndex - 3; i >= 0; --i) {
                gcd = IntMath.gcd(gcd, input.boardLens[maxBoardIndex - i]);
            }

            int gcdMaxRest = IntMath.gcd(gcd,
                    input.boardLens[maxBoardIndex]);

            // find s and t
            int[] st = GCD.gcdExtended(input.boardLens[maxBoardIndex], gcd);
            Preconditions.checkState(gcdMaxRest == st[0]);
            int s = st[1];
            int t = st[2];

            // find k
            int k = rest / gcdMaxRest;
            int k_rem = rest % gcdMaxRest;

            if (k_rem != 0) {
                log.debug("invalid {} gcdMaxRest {}", rest, gcdMaxRest);
                Preconditions.checkState(k_rem != 0);
                //boardNum >= INVALID
                continue;
            } else {
                Preconditions.checkState(k_rem == 0);
            }

            if (k_rem == 0) {
                // log.debug(" gcd[0..maxBoard-1] {} * {} + maxBoard {} * {} = {}",
                // gcd,s,input.boardLens[maxBoardIndex], t,rest);
            }
            
           // log.debug("Case number {} max board index {} sum {} boardNum {}", caseNumber, maxBoardIndex, sum, boardNum);
            
            int maxBigBoardsToAdd = maxBoardIndex == 0 ? 0 : 1+(input.boardLens[maxBoardIndex] - rest) / (input.boardLens[maxBoardIndex] - input.boardLens[maxBoardIndex-1]); 
            
            maxBigBoardsToAdd = 15;
            
            /*
            int boardNum = solve_mod(rest, input.boardLens[maxBoardIndex],
                    maxBoardIndex - 1, input.boardLens, maxBigBoardsToAdd);
            if (boardNum >= INVALID)
                continue;
            
            long newSum = sum + boardNum;
            minSum = Math.min(newSum,minSum);
            */
  
            int maxLen =  300000000; //input.boardLens[maxBoardIndex] * 20;
            solve_iter(maxLen, input.boardLens);
            
            int bigBoardsRemoved  = 0;
            while(true) {
                
                //Preconditions.checkState(bigBoardsRemoved < 1000);
                if (rest > maxLen)
                    break;
                
                int boardsNeeded = memo[rest];//, maxBoardIndex-1, input.boardLens);
                       
                long newSum = sum + boardsNeeded - bigBoardsRemoved;
                minSum = Math.min(newSum,minSum);
            //    log.debug("Rest {} k {} k rem {}.  check {}.  counter {}  diff {}", rest, k, k_rem,boardsNeeded,bigBoardsRemoved,boardsNeeded-bigBoardsRemoved);
                
                rest += input.boardLens[maxBoardIndex];
                k = rest / gcd;
                k_rem = rest % gcd;
            
                
                
                ++bigBoardsRemoved;
            }
            
            
            //sum += solve(rest, maxBoardIndex-1, input.boardLens); 
           
            
            
           // minSum = Math.min(sum,minSum);
        }
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        if ( minSum ==Long.MAX_VALUE)
        return ("Case #" + caseNumber + ": IMPOSSIBLE");
        else
            return ("Case #" + caseNumber + ": " + minSum);
    }
    
    final static int INVALID = IntMath.pow(10, 8); 
    int[][] memoize;
    
    int[][] memoize_mod_board_count;
    long solve_mod_outer;
    long solve_mod_inner;
    
    public int solve_mod(final int boardLengthNeeded, final int mod, int maxBoardIndex, int[] boardLengths, final int maxBoardsToAdd) {
        
        if (boardLengthNeeded == 0) {
            return 0;
        } 
        if (maxBoardIndex < 0) {
            return INVALID;
        }
        
        if ( memoize_mod_board_count[boardLengthNeeded][maxBoardIndex] >= 0) {
            return memoize_mod_board_count[boardLengthNeeded][maxBoardIndex];
        }
        
        ++solve_mod_outer;
        
        int possibleBoardsToAdd = maxBoardsToAdd;
        
        //log.debug("Solve mod board len needed {} board index {}  mod {}", boardLengthNeeded, maxBoardIndex, mod);
        //Set<Integer> seenModValues = new HashSet<Integer>();
        int minCost = INVALID;
        
        minCost = solve_mod(boardLengthNeeded,mod,maxBoardIndex-1, boardLengths, maxBoardsToAdd);
        
        //diff = current sum - L
        
        
        int currentLengthNeeded = boardLengthNeeded;
        int numAdded = 0;
        
        while(true) {
            ++solve_mod_inner;
            
            if (solve_mod_inner % 10000000 == 0) {
             log.debug("Outer {} inner {} Num added {} possible to add {} maxBoardIndex {}", 
                     solve_mod_outer, solve_mod_inner/solve_mod_outer,numAdded, possibleBoardsToAdd, maxBoardIndex);
            }
            currentLengthNeeded -= boardLengths[maxBoardIndex];
            numAdded++;
            
            if (currentLengthNeeded < 0) {
                //We added too many, take a big log off the pile
                currentLengthNeeded += mod;
                numAdded --;
                possibleBoardsToAdd --;
            }
//            Preconditions.checkState(newMod >= 0);
            //Preconditions.checkState(newMod < mod);
            if (currentLengthNeeded == boardLengthNeeded) { //seenModValues.contains(currentLengthNeeded)) {
                //log.debug("Cycle length {}", numAdded);
                break;
            }
            
            if (possibleBoardsToAdd < 0) {
                break;
            }
          //  seenModValues.add(currentLengthNeeded);
            
            //int numTakenAway = (currentLengthNeeded - boardLengthNeeded) / mod;
            
            int cost =  numAdded + solve_mod( currentLengthNeeded , mod, maxBoardIndex -1, boardLengths,possibleBoardsToAdd);
            minCost = Math.min(minCost,cost);
            
        }
        
        memoize_mod_board_count[boardLengthNeeded][maxBoardIndex] = minCost;
            
        return minCost;
    }
    
    public int[] memo;
    // f i n d t h e minimum number o f c o i n s nee de d t o make N
    public int solve_iter(int targetLength, int[] boardLength) {

        memo = new int[targetLength + 1];
        // b a s e c a s e
        memo[0] = 0;
        // f i l l i n memo a r r a y

        for (int i = 1; i <= targetLength; ++i) {
            memo[i] = INVALID;

            for (int j = 0; j < boardLength.length; ++j) {
                if (boardLength[j] <= i) {
                    memo[i] = Math.min(memo[i], 1 + memo[i - boardLength[j]]);
                }
            }
        }
    
        return memo[targetLength];
    }
    
    
    public int solve(int targetLength, int maxBoardIndex, int[] boardLengths) {
        
        if (targetLength < 0) {
            return INVALID;
        }
        if (targetLength == 0) {
            return 0;
        } 
        if (maxBoardIndex < 0) {
            return INVALID;
        }
        if ( memoize[targetLength][maxBoardIndex] >= 0) {
            return memoize[targetLength][maxBoardIndex];
        }
        
        int ret = 0;
        if (maxBoardIndex == 0) {
            if (targetLength % boardLengths[0] == 0) {
                ret = targetLength / boardLengths[0];
            } else {
                ret = Integer.MAX_VALUE;
            }
        } else {
            int s1 = 1 + solve(targetLength - boardLengths[maxBoardIndex], maxBoardIndex, boardLengths);
            int s2 = solve(targetLength, maxBoardIndex - 1, boardLengths);
            ret = Math.min(s1,s2);
        }
        
        memoize[targetLength][maxBoardIndex] = ret;
        return ret;
    }
    
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        
        InputData  input = new InputData(testCase);

        input.L = Long.parseLong(line[0]);
        input.N = Integer.parseInt(line[1]);
        
        input.boardLens = new int[input.N];
        
        line = br.readLine().split(" ");
        
        for(int i = 0; i < input.N; ++i) {
            input.boardLens[i] = Integer.parseInt(line[i]);
           
        }
        
        List<Integer> ii = Arrays.asList(ArrayUtils.toObject(input.boardLens));
        SortedSet<Integer> ss = new TreeSet<Integer>(ii);
        
        Integer[] array = new Integer[ss.size()];
        ss.toArray(array);
        input.boardLens = ArrayUtils.toPrimitive(array);
        
        input.N = input.boardLens.length;
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           // args = new String[] { "sample.txt" };
            args = new String[] { "B-small-practice.in" };
          //  args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}