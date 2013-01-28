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
        //Number characters already typed
        in.A = scanner.nextInt();
        
        //Total characters in password
        in.B = scanner.nextInt();

        in.p = new double[in.A];
        for (int i = 0; i < in.A; ++i) {
            //Probability letter was correctly typed
            in.p[i] = scanner.nextDouble();
        }

        return in;
    }

    public String handleCase(InputData in) {

        /**
         *  Prob that characters 0..i typed correctly
         */
        double[] pProduct = new double[in.A];

        
        pProduct[0] = in.p[0];
        for (int i = 1; i < in.A; ++i) {
            pProduct[i] = pProduct[i - 1] * in.p[i];
        }

        /**
         * Cost will be enter, the length of entire password, return
         */
        final double evEnterRightAway = 2 + in.B;

        double bestEV = evEnterRightAway;

        for (int bsCount = 0; bsCount <= in.A; ++bsCount) {

            //Probability that all non erased letters are correct
            double allProb = (bsCount == in.A ? 1 :
                pProduct[in.A - 1 - bsCount]); //remaining letters (-1 for 0 based index)

            /**
             * the expected value is 
             * 
             * probability that remaining characters are correct *
             *     cost of backspaces, retyped characters (the 2*)
             *     then the rest of the password, plus enter
             *     
             * probability that a mistake was made in the non erased portion *
             *      the exact same cost as before + retyping the pw + return      
             */
            double evBs = allProb * (2 * bsCount + in.B - in.A + 1) + 
                    (1 - allProb) * (2 * bsCount + in.B - in.A + 1 + in.B + 1);
            bestEV = Math.min(bestEV, evBs);
        }

        return String.format("Case #%d: %s", in.testCase, DoubleFormat.df6.format(bestEV));

    }

}
