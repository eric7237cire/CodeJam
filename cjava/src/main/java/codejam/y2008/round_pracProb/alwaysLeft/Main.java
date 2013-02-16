package codejam.y2008.round_pracProb.alwaysLeft;

import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Maps;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
      // super();
        super("B", true,true);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
        in.entranceToExit = scanner.next();
        in.exitToEntrance = scanner.next();

        return in;
    }

    static PointInt[] directions = new PointInt[] {
        new PointInt(0,1), //North
        new PointInt(1,0), //East
        new PointInt(0,-1), //South
        new PointInt(-1,0) //West
    };
    
    /**
     * Bits are North 0, South 1, West 2, East 3
     */
    static int[] directionBits = new int[] { 0, 3, 1, 2 };
    
    static void setBits(BitSetInt bs, int direction) {        
        bs.set(directionBits[direction % 4]);
    }
        
    @Override
    public String handleCase(InputData in)
    {
        PointInt currentSquare = new PointInt(0,0);
        int directionIdx = 2; //South
        
        /**
         * As we do not know the dimensions of the maze, use a map
         * to store the wallGap data (can pass or not) 
         */
        Map<PointInt, BitSetInt> canPassMap = Maps.newHashMap();
        canPassMap.put(currentSquare, new BitSetInt());
        
        /**
         * Walk through the front
         */
        MazeWalker mw = new MazeWalker(currentSquare, canPassMap, directionIdx);
        mw.walkThroughMaze(in.entranceToExit);
        
        /**
         * Get the exit coordinate and reverse direction
         */
        log.debug("\nNow exit\n");
        PointInt exit = mw.currentSquare;
        directionIdx = (mw.directionIdx+2) % 4;
        
        /**
         * Then walk through again
         */
        MazeWalker back = new MazeWalker(exit,  canPassMap, directionIdx);
        back.walkThroughMaze(in.exitToEntrance);
            
        StringBuffer ans = new StringBuffer();
        ans.append( "Case #" + in.testCase + ":\n");
        
        /**
         * The walkthroughs may not have visited the same set of squares,
         * so we need to take the extremes from both.
         * 
         * Print
         */
        for(int y = Math.max(mw.maxY,back.maxY); y >= Math.min(mw.minY,back.minY); --y)
        {
            for(int x = Math.min(mw.minX,back.minX); x <= Math.max(mw.maxX,back.maxX); ++x)
            {
                BitSetInt walls = canPassMap.get(new PointInt(x,y));
              //  log.debug("Coord :  {}  Can pass? N {} S {} E {} W {}  = {}", new PointInt(x,y), 
               //         walls.isSet(0),walls.isSet(1), walls.isSet(2), walls.isSet(3), Integer.toHexString(walls.getBits()) );
                
                ans.append(Integer.toHexString(walls.getBits()));
            }
            ans.append('\n');
        }
        
        ans.deleteCharAt(ans.length()-1);
        
        return ans.toString();
    }
    
    static class MazeWalker {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        
        PointInt currentSquare;
        Map<PointInt, BitSetInt> wallGapMap; 
        int directionIdx;
        
        public MazeWalker(PointInt currentSquare, Map<PointInt, BitSetInt> wallMap, int directionIdx) {
            super();
            this.currentSquare = currentSquare;
            this.wallGapMap = wallMap;
            this.directionIdx = directionIdx;
        }

        void walkThroughMaze(String steps) {
            for(int c = 0; c < steps.length(); ++c) {
                char ch = steps.charAt(c);
                
                //Do not count entrance and since it is only the current square, not the exit either
                if (c > 0) {
                    minX = Math.min(minX, currentSquare.x());
                    maxX = Math.max(maxX, currentSquare.x());
                    minY = Math.min(minY, currentSquare.y());
                    maxY = Math.max(maxY, currentSquare.y());
                }
                
                switch(ch) {
                case 'W':
                    BitSetInt prevWallGaps = wallGapMap.get(currentSquare);
                    setBits(prevWallGaps, directionIdx);
                    
                    currentSquare = PointInt.add(currentSquare, directions[directionIdx]);
                    
                    BitSetInt wallGaps = wallGapMap.get(currentSquare);
                    if (wallGaps == null) {
                        wallGaps = new BitSetInt();
                        wallGapMap.put(currentSquare, wallGaps);
                    }
                    
                    setBits(wallGaps, directionIdx + 2) ;
                    break;
                case 'R':
                    directionIdx += 1;
                    directionIdx %= 4;
                    break;
                case 'L':
                    directionIdx += 3;
                    directionIdx %= 4;
                    break;
                }
                
              //  log.debug("After Char {} position {} direction {}", ch, currentSquare, directionIdx);
            }
        }
    
            
        
        
    }

}