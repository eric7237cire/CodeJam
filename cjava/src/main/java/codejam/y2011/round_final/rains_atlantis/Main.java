package codejam.y2011.round_final.rains_atlantis;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.Grid;

import com.google.common.base.Preconditions;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] { "sample.in" };
     //    return new String[] { "B-small-practice.in" };
       //  return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        in.H = scanner.nextInt();
        in.W = scanner.nextInt();
        in.M = scanner.nextLong();
        
        in.grid =  Grid.buildFromScanner(scanner, in.H, in.W, Grid.fromScannerLong, 0l);
        
        
        return in;
    }

    void determineWaterLevel(Grid<Long> land, Grid<Long> waterLevel)
    {
        //Find cheapest path to edge
        
        //level location
        Queue< Integer > toVisit = new ArrayDeque<Integer>();
        
        for(int gi = 0; gi < land.getSize(); ++gi) {
            waterLevel.setEntry(gi, Long.MAX_VALUE);
            toVisit.add( gi );
        }
        
        while(!toVisit.isEmpty()) {
            Integer gridIndex = toVisit.poll();
            
            long nLevel = Long.MAX_VALUE;
            
            for(int d = 0; d < 4; ++d) {
                //The grid will return 0 for adj to edge
                nLevel = Math.min(nLevel, waterLevel.getEntry( gridIndex, waterLevel.directions[d]) );
            }
            
            //Level can not be lower than the land
            nLevel = Math.max(nLevel, land.getEntry(gridIndex));
            
            //If we have not improved the square, move on
            if (nLevel >= waterLevel.getEntry(gridIndex))
                continue;
            
            //Improve entry
            waterLevel.setEntry(gridIndex, nLevel);
            
            //Recheck the adjacent squares
            for(int dir = 0; dir < 4; ++dir) {
                Integer index = waterLevel.getIndex(gridIndex, waterLevel.directions[dir]);
                if (index == null) 
                    continue;
                
                toVisit.add(index);
            }
            
        }
        
        
        
    }
    
    
        void oldDetermineWaterLevel(Grid<Long> grid, int gridIndex)
        {
            //Find cheapest path to edge
            
            //level location
            PriorityQueue< Pair<Long, Integer> > toVisit = new PriorityQueue<>();
            
            boolean[] seen = new boolean[grid.getSize()];
            
            toVisit.add(new ImmutablePair<>(grid.getEntry(gridIndex), gridIndex));
            
            while(!toVisit.isEmpty()) {
                Pair<Long, Integer> levelIndex = toVisit.poll();
                
                if (seen[levelIndex.getRight()])
                    continue;
                
                
                seen[levelIndex.getRight()] = true;
                
                if (grid.minDistanceToEdge(levelIndex.getRight()) == 0) 
                {
                    grid.setEntry(gridIndex, Math.max(grid.getEntry(gridIndex), levelIndex.getLeft()));
                    return;
                }
                
                for(int dir = 0; dir <= 3; ++dir) {
                    Integer index = grid.getIndex(levelIndex.getRight(), Direction.NORTH.turn(2*dir));
                    if (index == null)
                        continue;
                    
                    //Max because the water 
                    toVisit.add(new ImmutablePair<>( Math.max( levelIndex.getLeft(), grid.getEntry(index)), index));
                }
                
            }
            
            
            log.error("ERror");
            
        }
        
        
        
    static class RetInfo {
        long minErosionAmount;
        long minWaterOnTop;
        boolean existEmergedFields;
        long maxLandHeight;
        
        RetInfo() {
            minErosionAmount = Long.MAX_VALUE;
            minWaterOnTop = Long.MAX_VALUE;
            
            existEmergedFields = false;
            maxLandHeight = 0;
        }
    }
    /**
     * 
     * @param land
     * @param waterLevel
     * @param nextGrid
     * @param maxErosion
     * @return smallest submerged water level
     */
    RetInfo doErosion(Grid<Long> land, Grid<Long> waterLevel, Grid<Long> nextGrid, long maxErosion) {
        RetInfo r = new RetInfo();
                
        for(int index = 0; index < waterLevel.getSize(); ++index) {
            
            long minWaterLevel = Long.MAX_VALUE;
            
            //Find minimum adjacent square
            for(int dir = 0; dir <= 3; ++dir) {
                // Integer adjIndex = waterLevel.getIndex(index, Direction.NORTH.turn(2*dir));
                
                Long adjWaterLevel = waterLevel.getEntry(index, Direction.NORTH.turn(2*dir));
                
                minWaterLevel = Math.min(minWaterLevel, adjWaterLevel);
            }
            
            Long curWaterLevel = waterLevel.getEntry(index);
            Long curLandLevel = land.getEntry(index);
            
            Preconditions.checkState(curWaterLevel >= minWaterLevel);
            Preconditions.checkState(curWaterLevel >= curLandLevel);
            
            if (curWaterLevel == curLandLevel) {
                //not submerged
                long erosion = curWaterLevel - minWaterLevel;
                
                erosion = Math.min(maxErosion, erosion);
                
                nextGrid.setEntry(index, Math.max(0, curLandLevel - erosion));
                r.minErosionAmount = Math.min(r.minErosionAmount, erosion);
            } else {
                //Submerged means that no adj square has a lower water level
                Preconditions.checkState(curWaterLevel == minWaterLevel);
                
                r.existEmergedFields = true;
            }
            
                       
            
            
        
        }
        
        return r;
    }
    
    
    public String bruteForce(InputData in) {
        
        Grid<Long> land = new Grid<Long>(in.grid);
        
        for(int iter = 0; iter < 600; ++iter) {
            
            log.debug("Grid land {}", land);
            
            Set<Integer> zeros = land.getIndexesOf(0l);
            
            if (zeros.size() == land.getSize()) {
                return String.format("Case #%d: %d", in.testCase, iter);
            }
            
            Grid<Long> waterLevel = Grid.buildEmptyGrid(land.getRows(),land.getCols(), 0l);
            
            determineWaterLevel(land, waterLevel);
            
           /* Grid<Long> oldWaterLevel = new Grid<>(land);
            
            for(int index = 0; index < waterLevel.getSize(); ++index) {
                oldDetermineWaterLevel(oldWaterLevel, index);
            }*/
            
            //log.debug("Old water level. is equals? {} {}", oldWaterLevel.equals(waterLevel), oldWaterLevel);
            
            /*
            int maxDistEdge = Math.max( waterLevel.getRows() / 2, waterLevel.getCols() / 2); 
            
            //go outside in
            for(int distEdge = 0; distEdge < maxDistEdge; ++distEdge )
            {
                int startCol = distEdge;
                int stopCol = waterLevel.getCols() - 1 - distEdge;
                int startRow = distEdge;
                int stopRow = waterLevel.getRows() - 1 - distEdge;
                
                for( int col = startCol; col <= stopCol; ++col) {
                    determineWaterLevel(waterLevel, startRow, col);
                    determineWaterLevel(waterLevel, stopRow, col);
                }
                
                for( int row = startRow; row <= stopRow; ++row) {
                    determineWaterLevel(waterLevel, row, startCol);
                    determineWaterLevel(waterLevel, row, stopCol);
                }
            }*/
            
            log.debug("Water level {}", waterLevel);
            
            Grid<Long> nextLand = new Grid<Long>(land);
            
            doErosion(land,waterLevel,nextLand, in.M);
            
            log.debug("After erosion {}", nextLand);
            
            land = nextLand;
            
            
            
        }
        
        return "g";
    }
    
    public String handleCase(InputData in) {

        //        return String.format("Case #%d: %d", in.testCase, minSize == Integer.MAX_VALUE ? 0 : minSize);
        
        return bruteForce(in);
    }

}
