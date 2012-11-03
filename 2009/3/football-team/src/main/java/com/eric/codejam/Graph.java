package com.eric.codejam;

import com.google.common.collect.BiMap;

public class Graph<Node> {
    BiMap<Node, Node> edges;
    
    void addEdge(Node nodeA, Node nodeB) {
        edges.put(nodeA, nodeB);
    }
    
    
}
