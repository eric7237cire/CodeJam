package codejam.utils.datastructures;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class GraphInt {
    
    Map<Integer, Set<Integer>> nodeConnections;
    
    public GraphInt() {
        nodeConnections = Maps.newHashMap();
    }
    public void addConnection(int nodeA, int nodeB) {
        Set<Integer> nodeANeighbors;
        Set<Integer> nodeBNeighbors;
        
        if (!nodeConnections.containsKey(nodeA)) {
            nodeANeighbors = Sets.newHashSet();
            nodeConnections.put(nodeA,nodeANeighbors);
        } else {
            nodeANeighbors = nodeConnections.get(nodeA);
        }
        
        if (!nodeConnections.containsKey(nodeB)) {
            nodeBNeighbors = Sets.newHashSet();
            nodeConnections.put(nodeB,nodeBNeighbors);
        } else {
            nodeBNeighbors = nodeConnections.get(nodeB);
        }
        
        nodeANeighbors.add(nodeB);
        nodeBNeighbors.add(nodeA);
    }
    
    public Set<Integer> getNeighbors(int node) {
        return nodeConnections.get(node);
    }
}
