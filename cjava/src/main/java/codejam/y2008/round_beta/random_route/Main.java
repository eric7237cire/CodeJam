package codejam.y2008.round_beta.random_route;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.graph.WeightedGraphInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        super();
       // super("A", true,true);
    }
    
    static class DijkstraNode {
        int nodeId;
        
        /**
         * distance from start to nodeId
         */
        int distance;
        
        /**
         * Previous nodes on the shortest path
         */
        List<Integer> previous;
        public DijkstraNode(int nodeId, int distance) {
            super();
            this.nodeId = nodeId;
            this.distance = distance;
            this.previous = Lists.newArrayList();
        }
    }
    
    /**
     * Indexes in graph go from 0 to nodeCount - 1
     */
    public DijkstraNode[] doDijkstra(WeightedGraphInt graph, int sourceNodeId, int nodeCount) {
        
        
        DijkstraNode[] dijNodes = new DijkstraNode[nodeCount];
        
        for(int n = 0; n < dijNodes.length; ++n) {
            dijNodes[n] = new DijkstraNode(n, Integer.MAX_VALUE);
        }
        
        
        PriorityQueue<DijkstraNode> toProcess = new PriorityQueue<>(1, new Comparator<DijkstraNode>() {

            @Override
            public int compare(DijkstraNode o1, DijkstraNode o2) {
                return Integer.compare(o1.distance, o2.distance);
            }
        });
        
        dijNodes[sourceNodeId].distance = 0;
        toProcess.add(dijNodes[sourceNodeId]);
        
        while(!toProcess.isEmpty()) {
            DijkstraNode nodeU = toProcess.poll();
            
            if (nodeU.distance == Integer.MAX_VALUE) {
             // all remaining vertices are inaccessible from source
                break;
            }
            
            Set<WeightedGraphInt.Edge> neighbors = graph.getEdges(nodeU.nodeId);
            
            for(WeightedGraphInt.Edge neighbor : neighbors) {
                int alt = neighbor.weight + nodeU.distance;
                
                /**
                 * Is path via u shorter than current distance
                 * of start to v ?
                 */
                DijkstraNode nodeV = dijNodes[neighbor.to];
                if (alt < nodeV.distance) {
                    toProcess.remove(nodeV);
                    nodeV.distance = alt;
                    nodeV.previous.clear();
                    nodeV.previous.add(nodeU.nodeId);
                    toProcess.add(nodeV);                    
                }
                /**
                 * If it is the same, it is an alternative
                 */
                if (alt == nodeV.distance) {
                    if (!nodeV.previous.contains(nodeU.nodeId))
                        nodeV.previous.add(nodeU.nodeId);
                }
            }
        }
        
        return dijNodes;
                             
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        

        return in;
    }

        
    @Override
    public String handleCase(InputData in)
    {
        WeightedGraphInt g = new WeightedGraphInt();
        g.addConnection(0,1,3);
        g.addConnection(1,3,1);
        g.addConnection(2,3,2);
        g.addConnection(0,2,2);
        
        //2 paths to 3
        g.addConnection(0, 4, 7);
        g.addConnection(1, 4, 4);
        g.addConnection(3, 4, 3);
        g.addConnection(2, 4, 6);
        
        DijkstraNode[] dnodes = doDijkstra(g, 0, 5);
            
        return "Case #" + in.testCase + ": " ;
    }

}