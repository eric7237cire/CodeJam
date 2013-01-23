package codejam.y2012.round_3.perfect_game;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.IndexMinPQ;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles
{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles()
    {
        return new String[] { "sample.in" };
        //    return new String[] { "B-small-practice.in" };
        //  return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    public static class Level
    {
        //Length in time
        int L;
        
        //Probability of death
        int P;
        
        //Indice
        int index;

        @Override
        public String toString()
        {
            return "Level [L=" + L + ", P=" + P + ", index=" + index + "]";
        }
        
        double getExpectedTime() {
            int pPass = 100 - P;
            
            double expectedRepeats = 1d / (pPass / 100d);
            
            return expectedRepeats * L;
        }
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);

        in.N = scanner.nextInt();

        in.levels = new Level[in.N];
        
        for (int i = 0; i < in.N; ++i) {
            Level level = new Level();
            level.index = i;
            in.levels[i] = level;
        }
        for (int i = 0; i < in.N; ++i)
        {
            in.levels[i].L = scanner.nextInt();
        }
        for (int i = 0; i < in.N; ++i)
        {
            in.levels[i].P = scanner.nextInt();
        }

        return in;
    }

    static void simulate(Level[] levels)
    {
        //death
        
        int nLevels = levels.length;
        long sum = 0;
        int iterations = 1;

        Random r = new Random();

        for (int i = 0; i < iterations; ++i)
        {
            int time = 0;

            loop: while (true)
            {

                for (int level = 0; level < nLevels; ++level)
                {
                    time += levels[level].L;

                    int perc = r.nextInt(100) + 1;

                    if (perc <= levels[level].P)
                    {
                        //dead
                        continue loop;
                    }
                }

                break;

            }

            sum += time;
        }

        log.debug("Average length {}", (double) sum / iterations);
    }

    double getExpectedLength(Level[] levels) {
        
        List<Double> expectedLength = Lists.newArrayList();
        
        expectedLength.add(  (100d / ( 100 - levels[0].P)) * levels[0].L );
        
        for(int level = 1 ; level < levels.length; ++level) {
            double expectedNumber = 100d / (100 - levels[level].P);
            
            expectedLength.add(expectedNumber * 
                    (expectedLength.get(level-1)+levels[level].L));
        }
        
        
        return expectedLength.get(expectedLength.size() - 1);
    }

    void swapLevels(Level[] levels, int lev1, int lev2) {
        Level temp = levels[lev1];
        levels[lev1] = levels[lev2];
        levels[lev2] = temp;
    }
    
    static class LevelCompare implements Comparator<Level> {

        @Override
        public int compare(Level o1, Level o2)
        {
            
            //double et = o1.getExpectedTime();
            //double et2 = o2.getExpectedTime();
            
            int lhs = o1.L * o2.P;
            int rhs = o2.L * o1.P;
            
            if (lhs != rhs)
                return lhs > rhs ? 1 : -1;
            
            return Integer.compare(o1.index, o2.index);
            
                
        }
        
    }
    
    public void handleSmall(InputData in)
    {
        double curExpectedLen = getExpectedLength(in.levels);
        simulate(in.levels);
        
        log.debug("Starting expected len {}", curExpectedLen);
        
        for(int i = 0; i < in.N; ++i)
        {
            for(int j = i + 1; j < in.N; ++j) {
                //Attempt to swap i and j
                swapLevels(in.levels, i, j);
                double swpExpectedLen = getExpectedLength(in.levels);
                
                log.debug("i: {};{} j: {};{} cur: {} after swap: {}",i,in.levels[i].index,
                        j,in.levels[j].index,curExpectedLen, swpExpectedLen);
                
                //no improvement, swap back
                if (swpExpectedLen > curExpectedLen) {
                    log.debug("No improvement");
                    swapLevels(in.levels, i, j);
                } else if (swpExpectedLen == curExpectedLen && in.levels[i].index < in.levels[j].index){
                    log.debug("Improvement only in indexes");
                    curExpectedLen = swpExpectedLen;
                } else if (swpExpectedLen == curExpectedLen && in.levels[i].index > in.levels[j].index){
                    log.debug("No improvement, index already lower");
                    swapLevels(in.levels, i, j);
                } else {
                    log.debug("Improvement");
                    curExpectedLen = swpExpectedLen;
                }
            }
        }
    }
    
    public String handleCase(InputData in)
    {
        log.debug("test");
        
        //List<Level> levels = Lists.newArrayList(in.levels);
        
        Arrays.sort(in.levels, new LevelCompare());
        
        StringBuffer r = new StringBuffer( String.format("Case #%d: ", in.testCase) );
        
        for(int i = 0; i < in.N; ++i) {
            if (i>0)
                r.append(' ');
            
            r.append(in.levels[i].index);
        }

        return r.toString();
    }

}
