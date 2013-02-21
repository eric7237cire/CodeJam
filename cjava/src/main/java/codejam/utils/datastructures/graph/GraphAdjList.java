package codejam.utils.datastructures.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/*
 * Nodes 0 based ints
 */
public class GraphAdjList {

    final static Logger log = LoggerFactory.getLogger("main");
    
    private boolean[][] connected;
    int maxNodeNum;

    public GraphAdjList(int maxNodeNum) {
        this.maxNodeNum = maxNodeNum;
        connected = new boolean[maxNodeNum][maxNodeNum];
    }
    
    public int getMaxNodes() {
        return maxNodeNum;
    }

    public void addConnection(int n1, int n2) {
        connected[n1][n1] = true;
        connected[n2][n2] = true;
        connected[n1][n2] = true;
        connected[n2][n1] = true;
    }

    public boolean isConnected(int n1, int n2) {
        return connected[n1][n2];
    }
    public boolean nodePresent(int n) {
        return isConnected(n,n);
    }
    
    

    
    /**
     * Imagine a bi-partite graph.  Left and Right side are stocks
     * with an edge if the left vertex is stricly greater than the right.
     * 
     * @param input
     * @return
     */
    public List<Pair<Integer, Integer>> getMaxMatching(List<Integer> lhsNodes, List<Integer> rhsNodes) {
        // Right to Left
        BiMap<Integer, Integer> matchesMap = HashBiMap.create();

        // Right hand side of the bipartite graph
        boolean[] seen;

        int count = 0;
        for (int lhsNodeIdx = 0; lhsNodeIdx < lhsNodes.size(); ++lhsNodeIdx) {
            seen = new boolean[rhsNodes.size()];

            if (findAugmentingPath(lhsNodes.get(lhsNodeIdx), rhsNodes, seen, matchesMap)) {
                ++count;
            }

            // printAugmentingPaths(input);
        }

        List<Pair<Integer, Integer>> matches = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : matchesMap.entrySet()) {
            matches.add(new ImmutablePair<>(entry.getValue(), entry.getKey()));
        }
        Preconditions.checkState(count == matches.size());
        return matches;
    }
    
    private boolean findAugmentingPath(int lhsVertex, List<Integer> rhsNodes, boolean[] seen, BiMap<Integer, Integer> matchesMap) {
        /* 
         * The vertex is part of the left hand side
         */
        log.debug("findAugPath");
        
        //Loop through all right hand side vertices
        for(int rhsVertexIdx = 0; rhsVertexIdx < rhsNodes.size(); ++rhsVertexIdx) {
            int rhsVertex = rhsNodes.get(rhsVertexIdx);
            if (!connected[lhsVertex][rhsVertex]) {
                continue;
            }
            if (seen[rhsVertexIdx]) {
                continue;
            }
            
            seen[rhsVertexIdx] = true;
            
            /*
             * Here the edge is either already in M or not.
             * If its not we are done, we found a path.  Otherwise
             * we try to rematch the connected lhsvertex to another greater rhsVertex .
             */
            if (!matchesMap.containsKey(rhsVertex) || findAugmentingPath(matchesMap.get(rhsVertex),rhsNodes,seen,matchesMap)) {
                
                //If it exists, free the existing match edge if it exists
                matchesMap.inverse().remove(lhsVertex);
                
                matchesMap.put(rhsVertex,  lhsVertex);
                return true;
            }
        }
        
        return false;
    }

    public List<List<Integer>> getConnectedComponents() {
        int[] componentNumPerNode = new int[maxNodeNum];

        List<List<Integer>> groups = new ArrayList<>();

        int currentNodeNum = 0;
        int currentGroupNum = 0;

        while (true) {
            ++currentGroupNum;
            
            while (currentNodeNum < maxNodeNum
                    && (componentNumPerNode[currentNodeNum] != 0 || !nodePresent(currentNodeNum) )) {
                // find next node num with no group num
                ++currentNodeNum;
            }

            if (currentNodeNum >= maxNodeNum) {
                break;
            }
            
            groups.add(new ArrayList<Integer>());

            // Search all nodes connected
            boolean[] visited = new boolean[maxNodeNum];

            List<Integer> nodesToVisit = new ArrayList<>();
            nodesToVisit.add(currentNodeNum);

            while (!nodesToVisit.isEmpty()) {
                Integer node = nodesToVisit.remove(nodesToVisit.size() - 1);

                if (visited[node]) {
                    continue;
                }

                visited[node] = true;
                componentNumPerNode[node] = currentGroupNum;
                groups.get(currentGroupNum - 1).add(node);

                for (int i = 0; i < maxNodeNum; ++i) {
                    if (isConnected(node, i)) {
                        nodesToVisit.add(i);
                    }
                }
            }

        }

        return groups;
    }
}
