package com.eric.codejam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

public class Graph<GraphNode> {
    final static Logger log = LoggerFactory.getLogger(Graph.class);
    
    private Map<GraphNode, Set<GraphNode>> edges;
    
    public Graph() {
        edges = new HashMap<>();
    }
    public Graph(Graph<GraphNode> g) {
    	edges = new HashMap<>();
    	for(GraphNode gn : g.edges.keySet()) {
    		edges.put(gn, new HashSet<GraphNode>(g.edges.get(gn)));
    	}
    }
    
    public Set<GraphNode> getNodes() {
    	return edges.keySet();
    }
    
    public List<Graph<GraphNode>> getAllConnectedGraphs() {
    	
    	Set<GraphNode> nodesNotInGraphs = new HashSet<>(edges.keySet());
    	
    	List<Graph<GraphNode>> ret = new ArrayList<>();
    	
    	while(!nodesNotInGraphs.isEmpty()) {
    		List<GraphNode> toVisit = new ArrayList<>();
    		Set<GraphNode> visited = new HashSet<>();
    		GraphNode startNode = nodesNotInGraphs.iterator().next();
    		nodesNotInGraphs.remove(startNode);
    		toVisit.add(startNode);
    		
    		while(!toVisit.isEmpty()) {
    			GraphNode gn = toVisit.remove(0);
    			if (visited.contains(gn)) {
    				continue;
    			}
    			visited.add(gn);
    			
    			toVisit.addAll( getConnectedGraphNodes(gn) );
    		}
    		
    		Graph<GraphNode> g = new Graph<>();
    		for(GraphNode gn : visited) {
    			g.edges.put(gn, edges.get(gn));    			
    		}
    		
    		nodesNotInGraphs.removeAll(visited);
    		ret.add(g);
    	}
    	
    	return ret;
    	
    	
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
    
    /**
     * Nodes must have at least degree <
     * @param degree
     */
    public void stripNodesOfDegreeLessThan(int degree) {
    	for(Iterator<Map.Entry<GraphNode, Set<GraphNode>>> it = edges.entrySet().iterator(); it.hasNext();) {
    		Map.Entry<GraphNode, Set<GraphNode>> entry = it.next();
    		
    		if (entry.getValue().size() < degree) {
    			for(GraphNode gn : entry.getValue()) {
    				edges.get(gn).remove(entry.getKey());
    			}
    			log.debug("Removing gn {}", entry.getKey());
    			it.remove();
    		}
    	}
    }
    
    public Set<GraphNode> getConnectedGraphNodes(GraphNode a) {
        return edges.get(a);
    }
    
    public boolean isConnected(GraphNode a, GraphNode b) {
    	if (!edges.containsKey(a)) {
    		return false;
    	}
    	
    	return edges.get(a).contains(b);
    }
    
    public Set<GraphNode> getTriangle(GraphNode gn) {
    	if (!edges.containsKey(gn)) {
    		return null;
    	}
    	
    	Set<GraphNode> cn = getConnectedGraphNodes(gn);
    	
    	for(GraphNode neighbor : cn) {
    		Set<GraphNode> neighConn = getConnectedGraphNodes(neighbor);
    		
    		Set<GraphNode> inter = Sets.intersection(cn, neighConn);
    		
    		if (inter.size() >= 1) {
    			return new HashSet<>(Arrays.asList(gn, neighbor, inter.iterator().next()));
    		}
    	}
    	
    	return null;
    	
    	
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
