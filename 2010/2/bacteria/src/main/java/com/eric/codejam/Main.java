package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.geometry.Rectangle;
import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.eric.codejam.utils.Direction;
import com.eric.codejam.utils.GraphAdjList;
import com.eric.codejam.utils.GridChar;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    //static final int RECT_SIZE_MAX = 100;
   // static final int RECT_NUM_MAX = 10;
    
    static final int RECT_SIZE_MAX = 1000000;
    static final int RECT_NUM_MAX = 1000;
    
    static List<List<Integer>> getRectIntersections(InputData input) {
        
        
        GraphAdjList graph = new GraphAdjList(input.rects.length);
        
        for(int rect1 = 0; rect1 < input.R; ++rect1) {
            graph.addConnection(rect1, rect1);
            
            Rectangle rec1 = input.rects[rect1];
            
            for(int rect2 = rect1 + 1; rect2 < input.R; ++rect2) {
                Rectangle rec2 = input.rects[rect2];
                if (input.rects[rect1].touchesHorVer(input.rects[rect2]) ||
                        (rec1.x2 == rec2.x1 - 1 && rec1.y1 == rec2.y2 + 1) ||
                        (rec2.x2 == rec1.x1 - 1 && rec2.y1 == rec1.y2 + 1) 
                        ) {
                    graph.addConnection(rect1, rect2);                                        
                }
            }
        }
        
        List<List<Integer>> rectIntersections = graph.getConnectedComponents();

        log.debug("Rect intersections {}", rectIntersections);

        return rectIntersections;
    }
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        GridChar.setPrintWidth(1);
        log.info("Starting calculating case {}", caseNumber);

        
        
        /*
        for(int rect1 = 0; rect1 < input.R; ++rect1) {
            List<Integer> inter = new ArrayList<>();
            inter.add(rect1);
            rectIntersections.add(inter);
        }*/
        
        int rounds = -1;
        if (RECT_SIZE_MAX <= 100) { 
            rounds = getRoundsBruteForce(input);
                
        for(Rectangle r : input.rects) {
        log.info("Rects " + r);
        }
        }
        
        List<List<Integer>> rectIntersections = getRectIntersections(input);
        
        int maxTime = 0;
        
        for(List<Integer> connectedRects : rectIntersections) {
            int time = findTimeToDecay(connectedRects, input);
            maxTime = Math.max(maxTime, time);
        }
        /*
        for(Rectangle rect : input.rects) {
            int x1 = Math.max(1, rect.x1-RECT_NUM_MAX);
            int x2 = Math.min(RECT_SIZE_MAX, rect.x1+5);
            int y1 = rect.y2;
            int y2 = Math.min(RECT_SIZE_MAX, rect.y2+RECT_NUM_MAX);
            
            Rectangle subSlice = new Rectangle(x1, y1, x2, y2);
            
            Preconditions.checkState(subSlice.area() < 1200000);
            
            
        }*/
        
        /*
        int maxCreationDiag = 0;
        
        for(Rectangle rect : input.rects) {
            int creationDiag1 = findLongestDiagonal(rect.x1, rect.y2+1, input);
            int creationDiag2 = findLongestDiagonal(rect.x2, rect.y2+1, input);
            int creationDiag = Math.max(creationDiag1, creationDiag2);
            maxCreationDiag = Math.max(maxCreationDiag, creationDiag);
        }
        
        maxTime = Math.max(maxTime, maxCreationDiag);
        */
        
        log.info("Case #" + caseNumber + ": " + rounds + " New Method " + maxTime);
        
        if (rounds >= 0)
            Preconditions.checkState(maxTime == rounds);
        
        // DecimalFormat decim = new DecimalFormat("0.00000000000");
        // decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

        return ("Case #" + caseNumber + ": " + maxTime);
    }
    
    /**
     * Assumption is r, c is the bottom of a rectangle
     * @param r
     * @param c
     * @param input
     * @return
     */
    static int findLongestDiagonal(int x, int y, InputData input) {
        //r, c must be empty, north must be filled, west must be filled
        int diagDistance = 0;
        
        int startX = x;
        int startY = y;
        
        
        
        boolean doneCalcDiagDist = false;
        while (y <= RECT_SIZE_MAX && x >= 1 && !doneCalcDiagDist) {
            

            //top filled ?  diagDist > 0 means must be a bottom right corner
            boolean aboveFilled = false;
            boolean currentSquareEmpty = true;
            for (Rectangle rect : input.rects) {
                if (diagDistance == 0 || rect.y2 == y-1 && rect.x2 == x) {
                    aboveFilled = true;
                }
                if (rect.intersects(new Rectangle(x, y, x, y))) {
                    currentSquareEmpty = false;
                }
            }

            if (!currentSquareEmpty || !aboveFilled) {
                break; // return distance;
            }

            ++diagDistance;
            y++;
            x--;
        }
        
        //see if it can be extended
        Rectangle maxTopExt = new Rectangle(startX, startY-1, RECT_SIZE_MAX, startY-1);
        List<Rectangle> topRects = new ArrayList<>();
        for(Rectangle rect : input.rects) {
            Rectangle interRect = rect.getIntersection(maxTopExt);
            if (interRect != null) {
                topRects.add(interRect);
            }
        }
        Collections.sort(topRects, new Comparator<Rectangle>() {

            @Override
            public int compare(Rectangle o1, Rectangle o2) {
                return ComparisonChain.start().compare(o1.x1, o2.x1).compare(o1.x2, o2.x2).result();
            }
            
        });
        
        int maxX = startX;
        Preconditions.checkState(topRects.size() > 0);
        
        for(Rectangle rect : topRects) {
            if (rect.x1 > maxX) {
                break;
            }
            
            maxX = Math.max(rect.x2, maxX);
        }
        
        int upRightExtension = maxX - startX;
        
        int endX = startX - (diagDistance - 1);
        int endY = startY - (diagDistance - 1);
        
        Rectangle maxWestExt = new Rectangle(endX-1, endY, endX-1,RECT_SIZE_MAX);
        List<Rectangle> leftRects = new ArrayList<>();
        for(Rectangle rect : input.rects) {
            Rectangle interRect = rect.getIntersection(maxWestExt);
            if (interRect != null) {
                topRects.add(interRect);
            }
        }
        Collections.sort(leftRects, new Comparator<Rectangle>() {

            @Override
            public int compare(Rectangle o1, Rectangle o2) {
                return ComparisonChain.start().compare(o1.y1, o2.y1).compare(o1.y2, o2.y2).result();
            }
            
        });
        
        int maxY = endY;
        
        for(Rectangle rect : leftRects) {
            if (rect.y1 > maxY) {
                break;
            }
            
            maxY = Math.max(rect.y2, maxY);
        }
        
        int westExtension = maxX;

        return diagDistance + Math.max(upRightExtension, westExtension);
    }
    
    static int findTimeToDecay(List<Integer> connectedRects, InputData input) {
        //find most north line via y - intercept  (lowest row value)
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
            maxY = Math.max(maxY, input.rects[rectNum].y2);
            maxX = Math.max(maxX, input.rects[rectNum].x2);
        }
        
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
    public InputData readInput(BufferedReader br, int testCase)
            throws IOException {

        InputData input = new InputData(testCase);
        input.R = Integer.parseInt(br.readLine());
        input.rects = new Rectangle[input.R];
        for (int i = 0; i < input.R; ++i) {
            String[] line = br.readLine().split(" ");
            input.rects[i] = new Rectangle(Integer.parseInt(line[0]),
                    Integer.parseInt(line[1]), Integer.parseInt(line[2]),
                    Integer.parseInt(line[3]));
        }

        return input;

    }

    public Main() {
        super();
    }

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
            // args = new String[] { "C-small-practice.in" };
            // args = new String[] { "B-large-practice.in" };
        }
        log.info("Input file {}", args[0]);

        Main m = new Main();
        Runner.goSingleThread(args[0], m, m);
        // Runner.go(args[0], m, m, new InputData(-1));

    }

}