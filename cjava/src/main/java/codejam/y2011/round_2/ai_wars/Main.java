package codejam.y2011.round_2.ai_wars;

import java.util.BitSet;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.graph.GraphInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        // return new String[] {"sample.in"};
        // return new String[] { "D-large-practice.in" };
        return new String[] { "D-small-practice.in", "D-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {



        /*
         * InputData.java
     Main.javaThe first line of the input gives the number of test cases, T. 
     T test cases follow. Each test case starts with a single line
      containing two space-separated integers: P, the number of planets,
       and W, the number of wormholes. Your home planet is planet 0, and 
       the A.I.'s home planet is planet 1.

     The second line of each test case will contain W space-separated 
     pairs of comma-separated integers xi,yi. Each of these indicates 
     that there is a two-way wormhole connecting planets xi and yi.
         */
        
        Pattern delim = scanner.delimiter();
        
        InputData in = new InputData(testCase);
       
        scanner.useDelimiter("\\p{javaWhitespace}+|,");
        in.P = scanner.nextInt();        
        in.W = scanner.nextInt();

        in.wormHoles = Lists.newArrayList();
        
       
        for(int i = 0; i < in.W; ++i) {
           
            int p1 = scanner.nextInt();
            int p2 = scanner.nextInt();
            
            in.wormHoles.add(new ImmutablePair<>(p1,p2));
        }
        
        scanner.useDelimiter(delim);

        return in;
    }

    static class Node {
        final BitSet planetsThreatened;
        final BitSet path;
        final int planetId;
        final int numPlanetsThreatened;
        final int pathLength;
        
//        private Node() {
//            planetsThreatened = new BitSet();
//            path = new BitSet();
//        }
        
        private static void addPlanetsThreatened(BitSet planetsThreatened, Set<Integer> neighbors) {
            for(int n : neighbors) {
                planetsThreatened.set(n);
            }
            
        }

        private Node(BitSet planetsThreatened, BitSet path, int planetId,
                int numPlanetsThreatened, int pathLength) {
            super();
            this.planetsThreatened = planetsThreatened;
            this.path = path;
            this.planetId = planetId;
            this.numPlanetsThreatened = numPlanetsThreatened;
            this.pathLength = pathLength;
        }

        static Node createRootNode(Set<Integer> neighbors, int planetId) {
            BitSet planetsThreatened = new BitSet();
            BitSet path = new BitSet();
            int numPlanetsThreatened = 0;
            int pathLength = 0;
            

            addPlanetsThreatened(planetsThreatened, neighbors);
            numPlanetsThreatened = neighbors.size();
            path.set(0);
            
            return new Node(planetsThreatened,path,planetId,numPlanetsThreatened, pathLength);
            
            
        }
        
        static Node createNode(Node parentNode, Set<Integer> neighbors, BitSet neighborhoodsFurtherDownPath, int planetId) {
            
            BitSet planetsThreatened = BitSet.valueOf(parentNode.planetsThreatened.toLongArray());
            BitSet path = BitSet.valueOf(parentNode.path.toLongArray());
            
            path.set(planetId);
            
            int numPlanetsThreatened = parentNode.numPlanetsThreatened;
            int pathLength = parentNode.pathLength + 1;
                        
            for(int n : neighbors) {
                planetsThreatened.set(n);
            }
            
            planetsThreatened.andNot(path);
            
            numPlanetsThreatened += planetsThreatened.cardinality() - parentNode.planetsThreatened.cardinality();
            
            //The key time saver.  We do only need to store planets that are reachable 
            planetsThreatened.and(neighborhoodsFurtherDownPath);
            
            return new Node(planetsThreatened,path,planetId,numPlanetsThreatened, pathLength);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(planetId, planetsThreatened);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;            
            if (planetId != other.planetId)
                return false;
            
            return planetsThreatened.equals(other.planetsThreatened) ;
            
            
        }
    }
    
    static class DijkstraNode {
        int nodeId;
        int distance;
        Integer previous;
        public DijkstraNode(int nodeId, int distance) {
            super();
            this.nodeId = nodeId;
            this.distance = distance;
        }
    }
    
    /**
     * Indexes in graph go from 0 to nodeCount - 1
     */
    public DijkstraNode[] doDijkstra(GraphInt graph, int sourceNodeId, int nodeCount) {
        
        
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
            
            Set<Integer> neighbors = graph.getNeighbors(nodeU.nodeId);
            
            for(int neighbor : neighbors) {
                int alt = 1 + nodeU.distance;
                DijkstraNode nodeV = dijNodes[neighbor];
                if (alt < nodeV.distance) {
                    toProcess.remove(nodeV);
                    nodeV.distance = alt;
                    nodeV.previous = nodeU.nodeId;
                    toProcess.add(nodeV);
                    
                }
            }
        }
        
        return dijNodes;
                             
    }
    
    public String handleCase(InputData in) {

        GraphInt graph = new GraphInt();
        
        for(int i = 0; i < in.W; ++i) {
            Pair<Integer,Integer> edge = in.wormHoles.get(i);
            graph.addConnection(edge.getLeft(), edge.getRight());
        }
        
        //Calculate distances from target planet
        DijkstraNode[] dijNodes = doDijkstra(graph, 1, in.P);
        
                
        int maxDistance = 0;
        for(DijkstraNode node : dijNodes) {
            if(node.distance == Integer.MAX_VALUE)
                continue;
            
            maxDistance = Math.max(maxDistance, node.distance);
        }
        
        //Calculate neighbor sets with a distance exactly k and 0 to k
        BitSet[] neighborhoodsAtDistanceK = new BitSet[maxDistance+1];
        BitSet[] neighborhoodsAtDistanceUptoK = new BitSet[maxDistance+1];
        
        for(int dis = 0; dis <= maxDistance; ++dis) {
            neighborhoodsAtDistanceK[dis] = new BitSet();
            neighborhoodsAtDistanceUptoK[dis] = new BitSet();
        }
        
        for(DijkstraNode node : dijNodes) {
            if(node.distance == Integer.MAX_VALUE)
                continue;
            
            Set<Integer> n = graph.getNeighbors(node.nodeId);
            
            for(int neighbor : n) {
                neighborhoodsAtDistanceK[node.distance].set(neighbor);
            }            
        }
        
        neighborhoodsAtDistanceUptoK[0].or(neighborhoodsAtDistanceK[0]);
        
        for(int dis = 1; dis <= maxDistance; ++dis) {
            neighborhoodsAtDistanceUptoK[dis].or(neighborhoodsAtDistanceUptoK[dis-1]);
            neighborhoodsAtDistanceUptoK[dis].or(neighborhoodsAtDistanceK[dis]);
        }
        
        //Do another BFS
        Set<Node> visited = Sets.newHashSet();
        
        PriorityQueue<Node> toVisit = new PriorityQueue<>(in.P, new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                /*
                 * Prioritize short path length but 
                 * maximum planets threatened
                 */
                return ComparisonChain.start().
                        compare(o1.pathLength, o2.pathLength)
                        .compare(o2.numPlanetsThreatened, o1.numPlanetsThreatened)
                        .result();
            }            
        });
        
        Node root = Node.createRootNode(graph.getNeighbors(0), 0);
        
        toVisit.add(root);
        
        while(!toVisit.isEmpty()) {
            Node node = toVisit.poll();
            
            DijkstraNode curDijNode = dijNodes[node.planetId];
            
            if (visited.contains(node)) {
                continue;
            }
            
            //Sanity check
            if (toVisit.size() > 1000000) {
                return "Too large" + toVisit.size();
            }
            
            //Visited equals specially tuned to only check location and planets threatened
            //due to the queue, we will visit the one with the most planets threatened
            visited.add(node);
            
            Set<Integer> neighbors = graph.getNeighbors(node.planetId);
            
            //We threaten their home world --> we are done
            if (neighbors.contains(1)) {
                return String.format("Case #%d: %d %d", in.testCase, node.pathLength, node.numPlanetsThreatened);
            }
            
            for(int adjNodeId : neighbors) {
                
                DijkstraNode adjDijNode = dijNodes[adjNodeId];
                
                //Time saver, only consider nodes that can be on the shortest path
                if (adjDijNode.distance != curDijNode.distance - 1)
                    continue;
                
                //Nodes further down path means 1 less distance to target world
                
                Node adjNode = Node.createNode(node, graph.getNeighbors(adjNodeId),
                        neighborhoodsAtDistanceUptoK[adjDijNode.distance-1],
                        adjNodeId);
                toVisit.add(adjNode);
            
            }
            
        }
        
        return String.format("Case #%d: IMPOSSIBLE", in.testCase);
        
    }

}
