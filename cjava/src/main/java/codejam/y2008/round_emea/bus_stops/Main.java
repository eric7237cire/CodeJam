package codejam.y2008.round_emea.bus_stops;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.datastructures.MatrixModded;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    @Override
    public String[] getDefaultInputFiles() {
     //   return new String[] {"sample.in"};
        return new String[] {"D-small-practice.in", "D-large-practice.in"};
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData i = new InputData(testCase);
        
        i.N = scanner.nextInt();
        i.K = scanner.nextInt();
        i.P = scanner.nextInt();
        return i;
    }
   
    
    
    //starting means before an interval of length P
    //starting | ending = 000111 |  00111
    //                    h    l    h   l
    
    //
    /**
     * in the arrays, every stop prior to right most 1 (bus) is visited.  
     * 
     * in currentEnd, any 0 before a 1 has been visited
     */
    public static int countBruteForce(int[] start, int[] ending, int[] current, int P) {
        
        int unvisitedBusStop = -1;
        //Find a gap in currentEnd
        for(int i = current.length - 1; i >= 0; --i) {
            if (current[i] == 1) {
                unvisitedBusStop = i + 1;
                break;
            }                
        }
        
        //Are we done ?
        if (unvisitedBusStop == current.length) {
            //All stops are visited, now check if it counts or not
            if (Arrays.equals(ending, current)) {
                return 1;
            } else {
                return 0;
            }
        }
        
        int sum = 0;
        
        //Find something in currentStart
        
        for(int i = 0; i < unvisitedBusStop; ++i) {
            if (current[i] == 1) {
                int busStart = i;
                
                int length = unvisitedBusStop - busStart;
                
                if (length > P)
                    continue;
                
                int[] curCopy = Arrays.copyOf(current,current.length);
                Preconditions.checkState(curCopy[busStart] == 1);
                Preconditions.checkState(curCopy[unvisitedBusStop] == 0);
                curCopy[busStart] = 0;
                curCopy[unvisitedBusStop] = 1;
                sum = IntMath.checkedAdd(sum, countBruteForce(start,ending,curCopy, P));
            }
        }
        
        return sum;
        
        
    }
    
    
    public static int countFast(int finalLength, int K, int P, int mod) {
        
        BitSetInt startState = BitSetInt.createWithBitsSet(0, K-1);
        
        /**
         * If the inputs are small enough, just handle it directly
         */
        if (finalLength <= K + P) {
            BitSetInt finalState = BitSetInt.createWithBitsSet(finalLength-K, finalLength-1);
            int[] r = count(startState.getBits(), finalLength, K, P, mod);
            return r[finalState.getBits()];
        }
        
        /**
         * First step determine transitions from startState to an interval of P after start state
         * 
         * ie, K = 3, P = 5
         * 
         * 111 | 00000  ==>  000 | xxxxx
         * 
         * 
         */
        List<Integer> stateList = Lists.newArrayList();
        
        int[] stateCounts = count(startState.getBits(), K+P, K, P, mod, stateList);
        
        int[][] stateCountsMatrix = new int[stateList.size()][1];
        
        //Calculate state counts, condensing it into a Sx1 matrix
        for (int i = 0; i < stateList.size(); ++i) {
            stateCountsMatrix[i][0] = stateCounts[stateList.get(i)];
        }
       
        
        //stateList needs to be bitshifted K elements to the left to make it of size P
        for(int stateNum = 0; stateNum < stateList.size(); ++stateNum) {
            
//            log.debug("start.  transition from starting state {} to state #{} : {}.  Count {}", 
//                     bsToStr( startState, K+P, P),
//                    stateNum, bsToStr(new BitSetInt(stateList.get(stateNum)), K+P, P), 
//                    stateCounts[stateList.get(stateNum)]);
            
            stateList.set(stateNum, stateList.get(stateNum) >> K);
        }
        
        stateCounts = null;
        
        //Then we need a transition matrix, going from every possible state to every possible state
        int[][] trans = new int[stateList.size()][stateList.size()];
        log.debug("Calculating transition matrix");
        
        log.debug("Calculating transition count");
        for(int stateNum = 0; stateNum < stateList.size(); ++stateNum) {
            int transStartState = stateList.get(stateNum);
            //log.debug("starting at state #{} : {}", stateNum, bsToStr( new BitSetInt(transStartState), 2*P, P));
            
            int[] transCounts = count(transStartState, 2 * P, K, P, mod);
            
            for(int endingStateNum = 0; endingStateNum < stateList.size(); ++endingStateNum) {
                int endingState = stateList.get(endingStateNum) << P;
//                log.debug("transition from state# {} : {} to state #{} : {}.  Count {}", 
//                        stateNum, bsToStr( new BitSetInt(transStartState), 2*P, P),
//                        endingStateNum, bsToStr(new BitSetInt(endingState), 2*P, P), 
//                        transCounts[endingState]);
                trans[endingStateNum][stateNum] = transCounts[endingState];
            }
        }
        log.debug("Done calculating transitions");
        
        //Done start
        
        log.debug("Calculating transitions matrices.  Size matrix {} x {}", trans.length, trans[0].length);
        int lengthLeft = finalLength - K;
        
        int pChunks = (lengthLeft - 1) / P - 1;
        
        List<int[][]> transformMatrices = new ArrayList<>();
        transformMatrices.add(trans);
        
        int biggestChunkIndex = pChunks > 0 ? IntMath.log2(pChunks, RoundingMode.DOWN) : 0;
                
        for(int i = 1; i <= biggestChunkIndex; ++i) {            
            int[][]  matrix = MatrixModded.multiply(transformMatrices.get(i-1), transformMatrices.get(i-1), mod);
                        
            transformMatrices.add(matrix); 
        }
        
        log.debug("Done Calculating transitions matrices");
        
        log.debug("Chunking");
        while(pChunks > 0) {
            int matrixIndex = IntMath.log2(pChunks, RoundingMode.DOWN);
            int[][] matrix = transformMatrices.get(matrixIndex);
            
            stateCountsMatrix = MatrixModded.multiply(matrix, stateCountsMatrix, mod);
            
            int pChunksTaken = 1 << matrixIndex;
            lengthLeft -= P * pChunksTaken;
            pChunks -= pChunksTaken;
        }
        
        log.debug("Done Chunking");
        
       
        
                
        //Figure out from the trans matrix which state is the good one
        Preconditions.checkState(lengthLeft <= 2 * P);
        
        BitSetInt finalState = BitSetInt.createWithBitsSet(lengthLeft - K,  lengthLeft-1);
        //log.debug("Final state {}", bsToStr(finalState, lengthLeft, P));
        
        //P to last stretch
        long finalSum = 0;
        
        //From all possible intermediate states to final state
        
        for(int stateNum = 0; stateNum < stateList.size(); ++stateNum) {
            int transStartState = stateList.get(stateNum);
            int[] transCounts = count(transStartState, lengthLeft, K, P, mod);
            
//            log.debug("final transition from state# {} : {} to final state : {}.  Initial Count {} * trans count {}", 
//                    stateNum, bsToStr( new BitSetInt(transStartState), lengthLeft, P),
//                     bsToStr(finalState, lengthLeft, P),
//                     stateCountsMatrix[stateNum][0],
//                     transCounts[finalState.getBits()]);
            
            finalSum += transCounts[finalState.getBits()] * stateCountsMatrix[stateNum][0] % mod;
            finalSum %= mod;
        }
        
        //log.debug("Final transition finished");
        
        return Ints.checkedCast(finalSum % mod);
        
    }
    
    public static int[] count(int startState, int finalLength, int K, int P, int mod) {
        List<Integer> finalStates = Lists.newArrayList();
        
        return count(startState, finalLength, K, P, mod, finalStates);
    }
    
    /**
     * 
     * @param startState in binary, initial configuration.  buses move from LSB to MSB
     * @param finalLength how many bus stops there are
     * @param K number of buses
     * @param P how far the buses can go
     * @param mod
     * @param finalStates  will be all the states such that finalLength has been visited.  1 bus each stop
     * @return the counts of all the configurations
     */
    public static int[] count(int startState, int finalLength, int K, int P, int mod, List<Integer> finalStates) {
        
        //Contstraints from problem
        Preconditions.checkState(K+P <= 20);
        Preconditions.checkArgument(finalLength <= 2*P);
        
        BitSetInt startStateBs = new BitSetInt(startState);
                
       // log.debug("Start state [{}]", bsToStr(startStateBs, finalLength, P));
        
        //All 0's before right most (MSB) 1 are considered visited
        int firstUnvisitedBusStop = startStateBs.getMostSigBitIndex() + 1;
        int lastUnvisitedBusStop = finalLength - 1;        
        
        /**
         * The idea is we go stop by stop, filling it in from previous rounds.
         * 
         * These vars hold the valid previous round states and their counts
         */
        int [] previousCounts = new int[1 << finalLength];
        previousCounts[startState] = 1;
        
        Set<Integer> previousStates = Sets.newHashSet();
        previousStates.add(startState);
        
        for(int unvisitedStop = firstUnvisitedBusStop; unvisitedStop <= lastUnvisitedBusStop; ++unvisitedStop) {
            
           // log.debug("Processing unvisited stop {}", unvisitedStop);
                        
            Set<Integer> states = Sets.newHashSet();
            
            //Loop through all possible previous states
            for(int prev : previousStates) {
                                
                Preconditions.checkState( Integer.bitCount(prev) == K); 
                    
                
                //For each set bit, starting at lowest, move that bus to unvisited stop
                //If the distance == P, we stop as going further would result in a 'stranded' bus
                //Ex
                
                // 111 | 0  if P = 3 we can only move the left most bus, other wise
                // we get
                // 101 | 1 where the leftmost bus cant get anywhere anymore
                
                BitSetInt busesLeft = new BitSetInt(prev);
                
                Preconditions.checkState(busesLeft.getMostSigBitIndex() == unvisitedStop - 1);
                
             //   log.debug("Previous state [{}]", bsToStr(busesLeft, finalLength, P));
                
                while(busesLeft.getBits() > 0) {
                    int bus = busesLeft.getLeastSignificantBitIndex();
                    int distance = unvisitedStop - bus;
                    Preconditions.checkState(distance > 0 && distance <= P);
                    
                    BitSetInt newState = new BitSetInt(prev);
                    newState.set(unvisitedStop);
                    newState.unset(bus);
                
              //      log.debug("new state [{}]", bsToStr(newState, finalLength, P));
                    
                    states.add(newState.getBits());
                    
                    previousCounts[newState.getBits()] += previousCounts[prev];
                    previousCounts[newState.getBits()] %= mod;
                    
             //       log.debug("count of {} + {} = {}", bsToStr(newState, finalLength, P), previousCounts[prev],previousCounts[newState.getBits()]);
                    
                    if (distance == P) {
                        break;
                    }
                    
                    busesLeft.unsetLeastSignificantSetBit();
                }
                
                previousStates = states;
                
            }
        }
       
        finalStates.addAll(previousStates);
        return previousCounts;
        
        
    }
    
    @Override
    public String handleCase(InputData input) {
        
        int ans = countFast(input.N, input.K, input.P, 30031);
        
        return String.format("Case #%d: %d", input.testCase, ans);
    }

}
