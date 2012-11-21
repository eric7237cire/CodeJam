package com.eric.codejam;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    static int[][][] memoize ;
    Map<List<Integer>, Integer> pathToIndex;
    
    final static int MOD = 10007;
    public static int LETTER_MAX = 26;
    
 
    static {
        //M+N choose M  M rows N cols
        memoize = new int[184756][LETTER_MAX+1][10];
    }
    public DynamicProgrammingLarge(Grid<Integer> grid) {
        this.grid = grid;
        
        paths = new ArrayList<>(IntMath.binomial(grid.getRows()+grid.getCols(),grid.getRows()));
        pathToIndex = new HashMap<>(IntMath.binomial(grid.getRows()+grid.getCols(),grid.getRows()));
        //log.info("Starting create all paths");
        createAllPaths(new ArrayList<Integer>(), grid.getRows(), grid.getCols());   
       // log.info("Done create all paths");
        
        
        for(int i= 0; i < paths.size(); ++i) {
            for(int j = 0; j <= LETTER_MAX; ++j) {
                Arrays.fill(memoize[i][j], -1);
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
            
            memoize[pathKey][maxCharacter][colLimit] = sum;
            return sum;
        }
        if (pathKey == paths.size() - 1) {
            int sum = 0;
            memoize[pathKey][maxCharacter][colLimit] = sum;
            
            return 0;
        }

        if (maxCharacter <= 0) {
            return 0;
        }
        Preconditions.checkArgument(pathKey < paths.size() - 2);
        // Find all maximal points
        final List<Integer> path = paths.get(pathKey);

        // Basically when the path has a corner
        boolean isMaxPoint = false;

        int sum = 0;

        
        // path row is one after the row
        int currentPathRow = path.get(colLimit);
        int nextPathRow = colLimit == grid.getCols() - 1 ? 0 : path
                .get(colLimit + 1);

        Preconditions.checkState(nextPathRow <= currentPathRow);

        /*
         * Find a maximal point either equal to maxCharacter or a variable.
         * The other letters are counted below.
         */
        if (nextPathRow < currentPathRow) {

            int letter = grid.getEntry(currentPathRow - 1, colLimit);

            if (letter > maxCharacter) {
                log.debug("Invalid letter ");
                sum = 0;
                memoize[pathKey][maxCharacter][colLimit] = sum;
                
                return 0;
            }

            if (letter == 0 || letter == maxCharacter) {
                // We have found a maximal point. Row = currentPathRow, Col
                // = colLimit
                isMaxPoint = true;
            }
        }

        //Count previous letters
        if (colLimit == 0) {
            sum = solve(pathKey, maxCharacter - 1, grid.getCols() - 1);
        } else {
            sum = solve(pathKey, maxCharacter, colLimit - 1);
        }
        
        log.debug("Sum starting {} for path {} max char <= {} col limit {} ", sum, path, 
                maxCharacter, colLimit);
       
        //Only max point is considered, because if there is not a max point, then
        //solve(pathKey, maxCharacter, colLimit + X would have counted that case.
        if (isMaxPoint) {
        
            List<Integer> intersectedPath = new ArrayList<>(path);

            intersectedPath.set(colLimit, intersectedPath.get(colLimit) - 1);

            int intPathIndex = pathToIndex.get(intersectedPath);
            
            int subSum = solve(intPathIndex, maxCharacter, colLimit);
            
            sum += subSum;
            log.debug("Adding for path {} intersecting path {} w/ size {} <= {}.  col limit {}.  sum = {}", 
                    path, intersectedPath, subSum, maxCharacter, colLimit, sum);
            sum %= MOD;

            //log.debug("Intersection {} for path {} has sum {}, cumul is now {}", intersectedPath, path, subSum,sum);
        
        }
        
        if (sum >= MOD) {
            sum -= MOD;
        }
        if (sum < 0) {
            sum += MOD;
        }
        log.debug("Returning {} for path {} <= {}.  col limit {}", sum, path, maxCharacter, colLimit);
         
        memoize[pathKey][maxCharacter][colLimit] = sum;
       
        return sum;
    }
   
    
    
    public static Integer solveGrid(Grid<Integer> grid) {

        DynamicProgrammingLarge ss = new DynamicProgrammingLarge(grid);
        
        return ss.solve(0, LETTER_MAX, grid.getCols() - 1);

    }
    
    /**
     * Starts at top left.  ends above top right
        ~
     ****
     ****
     ****
     ~
     
     3-2-1-0 
     
     Creates first 3 3 3 3 ; 3 3 3 2 ; etc
      
     * @param pathSoFar
     * @param afterRowValue
     * @param cols
     */
    private void createAllPaths(List<Integer> pathSoFar, int afterRowValue, int cols) {
        if (pathSoFar.size() == cols) {
            log.debug("Path {} : {}", paths.size(), pathSoFar);
            paths.add(new ArrayList<Integer>(pathSoFar));
            pathToIndex.put(paths.get(paths.size()-1), paths.size()-1);
            return;
        }
        
        for(int i = afterRowValue; i >= 0; --i) {
            pathSoFar.add(i);
            createAllPaths(pathSoFar, i, cols);
            pathSoFar.remove(pathSoFar.size() -1);
        }
    }
}
