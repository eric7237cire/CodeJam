package codejam.y2008.round_emea.bus_stops;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.PermutationWithRepetition;

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
        return new String[] {"sample.in"};
        //return new String[] {"B-small-practice.in"};
        //return new String[] {"B-large-practice.in"};
     //   return new String[] {"A-small-practice.in", "A-large-practice.in"};
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData i = new InputData(testCase);
        
        i.N = scanner.nextInt();
        i.K = scanner.nextInt();
        i.P = scanner.nextInt();
        return i;
    }
   
    public static int sumOfProductAllPermutationsFast(Integer[] numbers, int permLength, int mod) {
        
        long firstChunk = 0;
        
        for(int n = 0; n < numbers.length; ++n) {
            firstChunk += numbers[n];
            firstChunk %= mod;
        }
        
        List<Integer> chunks = new ArrayList<Integer>();
        chunks.add(Ints.checkedCast(firstChunk));
        
        int biggestChunkIndex = IntMath.log2(permLength, RoundingMode.DOWN) ;
                
        int chunkSize = 1;
        for(int i = 1; i <= biggestChunkIndex; ++i) {
            chunkSize *= 2;
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
    
    //starting means before an interval of length P
    //starting | ending = 000111 |  00111
    //                    h    l    h   l
    
    //
    /**
     * Every stop visited in start.  length all arrays = P * 2
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
    
    private static String bsToStr (BitSetInt bs, int length, int P) {
        String str = Integer.toBinaryString(bs.getBits());
        str = StringUtils.reverse(str);
        str = StringUtils.rightPad(str, length, '0');
        
        return str.substring(0, P) + " | " + str.substring(P);
    }
    
    
    public static int countFast(int finalLength, int K, int P, int mod) {
        
        BitSetInt startState = new BitSetInt();
        for(int i = 0; i < K; ++i) {
            startState.set(i);
        }
        
        if (finalLength <= K + P) {
            BitSetInt finalState = new BitSetInt();
            for(int i = finalLength - K; i < finalLength; ++i) {
                finalState.set(i);
            }
            int[] r = count(startState.getBits(), finalLength, K, P, mod);
            return r[finalState.getBits()];
        }
        
        //First step determine transitions from startState to an interval of P after start state
        List<Integer> stateList = Lists.newArrayList();
        
        int[] stateCounts = count(startState.getBits(), K+P, K, P, mod, stateList);
        
        FieldMatrix<BigFraction> initialStateCountsMatrix = new Array2DRowFieldMatrix<BigFraction>(new BigFraction(1,1).getField(), stateList.size(), 1);
        
        //Multiply by initial state counts
        for(int i = 0; i < stateList.size(); ++i) {
            initialStateCountsMatrix.setEntry(i, 0, new BigFraction(stateCounts[stateList.get(i)]));
        }
        
       
        
        //stateList needs to be bitshifted K elements to the left to make it of size P
        for(int stateNum = 0; stateNum < stateList.size(); ++stateNum) {
            
            log.debug("start.  transition from starting state {} to state #{} : {}.  Count {}", 
                     bsToStr( startState, K+P, P),
                    stateNum, bsToStr(new BitSetInt(stateList.get(stateNum)), K+P, P), 
                    stateCounts[stateList.get(stateNum)]);
            
            stateList.set(stateNum, stateList.get(stateNum) >> K);
        }
        
        stateCounts = null;
        
        //Then we need a transition matrix, going from every possible state to every possible state
        FieldMatrix<BigFraction> trans = new Array2DRowFieldMatrix<BigFraction>(new BigFraction(1,1).getField(), stateList.size(), stateList.size());
        log.debug("Calculating transition matrix");
        
        for(int stateNum = 0; stateNum < stateList.size(); ++stateNum) {
            int transStartState = stateList.get(stateNum);
            log.debug("starting at state #{} : {}", stateNum, bsToStr( new BitSetInt(transStartState), 2*P, P));
            int[] transCounts = count(transStartState, 2 * P, K, P, mod);
            
            for(int endingStateNum = 0; endingStateNum < stateList.size(); ++endingStateNum) {
                int endingState = stateList.get(endingStateNum) << P;
                log.debug("transition from state# {} : {} to state #{} : {}.  Count {}", 
                        stateNum, bsToStr( new BitSetInt(transStartState), 2*P, P),
                        endingStateNum, bsToStr(new BitSetInt(endingState), 2*P, P), 
                        transCounts[endingState]);
                trans.setEntry(endingStateNum, stateNum, new BigFraction(transCounts[endingState]));
            }
        }
        
        //Done start
        
        int lengthLeft = finalLength - K;
        
        int pChunks = (lengthLeft - 1) / P - 1;
        
        FieldMatrix<BigFraction> stateCountsMatrix = //new Array2DRowFieldMatrix<BigFraction>(new BigFraction(1,1).getField(), stateList.size(), 1);
                new Array2DRowFieldMatrix<BigFraction>(initialStateCountsMatrix.getData());
        
        for(int i = 0; i < stateList.size(); ++i) {
           // stateCountsMatrix.setEntry(i, 0, new BigFraction(1, 1));
        }
        
        for(int i = 0; i < pChunks; ++i) {
            stateCountsMatrix = trans.multiply(stateCountsMatrix);
            lengthLeft -= P;
        }
        
        //Multiply by initial state counts
        for(int i = 0; i < stateList.size(); ++i) {
          //  stateCountsMatrix.multiplyEntry(i, 0, initialStateCountsMatrix.getEntry(i,0));
        }
        
        //Done middle
        
        
                
        //Figure out from the trans matrix which state is the good one
        Preconditions.checkState(lengthLeft <= 2 * P);
        
        BitSetInt finalState = BitSetInt.createWithBitsSet(lengthLeft - K,  lengthLeft-1);
        log.debug("Final state {}", bsToStr(finalState, lengthLeft, P));
        
        //P to last stretch
        long finalSum = 0;
        
        //From all possible intermediate states to final state
        
        for(int stateNum = 0; stateNum < stateList.size(); ++stateNum) {
            int transStartState = stateList.get(stateNum);
            int[] transCounts = count(transStartState, lengthLeft, K, P, mod);
            
            log.debug("final transition from state# {} : {} to final state : {}.  Initial Count {} * trans count {}", 
                    stateNum, bsToStr( new BitSetInt(transStartState), lengthLeft, P),
                     bsToStr(finalState, lengthLeft, P),
                     stateCountsMatrix.getEntry(stateNum,0).getNumeratorAsLong(),
                     transCounts[finalState.getBits()]);
            
            finalSum += transCounts[finalState.getBits()] * (stateCountsMatrix.getEntry(stateNum,0).getNumeratorAsLong() % mod);
        }
        
        return Ints.checkedCast(finalSum % mod);
        
    }
    
    public static int[] count(int startState, int finalLength, int K, int P, int mod) {
        List<Integer> finalStates = Lists.newArrayList();
        
        return count(startState, finalLength, K, P, mod, finalStates);
    }
    /**
     * startState is a bitArray of length 2 * P
     * 
     * returns count of all valid end states (with a bus at most sig bit)
     */
    public static int[] count(int startState, int finalLength, int K, int P, int mod, List<Integer> finalStates) {
        
        BitSetInt startStateBs = new BitSetInt(startState);
                
       // log.debug("Start state [{}]", bsToStr(startStateBs, finalLength, P));
        
        int firstUnvisitedBusStop = startStateBs.getMostSigBitIndex() + 1;
        int lastUnvisitedBusStop = finalLength - 1;        
        
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
        
        
        
        return String.format("Case #%d: IMPOSSIBLE", input.testCase);
        //        return String.format("Case #%d: %s %s", input.testCase, df.format(p.getX()), df.format(p.getY()));
    }

}
