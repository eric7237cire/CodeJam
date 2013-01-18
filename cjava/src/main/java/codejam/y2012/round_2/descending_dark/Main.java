package codejam.y2012.round_2.descending_dark;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.BitSetLong;
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
         return new String[] {"sample.in"};
        //return new String[] {"D-small-practice.in"};
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
    
    /*
    old_safety = [SAFE] * (n+1)
            for length in {n, n-1, ..., 1}:
              for i in [0, length-1]:
                pos_safety[i] = best(old_safety[i], old_safety[i+1])
                if moving down leaves SC:
                  pos_safety[i] = UNSAFE
                elif moving down is legal and pos_safety[i] == SAFE:
                  pos_safety[i] = SAFE_WITH_PROGRESS
              old_safety = pos_safety
            return (pos_safety[0] == SAFE_WITH_PROGRESS)
            */
                    
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
    
    /**
     * Used when treating this problem as an intersection of 
     * state machines.  Basically for a grid index and a direction,
     * we return the next grid index.
     * @param in
     * @return
     */
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
    
    static class Segment
    {
        int length;
        //Moving down remains in reachable squares
        BitSetLong safeToGoDown;
        
        //Moving down leaves reachable squares, index from start of segment
        BitSetLong unsafeToGoDownBitSet;
        
        Segment() {
            length = 0;
            safeToGoDown = new BitSetLong();
            unsafeToGoDownBitSet = new BitSetLong();
        }

        @Override
        public String toString() {
            return "Segment [length=" + length + ", safeToGoDown=" + safeToGoDown + ", unsafeToGoDownBitSet=" + unsafeToGoDownBitSet + "]";
        }
    }
    
    void buildSegments(GridChar grid, Set<Integer> reachableSquares, List<Segment> segments) {
        
        for(int reachableSquareGridIndex : reachableSquares) 
        {
            //To be the start of a segment, directly to the left must not be reachable
            Integer leftGridIndex = grid.getIndex(reachableSquareGridIndex, Direction.WEST);
            if (reachableSquares.contains(leftGridIndex))
                continue;
            
            Integer currentIndex = reachableSquareGridIndex;
            int currentOffset = 0;
            
            Segment s = new Segment();
            
            while(reachableSquares.contains(currentIndex)) {
                Integer belowIndex = grid.getIndex(currentIndex, Direction.SOUTH);
                char sqBelow = grid.getEntry(belowIndex);
                
                if (sqBelow == '#') {
                    //neither safe to go down nor unsafe
                } else if (reachableSquares.contains(belowIndex)) {
                    //Safe to go down
                    s.safeToGoDown.set(currentOffset);
                } else {
                    //Not safe to go down, we leave the set of reachable squares
                    s.unsafeToGoDownBitSet.set(currentOffset);
                }
                
                ++currentOffset;
                currentIndex = grid.getIndex(currentIndex, Direction.EAST);
            }
            
            s.length = currentOffset;
            segments.add(s);
            
        }
    }
    
    public String handleCase(InputData in) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Case #%d:\n", in.testCase));
        
        for(int d = 0; d <= 9; ++d) {
            Set<Integer> caveSquares = Sets.newHashSet();
            Integer caveLoc = getCaveSquares(in.grid, d, caveSquares);
            
            if (caveLoc == null)
                continue;
            
            List<Segment> segments = Lists.newArrayList();
            
            buildSegments(in.grid, caveSquares, segments);
            
            while (true) {
                int maxLen = 0;
                for (Segment s : segments)
                    maxLen = Math.max(maxLen, s.length);
                
                //Go through each segment, for each length we combine
                //which squares are not safe to go down
                long[] badByLen = new long[maxLen + 1];
                for (Segment s : segments) {
                    badByLen[s.length] |= s.unsafeToGoDownBitSet.getBits();
                }
                
                /*
                 * Build up possible moves starting at smallest segment
                 */
                BitSetLong[] possible = new BitSetLong[maxLen + 1];
                possible[1] = new BitSetLong(1);
                for (int len = 1; len <= maxLen; ++len) {
                    
                    //Weed out squares unsafe to go down
                    possible[len].unsetMultiple(badByLen[len]);
                    
                    /*Say we have len = 3 and
                     * possible[3] = 101
                     *      offset 2 1 0
                     *      
                     * This means for the next interval we can be either
                     * in what is possible or 1 to the right
                     *  101
                     * 101-
                     * 
                     * This is explained better by the proof.  Basically
                     * len 3 [] [] [] 
                     * len 4 [] [] [] []
                     * in len 4 I can do moves to be either same offset as len 3
                     * or 1 to the right.  
                     */
                    
                    if (len < maxLen) {
                        possible[len + 1] = new BitSetLong( possible[len].getBits() | (possible[len].getBits() << 1) );
                    }
                }
                
                //Do the same logic but from the top ... TODO Why?
                //maybe to cover the badByLen in the other direction
                for (int len = maxLen; len > 1; --len) {
                    possible[len - 1].restrictToSet( possible[len].getBits() | (possible[len].getBits() >> 1) );
                }
                List<Segment> remaining = new ArrayList<Segment>();
                for (Segment s : segments)
                    //If we are still in the segment
                    if ((s.safeToGoDown.getBits() & possible[s.length].getBits()) == 0) {
                        remaining.add(s);
                    }
                if (remaining.size() == segments.size())
                    break;
                segments = remaining;
            }
            
            //One segment remaining means we are in the horizontal segment containing the cave
            boolean lucky = segments.size() == 1; 
            
            sb.append(String.format("%d: %d %s\n", d, caveSquares.size(), lucky ? "Lucky" : "Unlucky" ));
        }
        
        return sb.toString();
    }
    public String handleCaseBruteForce(InputData in) {

        
        int[][] transitions = buildTransitions(in);
        
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("Case #%d:\n", in.testCase));
        
        for(int d = 0; d <= 9; ++d) {
            Set<Integer> caveSquares = Sets.newHashSet();
            Integer caveLoc = getCaveSquares(in.grid, d, caveSquares);
            
            if (caveLoc == null)
                continue;
            
            //State is the cross product of each individual state machine
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
