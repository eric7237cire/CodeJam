package codejam.y2013.round_qual.fairsquare;

import java.math.RoundingMode;
import java.util.Scanner;

import com.google.common.math.DoubleMath;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2009.round_3.interesting_ranges.BruteForce;


public class FairAndSquare extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData> 
{
    public FairAndSquare()
    {
        super("C", 1, 0);
        //setLogInfo();
        
    }
    
    public String[] getDefaultInputFiles()
    {
        if (true)
        {
            return super.getDefaultInputFiles();
        } else {
            return new String[] { "C-large-practice-1.in" };
        }
        
    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        
        in.start = scanner.nextBigInteger(10);
        in.stop  = scanner.nextBigInteger(10);
        
        
        return in;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in) 
    {
        
        
        return handleCaseBruteForce(in);
       
    }
    
    public String handleCaseBruteForce(InputData in) 
    {
        
        int start = (int) in.start.longValue();
        int stop = (int) in.stop.longValue();
        
        int count = 0;
        for(int i = start; i <= stop; ++i)
        {
            if (BruteForce.isPalin(i))
            {
                //log.debug("Palin {}", i);
                int sqRoot = DoubleMath.roundToInt(Math.sqrt(i), RoundingMode.HALF_EVEN);
                
                if (sqRoot * sqRoot == i && BruteForce.isPalin(sqRoot)) 
                {
                    log.debug("Palin fair & square {}", i);
                    ++count;
                }
            }
            
        }
        return String.format("Case #%d: %d", 
                    in.testCase, count);
        
       
    }

}
