package codejam.y2008.round_emea.rainbow_trees;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.GraphInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.common.math.IntMath;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    @Override
    public String[] getDefaultInputFiles() {
      //  return new String[] {"sample.in"};
        return new String[] {"C-small-practice.in"};
       // return new String[] {"B-large-practice.in"};
     //   return new String[] {"A-small-practice.in", "A-large-practice.in"};
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
  
  
    @Override
    public String handleCase(InputData input) {
        
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
