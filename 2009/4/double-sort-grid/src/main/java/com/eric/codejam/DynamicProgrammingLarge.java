package com.eric.codejam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.utils.Grid;
import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

public class DynamicProgrammingLarge {
    
    final static Logger log = LoggerFactory.getLogger(DynamicProgrammingLarge.class);

    //A path goes from bottom left to top right, each step goes to the right one.
    //paths [ index of path ] [ row values ]
    //
    List<List<Integer>> paths; 
    Grid<Integer> grid;
    
    
    Map<List<Integer>, Integer> pathToIndex;
    
    public DynamicProgrammingLarge(Grid<Integer> grid) {
        this.grid = grid;
        
        paths = new ArrayList<>();
        pathToIndex = new HashMap<>();
        createAllPaths(new ArrayList<Integer>(), grid.getRows(), grid.getCols());   
        
        int totalPathNumber = IntMath.binomial(grid.getRows()+grid.getCols(), grid.getRows());
        Preconditions.checkState(totalPathNumber == paths.size());
    }
    
    public int solve(int pathKey, int maxCharacter) {
        
        if (pathKey == 1) {
            return maxCharacter;
        }
        //Find all maximal points
        List<Integer> path = paths.get(pathKey);
        
        List<Integer> newPath = new ArrayList<>(path);
        
        //Basically when the path has a corner
        List<Integer> maximalPoints = new ArrayList<>();
        boolean foundMaxPoint = false;
        
        int sum = 0;
        
        for(int pathRowIdx = 0; pathRowIdx < grid.getCols(); ++pathRowIdx ) {
            //path row is one after the row
            int currentPathRow = path.get(pathRowIdx);
            int nextPathRow = pathRowIdx == grid.getCols() - 1 ? 0 : path.get(pathRowIdx+1);
            
            Preconditions.checkState(nextPathRow <= currentPathRow);
            
            if (nextPathRow < currentPathRow) {
                
                int letter = grid.getEntry(currentPathRow-1, pathRowIdx);
                
                if (letter > maxCharacter) {
                    log.debug("Invalid letter ");
                    return 0;
                }
                
                if (letter != 0 && letter <= maxCharacter) {
                    newPath.set(pathRowIdx, newPath.get(pathRowIdx) - 1);
                    Preconditions.checkState(newPath.get(pathRowIdx) >= 0);
                    continue;
                }
                
                if (!foundMaxPoint) {
                //We have found a maximal point.  Row = currentPathRow, Col = pathRowIdx
                maximalPoints.add(grid.getIndex(currentPathRow, pathRowIdx));
                newPath.set(pathRowIdx, newPath.get(pathRowIdx) - 1);
                Preconditions.checkState(newPath.get(pathRowIdx) >= 0);
                foundMaxPoint = true;
                }
                
            }
            
            
        }
        
        
        int newPathIndex = pathToIndex.get(newPath);
        
        sum += solve(newPathIndex, maxCharacter);
        
        if (foundMaxPoint) {
            for(int letter = maxCharacter - 1; letter >= 1; -- letter) {
                sum += solve(newPathIndex, letter);
            }
        }
        
        log.debug("Returning {} for path {}", sum, path);
        return sum;
        
        //
    }
    
    public static int LETTER_MAX = 4;
    
    public static Integer solveGrid(Grid<Integer> grid) {

        DynamicProgrammingLarge ss = new DynamicProgrammingLarge(grid);
        
        return ss.solve(0, LETTER_MAX);

    }
    
    private void createAllPaths(List<Integer> pathSoFar, int beforeRowValue, int cols) {
        if (pathSoFar.size() == cols) {
            log.debug("Path {} : {}", paths.size(), pathSoFar);
            paths.add(new ArrayList<Integer>(pathSoFar));
            pathToIndex.put(paths.get(paths.size()-1), paths.size()-1);
            return;
        }
        
        for(int i = beforeRowValue; i >= 0; --i) {
            pathSoFar.add(i);
            createAllPaths(pathSoFar, i, cols);
            pathSoFar.remove(pathSoFar.size() -1);
        }
    }
}
