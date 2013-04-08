package codejam.y2013.round_online.oceanview;

import java.util.Scanner;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;


public class OceanView extends InputFilesHandler implements
TestCaseHandler<InputData>, TestCaseInputScanner<InputData> 
{
    public OceanView()
    {
        super("C", 1,1);
        setLogInfo();
        
    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData in = new InputData(testCase);
        int N  = scanner.nextInt();
        in.heights = new int[N];
        
        for(int i = 0; i < N; ++i)
        {
            in.heights[i] = scanner.nextInt();                    
        }
        return in;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in) 
    {
        
        int[] dp = new int[in.heights.length];
        
        int max = 1;
        
        for(int i = 0; i < dp.length; ++i)
        {
            dp[i] = 1;
            
            for(int j = 0; j < i; ++j)
            {
                if (in.heights[j] < in.heights[i])
                {
                    dp[i] = Math.max(dp[i], 1+dp[j]);
                    max = Math.max(max, dp[i]);
                }
            }
        }
        
        return String.format("Case #%d: %d", in.testCase, dp.length - max);
       
    }

}

    

