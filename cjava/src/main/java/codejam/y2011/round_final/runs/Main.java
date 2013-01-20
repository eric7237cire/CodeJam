package codejam.y2011.round_final.runs;

import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.LargeNumberUtils;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
       // return new String[] { "sample.in" };
        // return new String[] { "A-small-practice.in" };
         return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        in.S = scanner.next();

        return in;
    }
    
    int getRuns(String s)
    {
        char lastCh = s.charAt(0);
        int runCount = 1;
        int boundaryBetDistinct = 0;
        int boundaryBetIdentical = 0;
        
        for(int cIdx = 1; cIdx < s.length(); ++cIdx)
        {
            char curCh = s.charAt(cIdx);
            
            if (curCh == lastCh) {
                ++boundaryBetIdentical;
            } else {
                ++runCount;
                ++boundaryBetDistinct;
                lastCh = curCh;
            }
        }
        
        //S : M = Na + Nb ....
        //Runs r0
        
        //Where we add 'x'
        Preconditions.checkState(runCount - 1 == boundaryBetDistinct);
        
        //'y'
        Preconditions.checkState(s.length() - runCount == boundaryBetIdentical);
        
        return runCount;
        
    }
    
    /**
     * 
     * @param M total number of all characters N_0 to N_c-1
     * @param Nc number of characters being added
     * @param r0 initial number of runs
     * @param r target number of runs
     */
    int countTransitions(int M, int Nc, int r0, int r, int[] fact)
    {
        if (r0 == 0)
            return r == 1 ? 1 : 0;
        
        if (M < r0)
            return 0;
       
        int result = 0;
        
        //Adding x to boundaries between distinct (adds 1 run)
        //Adding y to boundaries between identical (adds 2 runs)
        // r0 + x + 2y = r
        for( int y = 0; r0 + 2 * y <= r; ++y)
        {
            int x = r - (r0 + 2 * y);
            
            if (Nc - 1 < x+y - 1)
                continue;
            
            if (M - r0 < y)
                continue;
            
            if (r0 + 1 < x)
                continue;
            
            //There are r0 - 1 distinct boundaries + the begining and end
            int nways_select_x = LargeNumberUtils.choose(r0 + 1, x, mod, fact);
            
            //Between identical
            int nways_select_y = LargeNumberUtils.choose(M - r0, y, mod, fact);
            
            //Now we use the 'stars and bars' idea to distribute Nc characters between x + y runs
            int nways_split = LargeNumberUtils.choose(Nc - 1, x+y-1, mod, fact);
            
            result = IntMath.checkedAdd(result,
                    LargeNumberUtils.safeMult(
                            LargeNumberUtils.safeMult(nways_select_x, nways_select_y, mod),
                            nways_split, mod)) % mod;
        }
        
        return result;
        
        
    }
    
    static final int mod = 1000003;

    /**
     * Looked at the solution.  Basically you can never have
     * a straight that encompasses another, so use a greedy strategy
     * to add the card to the shortest straight.
     */
    public String handleCase(InputData in) {

        int[] fact = LargeNumberUtils.generateModFactorial(in.S.length(), mod);
        
        int[] freq = new int[26];
        for(int ch = 0; ch < 26; ++ch) {
            freq[ch] = StringUtils.countMatches(in.S, "" + ( (char) ('a' + ch ) ) );
        }
        
        int M = 0;
        int runs_goal = getRuns(in.S);
        int[] runs_count = new int[ runs_goal + 1];
        runs_count[0] = 1;  //runs_count[0] = 1 means ways of using prev character to be exactly [0] runs 
        
        //Get X(c, r) == arranging all characters of type c to be exactly r runs
        //Recurence X(c-1, r0) * transitions(...) = 
        
        for(int i = 0; i < 26; ++i) {
            int Nc = freq[i];
            
            if (Nc == 0)
                continue;
            
            int[] old_runs_count = runs_count; //Arrays.copyOf( runs_count,  runs_count.length );
            
            runs_count = new int[ runs_goal + 1];
            
            for(int r0 = 0; r0 <= runs_goal; ++r0) {
                for(int r = r0 + 1; r <= runs_goal; ++r) {
                    int nways = countTransitions(M, Nc, r0, r, fact);
                    runs_count[r] = IntMath.checkedAdd(runs_count[r],
                            LargeNumberUtils.safeMult(nways, old_runs_count[r0],mod)) % mod;
                }
            }
            M += Nc;
        }
        
        return String.format("Case #%d: %d", in.testCase, runs_count[runs_goal]);
        
    }

}
