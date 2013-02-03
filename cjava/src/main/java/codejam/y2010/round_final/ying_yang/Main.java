package codejam.y2010.round_final.ying_yang;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.GridChar;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        super();
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        in.nRows = scanner.nextInt();
        in.nCols = scanner.nextInt();
        
        return in;
    }

    class Diagonal {
        PointInt start;
        
        int[] slope;
        boolean isWhite;
        public Diagonal(PointInt start, int[] slope, boolean isWhite) {
            super();
            this.start = start;
            this.slope = slope;
            this.isWhite = isWhite;
        }
        
        /**
         * Given 2 diagonals, returns the intersection
         * in the interior of the grid.  Or null if none exists
         * 
         */
        PointInt intersection(Diagonal o, InputData in) {
            int mOther = o.slope[0] * o.slope[1];
            int m = slope[0] * slope[1];
            int num =
                    o.start.getY()-mOther*o.start.getX()-start.getY()+m*start.getX();
            
            int denom = m - mOther;
            
            if (num % denom != 0)
                return null;
            
            int x = num / denom;
            
            int y = m * (x - start.getX()) + start.getY();
            int y2 = mOther * (x - o.start.getX()) + o.start.getY();
            
            Preconditions.checkArgument(y == y2);
            
            if (y <= 0 || y >= in.nRows-1)
                return null;
            
            if (x <= 0 || x >= in.nCols-1)
                return null;
            
            return new PointInt(x, y);
        }

        @Override
        public String toString()
        {
            return "Diagonal [start=" + start + ", slope=" + Arrays.toString(slope) + ", isWhite=" + isWhite + "]";
        }
        
    }
    
    /**
     * What the slopes of the diagonal lines are if the
     * boundary starts at the given side
     * 
     * Slope is away from other color
     */
    int[][] slopesBeginBoundary = new int[][]
            {

            { 1, 1 },             //Bottom row
            { -1, 1},  //right side
            { -1, -1}, //top side
            {1, -1}
            };
    
    int[][] slopesEndBoundary = new int[][]
            {

            { -1, 1 },             //Bottom row
            { -1, -1},  //right side
            { 1, -1}, //top side
            {1, 1} //left side
            };
    
    int[][] cornerSlopes = new int[][] 
            {
            { 1, 1 },             //Bottom left
            { -1, 1},  //bottom  right 
            { -1, -1}, //top right
            {1, -1} //top left 
            
            };
    
    int[][] corners = new int[][] 
            {
            { 0, 0 },             //Bottom left
            { 1, 0},  //bottom  right 
            { 1, 1}, //top right
            {0, 1} //top left 
            
            };
    
    
    final static int BOTTOM = 0;
    final static int RIGHT = 1;
    final static int TOP = 2;
    final static int LEFT = 3;
    
    static int getSide(int borderPos, InputData in) {
        PointInt coord = getCoords(in,borderPos);
        
        if (coord.getY() == 0 ) {
            return BOTTOM;
        }
        
        if (coord.getY() == in.nRows - 1) {
            return TOP;
        }
        
        if (coord.getX() == 0) {
            return LEFT;
        }
        
        if (coord.getX() == in.nCols - 1) {
            return RIGHT;
        }
        
        Preconditions.checkState(false);
        return -1;
    }
  
    /**
     * Based on a border length, return the coordinates
     * @param in
     * @param border
     * @return
     */
    static PointInt getCoords(InputData in, int border) {
        
        //Bottom 0 to M - 1
        //then right has only length N - 2.  M to M + N - 3
        //top  M+N-2 to 2M+N-3
        //right 2M+N-2 to 2M+2N-5
        
        int rightStart = in.nCols;
        int rightLen = in.nRows-2;
        
        int topStart = rightStart+rightLen;
        int topLen = in.nCols;
        
        int leftStart = topStart+topLen;
        int leftLen = in.nRows-2;
        
        if (border >= 0 && border < in.nCols) {
            //bottom
            return new PointInt(border, 0);
        }
        if (border >= rightStart && border < rightStart+rightLen) {
            //right
            return new PointInt(in.nCols-1, border-rightStart+1 );
        }
        if (border >= topStart && border < topStart+topLen) {
            //top
            return new PointInt(in.nCols - 1 - (border - topStart), in.nRows-1);
        }
        if (border >= leftStart && border < leftStart+leftLen) {
            //left
            return new PointInt(0, in.nRows - 2- (border-leftStart));
        }
        return null;
    }
    
   
    
    @Override
    public String handleCase(InputData in) {
       
        final int borderLen = 2*in.nRows + 2*in.nCols - 4;
        
        /**
         * Loop through all starting positions for the white border
         * and all possible lengths
         */
        for(int startWhite = 0; startWhite < borderLen; ++startWhite)
        {
            for(int whiteBorderLen = 1; whiteBorderLen < borderLen; ++whiteBorderLen)
            {
                int endWhite = (startWhite + whiteBorderLen - 1) % borderLen;
                
                log.debug("Start white {} end white {}",startWhite,endWhite);
                GridChar grid = GridChar.buildEmptyGrid(in.nRows, in.nCols, '.');
                grid.setyZeroOnTop(false);
                
                int startBlack = (endWhite + 1) % borderLen;
                int endBlack = (borderLen + startWhite - 1) % borderLen;
                
                log.debug("Start black {} end black {}",startBlack,endBlack);
                
                /**
                 * A la brute force, set the border in the grid
                 */
                int border = startBlack;
                while(border != startWhite)
                {
                    PointInt rc = getCoords(in,border);
                    grid.setEntry(rc.getY(), rc.getX(), '#');
                    
                    ++border;
                    border %= borderLen;
                }
                
                border = startWhite;
                while(border != startBlack) {
                    
                    PointInt rc = getCoords(in,border);
                    grid.setEntry(rc.getY(), rc.getX(), '0');
                    
                    ++border;
                    border %= borderLen;
                }
                
               
                
                /**
                 * Build all the diagonals
                 */
                List<Diagonal> diags = Lists.newArrayList();
                
                //Corners
                for(int c = 0; c < 4; ++c) {
                    PointInt coord = new PointInt(
                            (in.nCols-1) * corners[c][0],
                            (in.nRows-1) * corners[c][1]);
                    
                    
                diags.add(new Diagonal(coord, cornerSlopes[c],
                        grid.getEntry(coord.getY(), coord.getX()) == '0'
                        ));
                }
                
                int sideWhiteStart = getSide(startWhite,in);
                int sideBlackStart = getSide(startBlack,in);
                
                int sideWhiteEnd = getSide(endWhite,in);
                int sideBlackEnd = getSide(endBlack,in);
                
                PointInt whiteStartPt = getCoords(in, startWhite);
                PointInt blackStartPt = getCoords(in, startBlack);
                
                PointInt whiteEndPt = getCoords(in, endWhite);
                PointInt blackEndPt = getCoords(in, endBlack);
                
                diags.add(new Diagonal(whiteStartPt, slopesBeginBoundary[sideWhiteStart], true));
                diags.add(new Diagonal(blackStartPt, slopesBeginBoundary[sideBlackStart], false));
                
                diags.add(new Diagonal(whiteEndPt, slopesEndBoundary[sideWhiteEnd], true));
                diags.add(new Diagonal(blackEndPt, slopesEndBoundary[sideBlackEnd], false));
                
                /**
                 * Partition the diagonals in 2 sets, slope parallel to 1 and to -1
                 * 
                 * use the trick that slope.x * slope.y is positive it's positev
                 */
                
                List<Diagonal> posSlopes = Lists.newArrayList();
                List<Diagonal> negSlopes = Lists.newArrayList();
                
                for(Diagonal d : diags) {
                    if (d.slope[0] * d.slope[1] > 0) {
                        posSlopes.add(d);
                    } else {
                        Preconditions.checkState(d.slope[0] * d.slope[1] < 0);
                        negSlopes.add(d);
                    }
                }
                
                
                for(Diagonal pos : posSlopes) {
                    for(Diagonal neg : negSlopes) {
                        PointInt inter = pos.intersection(neg, in);
                        
                        log.debug("Diag pos={} neg={} inter = {}",pos,neg,inter);
                        
                        if (inter == null)
                            continue;
                        
                        /**
                         * Calculate if we are an even or odd distance, to determine color
                         */
                        
                        boolean isSameColor = (pos.start.getX()-inter.getX()) % 2 == 0;
                        boolean isWhite = (isSameColor && pos.isWhite) || (!isSameColor && !pos.isWhite);
                        
                        boolean isSameColorNeg = (neg.start.getX()-inter.getX()) % 2 == 0;
                        boolean isWhiteNeg = (isSameColorNeg && neg.isWhite) || (!isSameColorNeg && !neg.isWhite);
                        
                        if (isWhite != isWhiteNeg) {
                            continue;
                        }
                        
                        grid.setEntry(inter.getY(),inter.getX(), isWhite ? 'W' : 'B');
                    }
                }
                
                log.debug(grid.toString());
                
            }
        }

        return String.format("Case #%d: ", in.testCase);
    }
}