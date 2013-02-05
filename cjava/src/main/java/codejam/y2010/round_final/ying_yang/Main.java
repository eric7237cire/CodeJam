package codejam.y2010.round_final.ying_yang;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.GridChar;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

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
        int x = point.getX();
        int y = point.getY();
        
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
    private static boolean fillInGrid(GridChar grid, InputData in, Set<PointInt> endPoints) {
       
        /**
         * Fill in rows knowing the degree of each vertex and the borders
         */
        
        //loop on all rows except topmost
        for(int y = 0; y < in.nRows-1; ++y)
        {
            //skip edges as it is already filled in
            for(int x = 1; x < in.nCols - 1; ++x) {
                char c = grid.getEntry(y, x);
                
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
                    
                    Preconditions.checkState(!(xx < 0 || xx >= in.nCols));
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
                 
                    
                char northernChar = grid.getEntry(y+1, x);
                
                if (northernNeighborColorWhite == true && northernChar == '#') {
                   // log.debug("{}, {} -- North must be white. isWhite? {} north color {} degree {} neigh {}", x, y, 
                   //         isWhite, northernChar, sameColorDegree, sameColorNeighbors);
                    return false;
                }
                
                if (!northernNeighborColorWhite && northernChar == '0') {
                   // log.debug("{}, {} -- North must be black", x, y);
                    return false;
                }
                
                grid.setEntry(y+1, x, northernNeighborColorWhite ? '0' : '#');
                
            
            }
        }
        
        //A final check for all the endpoints
        
        PointInt bp = null;
        PointInt wp = null;
        
        for(PointInt ep : endPoints) {
            int degree = getDegree(ep, grid,in);
            
            if (degree != 1)
                return false;
            
            int x = ep.getX();
            int y = ep.getY();
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
        
        for(int y = 0; y < in.nRows; ++y)
        {
            for(int x = 0; x < in.nCols; ++x) {
                PointInt p = new PointInt(x,y);
                
                int degreeNeeded =  (endPoints.contains(p)) ? 1 : 2;
                
                int degree = getDegree(p, grid,in);
                
                if (degree != degreeNeeded) {
                    return false;
                }
                
            }
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
        
        //TODO do this while filling stuff in
        int white = getConnectedCount(grid,wp, in);
        int black = getConnectedCount(grid, bp, in);
        
        if (totalNodes != white+black)
            return false;
        
        return true;
    }
    
    
    
    private boolean checkDiagonal(GridChar grid, PointInt start,
            PointInt delta, InputData in)
    {
        
        int distance = 0;
        
        int y = start.getY();
        int x = start.getX();
        
        char startColor = grid.getEntry(y, x);
        
        while(x >= 0 && x < in.nCols &&
                y >= 0 && y < in.nRows)                
        {
            
            
            Preconditions.checkState(distance < 100000);
            
            char cur = grid.getEntry(y, x);
            
            Preconditions.checkState(cur == '0' || cur ==  '#');
            
            if (distance % 2 == 0 && cur != startColor)
                return false;
            
            if (distance % 2 == 1 && cur == startColor)
                return false;
            
            y += delta.getY();
            x += delta.getX();
            
            ++distance;
        }
        
        return true;
    }
    
    private static int getConnectedCount(GridChar grid, PointInt start, InputData in) {
        Queue<PointInt> q = new LinkedList<PointInt>();
        
        q.add(start);
        
        char color = grid.getEntry(start.getY(),start.getX());
        
        Set<PointInt> visited = Sets.newHashSet();
        
        while(!q.isEmpty()) {
            PointInt p = q.poll();
            if (visited.contains(p))
                continue;
            
            visited.add(p);
            
            int x = p.getX();
            int y = p.getY();
            
            for(int d = 0; d <= 3; d += 1) {
                int xx = x + delta[d][0];
                int yy = y + delta[d][1];
                
                if (xx < 0 || xx >= in.nCols)
                    continue;
                
                if (yy < 0 || yy >= in.nRows)
                    continue;
                
                char path = grid.getEntry(yy,xx);
                
                if (path == color) {
                    q.add(new PointInt(xx,yy));
                }
            }
        }
        
        return visited.size();
        
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
                    o.start.getY()-mOther*o.start.getX()-start.getY()+m*start.getX();
            
            int denom = m - mOther;
            
            if (num % denom != 0)
                return null;
            
            int x = num / denom;
            
            int y = m * (x - start.getX()) + start.getY();
            int y2 = mOther * (x - o.start.getX()) + o.start.getY();
            
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
        int count = 0;
        
        boolean debug = false;
        
        int[] startToFirstCornerDistance = new int[borderLen];
        
        /**
         * For symettry, calculate distance of start of white border
         * and distance to nearest 1st or 3rd corner  on it's path
         */
        
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
         * Loop through all starting positions for the white border
         * and all possible lengths
         */
        for(int startWhite = 0; startWhite < borderLen; ++startWhite)
        {
            log.debug("Start white {} case {}", startWhite, in.testCase);
            int distanceToFirstCorner = startToFirstCornerDistance[startWhite];
            
            for(int whiteBorderLen = 1; whiteBorderLen < borderLen; ++whiteBorderLen)
            {
                final int endWhite = (startWhite + whiteBorderLen - 1) % borderLen;
            
                final int startBlack = (endWhite + 1) % borderLen;
                final int endBlack = (borderLen + startWhite - 1) % borderLen;
                
                
                
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
                GridChar grid = GridChar.buildEmptyGrid(in.nRows, in.nCols, '.');
                grid.setyZeroOnTop(false);
                
                
                
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
                    PointInt coordCorner = in.corners[c];
                    
                    
                    diags.add(new Diagonal(coordCorner, cornerSlopes[c],
                        grid.getEntry(coordCorner.getY(), coordCorner.getX()) == '0'
                        ));
                }
                
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
                        
                        boolean isSameColor = (pos.start.getX()-inter.getX()) % 2 == 0;
                        boolean isWhite = (isSameColor && pos.isWhite) || (!isSameColor && !pos.isWhite);
                        
                        boolean isSameColorNeg = (neg.start.getX()-inter.getX()) % 2 == 0;
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
                
                //tryEndsOld(potentialBlackEnd, potentialWhiteEnd, in, grid);
                
                List<PointInt> be = Lists.newArrayList(blackEndPoints);
                List<PointInt> we = Lists.newArrayList(whiteEndPoints);
                
                int countThis = tryEnds(be, we, in, grid);
                                
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
    
    private static int tryEnds( List<PointInt> potentialBlackEnd, List<PointInt> potentialWhiteEnd, InputData in, GridChar grid ) {
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
                            
                        
                        GridChar tryGrid = new GridChar(grid);
                        
                        Set<PointInt> endPoints = Sets.newHashSet(whiteEnd1,
                                whiteEnd2,blackEnd1,blackEnd2);
                        
                        boolean ok = fillInGrid(tryGrid, in, endPoints);
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