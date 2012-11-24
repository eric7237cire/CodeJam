package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.geometry.Rectangle;
import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.eric.codejam.utils.Direction;
import com.eric.codejam.utils.GridChar;
import com.google.common.base.Preconditions;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    int RECT_MAX = 12;

    
    static List<List<Integer>> getRectIntersections(InputData input) {
        List<List<Integer>> rectIntersections = new ArrayList<>();
        

        int[] rectGroupNum = new int[input.R];
        Arrays.fill(rectGroupNum, -1);
        
        int currentGroupNum = -1;
        for(int rect1 = 0; rect1 < input.R; ++rect1) {
            if (rectGroupNum[rect1] == -1) {
                rectGroupNum[rect1] = ++currentGroupNum;
                rectIntersections.add(new ArrayList<Integer>());
                rectIntersections.get(rectGroupNum[rect1]).add(rect1);
            }
            
            for(int rect2 = rect1 + 1; rect2 < input.R; ++rect2) {
                if (input.rects[rect1].touches(input.rects[rect2])) {
                    
                    if(rectGroupNum[rect2] == rectGroupNum[rect1]) {
                        continue;
                    } else
                    if(rectGroupNum[rect2] != -1) {
                        
                        int currentGroupNumToKeep = Math.min(rectGroupNum[rect1], rectGroupNum[rect2]);
                        int groupNumToRemove = Math.max(rectGroupNum[rect1], rectGroupNum[rect2]);
                        Preconditions.checkState(currentGroupNumToKeep != groupNumToRemove);
                        rectIntersections.get(currentGroupNumToKeep).addAll(rectIntersections.get(groupNumToRemove));
                        
                        for(int intRec : rectIntersections.get(currentGroupNumToKeep)) {
                            rectGroupNum[intRec] = currentGroupNumToKeep;
                        }
                        rectIntersections.get(groupNumToRemove).clear();
                        
                    } else {
                        rectIntersections.get(rectGroupNum[rect1]).add(rect2);
                        rectGroupNum[rect2] = currentGroupNum;
                    }                    
                }
            }
        }

        log.debug("Rect intersections {}", rectIntersections);

        return rectIntersections;
    }
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);

        List<List<Integer>> rectIntersections = new ArrayList<>();
        
        /*
        for(int rect1 = 0; rect1 < input.R; ++rect1) {
            List<Integer> inter = new ArrayList<>();
            inter.add(rect1);
            rectIntersections.add(inter);
        }*/
        
        int rounds = getRoundsBruteForce(input);
                
        
        
        // DecimalFormat decim = new DecimalFormat("0.00000000000");
        // decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

        return ("Case #" + caseNumber + ": " + rounds);
    }
    
    int getRoundsBruteForce(InputData input) {
        GridChar grid = GridChar.buildEmptyGrid(RECT_MAX, RECT_MAX, '0');
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
           // log.debug("Grid {}", grid);
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