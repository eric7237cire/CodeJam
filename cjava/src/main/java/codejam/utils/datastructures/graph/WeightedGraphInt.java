package codejam.utils.datastructures.graph;

import java.util.Collections;
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
        @Override
        public String toString()
        {
            return "Edge [to=" + to + ", weight=" + weight + "]";
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
    
    public void addConnection(int nodeA, int nodeB, int weight) {
        addOneWayConnection(nodeA, nodeB, weight);
        
        addOneWayConnection(nodeB, nodeA, weight);
    }
    

    public Set<Edge> getEdges(int node) {
        return nodeConnections.get(node);
    }
    
    public int V() {
        return nodeConnections.keySet().size();
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append("Graph\n");
        
        int maxNode = Collections.max(nodeConnections.keySet());
        
        for(int node = 0; node <= maxNode; ++node) {
            sb.append("Connections from node ");
            sb.append(node);
            sb.append("\n");
            
            Set<Edge> edges = nodeConnections.get(node);
            
            if (edges == null)
                continue;
            
            for(Edge e : edges) {
                sb.append(e);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}
