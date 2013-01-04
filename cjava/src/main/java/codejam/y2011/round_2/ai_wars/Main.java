package codejam.y2011.round_2.ai_wars;

import java.util.Arrays;
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

import codejam.utils.datastructures.GraphInt;
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
         return new String[] {"sample.in"};
        //return new String[] { "B-small-practice.in", "B-large-practice.in" };
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
        //scanner.useDelimiter(Pattern.compile(",|\n|\r\n"));
        scanner.useDelimiter("\\p{javaWhitespace}+|,");
        //\p{javaWhitespace}+
        //\p{javaWhitespace}+
        in.P = scanner.nextInt();        
        in.W = scanner.nextInt();

        in.wormHoles = Lists.newArrayList();
        
        
        
        //String l = scanner.nextLine();
       // scanner.useDelimiter("\\s+,?");
       
        for(int i = 0; i < in.W; ++i) {
            //String s = scanner.findInLine("\\d+,\\d+");
            int p1 = scanner.nextInt();
            int p2 = scanner.nextInt();
            
            in.wormHoles.add(new ImmutablePair<>(p1,p2));
        }
        
        scanner.useDelimiter(delim);

        return in;
    }

    static class Node {
        BitSet planetsThreatened;
        BitSet path;
        int planetId;
        int numPlanetsThreatened;
        int pathLength;
        
        Node() {
            planetsThreatened = new BitSet();
            path = new BitSet();
        }
        void addPlanetsThreatened(Set<Integer> neighbors) {
            for(int n : neighbors) {
                planetsThreatened.set(n);
            }
            
        }
        
        static Node createNode(Node parentNode, Set<Integer> neighbors, int planetId) {
            Node node = new Node();
            node.planetsThreatened = BitSet.valueOf(parentNode.planetsThreatened.toLongArray());
            node.path = BitSet.valueOf(parentNode.path.toLongArray());
            
            node.path.set(planetId);
            node.addPlanetsThreatened(neighbors);
            
            node.planetsThreatened.andNot(node.path);
            
            node.numPlanetsThreatened = node.planetsThreatened.cardinality();
            
            node.planetId = planetId;
            node.pathLength = parentNode.pathLength + 1;
            return node;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(planetId, planetsThreatened, path);
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
            
            return planetsThreatened.equals(other.planetsThreatened) &&
                    path.equals(other.path);
            
            
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
        
        DijkstraNode[] dijNodes = doDijkstra(graph, 1, in.P);
        
        
        Set<Node> visited = Sets.newHashSet();
        
        PriorityQueue<Node> toVisit = new PriorityQueue<>(in.P, new Comparator<Node>() {

            @Override
            public int compare(Node o1, Node o2) {
                return ComparisonChain.start().
                        compare(o1.pathLength, o2.pathLength)
                        .compare(o2.numPlanetsThreatened, o1.numPlanetsThreatened)
                        .result();
            }            
        });
        
        Node root = new Node();
        root.planetId = 0;
        root.addPlanetsThreatened(graph.getNeighbors(0));
        root.numPlanetsThreatened = graph.getNeighbors(0).size();
        root.path.set(0);
        
        toVisit.add(root);
        
        while(!toVisit.isEmpty()) {
            Node node = toVisit.poll();
            
            DijkstraNode curDijNode = dijNodes[node.planetId];
            
            if (visited.contains(node)) {
                continue;
            }
            
            visited.add(node);
            
            Set<Integer> neighbors = graph.getNeighbors(node.planetId);
            
            if (neighbors.contains(1)) {
                return String.format("Case #%d: %d %d", in.testCase, node.pathLength, node.numPlanetsThreatened);
            }
            
            for(int adjNodeId : neighbors) {
                
                DijkstraNode adjDijNode = dijNodes[adjNodeId];
                
                if (adjDijNode.distance != curDijNode.distance - 1)
                    continue;
                
                //if (!node.path.get(adjNodeId)) {
                    Node adjNode = Node.createNode(node, graph.getNeighbors(adjNodeId), adjNodeId);
                    toVisit.add(adjNode);
                //}
            }
            
        }
        
        return String.format("Case #%d: IMPOSSIBLE", in.testCase);
        
    }

}
