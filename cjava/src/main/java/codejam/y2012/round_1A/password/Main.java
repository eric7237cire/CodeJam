package codejam.y2012.round_1A.password;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleFormat;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        // return new String[] {"sample.in"};
        return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        in.A = scanner.nextInt();
        in.B = scanner.nextInt();

        in.p = new double[in.A];
        for (int i = 0; i < in.A; ++i) {
            in.p[i] = scanner.nextDouble();
        }

        // log.info("TestCase {} Grid {}", testCase, in.grid);
        return in;
    }

    public String handleCase(InputData in) {

        // Prob correct
        double[] pProduct = new double[in.A];

        pProduct[0] = in.p[0];
        for (int i = 1; i < in.A; ++i) {
            pProduct[i] = pProduct[i - 1] * in.p[i];
        }

        double evEnterRightAway = 2 + in.B;

        double bestEV = evEnterRightAway;

        for (int bsCount = 0; bsCount <= in.A; ++bsCount) {

            double allProb = (bsCount == in.A ? 1 : pProduct[in.A - 1 - bsCount]);

            double evBs = allProb * (2 * bsCount + in.B - in.A + 1) + (1 - allProb) * (2 * bsCount + in.B - in.A + 1 + in.B + 1);
            bestEV = Math.min(bestEV, evBs);
        }

        return String.format("Case #%d: %s", in.testCase, DoubleFormat.df6.format(bestEV));

    }

}
