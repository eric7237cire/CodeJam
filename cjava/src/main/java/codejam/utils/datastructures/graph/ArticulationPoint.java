package codejam.utils.datastructures.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import codejam.utils.datastructures.graph.GraphInt;

import com.google.common.collect.Maps;


/*************************************************************************
 *  Compilation:  javac Biconnected.java
 *  Dependencies: Graph.java 
 *
 *  Identify articulation points and print them out.
 *  This can be used to decompose a graph into biconnected components.
 *  Runs in O(E + V) time.
 *
 *  http://www.cs.brown.edu/courses/cs016/book/slides/Connectivity2x2.pdf
 *
 *************************************************************************/

public class ArticulationPoint {
    private Map<Integer,Integer> low;
    private Map<Integer,Integer> pre;
    private int cnt;
    private List<Integer> articulation;

    public ArticulationPoint(GraphInt G) {
        Set<Integer> nodes = G.getNodes();
        low = Maps.newHashMap();
        pre = Maps.newHashMap();
        articulation = new ArrayList<>();
        for (int v : nodes) low.put(v, -1);
        for (int v : nodes) pre.put(v,-1);
        
        for (int v : nodes)
            if (pre.get(v) == -1)
                dfs(G, v, v);
    }

    private void dfs(GraphInt G, int u, int v) {
        int children = 0;
        pre.put(v, cnt++);
        low.put(v, pre.get(v));
        for (int w : G.getNeighbors(v)) {
            if (pre.get(w) == -1) {
                children++;
                dfs(G, v, w);

                // update low number
                low.put(v, Math.min(low.get(v), low.get(w)));

                // non-root of DFS is an articulation point if low[w] >= pre[v]
                if (low.get(w) >= pre.get(v) && u != v) 
                    articulation.add(v);
            }

            // update low number - ignore reverse of edge leading to v
            else if (w != u)
                low.put(v, Math.min(low.get(v), pre.get(w)));
        }

        // root of DFS is an articulation point if it has more than 1 child
        if (u == v && children > 1)
            articulation.add(v);
    }

    // is vertex v an articulation point?
    public List<Integer> getArticulationPoints() { return articulation; }
}
