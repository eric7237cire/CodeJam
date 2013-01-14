package codejam.y2008.round_final.bridge_builders;


import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.BreadthFirstPaths;
import codejam.utils.datastructures.Edge;
import codejam.utils.datastructures.EdgeWeightedGraph;
import codejam.utils.datastructures.Graph;
import codejam.utils.datastructures.LazyPrimMST;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
         //return new String[] {"sample.in"};
        // return new String[] { "D-small-practice.in" };
       return new String[] { "D-small-practice.in", "D-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        
        InputData in = new InputData(testCase);
       
        in.R = scanner.nextInt(); // rows
        in.C = scanner.nextInt();
        
        in.grid = GridChar.buildFromScanner(scanner, in.R, in.C, '.');
        
        return in;
    }

   
    
    public String handleCase(InputData in) {

   
        Graph g  = new Graph(in.grid.getSize());
        
        List<Integer> forests = Lists.newArrayList();
        
        for(int n = 0; n < in.grid.getSize(); ++n) {
            char sq = in.grid.getEntry(n);
            
            if (sq == '.')
                continue;
            
            if (sq == 'T')
                forests.add(n);
            
            for(int dIdx = 0; dIdx <= 1; ++dIdx) {
                Direction dir = Direction.EAST.turn(dIdx * 2);
                
                Integer adj = in.grid.getIndex(n, dir);
                
                if (adj != null && in.grid.getEntry(adj) != '.') {
                    g.addEdge(n, adj);
                }
            }
        }
        
        EdgeWeightedGraph forestGraph = new EdgeWeightedGraph(forests.size());
        
        //Map<Pair<Integer,Integer>, BreadthFirstPaths> forestPaths = Maps.newHashMap();
        List<BreadthFirstPaths> forestPaths = Lists.newArrayList();
        
        for(int f1Idx = 0; f1Idx < forests.size(); ++f1Idx) {
            int f1 = forests.get(f1Idx);
            
            BreadthFirstPaths bfp = new BreadthFirstPaths(g, f1);
            
            for(int f2Idx = f1Idx+1; f2Idx < forests.size(); ++f2Idx) {
                int f2 = forests.get(f2Idx);
                
                forestGraph.addEdge(new Edge(f1Idx, f2Idx, bfp.distTo(f2)));
            }
            
            forestPaths.add(bfp);
        }
        
        //PrimMST mst = new PrimMST(forestGraph);
        LazyPrimMST mst = new LazyPrimMST(forestGraph);
        
        Set<Integer> connectedIslands = Sets.newHashSet();
        
        connectedIslands.add(0);
        
        //Set<Integer>
        int totalCost = 0;
        
        for(Edge forestToForest : mst.edges()) {
            int f1Idx = forestToForest.either();
            int f2Idx = forestToForest.other(f1Idx);
            //make f1 the visited one
            
            int f2 = forests.get(f2Idx);
            int f1 = forests.get(f1Idx);
            if (!connectedIslands.contains(f1)) {
                Preconditions.checkState(connectedIslands.contains(f2));
                int swap = f1Idx;
                f1Idx = f2Idx;
                f2Idx = swap;
                
                swap = f1;
                f1 = f2;
                f2 = swap;
            }
            //Get path between the 2 forrests
            Iterable<Integer> path = forestPaths.get(f1Idx).pathTo(f2);
        
            int cost = 0;
           // log.debug("Starting forest path path between {} and {}", in.grid.getRowCol(f1), in.grid.getRowCol(f2));
            for(Integer island : path) {
                
                if (!connectedIslands.contains(island)) {
                    totalCost+= cost;
                    connectedIslands.add(island);
                }
               // log.debug("Forest path: {} cost {} total cost {}", in.grid.getRowCol(island), cost, totalCost);
                cost++;
            }
        }
        
        //Now for the rest, do an uber BST from all forests
        
        BreadthFirstPaths finalBFP = new BreadthFirstPaths(g, forests);
        
        for(int n = 0; n < in.grid.getSize(); ++n) {
            if (connectedIslands.contains(n))
                continue;
            
            char sq = in.grid.getEntry(n);
            
            if (sq == '.')
                continue;
            
            int cost = finalBFP.distTo(n);
            totalCost += cost;
            
        }
                
        return String.format("Case #%d: %d", in.testCase, totalCost);        
    }

}
