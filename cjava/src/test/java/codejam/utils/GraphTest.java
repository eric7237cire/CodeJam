package codejam.utils;


import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import codejam.utils.datastructures.GraphAdjList;

public class GraphTest {
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
}
