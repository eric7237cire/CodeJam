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
import codejam.y2008.round_beta.random_route.InputData.Road;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        super("C", 0,0);
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
        Multiset<Integer> previous;
        public DijkstraNode(int nodeId, int distance) {
            super();
            this.nodeId = nodeId;
            this.distance = distance;
            this.previous = HashMultiset.create();
        }
    }
    
    /**
     * Indexes in graph go from 0 to nodeCount - 1
     */
    public DijkstraNode[] doDijkstra(WeightedGraphInt graph, int sourceNodeId, int nodeCount, InputData in) {
        
        
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
            
            if (neighbors == null)
                continue;
            
            for(WeightedGraphInt.Edge neighbor : neighbors) {
                int alt = neighbor.weight + nodeU.distance;
                
                /**
                 * Is path via u shorter than current distance
                 * of start to v ?
                 */
                DijkstraNode nodeV = dijNodes[neighbor.to];
                
                Road r = buildRoad(nodeU.nodeId,nodeV.nodeId,neighbor.weight);
                int roadCount = in.count.get(r);
                
                if (alt < nodeV.distance) {
                    toProcess.remove(nodeV);
                    nodeV.distance = alt;
                    nodeV.previous.clear();
                    nodeV.previous.setCount(nodeU.nodeId, roadCount);
                    toProcess.add(nodeV);                    
                }
                /**
                 * If it is the same, it is an alternative
                 */
                if (alt == nodeV.distance) {
                    nodeV.previous.setCount(nodeU.nodeId, roadCount);
                }
            }
        }
        
        return dijNodes;
                             
    }
    
    public int countReachableCities(DijkstraNode[] nodes, int startNode) {
        
        int reachable = 0;
        
        for(int n = 0; n < nodes.length; ++n) {
            if (n == startNode)
                continue;
        
            if (nodes[n].distance < Integer.MAX_VALUE) {
                ++reachable;
            }
            
        }
        
        return reachable;
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
        
        in.minWeight = Maps.newHashMap();
        in.count = Maps.newHashMap();
        
        for(int r = 0; r < in.nRoads; ++r) {
            int node1 = getNode(scanner.next(), strToNode);
            int node2 = getNode(scanner.next(), strToNode);
            
            Pair<Integer,Integer> edge = buildEdge(node1,node2);
            int weight = scanner.nextInt();
            
            Integer minW = in.minWeight.get(edge);
            InputData.Road road = buildRoad(node1,node2,weight);
            Integer count = in.count.get(road);
            
            if (count == null ) {
                count = 0;
            }
            
            ++count;
            
            in.count.put(road, count);    
            
            if (minW == null || minW > weight) {
                in.minWeight.put(edge, weight);
            }
            
            in.roads.add(road);
            in.graph.addOneWayConnection(node1, node2, weight);
        }
        
        in.cityCount = strToNode.size();

        return in;
    }
    
    Pair<Integer,Integer> buildEdge(int fromNode, int toNode) {
        return new ImmutablePair<>(fromNode,toNode);
    }
    InputData.Road buildRoad(int node1, int node2, int weight) {
        InputData.Road r = new InputData.Road(
                buildEdge(node1,node2),
                weight);
        return r;
                
        
    }
    
    int getNode(String str, Map<String, Integer> strToNode) {
        Integer nodeId = strToNode.get(str);
        
        if (nodeId == null) {
            nodeId = strToNode.size();
            strToNode.put(str,nodeId);
            log.debug("City {} = id {}", str, nodeId);
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
        
        //log.debug("Graph {}", g);
        Map<Pair<Integer,Integer>, Double> edgeHits = Maps.newHashMap();
        
        
        for(Road r : in.roads) {
            edgeHits.put(r.edge,0d);
        }
        
        DijkstraNode[] dnodes = doDijkstra(g, 0, in.cityCount, in);
        final int reachableCities = countReachableCities(dnodes,0);
        
        
        for(int targetCity = 1; targetCity < in.cityCount; ++targetCity)
        {
            List<List<Integer>> paths = Lists.newArrayList();
            
            List<Integer> init = Lists.newArrayList();
            init.add(targetCity);
            
            Queue<List<Integer>> toVisit = new LinkedList<>();
            toVisit.add(init);
            
            /**
             * Build all paths
             */
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
                    
                   // Pair<Integer,Integer> edge = buildEdge(prev,last);
                    
                    //int edgeCount = in.count.get(edge);
                    //for(int count = 0; count < edgeCount; ++count) {
                        toVisit.add(newPath);
                    //}
                    
                }
            }
            
            //log.debug("Target city {} paths {}", targetCity, paths.size());
            for(List<Integer> path : paths) {
                //log.debug("Path {}", path);
                for(int n2Idx = 1; n2Idx < path.size(); ++n2Idx) {
                    int n1Idx = n2Idx - 1;
                        int n1 = path.get(n1Idx);
                        int n2 = path.get(n2Idx);
                        
                        // 1 / # of target cities
                        double prob = 1d / (reachableCities);
                        
                        // 1 / # of paths
                        prob *= 1d / paths.size();
                        
                        //log.debug("Adding {} to edge {} {}", prob, n1, n2);
                        prob += edgeHits.get(buildEdge(n2,n1));
                        
                        edgeHits.put(buildEdge(n2,n1), prob);
                        
                       // log.debug("Prob now {}", edgeHits.get(buildEdge(n2,n1)));
                    
                }
            }
            
        }
        
        StringBuffer sb = new StringBuffer();
        sb.append("Case #" + in.testCase + ": ") ;
        
        for(Road r : in.roads) {
            Integer count = in.count.get(r);
           
            Integer min = in.minWeight.get(r.edge);
            Preconditions.checkState(count != null);
            Preconditions.checkState(min != null);
            
            /**
             * We will never use a road if its weight is not minimal
             */
            if (r.weight > min) {
                sb.append("0 ");
                continue;
            }
            sb.append(edgeHits.get(r.edge) / count);
            sb.append(" ");
        }
        
        return sb.toString();
    }

}