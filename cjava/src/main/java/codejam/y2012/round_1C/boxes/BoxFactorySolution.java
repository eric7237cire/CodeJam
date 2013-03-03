package codejam.y2012.round_1C.boxes;

import java.util.Scanner;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2012.round_1C.boxes.SolInputData.Element;

public class BoxFactorySolution extends InputFilesHandler
implements TestCaseHandler<SolInputData>, TestCaseInputScanner<SolInputData>, DefaultInputFiles 
{
    public BoxFactorySolution() {
        super("C", 1, 1);
        setLogInfo();
    }

    @Override
    public SolInputData readInput(Scanner scanner, int testCase)
    {
        SolInputData in = new SolInputData(testCase);
        in.N = scanner.nextInt();
        in.M = scanner.nextInt();
        
        in.a = new Element[in.N+1];
        in.b = new Element[in.M+1];
        
        for(int i = 1; i <= in.N; ++i)
        {            
            in.a[i] = new Element(scanner.nextLong(), scanner.nextInt());            
        }
        
        for(int i = 1; i <= in.M; ++i)
        {
            in.b[i] = new Element(scanner.nextLong(), scanner.nextInt());
        }
        return in;
    }

    @Override
    public String handleCase(SolInputData in)
    {
        /**
         * dp[i][j] = 
         * Longest subsequence of using A[1..i] B[1..j]
         */
        long [][] dp = new long[in.N+1][in.M+1];
        
        
        for (int i = 1; i <= in.N; ++i) {
            for (int j = 1; j <= in.M; ++j) {
                if (in.a[i].type == in.b[j].type) {
                    long totalA = in.a[i].count;
                    long totalB = in.b[j].count;
                    int prevA = i - 1;
                    int prevB = j - 1;
                    boolean finished = false;
                    while (!finished) {
                        dp[i][j] = Math.max(dp[i][j], dp[prevA][prevB] + Math.min(totalA, totalB));
                        int state = (totalA < totalB) ? -1 : (totalA == totalB ? 0 : 1);
                        finished = false;
                        //total B >= total A, so try to match more from A of the same type
                        if (state <= 0) {
                            //Skip over anything of the other type
                            while (prevA > 0 && in.a[prevA].type != in.a[i].type) {
                                --prevA;
                            }
                            if (prevA == 0) {
                                finished = true;
                            } else {
                                totalA += in.a[prevA].count;
                                --prevA;
                            }
                        }
                        if (state >= 0) {
                            while (prevB > 0 && in.b[prevB].type != in.b[j].type) {
                                --prevB;
                            }
                            if (prevB == 0) {
                                finished = true;
                            } else {
                                totalB += in.b[prevB].count;
                                --prevB;
                            }
                        }                        
                    }
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return String.format("Case #%d: %d", in.testCase, dp[in.N][in.M]);

    }


}

