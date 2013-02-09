package codejam.y2011.round_2.ai_wars;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.BreadthFirstPaths;
import codejam.utils.datastructures.graph.Graph;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;

public class Solution extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>
{

    public Solution() {
        super("D", 1, 1, 1);
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {
        Main m = new Main();
        return m.readInput(scanner,testCase);
    }

    final int TARGET = 1;
    final int HOME = 0;
    
    
    BitSet[] computerNeighborsMask(Graph g)
    {
        BitSet[] neighMask = new BitSet[g.V()];
        
        
        for(int v = 0; v < g.V(); ++v) {
            BitSet bs = new BitSet();
            for (int u : g.adj(v)) {
                bs.set(u);
            }
            neighMask[v] = bs;
        }
        
        return neighMask;
    }
    
    int G(int a, int b, int c, BitSet[] neighMask) {
        //c and not (a or b)
        
        BitSet ans = BitSet.valueOf(neighMask[c].toLongArray());
        ans.andNot(neighMask[a]);
        ans.andNot(neighMask[b]);
        
        return ans.cardinality();
    }
    
    
    @Override
    public String handleCase(InputData in)
    {
        /**
         * First create a graph
         */
        Graph g = new Graph(in.P);
        for( Pair<Integer,Integer> wormHole : in.wormHoles) {
            g.addEdge(wormHole.getLeft(), wormHole.getRight());
        }
        
        BreadthFirstPaths bfp = new BreadthFirstPaths(g, HOME);
        
        final int D = bfp.distTo(TARGET);
        
        Preconditions.checkState(bfp.distTo(HOME) == 0);
        
        /**
         * F[a][b] a and b are adjancent.  dist[a]  = D-i dist[b] == D-i-1
         * ie b is closer to target planet
         * 
         *  the maximum number of planets threatened or conquered by a shortest path 0→...→a→b
         */
        final int[][] F = new int[in.P][in.P];
        
        
        
        BitSet[] neighMasks = computerNeighborsMask(g);
        
        BitSet neighHome = neighMasks[HOME];
        
        Queue<Integer> toVisit = new LinkedList<>();
        
        //Compute F[0][nodeB]
        for(Integer nodeA : g.adj(HOME)) {
            if (bfp.distTo(nodeA) != 1) 
                continue;
        
            if (nodeA == TARGET) {
                return String.format("Case #%d: %d %d", in.testCase, 0, neighHome.cardinality());
            }
            
            BitSet neighNodeB = neighMasks[nodeA];
            BitSet allNeighbors = BitSet.valueOf(neighNodeB.toLongArray());
            
            allNeighbors.or(neighHome);
            
            F[0][nodeA] = allNeighbors.cardinality();
           
            //log.debug("F[{}][{}] = {}", 0, nodeA,  F[0][nodeA]);
            toVisit.add(nodeA);
            /*for(Integer nodeA : g.adj(nodeB)) {
                if (bfp.distTo(nodeA) != D - 2) 
                    continue;
            }*/
        }
        

        boolean[] visited = new boolean[in.P];
        
        
        //int[][] gMemo = 
        
        int ans = 0;
        
        while(!toVisit.isEmpty()) {
            int nodeB = toVisit.poll();
            
            if (visited[nodeB])
                continue;
            
            visited[nodeB] = true;
            
            for(Integer nodeC : g.adj(nodeB)) {
                if (bfp.distTo(nodeC) != bfp.distTo(nodeB) + 1) 
                    continue;
            
                for(Integer nodeA : g.adj(nodeB)) {
                    if (bfp.distTo(nodeA) != bfp.distTo(nodeB) - 1)
                        continue;
            
                    /*
                    log.debug("F[{}][{}]={} + G({},{},{}) = {}",
                            nodeA, nodeB,
                            F[nodeA][nodeB],
                            nodeA, nodeB, nodeC,  G(nodeA, nodeB, nodeC, neighMasks));
                    */
                    F[nodeB][nodeC] = Math.max(F[nodeB][nodeC],
                            F[nodeA][nodeB] + G(nodeA, nodeB, nodeC, neighMasks));
                    
                    if (bfp.distTo(nodeB) == D-2 && bfp.distTo(nodeB) == D-1) {
                        ans = Math.max(ans, F[nodeB][nodeC]);
                    }
                }
                
                
                toVisit.add(nodeC);
            }
        }
        
        for(Integer nodeB : g.adj(TARGET)) {
            if (bfp.distTo(nodeB) != D-1) 
                continue;
            for(Integer nodeA : g.adj(nodeB)) {
                if (bfp.distTo(nodeA) != D-2) 
                    continue;
                ans = Math.max(ans, F[nodeA][nodeB]);
            }            
        }

        /*
        The answer to the problem is the maximum value of F(a, b) - D, 
        where a, b are adjacent, dist[a] = D-2, dist[b] = D-1, and b is adjacent to 1.
        */
        
        // TODO Auto-generated method stub
        return String.format("Case #%d: %d %d", in.testCase, D-1, ans-D);
    
    }

}
