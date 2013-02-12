package codejam.utils.datastructures.graph;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * No self loops
 * No duplicate edges
 *
 */
public class GraphInt  {
    
    Map<Integer, Set<Integer>> nodeConnections;
    
    //Number of vertices
    int maxVNum;
    public GraphInt() {
        nodeConnections = Maps.newHashMap();
        maxVNum = 0;
    }
    
    public GraphInt(GraphInt graph) {
        nodeConnections = Maps.newHashMap();
        for(Map.Entry<Integer, Set<Integer>> entry : graph.nodeConnections.entrySet()) {
            nodeConnections.put(entry.getKey(), Sets.newHashSet(entry.getValue()));
        }
    }
    
    public int V() {
        return nodeConnections.keySet().size();
    }
    
    public Set<Integer> getNodes() {return nodeConnections.keySet(); }
    
    public void addNode(int node) {
        maxVNum = Math.max(maxVNum, node);
        
        if (!nodeConnections.containsKey(node)) {
            nodeConnections.put(node, Sets.<Integer>newHashSet());
        }
    }
    
    public int getMaxVNum()
    {
        return maxVNum;
    }

    public void removeConnection(int nodeA, int nodeB) {
        if (nodeA == nodeB)
            return;
        
        Set<Integer> nodeANeighbors;
        Set<Integer> nodeBNeighbors;
        
        if (nodeConnections.containsKey(nodeA)) {            
        
            nodeANeighbors = nodeConnections.get(nodeA);
            nodeANeighbors.remove(nodeB);
        }
        
        if (nodeConnections.containsKey(nodeB)) {
            nodeBNeighbors = nodeConnections.get(nodeB);
            nodeBNeighbors.remove(nodeA);
        }
        
    }
    
    /**
     * Only works properly in undirected graphs
     * @param nodeA
     */
    public void removeNode(int nodeA) {
        
        Set<Integer> nodeANeighbors = nodeConnections.get(nodeA);
            
        for(int adj : nodeANeighbors) {
            nodeConnections.get(adj).remove(nodeA);
        }
        
        nodeConnections.remove(nodeA);
        
    }
    
    /**
     * Used in brute force graph coloring problem
     * @param nodeA
     * @param nodeB
     */
    public void contractEdge(int nodeA, int nodeB) {
        //Will merge B into A
        Set<Integer> nodeANeighbors = nodeConnections.get(nodeA);
        Set<Integer> nodeBNeighbors = nodeConnections.get(nodeB);
        
        Set<Integer> adjNodeAB = nodeANeighbors;
        adjNodeAB.addAll(nodeBNeighbors);
        
        //No self loops
        adjNodeAB.remove(nodeB);
        adjNodeAB.remove(nodeA);
        nodeConnections.remove(nodeB);
        
        nodeConnections.put(nodeA, adjNodeAB);
        
        for(Integer adjNode : adjNodeAB) {
            nodeConnections.get(adjNode).remove(nodeB);
            nodeConnections.get(adjNode).add(nodeA);
        }
    }
    
    public void addConnection(int nodeA, int nodeB) {
        if (nodeA == nodeB)
            return;
        
        addOneWayConnection(nodeA, nodeB);
        addOneWayConnection(nodeB, nodeA);
    }
    
    public void addOneWayConnection(int nodeA, int nodeB) {
        addNode(nodeA);
        addNode(nodeB);
        
        Set<Integer> nodeANeighbors;
        
        if (!nodeConnections.containsKey(nodeA)) {
            nodeANeighbors = Sets.newHashSet();
            nodeConnections.put(nodeA,nodeANeighbors);
        } else {
            nodeANeighbors = nodeConnections.get(nodeA);
        }
        
        nodeANeighbors.add(nodeB);
    }

   
    
    
    public Set<Integer> getNeighbors(int node) {
        return nodeConnections.get(node);
    }
    
    public boolean nodeExists(int node) {
        return nodeConnections.containsKey(node);
    }
    
    public int getDegree(int node) {
        if (!nodeExists(node))
            return 0;
        
        return getNeighbors(node).size();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(nodeConnections);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GraphInt other = (GraphInt) obj;
        
        return Objects.equal(nodeConnections, other.nodeConnections);
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Graph V=").append(V()).append("\n");
        
        for(Integer v : nodeConnections.keySet()) {
            sb.append(" vertex ").append(v);
            sb.append(" adjList -- ");
            sb.append(Joiner.on(", ").join(nodeConnections.get(v)));
            sb.append("\n");
            
        }
        return sb.toString();
    }

   
}
