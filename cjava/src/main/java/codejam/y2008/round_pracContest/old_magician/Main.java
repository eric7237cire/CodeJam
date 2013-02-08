package codejam.y2008.round_pracContest.old_magician;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        super("A", 1,1);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.W = scanner.nextInt();
        in.B = scanner.nextInt();
        
        return in;
    }
    
    
    int solve(int w, int b, int[][] memo) {
        if (memo[w][b] >= 0)
            return memo[w][b];
        
        if (w == 0 && b == 1) {
            return 2;
        }
        if (w == 1 && b == 0) {
            return 1;
        }
        
        int ans = -1;
        if (w >= 2) {
            //2 whites replaced with white
            int r = solve(w - 1, b,memo);
            if (ans == -1 || ( ans != 0 && ans == r)) {
                ans = r;
            } else {
                //unknown
                ans = 0;
            }
        }
        
        if (b >= 2) {
            int r = solve(w + 1, b - 2,memo);
            if ( ans == -1 || ( ans != 0 && ans == r) ) {
                ans = r;
            } else {
                //unknown
                ans = 0;
            }
        }
        
        if (w >= 1 && b >= 1) {
            int r = solve(w - 1, b, memo);
            if (ans == -1 || ( ans != 0 && ans == r)) {
                ans = r;
            } else {
                //unknown
                ans = 0;
            }
        }
        
        memo[w][b] = ans;
        return ans;
    }
    
    @Override
    public String handleCase(InputData in)
    {
        if (in.W >= 0) {
            String ansStr = "UNKNOWN";
            if (in.B % 2 == 1)
                ansStr = "BLACK";
            if (in.B % 2 == 0)
                ansStr = "WHITE";
            return  String.format("Case #%d: %s", in.testCase, ansStr);
        }
        
        int[][] memo = new int[2001][2001];
        for(int[] d2 : memo) {
            Arrays.fill(d2, -1);
        }
        
        for(int w = 0; w <= 50; ++w) {
            for(int b = 0; b <= 10; ++b) {
                int ans = solve(w, b, memo);
                String ansStr = "UNKNOWN";
                if (ans == 2)
                    ansStr = "BLACK";
                if (ans == 1)
                    ansStr = "WHITE";
                log.debug("w {} b {} ans {}", w, b, ansStr);
            }
        }
        
        
       
        int ans = solve(in.W, in.B, memo);
        String ansStr = "UNKNOWN";
        if (ans == 2)
            ansStr = "BLACK";
        if (ans == 1)
            ansStr = "WHITE";
        return  String.format("Case #%d: %s", in.testCase, ansStr);
        
    }
    

}