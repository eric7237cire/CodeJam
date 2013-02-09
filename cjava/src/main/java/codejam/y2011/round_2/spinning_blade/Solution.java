package codejam.y2011.round_2.spinning_blade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Solution
{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    

    static class  Summatron {
        
        int[][] t;
        int[][] sum;
        
        Summatron(int rows, int cols) {            
            t = new int[rows+1][cols+1];
            sum = new int[rows+1][cols+1];
        }
        
        /**
         * Once input is done, find the sums
         */
        void init(int rows, int cols) {
            
            for (int i = 1; i <= rows; ++i)
            {
                for (int j = 1; j <= cols; ++j) {
                    sum[i][j] = t[i][j] + sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1];
                }
            }
        }

        int rectangleSum(int left, int r, int u, int d) {
            /**
             * Say we have
             * 
             * 00lr 
             * 1122  0   (rows numbered 1 for topmost)
             * 1122  u - 1
             * 3344  u
             * 3344  d
             * 
             * 
             * and we want 22
             *             22
             *             
             */
            return sum[d][r] // 1 2 3 4
                    - sum[d][left - 1] // 1 3   
                    - sum[u - 1][r]  //1 2
                    + sum[u - 1][left - 1]; // 1
        }

        int bladeSum(int l, int r, int topRow, int bottomRow) {
           
            //Minus the corners
            return rectangleSum(l, r, topRow, bottomRow)   
                    - t[bottomRow][l]   
                    - t[bottomRow][r]  
                    - t[topRow][l] 
                    - t[topRow][r];
        }
    };

    

    public int solve(InputData in) {
    
        Summatron classic, sumX, sumY;
        int rows, cols;
        rows = in.R;
        cols = in.C;
        
        classic = new Summatron(rows, cols);
        sumX = new Summatron(rows, cols);
        sumY = new Summatron(rows, cols);
        
        for (int i = 1; i <= rows; ++i) {
            for (int j = 1; j <= cols; ++j) {
                int value = in.cells.getEntry(i-1,j-1);
        
                classic.t[i][j] = value;
                
                sumX.t[i][j] = j * value;
                sumY.t[i][j] = i * value;
            }
        }
        classic.init(in.R, in.C);
        sumX.init(in.R, in.C);
        sumY.init(in.R, in.C);
       
        for (int size = Math.min(rows, cols); size >= 3; --size) {
            for (int topRow = 1; topRow + size - 1 <= rows; ++topRow) {
            for (int leftCol = 1; leftCol + size - 1 <= cols; ++leftCol) {
                
                    
                    int r = leftCol + size - 1;
                    int d = topRow + size - 1;
                    
                    //weight must be even
                    int cSum = classic.bladeSum(leftCol, r, topRow, d);
                    if ((cSum * (size - 1)) % 2 != 0)
                        continue;
                    int cSumX = sumX.bladeSum(leftCol, r, topRow, d) - (cSum * (2 * leftCol + (size) - 1)) / 2;
                    int cSumY = sumY.bladeSum(leftCol, r, topRow, d) - (cSum * (2 * topRow + (size) - 1)) / 2;
                    
                    int centerX = (leftCol - 1 + leftCol - 1 + size - 1) ; // /2d
                    int centerY = (topRow - 1 + topRow - 1 + size - 1) ;   // / 2d
                    int massBlade = cSum;
                    
                   // long centerMassX = 2 * sumX.bladeSum(leftCol, r, topRow, d)  / massBlade;
                   // long centerMassY = 2 * sumY.bladeSum(leftCol, r, topRow, d) / massBlade;
                    
                   // log.debug("sol 2 top Row {} left col {} size {} mass blade {} Center x {} y {} center mass x{} y{}",
                       //     topRow, leftCol, size, massBlade, centerX, centerY, centerMassX, centerMassY);
                    /*
                    log.debug("Sol top Row {} left col {} size {} Center x {} y {} center mass x {} y {}",
                            topRow, leftCol, size, cSumX, cSumY, sumX.bladeSum(leftCol, r, topRow, d),
                            sumY.bladeSum(leftCol, r, topRow, d));
                    */
                    
                    if (cSumX == 0 && cSumY == 0) {
                        
                        return size;
                        
                    }
                }
            }
        }
       
        
        return 0;
    }

}
