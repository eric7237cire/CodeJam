package com.eric.codejam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.utils.Grid;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

public class DynamicProgrammingLarge {
    
    final static Logger log = LoggerFactory.getLogger(DynamicProgrammingLarge.class);

    //A path goes from bottom left to top right, each step goes to the right one.
    //paths [ index of path ] [ row values ]
    //
    List<List<Integer>> paths; 
    Grid<Integer> grid;
    
    int[][][] memoize ;
    Map<List<Integer>, Integer> pathToIndex;
    
    final static int MOD = 10007;
    public static int LETTER_MAX = 26;
    
    //int[][][] check;
    
    public DynamicProgrammingLarge(Grid<Integer> grid) {
        this.grid = grid;
        
        paths = new ArrayList<>();
        pathToIndex = new HashMap<>();
        //log.info("Starting create all paths");
        createAllPaths(new ArrayList<Integer>(), grid.getRows(), grid.getCols());   
       // log.info("Done create all paths");
        
        memoize = new int[paths.size()][LETTER_MAX+1][grid.getCols()];
       // check = new int[paths.size()][LETTER_MAX+1][grid.getCols()];
        for(int i= 0; i < paths.size(); ++i) {
            for(int j = 0; j <= LETTER_MAX; ++j) {
                for(int k = 0; k < grid.getCols(); ++k) {
                    memoize[i][j][k] = -1;
                  //  check[i][j][k] = -1;
                }
            }
        }
        int totalPathNumber = IntMath.binomial(grid.getRows()+grid.getCols(), grid.getRows());
        Preconditions.checkState(totalPathNumber == paths.size());
        
        //2 2
        /*
        check[0][4][1] = 30;
        //2 1
        check[1][4][0] = 20;
        check[1][4][1] = 30;
        check[1][3][1] = 14;
        check[1][3][0] = 8;
        //b
        check[1][2][1] = 5;
        check[1][2][0] = 2;
        //a
        check[1][1][1] = 1;
      //  check[1][1][0] = 0;
        //2 0
        check[2][4][1] = 10;
        check[2][3][1] = 6;
        check[2][3][0] = 6;
        check[2][2][1] = 3;
        check[2][2][0] = 3;*/
    }
    
    
    /*
     * dp[P][c][k] := the number of ways one can fill all the squares above the path P,
     *  using only the letters no greater than c, and the letter c does not occur anywhere
     *   after column k,so that the upper part is doubly sorted, and any pre-filled letter
     *    in the upper part is respected.
     */
    public int solve(int pathKey, int maxCharacter, int colLimit) {
        
        if (memoize[pathKey][maxCharacter][colLimit] >= 0) {
            return memoize[pathKey][maxCharacter][colLimit];
        }
        
        if (pathKey == paths.size() - 2) {
            int letter = grid.getEntry(0);
            int sum = 0;
            if (maxCharacter < letter) {
                sum = 0;
            } else if ( letter == 0) {
                sum = maxCharacter;
            } else {
                sum = 1;
            }
            //checkPath(pathKey, maxCharacter, sum);
            sum %= MOD;
            memoize[pathKey][maxCharacter][colLimit] = sum;
            return sum;
        }
        if (pathKey == paths.size() - 1) {
            int sum = 0;
            memoize[pathKey][maxCharacter][colLimit] = sum;
            //checkPath(pathKey, maxCharacter, sum);
            return 0;
        }
        if (colLimit == 0 && maxCharacter == 1) {
            //return 0;
        }
        if (maxCharacter <= 0) {           
            return 0;
        }
        Preconditions.checkArgument(pathKey < paths.size() - 2);
        //Find all maximal points
        final List<Integer> path = paths.get(pathKey);
           
        List<Integer> maxPointColumns = new ArrayList<>(10);
        
        //Basically when the path has a corner
        int numMaxPoints = 0;
        
       // int maxColumn = 0;
        int sum = 0;
                
        for(int pathRowIdx = 0; pathRowIdx <= colLimit; ++pathRowIdx ) {
            //path row is one after the row
            int currentPathRow = path.get(pathRowIdx);
            int nextPathRow = pathRowIdx == grid.getCols() - 1 ? 0 : path.get(pathRowIdx+1);
            
            Preconditions.checkState(nextPathRow <= currentPathRow);
            
            if (nextPathRow < currentPathRow) {
                
                int letter = grid.getEntry(currentPathRow-1, pathRowIdx);
                
                if (letter > maxCharacter) {
                    log.debug("Invalid letter ");
                    sum = 0;
                    memoize[pathKey][maxCharacter][colLimit] = sum;
                    //checkPath(pathKey, maxCharacter, sum);
                    return 0;
                }
                
               
                if (letter == 0 || letter == maxCharacter) {
                    // We have found a maximal point. Row = currentPathRow, Col
                    // = pathRowIdx
                    ++numMaxPoints;
                    maxPointColumns.add(pathRowIdx);
                    
                 //   maxColumn = pathRowIdx;
                }
            }
            
        }
        
        
        if (colLimit == 0 ) {
            sum = solve(pathKey, maxCharacter - 1 , grid.getCols() - 1 );
//            return sum;
        } else {
        sum = solve(pathKey, maxCharacter , colLimit - 1);
        }
        
        log.debug("Sum starting {} for path {} max char <= {} col limit {} numMaxPoints {}", sum, path, 
                maxCharacter, colLimit, numMaxPoints);
       
        
        if (numMaxPoints > 0 && maxPointColumns.get(numMaxPoints-1) == colLimit) {
        int allSubSets = (1 << numMaxPoints) - 1;
        allSubSets &= (1 << (numMaxPoints-1));
        
        for(int subSetsBitSet = allSubSets; subSetsBitSet <= allSubSets; ++subSetsBitSet) {
            
            //Inclusion / exclusion Principal 
            int addOrSub = -1;
                        
            List<Integer> intersectedPath = new ArrayList<>(path);
            
            
            
            //improve
            for(int j = 0; j < numMaxPoints; ++j ) {
                if ( ((1 << j) & subSetsBitSet) > 0) {                    
                    int maxPointCol = maxPointColumns.get(j);
       //             maxColumn = maxPointCol;
                    addOrSub *= -1;
                    intersectedPath.set(maxPointCol, intersectedPath.get(maxPointCol) - 1);
                }
            }
                                   
            int intPathIndex = pathToIndex.get(intersectedPath);            
          
            
            int subSum = solve(intPathIndex, maxCharacter, colLimit);
            
            sum += addOrSub * subSum;
            log.debug("Adding for path {} intersecting path {} w/ size {} <= {}.  col limit {}.  sum = {}", 
                    path, intersectedPath, subSum, maxCharacter, colLimit, sum);
            sum %= MOD;

            //log.debug("Intersection {} for path {} has sum {}, cumul is now {}", intersectedPath, path, subSum,sum);
        }
        }
        
        if (sum >= MOD) {
            sum -= MOD;
        }
        if (sum < 0) {
            sum += MOD;
        }
        log.debug("Returning {} for path {} <= {}.  col limit {}", sum, path, maxCharacter, colLimit);
        
        Grid<Integer> testGrid = new Grid<Integer>(this.grid);
        testGrid.addRow(0);
        for(int pathRowIdx = colLimit+1; pathRowIdx < grid.getCols(); ++pathRowIdx ) {
            int currentPathRow = path.get(pathRowIdx);
            for (int r = currentPathRow + 1; r < testGrid.getRows(); ++r) {
                testGrid.setEntry(r, pathRowIdx, maxCharacter == 1 ? 1 : maxCharacter-1);
            }
        }
        
        
        
        memoize[pathKey][maxCharacter][colLimit] = sum;
       
        return sum;
    }
    
    /*
    public void checkPath(int pathKey, int maxLetter, int sum) {
        Grid<Integer> checkGrid = new Grid<Integer>(grid);
        checkGrid.addRow(maxLetter);
        List<Integer> path = paths.get(pathKey);
        for(int pathRowIdx = 0; pathRowIdx < grid.getCols(); ++pathRowIdx ) {
            //path row is one after the row
            int currentPathRow = path.get(pathRowIdx);
            
            for(int r = currentPathRow; r < checkGrid.getRows(); ++ r) {
                checkGrid.setEntry(r,pathRowIdx, maxLetter);
            }
        }
        
        int count = BruteForce.count(checkGrid, checkGrid);
        Preconditions.checkState(count == sum);
    }*/
    
    
    
    public static Integer solveGrid(Grid<Integer> grid) {

        DynamicProgrammingLarge ss = new DynamicProgrammingLarge(grid);
        
        return ss.solve(0, LETTER_MAX, grid.getCols() - 1);

    }
    
    private void createAllPaths(List<Integer> pathSoFar, int beforeRowValue, int cols) {
        if (pathSoFar.size() == cols) {
            log.debug("Path {} : {}", paths.size(), pathSoFar);
            paths.add(new ArrayList<Integer>(pathSoFar));
            pathToIndex.put(paths.get(paths.size()-1), paths.size()-1);
            return;
        }
        
        for(int i = beforeRowValue; i >= 0; --i) {
            pathSoFar.add(i);
            createAllPaths(pathSoFar, i, cols);
            pathSoFar.remove(pathSoFar.size() -1);
        }
    }
}
