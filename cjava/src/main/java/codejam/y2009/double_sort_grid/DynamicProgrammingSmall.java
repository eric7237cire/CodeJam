package codejam.y2009.double_sort_grid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.utils.Grid;

/**
 * Goes row by row
 * @author thresh
 *
 */
public class DynamicProgrammingSmall {

    final static Logger log = LoggerFactory.getLogger(DynamicProgrammingSmall.class);

    Grid<Integer> grid;
    int rows;
    int cols;

    public static final int LETTER_MAX = 26;

    private static class DynamicProgrammingState {
        int[] upperRow;
        int position;
        int letterAtPosition;

        public DynamicProgrammingState(int[] upperRow, int position,
                int letterAtPosition) {
            super();
            this.upperRow = upperRow;
            this.position = position;
            this.letterAtPosition = letterAtPosition;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + letterAtPosition;
            result = prime * result + position;
            result = prime * result + Arrays.hashCode(upperRow);
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DynamicProgrammingState other = (DynamicProgrammingState) obj;
            if (letterAtPosition != other.letterAtPosition)
                return false;
            if (position != other.position)
                return false;
            if (!Arrays.equals(upperRow, other.upperRow))
                return false;
            return true;
        }
        
        

    }

    private Map<DynamicProgrammingState, Integer> memoize;
    long count = 0;
    long memCount = 0;
    
    public DynamicProgrammingSmall(Grid<Integer> grid) {
        // Preconditions.checkArgument(grid.getRows() == 1);
        this.grid = grid;
        this.rows = grid.getRows();
        this.cols = grid.getCols();
        
        this.memoize = new HashMap<>();

    }

    public static Integer solveGrid(Grid<Integer> grid) {

        DynamicProgrammingSmall ss = new DynamicProgrammingSmall(grid);

        int[] upperRowLimits = new int[grid.getCols()];
        return ss.solve(0, 1, upperRowLimits);
    }

    /*
     * Returns sum @ index given the rest must be >= letter
     */
    int solve(int position, int letterAtPreviousPositionMax,
            int[] upperRowLimits) {

        int row = position / grid.getCols();
        int col = position % grid.getCols();
        
        if (position >= grid.getSize()) {
            return 1;
        }
    
        memCount++;
        DynamicProgrammingState state = new DynamicProgrammingState(upperRowLimits, position, letterAtPreviousPositionMax);

        if (memoize.containsKey(state)) {
            return memoize.get(state);
        }
        count++;
        
        if (count % 100000 == 0) {
            log.info("Count {}.  Total raw count {}", count, memCount);
        }

        int upperCaracterMax = upperRowLimits[col];
        
        if (col == 0) {
            letterAtPreviousPositionMax = 1;
        }

        int lowerLimit = Math
                .max(letterAtPreviousPositionMax, upperCaracterMax);

        int curChar = grid.getEntry(row, col);

        int sum = 0;

        if (curChar == 0) // flexible
        {
            for (int letter = lowerLimit; letter <= LETTER_MAX; ++letter) {
                int[] newUpperRowLimits = Arrays.copyOf(upperRowLimits,
                        grid.getCols());
                newUpperRowLimits[col] = letter;
                sum += solve(position + 1, letter, newUpperRowLimits);
                sum %= 10007;
            }
        } else {
            if (lowerLimit <= curChar) {
                int[] newUpperRowLimits = Arrays.copyOf(upperRowLimits,
                        grid.getCols());
                newUpperRowLimits[col] = curChar;

                sum += solve(position + 1, curChar, newUpperRowLimits);
            }
        }
        
        log.debug("Solve pos {} previous letter {} upper row {} \n SUM = {}", 
                position, letterAtPreviousPositionMax, upperRowLimits, sum);

        memoize.put(state, sum % 10007);
        return sum % 10007;
    }
}
