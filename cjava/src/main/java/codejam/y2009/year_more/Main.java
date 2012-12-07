package codejam.y2009.year_more;

import java.io.File;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    
    
    public static class Tournament {
        int rounds;
        int[] roundDays;
    }
    

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        int N = scanner.nextInt();
        int T = scanner.nextInt();
        InputData input = new InputData(testCase);
        input.N = N;
        input.T = T;
        
        List<Tournament> tournaments = new ArrayList<>();
        for(int i = 0; i < T; ++i) {
            Tournament t = new Tournament();
            t.rounds = scanner.nextInt();
            t.roundDays = new int[t.rounds];
            t.roundDays[0] = 0;
            for(int j =1; j < t.rounds; ++j) {
                t.roundDays[j] = scanner.nextInt() - 1;
            }
            tournaments.add(t);
        }
        input.tournaments = tournaments;
        return input;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData input) {
        int N = input.N;
        int T = input.T;
        

        log.info("Starting case {}", input.testCase);
        List<Tournament> tournaments = input.tournaments;
        
//        Happiness h = Happiness.create(N, 50, 10000, tournaments);
        RealSolution h = RealSolution.create(N, 50, 10000, tournaments);
        
        BigInteger[] div = BigInteger.valueOf(h.getNumerator()).divideAndRemainder(BigInteger.valueOf(h.denom));
        
        BigInteger divisor = div[1].gcd(BigInteger.valueOf(h.denom));
        
        String ans  = "" + new Integer(div[0].intValue() + h.wholeNumber).toString() + "+" + div[1].divide(divisor) + "/" + BigInteger.valueOf(h.denom).divide(divisor);
                


        return ("Case #" + input.testCase + ": " + ans);
    }
}