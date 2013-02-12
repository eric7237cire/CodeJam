package codejam.y2011.round_3.perpetual_motion;

import java.util.Scanner;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

import codejam.utils.datastructures.graph.GraphInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.GridChar;
import codejam.utils.utils.LargeNumberUtils;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {


    public Main()
    {
        super("C", 1, 1, 0);
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
        
        in.R = scanner.nextInt();
        in.C = scanner.nextInt();
       
        in.floor = GridChar.buildFromScanner(scanner, in.R, in.C, ' ');
        return in;
    }

    private void printGraph(GraphInt g, int nSquares, GridChar floor) {
        for(int i = 0; i < nSquares; ++i)
        {
            
            
            int[] rc = floor.getRowCol(i);
            
            if (!g.nodeExists(i)) {
                log.debug("Square r:{} c:{} removed", rc[0]+1,rc[1]+1);
                continue;
            }
            
            Set<Integer> adjList = g.getNeighbors(i);
            log.debug("Square r:{} c:{} neighbors : ", rc[0]+1,rc[1]+1);
            
            for(int j : adjList) {
                int[] rcAdj = floor.getRowCol(j - nSquares);
                
                log.debug("neighbor square r:{} c:{} ", rcAdj[0]+1,rcAdj[1]+1);
            }
        }
    }
    
    /**
     * true -- removed one
     * false -- degree of zero found
     * 
     * null nothing changed
     */
    Boolean removeSingleDegreeEndNodes(GraphInt g, int nSquares, InputData in) {
        for (int endNode = nSquares; endNode < 2 * nSquares; ++endNode)
        {
            int[] rcAdj = in.floor.getRowCol(endNode - nSquares);
            
            if (!g.nodeExists(endNode))
                continue;
            
            if (g.getDegree(endNode) == 0)
            {
                return false;
            }
            
           // log.debug("Degree of end node r:{} c:{}  is {}", rcAdj[0] + 1, rcAdj[1] + 1, g.getDegree(endNode));

            Set<Integer> adjNodes = g.getNeighbors(endNode);

            if (adjNodes.size() == 1)
            {
                //No choice but to direct the start node to this end node
                int startNode = adjNodes.iterator().next();

                int[] rc = in.floor.getRowCol(startNode);
                

                log.debug("Belt at square row:{} col:{} must go to row:{} col:{}", rc[0] + 1, rc[1] + 1, rcAdj[0] + 1, rcAdj[1] + 1);

                g.removeNode(startNode);
                g.removeNode(endNode);
                
               // printGraph(g, nSquares, in.floor);

                return true;
            }
        }
        
        return null;
    }
    
    boolean removeEvenDegreeEndNode(GraphInt g, int nSquares, InputData in) {
        for (int endNode = nSquares; endNode < 2 * nSquares; ++endNode)
        {
            int[] rcAdj = in.floor.getRowCol(endNode - nSquares);
            
            if (!g.nodeExists(endNode))
                continue;
            
            Preconditions.checkState (g.getDegree(endNode) == 2);
            
           // log.debug("Degree of end node r:{} c:{}  is {}", rcAdj[0] + 1, rcAdj[1] + 1, g.getDegree(endNode));

            Set<Integer> adjNodes = g.getNeighbors(endNode);

            Preconditions.checkState(adjNodes.size() == 2);
            
            //Pick one
            
            //No choice but to direct the start node to this end node
            int startNode = adjNodes.iterator().next();

            int[] rc = in.floor.getRowCol(startNode);
            

            log.debug("Break cycle at square row:{} col:{} assign to row:{} col:{}", rc[0] + 1, rc[1] + 1, rcAdj[0] + 1, rcAdj[1] + 1);

            g.removeNode(startNode);
            g.removeNode(endNode);
            
           // printGraph(g, nSquares, in.floor);

            return true;
        
        }
        
        return false;
    }
  
    @Override
    public String handleCase(InputData in) {
       
        GraphInt g = new GraphInt();
        
        int nSquares = in.C * in.R;
        //Start nodes have id 0 -> nSquares - 1
        
        for(int r = 0; r < in.R; ++r) {
            for(int c = 0; c < in.C; ++c) {
                char sq = in.floor.getEntry(r,c);
                
                int nextR1 = r;
                int nextC1 = c;
                int nextR2 = r;
                int nextC2 = c;
                
                if (sq == '|') {
                    nextR1 = (in.R + r - 1) % in.R;
                    nextR2 = (r + 1) % in.R;
                    nextC1 = c;
                    nextC2 = c;
                } 
                if (sq == '/') {
                    nextR1 = (in.R + r - 1) % in.R;
                    nextC1 = (c+1) % in.C;
                    
                    nextR2 = (r + 1) % in.R;           
                    nextC2 = (in.C + c-1) % in.C;
                }
                if (sq == '-') {
                    nextR1 = r ;
                    nextC1 = (c+1) % in.C;
                    
                    nextR2 = r ;                    
                    nextC2 = (in.C + c-1) % in.C;
                }
                if (sq == '\\') {
                    nextR1 = (in.R + r - 1) % in.R;
                    nextC1 = (in.C + c-1) % in.C;
                    
                    nextR2 = (r + 1) % in.R;           
                    nextC2 = (c+1) % in.C;
                }
                
                int curIndex = in.floor.getIndex(r,c);
                
                int nextIndex = nSquares + in.floor.getIndex(nextR1,nextC1);
                int next2Index = nSquares + in.floor.getIndex(nextR2,nextC2);
                
                g.addConnection(curIndex, nextIndex);
                g.addConnection(curIndex, next2Index);
                
                //Make sure all end nodes exist
                g.addNode(curIndex + nSquares);
            }
        }
        
        boolean allEndNodesAreConnected = true;
        
        phase1: while (allEndNodesAreConnected == true)
        {
            Boolean r = removeSingleDegreeEndNodes(g, nSquares, in);
            
            if (r == null)
                break;

            allEndNodesAreConnected = r;

        }
        
        if (!allEndNodesAreConnected) {
            return String.format("Case #%d: 0", in.testCase);
        }
        
        int cyclesFound = 0;
        
        while(removeEvenDegreeEndNode(g, nSquares, in)) {
            
            while(removeSingleDegreeEndNodes(g, nSquares, in) != null) {};
            
            cyclesFound++;
        }
        
        int mod = 1000003;
        
       //printGraph(g, nSquares, in.floor);
        int count = (int) LargeNumberUtils.pow(2, cyclesFound, mod); //IntMath.pow(2, cyclesFound);

        return String.format("Case #%d: %d", in.testCase, count);
    }
}