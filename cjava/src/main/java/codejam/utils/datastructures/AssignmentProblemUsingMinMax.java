package codejam.utils.datastructures;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.graph.MincostMaxflowLong;

/*************************************************************************
 *  Compilation:  javac AssignmentProblem.java
 *  Execution:    java AssignmentProblem N
 *  Dependencies: DijkstraSP.java DirectedEdge.java
 *
 *  Solve an N-by-N assignment problem in N^3 log N time using the
 *  successive shortest path algorithm.
 *
 *  Remark: could use dense version of Dijsktra's algorithm for
 *  improved theoretical efficiency of N^3, but it doesn't seem to
 *  help in practice.
 *
 *  Assumes N-by-N cost matrix is nonnegative.
 *
 *
 *********************************************************************/

public class AssignmentProblemUsingMinMax {
    
 
    public static long doAssignmentProblemUsingMinMax(long[][] weight) {
        
        MincostMaxflowLong mm = new MincostMaxflowLong();
        
        int N = weight.length;
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                //Flow edges from row to column nodes
                mm.addArc(i, j+N, 1, weight[i][j]);
            }
        }
        
        int s = 2 * N;
        int t = s + 1;
        //We have nRows of flow going to the row nodes
        //and nCols (==nRows) of flow goint to sink
        for(int i = 0; i < N; ++i)
        {
            mm.addArc(s, i, 1, 0);
            mm.addArc(i+N, t, 1, 0);
        }

        Pair<Long,Long> flowCost = mm.getFlow(s,t);
        
        return flowCost.getRight();

    }

  

}