package codejam.y2009.round_2.stock_charts;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.graph.GraphAdjList;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

    private GraphAdjList buildCanMatch(InputData input) {
        GraphAdjList graph = new GraphAdjList(input.n * 2);

        for (int i = 0; i < input.n; ++i) {

            for (int j = 0; j < input.n; ++j) {
                boolean canMatchB = isStrictlyGreater(input.stocks.get(i),
                        input.stocks.get(j));
                if (canMatchB)
                    graph.addConnection(i, j + input.n);
            }
        }

        return graph;
    }

    private boolean isStrictlyGreater(List<Integer> a, List<Integer> b) {

        // Preconditions.checkArgument(a.size() == k && b.size() == k);
        for (int i = 0; i < a.size(); ++i) {
            if (a.get(i) <= b.get(i)) {
                return false;
            }
        }

        return true;
    }

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.n = scanner.nextInt();
        input.k = scanner.nextInt();

        input.stocks = new ArrayList<>();
        for (int n = 0; n < input.n; ++n) {
            List<Integer> values = new ArrayList<>(input.k);
            for (int k = 0; k < input.k; ++k) {
                int value = scanner.nextInt();
                values.add(value);
            }
            input.stocks.add(values);
        }

        return input;
    }

    /* (non-Javadoc)
     * @see com.eric.codejam.multithread.Consumer.TestCaseHandler#handleCase(int, java.lang.Object)
     */
    @Override
    public String handleCase(InputData data) {

        GraphAdjList graph = buildCanMatch(data);
        
        List<Integer> lhsNodes = new ArrayList<>();
        List<Integer> rhsNodes = new ArrayList<>();
        
        for(int n = 0; n < data.n; ++n) {
            lhsNodes.add(n);
            rhsNodes.add(n+data.n);
        }
        
        List<Pair<Integer,Integer>> matches = graph.getMaxMatching(lhsNodes,rhsNodes);
        int min = data.n - matches.size();

        return ("Case #" + data.testCase + ": " + min);
    }
}