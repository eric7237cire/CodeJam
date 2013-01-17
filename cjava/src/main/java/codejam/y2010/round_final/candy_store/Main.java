package codejam.y2010.round_final.candy_store;

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

        in.maxPeople = scanner.nextInt();
        in.maxCents = scanner.nextLong();

        return in;
    }

    @Override
    public String handleCase(InputData in) {
        long sum = 0, num_boxes = 0;
        while (sum < in.maxCents * in.maxPeople) {
            num_boxes++;
            sum += (sum / in.maxPeople) + 1;
        }

        return String.format("Case #%d: %d", in.testCase, num_boxes);

    }
}