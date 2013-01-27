package codejam.y2009.round_final.marbles;

import java.util.Scanner;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.Bipartite;
import codejam.utils.datastructures.graph.Graph;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class Main implements TestCaseHandler<InputData>, 
TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
       return new String[] {"sample.in"};
        //return new String[] {"E-small-practice.in"};
       // return new String[] {"A-small-practice.in", "A-large-practice.in"};
    }
    
   

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
        
        in.N = scanner.nextInt();
        in.colorToId = Maps.newHashMap();
        
        in.colorLoc = Lists.newArrayList();
        in.locColor = new int[2 * in.N];
        
        for(int p = 0; p < 2 * in.N; ++p) {
            
            String color = scanner.next();
            
            int colorId = -1;
            if (!in.colorToId.containsKey(color)) {
                colorId = in.colorLoc.size();
                in.colorToId.put(color,colorId);
                in.colorLoc.add(new MutablePair<>(p, -1));
            } else {
                colorId = in.colorToId.get(color);
                in.colorLoc.get(colorId).setValue(p);
            }
            
            in.locColor[p] = colorId;
            
        }
               
        return in;
    }

    
    
    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in) {
       
        /*Create a graph where
         * Each node is a pair of marbles
         * If the marbles go A B A B, then add an edge (they must be on opposite sides)
         */
        
        Graph graph = new Graph(in.N);
        
        for(int c = 0; c < in.N; ++c) {
            for(int c2 = c+1; c2 < in.N; ++c2) {
                Pair<Integer,Integer> cLocs = in.colorLoc.get(c);
                Pair<Integer,Integer> c2Locs = in.colorLoc.get(c2);
                
                if (cLocs.getLeft() > c2Locs.getLeft() && 
                        cLocs.getLeft() < c2Locs.getRight() && 
                        cLocs.getRight() > c2Locs.getRight()
                        ) {
                    // c2 c c2 c
                    graph.addEdge(c, c2);
                } else 
                if (c2Locs.getLeft() > cLocs.getLeft() && 
                        c2Locs.getLeft() < cLocs.getRight() &&
                        c2Locs.getRight() > cLocs.getRight()
                        ) {
                    // c c2 c c2
                    graph.addEdge(c, c2);
                }
            }
        }
        
        Bipartite b = new Bipartite(graph);
        boolean isBipartite = b.isBipartite();
        
        return String.format("Case #%d: %d", in.testCase, isBipartite ? 0 : -1);
    }
}