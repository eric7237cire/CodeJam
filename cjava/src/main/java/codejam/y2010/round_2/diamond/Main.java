package codejam.y2010.round_2.diamond;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;


public class Main implements TestCaseHandler<InputData>,
TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    
    @Override
    public String[] getDefaultInputFiles() {
    //    return new String[] { "sample.in" };
     //   return new String[] { "A-small-practice.in" };
        return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }
    
    @Override
    public String handleCase(InputData in) {

        int maxDimension = in.k * 2 - 1; 
        int diamondSize = in.k;
        int[][] a = in.startDiamond;
        
        boolean[] rowSymmetry = new boolean[maxDimension];
        
        for (int reflectionRow = 0; reflectionRow < maxDimension; reflectionRow++) {
            rowSymmetry[reflectionRow] = true;
            //for each line 
            for (int row = 0; row < maxDimension; row++) {
                
                /**
                 * if reflection line is like 7, row is 11 we want
                 * 11- 7 = 4  7 - 4 = row 3
                 * 
                 * 14 - 11 = 3
                 */
                int reflectedRow = (2 * reflectionRow - row);
                if (reflectedRow < 0 || reflectedRow >= maxDimension) continue;
                for (int col = 0; col < maxDimension; col++) {
                    if (a[row][col] >= 0 && a[reflectedRow][col] >= 0 && a[row][col] != a[reflectedRow][col]) {
                        rowSymmetry[reflectionRow] = false;
                        break;
                    }
                }
                if (!rowSymmetry[reflectionRow]) break;
            }
            //log.debug("rs[r] {}  
        }
        
       // System.out.println(Arrays.toString(rowSymmetry));
        
        boolean[] columnSymmetry = new boolean[maxDimension];
        for (int reflectionCol = 0; reflectionCol < maxDimension; reflectionCol++) {
            columnSymmetry[reflectionCol] = true;
            for (int row = 0; row < maxDimension; row++) {
                for (int col = 0; col < maxDimension; col++) {
                    int reflectedCol = (2 * reflectionCol - col);
                    if (reflectedCol < 0 || reflectedCol >= maxDimension) continue;
                    if (a[row][col] >= 0 && a[row][reflectedCol] >= 0 && a[row][col] != a[row][reflectedCol]) {
                        columnSymmetry[reflectionCol] = false;
                        break;
                    }
                }
                if (!columnSymmetry[reflectionCol]) break;
            }
        }

        //Center of diamond
        int curCenterRow = in.k-1;
        int curCenterCol = in.k-1;

        int res = Integer.MAX_VALUE;

        for (int row = 0; row < maxDimension; row++) 
        {
            for (int col = 0; col < maxDimension; col++) 
            {
                if (rowSymmetry[row] && columnSymmetry[col]) 
                {
            
                    //Every time we move the center, we must add to the dimension of the diamond
                    int dCheck = in.k+Math.abs(row - curCenterRow) +
                            Math.abs(col - curCenterCol);
                    
                    res = Math.min(res, dCheck);
                }
            }
        }

        //System.out.println(res);

        //The dimension squared is the number of elements, this calculation returns the #
        //of elements added
        res = (res * res) - (diamondSize * diamondSize);
        
        return ("Case #" + in.testCase + ": " + res);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
    
        InputData  input = new InputData(testCase);
        //read in diamond
        int n = scanner.nextInt();  //size of diamond
        
        //Number of lines
        int m = 2 * n - 1;
        int[][] a = new int[m][m];
        for (int[] ints : a) {
            Arrays.fill(ints, -1);
        }
        for (int i = 0; i < m; i++) {
            //height of diamond, find column number, goes from 1 to n back to 1
            int h = i < n ? i + 1 : 2 * n - 1 - i;
            
            //empty space ?
            int d = n - h; 
            for (int j = 0; j < h; j++) {
                int c = scanner.nextInt();
                //a space between each number
                a[i][d + j * 2] = c;
            }
        }
               
        
        input.startDiamond = a;
        input.k = n;
        
        return input;
        
    }

    
}