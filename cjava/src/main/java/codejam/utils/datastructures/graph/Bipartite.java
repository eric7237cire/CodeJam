package codejam.utils.datastructures.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Bipartite
{

    final static Logger log = LoggerFactory.getLogger("main");
    
    /**
     * Only works in undirected graphs
     * @param graph
     * @param left
     * @param right
     * @return
     */
    public static boolean getBipartite(GraphInt graph, List<Integer> left, List<Integer> right)
    {
        int[] color = new int[graph.getMaxVNum()+1];
        
        
        
        for(int v = 0; v <= graph.maxVNum; ++v) 
        {
            if (color[v] != 0 || !graph.nodeExists(v))
                continue;
            
            Queue<Integer> toVisit = new LinkedList<>();
            toVisit.add(v);
            
            color[v] = 1;
            
            while(!toVisit.isEmpty())
            {
                Integer node = toVisit.poll();
                
                int curColor = color[node];
                int adjColor = 3 - curColor;
                
                Set<Integer> adjNodes = graph.getNeighbors(node);
                
                for(int adj : adjNodes) {
                    if (color[adj] == 0) {
                        toVisit.add(adj);
                        color[adj] = adjColor;
                    }
                    
                    if (color[adj] == curColor) {
                        log.debug("Cur node {} adj Node {} is already colored {}",node,adj,curColor);
                        return false;
                    }
                }
            }
        }
        
        for(int v = 0; v <= graph.getMaxVNum(); ++v)
        {
            if (color[v] == 1)
                left.add(v);
            
            if (color[v] == 2)
                right.add(v);
        }
        
        
        return true;
    }


    /**
     * Imagine a bi-partite graph.  Left and Right side are stocks
     * with an edge if the left vertex is stricly greater than the right.
     * 
     * @param input
     * @return
     */
    public static List<Pair<Integer, Integer>> getMaxMatching(GraphInt graph, List<Integer> lhsNodes, List<Integer> rhsNodes, List<Pair<Integer, Integer>> greedyMatch) {
        // Right to Left
        BiMap<Integer, Integer> matchesMap = HashBiMap.create();
        
        int count = 0;
        
        if (greedyMatch != null) {
            for (Pair<Integer,Integer> edge : greedyMatch)
            {
                matchesMap.put(edge.getRight(), edge.getLeft());
            }
            count = matchesMap.size();
        }

        // Right hand side of the bipartite graph
        boolean[] seen;

        
        for (int lhsNodeIdx = 0; lhsNodeIdx < lhsNodes.size(); ++lhsNodeIdx) {
            
            if (matchesMap.inverse().containsKey(lhsNodes.get(lhsNodeIdx)))
                continue;
            
            seen = new boolean[rhsNodes.size()];

            
            
            if (findAugmentingPath(graph,lhsNodes.get(lhsNodeIdx), rhsNodes, seen, matchesMap)) {
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
    
    private static boolean findAugmentingPath(GraphInt graph, int lhsVertex,
            List<Integer> rhsNodes, boolean[] seen, BiMap<Integer, Integer> matchesMap) {
        /* 
         * The vertex is part of the left hand side
         */
        //log.debug("findAugPath");
        
        //Loop through all right hand side vertices
        for(int rhsVertexIdx = 0; rhsVertexIdx < rhsNodes.size(); ++rhsVertexIdx) {
            int rhsVertex = rhsNodes.get(rhsVertexIdx);
            if (!graph.getNeighbors(lhsVertex).contains(rhsVertex)) {
                continue;
            }
            if (seen[rhsVertexIdx]) {
                continue;
            }
            
            seen[rhsVertexIdx] = true;
            
            /*
             * Here the edge is either already in M or not.
             * If its not we are done, we found a path.  Otherwise
             * we try to rematch the connected lhsvertex to another rhsVertex .
             */
            if (!matchesMap.containsKey(rhsVertex) || findAugmentingPath(graph, matchesMap.get(rhsVertex),rhsNodes,seen,matchesMap)) {
                
                //If it exists, free the existing match edge if it exists
                matchesMap.inverse().remove(lhsVertex);
                
                matchesMap.put(rhsVertex,  lhsVertex);
                return true;
            }
        }
        
        return false;
    }
}
