package codejam.y2009.round_1C.bribe_prisoner;

import java.util.List;

import codejam.utils.multithread.Consumer.TestCaseHandler;

public class DynamicBottomUp implements TestCaseHandler<InputData>
{

    @Override
    public String handleCase(InputData in)
    {
        int P, Q;
        P = in.numCells;
        Q = in.numToBeFreed;

        List<Integer> cells = in.listToBeFree;
        
        cells.add(0, 0);
        cells.add(P + 1);

        int[][] mat = new int[Q+2][Q+2];
        //mat[0][2] seems like if prisoner index 1 is left, the cost
        //so mat[1][3] means  prisoner 1 and 3 are freed, cost of freeing prisoner 2
        //cout << "Case " << it << endl;
        
        //Calculate gaps 
        for(int d = 2; d < Q + 2; ++d)
        {
            for(int i = 0; i < Q + 2 - d; ++i)
            {
                int j = i + d;
                int bcost = Integer.MAX_VALUE;
                for(int k = i + 1; k < j; ++k)
                {
                    bcost = Math.min(bcost,
                            mat[i][k] + mat[k][j]
                                    + cells.get(j) - cells.get(i) - 2);
                }
                mat[i][j] = bcost;
                //cout << i << ' ' << j << ": " << bcost << endl;
            }
        }

        return String.format("Case #%d: %d", in.testCase, mat[0][Q + 1]);
    }

    
// 
    // 0  1   2   3      4
    // 0  3   6   14    21
    /*
    First gap of 2, which is really a prisoner by himself, cost is just cells between previous and next
    mat [0][2] = 4 (cost of freeing 3 if 0 and 6 already done ;  1 2  -  4 5 == 4
    mat [1][3] = 9 (cost of freeing 6 if 3 and 14 already done ;  4 5 - 7  8 9 10   11 12  13 == 9
    mat [2][4] = 21-6 - 2 == 13
     
    
    mat [0][3] = minimum of freeing 6 first; costing 12, then 3 ; costing 4  == 16
               or           freeing 3 first, costing 12, then 6 ; costing 9
     cost of freeing 3 then 0 2
     k is 1 or 2
     */
}
