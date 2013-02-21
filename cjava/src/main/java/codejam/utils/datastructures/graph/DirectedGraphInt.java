package codejam.utils.datastructures.graph;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;

/**
 * Allows multiple connections & self loops too
 * 
 *
 */
public class DirectedGraphInt
{

    Map<Integer, Multiset<Integer>> nodeOutEdges;
    Map<Integer, Multiset<Integer>> nodeInEdges;

    //Number of vertices
    int maxVNum;

    public DirectedGraphInt() {
        nodeOutEdges = Maps.newHashMap();
        nodeInEdges = Maps.newHashMap();
        maxVNum = 0;
    }

    /*
    public DirectedGraphInt(DirectedGraphInt graph) {
        nodeOutEdges = Maps.newHashMap();
        nodeInEdges = Maps.newHashMap();
        for (Map.Entry<Integer, Set<Integer>> entry : graph.nodeOutEdges.entrySet())
        {
            nodeOutEdges.put(entry.getKey(), Sets.newHashSet(entry.getValue()));
        }
        for (Map.Entry<Integer, Set<Integer>> entry : graph.nodeInEdges.entrySet())
        {
            nodeInEdges.put(entry.getKey(), Sets.newHashSet(entry.getValue()));
        }
    }*/

    public int V()
    {
        return nodeOutEdges.keySet().size();
    }

    public Set<Integer> getNodes()
    {
        return nodeOutEdges.keySet();
    }

    public void addNode(int node)
    {
        maxVNum = Math.max(maxVNum, node);

        if (!nodeOutEdges.containsKey(node))
        {
            nodeOutEdges.put(node, HashMultiset.<Integer>create());
            nodeInEdges.put(node, HashMultiset.<Integer>create());
        }
    }

    public int getMaxVNum()
    {
        return maxVNum;
    }

    public int edgeCount()
    {
        int edges = 0;
        for (int n : nodeOutEdges.keySet())
        {
            edges += getOutboundDegree(n);
        }
        return edges;
    }

    //Removes a single edge
    public void removeConnection(int from, int to)
    {
        Multiset<Integer> outSet;

        if (nodeOutEdges.containsKey(from))
        {
            outSet = nodeOutEdges.get(from);
            outSet.remove(to);

            nodeInEdges.get(to).remove(from);
        }
    }

    /**
     * 
     */
    public void removeNode(int nodeA)
    {

        Multiset<Integer> outSet = nodeOutEdges.get(nodeA);

        for (int adj : outSet.elementSet())
        {
            nodeInEdges.get(adj).remove(nodeA, Integer.MAX_VALUE);            
        }

        nodeOutEdges.remove(nodeA);

    }

    /**
     * From A to B
     * @param from
     * @param to
     */
    public void addOneWayConnection(int from, int to)
    {
        addNode(from);
        addNode(to);

        Multiset<Integer> outSet = nodeOutEdges.get(from);
        outSet.add(to);

        Multiset<Integer> inSet = nodeInEdges.get(to);
        inSet.add(from);
    }
    
    public void setEdgesBetween(int from, int to, int count)
    {
        addNode(from);
        addNode(to);

        Multiset<Integer> outSet = nodeOutEdges.get(from);
        outSet.setCount(to, 1);

        Multiset<Integer> inSet = nodeInEdges.get(to);
        inSet.setCount(from, 1);
    }

    public Set<Integer> getOutbound(int node)
    {
        return nodeOutEdges.get(node).elementSet();
    }
    
    public Set<Integer> getInbound(int node)
    {
        if (!nodeExists(node))
        {
            return Collections.emptySet();
        }
        return nodeInEdges.get(node).elementSet();
    }

    public boolean isConnected(int from, int to)
    {
        if (!nodeExists(from))
            return false;

        return nodeOutEdges.get(from).contains(to);
    }

    public boolean nodeExists(int node)
    {
        return nodeOutEdges.containsKey(node);
    }

    public int getOutboundDegree(int node)
    {
        if (!nodeExists(node))
            return 0;

        return nodeOutEdges.get(node).size();
    }

    public int getInboundDegree(int node)
    {
        if (!nodeExists(node))
            return 0;

        return nodeInEdges.get(node).size();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return Objects.hashCode(nodeOutEdges);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
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

        for (Integer v : nodeOutEdges.keySet())
        {
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
