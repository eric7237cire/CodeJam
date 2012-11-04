package com.eric.codejam;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Graph<GraphNode> {
    final static Logger log = LoggerFactory.getLogger(Graph.class);
    
    Map<GraphNode, Set<GraphNode>> edges;
    
    Graph() {
        edges = new HashMap<>();
    }
    void addEdge(GraphNode nodeA, GraphNode nodeB) {
        if (!edges.containsKey(nodeA)) {
            edges.put(nodeA, new HashSet<GraphNode>());
        }
        if (!edges.containsKey(nodeB)) {
            edges.put(nodeB, new HashSet<GraphNode>());
        }
        
        edges.get(nodeA).add(nodeB);
        edges.get(nodeB).add(nodeA);
    }
    
    Set<GraphNode> getConnectedGraphNodes(GraphNode a) {
        return edges.get(a);
    }
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(GraphNode gn : edges.keySet()) {
            Set<GraphNode> set = edges.get(gn);
            for(GraphNode r : set) {
                //log.debug("Edge {} to {}", gn, r);
                sb.append("Edge " + gn.toString() + " to " + r.toString());
            }
        }
        
        return sb.toString();
    }
}
