package codejam.utils.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For 1 and 0
 *
 */
public class MatrixInt
{

    final static Logger log = LoggerFactory.getLogger(MatrixInt.class);
    
    public int[][] data;
    final int rows;
    final int cols;
    public MatrixInt(int r, int c) {
        data = new int[r][c];
        rows = r;
        cols = c;
    }

    
    //for only 1 and 0
    public void rowEchelonForm()
    {
        int activeRow = 0;
        for(int c = 0; c < cols; ++c) {
            
            int nonZeroRow = -1;
                        
            //Find an entry that is non zero
            for(int r = activeRow; r < rows; ++r) {
                if (data[r][c] != 0) {
                    nonZeroRow = r;
                    break;
                }
            }
            
            //Go to next column
            if (nonZeroRow < 0)
                continue;
            
            //Swap with first
            swapRows(activeRow, nonZeroRow);
            
            //Eliminate the rest
            for(int r = activeRow+1; r < rows; ++r) {
                if (data[r][c] != 0) {
                    addRow(activeRow, r, r);
                }
            }
            
            //Row is done with 000s followed by a 1, move on to next
            ++activeRow;
        }
        
    }
    
    public void reducedRowEchelonForm() {
        for (int r = rows -1 ; r >= 0; --r) {
            int firstNonZeroCol = -1;
            for(int c = 0; c < cols; ++c) {
                if (data[r][c] != 0) {
                    firstNonZeroCol = c;
                    break;
                }
            }
            
            if (firstNonZeroCol==-1)
                continue;
                
            for(int rowAbove = r - 1; rowAbove >= 0; --rowAbove) {
                if (data[rowAbove][firstNonZeroCol] != 0) {
                    addRow(r,rowAbove,rowAbove);
                }
            }
        }
    }
    
    public void debugPrint() {
        for(int r = 0; r < rows; ++r) {
            log.debug("{}", (Object)data[r]);
        }
    }
    
    public void getAns(Boolean[] ans) {
        nextRow:
        for(int r = 0; r < rows; ++r) {
            int colWithOne = -1;
            
            for(int c = 0; c < cols -1 ; ++c) {
                if (data[r][c] != 0 && colWithOne >= 0)
                    continue nextRow;
                
                if (data[r][c] != 0 && colWithOne < 0)
                    colWithOne = c;
                
                
            }
            
            if (colWithOne < 0)
                continue;
            
            ans[colWithOne] = data[r][cols-1] == 1; 
        }
    }
    
    void addRow(int r1, int r2, int destRow) {
        for(int c = 0; c < cols; ++c) {
            data[destRow][c] = data[r1][c] + data[r2][c];
            data[destRow][c] %= 2;
        }
    }
    
    void swapRows(int r1, int r2) {
        if (r1==r2)
            return;
        
        int[] temp = data[r1];
        data[r1] = data[r2];
        data[r2] = temp;
    }
}
