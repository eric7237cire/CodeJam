package codejam.utils.datastructures.graph;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//ALso KosarajuSharirSCC for bridge finding
public class BridgeDirectedGraph
{

    private static final int DFS_WHITE = -1; // normal DFS
    final static Logger log = LoggerFactory.getLogger("main");
    
    /**
     * dfs_num is order that vertex has been first visited
     * index is vertex number
     */
    private int[] dfs_num, dfs_low;

    private boolean[] visited;
    private static Stack<Integer> S; // additional information for SCC
    
    private int numComp, dfsNumberCounter;
    private int V;

    private DirectedGraphInt graph;
    
    private int[] scCompNum;

    public BridgeDirectedGraph(DirectedGraphInt graph) {
        this.graph = graph;
        V = graph.maxVNum+1;
        //Verticies must be 0 indexed
        //Preconditions.checkState(graph.getMaxVNum() < V);

        initTarjanSCC();
    }

    public void go()
    {
        for (int i = 0; i < V; i++)
        {
            if (!graph.nodeExists(i))
                continue;
            
            if (dfs_num[i] == DFS_WHITE)
                tarjanSCC(i);
        }
    }

    private void initTarjanSCC()
    {
        dfs_num = new int[V];
        Arrays.fill(dfs_num, DFS_WHITE);
        numComp = 0;
        
        dfs_low = new int[V];
        //dfs_low.addAll(Collections.nCopies(V, 0));
        dfsNumberCounter = 0;
        numComp = 0;
        S = new Stack<Integer>();
        visited = new boolean[V];
        
        scCompNum = new int[V];
        //visited.addAll(Collections.nCopies(V, false));
    }

    public int compNum (int v) {
        return scCompNum[v];
    }
    
    private void tarjanSCC(int u)
    {
        dfs_num[u] = dfsNumberCounter;
        dfs_low[u] = dfsNumberCounter++; // dfs_low[u] <= dfs_num[u]
        S.push(u); // store u according to order of visitation
        visited[u] = true;

        Iterator<Integer> it = graph.getOutbound(u).iterator();
        while (it.hasNext())
        { // try all neighbors v of vertex u
            int v = it.next();
            if (dfs_num[v] == DFS_WHITE) // a tree edge
            {
                tarjanSCC(v);
            }
            if (visited[v]) // condition for update
                dfs_low[u] = Math.min(dfs_low[u], dfs_low[v]);
        }

        if (dfs_low[u] == dfs_num[u])
        { // if this is a root (start) of an SCC
            ++numComp;
            log.debug("Strongly connected component num {}", numComp);
            while (true)
            {
                int v = S.peek();
                S.pop();
                visited[v] = false;
                log.debug(" {}", v);
                scCompNum[v] = numComp; 
                if (u == v)
                    break;
            }
           // System.out.printf("\n");
        }
    }

}
