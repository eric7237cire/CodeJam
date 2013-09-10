package codejam.y2013.round_qual.fairsquare;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.TreeSet;

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
        super("C", 0, 0);
        //setLogInfo();
        
    }
    
    public String[] getDefaultInputFiles()
    {
        if (false)
        {
            return super.getDefaultInputFiles();
        } else {
            return new String[] { "C-small-practice.in", "C-large-practice-1.in" };
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
        
        
       // return handleCaseBruteForce(in);
        return handleCaseBruteForceFaster(in);
       
    }
    

    static TreeSet<Long> set = new TreeSet<Long>();
    
    public String handleCaseBruteForceFaster(InputData in) 
    {
        
        if (set.size() == 0)
        {
        for(long i = 0; i < 100000000; ++i)
        {
            if (BruteForce.isPalin(BigInteger.valueOf(i)))
            {
                long sq = i * i;
                if (BruteForce.isPalin(BigInteger.valueOf(sq)))
                {
                    log.debug("Adding {}*{}= {}", i, i, sq);
                    //log.debug("Adding 100000000000000");
                    set.add(sq);
                }
            }
        }
        }
        
        long start =  in.start.longValue();
        long stop =  in.stop.longValue();
        
        int count = 
        set.headSet(stop, true).tailSet(start,true).size();
        
        log.debug("Start {} stop  {}", start, stop);
        
        return String.format("Case #%d: %d", 
                    in.testCase, count);
        
       
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
                    log.debug("Palin fair & square {} root {}", i, sqRoot);
                    ++count;
                }
            }
            
        }
        return String.format("Case #%d: %d", 
                    in.testCase, count);
        
       
    }

}
