package codejam.utils.datastructures.graph;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class WeightedGraphInt
{
    
    public static class Edge {
        public final int to;
        public final int weight;
        public Edge(int to, int weight) {
            super();
            this.to = to;
            this.weight = weight;
        }
        
    }

    public WeightedGraphInt() {
        nodeConnections = Maps.newHashMap();
        
    }
    
    Map<Integer, Set<Edge>> nodeConnections;
    
    
    public void addOneWayConnection(int nodeA, int nodeB, int weight) {
        Set<Edge> nodeANeighbors;
        
        if (!nodeConnections.containsKey(nodeA)) {
            nodeANeighbors = Sets.newHashSet();
            nodeConnections.put(nodeA,nodeANeighbors);
        } else {
            nodeANeighbors = nodeConnections.get(nodeA);
        }
        
        nodeANeighbors.add(new Edge(nodeB, weight));
    }
    

    public Set<Edge> getEdges(int node) {
        return nodeConnections.get(node);
    }
    
    public int V() {
        return nodeConnections.keySet().size();
    }

}
