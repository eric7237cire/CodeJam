package codejam.y2011.round_1B.house_kittens;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.GraphInt;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.N = scanner.nextInt();
        input.M = scanner.nextInt();
        input.interiorWalls = new ArrayList<>(input.M);
        for(int i = 0; i < input.M; ++i) {
            input.interiorWalls.add(new MutablePair<>(scanner.nextInt(), 0));        
        }
        for(int i = 0; i < input.M; ++i) {
            ((MutablePair<Integer,Integer>) input.interiorWalls.get(i)).setRight(scanner.nextInt());
        }
        return input;
    }
    
    
    @Override
    public String handleCase(InputData input) {
        
        /*
         * List of the pillars (vertices) of each room
         * 
         * index = roomIndex
         */
        List<List<Integer>> roomPillars = new ArrayList<>();
        
        /*
         * List of the walls touching each room.
         * 
         * index = roomIndex
         */
        List<Set<Integer>> roomInteriorWalls = new ArrayList<>();
        
        //generate first set
        List<Integer> allVertices = new ArrayList<>();
        for(int n = 1; n <= input.N; ++n) {
            allVertices.add(n);
        }
        roomPillars.add(allVertices);
        roomInteriorWalls.add( Sets.<Integer>newHashSet());
       
        for(int wallIndex = 0; wallIndex < input.interiorWalls.size(); ++wallIndex) {
            Pair<Integer,Integer> interiorWall = input.interiorWalls.get(wallIndex);
           
            //find the set that contains both vertexes.  This will be the set
            //split by the wall
            int setIndex = 0;
            List<Integer> set = roomPillars.get(setIndex);
            
            while(!set.contains(interiorWall.getLeft()) || !set.contains(interiorWall.getRight())) {
                ++setIndex;
                set = roomPillars.get(setIndex);
            }
            
            
            //Now divide the set into 2            
            List<Integer> set1  = new ArrayList<>();
            List<Integer> set2 = new ArrayList<>();
            
            //go from u to v
            for(int n = interiorWall.getLeft(); n <= interiorWall.getRight(); ++n) {
                if (set.contains(n))
                    set1.add(n);
            }
            
            //go from v to u
            for(int i = interiorWall.getRight(); i <= interiorWall.getLeft() + input.N; ++i) {
                int n = i > input.N ? i - input.N : i;
                if (set.contains(n))
                    set2.add(n);
            }
        
            //Find adjacent interior room walls of set we are going to remove
            
            //wall indexes that are adjacent to the set
            Set<Integer> adjRemove = roomInteriorWalls.get(setIndex);
            
            //Determine which walls are adjacent to the new set split by the wall just added
            Set<Integer> adjSet1 = Sets.newHashSet(wallIndex);
            Set<Integer> adjSet2 = Sets.newHashSet(wallIndex);
            
            for(Integer wallIndexOfRemovedSet : adjRemove) {
                Pair<Integer,Integer> wall = input.interiorWalls.get(wallIndexOfRemovedSet);
                if (set1.contains(wall.getLeft()) && set1.contains(wall.getRight())) {
                    adjSet1.add(wallIndexOfRemovedSet);
                }
                if (set2.contains(wall.getLeft()) && set2.contains(wall.getRight())) {
                    adjSet2.add(wallIndexOfRemovedSet);
                }
            }
            
            
            roomPillars.remove(setIndex);            
            roomInteriorWalls.remove(setIndex);
            
            roomPillars.add(set1);
            roomPillars.add(set2);
            
            roomInteriorWalls.add(adjSet1);
            roomInteriorWalls.add(adjSet2);
            
        }
        
        
        //Now create a map from wall index to room indexes
        List<List<Integer>> wallToRooms = new ArrayList<>();
        for(int wallIndex = 0; wallIndex < input.interiorWalls.size(); ++wallIndex) {
            wallToRooms.add(new ArrayList<Integer>());
        }
        
        for(int roomIndex = 0; roomIndex < roomInteriorWalls.size(); ++roomIndex) {
            for(int wallIndex : roomInteriorWalls.get(roomIndex)) {
                wallToRooms.get(wallIndex).add(roomIndex);
            }
        }
        
        //And finally a graph connecting all rooms that share a wall (ie, share 2 vertices)
        GraphInt graph = new GraphInt();
        
        for(int wallIndex = 0; wallIndex < wallToRooms.size(); ++wallIndex) {
            for(int roomIndex : wallToRooms.get(wallIndex)) {
                for(int room2Index : wallToRooms.get(wallIndex)) {
                    if (roomIndex != room2Index) {
                        graph.addConnection(roomIndex,room2Index);
                    }
                }
            }
        }
        
        //Now do a breadth first ordering
        Queue<Integer> queue = new LinkedList<>();
        LinkedHashSet<Integer> visited = new LinkedHashSet<>();
        queue.add(0);
                
        while(!queue.isEmpty()) {
            int roomIndex = queue.remove();
            
            if (visited.contains(roomIndex))
                continue;
            
            visited.add(roomIndex);
            
            Set<Integer> neighbors = graph.getNeighbors(roomIndex);
            for(Integer neighRoomIndex : neighbors) {                
                queue.add(neighRoomIndex);
            }
        }

        List<List<Integer>> orderedRoomPillars = new ArrayList<>();
        
        for(Integer roomIndex : visited) {
            orderedRoomPillars.add(roomPillars.get(roomIndex));
        }
        
        //do breadth width search of sets
        
        int minVertexCount = Integer.MAX_VALUE;
        
        
        for(int i = 0; i < roomPillars.size(); ++i) {
            List<Integer> set = roomPillars.get(i);
            minVertexCount = Math.min(minVertexCount, set.size());
        }
        
        
        
        int colors = minVertexCount; 
        
        int[] assignment = new int[input.N];
        for(List<Integer> roomPillarIterator : orderedRoomPillars) {
            assignRoom(roomPillarIterator, colors, assignment);
        }
                
                
        return "Case #" + input.testCase + ": " + colors + "\n" + Ints.join(" ",assignment);
    }
    
   
        
    void assignRoom(List<Integer> roomPillars, final int colors, int[] assignment) {

        BitSet usedColors = new BitSet(colors);

        // Find unassigned colors
        for (Integer vertex : roomPillars) {
            int color = assignment[vertex - 1];
            if (color > 0) {
                usedColors.set(color - 1);
            }
        }

        for (int i = 0; i < roomPillars.size(); ++i) {
            int vertex = roomPillars.get(i);
            int prevVertex = roomPillars.get(i == 0 ? roomPillars.size() - 1 : i - 1);
            int nextVertex = roomPillars.get(i == roomPillars.size() - 1 ? 0 : i + 1);

            int color = assignment[vertex - 1];
            int prevColor = assignment[prevVertex - 1];
            int nextColor = assignment[nextVertex - 1];

            // Color already assigned
            if (color != 0) {
                continue;
            }

            // Use an unused color if it exists
            int unusedColor = usedColors.nextClearBit(0);

            if (unusedColor < colors) {
                // usedColors |= unusedColorBit;
                color = unusedColor + 1;
                Preconditions.checkState(1 <= color && color <= colors);
                assignment[vertex - 1] = color;

                usedColors.set(unusedColor);
            } else {
                // Use a color that is not the next or previous color
                for (int tryColor = 1; tryColor <= colors; ++tryColor) {
                    if (prevColor != tryColor && nextColor != tryColor) {
                        assignment[vertex - 1] = tryColor;
                        break;
                    }
                }
            }

        }

    }
    
    /*
    private boolean backtrack(int[] solution, int currentSet, 
            List<List<Integer>> vertexSets, final int colors) {

        
        if (!isValidPartial(vertexSets, solution, colors)) {
            //solution[verticesColored+1] = 0;
            return false;
        }
        if (currentSet == vertexSets.size() && isValid(vertexSets,solution,colors)) {
            return true;
        }
        
        List<Integer> set = vertexSets.get(currentSet);
        
        
        
        assignRoom(set, colors, solution);
        
        boolean r = backtrack(solution, currentSet+1, vertexSets, colors);
        //boolean r = backtrack(solution, currentSet, currentVertex+1, vertexSets, colors);
        Preconditions.checkState(r);
        
        return true;
    }
    */
    
    /*
    private boolean isValidPartial(List<List<Integer>> vertexSets, int[] assignment, int colors) {
        BitSet hasAllColors = new BitSet(colors);
        hasAllColors.set(0, colors, true);
        
        for(List<Integer> set : vertexSets) {
            BitSet colorCheck = new BitSet(colors);
            int blanks = 0;
            int usedColors = 0;
            for(int i = 0; i < set.size(); ++i) {
                int v = set.get(i);
                int prevVertex = set.get( i == 0 ? set.size() - 1 : i - 1);
                int nextVertex = set.get( i == set.size() - 1 ? 0 : i + 1);

                int color = assignment[v-1];
                int prevColor = assignment[prevVertex - 1];
                int nextColor = assignment[nextVertex - 1];
                
                
                if (color == 0) {
                    blanks++;
                } else if (color == prevColor || color == nextColor) {
                    return false;
                }
                else if (!colorCheck.get(color-1)) {
                    colorCheck.set(color-1);
                    usedColors++;
                }
            }
            
            if (usedColors + blanks < colors) 
                return false;
        }
        
        return true;
    }
    
    private
    boolean isValid(List<List<Integer>> vertexSets, int[] assignment, int colors) {
        BitSet hasAllColors = new BitSet(colors);
        hasAllColors.set(0, colors, true);
        
        for(List<Integer> set : vertexSets) {
            BitSet colorCheck = new BitSet(colors);
            for(Integer v : set) {
                int color = assignment[v-1];
                colorCheck.set(color-1);
            }
            if (!colorCheck.equals(hasAllColors)) 
                return false;
        }
        
        return true;
    }
    */
}
