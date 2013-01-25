package codejam.y2008.round_emea.rainbow_trees;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.TreeInt;
import codejam.utils.datastructures.graph.GraphInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    @Override
    public String[] getDefaultInputFiles() {
      // return new String[] {"sample.in"};
        return new String[] {"C-small-practice.in", "C-large-practice.in"};
      
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData i = new InputData(testCase);
        i.n = scanner.nextInt();
        i.k = scanner.nextInt();
        i.edges = new ArrayList<>(i.n-1);
        
        for(int ii = 0; ii < i.n - 1; ++ii) {
            i.edges.add(new ImmutablePair<>(scanner.nextInt(),
                    scanner.nextInt()));
        }
        return i;
    }
   
    public Pair<Integer, Integer> createEdge(int n1, int n2) {
        return new ImmutablePair<>(Math.min(n1,n2), Math.max(n1,n2));
    }
  
    public long perm(int n, int k) {
        Preconditions.checkArgument(k >= 1);
        long r = 1;
        for(int i = 0; i < k; ++i) {
            r *= (n-i);
            r %= MOD;
        }
        return r;
    }
    
    static class NodeData {
        BitSet vertices;
        long chromNum;
    }
    
    private static final int MOD = 1000000009;
    private static final BigInteger MOD_BI = BigInteger.valueOf(MOD);
  
    //GivenSolution
    public String handleCase(InputData input) {
        
        //First create the graph of vertices
        GraphInt vertexGraph = new GraphInt();
        
        for(Pair<Integer, Integer> edge : input.edges ) {                       
            vertexGraph.addConnection(edge.getLeft()-1, edge.getRight()-1);
        }
               
        int[] parentNode = new int[input.n];
        Arrays.fill(parentNode, -1);
        
        Queue<Integer> toVisit = new LinkedList<>();
        LinkedList<Integer> vertexOrdering = new LinkedList<>();
        
        parentNode[0] = input.n;
        
        toVisit.add(0);
        
        
        //BFS
        while(!toVisit.isEmpty()) {
            Integer nodeId = toVisit.poll();

            vertexOrdering.add(nodeId);
            Preconditions.checkState(parentNode[nodeId] >= 0);
            
            Set<Integer> connections = vertexGraph.getNeighbors(nodeId);
            //Get all new children from old tree
            for(Integer childNode : connections) {
                if (parentNode[childNode] == -1) {
                    toVisit.add(childNode);
                    parentNode[childNode] = nodeId;
                }
            }
        }
        
        //Reverse the BFS to make it children first
        Collections.reverse(vertexOrdering);
        
        long[] waysToColor = new long[input.n];
        Arrays.fill(waysToColor, -1);
        
        for(Integer nodeId : vertexOrdering) {
            Set<Integer> connections = vertexGraph.getNeighbors(nodeId);
            
            Integer parentNodeId = parentNode[nodeId];
            
            //The root has parentNodeId == n ; the degree of its
            //parent is just defined to be 0
            Integer degreeParent = parentNodeId == input.n ?
                    0 : vertexGraph.getNeighbors(parentNodeId).size();
            
            long prod = 1;
            
            //Ways to color immediate children
            int index = 0;
            for(Integer childNode : connections) {
                if (childNode.equals(parentNodeId))
                    continue;
                
                prod *= Math.max(0, input.k - degreeParent - index);
                prod %= MOD;
                ++index;
                
                Preconditions.checkState(waysToColor[childNode] >= 0);
                        
                //And ways to color subtree
                prod = LongMath.checkedMultiply(prod,waysToColor[childNode]) ;
                prod %= MOD;
                
            }
            
            Preconditions.checkState(prod >= 0);
            waysToColor[nodeId] = prod;
        }
        
        
        
        long ans = waysToColor[0];
        
        
        
        return String.format("Case #%d: %d", input.testCase, ans);
    
       
    }
    
    public String handleCaseMySolution(InputData input) {
        
        
        //First create the graph of vertices
        GraphInt vertexGraph = new GraphInt();
        List<Pair<Integer,Integer>>  edges = new ArrayList<>();
        BiMap<Pair<Integer,Integer>, Integer> edgeIntMap = HashBiMap.create();
        
        for(Pair<Integer, Integer> edge : input.edges ) {
            edges.add(createEdge(edge.getLeft(), edge.getRight()));
            edgeIntMap.put(edges.get(edges.size()-1), edges.size()-1);
            
            vertexGraph.addConnection(edge.getLeft(), edge.getRight());
        }
               
        TreeInt<NodeData> tree = vertexGraph.convertToTree(1);
        
        Stack<TreeInt<NodeData>.Node> toVisit = new Stack<>();
        Set<TreeInt<NodeData>.Node> visited = new HashSet<>();
        
        toVisit.add(tree.getRoot());
        tree.getRoot().setData(null);
        
        while(!toVisit.isEmpty()) {
            TreeInt<NodeData>.Node node = toVisit.peek();
            
            //If node has height <= 2, then we can process it directly
            //Add all edges connected to the edge connecting the 
            //subtree rooted at node to the tree
            if (node.getHeight() <= 2) {
                BitSet vertexSet = BitSet.valueOf(node.getChildrenBits().toLongArray());
                vertexSet.set(node.getId());
                if (node.getParent() != null) {
                    vertexSet.or(node.getParent().getChildrenBits());
                    vertexSet.set(node.getParent().getId());
                    if (node.getParent().getParent() != null) {
                        vertexSet.set(node.getParent().getParent().getId());
                    }
                }
            
                NodeData data = new NodeData();
                data.chromNum = perm(input.k, vertexSet.cardinality() - 1);
                data.vertices = vertexSet;
                
                node.setData(data);
                visited.add(node);
                toVisit.pop();
                continue;
            }
            
            //Have children been visited yet?
            Iterator<TreeInt<NodeData>.Node> childIt = node.getChildren().iterator(); 
            TreeInt<NodeData>.Node child = childIt.next(); 
            if (!visited.contains(child)) {
                //Add all children to stack
                toVisit.add(child);
                while(childIt.hasNext()) {
                    child = childIt.next();
                    toVisit.add(child);
                }
                continue;
            }
            
            //All children have been visited
                        
            BitSet vertexSet = BitSet.valueOf(node.getChildrenBits().toLongArray());
            
            vertexSet.set(node.getId());
            
            /**
             * include edges from parent to its children as well as
             * the edge from parent to grand - parent.
             */
            if (node.getParent() != null) {
                vertexSet.or(node.getParent().getChildrenBits());
                vertexSet.set(node.getParent().getId());
                
                if (node.getParent().getParent() != null) {
                    vertexSet.set(node.getParent().getParent().getId());
                }
            }
            
            /**
             * Combine this sub graph with children.  Combine all the vertices
             * and the chromatic number.  Divide by the intersection between
             * this node and the children.
             */
            long cn = perm(input.k, vertexSet.cardinality() - 1);
            
            childIt = node.getChildren().iterator();
            while(childIt.hasNext()) {
                child = childIt.next();
                
                //It was already included by the vertices set
                if (child.getHeight() <= 1)
                    continue;
                
                BitSet intersection = BitSet.valueOf(vertexSet.toLongArray());
                
                intersection.and(child.getData().vertices);
                long interSecCn = perm(input.k, intersection.cardinality()-1);
                cn *= child.getData().chromNum;
                cn %= MOD;
                if (interSecCn == 0) {
                    Preconditions.checkState(cn == 0);
                } else {
                    cn = BigInteger.valueOf(cn).
                            multiply(
                                    BigInteger.valueOf(interSecCn).modInverse(MOD_BI))
                                    .mod(MOD_BI).longValue();
                    
                }
                
                
                vertexSet.or(child.getData().vertices);
            }
            
            NodeData data = new NodeData();
            data.chromNum = cn;
            data.vertices = vertexSet;
            node.setData(data);
            
            toVisit.pop();
            visited.add(node);
        }
        
        long ans = tree.getRoot().getData().chromNum;
        
        
        
        return String.format("Case #%d: %d", input.testCase, ans);
    
       
    }
    
    public String handleCaseBruteForce(InputData input) {
        //First create the graph of vertices
        GraphInt vertexGraph = new GraphInt();
        List<Pair<Integer,Integer>>  edges = new ArrayList<>();
        BiMap<Pair<Integer,Integer>, Integer> edgeIntMap = HashBiMap.create();
        
        for(Pair<Integer, Integer> edge : input.edges ) {
            edges.add(createEdge(edge.getLeft(), edge.getRight()));
            edgeIntMap.put(edges.get(edges.size()-1), edges.size()-1);
            
            vertexGraph.addConnection(edge.getLeft(), edge.getRight());
        }
        
        
        //Create edge graph
        GraphInt edgeGraph = new GraphInt();
        
        for(Pair<Integer,Integer> edge : edges) {
            Set<Integer> leftNeighbors = vertexGraph.getNeighbors(edge.getLeft());
            Set<Integer> rightNeighbors = vertexGraph.getNeighbors(edge.getRight());
            int edgeNum = edgeIntMap.get(edge);
            
            edgeGraph.addNode(edgeNum);
            
            for(Integer neigh : leftNeighbors) {                
                Pair<Integer,Integer> adjEdge = createEdge(neigh, edge.getLeft());                
                int adjEdgeNum = edgeIntMap.get(adjEdge);                
                edgeGraph.addConnection(edgeNum,adjEdgeNum);
            }
            
            for(Integer neigh : rightNeighbors) {
                Pair<Integer,Integer> adjEdge = createEdge(neigh, edge.getRight());                
                int adjEdgeNum = edgeIntMap.get(adjEdge);                
                edgeGraph.addConnection(edgeNum,adjEdgeNum);
            }
        }
        
      //Same as edgeGraph with edges added between neighbors of neighbors
        GraphInt rainbowGraph = new GraphInt();
        for(int edgeNum = 0; edgeNum < input.n - 1; ++edgeNum) {
            Set<Integer> adjEdges = edgeGraph.getNeighbors(edgeNum);
            
            rainbowGraph.addNode(edgeNum);
            
            for(Integer adjEdge : adjEdges) {
                rainbowGraph.addConnection(adjEdge,edgeNum);
                
                Set<Integer> adjRainEdges = edgeGraph.getNeighbors(adjEdge);
                for(Integer rainEdge : adjRainEdges) {
                    rainbowGraph.addConnection(rainEdge,edgeNum);
                }
            }
        }
        
        
        Map<GraphInt, Integer> memoize = Maps.newHashMap();
        
         int r = getChromaticPolynomial(rainbowGraph, input.k, memoize);
        return String.format("Case #%d: %d", input.testCase, r);
    }
    
    int getChromaticPolynomial(GraphInt graph, int k, Map<GraphInt, Integer> memoize) {
        
        Integer ret = memoize.get(graph);
        
        if (ret != null)
            return ret;
        
        //Get an edge
        Integer v = null;
        Integer u = null;
        Iterator<Integer> it = graph.getNodes().iterator();
        while(it.hasNext() ) {
            u = it.next();
            Set<Integer> adjNodes = graph.getNeighbors(u);
            
            if (!adjNodes.isEmpty()) {
                v = adjNodes.iterator().next();
                break;
            }
        }
        
        //If there is no edge, then we can return k ^ |v|
        if (v==null) {
            if (graph.getNodes().size() == 0)
                return 0;
            
            return IntMath.pow(k, graph.getNodes().size());
        }
        
        GraphInt withoutEdgeUV = new GraphInt(graph);
        withoutEdgeUV.removeConnection(u,v);
        int r = getChromaticPolynomial(withoutEdgeUV, k, memoize);
        
        
        //Now contract edge uv
        GraphInt withContraction = new GraphInt(graph);
        withContraction.contractEdge(u,v);
        Preconditions.checkState(withContraction.getNodes().size() + 1== graph.getNodes().size());
        r-=getChromaticPolynomial(withContraction, k, memoize);
        
        memoize.put(graph, r);
        return r;
    }

}
