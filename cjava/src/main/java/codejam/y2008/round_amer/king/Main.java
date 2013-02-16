package codejam.y2008.round_amer.king;

import java.util.Iterator;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.UndirectedGraph;
import codejam.utils.datastructures.graph.EdmondsMatching;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String[] getDefaultInputFiles() {
        //return new String[] { "sample.in"};
     //   return new String[] { "p43.in"};
       // return new String[] {  };
        return new String[] { "D-small-practice.in", "D-large-practice.in" };
        //return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.row = scanner.nextInt();
        input.col = scanner.nextInt();
        GridChar grid = GridChar.buildFromScanner(scanner,input.row,input.col, '#');
        input.grid = grid;
        
        return input;
    }
    
    public static boolean skipDebug = false;
    
    
    @Override
    public String handleCase(InputData input) {
        return correctSolution(input);
        //return OldSolution.awinsIfEven(input);
        //log.error(str);
        //return bruteForce(input);
    }
    
    public String correctSolution(InputData in) {
        /**
         * Build graph
         */
        //Graph graph = new Graph(in.grid.getSize());
        UndirectedGraph<Integer> unGraph = new UndirectedGraph<>();
        UndirectedGraph<Integer> unGraphWithoutKing = new UndirectedGraph<>();
                
        for(int gridIndex = 0; gridIndex < in.grid.getSize(); ++gridIndex) 
        {
            unGraph.addNode(gridIndex);
            unGraphWithoutKing.addNode(gridIndex);
            char curSq = in.grid.getEntry(gridIndex);
        
            if (curSq == '#')
                continue;
            
            for(Direction dir : Direction.values()) {
                Integer childIdx = in.grid.getIndex(gridIndex,dir);
                if (childIdx == null)
                    continue;
                
                unGraph.addNode(childIdx);
                unGraphWithoutKing.addNode(childIdx);
                
                char adjSq = in.grid.getEntry(childIdx);
                
                if (adjSq == '#')
                    continue;
                
                unGraph.addEdge(gridIndex, childIdx);
                
                if (curSq != 'K' && adjSq != 'K')
                    unGraphWithoutKing.addEdge(gridIndex, childIdx);
            }
        }
        
         
        UndirectedGraph<Integer> match = EdmondsMatching.maximumMatching(unGraph);
        UndirectedGraph<Integer> matchNoKing = EdmondsMatching.maximumMatching(unGraphWithoutKing);
        
        int matchCount = countEdgesInMatching(match);
        int matchCountNoKing = countEdgesInMatching(matchNoKing);
        return String.format("Case #%d: %s", in.testCase, matchCount > matchCountNoKing ? "A" : "B");
        
        
    }
    
    int countEdgesInMatching(UndirectedGraph<Integer> uGraph) {
        Iterator<Integer> it = uGraph.iterator();
        int count = 0;
        while(it.hasNext()) {
            count += uGraph.edgesFrom(it.next()).size();
        }
        
        return count / 2;
    }
    
    

}
