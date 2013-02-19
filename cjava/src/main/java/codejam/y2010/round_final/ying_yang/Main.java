package codejam.y2010.round_final.ying_yang;

import java.util.Arrays;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.GridChar;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

   
    public Main()
    {
        super("F", 1, 1);
       // (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        in.nRows = scanner.nextInt();
        in.nCols = scanner.nextInt();
        
        in.corners = new PointInt[] {
                new PointInt(0, 0), //bottom left
                new PointInt(in.nCols-1, 0), //bottom right
                new PointInt(in.nCols-1, in.nRows-1), //top right
                new PointInt(0, in.nRows-1) // top left
        };
        
        in.cornerSet = Sets.newHashSet(in.corners);
        
        return in;
    }

    class PathEnd {
        Diagonal pos;
        Diagonal neg;
        PointInt pathStart;
        boolean isWhite;
        public PathEnd(Diagonal pos, Diagonal neg, PointInt pathStart, boolean isWhite) {
            super();
            this.pos = pos;
            this.neg = neg;
            this.pathStart = pathStart;
            this.isWhite = isWhite;
        }        
    }
    
    
    boolean isCorner(PointInt point, InputData in) {
        for(int c = 0; c < 4; ++c) {
            if (in.corners[c].equals(point))
                return true;
        }
        return false;
    }
    
    private static final int[][] delta = new int[][] {
            {0, 1}, //North
            {1, 0}, //East
            {0, -1}, //South
            {-1, 0} //West
    };
    
    /**
     * Diagonals going in the opposite direction of
     * the beginning of a path
     */
    PointInt[][] diagDirections = new PointInt[][] {
      {new PointInt(-1, -1), new PointInt(1, -1)}, //if path goes north, both diags go south
      {new PointInt(-1, -1), new PointInt(-1, 1)}, //both west
      {new PointInt(-1, 1), new PointInt(1, 1)}, //both north
      {new PointInt(1, -1), new PointInt(1, 1)}, //both east
    };
    
    
    private static int getDegree(PointInt point, GridChar grid, InputData in) {
        int x = point.x();
        int y = point.y();
        
        int sameColorNeighbors = 0;
        
        char c = grid.getEntry(y, x);
        
        Preconditions.checkState(c != '.');
        
        boolean isWhite = c =='0';
        
        for(int d = 0; d <= 3; d += 1) {
            int xx = x + delta[d][0];
            int yy = y + delta[d][1];
            
            if (xx < 0 || xx >= in.nCols)
                continue;
            
            if (yy < 0 || yy >= in.nRows)
                continue;
            
            char adj = grid.getEntry(yy, xx);
            
            if (adj== '.')
                continue;
           // Preconditions.checkState(adj != '.');
            
            if (adj == '0' && isWhite) {
                ++sameColorNeighbors;                        
            }
            
            if (adj == '#' && !isWhite) {
                ++sameColorNeighbors;
            }
        }
        
        return sameColorNeighbors;
    }
    
    private static void fillInBottomRow(GridChar grid, GridChar checkGrid, InputData in, 
            Set<PointInt> endPoints, PointInt[] borderBegEnd, boolean c0IsWhite, boolean c1IsWhite) {
        
        //int[] startStop = null; 
        //char[] fillChar = null;
        
        boolean checkGridDo = false;
        
        /*
         * Fill in bottom row based on bools
         */
        if (c0IsWhite && c1IsWhite)
        {
            if (borderBegEnd[2].y() == 0)
            {
                /*
                startStop = new int[] {
                        0,
                        borderBegEnd[2].x() - 1,
                        borderBegEnd[2].x(),
                        borderBegEnd[3].x(),
                        borderBegEnd[3].x()+1,
                        in.nCols-1
                };
                fillChar = new char[] { '0', '#', '0' };
                */
                
                //Black border start between
                for(int x = 0; x < borderBegEnd[2].x(); ++x)
                {
                    if (checkGridDo) Preconditions.checkState(checkGrid.getEntry(0,x) == '0');
                    grid.setEntry(0, x, '0');
                }
                for(int x = borderBegEnd[2].x(); x <= borderBegEnd[3].x(); ++x)
                {
                    if (checkGridDo) Preconditions.checkState(checkGrid.getEntry(0,x) == '#');
                    grid.setEntry(0, x, '#');
                }
                for(int x = borderBegEnd[3].x()+1; x < in.nCols; ++x)
                {
                    if (checkGridDo) Preconditions.checkState(checkGrid.getEntry(0,x) == '0');
                    grid.setEntry(0, x, '0');
                }
            }
            
            for(int x = 0; x < in.nCols ; ++x) {
                if (checkGridDo) Preconditions.checkState(checkGrid.getEntry(0,x) == '0');
                grid.setEntry(0, x, '0');
            }
        }
        else if (!c0IsWhite && !c1IsWhite)
        {
            //Does white border start and end between the corners?
            if (borderBegEnd[0].y() == 0)
            {
                for(int x = 0; x < borderBegEnd[0].x(); ++x)
                {
                    if (checkGridDo) Preconditions.checkState(checkGrid.getEntry(0,x) == '#');
                    grid.setEntry(0, x, '#');
                }
                for(int x = borderBegEnd[0].x(); x <= borderBegEnd[1].x(); ++x)
                {
                    if (checkGridDo) Preconditions.checkState(checkGrid.getEntry(0,x) == '0');
                    grid.setEntry(0, x, '0');
                }
                for(int x = borderBegEnd[1].x()+1; x < in.nCols; ++x)
                {
                    if (checkGridDo) Preconditions.checkState(checkGrid.getEntry(0,x) == '#');
                    grid.setEntry(0, x, '#');
                }
            }
            else 
            {
                for(int x = 0; x < in.nCols ; ++x) {
                    if (checkGridDo) Preconditions.checkState(checkGrid.getEntry(0,x) == '#');
                    grid.setEntry(0, x, '#');
                }
            }
        }
        else if (c0IsWhite && !c1IsWhite)
        {
            int colEndWhite = borderBegEnd[1].x();
            Preconditions.checkState(borderBegEnd[1].y() == 0);
            
            //Check that black border starts just after
            Preconditions.checkState(borderBegEnd[2].y() == 0);
            Preconditions.checkState(borderBegEnd[2].x() == colEndWhite+1);
            
            for(int x = 0; x <= colEndWhite; ++x) {
                if (checkGridDo)     Preconditions.checkState(checkGrid.getEntry(0,x) == '0');
                grid.setEntry(0, x, '0');
            }
            for(int x = colEndWhite+1; x < in.nCols; ++x) {
                if (checkGridDo) Preconditions.checkState(checkGrid.getEntry(0,x) == '#');
                grid.setEntry(0, x, '#');
            }
        }
        else if (!c0IsWhite && c1IsWhite)
        {
            int colEndBlack = borderBegEnd[3].x();
            Preconditions.checkState(borderBegEnd[3].y() == 0);
            
            //Check that white border starts just after
            Preconditions.checkState(borderBegEnd[0].y() == 0);
            Preconditions.checkState(borderBegEnd[0].x() == colEndBlack+1);
            
            for(int x = 0; x <= colEndBlack; ++x) {
                if (checkGridDo) Preconditions.checkState(checkGrid.getEntry(0,x) == '#');
                grid.setEntry(0, x, '#');
            }
            for(int x = colEndBlack+1; x < in.nCols; ++x) {
                if (checkGridDo)Preconditions.checkState(checkGrid.getEntry(0,x) == '0');
                grid.setEntry(0, x, '0');
            }
            
        }
    }
    
    private static class CheckBorderVars
    {
        char borderColor;
        PointInt whiteBorderStartPt = null;
        PointInt blackBorderStartPt = null;
                
        int whiteBorderLen;
        GridChar grid;
        boolean ok;
    }
    
    static private void checkBorderHelper(CheckBorderVars v, int x, int y)
    {
        char curColor = v.grid.getEntry(y, x);
        
        if (curColor == '0')
            ++v.whiteBorderLen;
        
        if (curColor != v.borderColor) 
        {
            if (curColor == '0') 
            {
                if (v.whiteBorderStartPt!=null) {
                    v.ok = false;
                    log.info("Second white start point found {}", v.whiteBorderStartPt);
                    return;
                }
                v.whiteBorderStartPt = new PointInt(x, y);
            }
            if (curColor == '#')
            {
                if (v.blackBorderStartPt!=null) {
                    v.ok = false;
                    log.info("Second black point found {}", v.blackBorderStartPt);
                    return;
                }
                v.blackBorderStartPt = new PointInt(x,y);
            }
            
            v.borderColor = curColor;
        }        
    }
    
    private static boolean checkBorder(boolean c0IsWhite, 
            InputData in, GridChar grid,
            PointInt whiteBorderStartCheck,
            PointInt blackBorderStartCheck,
            int whiteBorderLenCheck
            )
    {
        CheckBorderVars v = new CheckBorderVars();
        v.grid = grid;
        v.borderColor = c0IsWhite ? '0' : '#';
        v.ok = true;
                
        /**
         * Each leg does not include the end
         * 
         * 4333
         * 4  2
         * 4  2
         * 1112
         */
        
        for(int x = 0; x < in.nCols - 1; ++x)
        {
            int y = 0;
            checkBorderHelper(v, x, y);                    
        }
        
        if (!v.ok)
            return false;
        
        for(int y = 0; y < in.nRows - 1; ++y)
        {
            int x = in.nCols - 1; 
            checkBorderHelper(v, x, y);                            
        }
        
        if (!v.ok)
            return false;
        
        for(int x = in.nCols-1; x >= 1; --x)
        {
            int y = in.nRows - 1;
            
            checkBorderHelper(v, x, y);
        }
        
        if (!v.ok)
            return false;
        
        
        for(int y = in.nRows-1; y >= 1; --y)
        {
            int x = 0;
            
            checkBorderHelper(v, x, y);
        }
        
        if (!v.ok)
            return false;
        
        //In case white border starts at 0,0
        if (v.whiteBorderStartPt == null && v.whiteBorderLen > 0
                && grid.getEntry(0,0) == '0')
        {
            v.whiteBorderStartPt = new PointInt(0,0);
        }
        
        log.info("Grid {}", grid);
        if (v.whiteBorderStartPt==null)
            return false;
        
        if (v.blackBorderStartPt == null)
            return false;
        
        //checkState(v.blackBorderStartPt != null);
        
        if (v.whiteBorderLen != whiteBorderLenCheck)
        {
            log.info("Grid {}", v.grid);
            log.info("Border len check failed actual {} check val {}", v.whiteBorderLen, whiteBorderLenCheck);
            return false;
        }
        
        if (!v.whiteBorderStartPt.equals(whiteBorderStartCheck))
        {
            log.info("White border start check failed {} {}", v.whiteBorderStartPt, whiteBorderStartCheck);
            return false;
        }
        
        if (!v.blackBorderStartPt.equals(blackBorderStartCheck))
        {
            log.info("Black border start check failed {} {}", v.blackBorderStartPt, blackBorderStartCheck);
            return false;
        }
        
        return true;
    }
    
    /**
     * 
     * @param grid
     * @param in
     * @param endPoints
     * @param borderBegEnd  w start, w end, b start, b end
     * @param c0IsWhite
     * @param c1IsWhite
     * @return
     */
    private static boolean fillInGrid(GridChar grid, InputData in, 
            Set<PointInt> endPoints, PointInt[] borderBegEnd,
            int whiteBorderStart, int blackBorderStart,
            int whiteBorderLen,
            boolean c0IsWhite, boolean c1IsWhite) {
       

        //GridChar grid = GridChar.buildEmptyGrid(in.nRows, in.nCols, '.');
        //grid.setyZeroOnTop(false); 
        
       fillInBottomRow(grid, null, in, endPoints, borderBegEnd, c0IsWhite, c1IsWhite);
        
        //int[][] degreeGrid = new int[in.nCols][in.nRows];
     //   boolean[][] connectedEdge = new boolean[in.nCols][in.nRows];
        
        PointInt bp = null;
        PointInt wp = null;
        
        
        
        /**
         * Fill in rows knowing the degree of each vertex and the borders
         */
        
        //loop on all rows except topmost
        for(int y = 0; y < in.nRows-1; ++y)
        {
            //skip edges as it is already filled in
            for(int x = 0; x < in.nCols ; ++x) {
                char c = grid.getEntry(y, x);
                
                /*
                if (x==0 || x == in.nCols-1)
                    connectedEdge[x][y] = true;
                if (y==0 || y == in.nRows-1)
                    connectedEdge[x][y] = true;
                */
                Preconditions.checkState(c != '.');
                
                PointInt p = new PointInt(x,y);
                final int sameColorDegree = endPoints.contains(p) ? 1 : 2;
                
                int sameColorNeighbors = 0;
                
                boolean isWhite = c =='0';
                
                /**
                 * North is not filled in
                 */
                for(int d = 1; d <= 3; d += 1) {
                    int xx = x + delta[d][0];
                    int yy = y + delta[d][1];
                    
                    if (xx < 0 || xx >= in.nCols)
                        continue; //Preconditions.checkState(!(xx < 0 || xx >= in.nCols));
                    if (yy < 0 || yy >= in.nRows)
                        continue;
                    
                    char adj = grid.getEntry(yy, xx);
                    
                   // if (adj== '.')
                    //    continue;
                    Preconditions.checkState(adj != '.');
                    
                    if (adj == '0' && isWhite) {
                        ++sameColorNeighbors;  
                       // connectedEdge[x][y] |= connectedEdge[xx][yy];
                    }
                    
                    if (adj == '#' && !isWhite) {
                      //  connectedEdge[x][y] |= connectedEdge[xx][yy];
                        ++sameColorNeighbors;
                    }
                }
                
                //Already too many neighbors
                if (sameColorNeighbors > sameColorDegree) {
                   // log.debug("{}, {} -- Too many neighbors {} > deg {}", x, y, sameColorNeighbors,sameColorDegree);
                    return false;
                }
                
                //Too few neighbors
                if (sameColorNeighbors == 0 && sameColorDegree == 2) {
                    //log.debug("{}, {} -- Too few neighbors {} > deg {}", x, y, sameColorNeighbors,sameColorDegree);
                    return false;
                }
                
                Boolean northernNeighborColorWhite = null;
                
                if (sameColorNeighbors == sameColorDegree - 1) {
                    //Northern color must be same color
                    northernNeighborColorWhite = isWhite;
                }
                
                if (sameColorNeighbors == sameColorDegree) {
                    //Northern neighbor must be other color
                    northernNeighborColorWhite = !isWhite;
                }
                
                Preconditions.checkState(northernNeighborColorWhite != null);
                 
                    /*
                char northernChar = grid.getEntry(y+1, x);
                
                if (northernNeighborColorWhite == true && northernChar == '#') {
                   // log.debug("{}, {} -- North must be white. isWhite? {} north color {} degree {} neigh {}", x, y, 
                   //         isWhite, northernChar, sameColorDegree, sameColorNeighbors);
                    return false;
                }
                
                if (!northernNeighborColorWhite && northernChar == '0') {
                   // log.debug("{}, {} -- North must be black", x, y);
                    return false;
                }*/
                
                grid.setEntry(y+1, x, northernNeighborColorWhite ? '0' : '#');
                
                /*
                Preconditions.checkState(checkGrid.getEntry(y+1,x) == '.' ||
                        checkGrid.getEntry(y+1,x) == grid.getEntry(y+1,x));
                */
                
                if (wp == null && isWhite) {
                    wp = new PointInt(x,y);
                }
                if (bp == null && !isWhite) {
                    bp = new PointInt(x,y);
                }
            
            }
        }
        
        
        
        
        //TODO
        /*
        final int borderLen = 2*in.nRows + 2*in.nCols - 4;
        
        //Check border corresponds
        
        int border = blackBorderStart;
        while(border != whiteBorderStart)
        {
            PointInt rc = getCoords(in,border);
            if ('#' != grid.getEntry(rc.y(), rc.x()))
            {
                checkState(!checkBorder(c0IsWhite, in, grid, borderBegEnd[0], borderBegEnd[2], whiteBorderLen));
                return false;
            }
            
            ++border;
            border %= borderLen;
        }
        
        border = whiteBorderStart;
        while(border != blackBorderStart) {
            
            PointInt rc = getCoords(in,border);
            if ('0' != grid.getEntry(rc.y(), rc.x()))
            {
                checkState(!checkBorder(c0IsWhite, in, grid, borderBegEnd[0], borderBegEnd[2], whiteBorderLen));
                return false;
            }
            
            ++border;
            border %= borderLen;
        }
        
        checkState(checkBorder(c0IsWhite, in, grid, borderBegEnd[0], borderBegEnd[2], whiteBorderLen));
        */
        
        if (!checkBorder(c0IsWhite, in,
                grid, borderBegEnd[0], borderBegEnd[2], whiteBorderLen))
            return false;
        
        //A final check for all the endpoints
        
        
        for(PointInt ep : endPoints) {
            int degree = getDegree(ep, grid,in);
            
            if (degree != 1)
                return false;
            
            int x = ep.x();
            int y = ep.y();
            char epColor = grid.getEntry(y,x);
            
            Integer pathDirection = null;
            
            if (epColor == '0' && wp == null) {
                wp = ep;
            }
            if (epColor == '#' && bp == null) {
                bp = ep;
            }
            
            //Find direction
            for(int d = 0; d <= 3; d += 1) {
                int xx = x + delta[d][0];
                int yy = y + delta[d][1];
                
                if (xx < 0 || xx >= in.nCols)
                    continue;
                
                if (yy < 0 || yy >= in.nRows)
                    continue;
                
                char path = grid.getEntry(yy,xx);
                
                if (path == epColor) {
                    Preconditions.checkState(pathDirection==null);
                    pathDirection = d;
                }
            }
            
            Preconditions.checkState(pathDirection!=null);
            
           // log.debug("Check grid {} ep {} dir {}", grid, ep, pathDirection);
            /*
             * These checks are redundant as the connect component check
             * covers it
            boolean ok = checkDiagonal(grid,ep, diagDirections[pathDirection][0], in);
            if (!ok)
                return false;
            
            ok = checkDiagonal(grid,ep, diagDirections[pathDirection][1], in);
            
            if (!ok)
                return false;
            */
           // log.debug("Passed");
        }
              
        //Final check for connectedness, it is p
        /*
         * 
20   0   0   0   0   
16   0   #   #   0   
12   0   0   0   0   
8    #   #   #   #   
4    #   0   0   #   
0    #   #   #   #   

this kind of grid passed diag checks, degree checks
         */
        
        int totalNodes = in.nCols*in.nRows;
        
        /*
        boolean has = hasMonocolorInteriorRow(grid,in);
        boolean has2 = hasMonocolorInteriorCol(grid,in);
        
        if (has || has2)
            return false;
        */
       
        int white = getConnectedCount(grid,wp, in);
        int black = getConnectedCount(grid, bp, in);

        if (totalNodes != white+black)
        {
            //log.info("Grid {}", grid);
            return false;
        }
        
        return true;
    }
    
    
    
     
    
    private static int getConnectedCount(GridChar grid, PointInt start, InputData in) {
        Queue<Integer> q = new LinkedList<Integer>();
        
        int startIndex = grid.getIndex(start.y(), start.x());
        
        q.add(startIndex);
        
        char color = grid.getEntry(startIndex);
        
        //Set<PointInt> visited = Sets.newHashSet();
        BitSet visited = new BitSet();
        
        while(!q.isEmpty()) {
            int p = q.poll();
            if (visited.get(p))
                continue;
            
            visited.set(p);
            
            int x = p % in.nCols;
            int y = p / in.nCols;
            
            for(int d = 0; d <= 3; d += 1) {
                int xx = x + delta[d][0];
                int yy = y + delta[d][1];
                
                if (xx < 0 || xx >= in.nCols)
                    continue;
                
                if (yy < 0 || yy >= in.nRows)
                    continue;
                
                char path = grid.getEntry(yy,xx);
                
                if (path == color) {
                    q.add(yy * in.nCols + xx);
                }
            }
        }
        
        return visited.cardinality();
        
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
         * in the interior of the grid or on the edge.
         * 
         *  Null if intersection is outside the grid
         *  or not an even integer.
         * 
         */
        PointInt intersection(Diagonal o, InputData in) {
            int mOther = o.slope[0] * o.slope[1];
            int m = slope[0] * slope[1];
            int num =
                    o.start.y()-mOther*o.start.x()-start.y()+m*start.x();
            
            int denom = m - mOther;
            
            if (num % denom != 0)
                return null;
            
            int x = num / denom;
            
            int y = m * (x - start.x()) + start.y();
            int y2 = mOther * (x - o.start.x()) + o.start.y();
            
            Preconditions.checkArgument(y == y2);
            
            if (y < 0 || y >= in.nRows)
                return null;
            
            if (x < 0 || x >= in.nCols)
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
    
    
    
    
    final static int BOTTOM = 0;
    final static int RIGHT = 1;
    final static int TOP = 2;
    final static int LEFT = 3;
    
    
    
    static int getSide(int borderPos, InputData in) {
        PointInt coord = getCoords(in,borderPos);
        
        if (coord.y() == 0 ) {
            return BOTTOM;
        }
        
        if (coord.y() == in.nRows - 1) {
            return TOP;
        }
        
        if (coord.x() == 0) {
            return LEFT;
        }
        
        if (coord.x() == in.nCols - 1) {
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
        int count = 0;
        
        boolean debug = false;
        
        GridChar grid = GridChar.buildEmptyGrid(in.nRows, in.nCols, '.');
        grid.setyZeroOnTop(false);
        
        
        /**
         * For symettry, calculate distance of start of white border
         * and distance to nearest bottom left or top right corner 
         *  on it's path.  If the distance is the same we can flip / rotate
         *  as needed to get exactly the same path
         * 
         * 
         */
        
        int[] startToFirstCornerDistance = new int[borderLen];
        
        for(int startWhite = 0; startWhite < borderLen; ++startWhite)
        {
            int border = startWhite;
            int distance = 0;
            while(border != startWhite || distance == 0)
            {
                PointInt rc = getCoords(in,border);
                if (in.corners[0].equals(rc) || in.corners[2].equals(rc)) {
                    break;
                }
                
                ++border;
                ++distance;
                border %= borderLen;
            }
            
            startToFirstCornerDistance[startWhite] = distance;            
        }
        
        int[][] symettryCache = new int[borderLen][borderLen];
        for(int[] sc : symettryCache) {
            Arrays.fill(sc, -1);
        }
        
        /**
         * Deterimen the border value for each corner,
         * in order to know its color only based on s
         */
        
        int[] cornerBorderIndex = new int[] 
        {
                0,
                in.nCols-1,
                in.nCols-1+in.nRows-1,
                in.nCols-1+in.nRows-1+in.nCols-1,
                in.nCols-1+in.nRows-1+in.nCols-1+in.nRows-1,
        };

        // y = 0 bottom
        Preconditions.checkState(new PointInt(0,0).equals( getCoords(in, cornerBorderIndex[0])) );
        Preconditions.checkState(new PointInt(in.nCols-1,0).equals( getCoords(in, cornerBorderIndex[1])) );
        Preconditions.checkState(new PointInt(in.nCols-1,in.nRows-1).equals( getCoords(in, cornerBorderIndex[2])) );
        Preconditions.checkState(new PointInt(0,in.nRows-1).equals( getCoords(in, cornerBorderIndex[3])) );
        //TODO
        
        /**
         * Loop through all starting positions for the white border
         * and all possible lengths
         */
        for(int startWhite = 0; startWhite < borderLen; ++startWhite)
        {
            log.info("Start white {} case {}", startWhite, in.testCase);
            int distanceToFirstCorner = startToFirstCornerDistance[startWhite];
            
            for(int whiteBorderLen = 1; whiteBorderLen < borderLen; ++whiteBorderLen)
            {
                /**
                 * Given the start of the white border and it's length,
                 * we can determine where it ends and the same for the blackborder
                 */
                final int endWhite = (startWhite + whiteBorderLen - 1) % borderLen;
            
                final int startBlack = (endWhite + 1) % borderLen;
                final int endBlack = (borderLen + startWhite - 1) % borderLen;
                
                /**
                 * Symettry checks for both white and black
                 */
                if (symettryCache[distanceToFirstCorner][whiteBorderLen] != -1) {
                    count += symettryCache[distanceToFirstCorner][whiteBorderLen];
                    //log.debug("Used cache");
                    continue;
                }
                
                int distanceToFirstCornerBlack = startToFirstCornerDistance[startBlack];
                final int blackBorderLen = borderLen - whiteBorderLen;
                
                if (symettryCache[distanceToFirstCornerBlack][blackBorderLen] != -1) {
                    count += symettryCache[distanceToFirstCornerBlack][blackBorderLen];
                    continue;
                }
                
               // log.debug("Start white {} end white {}",startWhite,endWhite);
                
                
                
                /**
                 * Build all the diagonals
                 */
                List<Diagonal> diags = Lists.newArrayList();
                
                boolean bottomLeftCornerIsWhite = false;
                boolean bottomRightCornerIsWhite = false;
                
                //Corners
                for(int c = 0; c < 4; ++c) {
                    PointInt coordCorner = in.corners[c];
                    
                    boolean isWhite = false;
                    if (startWhite <= endWhite)
                    {
                        if (startWhite <= cornerBorderIndex[c] &&
                                cornerBorderIndex[c] <= endWhite) {
                            isWhite = true;
                        }
                    } else {
                        Preconditions.checkState(startBlack < endBlack);
                        
                        if (! (startBlack <= cornerBorderIndex[c] &&
                                cornerBorderIndex[c] <= endBlack) ) {
                            isWhite = true;
                        }
                    }
                    
                   // boolean isWhiteCheck = grid.getEntry(coordCorner.y(), coordCorner.x()) == '0';
                        
                    //Preconditions.checkState(isWhite == isWhiteCheck);
                    
                    diags.add(new Diagonal(coordCorner, cornerSlopes[c],
                            isWhite
                        //grid.getEntry(coordCorner.y(), coordCorner.x()) == '0'
                        ));
                }
                
                bottomLeftCornerIsWhite = diags.get(0).isWhite;
                bottomRightCornerIsWhite = diags.get(1).isWhite;
                
                int sideWhiteStart = getSide(startWhite,in);
                int sideBlackStart = getSide(startBlack,in);
                
                int sideWhiteEnd = getSide(endWhite,in);
                int sideBlackEnd = getSide(endBlack,in);
                
                PointInt whiteBorderStartPt = getCoords(in, startWhite);
                PointInt blackBorderStartPt = getCoords(in, startBlack);
                
                PointInt whiteBorderEndPt = getCoords(in, endWhite);
                PointInt blackBorderEndPt = getCoords(in, endBlack);
                
                diags.add(new Diagonal(whiteBorderStartPt, slopesBeginBoundary[sideWhiteStart], true));
                diags.add(new Diagonal(blackBorderStartPt, slopesBeginBoundary[sideBlackStart], false));
                
                diags.add(new Diagonal(whiteBorderEndPt, slopesEndBoundary[sideWhiteEnd], true));
                diags.add(new Diagonal(blackBorderEndPt, slopesEndBoundary[sideBlackEnd], false));
                
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
                
                Set<PointInt> blackEndPoints = Sets.newHashSet();
                Set<PointInt> whiteEndPoints = Sets.newHashSet();
                
                //First check the corners
                if (in.cornerSet.contains(whiteBorderStartPt)) {
                    whiteEndPoints.add(whiteBorderStartPt);
                }
                
                if (in.cornerSet.contains(whiteBorderEndPt)) {
                    whiteEndPoints.add(whiteBorderEndPt);
                }
                
                if (in.cornerSet.contains(blackBorderStartPt)) {
                    blackEndPoints.add(blackBorderStartPt);
                }
                
                if (in.cornerSet.contains(blackBorderEndPt)) {
                    blackEndPoints.add(blackBorderEndPt);
                }
                
                
                /**
                 * Intersect all the diagonals
                 */
                for(Diagonal pos : posSlopes) {
                    for(Diagonal neg : negSlopes) {
                        PointInt inter = pos.intersection(neg, in);
                       
                        if (debug)
                            log.debug("Diag pos={} neg={} inter = {}",pos,neg,inter);
                        
                        if (inter == null)
                            continue;
                        
                        /**
                         * Calculate if we are an even or odd distance, to determine color
                         */
                        
                        boolean isSameColor = (pos.start.x()-inter.x()) % 2 == 0;
                        boolean isWhite = (isSameColor && pos.isWhite) || (!isSameColor && !pos.isWhite);
                        
                        boolean isSameColorNeg = (neg.start.x()-inter.x()) % 2 == 0;
                        boolean isWhiteNeg = (isSameColorNeg && neg.isWhite) || (!isSameColorNeg && !neg.isWhite);
                        
                        if (isWhite != isWhiteNeg) {
                            continue;
                        }
                        
                        if (isWhite) {                
                            whiteEndPoints.add(inter);
                        } else {
                            blackEndPoints.add(inter);
                        }
                        
                        //grid.setEntry(inter.getY(),inter.getX(), isWhite ? 'W' : 'B');
                    }
                }
                
                /**
                 * Try all end points to see if valid grids are
                 * created.
                 */
                                
                List<PointInt> be = Lists.newArrayList(blackEndPoints);
                List<PointInt> we = Lists.newArrayList(whiteEndPoints);
                
                PointInt[] borderBegEnd = new PointInt[] {
                        whiteBorderStartPt,
                        whiteBorderEndPt,
                        blackBorderStartPt,
                        blackBorderEndPt
                };
                
                int countThis = tryEnds(be, we, in, grid,borderBegEnd,
                        startWhite, startBlack,
                        whiteBorderLen,
                        bottomLeftCornerIsWhite, bottomRightCornerIsWhite);
                                
                /**
                 * Save the answer for both
                 */
                
                if (symettryCache[distanceToFirstCorner][whiteBorderLen] == -1) {
                    symettryCache[distanceToFirstCorner][whiteBorderLen] = countThis;
                }
                
                if (symettryCache[distanceToFirstCornerBlack][blackBorderLen] == -1) {
                    symettryCache[distanceToFirstCornerBlack][blackBorderLen] = countThis;
                }
                
                
                count+=countThis;
                
              // log.debug(grid.toString());
                
            }
        }

        return String.format("Case #%d: %d", in.testCase, count);
    }
    
    private static int tryEnds( List<PointInt> potentialBlackEnd, 
            List<PointInt> potentialWhiteEnd, 
            InputData in, GridChar grid, PointInt[] borderBegEnd,
            int whiteBorderStart, int blackBorderStart,
            int whiteBorderLen,
            boolean c0IsWhite, boolean c1IsWhite) {
        int count = 0;
        
        for(int bEnd1 = 0; bEnd1 < potentialBlackEnd.size(); ++bEnd1) {
            for(int bEnd2 = bEnd1+1; bEnd2 < potentialBlackEnd.size(); ++bEnd2) {
                for(int wEnd1 = 0; wEnd1 < potentialWhiteEnd.size(); ++wEnd1) {
                    for(int wEnd2 = wEnd1+1; wEnd2 < potentialWhiteEnd.size(); ++wEnd2) {
                        PointInt whiteEnd1 = potentialWhiteEnd.get(wEnd1);
                        PointInt whiteEnd2 = potentialWhiteEnd.get(wEnd2);
                        
                        PointInt blackEnd1 = potentialBlackEnd.get(bEnd1);
                        PointInt blackEnd2 = potentialBlackEnd.get(bEnd2);
        
                        Preconditions.checkState(!blackEnd1.equals(blackEnd2));
                            
                        
                        Preconditions.checkState(!whiteEnd1.equals(whiteEnd2)); 
                            
                        
                       // GridChar tryGrid = new GridChar(grid);
                        
                        Set<PointInt> endPoints = Sets.newHashSet(whiteEnd1,
                                whiteEnd2,blackEnd1,blackEnd2);
                        
                        boolean ok = fillInGrid(grid, in, endPoints,
                                borderBegEnd, 
                                whiteBorderStart, blackBorderStart,
                                whiteBorderLen,
                                c0IsWhite, c1IsWhite);
                       // log.debug("White end 1 {} 2 {}  Black end 1 {} 2 {}",
                                //whiteEnd1,  whiteEnd2,blackEnd1,blackEnd2);
                        //log.debug("Try grid ok {}.  Count: {}\n{}", ok,count,tryGrid);
                        
                        if (!ok)
                            continue;
        
                        ++count;
                        
                        /*
                        log.debug("Found grid. we {} {} be {} {}  Count: {}\n{}", 
                                whiteEnd1, whiteEnd2,
                                blackEnd1, blackEnd2,
                                count,tryGrid);
                        */
                    }
                }
            }
        }
        
        return count;
    }
    
    
    
}