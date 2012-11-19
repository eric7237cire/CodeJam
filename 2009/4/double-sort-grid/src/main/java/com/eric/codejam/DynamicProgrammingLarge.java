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
        
        if (pathKey == paths.size() - 2) {
            int sum = maxCharacter;
            checkPath(pathKey, maxCharacter, sum);
            return maxCharacter;
        }
        if (pathKey == paths.size() - 1) {
            int sum = 0;
            //checkPath(pathKey, maxCharacter, sum);
            return 0;
        }
        if (maxCharacter <= 0) {
            return 0;
        }
        Preconditions.checkArgument(pathKey < paths.size() - 2);
        //Find all maximal points
        List<Integer> path = paths.get(pathKey);
        
        List<List<Integer>> newPath = new ArrayList<>();
        
        //Basically when the path has a corner
        List<Integer> maximalPoints = new ArrayList<>();
        
        int sum = 0;
        
        boolean atLeastOneEqualMaxCharacter = false;
        
        List<Integer> pathWithoutPrefilled = new ArrayList<Integer>(path);
        
        for(int pathRowIdx = 0; pathRowIdx < grid.getCols(); ++pathRowIdx ) {
            //path row is one after the row
            int currentPathRow = path.get(pathRowIdx);
            int nextPathRow = pathRowIdx == grid.getCols() - 1 ? 0 : path.get(pathRowIdx+1);
            
            Preconditions.checkState(nextPathRow <= currentPathRow);
            
            if (nextPathRow < currentPathRow) {
                
                int letter = grid.getEntry(currentPathRow-1, pathRowIdx);
                
                if (letter > maxCharacter) {
                    log.debug("Invalid letter ");
                    sum = 0;
                    checkPath(pathKey, maxCharacter, sum);
                    return 0;
                }
                
                if (letter != 0 ) {
                    Preconditions.checkState(letter <= maxCharacter);
                  
                    if (letter == maxCharacter) {
                        atLeastOneEqualMaxCharacter = true;
                    }
                    pathWithoutPrefilled.set(pathRowIdx, pathWithoutPrefilled.get(pathRowIdx) - 1);
                    Preconditions.checkState(pathWithoutPrefilled.get(pathRowIdx) >= 0);
                    continue;
                }
                
                //atLeastOneEqualMaxCharacter = true;
                                
                //We have found a maximal point.  Row = currentPathRow, Col = pathRowIdx
                maximalPoints.add(grid.getIndex(currentPathRow-1, pathRowIdx));
                List<Integer> newPathWithoutMaxPoint = new ArrayList<>(path);
                newPathWithoutMaxPoint.set(pathRowIdx, newPathWithoutMaxPoint.get(pathRowIdx) - 1);
                Preconditions.checkState(newPathWithoutMaxPoint.get(pathRowIdx) >= 0);
                
                newPath.add(newPathWithoutMaxPoint);
                
            }
            
            
        }
        
        if (!atLeastOneEqualMaxCharacter && maximalPoints.size() == 0) {
            sum = solve(pathKey, maxCharacter-1);
            checkPath(pathKey, maxCharacter, sum);
            return sum;
        }
        
        if (maximalPoints.size() == 0) {
            int newPathIdx = pathToIndex.get(pathWithoutPrefilled);
            Preconditions.checkState(newPathIdx != pathKey);
            
            sum = solve(newPathIdx, maxCharacter);
            checkPath(pathKey, maxCharacter, sum);
            return sum;
        }
        
        if (maximalPoints.size() == 1 && grid.getEntry(maximalPoints.get(0)) == 0) {
            int pathIndex = pathToIndex.get(newPath.get(0));
            for(int letter = maxCharacter ; letter >= 1; -- letter) {
                sum += solve(pathIndex, letter);
            }
            checkPath(pathKey, maxCharacter, sum);
            return sum;
        }
        
        int allSubSets = (1 << maximalPoints.size()) - 1; 
        
        for(int subSetsBitSet = 1; subSetsBitSet <= allSubSets; ++subSetsBitSet) {
            int numBits = Integer.bitCount(subSetsBitSet);
            int addOrSub = numBits % 2 == 0 ? -1 : 1;
            
            List<List<Integer>> pathsToIntersect = new ArrayList<>();
            //improve
            for(int j = 0; j < maximalPoints.size(); ++j ) {
                if ( ((1 << j) & subSetsBitSet) > 0) {
                    pathsToIntersect.add(newPath.get(j));
                }
            }
            
            Preconditions.checkState(pathsToIntersect.size() == numBits);
            Preconditions.checkState(pathsToIntersect.size() > 0);
            
            List<Integer> intersectedPath = new ArrayList<>(pathWithoutPrefilled);
            
            for(List<Integer> pathToIntersect : pathsToIntersect) {
                for(int ptoi = 0; ptoi < grid.getCols(); ++ptoi) {
                    intersectedPath.set(ptoi, Math.min(pathToIntersect.get(ptoi), intersectedPath.get(ptoi)));
                }
                
            }
            
            int intPathIndex = pathToIndex.get(intersectedPath);
            
            for(int maxChar = maxCharacter; maxChar >= 1; maxChar --) {
               // if (maximalPoints.get)
            //sum += addOrSub * solve(intPathIndex, maxCharacter);
                sum += addOrSub * solve(intPathIndex, maxChar);
            }
            log.debug("Intersection {}, sum is now {}", intersectedPath, sum);
        }
        
        //int newPathIndex = pathToIndex.get(newPath);
        
        //sum += solve(pathKey, maxCharacter-1);
        
        /*
            for(int letter = maxCharacter-1 ; letter >= 1; -- letter) {
                sum += solve(newPathIndex, letter);
            }
        */
        
        log.debug("Returning {} for path {} <= {}", sum, path, maxCharacter);
        checkPath(pathKey, maxCharacter, sum);
        return sum;
        
        //
    }
    
    public void checkPath(int pathKey, int maxLetter, int sum) {
        Grid<Integer> checkGrid = new Grid<Integer>(grid);
        checkGrid.addRow(maxLetter);
        List<Integer> path = paths.get(pathKey);
        for(int pathRowIdx = 0; pathRowIdx < grid.getCols(); ++pathRowIdx ) {
            //path row is one after the row
            int currentPathRow = path.get(pathRowIdx);
            
            for(int r = currentPathRow; r < checkGrid.getRows(); ++ r) {
                checkGrid.setEntry(r,pathRowIdx, maxLetter);
            }
        }
        
        int count = BruteForce.count(checkGrid, checkGrid);
        Preconditions.checkState(count == sum);
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
