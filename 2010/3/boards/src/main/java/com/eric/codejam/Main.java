package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.google.common.collect.ComparisonChain;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    final static int MAX_BOARD_LENGTH = 100000;
    final static int MAX_N = 100;
    
    //Solves ax + by = L.  Not used, mauvaise piste!
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
        
        
        
        //where x is first positive 
        
        // 0 <= x_0 + b*k / gcd_ab
        //-x_0 <= b*k / gcd_ab
        //-x_0 * gcd_ab / b <= k

        //0 <= y_0 - a*k / gcd_ab
        //-y_0 <= -a*k / gcd_ab
        //y_0 >= a*k / gcd_ab
        //y_0 * gcd_ab / a >= k
        
        //long start = y_0 * gcd_ab / a - 1;
        BigInteger[] startBi = BigInteger.valueOf(-x_0 * gcd_ab).divideAndRemainder(BigInteger.valueOf(b));
        long start = startBi[0].longValue();
        if (startBi[1].longValue() > 0) {
            start++;
        }        
        
        log.debug("x_0 {} y_0 {} k {}", x_0, y_0, start);
        
        for(long k = start; k <= start +5; ++k) {
            long x = x_0 + b*k / gcd_ab;
            long y = y_0 - a*k  / gcd_ab;
            
            
            log.debug("Solution x: [{}] y: [{}] to {}x + {}y = {}.   x+y  = {}",x,y,a,b,L, x+y);
            Preconditions.checkState(a*x + b*y == L);
            
            //10000653330
        }
    }
    
    private static class Node {
        Integer residue;
        int cost;
        public Node(Integer residue, int distance) {
            super();
            this.residue = residue;
            this.cost = distance;
        }
        
    }
    /*
     * Finds shortest distance to a target leftover.
     * 
     * 
     */
    public Integer doBreadthFirstSearch(int boardLengths[], int largestBoardLength, int targetResidue) {
        int[] distance = new int[largestBoardLength];
        int[] previous = new int[largestBoardLength];
        
        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(previous, -1);
        
        SortedSet<Node> toProcess = new TreeSet<>(new Comparator<Node>() {
            public int compare(Node a, Node b) {
                return ComparisonChain.start().compare(a.cost, b.cost).compare(a.residue,b.residue).result();
            }
        });
        Set<Integer> visited = new HashSet<Integer>();
        
        toProcess.add(new Node(0,0));
        distance[0] = 0;
        
        while(!toProcess.isEmpty()) {
            Node currentNode = toProcess.first();
            int currentResidue = currentNode.residue;
            toProcess.remove(currentNode);
            
            //log.debug("Current residue {}  cost {}", currentResidue, currentNode.cost);
            if (visited.contains(currentResidue)) {
                continue;
            }
            
            if (currentResidue == targetResidue) {
                return currentNode.cost;
            }
            
            for(int i = 0; i < boardLengths.length - 1; ++i) {
                Integer newResidue = currentResidue + boardLengths[i];
                //Costs 1 to add a new board
                int cost = 1;
                if (newResidue >= largestBoardLength) {
                    //take away a larger board
                    --cost;
                    newResidue -= largestBoardLength; 
                }
                
                Preconditions.checkState(distance[currentResidue] < Integer.MAX_VALUE);
                int newCost = distance[currentResidue] + cost;
                
                if (newCost < distance[newResidue]) {
                    distance[newResidue] = newCost;
                    previous[newResidue] = currentResidue;
                    toProcess.add(new Node(newResidue, newCost));
                }               
                                
            }
            
            visited.add(currentResidue);
            Preconditions.checkState(distance[currentResidue] >= currentNode.cost);
            distance[currentResidue] = currentNode.cost;
        }
        
        return null;
        
    }
    
    
    
    
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);

        
        
        long minSum = Long.MAX_VALUE;
       
        int maxLen =  100000000; //input.boardLens[maxBoardIndex] * 20;
        //solve_iter(maxLen, input.boardLens);
        
        //We are forced to use the max index, see solution for explanation. 
        
        int maxBoardIndex = input.N - 1; 
            
            log.debug("Case number {} max board index {}", caseNumber, maxBoardIndex);
            
            
            
            final long sum = input.L / input.boardLens[maxBoardIndex];
            int rest = Ints.checkedCast(input.L % input.boardLens[maxBoardIndex]);
            
            Integer cost = doBreadthFirstSearch(input.boardLens,input.boardLens[maxBoardIndex],rest);
        
            if (cost==null) {
                return ("Case #" + caseNumber + ": IMPOSSIBLE");
            }
            long total = sum+cost;
            
            
            return ("Case #" + caseNumber + ": " + total);
            
    }
    
    final static int INVALID = IntMath.pow(10, 8); 
        
    int[][] memoize_mod_board_count;
    
    /*
     * The right idea but the wrong implementation.  Was trying to find lowest mod count.
     * 
     * The shortest path search was better obviously.
     */
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
        
        
        int possibleBoardsToAdd = maxBoardsToAdd;
        
        //log.debug("Solve mod board len needed {} board index {}  mod {}", boardLengthNeeded, maxBoardIndex, mod);
        //Set<Integer> seenModValues = new HashSet<Integer>();
        int minCost = INVALID;
        
        minCost = solve_mod(boardLengthNeeded,mod,maxBoardIndex-1, boardLengths, maxBoardsToAdd);
        
        //diff = current sum - L
        
        
        int currentLengthNeeded = boardLengthNeeded;
        int numAdded = 0;
        
        while(true) {
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
    //Bottom up DP for once.  Did pretty well for most of large too
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
          //  args = new String[] { "sample.txt" };
            args = new String[] { "B-small-practice.in" };
          //  args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}