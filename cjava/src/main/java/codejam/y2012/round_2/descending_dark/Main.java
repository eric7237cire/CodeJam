package codejam.y2012.round_2.descending_dark;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        // return new String[] {"sample.in"};
        return new String[] {"D-small-practice.in"};
       // return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);
        
        /*
         * The first line of the input gives the number of test cases, 
         * T. T test cases follow, 
         * beginning with a line containing integers R and C, 
         * representing the number of rows and columns in the mountain layout.

    This is followed by R lines, each containing C characters,
     describing a mountain layout. As in the example above, 
     a '#' character represents an impassable square, a '.' 
     character represents a passable square, 
     and the digits '0'-'9' represent caves 
     (which are also passable squares).
         */
        
        in.R = scanner.nextInt();
        in.C = scanner.nextInt();
        in.grid = GridChar.buildFromScanner(scanner, in.R, in.C, '#');
                
        return in;
    }
    
    Integer getCaveSquares(GridChar grid, int caveNum, Set<Integer> caveSquares) {
        
        Preconditions.checkArgument(caveNum >=0 && caveNum < 10);
        
        Set<Integer> caveLocs = grid.getIndexesOf(Integer.toString(caveNum).charAt(0));
        
        if (caveLocs.isEmpty())
            return null;
        
        Preconditions.checkState(caveLocs.size() == 1);
        
        Set<Integer> visited = caveSquares;
        
        Queue<Integer> toVisit = new LinkedList<>();
        
        final int caveLoc  = caveLocs.iterator().next();
        
        toVisit.add(caveLoc);
        
        while(!toVisit.isEmpty()) {
            Integer curLoc = toVisit.poll();
            
            if (visited.contains(curLoc)) {
                continue;
            }
            
            visited.add(curLoc);
            
            char right = grid.getEntry(curLoc, Direction.EAST);
            
            if (Character.isDigit(right) || right == '.') {
                toVisit.add(grid.getIndex(curLoc,Direction.EAST));
            }
            
            char left = grid.getEntry(curLoc, Direction.WEST);
            
            if (Character.isDigit(left) || left == '.') {
                toVisit.add(grid.getIndex(curLoc,Direction.WEST));
            }
            
            char up = grid.getEntry(curLoc, Direction.NORTH);
            
            if (Character.isDigit(up) || up == '.') {
                toVisit.add(grid.getIndex(curLoc,Direction.NORTH));
            }
        }
        
        return caveLoc;
    }
    
    final static int RIGHT = 2;
    final static int LEFT = 1;
    final static int DOWN = 0;
    
    int[][] buildTransitions(InputData in) {
        int[][] transitions = new int[in.C*in.R][3];
        GridChar grid = in.grid; 
        
        for(int curLoc = 0; curLoc < in.grid.getSize(); ++curLoc) {
            char cur = grid.getEntry(curLoc);
            
            if (cur == '#') {
                transitions[curLoc] = null;
            }
            
            transitions[curLoc] = new int[] {curLoc, curLoc, curLoc};
            
            char right = grid.getEntry(curLoc, Direction.EAST);
            
            if (Character.isDigit(right) || right == '.') {
                transitions[curLoc][RIGHT] = grid.getIndex(curLoc,Direction.EAST);
            }
            
            char left = grid.getEntry(curLoc, Direction.WEST);
            
            if (Character.isDigit(left) || left == '.') {
                transitions[curLoc][LEFT] = grid.getIndex(curLoc,Direction.WEST);
            }
            
            char down = grid.getEntry(curLoc, Direction.SOUTH);
            
            if (Character.isDigit(down) || down == '.') {
                transitions[curLoc][DOWN] = grid.getIndex(curLoc,Direction.SOUTH);
            }
        }
        
        return transitions;
    }
    
    public String handleCase(InputData in) {

        
        int[][] transitions = buildTransitions(in);
        
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Case #%d:\n", in.testCase));
        
        for(int d = 0; d <= 9; ++d) {
            Set<Integer> caveSquares = Sets.newHashSet();
            Integer caveLoc = getCaveSquares(in.grid, d, caveSquares);
            
            if (caveLoc == null)
                continue;
            
            List<Integer> startState = Lists.newArrayList(caveSquares);
            
            List<Integer> finalState = Lists.newArrayList(startState);
            for(int i = 0; i < finalState.size(); ++i) {
                finalState.set(i, caveLoc);
            }
            
            Queue<List<Integer>> toVisit = new LinkedList<>();
            Set<List<Integer>> visited = Sets.newHashSet();
            
            toVisit.add(startState);
            
            boolean lucky = false;
            
            while(!toVisit.isEmpty()) {
                List<Integer> state = toVisit.poll();
                
                if (state.equals(finalState)) {
                    lucky = true;
                    break;
                }
                
                if (visited.contains(state)) {
                    continue;
                }
                
                visited.add(state);
                
                for(int move = 0; move < 3; ++move) {
                    List<Integer> newState = Lists.newArrayList();
                    boolean add = true;
                    
                    for(int stateIdx = 0; stateIdx < state.size(); ++stateIdx) {
                        newState.add( transitions[state.get(stateIdx)][move]);
                        if (!caveSquares.contains(newState.get(stateIdx))) {
                            add = false;
                            break;
                        }
                        
                    }
                    if (add) {
                    toVisit.add(newState);
                    }
                }
            }
        
            sb.append(String.format("%d: %d %s\n", d, caveSquares.size(), lucky ? "Lucky" : "Unlucky" ));
            
        }
        
        return sb.toString();
    }

}
