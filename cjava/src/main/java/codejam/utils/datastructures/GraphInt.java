package codejam.utils.datastructures;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import codejam.utils.datastructures.TreeInt.Node;

import com.google.common.base.Preconditions;
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
    
    public TreeInt convertToTree(int root) {
        TreeInt newTree = new TreeInt(root);
        
        Queue<Integer> toVisit = new LinkedList<>();
        
        toVisit.add(root);
        
        while(!toVisit.isEmpty()) {
            Integer nodeId = toVisit.poll();
            
            Preconditions.checkState(newTree.getNodes().containsKey(nodeId));
            Node newTreeNode = newTree.getNodes().get(nodeId);
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
