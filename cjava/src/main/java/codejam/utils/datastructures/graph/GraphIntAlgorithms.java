package codejam.utils.datastructures.graph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import codejam.utils.datastructures.TreeInt;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

public class GraphIntAlgorithms
{

    public GraphIntAlgorithms() {
        // TODO Auto-generated constructor stub
    }

    public static int getMaximumDegree(GraphInt g)
    {
        int maxDegree = 0;
        for(int v = 0; v <= g.getMaxVNum(); ++v) {
            maxDegree = Math.max(maxDegree, g.getDegree(v));
        }
        
        return maxDegree;
    }
    
    public static <T> TreeInt<T>  convertToTree(GraphInt g, int root) {
        TreeInt<T> newTree = new TreeInt<T>(root);
        
        Queue<Integer> toVisit = new LinkedList<>();
        
        toVisit.add(root);
        
        while(!toVisit.isEmpty()) {
            Integer nodeId = toVisit.poll();
            
            Preconditions.checkState(newTree.getNodes().containsKey(nodeId));
            TreeInt<T>.Node newTreeNode = newTree.getNodes().get(nodeId);
            Set<Integer> connections = g.getNeighbors(nodeId);
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
    
    /**
     * Gets connected nodes without using edge from u to v
     * 
     * @param startingNode
     *            where to start
     * @param U
     * @param V
     * @return
     */
    public static Set<Integer> getConnectedNodesWithoutEdge(GraphInt g, int startingNode, int U,
            int V) {

        Set<Integer> visitedNodes = Sets.newHashSet();

        LinkedList<Integer> toVisit = new LinkedList<>();
        toVisit.add(startingNode);

        while (!toVisit.isEmpty()) {

            Integer loc = toVisit.poll();

            if (visitedNodes.contains(loc))
                continue;

            visitedNodes.add(loc);

            Set<Integer> adjNodes = g.getNeighbors(loc);

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
    
    public static Set<Integer> getConnectedNodesWithoutNode(GraphInt g, int startingNode, 
            int V) {

        Set<Integer> visitedNodes = Sets.newHashSet();

        LinkedList<Integer> toVisit = new LinkedList<>();
        toVisit.add(startingNode);

        while (!toVisit.isEmpty()) {

            Integer loc = toVisit.poll();

            if (visitedNodes.contains(loc))
                continue;

            visitedNodes.add(loc);

            Set<Integer> adjNodes = g.getNeighbors(loc);

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
    
}
