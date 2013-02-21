package codejam.y2012.round_final.shifting_paths;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.IntegerPair;
import codejam.utils.utils.LongIntPair;

import com.google.common.collect.Lists;

public class ShiftingPaths extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles
{

   
    public ShiftingPaths() {
        super("E", 1, 0);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);

        in.N = scanner.nextInt();
        in.leftRight = new IntegerPair[in.N];

        for (int i = 0; i < in.N - 1; ++i)
        {
            in.leftRight[i] = new IntegerPair(scanner.nextInt()-1, scanner.nextInt()-1);
        }
        return in;
    }

    public String handleCase(InputData in)
    {

        long visitedOdd = (1L << in.N) - 1;
        int curLoc = 0;
                
        int maxSteps = (1 << 10) * 10;
       // maxSteps = 10;
        
        int step = 0;
        for( ; step < maxSteps; ++step)
        {
            log.debug("Step {} visited {} cur location {}", step, StringUtils.reverse( Long.toBinaryString(visitedOdd) ), curLoc+1);
            boolean goLeft = (visitedOdd & 1 << curLoc) != 0;
            
            visitedOdd ^= 1 << curLoc;
            
            curLoc = goLeft ? in.leftRight[curLoc].first() : in.leftRight[curLoc].second();
            
            if (curLoc == in.N - 1)
                break;
        }
        
        
        if (step == maxSteps)
        {
            return String.format("Case #%d: Infinity", in.testCase);
        }
        return String.format("Case #%d: %d", in.testCase, 1+step);

    }

}
