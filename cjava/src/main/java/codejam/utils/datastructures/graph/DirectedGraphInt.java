package codejam.utils.datastructures.graph;

import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class DirectedGraphInt
{

   
        
        Map<Integer, Set<Integer>> nodeOutEdges;
        Map<Integer, Set<Integer>> nodeInEdges;
        
        //Number of vertices
        int maxVNum;
        public DirectedGraphInt() {
            nodeOutEdges = Maps.newHashMap();
            nodeInEdges = Maps.newHashMap();
            maxVNum = 0;
        }
        
        public DirectedGraphInt(DirectedGraphInt graph) {
            nodeOutEdges = Maps.newHashMap();
            nodeInEdges = Maps.newHashMap();
            for(Map.Entry<Integer, Set<Integer>> entry : graph.nodeOutEdges.entrySet()) {
                nodeOutEdges.put(entry.getKey(), Sets.newHashSet(entry.getValue()));
            }
            for(Map.Entry<Integer, Set<Integer>> entry : graph.nodeInEdges.entrySet()) {
                nodeInEdges.put(entry.getKey(), Sets.newHashSet(entry.getValue()));
            }
        }
        
        public int V() {
            return nodeOutEdges.keySet().size();
        }
        
        public Set<Integer> getNodes() {return nodeOutEdges.keySet(); }
        
        public void addNode(int node) {
            maxVNum = Math.max(maxVNum, node);
            
            if (!nodeOutEdges.containsKey(node)) {
                nodeOutEdges.put(node, Sets.<Integer>newHashSet());
                nodeInEdges.put(node, Sets.<Integer>newHashSet());
            }
        }
        
        public int getMaxVNum()
        {
            return maxVNum;
        }

        public void removeConnection(int from, int to) {
                       
            Set<Integer> nodeANeighbors;           
            
            if (nodeOutEdges.containsKey(from)) {            
            
                nodeANeighbors = nodeOutEdges.get(from);
                nodeANeighbors.remove(to);
                
                nodeInEdges.get(to).remove(from);
            }
            
            
            
        }
        
        /**
         * Only works properly in undirected graphs
         * @param nodeA
         */
        public void removeNode(int nodeA) {
            
            Set<Integer> nodeANeighbors = nodeOutEdges.get(nodeA);
                
            for(int adj : nodeANeighbors) {
                nodeOutEdges.get(adj).remove(nodeA);
            }
            
            nodeOutEdges.remove(nodeA);
            
        }
        
       
        /**
         * From A to B
         * @param from
         * @param to
         */
        public void addOneWayConnection(int from, int to) {
            addNode(from);
            addNode(to);
            
            Set<Integer> outSet = nodeOutEdges.get(from);
            
            outSet.add(to);
            
            Set<Integer> inSet = nodeInEdges.get(to);
            inSet.add(from);
        }

       
        
        
        public Set<Integer> getOutbound(int node) {
            return nodeOutEdges.get(node);
        }
        
        public boolean isConnected(int from, int to)
        {
            if (!nodeExists(from))
                return false;
            
            return nodeOutEdges.get(from).contains(to);
        }
        
        public boolean nodeExists(int node) {
            return nodeOutEdges.containsKey(node);
        }
        
        public int getOutboundDegree(int node) {
            if (!nodeExists(node))
                return 0;
            
            return nodeOutEdges.get(node).size();
        }
        
        public int getInboundDegree(int node) {
            if (!nodeExists(node))
                return 0;
            
            return nodeInEdges.get(node).size();
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return Objects.hashCode(nodeOutEdges);
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
            
            return Objects.equal(nodeOutEdges, other.nodeConnections);
        }

        @Override
        public String toString()
        {
            StringBuffer sb = new StringBuffer();
            sb.append("Graph V=").append(V()).append("\n");
            
            for(Integer v : nodeOutEdges.keySet()) {
                sb.append("vertex ").append(v);
                sb.append("\noutConnections -- ");
                sb.append(Joiner.on(", ").join(nodeOutEdges.get(v)));
                sb.append("\ninConnections -- ");
                sb.append(Joiner.on(", ").join(nodeInEdges.get(v)));
                sb.append("\n\n");
                
            }
            return sb.toString();
        }

       
    }



