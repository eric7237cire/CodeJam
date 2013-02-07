package codejam.y2008.round_beta.random_route;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.graph.WeightedGraphInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
        
        in.nRoads = scanner.nextInt();
        
        Map<String, Integer> strToNode = Maps.newHashMap();
        
        //First node is 0
        getNode(scanner.next(), strToNode);
        
        in.graph = new WeightedGraphInt();
        in.roads = Lists.newArrayList();
        
        for(int r = 0; r < in.nRoads; ++r) {
            int node1 = getNode(scanner.next(), strToNode);
            int node2 = getNode(scanner.next(), strToNode);
            in.roads.add(buildEdge(node1,node2));
            int weight = scanner.nextInt();
            in.graph.addConnection(node1, node2, weight);
        }

        return in;
    }
    
    Pair<Integer,Integer> buildEdge(int node1, int node2) {
        return new ImmutablePair<>(Math.min(node1,node2), Math.max(node1,node2));
    }
    
    int getNode(String str, Map<String, Integer> strToNode) {
        Integer nodeId = strToNode.get(str);
        
        if (nodeId == null) {
            nodeId = strToNode.size();
            strToNode.put(str,nodeId);
        }
        
        return nodeId;
    }

    
        
    @Override
    public String handleCase(InputData in)
    {
        /*
        WeightedGraphInt g = new WeightedGraphInt();
        g.addConnection(0,1,3);
        g.addConnection(1,3,1);
        g.addConnection(2,3,2);
        g.addConnection(0,2,2);
        
        //2 paths to 3
        g.addConnection(0, 4, 7);
        g.addConnection(1, 4, 4);
        g.addConnection(3, 4, 3);
        g.addConnection(2, 4, 5);
        
        g.addConnection(3, 5, 2);
        g.addConnection(5, 4, 1);
        */
        
        WeightedGraphInt g = in.graph;
        Map<Pair<Integer,Integer>, Double> edgeHits = Maps.newHashMap();
        for(Pair<Integer,Integer> r : in.roads) {
            edgeHits.put(r,0d);
        }
        
        DijkstraNode[] dnodes = doDijkstra(g, 0, g.V());
    
        for(int targetCity = 1; targetCity < g.V(); ++targetCity)
        {
            List<List<Integer>> paths = Lists.newArrayList();
            
            List<Integer> init = Lists.newArrayList();
            init.add(targetCity);
            
            Queue<List<Integer>> toVisit = new LinkedList<>();
            toVisit.add(init);
            
            while(!toVisit.isEmpty()) 
            {
                List<Integer> path = toVisit.poll();
                
                //start node
                if (path.get(path.size()-1) == 0) {
                    paths.add(path);
                    continue;
                }
                
                int last = path.get(path.size()-1);
                
                for(int prev : dnodes[last].previous)
                {
                    List<Integer> newPath = Lists.newArrayList(path);
                    newPath.add(prev);
                    toVisit.add(newPath);
                }
            }
            
            for(List<Integer> path : paths) {
                for(int n1 = 0; n1 < path.size(); ++n1) {
                    for(int n2 = n1 + 1; n2 < path.size(); ++n2)
                    {
                        // 1 / # of target cities
                        double prob = 1d / (g.V()-1);
                        
                        // 1 / # of paths
                        prob *= 1d / paths.size();
                        
                        prob += edgeHits.get(buildEdge(n1,n2));
                        
                        edgeHits.put(buildEdge(n1,n2), prob);
                    }
                }
            }
            
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append("Case #" + in.testCase + ": ") ;
        
        for(Pair<Integer,Integer> r : in.roads) {
            sb.append(edgeHits.get(r));
            sb.append(" ");
        }
        
        return sb.toString();
    }

}