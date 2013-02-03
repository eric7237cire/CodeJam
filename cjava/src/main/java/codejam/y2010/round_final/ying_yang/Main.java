package codejam.y2010.round_final.ying_yang;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.GridChar;

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
       
        in.height = scanner.nextInt();
        in.length = scanner.nextInt();
        
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
        
        if (coord.getY() == in.height - 1) {
            return TOP;
        }
        
        if (coord.getX() == 0) {
            return LEFT;
        }
        
        if (coord.getX() == in.length - 1) {
            return RIGHT;
        }
        
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
        
        int rightStart = in.length;
        int rightLen = in.height-2;
        
        int topStart = rightStart+rightLen;
        int topLen = in.length;
        
        int leftStart = topStart+topLen;
        int leftLen = in.height-2;
        
        if (border >= 0 && border < in.length) {
            //bottom
            return new PointInt(0, border);
        }
        if (border >= rightStart && border < rightStart+rightLen) {
            //right
            return new PointInt(border-rightStart+1, in.length-1);
        }
        if (border >= topStart && border < topStart+topLen) {
            //top
            return new PointInt(in.height-1, in.length - 1 - (border - topStart));
        }
        if (border >= leftStart && border < leftStart+leftLen) {
            //left
            return new PointInt(in.height - 2- (border-leftStart), 
                    0);
        }
        return null;
    }
    
   
    
    @Override
    public String handleCase(InputData in) {
       
        final int borderLen = 2*in.height + 2*in.length - 4;
        for(int startWhite = 0; startWhite < borderLen; ++startWhite)
        {
            for(int whiteBorderLen = 1; whiteBorderLen < borderLen; ++whiteBorderLen)
            {
                int endWhite = (startWhite + whiteBorderLen - 1) % borderLen;
                
                log.debug("Start white {} end white {}",startWhite,endWhite);
                GridChar grid = GridChar.buildEmptyGrid(in.height, in.length, '.');
                grid.setyZeroOnTop(false);
                
                int startBlack = (endWhite + 1) % borderLen;
                int endBlack = (borderLen + startWhite - 1) % borderLen;
                
                log.debug("Start black {} end black {}",startBlack,endBlack);
                
               // Preconditions.checkState(false);
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
                
                log.debug(grid.toString());
                
                List<Diagonal> diags = Lists.newArrayList();
                
                //Corners
                for(int c = 0; c < 4; ++c) {
                    PointInt coord = new PointInt(
                            in.length * corners[c][0],
                            in.height * corners[c][1]);
                    
                    
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
                
            }
        }

        return String.format("Case #%d: ", in.testCase);
    }
}