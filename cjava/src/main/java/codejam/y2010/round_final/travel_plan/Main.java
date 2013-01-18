package codejam.y2010.round_final.travel_plan;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
         return new String[] {"sample.in"};
        // return new String[] {"B-small-practice.in"};
        //return new String[] { "C-small-practice.in", "C-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        /*
         *  Each test case description starts with a line containing the number of planets N.
         *   The next line contains N numbers Xi, the coordinates of the planets.
         *    The next line contains the amount of fuel F that you have.
         */
        
        in.N = scanner.nextInt();
        
        in.X = new long[in.N];
        
        for(int p = 0; p < in.N; ++p) {
            in.X[p] = scanner.nextLong();
        }
        
        in.F = scanner.nextLong();

        return in;
    }
    
    /**
     * 
     * @param t  the current assignments t[0] corresponds to intervals[0]
     * @param intervals all the intervals, their lengths
     * @param curIdx cur progress, goes downward
     */
    void calcLeft( int[] t, long[] intervals, int curIdx, List<Long> leftPossible) {
        
        Preconditions.checkState(t[curIdx] > 0);
        
        if (curIdx == 0) {
            long len = 0;
            //Done, now calculate length
            for(int i = 0; i < t.length; ++i) {
                len += t[i] * intervals[i];
            }
            leftPossible.add(len);
            return;
        }
        
        //First save some time by calculating a max and min based on distance to t[0] which
        //must be equal 2.  at t[0]=2, t[1] = {2, 4}, t[2] = {2, 4, 6}
        int nextIdx = curIdx - 1;
        int maxTimes = 2 + nextIdx * 2;
        
        for( int tNext = Math.max(t[curIdx] - 2, 2); tNext < t[curIdx] + 2 && tNext <= maxTimes; tNext += 2 )
        {
            t[nextIdx] = tNext;
            calcLeft( t, intervals, nextIdx, leftPossible);
        }
    }
    
    /**
     * 
     * @param t times, starting at midIndex + 1
     * @param intervals       t[curIdx] = intervals[startIdx + curIdx]
     */
    void calcRight( int[] t, long[] intervals, int curIdx, final int startIdx, List<Long> rightPossible) {
        
        Preconditions.checkState(t[curIdx] > 0);
        
        int realIdx = startIdx + curIdx;
        
        if (realIdx == intervals.length - 1) {
            long len = 0;
            //Done, now calculate length, skipping middle interval which was already counted
            for(int i = 1; i < t.length; ++i) {
                len += t[i] * intervals[i+startIdx];
            }
            rightPossible.add(len);
            return;
        }
        
        //First save some time by calculating a max and min based on distance to t[0] which
        //must be equal 2.  t[ last ] = 2, t[ last - 1] = {2, 4}
        int maxTimes = 2 + (intervals.length - 1 - realIdx) * 2;
        int nextIdx = curIdx + 1;
        
        for( int tNext = Math.max(2, t[curIdx] - 2); tNext < t[curIdx] + 2 && tNext <= maxTimes; tNext += 2 )
        {
            t[nextIdx] = tNext;
            calcRight( t, intervals, nextIdx, startIdx, rightPossible);
        }
    }

    @Override
    public String handleCase(InputData in) {
        
        //It does not matter that we start/stop at X1, it is a cycle path, so it would work from any planet
        Arrays.sort(in.X);
        
        long[] intervals = new long[in.N - 1];
        
        for(int p = 1; p < in.N; ++p) {
            intervals[p-1] = in.X[p] - in.X[p-1];
        }
        
        int midIndex = intervals.length / 2;
        //  1 -- 0 ; 2 -- 01* 3 -- 01*2  4 -- 012*3 5 -- 012*34
        
        int maxMiddleTimes = (in.N - midIndex) * 2;
        
        long globalBest = 0;
        
        for(int midTimes = 2; midTimes <= maxMiddleTimes; midTimes += 2) 
        {
            //1st have includes midIndex
            List<Long> leftPossible = Lists.newArrayList();
            
            int[] leftT = new int[ midIndex + 1 ];
            
            //Populate value for mid times
            leftT[ midIndex ] = midTimes;
            
            calcLeft( leftT, intervals, midIndex, leftPossible);


            List<Long> rightPossible = Lists.newArrayList();

            //It is in the right part too, but not counted
            int[] rightT = new int [ intervals.length - midIndex ];
            rightT[0] = midTimes;
            
            calcRight( rightT, intervals, 0, midIndex, rightPossible);
            
            for(int lIdx = 0; lIdx < leftPossible.size(); ++lIdx) {
                for(int rIdx = 0; rIdx < rightPossible.size(); ++rIdx) {
                    long distance = leftPossible.get(lIdx) + rightPossible.get(rIdx);
                    
                    if (distance > in.F)
                        continue;
                    
                    globalBest = Math.max(distance, globalBest);
                }
            }
        }
        
        
        return String.format("Case #%d: %d", in.testCase, globalBest);

    }
}