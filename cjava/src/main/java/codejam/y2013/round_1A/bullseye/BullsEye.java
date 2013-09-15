package codejam.y2013.round_1A.bullseye;

import java.util.Scanner;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.math.LongMath;


public class BullsEye extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData> 
{
    public BullsEye()
    {
        super("A", 1, 1);
        //setLogInfo();
        
    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
        InputData in = new InputData(testCase);
        
        in.radius = scanner.nextLong();
        in.paint = scanner.nextLong();
        
        return in;
    }
    
    long getPaintNeeded(long n, long radius)
    {
        try {
            //2*r+1
        long a_1 = LongMath.checkedMultiply(2, radius) + 1;
        
            //a_1 + 4 * (n-1)
        long a_n = LongMath.checkedAdd(a_1, 
                LongMath.checkedMultiply(4L, n-1));
        
        //n * (a_1*a_n) / 2
        long ans = LongMath.checkedMultiply(n, LongMath.checkedAdd(a_1, a_n)) / 2;
        return ans;
        
        } catch (ArithmeticException ex) {
            return Long.MAX_VALUE;
        }
        
    }
    
    long binarySearch(long radius, long paintAmt)
    {
        long possible = 1;
        long impossible = Long.MAX_VALUE;
       // long impossible = 1000000000;
        
        while(true)
        {
            long testMid = possible + (impossible - possible) / 2;
            
            long paintNeeded = getPaintNeeded(testMid, radius);
            
            if (paintNeeded <= paintAmt)
            {
                possible = testMid;
            } else {
                impossible = testMid;
            }
            
            if (impossible - possible <= 1)
            {
                return possible;
            }
        }
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in) 
    {
        
        long maxN = binarySearch(in.radius, in.paint);
        return String.format("Case #%d: %d", 
                in.testCase,
                maxN);
       
       
    }

    public String handleCaseBruteForce(InputData in) 
    {
        long paintLeft = in.paint;
        long r = in.radius;
        /*
         * (r+b+1)^2 - (r+b)^2 simplifies to
         * 2r + b + 1 
         * or
         * 2r + 2a
         * 
         * 2r + (2*i - 1) for i 1 to whatever
         */
        for(int i = 0; i < 1000; ++i)
        {
            int a = 2*i + 1;
            int b = a - 1;
            
            long paintNeeded = (a+r) * (a+r) - (b+r) * (b+r);
            //log.debug("Paint needed {} a={} b={} r={}", paintNeeded, a, b, r);
            if (paintNeeded <= paintLeft)
            {
                paintLeft -= paintNeeded;
            } else {
                
                long test = getPaintNeeded(i, in.radius);
                log.debug("Test {}.  paint {}", test, in.paint);
                return String.format("Case #%d: %d", 
                        in.testCase,
                        i);
            }
        }
       
        
        return String.format("Case #%d: UH", 
                in.testCase);
       
    }
}
