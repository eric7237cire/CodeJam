package codejam.y2010.round_2.bacteria;

import java.util.List;
import java.util.Scanner;

import com.google.common.base.Preconditions;

import codejam.utils.datastructures.graph.CCbfs;
import codejam.utils.datastructures.graph.GraphInt;
import codejam.utils.geometry.Rectangle;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData> {

    public Main() {
        super("C", 1, 1, 0);
    }
    

    //static final int RECT_SIZE_MAX = 100;
   // static final int RECT_NUM_MAX = 10;
    
    static final int RECT_SIZE_MAX = 1000000;
    static final int RECT_NUM_MAX = 1000;
    
    
    static List<List<Integer>> getRectIntersections(InputData input) {
        
        
        GraphInt graph = new GraphInt(); //input.rects.length);
        
        for(int rect1 = 0; rect1 < input.R; ++rect1) {
            graph.addNode(rect1);
            
            Rectangle rec1 = input.rects[rect1];
            
            for(int rect2 = rect1 + 1; rect2 < input.R; ++rect2) {
                Rectangle rec2 = input.rects[rect2];
                //Also include north east to south west connections
                if (input.rects[rect1].touchesHorVer(input.rects[rect2]) ||
                        (rec1.x2 == rec2.x1 - 1 && rec1.y1 == rec2.y2 + 1) ||
                        (rec2.x2 == rec1.x1 - 1 && rec2.y1 == rec1.y2 + 1) 
                        ) {
                    graph.addConnection(rect1, rect2);                                        
                }
            }
        }
        
        CCbfs cc = new CCbfs(graph);
        List<List<Integer>> rectIntersections = cc.go();

        log.debug("Rect intersections {}", rectIntersections);

        return rectIntersections;
    }
    
    
    
    @Override
    public String handleCase(InputData input) {

        GridChar.setPrintWidth(1);
        

        List<List<Integer>> rectIntersections = getRectIntersections(input);

        int maxTime = 0;

        for (List<Integer> connectedRects : rectIntersections) {
            int time = findTimeToDecay(connectedRects, input);
            maxTime = Math.max(maxTime, time);
        }

        return ("Case #" + input.testCase + ": " + maxTime);
    }
    

    /**
     * The key idea.  Basically in a connected blob of rectangles, the
     * time to live is equal to the difference in the y int of the top/left
     * corner (first to be eaten) and bottom right corner.
     * 
     * The bottom right corner is either from a real rectangle or
     * the rectangle needed to cover the blob.  The distance is
     * the time it takes for a line of slope 1 to go across the 
     * connected set.
      
       1
     11 
     1
     
     behaves like
     
       1
     111
     111
     
     because of the creation of bacteria
     
     * 
     * @param connectedRects
     * @param input
     * @return
     */
    static int findTimeToDecay(List<Integer> connectedRects, InputData input) {
        //find most north line via y - intercept  (lowest row value)
        /**
         * Because north is in direction of decrasing y, we want
         * y - y' / x = -1
         * 
         * basically the slope delta(y - y_intercept) / delta(x)
         * 
         * y - y' = -x
         * y+x = y'
         * 
         * Graph goes like
         *   12345
         * 1 
         * 2   1
         * 3
         * 4
         * 5
         * 
         * so the 1 is on a line x+y = 5  -- (3,2) (4,1) (5,0)  5 is the y intercept
         * 
         */
        int northYInt = Integer.MAX_VALUE;
        int southYInt = 0;
        
        if (connectedRects.isEmpty()) {
            return 0;
        }
        
        int maxX = 0;
        int maxY = 0;
        for(Integer rectNum : connectedRects) {
            northYInt = Math.min(northYInt, input.rects[rectNum].y1 + input.rects[rectNum].x1);
            southYInt = Math.max(southYInt, input.rects[rectNum].y2 + input.rects[rectNum].x2);
            
            //North is in direction of decreasing y coordinate
            Preconditions.checkState(northYInt <= southYInt);
            
            maxY = Math.max(maxY, input.rects[rectNum].y2);
            maxX = Math.max(maxX, input.rects[rectNum].x2);
        }
        
        //This is the check for the bottom right corner being 'filled in'
        southYInt = Math.max(southYInt, maxY + maxX);
        
        return southYInt - northYInt + 1;
    }
    
    static int getRoundsBruteForce(InputData input) {
        GridChar grid = GridChar.buildEmptyGrid(RECT_SIZE_MAX, RECT_SIZE_MAX, '0');
        for (Rectangle rect : input.rects) {
            for (int r = rect.y1; r <= rect.y2; ++r) {
                for (int c = rect.x1; c <= rect.x2; ++c) {
                    grid.setEntry(r - 1, c - 1, '1');
                }
            }
        }

        log.debug("Start Grid {}", grid);

        int rounds = 0;
        while (true) {
            GridChar newGrid = new GridChar(grid);
            boolean atLeastOneAlive = false;
            for (int idx = 0; idx < grid.getSize(); ++idx) {

                if (grid.getEntry(idx) == '1'
                        && grid.getEntry(idx, Direction.NORTH) == '0'
                        && grid.getEntry(idx, Direction.WEST) == '0') {
                    newGrid.setEntry(idx, '0');
                } else if (grid.getEntry(idx) == '1') {

                    atLeastOneAlive = true;
                }

                if (grid.getEntry(idx) == '0'
                        && grid.getEntry(idx, Direction.NORTH) == '1'
                        && grid.getEntry(idx, Direction.WEST) == '1') {
                    newGrid.setEntry(idx, '1');
                }

            }
            ++rounds;
            if (!atLeastOneAlive) {
                break;
            }
            grid = newGrid;
            //log.debug("Grid {}", grid);
        }
        
        return rounds;
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData input = new InputData(testCase);
        input.R = scanner.nextInt();
        input.rects = new Rectangle[input.R];
        for (int i = 0; i < input.R; ++i)
        {

            input.rects[i] = new Rectangle(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        }

        return input;

    }
   

}