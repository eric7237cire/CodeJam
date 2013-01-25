package codejam.y2012.round_1C.diamond_inheritance;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import codejam.utils.datastructures.graph.GraphInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

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
        in.N = scanner.nextInt();
        in.graph = new GraphInt();
        
        for(int n = 1; n <= in.N; ++n) {
            int M = scanner.nextInt();
            for(int m = 0; m < M; ++m) {
                int inhFrom = scanner.nextInt();
                in.graph.addOneWayConnection(n, inhFrom);
            }
        }

        return in;
    }

    public String handleCase(InputData in) {

        for(int startNode = 0; startNode < in.N; ++startNode) {
            Set<Integer> visitedNodes = Sets.newHashSet();
    
            LinkedList<Integer> toVisit = new LinkedList<>();
            toVisit.add(startNode);
    
            while (!toVisit.isEmpty()) {
    
                Integer loc = toVisit.poll();
    
                if (visitedNodes.contains(loc)) {
                    return String.format("Case #%d: Yes", in.testCase);
                }
    
                visitedNodes.add(loc);
    
                Set<Integer> adjNodes = in.graph.getNeighbors(loc);
    
                if (adjNodes == null)
                    continue;
    
                for (Integer child : adjNodes) {
                    toVisit.add(child);
                }
            }
        
        }
       
        return String.format("Case #%d: No", in.testCase);
    }

}
