package codejam.y2009.round_final.double_sort_grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.utils.Grid;


public class DynamicProgrammingLarge {
    
    private static class Memoize extends ThreadLocal<int[][][]> {

        @Override
        protected int[][][] initialValue() {
            log.info("InitialValue");
            return new int[184756][LETTER_MAX][10];
        }
        
    }
    
    final static Logger log = LoggerFactory.getLogger(DynamicProgrammingLarge.class);

    //A path goes from bottom left to top right, each step goes to the right one.
    //paths [ index of path ] [ row values ]
    //
    static int[][] paths;
    //Path without max point at column or -1
    //[pathKey][Column]
    static int[][] nextPath;
    static int pathCount;
    int[][] grid;
    
    static Memoize memoizeTL;
    static Map<List<Integer>, Integer> pathToIndex;
    
    final static int MOD = 10007;
    public static int LETTER_MAX = 26;
    
    final static int MAX_ROWS = 10;
    final static int MAX_COLS = 10;
 
    static {
        //M+N choose M  M rows N cols

        memoizeTL = new Memoize();
        //paths[index][col] = row   A path goes from bottom left to top right 
        paths = new int[ 184756 ][MAX_COLS];
                
        nextPath = new int [ 184756 ][MAX_COLS];
        pathToIndex = new HashMap<>(184756);
        
        createAllPaths(new ArrayList<Integer>(), MAX_ROWS, MAX_COLS);
        
        buildNextPaths();
        
        pathToIndex = null;
    }
    public DynamicProgrammingLarge(Grid<Integer> grid) {
        //Fill in rest of grid with z to be able to reuse maxpath / lattice stuff
        this.grid = new int[MAX_ROWS][MAX_COLS]; //rid.buildEmptyGrid(MAX_ROWS, MAX_COLS, LETTER_MAX-1);
        
        for(int[] data : this.grid) {
            Arrays.fill(data, LETTER_MAX-1);
        }
        
        for(int r = 0; r < grid.getRows(); ++r) {
            for(int c = 0; c < grid.getCols(); ++c) {
                this.grid[r][c] = grid.getEntry(r,c);
            }
        }

        //log.info("Starting create all paths");
           
       // log.info("Done create all paths");
        
        int[][][] memoize = memoizeTL.get();
        for(int i= 0; i < pathCount; ++i) {
            for(int j = 0; j < LETTER_MAX; ++j) {
                Arrays.fill(memoize[i][j], -1);
            }
        }
     
    }
    
    
    /*
     * dp[P][c][k] := the number of ways one can fill all the squares above the path P,
     *  using only the letters no greater than c, and the letter c does not occur anywhere
     *   after column k,so that the upper part is doubly sorted, and any pre-filled letter
     *    in the upper part is respected.
     */
    public int solve(int pathKey, int maxCharacter, int colLimit) {
        
        int[][][] memoize = memoizeTL.get();
        
        if (maxCharacter < 0) {
            return 0;
        }
        
        if (memoize[pathKey][maxCharacter][colLimit] >= 0) {
            return memoize[pathKey][maxCharacter][colLimit];
        }
        
        /**
         * The lattice path only containing the top left corner
         */
        if (pathKey == pathCount - 2) {
            int letter = grid[0][0];
            int sum = 0;
            //Is it a free character ?
            if ( letter == LETTER_MAX) {
                sum = maxCharacter+1;
            } else
            if (maxCharacter < letter) {
                sum = 0;
            } else {
                sum = 1;
            }
            
            memoize[pathKey][maxCharacter][colLimit] = sum;
            return sum;
        }
        
        //Preconditions.checkArgument(pathKey < pathCount - 2);
        
        // Find all maximal points
        final int[] path = paths[pathKey];

        // Basically when the path has a corner
        boolean isMaxPoint = false;

        int sum = 0;

        
        //The next monotone path not including the max point at colLimit
        int nextPathKey = nextPath[pathKey][colLimit];
        
        int currentPathRow = path[colLimit];
        
        /*
         * Find a maximal point either equal to maxCharacter or a variable.
         * The other letters are counted below.
         */
        if (nextPathKey >= 0) {

            int letter = grid[currentPathRow - 1][colLimit];

            if (letter > maxCharacter && letter != LETTER_MAX) {
            //    log.debug("Invalid letter ");
                sum = 0;
                memoize[pathKey][maxCharacter][colLimit] = sum;                
                return 0;
            }

            if (letter == LETTER_MAX || letter == maxCharacter) {
                // We have found a maximal point. 
                isMaxPoint = true;
            }
        }

        //Count previous letters
        if (colLimit == 0) {
            sum = solve(pathKey, maxCharacter - 1, MAX_COLS - 1);
        } else {
            sum = solve(pathKey, maxCharacter, colLimit - 1);
        }
        
       
        //Only max point is considered, because if there is not a max point, then
        //solve(pathKey, maxCharacter, colLimit + X would have counted that case.
        if (isMaxPoint) {
            int subSum = solve(nextPathKey, maxCharacter, colLimit);

            sum += subSum;
            
            if (sum >= MOD) {
                sum -= MOD;
            }
        }
        
        memoize[pathKey][maxCharacter][colLimit] = sum;
       
        return sum;
    }
   
    
    
    public static Integer solveGrid(Grid<Integer> grid) {

        DynamicProgrammingLarge ss = new DynamicProgrammingLarge(grid);
        
        return ss.solve(0, LETTER_MAX-1, MAX_COLS-1);

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
    private static void createAllPaths(List<Integer> pathSoFar,
            int afterRowValue, int cols) {
        if (pathSoFar.size() == cols) {
            // log.debug("Path {} : {}", paths.size(), pathSoFar);
            for (int c = 0; c < MAX_COLS; ++c)
            {
                paths[pathCount][c] = pathSoFar.get(c);             
            }
            pathToIndex.put(new ArrayList<Integer>(pathSoFar), pathCount);
            pathCount++;

            return;
        }

        for (int i = afterRowValue; i >= 0; --i) {
            pathSoFar.add(i);
            createAllPaths(pathSoFar, i, cols);
            pathSoFar.remove(pathSoFar.size() - 1);
        }
    }
    
    /**
     * BUild up a structure for finding the index of a lattice path
     * not including a maximum point (a "corner" in the path)
     */
    private static void buildNextPaths() {
        
    
        for(int pathKey = 0; pathKey < pathCount; ++pathKey) {
            int[] path = paths[pathKey];
            
            for(int c = 0; c < MAX_COLS; ++c) {
                int currentPathRow = path[c];
                
                int nextPathRow = c == MAX_COLS - 1 ? 0 : path[c+1];
                
                if (nextPathRow < currentPathRow) {
                    //its a max point!  Find path not including this point
                    
                    List<Integer>  intersectedPath = new ArrayList<Integer>();
                    
                    
                    //Build up path with only once change
                    for (int index = 0; index < MAX_COLS; index++)
                    {
                        intersectedPath.add(path[index]);
                    }
                    intersectedPath.set(c, path[c] - 1);

                    int intPathIndex = pathToIndex.get(intersectedPath);
                    nextPath[pathKey][c] = intPathIndex;
                } else {
                    //not a max point
                    nextPath[pathKey][c] = -1;
                }
            }
        }

        
    }
}
