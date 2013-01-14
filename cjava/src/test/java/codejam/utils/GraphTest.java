package codejam.utils;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.Edge;
import codejam.utils.datastructures.EdgeWeightedGraph;
import codejam.utils.datastructures.GraphAdjList;
import codejam.utils.datastructures.PrimMST;
import codejam.y2008.KingTest;

public class GraphTest {
    
    final static Logger log = LoggerFactory.getLogger(GraphTest.class);
    
    @Test
    public void testMatching() {
        GraphAdjList graph = new GraphAdjList(10);
        graph.addConnection(1,5);
        graph.addConnection(1,6);
        graph.addConnection(1,7);
        graph.addConnection(1,8);
        
        graph.addConnection(2,8);
        graph.addConnection(3,8);
        graph.addConnection(4,8);
        graph.addConnection(4,5);
        
        List<Integer> lhs = Arrays.asList(1,2,3,4);
        List<Integer> rhs = Arrays.asList(5,6,7,8);
        
       // List<Pair<Integer,Integer>> matching = graph.getMaxMatching(lhs,rhs);
        
       // assertEquals(3, matching.size());
        
        graph = new GraphAdjList(10);
        
        graph.addConnection(1,5);
        graph.addConnection(1,8);
        graph.addConnection(2,6);
        graph.addConnection(2,7);
        graph.addConnection(2,8);
        graph.addConnection(3,6);
        graph.addConnection(3,7);
        graph.addConnection(4,5);
        
        List<Pair<Integer,Integer>> matching2 = graph.getMaxMatching(lhs,rhs);
        
        assertEquals(4, matching2.size());
    }
    
    @Test
    public void testMST() {
        EdgeWeightedGraph graph = new EdgeWeightedGraph(9);
        
        graph.addEdge(new Edge('a'-'a', 'b'-'a', 4));
        graph.addEdge(new Edge('a'-'a', 'h'-'a', 8));
        graph.addEdge(new Edge('b'-'a', 'h'-'a', 11));
        
        graph.addEdge(new Edge('b'-'a', 'c'-'a', 8));
        graph.addEdge(new Edge('i'-'a', 'c'-'a', 2));
        graph.addEdge(new Edge('i'-'a', 'h'-'a', 7));
        
        graph.addEdge(new Edge('f'-'a', 'c'-'a', 4));
        graph.addEdge(new Edge('f'-'a', 'd'-'a', 14));        
        graph.addEdge(new Edge('g'-'a', 'f'-'a', 2));
        
        graph.addEdge(new Edge('d'-'a', 'e'-'a', 9));
        graph.addEdge(new Edge('f'-'a', 'e'-'a', 10));
        graph.addEdge(new Edge('h'-'a', 'g'-'a', 1));
        
        graph.addEdge(new Edge('c'-'a', 'd'-'a', 7));
        graph.addEdge(new Edge('g'-'a', 'i'-'a', 6));
        
        PrimMST prim = new PrimMST(graph);
        
        for(Edge e :  prim.edges()) {
            log.debug("EDge {}", e);
        }
        
        log.debug("Weight {}", prim.weight());
                
    }
}
