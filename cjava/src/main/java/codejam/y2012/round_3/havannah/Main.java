package codejam.y2012.round_3.havannah;

import java.util.BitSet;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2012.round_3.havannah.DynamicUnionFind.Component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles
{

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles()
    {
        return new String[] { "sample.in" };
        //    return new String[] { "B-small-practice.in" };
        //  return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }


    /**
     * Corners have an index 0 to 5 inclusive
     * @param in
     * @param cellToIndex
     */
    void enumerateCorners(InputData in, Map<Pair<Integer,Integer>, Integer> cellToIndex)
    {
        cellToIndex.put(new ImmutablePair<>(1, 1), 0);
        cellToIndex.put(new ImmutablePair<>(1, in.S), 1);
        cellToIndex.put(new ImmutablePair<>(in.S, in.S*2-1), 2);
        cellToIndex.put(new ImmutablePair<>(in.S*2-1, in.S*2-1), 3);
        cellToIndex.put(new ImmutablePair<>(in.S*2-1, in.S), 4);
        cellToIndex.put(new ImmutablePair<>(in.S, 1), 5);
    }
    
    /**
     * 
     *  {(1, X), (X, 1), (X, S * 2 - 1), (S * 2 - 1, X)}
     *   where X is any integer, together with all cells for which |row - col| == S - 1.
     *  
     *  Edges given index 6 to 11
     *  
     * @param in
     * @param cellToIndex
     */
    
    void enumerateEdges(InputData in, Map<Pair<Integer,Integer>, Integer> cellToIndex)
    {
        
        int startSize = cellToIndex.size();

        for(int i = 2; i <= in.S-1; ++i) {
            //Lower right
            cellToIndex.put(new ImmutablePair<>(1, i), 6);
            
            //Lower left
            cellToIndex.put(new ImmutablePair<>(i, 1), 11);
        }
        
        Preconditions.checkState(startSize + (in.S-2) * 2 == cellToIndex.size());
        
        //Right
        int a = 2;
        int b = in.S + 1;
        
        //Side is S minus both corners
        for(int i = 0; i < in.S - 2; ++i)
        {
            cellToIndex.put(new ImmutablePair<>(a+i, b+i), 7);
            
            cellToIndex.put(new ImmutablePair<>(b+i, a+i), 10);
        }
        
        Preconditions.checkState(startSize + (in.S-2) * 4 == cellToIndex.size());
        
        //Upper right edge and upper left
        for( a = in.S + 1; a <= 2*in.S - 1; ++ a) {
            cellToIndex.put(new ImmutablePair<>(a, 2*in.S-1), 8);
            
            b = a;
            cellToIndex.put(new ImmutablePair<>(2*in.S-1, b), 9);
        }        
        
        int endSize = cellToIndex.size();
        
        //Make sure all edges added
        Preconditions.checkState(startSize + (in.S-2) * 6 == endSize);
        
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);

        in.S = scanner.nextInt();
        in.Moves = scanner.nextInt();
        
        in.moveList = Lists.newArrayList();
        
        for(int i = 0; i < in.Moves; ++i) {
            in.moveList.add(new ImmutablePair<>(scanner.nextInt(), scanner.nextInt()));
        }
        
        return in;
    }

    int[][] neighborDeltas = new int[][]
            {
            {1,1},
            {0,1},
            {-1,0},
            {-1,-1},
            {0,-1},
            {1,0}
            };
    
    
    
    
    public String handleCase(InputData in)
    {
        Map<Pair<Integer,Integer>, Integer> cellToIndex = Maps.newHashMap();
        
        enumerateCorners(in, cellToIndex);
        enumerateEdges(in, cellToIndex);
        
        BitSet edgeMask = new BitSet();
        BitSet cornerMask = new BitSet();
        for(int i = 0; i <= 5; ++i) {
            cornerMask.set(i);
            edgeMask.set(i+6);
        }
        

        DynamicUnionFind uf = new DynamicUnionFind(112);
        
        StringBuffer r = new StringBuffer( String.format("Case #%d: ", in.testCase) );
        
        for(int moveIdx = 0; moveIdx < in.moveList.size(); ++moveIdx) {
            
            Pair<Integer,Integer> move = in.moveList.get(moveIdx);
            
            //Change into an id
            int moveId = -1;
            if (!cellToIndex.containsKey(move)) {
                moveId = cellToIndex.size();
                cellToIndex.put(move, moveId);
            } else {
                moveId = cellToIndex.get(move);
            }
            
            uf.addNode(moveId);
            
            for( int[] delta : neighborDeltas) {
                Pair<Integer,Integer> neighbor = new ImmutablePair<>(
                        move.getLeft()+delta[0],
                        move.getRight()+delta[1]);
                
                Integer neighborId = cellToIndex.get(neighbor);
                
                if (neighborId == null)
                    continue;
                
                if (uf.getGroupNumber(neighborId) == null)
                    continue;
                
                uf.mergeComponentsOfNodes(moveId, neighborId);
            }
            
            Component com = uf.getGroup(moveId);
            
            BitSet edges = BitSet.valueOf(edgeMask.toLongArray());
            edges.and( com.members );
            
            BitSet corners = BitSet.valueOf(cornerMask.toLongArray());
            corners.and( com.members );
            
            boolean hasBridge = corners.cardinality() >= 2;
            boolean hasFork = edges.cardinality() >= 3;
            boolean hasRing = false;
            
            if (!hasRing && !hasFork && !hasBridge)
                continue;
            
            if (hasFork && hasRing && hasBridge) {
                r.append(String.format("bridge-fork-ring in move %d", moveIdx+1));
            } else if (hasFork && hasRing) {
                r.append(String.format("fork-ring in move %d", moveIdx+1));  
            } else if (hasBridge && hasRing) {
                r.append(String.format("bridge-ring in move %d", moveIdx+1));
            } else if (hasFork && hasBridge) {
                r.append(String.format("bridge-fork in move %d", moveIdx+1));
            } else if (hasRing) {
                r.append(String.format("ring in move %d", moveIdx+1));
            } else if (hasFork) {
                r.append(String.format("fork in move %d", moveIdx+1));
            } else if (hasBridge) {
                r.append(String.format("bridge in move %d", moveIdx+1));
            }
        
            return r.toString();
        }



        r.append("none");
        return r.toString();
    }

    
}
