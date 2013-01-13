package codejam.y2008.round_final.juice;


import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
       //  return new String[] {"sample.in"};
        // return new String[] { "D-large-practice.in" };
       return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        
        InputData in = new InputData(testCase);
       
        in.N = scanner.nextInt();
        in.A = new int[in.N];
        in.B = new int[in.N];
        in.C = new int[in.N];
        
        for(int i = 0; i < in.N; ++i) {
            in.A[i] = scanner.nextInt();
            in.B[i] = scanner.nextInt();
            in.C[i] = scanner.nextInt();
        }
        return in;
    }

        
    
    public String handleCase(InputData in) {

        //Try all C's
        int maxSatis = 0;
        
        for(int cCandidate = 0; cCandidate <= 10000; ++cCandidate) {
            
            //First prerecord how many are on each horizontal and vertical line below
            //the line A + B <= 10000 - C
            
            //Let A be the x axis and B the y
            int[] horizontal = new int[10002];
            int[] vertical = new int[10001];
            
            for(int n = 0; n < in.N; ++n) {
                if (in.C[n] <= cCandidate && in.A[n] + in.B[n] + cCandidate <= 10000) {
                    vertical[in.A[n]]++;
                    horizontal[in.B[n]]++;
                }
            }
            
            int Q = 0;
            
            //Now all points on the line are tried.  10000-cCan is the x intercept
            for(int aOnLine = 0; aOnLine <= 10000 - cCandidate; ++aOnLine) {
                int bOnLine = 10000 - cCandidate - aOnLine;
                Q = Q + vertical[aOnLine] - horizontal[bOnLine+1];
                if (Q > maxSatis) {
                    maxSatis = Q;
                }
            }
        }
        
        return String.format("Case #%d: %d", in.testCase, maxSatis);        
    }

}
