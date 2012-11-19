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
    
    public DynamicProgrammingLarge(Grid<Integer> grid) {
        this.grid = grid;
        
        paths = new ArrayList<>();
        pathToIndex = new HashMap<>();
        //log.info("Starting create all paths");
        createAllPaths(new ArrayList<Integer>(), grid.getRows(), grid.getCols());   
       // log.info("Done create all paths");
        
        memoize = new int[paths.size()][LETTER_MAX+1][grid.getCols()];
        for(int i= 0; i < paths.size(); ++i) {
            for(int j = 0; j <= LETTER_MAX; ++j) {
                for(int k = 0; k < grid.getCols(); ++k) {
                    memoize[i][j][k] = -1;
                }
            }
        }
        int totalPathNumber = IntMath.binomial(grid.getRows()+grid.getCols(), grid.getRows());
        Preconditions.checkState(totalPathNumber == paths.size());
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
        if (maxCharacter <= 0) {           
            return 0;
        }
        Preconditions.checkArgument(pathKey < paths.size() - 2);
        //Find all maximal points
        List<Integer> path = paths.get(pathKey);
           
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
        
        
        
        sum = solve(pathKey, maxCharacter - 1, grid.getCols() - 1);
       
        log.debug("Sum starting {} for path {} max char <= {}", sum, path, maxCharacter);
       
        
        int allSubSets = (1 << numMaxPoints) - 1; 
        
        for(int subSetsBitSet = 1; subSetsBitSet <= allSubSets; ++subSetsBitSet) {
            
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
            sum %= MOD;

            //log.debug("Intersection {} for path {} has sum {}, cumul is now {}", intersectedPath, path, subSum,sum);
        }
        
        if (sum >= MOD) {
            sum -= MOD;
        }
        if (sum < 0) {
            sum += MOD;
        }
        log.debug("Returning {} for path {} <= {}", sum, path, maxCharacter);
        
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
