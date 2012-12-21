package codejam.y2008.round_emea.bus_stops;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.PermutationWithRepetition;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

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
    
    /**
     * Every stop visited in start.  length all arrays = P * 2
     * 
     * in currentEnd, any 0 before a 1 has been visited
     */
    public static int count(int currentBits, int P, int[] memoize) {
        
        if (memoize[currentBits] >= 0) {
            return memoize[currentBits];
        }
        
        int[] current = new BitSetInt(currentBits).toIntArray(2 * P);
        
        int unvisitedBusStop = -1;
        //Find first unvisited stop, which is directly after the last bus
        //Remember, the assumption is that any stop before this bus is visited
        for(int i = current.length - 1; i >= 0; --i) {
            if (current[i] == 1) {
                unvisitedBusStop = i + 1;
                break;
            }                
        }
        
        //Are we done ?
        if (unvisitedBusStop == current.length) {
            memoize[currentBits] = 1;
            return 1;
        }
        
        int sum = 0;
        
        //Find something in currentStart
        
        for(int i = 0; i < unvisitedBusStop; ++i) {
            if (current[i] == 1) {
                int busStart = i;
                
                int length = unvisitedBusStop - busStart;
                
                if (length > P)
                    continue;
                
                BitSetInt curCopy = new BitSetInt(currentBits);
                Preconditions.checkState(curCopy.isSet(busStart) );
                Preconditions.checkState(!curCopy.isSet(unvisitedBusStop));
                curCopy.unsetBit(busStart);
                curCopy.setBit(unvisitedBusStop);
                sum = IntMath.checkedAdd(sum, count(curCopy.getBits(), P, memoize));
            }
        }
        
        
        return sum;
        
        
    }
    
    @Override
    public String handleCase(InputData input) {
        
        
        
        return String.format("Case #%d: IMPOSSIBLE", input.testCase);
        //        return String.format("Case #%d: %s %s", input.testCase, df.format(p.getX()), df.format(p.getY()));
    }

}
