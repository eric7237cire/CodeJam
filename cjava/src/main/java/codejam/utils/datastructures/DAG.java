package codejam.utils.datastructures;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

public class DAG<Node> {
    public BiMap<Node, Integer> nodes = HashBiMap.create();
    int nextNodeNum = 0;
    
    public Map<Edge, Integer> edges = Maps.newHashMap();
    public Map<Integer,List<Edge>> connections = Maps.newHashMap();
    
    public int getNumNodes() {
        return nextNodeNum;
    }
    
    
    
    public static class Edge {
        public final int from;
        public final int to;
        
        public Edge(int from, int to) {
            super();
            this.from = from;
            this.to = to;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(from,to);
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Edge other = (Edge) obj;
            return from == other.from && to == other.to;
        }
        
    }
    public void addNode(Node node) {
        if (nodes.containsKey(node)) {
            return;
        }
        
        nodes.put(node,nextNodeNum);
        nextNodeNum++;
    }
    
    public boolean nodeExists(Node node) {
        return nodes.containsKey(node); 
    }
    
    public void addEdge(Node from, Node to, int weight) {
        int fromNum = nodes.get(from);
        int toNum = nodes.get(to);
        Edge edge = new Edge(fromNum,toNum);
        edges.put(edge,weight);
        
        if (!connections.containsKey(fromNum)) {
            connections.put(fromNum, new ArrayList<Edge>());
        }
        
        List<Edge> connectionList = connections.get(fromNum);
        
        connectionList.add(edge);
    }
    
    
}
