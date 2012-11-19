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
    
    int[][] memoize ;
    Map<List<Integer>, Integer> pathToIndex;
    
    final static int MOD = 10007;
    public static int LETTER_MAX = 26;
    
    public DynamicProgrammingLarge(Grid<Integer> grid) {
        this.grid = grid;
        
        paths = new ArrayList<>();
        pathToIndex = new HashMap<>();
        createAllPaths(new ArrayList<Integer>(), grid.getRows(), grid.getCols());   
        
        memoize = new int[paths.size()][LETTER_MAX+1];
        for(int i= 0; i < paths.size(); ++i) {
            for(int j = 0; j <= LETTER_MAX; ++j) {
                memoize[i][j] = -1;
            }
        }
        int totalPathNumber = IntMath.binomial(grid.getRows()+grid.getCols(), grid.getRows());
        Preconditions.checkState(totalPathNumber == paths.size());
    }
    
    
    
    public int solve(int pathKey, int maxCharacter) {
        
        if (memoize[pathKey][maxCharacter] >= 0) {
            return memoize[pathKey][maxCharacter];
        }
        
        if (pathKey == paths.size() - 2) {
            int letter = grid.getEntry(0);
            int sum = 0;
            if (maxCharacter < letter) {
                sum = 0;
            } else if ( letter == 0) {
                sum = maxCharacter;
            } else {
                sum = 1;
            }
            checkPath(pathKey, maxCharacter, sum);
            sum %= MOD;
            return sum;
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
                
                if (letter != 0 && letter == maxCharacter) {
                    Preconditions.checkState(letter <= maxCharacter);
                  
                    if (letter == maxCharacter) {
                        atLeastOneEqualMaxCharacter = true;
                    }
                    //pathWithoutPrefilled.set(pathRowIdx, pathWithoutPrefilled.get(pathRowIdx) - 1);
                    //Preconditions.checkState(pathWithoutPrefilled.get(pathRowIdx) >= 0);
                    //continue;
                }
                
                
                //atLeastOneEqualMaxCharacter = true;
                                if (letter == 0 || letter == maxCharacter) {
                //We have found a maximal point.  Row = currentPathRow, Col = pathRowIdx
                maximalPoints.add(grid.getIndex(currentPathRow-1, pathRowIdx));
                List<Integer> newPathWithoutMaxPoint = new ArrayList<>(path);
                newPathWithoutMaxPoint.set(pathRowIdx, newPathWithoutMaxPoint.get(pathRowIdx) - 1);
                Preconditions.checkState(newPathWithoutMaxPoint.get(pathRowIdx) >= 0);
                
                newPath.add(newPathWithoutMaxPoint);
                                }
            }
            
            
        }
        
        boolean atLeastOneZeroOrMaxLetter = false;
        boolean atLeasteOneNonZero = false;
        for(Integer maxPointGridIdx : maximalPoints) {
            int maxPointLetter = grid.getEntry(maxPointGridIdx);
            if (maxPointLetter == 0 || maxPointLetter == maxCharacter) {
                atLeastOneZeroOrMaxLetter = true;
                break;
            }
            if (maxPointLetter != 0) {
                atLeasteOneNonZero = true;
            }
        }
        if (!atLeastOneZeroOrMaxLetter) {
            sum = solve(pathKey, maxCharacter-1);
            checkPath(pathKey, maxCharacter, sum);
            return sum;
        }
        
        if (maximalPoints.size() == 0) {
            int newPathIdx = pathToIndex.get(pathWithoutPrefilled);
            Preconditions.checkState(newPathIdx != pathKey);
            
            sum = solve(newPathIdx, maxCharacter);
            sum %= MOD;
            checkPath(pathKey, maxCharacter, sum);
            return sum;
        }
        
        //if (!atLeasteOneNonZero) {
        sum = solve(pathKey, maxCharacter - 1);
        //}
        log.debug("Sum starting {} for path {}", sum, path);
        /*
        if (maximalPoints.size() == 1 && grid.getEntry(maximalPoints.get(0)) == 0) {
            int pathIndex = pathToIndex.get(newPath.get(0));
            for(int letter = maxCharacter ; letter >= 1; -- letter) {
                sum += solve(pathIndex, letter);
            }
            checkPath(pathKey, maxCharacter, sum);
            return sum;
        }*/
        
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
            
           // for(int maxChar = maxCharacter; maxChar >= 1; maxChar --) {
               // if (maximalPoints.get)
            int subSum = solve(intPathIndex, maxCharacter);
            sum += addOrSub * subSum;
            sum %= MOD;
              //  sum += addOrSub * solve(intPathIndex, maxChar);
           // }
            log.debug("Intersection {} for path {} has sum {}, cumul is now {}", intersectedPath, path, subSum,sum);
        }
        
        //int newPathIndex = pathToIndex.get(newPath);
        
        //sum += solve(pathKey, maxCharacter-1);
        
        /*
            for(int letter = maxCharacter-1 ; letter >= 1; -- letter) {
                sum += solve(newPathIndex, letter);
            }
        */
        
        if (sum >= MOD) {
            sum -= MOD;
        }
        if (sum < 0) {
            sum += MOD;
        }
        log.debug("Returning {} for path {} <= {}", sum, path, maxCharacter);
        checkPath(pathKey, maxCharacter, sum);
        memoize[pathKey][maxCharacter] = sum;
        return sum;
        
        //
    }
    
    public void checkPath(int pathKey, int maxLetter, int sum) {
        if (1==1) 
                return;
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
