package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    final static int MAX_BOARD_LENGTH = 100;
    final static int MAX_N = 100;
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);

        log.info("Done calculating answer case {}", caseNumber);
        
        long minSum = Long.MAX_VALUE;
        
        
        
        for(int maxBoardIndex = input.N - 1; maxBoardIndex >= 0; --maxBoardIndex) {
            
            log.debug("Case number {} max board index {}", caseNumber, maxBoardIndex);
            memoize_mod_board_count = new int[MAX_BOARD_LENGTH][MAX_N];
            memoize_mod_board_len = new int[MAX_BOARD_LENGTH][MAX_N];
            for(int i = 0; i < MAX_BOARD_LENGTH; ++i) {
                for(int j = 0; j < MAX_N; ++j) {
                    memoize_mod_board_count[i][j] = -1;
                    memoize_mod_board_len[i][j] = -1;
                }
            }
            
            long sum = 0;
            int rest = Ints.checkedCast(input.L % input.boardLens[maxBoardIndex]);
            sum = input.L / input.boardLens[maxBoardIndex];
            
            
            //int boardValues = solve_mod(0, input.boardLens[maxBoardIndex], maxBoardIndex-1, input.boardLens, false, rest);
            
            
            
            int boardNum = solve_mod(0, input.boardLens[maxBoardIndex], maxBoardIndex-1, input.boardLens, true,rest);
            if (boardNum >= INVALID)
                continue;
            sum += boardNum;
            //sum += solve(rest, maxBoardIndex-1, input.boardLens); 
            
            minSum = Math.min(sum,minSum);
        }
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        if ( minSum ==Long.MAX_VALUE)
        return ("Case #" + caseNumber + ": IMPOSSIBLE");
        else
            return ("Case #" + caseNumber + ": " + minSum);
    }
    
    final static int INVALID = 10000000; 
    //int[][] memoize;
    int[][] memoize_mod_board_len;
    int[][] memoize_mod_board_count;
    
    public int solve_mod(int currentRemainder, final int mod, int maxBoardIndex, int[] boardLengths, boolean boardCount, final int targetRemainder) {
        
        
        if (currentRemainder < 0) {
            return INVALID;
        }
        if (currentRemainder == targetRemainder) {
            return 0;
        } 
        if (maxBoardIndex < 0) {
            return INVALID;
        }
        
        if (boardCount && memoize_mod_board_count[currentRemainder][maxBoardIndex] >= 0) {
            return memoize_mod_board_count[currentRemainder][maxBoardIndex];
        }
        if (!boardCount && memoize_mod_board_len[currentRemainder][maxBoardIndex] >= 0) {
            return memoize_mod_board_len[currentRemainder][maxBoardIndex];
        }
        
        Set<Integer> seenModValues = new HashSet<Integer>();
        int minCost = INVALID;
        
        minCost = solve_mod(currentRemainder,mod,maxBoardIndex-1, boardLengths, boardCount,targetRemainder);
        
        int addedSum = 0;
        int numAdded = 0;
        while(true) {
            addedSum += boardLengths[maxBoardIndex];
            numAdded++;
            int newMod = addedSum;
            if (newMod >= mod) {
                //Here we take a board of length mod off the pile
                newMod -= mod;
                addedSum -= mod;
                numAdded--;
            }
            Preconditions.checkState(newMod >= 0);
            Preconditions.checkState(newMod < mod);
            if (seenModValues.contains(newMod)) {
                break;
            }
            seenModValues.add(newMod);
            int cost = (boardCount ? numAdded : addedSum) + solve_mod( (currentRemainder + addedSum) % mod, mod, maxBoardIndex -1, boardLengths, boardCount, targetRemainder);
            minCost = Math.min(minCost,cost);
        }
        
        if (boardCount ) {
             memoize_mod_board_count[currentRemainder][maxBoardIndex] = minCost;
        }
        if (!boardCount) {
             memoize_mod_board_len[currentRemainder][maxBoardIndex] = minCost;
        }
            
        return minCost;
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
        
        if (maxBoardIndex == 0) {
            if (targetLength % boardLengths[0] == 0) {
                return targetLength / boardLengths[0];
            } else {
                return Integer.MAX_VALUE;
            }
        } else {
            int s1 = 1 + solve(targetLength - boardLengths[maxBoardIndex], maxBoardIndex, boardLengths);
            int s2 = solve(targetLength, maxBoardIndex - 1, boardLengths);
            return Math.min(s1,s2);
        }
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
        
        Arrays.sort(input.boardLens);
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           // args = new String[] { "sample.txt" };
            args = new String[] { "B-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}