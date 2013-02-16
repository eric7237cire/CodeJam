package codejam.y2008.round_3.no_cheating;

import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.graph.FlowEdge;
import codejam.utils.datastructures.graph.FlowNetwork;
import codejam.utils.datastructures.graph.FordFulkerson;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>
{

    public Main() {
        super("C", 1, 1, 1);
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

      // GraphInt graph = new GraphInt();
      // UndirectedGraph<Integer> uGraph = new UndirectedGraph<Integer>();
       int N = in.grid.getSize();
       FlowNetwork fn = new FlowNetwork(N+2);
       
       int s = N, t = N + 1;
       
       Direction[] dirs = new Direction[] {
               Direction.NORTH_EAST,
               Direction.EAST,
               Direction.WEST,
               Direction.NORTH_WEST
       };
       

       List<Integer> left = Lists.newArrayList();
       List<Integer> right = Lists.newArrayList();
       //Greedy matching
       FlowEdge[]  stEdges = new FlowEdge[N];
       
       FlowEdge[][] leftRightEdges = new FlowEdge[N][N];
       
       boolean[] matched = new boolean[N];
       
       List<Pair<Integer,Integer>> greedyMatch = Lists.newArrayList();
       
       int vertexCount  = 0;
       
       for(int gcIdx = 0; gcIdx < in.grid.getSize(); ++gcIdx)
       {
           char curCh = in.grid.getEntry(gcIdx);
           if (curCh == 'x')
               continue;
           
           ++vertexCount;
         //  graph.addNode(gcIdx);
          // uGraph.addNode(gcIdx);
           
           int col = in.grid.getRowCol(gcIdx)[1];
           
           if (col % 2 == 0) {
               left.add(gcIdx);
               FlowEdge fe = new FlowEdge(s, gcIdx, 1);
               stEdges[gcIdx] = fe;
               fn.addEdge(fe);
           } else {
               right.add(gcIdx);
               FlowEdge fe = new FlowEdge(gcIdx, t, 1);
               stEdges[gcIdx] = fe;
               fn.addEdge(fe);
           }
           
           for(Direction dir : dirs) {
               Integer adjIdx = in.grid.getIndex(gcIdx,dir);
               
               if (adjIdx == null)
                   continue;
               
               char ch = in.grid.getEntry(adjIdx);
               
               if (ch == 'x')
                   continue;
               
             
               
               //Even though only this node can see the other paper, in terms of 
               //a configuration, both directions mean cheating can occur
               
            //   graph.addConnection(gcIdx, adjIdx);
               
            //   uGraph.addNode(adjIdx);
            //   uGraph.addEdge(gcIdx,adjIdx);
       
               int leftIdx = col % 2 == 0 ? gcIdx : adjIdx;
               int rightIdx  = col % 2 == 1 ? gcIdx : adjIdx;
               FlowEdge fe = new FlowEdge(leftIdx, rightIdx, 1);
               
               leftRightEdges[leftIdx][rightIdx] = fe;
                
               fn.addEdge(fe);
               
               if (!matched[leftIdx] && !matched[rightIdx]) {
                   matched[leftIdx] = true;
                   matched[rightIdx] = true;
                   greedyMatch.add(new ImmutablePair<>(leftIdx,rightIdx));
               }
               
           }
       }
       
       for(Pair<Integer,Integer> matchEdge : greedyMatch) {
           int le = matchEdge.getLeft();
           int re = matchEdge.getRight();
           
           stEdges[le].addResidualFlowTo(le, 1d);
           leftRightEdges[le][re].addResidualFlowTo(re, 1d);
           stEdges[re].addResidualFlowTo(t, 1d);
       }
       
      // log.debug("Grid {}", in.grid);
       
      // log.debug("Graph {}", graph);
       //boolean b = Bipartite.getBipartite(graph,left,right);
       
      // log.debug("left {} right {}", left, right);
       //List<Pair<Integer,Integer>> matching = Bipartite.getMaxMatching(graph,left,right);
       
       /*UndirectedGraph<Integer> g = EdmondsMatching.maximumMatching(uGraph);
       int mSize = 0;
       for(int gcIdx = 0; gcIdx < in.grid.getSize(); ++gcIdx) 
       {
           if (g.nodeExists(gcIdx))
               mSize += g.edgesFrom(gcIdx).size();
       }
       mSize /= 2;
       */
       FordFulkerson ff = new FordFulkerson(fn,s,t);
       int mSize = DoubleMath.roundToInt(ff.value(), RoundingMode.HALF_EVEN);
       log.debug("Match size {} Flow {}",mSize,ff.value());
       
       
       
      // log.debug("Max matching {}", matching);
      // int indSet = graph.V() - matching.size();
       int indSet = vertexCount - mSize;
       
        return String.format("Case #%d: %d", in.testCase, indSet);
        
    }
}