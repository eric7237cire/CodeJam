package codejam.y2010.round_final.travel_plan;

import java.util.Arrays;
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
        // return new String[] {"sample.in"};
        // return new String[] {"B-small-practice.in"};
        return new String[] { "C-small-practice.in", "C-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        /*
         *  Each test case description starts with a line containing the number of planets N.
         *   The next line contains N numbers Xi, the coordinates of the planets.
         *    The next line contains the amount of fuel F that you have.
         */
        
        in.N = scanner.nextInt();
        
        in.X = new long[in.N];
        
        for(int p = 0; p < in.N; ++p) {
            in.X[p] = scanner.nextLong();
        }
        
        in.F = scanner.nextLong();

        return in;
    }

    @Override
    public String handleCase(InputData in) {
        
        //It does not matter that we start/stop at X1, it is a cycle path, so it would work from any planet
        Arrays.sort(in.X);
        
        long[] intervals = new long[in.N - 1];
        
        for(int p = 1; p < in.N; ++p) {
            intervals[p-1] = in.X[p] - in.X[p-1];
        }
        
        int midIndex = in.N / 2;
        
        int maxMiddleTimes = (in.N - midIndex) * 2;
        
        
        
        return String.format("Case #%d: %d", in.testCase, num_boxes);

    }
}