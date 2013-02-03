package codejam.y2010.round_final.ying_yang;

import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    @Override
    public String handleCase(InputData in) {
       
        final int borderLen = 2*in.N + 2*in.M - 4;
        for(int startWhite = 0; startWhite < borderLen; ++startWhite)
        {
            for(int endWhite = startWhite; endWhite < borderLen; ++endWhite)
            {
                log.debug("Start white {} end white {}",startWhite,endWhite);
                GridChar grid = GridChar.buildEmptyGrid(in.N, in.M, '.');
                grid.setyZeroOnTop(false);
                
                for(int border = 0; border < startWhite; ++border) {
                    Pair<Integer,Integer> rc = getRowCol(in,border);
                    grid.setEntry(rc.getLeft(), rc.getRight(), '#');
                }
                for(int border = startWhite; border <= endWhite; ++border) {
                    Pair<Integer,Integer> rc = getRowCol(in,border);
                    grid.setEntry(rc.getLeft(), rc.getRight(), '0');
                }
                for(int border = endWhite + 1; border < borderLen; ++border) {
                    Pair<Integer,Integer> rc = getRowCol(in,border);
                    grid.setEntry(rc.getLeft(), rc.getRight(), '#');
                }
                log.debug(grid.toString());
            }
        }

        return String.format("Case #%d: ", in.testCase);
    }
}