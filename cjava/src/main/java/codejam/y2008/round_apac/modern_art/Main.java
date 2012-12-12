package codejam.y2008.round_apac.modern_art;

import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.N = scanner.nextInt();
        input.largeConnections = new ArrayList<>(input.N-1);
        for(int i = 0; i < input.N - 1; ++i) {
            input.largeConnections.add(new ImmutablePair<>(scanner.nextInt(), scanner.nextInt()));
        }
        input.M = scanner.nextInt();
        input.smallConnections = new ArrayList<>(input.M-1);
        for(int i = 0; i < input.M - 1; ++i) {
            input.smallConnections.add(new ImmutablePair<>(scanner.nextInt(), scanner.nextInt()));
        }
        return input;
    }

    @Override
    public String handleCase(InputData input) {
        // TODO Auto-generated method stub
        return null;
    }

}
