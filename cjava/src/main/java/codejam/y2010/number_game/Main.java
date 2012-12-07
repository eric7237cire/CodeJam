package codejam.y2010.number_game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    static List<Range<Integer>> ranges;
    static {
        ranges = new ArrayList<>(1000000);

        int lastLowerBound = 1;
        ranges.add(Ranges.closed(1, 1));
        for (int i = 1; i < 1000000; ++i) {
            int n = i + 1;
            if (ranges.get(lastLowerBound - 1).upperEndpoint() < n) {
                ++lastLowerBound;
            }
            ranges.add(Ranges.closed(lastLowerBound, lastLowerBound + n - 1));

        }
    }

    @Override
    public String handleCase(InputData input) {

        // log.info("Starting calculating case {}", caseNumber);

        long count = 0;

        Range<Integer> bRange = Ranges.closed(input.B1, input.B2);

        for (int a = input.A1; a <= input.A2; ++a) {

            count += bRange.upperEndpoint() - bRange.lowerEndpoint() + 1;

            Range<Integer> losingRange = ranges.get(a - 1);
            Preconditions.checkState(losingRange != null);
            if (losingRange.isConnected(bRange)) {
                Range<Integer> inter = bRange.intersection(losingRange);
                count -= (inter.upperEndpoint() - inter.lowerEndpoint() + 1);
            }

        }

        log.info("Done calculating answer case {}. ", input.testCase, count);

        return ("Case #" + input.testCase + ": " + count);
    }

    public boolean isWinning(int A, int B) {

        int G = Math.max(A, B);
        int L = Math.min(A, B);

        if (G == L) {
            return false;
        }

        if (G % L == 0)
            return true;

        

        int notFlexCount = 0;
        while (L > 0) {
            boolean flexNode = false;
            if (G - L >= L) {
                flexNode = true;
            }

            if (!flexNode) {
                notFlexCount++;
            } else {
                return notFlexCount % 2 == 0;
            }
            // log.debug("G {} L {} flexible? {}", G, L, flexNode);
            int newG = L;
            L = G % L;
            G = newG;

            G = Math.max(G, L);
            L = Math.min(G, L);
        }
        log.debug("\n\n");

        return notFlexCount % 2 == 0;

    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
            {

        

        InputData input = new InputData(testCase);
        input.A1 = scanner.nextInt();
        input.A2 = scanner.nextInt();
        input.B1 = scanner.nextInt();
        input.B2 = scanner.nextInt();

        return input;

    }

   
}