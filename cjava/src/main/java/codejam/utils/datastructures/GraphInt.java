package codejam.utils.datastructures;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import codejam.utils.utils.Direction;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class GraphInt {
    
    Map<Integer, Set<Integer>> nodeConnections;
    
    //Number of vertices
    int V;
    public GraphInt() {
        nodeConnections = Maps.newHashMap();
        V = 0;
    }
    
    public int V() {
        return nodeConnections.keySet().size();
    }
    
    public Set<Integer> getNodes() {return nodeConnections.keySet(); }
    
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
    
    public void addOneWayConnection(int nodeA, int nodeB) {
        Set<Integer> nodeANeighbors;
        
        if (!nodeConnections.containsKey(nodeA)) {
            nodeANeighbors = Sets.newHashSet();
            nodeConnections.put(nodeA,nodeANeighbors);
        } else {
            nodeANeighbors = nodeConnections.get(nodeA);
        }
        
        nodeANeighbors.add(nodeB);
    }

    /**
     * Gets connected nodes without using edge from u to v
     * 
     * @param startingNode
     *            where to start
     * @param U
     * @param V
     * @return
     */
    public Set<Integer> getConnectedNodesWithoutEdge(int startingNode, int U,
            int V) {

        Set<Integer> visitedNodes = Sets.newHashSet();

        LinkedList<Integer> toVisit = new LinkedList<>();
        toVisit.add(startingNode);

        while (!toVisit.isEmpty()) {

            Integer loc = toVisit.poll();

            if (visitedNodes.contains(loc))
                continue;

            visitedNodes.add(loc);

            Set<Integer> adjNodes = getNeighbors(loc);

            if (loc == U) {
                adjNodes = Sets.newHashSet(adjNodes);
                adjNodes.remove(V);
            } else if (loc == V) {
                adjNodes = Sets.newHashSet(adjNodes);
                adjNodes.remove(U);
            }

            for (Integer child : adjNodes) {
                toVisit.add(child);
            }
        }

        return visitedNodes;

    }
    
    public Set<Integer> getConnectedNodesWithoutNode(int startingNode, 
            int V) {

        Set<Integer> visitedNodes = Sets.newHashSet();

        LinkedList<Integer> toVisit = new LinkedList<>();
        toVisit.add(startingNode);

        while (!toVisit.isEmpty()) {

            Integer loc = toVisit.poll();

            if (visitedNodes.contains(loc))
                continue;

            visitedNodes.add(loc);

            Set<Integer> adjNodes = getNeighbors(loc);

            if (loc == V) {
                continue;
            }

            for (Integer child : adjNodes) {
                if (child == V)
                    continue;
                toVisit.add(child);
            }
        }

        return visitedNodes;

    }
    
    
    public Set<Integer> getNeighbors(int node) {
        return nodeConnections.get(node);
    }
    
    public <T> TreeInt<T>  convertToTree(int root) {
        TreeInt<T> newTree = new TreeInt<T>(root);
        
        Queue<Integer> toVisit = new LinkedList<>();
        
        toVisit.add(root);
        
        while(!toVisit.isEmpty()) {
            Integer nodeId = toVisit.poll();
            
            Preconditions.checkState(newTree.getNodes().containsKey(nodeId));
            TreeInt<T>.Node newTreeNode = newTree.getNodes().get(nodeId);
            Set<Integer> connections = getNeighbors(nodeId);
            //Get all new children from old tree
            for(Integer childNode : connections) {
                //Add children to new tree node
                if (newTreeNode.getParent() == null || childNode != newTreeNode.getParent().getId()) {
                    newTreeNode.addChild(childNode);
                    toVisit.add(childNode);
                }
            }
            
            
        }
        
        return newTree;
    }
}
