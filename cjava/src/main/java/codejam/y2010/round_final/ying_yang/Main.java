package codejam.y2010.round_final.ying_yang;

import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.GridChar;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
        super();
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        in.N = scanner.nextInt();
        in.M = scanner.nextInt();
        
        return in;
    }

    class Diagonal {
        int startRow;
        int startCol;
        
        int slope;
        boolean isWhite;
    }
  
    /**
     * Based on a border length, return the coordinates
     * @param in
     * @param border
     * @return
     */
    Pair<Integer,Integer> getRowCol(InputData in, int border) {
        
        //Bottom 0 to M - 1
        //then right has only length N - 2.  M to M + N - 3
        //top  M+N-2 to 2M+N-3
        //right 2M+N-2 to 2M+2N-5
        
        int rightStart = in.M;
        int rightLen = in.N-2;
        
        int topStart = rightStart+rightLen;
        int topLen = in.M;
        
        int leftStart = topStart+topLen;
        int leftLen = in.N-2;
        
        if (border >= 0 && border < in.M) {
            //bottom
            return new ImmutablePair<>(0, border);
        }
        if (border >= rightStart && border < rightStart+rightLen) {
            //right
            return new ImmutablePair<>(border-rightStart+1, in.M-1);
        }
        if (border >= topStart && border < topStart+topLen) {
            //top
            return new ImmutablePair<>(in.N-1, in.M - 1 - (border - topStart));
        }
        if (border >= leftStart && border < leftStart+leftLen) {
            //left
            return new ImmutablePair<>(in.N - 2- (border-leftStart), 
                    0);
        }
        return null;
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
    
    @Override
    public String handleCase(InputData in) {
       
        final int borderLen = 2*in.N + 2*in.M - 4;
        for(int startWhite = 0; startWhite < borderLen; ++startWhite)
        {
            for(int whiteBorderLen = 1; whiteBorderLen < borderLen; ++whiteBorderLen)
            {
                int endWhite = (startWhite + whiteBorderLen - 1) % borderLen;
                
                log.debug("Start white {} end white {}",startWhite,endWhite);
                GridChar grid = GridChar.buildEmptyGrid(in.N, in.M, '.');
                grid.setyZeroOnTop(false);
                
                int startBlack = (endWhite + 1) % borderLen;
                int endBlack = (borderLen + startWhite - 1) % borderLen;
                
                log.debug("Start black {} end black {}",startBlack,endBlack);
                
               // Preconditions.checkState(false);
                int border = startBlack;
                while(border != startWhite)
                {
                    Pair<Integer,Integer> rc = getRowCol(in,border);
                    grid.setEntry(rc.getLeft(), rc.getRight(), '#');
                    
                    ++border;
                    border %= borderLen;
                }
                
                border = startWhite;
                while(border != startBlack) {
                    
                    Pair<Integer,Integer> rc = getRowCol(in,border);
                    grid.setEntry(rc.getLeft(), rc.getRight(), '0');
                    
                    ++border;
                    border %= borderLen;
                }
                
                log.debug(grid.toString());
                
                
                
            }
        }

        return String.format("Case #%d: ", in.testCase);
    }
}