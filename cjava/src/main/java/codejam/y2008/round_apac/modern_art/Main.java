package codejam.y2008.round_apac.modern_art;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.GraphAdjList;
import codejam.utils.datastructures.GraphInt;
import codejam.utils.datastructures.TreeInt;
import codejam.utils.datastructures.TreeInt.Node;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] { "D-small-practice.in", "D-large-practice.in" };
    }
    
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
        
        if (input.M == 1) {
            return "Case #" + input.testCase + ": YES";
        }
        
        
        GraphInt largeGraph = new GraphInt();
        GraphInt smallGraph = new GraphInt();
        
        for(Pair<Integer,Integer> edge : input.largeConnections) {
            largeGraph.addConnection(edge.getLeft(), edge.getRight());
        }
        for(Pair<Integer,Integer> edge : input.smallConnections) {
            smallGraph.addConnection(edge.getLeft(), edge.getRight());
        }
        
        TreeInt largeTree = largeGraph.convertToTree(1);
        TreeInt smallTree = smallGraph.convertToTree(1);
        
        for(int largeRoot = 1; largeRoot <= input.N; ++largeRoot) {
            TreeInt newTree = largeTree.reroot(largeRoot);
        
            boolean is = top_down_unordered_subtree_isomorphism(smallTree.getRoot(), newTree.getRoot());
            
            if (is) {
                return "Case #" + input.testCase + ": YES";
            } 
            
        }
        return "Case #" + input.testCase + ": NO";
    }

    //True if the subtrees at smallTreeNode and largeTreeNode are isomorphic
    boolean top_down_unordered_subtree_isomorphism(
            Node smallTreeNode,  Node largeTreeNode
    // node_array<set<node> >& B
    ) {

        int smallChildrenCount = smallTreeNode.getChildren().size();
        int largeChildrenCount = largeTreeNode.getChildren().size();

        if (smallChildrenCount > largeChildrenCount || smallTreeNode.getHeight() > largeTreeNode.getHeight()
                || smallTreeNode.getSize() > largeTreeNode.getSize())
            return false;

        GraphAdjList graph = new GraphAdjList(largeChildrenCount + smallChildrenCount);
        // Bipartite match from larger tree nodes children to smaller ones

        List<Integer> lhsNodes = new ArrayList<>();
        List<Integer> rhsNodes = new ArrayList<>();

        for (int i = 0; i < largeChildrenCount; ++i) {
            lhsNodes.add(i);
        }
        for (int i = 0; i < smallChildrenCount; ++i) {
            rhsNodes.add(i + largeChildrenCount);
        }

        List<Node> lcChildNodes = new ArrayList<Node>(
                largeTreeNode.getChildren());
        List<Node> smChildNodes = new ArrayList<Node>(
                smallTreeNode.getChildren());

        for (int largeChildNodeIdx = 0; largeChildNodeIdx < largeChildrenCount; ++largeChildNodeIdx) {
            for (int smallChildNodeIdx = 0; smallChildNodeIdx < smallChildrenCount; ++smallChildNodeIdx) {
                Node lc = lcChildNodes.get(largeChildNodeIdx);
                Node sm = smChildNodes.get(smallChildNodeIdx);

                if (top_down_unordered_subtree_isomorphism( sm,
                         lc)) {
                    graph.addConnection(largeChildNodeIdx, smallChildNodeIdx
                            + largeChildrenCount);
                }
            }
        }

        List<Pair<Integer, Integer>> L = graph.getMaxMatching(lhsNodes,
                rhsNodes);

        if (L.size() == smallChildrenCount) {
            return true;
        } else {
            return false;
        }
    }
}
