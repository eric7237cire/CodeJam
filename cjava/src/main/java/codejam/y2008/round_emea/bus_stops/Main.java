package codejam.y2008.round_emea.bus_stops;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.PermutationWithRepetition;

import com.google.common.base.Preconditions;
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
    
    /**
     * startState is a bitArray of length 2 * P
     * 
     * returns count of all valid end states (with a bus at most sig bit)
     */
    public static int[] count(int startState, int finalLength, int K, int P, int mod) {
        
        BitSetInt startStateBs = new BitSetInt(startState);
                
        log.debug("Start state [{}]", bsToStr(startStateBs, finalLength, P));
        
        int firstUnvisitedBusStop = startStateBs.getMostSigBitIndex() + 1;
        int lastUnvisitedBusStop = finalLength - 1;        
        int stopsToFill = finalLength - firstUnvisitedBusStop;
        
        int [] previousCounts = new int[1 << finalLength];
        previousCounts[startState] = 1;
        
        Set<Integer> previousStates = Sets.newHashSet();
        previousStates.add(startState);
        
        for(int unvisitedStop = firstUnvisitedBusStop; unvisitedStop <= lastUnvisitedBusStop; ++unvisitedStop) {
            
            log.debug("Processing unvisited stop {}", unvisitedStop);
            
            int lenPrev = unvisitedStop;
            
            final int maxPrevState = ( 1 << Math.min(lenPrev,P) ) - 1;
            
            Set<Integer> states = Sets.newHashSet();
            
            //Loop through all possible previous states
            for(int prev : previousStates) {
                
                
                /*int prev = prevLoop;
                
                if (lenPrev < P) {
                   // prev >>= (P - lenPrev);
                } else if (lenPrev > P) {
                    prev <<= (lenPrev - P);
                }*/
                
                Preconditions.checkState( Integer.bitCount(prev) == K); 
                    
                
                //For each set bit, starting at lowest, move that bus to unvisited stop
                //If the distance == P, we stop as going further would result in a 'stranded' bus
                
                BitSetInt busesLeft = new BitSetInt(prev);
                
                Preconditions.checkState(busesLeft.getMostSigBitIndex() == unvisitedStop - 1);
                
                log.debug("Previous state [{}]", bsToStr(busesLeft, finalLength, P));
                
                while(busesLeft.getBits() > 0) {
                    int bus = busesLeft.getLeastSignificantBitIndex();
                    int distance = unvisitedStop - bus;
                    Preconditions.checkState(distance > 0 && distance <= P);
                    
                    BitSetInt newState = new BitSetInt(prev);
                    newState.set(unvisitedStop);
                    newState.unset(bus);
                
                    log.debug("new state [{}]", bsToStr(newState, finalLength, P));
                    
                    states.add(newState.getBits());
                    
                    previousCounts[newState.getBits()] += previousCounts[prev];
                    previousCounts[newState.getBits()] %= mod;
                    
                    log.debug("count of {} + {} = {}", bsToStr(newState, finalLength, P), previousCounts[prev],previousCounts[newState.getBits()]);
                    
                    if (distance == P) {
                        break;
                    }
                    
                    busesLeft.unsetLeastSignificantSetBit();
                }
                
                previousStates = states;
                
            }
        }
       
        
        return previousCounts;
        
        
    }
    
    @Override
    public String handleCase(InputData input) {
        
        
        
        return String.format("Case #%d: IMPOSSIBLE", input.testCase);
        //        return String.format("Case #%d: %s %s", input.testCase, df.format(p.getX()), df.format(p.getY()));
    }

}
