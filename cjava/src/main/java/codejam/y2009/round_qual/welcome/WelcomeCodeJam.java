package codejam.y2009.round_qual.welcome;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class WelcomeCodeJam extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    public WelcomeCodeJam()
    {
        super("C", 1, 1);
        
    }
	@Override
    public InputData readInput(Scanner scanner, int testCase) {
       InputData in = new InputData(testCase);
       in.S = scanner.nextLine();
       if (in.S.length() == 0)
           in.S = scanner.nextLine();
       return in;
    }

    @Override
    public String handleCase(InputData in) {
        String target = "welcome to code jam";

        //400263727 
        
        int[][] dp = new int[in.S.length()+1][target.length()];
        
        for(int i = 1; i <= in.S.length(); ++i)
        {
            log.debug("Position {} prev counts {}", i, dp[i-1]);
            
            char srcChar = in.S.charAt(i-1);
            
            for(int j = 0; j < target.length(); ++j)
            {
                char tarChar = target.charAt(j);
                
                if (srcChar != tarChar)
                {
                    dp[i][j] = dp[i-1][j];
                } else {
                    
                    if (j == 0) {
                        dp[i][j] = dp[i-1][j] + 1;
                    } else {
                        dp[i][j] = dp[i-1][j] + dp[i-1][j-1];
                    }
                }
                
                dp[i][j] %= 10000;
            }
        }
        

        String ans = Integer.toString(dp[in.S.length()][target.length()-1]);
        ans = StringUtils.leftPad(ans, 4, '0'); 
        
        return "Case #" + in.testCase + ": " + ans;

    }
}