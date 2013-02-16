package codejam.utils.datastructures.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.google.common.collect.Maps;

/*************************************************************************
 *  Compilation:  javac Bridge.java
 *  Dependencies: Graph.java 
 *
 *  Identifies bridge edges and prints them out. This decomposes
 *  a directed graph into two-edge connected components.
 *  Runs in O(E + V) time.
 *
 *  Key quantity:  low[v] = minimum DFS preorder number of v
 *  and the set of vertices w for which there is a back edge (x, w)
 *  with x a descendant of v and w an ancestor of v.
 *
 *  Note: code assumes no parallel edges, e.g., two parallel edges
 *  would be (incorrectly) identified as bridges.
 *
 *************************************************************************/

//http://stackoverflow.com/questions/11218746/bridges-in-a-connected-graph
public class Bridge {
    final static Logger log = LoggerFactory.getLogger(Bridge.class);
    
    
    private int cnt;          // counter
    private Map<Integer,Integer> pre;        // pre[v] = order in which dfs examines v
    private Map<Integer,Integer> low;        // low[v] = lowest preorder of any vertex connected to v

    List<Pair<Integer,Integer>> bridges;
    
    public Bridge(GraphInt G) {
        Set<Integer> nodes = G.getNodes();
        
        low = Maps.newHashMap();
        pre = Maps.newHashMap();
        bridges = new ArrayList<>();
        for (int v : nodes) low.put(v, -1);
        for (int v : nodes) pre.put(v,-1);
        
        for (int v : nodes)
            if (pre.get(v) == -1)
                dfs(G, v, v);
    }

    public List<Pair<Integer,Integer>> getBridges() { return bridges ; }

    private void dfs(GraphInt G, int u, int v) {
        pre.put(v, cnt++);
        low.put(v, pre.get(v));
        for (int w : G.getNeighbors(v)) {
            if (pre.get(w) == -1) {
                dfs(G, v, w);
                low.put(v, Math.min(low.get(v), low.get(w)));
                if (low.get(w) == pre.get(w)) {
                    //log.debug(v + "-" + w + " is a bridge");
                    bridges.add(new ImmutablePair<>(v,w));
                }
            }

            // update low number - ignore reverse of edge leading to v
            else if (w != u)
                low.put(v, Math.min(low.get(v), pre.get(w)));
        }
    }



}

