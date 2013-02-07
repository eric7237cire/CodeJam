package codejam.y2008.round_beta.hexagon_game;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.AssignmentProblem;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
      
        super("D", 0,0);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.checkerPos = Lists.newArrayList();
        in.checkerWeight = Lists.newArrayList();
        
        
        String[] allPos = scanner.nextLine().split(" ");
        if (allPos.length < 3) {
            allPos = scanner.nextLine().split(" ");
        }
        for(String pos : allPos) {
            in.checkerPos.add(Integer.parseInt(pos));
        }
        
        String[] weights = scanner.nextLine().split(" ");
        for(String weight : weights) {
            in.checkerWeight.add(Integer.parseInt(weight));
        }

        return in;
    }

    int totalCheckers(int S) {
        int smallestRow = (S+1) / 2;
        return 2 * (sumFormula(S-1) - sumFormula(smallestRow-1)) + S;
    }
    
    int sumFormula(int n) {
        return n * (n+1) / 2;
    }
    
    void addDist(int n1, int n2, int d, int[][] dist) {
        dist[n1][n2] = d;
        dist[n2][n1] = d;
    }
    
    /**
     * 
     */
    int dist(PointInt p1, PointInt p2) {
        int diffY = Math.abs(p2.getY()-p1.getY());
        int diffX = Math.abs(p2.getX()-p1.getX());
        
       
        //Once we have changed rows, we no longer need to move any x
        if (diffY >= diffX) {
            return diffY;
        }
        
        //on same row, each move is 2x
        return diffY + (diffX - diffY) / 2;
    }
        
    @Override
    public String handleCase(InputData in)
    {
        
        int S = in.checkerPos.size();
        
        final int total = totalCheckers(S);
        
        int[][] dist = new int[total][total];
        
        for(int[] d2 : dist) {
            Arrays.fill(d2, Integer.MAX_VALUE);
        }
        
        /**
         * Walk 2 counters, from the beginning and end
         */
        int startNode = 0;
        int endNode = total - 1;
        
        PointInt[] coordNumber = new PointInt[total];
        
        
        int numRows = 2 * (S - (S+1) / 2) + 1;
        
        for(int row = 0; row <= numRows / 2; ++row) {
            int rowLen = (S+1) / 2 + row;
            
            int startX = S - rowLen;
            
           // log.debug("Starting row of len {}", rowLen);
            for(int i = 0; i < rowLen; ++i) {
               // log.debug("Start node {} end node {}", startNode, endNode);
                coordNumber[startNode] = new PointInt(startX+2*i, numRows-1-row);
                coordNumber[endNode] = new PointInt(startX+rowLen*2-2 - i*2, row);

 
                if (rowLen < S) {
                    //Above 1
                    addDist(endNode, endNode - rowLen, 1, dist);
                    
                    //Above 2
                    addDist(endNode, endNode - rowLen - 1, 1, dist);
                
                    //Below 1
                    addDist(startNode, startNode+rowLen, 1, dist);
                    
                    //Below 2
                    addDist(startNode, startNode+rowLen+1, 1, dist);
                }
                
                if (i > 0) {
                    //Add left
                    addDist(startNode, startNode - 1, 1, dist);
                    
                    //Add right
                    addDist(endNode, endNode + 1, 1, dist);
                }
                
                ++startNode;
                --endNode;
                
            }
        }
        
        for(int i = 0; i < total; ++i) {
            dist[i][i] = 0;
        }
          
        log.debug("Start distance");
        for(int k = 0; k < total; ++k)
        {
            for(int i = 0; i < total; ++i) 
            {
                for(int j = 0; j < total; ++j)
                {
                    long temp = (long) dist[i][k] + dist[k][j];  //This will optimize the code performance , by removing the redundant expression .
                    if (temp < dist[i][j]) {
                        dist[i][j] = (int) temp;
                    }
                }
                
            }
        }
        log.debug("End distance");
       
        for(int i = 0; i < total; ++i) 
        {
            for(int j = 0; j < total; ++j)
            {
                log.debug("Distance Node {} and {} = {} ; coords {} and {} dist = {}",
                        i+1, j+1, dist[i][j],
                        coordNumber[i], coordNumber[j],
                        dist(coordNumber[i], coordNumber[j]));
                Preconditions.checkState(dist[i][j] == dist(coordNumber[i], coordNumber[j]));
                
                
            }
            
        }
        
        List<Integer> diagOnePos = Lists.newArrayList();
        List<Integer> diagTwoPos = Lists.newArrayList();
        
        int diagOne = 0;
        int diagTwo = (S+1) / 2 - 1;
        
        for(int rowLen = (S+1) / 2; rowLen <= S; ++rowLen) {
            diagOnePos.add(diagOne);
            diagTwoPos.add(diagTwo);
            
            diagOne += rowLen + 1;
            diagTwo += rowLen;
        }
        
        int midPoint = diagOnePos.get(diagOnePos.size()-1);
        Preconditions.checkState(diagTwoPos.get(diagOnePos.size()-1) == midPoint);
        
        diagOne = midPoint;
        diagTwo = midPoint;
        
        for(int rowLen = S-1; rowLen >= (S+1) / 2; --rowLen) {
            diagOne += rowLen + 1;
            diagTwo += rowLen;
            
            diagOnePos.add(diagOne);
            diagTwoPos.add(diagTwo);
            
        }
        
        List<Integer> diagThreePos = Lists.newArrayList();
        for(int i = midPoint - S / 2; i <= midPoint + S / 2; ++i) {
            diagThreePos.add(i);
        }
        
        log.debug("diag 1 : {} diag 2 : {} diag 3 : {}", diagOnePos, diagTwoPos, diagThreePos);
        
        List<List<Integer>> diags = Lists.newArrayList();
        diags.add(diagOnePos); diags.add(diagTwoPos); diags.add(diagThreePos);
        
        int min = Integer.MAX_VALUE;
        for(List<Integer> diag : diags) {
            double[][] weight = new double[S][S];
            
            for(int posIdx = 0; posIdx < S; ++posIdx) {
                for(int diagPosIdx = 0; diagPosIdx < S; ++diagPosIdx) {
                    int pos = in.checkerPos.get(posIdx) - 1;
                    int diagPos = diag.get(diagPosIdx);
                    
                    weight[posIdx][diagPosIdx] = in.checkerWeight.get(posIdx) * dist[pos][diagPos];
                }
            }
            
            AssignmentProblem ap = new AssignmentProblem(weight);
            int minWeight = DoubleMath.roundToInt(ap.weight(), RoundingMode.HALF_EVEN);
            
            min = Math.min(minWeight, min);
        }
        
        
        
        return "Case #" + in.testCase + ": " + min;
    }

}