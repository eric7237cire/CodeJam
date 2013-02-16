package codejam.y2008.round_3.no_cheating;

import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.graph.Bipartite;
import codejam.utils.datastructures.graph.GraphInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>
{

    public Main() {
        super("C", 1, 1, 0);
        //((ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);

        in.R = scanner.nextInt();
        in.C = scanner.nextInt();
        
        in.grid = GridChar.buildFromScanner(scanner,in.R,in.C, 'x');
        return in;
    }

    @Override
    public String handleCase(InputData in)
    {

       GraphInt graph = new GraphInt();
       
       Direction[] dirs = new Direction[] {
               Direction.NORTH_EAST,
               Direction.EAST,
               Direction.WEST,
               Direction.NORTH_WEST
       };
       

       List<Integer> left = Lists.newArrayList();
       List<Integer> right = Lists.newArrayList();
       
       for(int gcIdx = 0; gcIdx < in.grid.getSize(); ++gcIdx)
       {
           char curCh = in.grid.getEntry(gcIdx);
           if (curCh == 'x')
               continue;
           
           graph.addNode(gcIdx);
           
           int col = in.grid.getRowCol(gcIdx)[1];
           
           if (col % 2 == 0) {
               left.add(gcIdx);
           } else {
               right.add(gcIdx);
           }
           
           for(Direction dir : dirs) {
               Integer adjIdx = in.grid.getIndex(gcIdx,dir);
               
               if (adjIdx == null)
                   continue;
               
               char ch = in.grid.getEntry(adjIdx);
               
               if (ch == 'x')
                   continue;
               
             //  graph.addOneWayConnection(gcIdx, adjIdx);
               
               //Even though only this node can see the other paper, in terms of 
               //a configuration, both directions mean cheating can occur
               graph.addConnection(gcIdx, adjIdx);
           }
       }
       
       
       
       log.debug("Grid {}", in.grid);
       
       log.debug("Graph {}", graph);
       //boolean b = Bipartite.getBipartite(graph,left,right);
       
       log.debug("left {} right {}", left, right);
       List<Pair<Integer,Integer>> matching = Bipartite.getMaxMatching(graph,left,right);
       
       log.debug("Max matching {}", matching);
       int indSet = graph.V() - matching.size();
       
        return String.format("Case #%d: %d", in.testCase, indSet);
        
    }
}