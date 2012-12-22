package codejam.y2009.round_4.year_more;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.fraction.BigFraction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
    //   return new String[] {"sample.in"};
     //   return new String[] {"A-small-practice.in"};
        return new String[] {"A-small-practice.in", "A-large-practice.in"};
    }
    
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

        log.info("Starting case {}", input.testCase);
        List<Tournament> tournaments = input.tournaments;

        // Happiness h = Happiness.create(N, 50, 10000, tournaments);
        RealSolution h = RealSolution.create(N, 50, 10000, tournaments);

        //
        // BigInteger[] div = h.getNumerator().divideAndRemainder((h.denom));
        //
        // BigInteger divisor = div[1].gcd((h.denom));

        // String ans = "" + div[0].toString() + "+" + div[1].divide(divisor) + "/" + h.denom.divide(divisor);

        BigFraction ev = h.expectedValue;
        long wholeNumber = ev.longValue();
        ev = ev.subtract(wholeNumber);

        return String.format("Case #%d: %d+%d/%d", input.testCase, wholeNumber, ev.getNumerator(), ev.getDenominator());
    }
}