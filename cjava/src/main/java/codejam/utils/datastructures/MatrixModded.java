package codejam.utils.datastructures;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

public class MatrixModded {

    public int[][] data;
    
    public MatrixModded(int rows, int cols) {
        data = new int[rows][cols];
    }
    
    static public int[][] multiply(int[][] m1, int[][] m2, int mod) {
        int m1Rows = m1.length;
        int m1Cols = m1[0].length;
        
        int m2Rows = m2.length;
        int m2Cols = m2[0].length;
        
        Preconditions.checkArgument(m1Cols == m2Rows);
        
        int[][] r = new int[m1Rows][m1Cols];
        
        for(int resultRow = 0; resultRow < m1Rows; ++resultRow) {
            for(int resultCol = 0; resultCol < m2Cols; ++resultCol) {
                long sum  = 0;
                for(int i = 0; i < m1Cols; ++i) {
                    sum += (long) m1[resultRow][i] * m2[i][resultCol];
                    sum %= mod;
                }
                
                r[resultRow][resultCol] = Ints.checkedCast(sum);
            }
        }
        
        return r;
    }

}
