package codejam.y2011.round_final.rains_atlantis;

import java.math.RoundingMode;
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
import com.google.common.math.LongMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] { "sample.in" };
     //    return new String[] { "B-small-practice.in" };
       // return new String[] { "B-small-practice.in", "B-large-practice.in" };
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
        boolean existSubmergedFields;
        long maxLandHeight;
        
        RetInfo() {
            minErosionAmount = Long.MAX_VALUE;
            minWaterOnTop = Long.MAX_VALUE;
            
            existSubmergedFields = false;
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
            
            if (curLandLevel == 0 && curWaterLevel == 0) {
                //Do nothing
            }            
            else if (curWaterLevel.equals( curLandLevel) ) {
                //not submerged
                long erosion = curWaterLevel - minWaterLevel;
                
                erosion = Math.min(maxErosion, erosion);
                
                nextGrid.setEntry(index, Math.max(0, curLandLevel - erosion));
                r.minErosionAmount = Math.min(r.minErosionAmount, erosion);
                
                r.maxLandHeight = Math.max(curLandLevel, r.maxLandHeight);
            } else {
                //Submerged means that no adj square has a lower water level
                Preconditions.checkState(curWaterLevel == minWaterLevel);
                
                long waterOnTop = curWaterLevel - curLandLevel;
                
                r.minWaterOnTop = Math.min(r.minWaterOnTop, waterOnTop);
                r.existSubmergedFields = true;
            }
            
                       
            
            
        
        }
        
        return r;
    }

    void doErosionBatch(Grid<Long> land, Grid<Long> waterLevel, Grid<Long> nextGrid, long batchSteps, long maxErosion) {
                
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
            
            if (curLandLevel == 0 && curWaterLevel == 0) {
                //Do nothing
            }  else
            if (curWaterLevel.equals( curLandLevel) ) {
                //not submerged
                long erosion = curWaterLevel - minWaterLevel;
                
                erosion = Math.min(maxErosion, erosion);
                
                Preconditions.checkState(erosion == maxErosion);
                
                nextGrid.setEntry(index, Math.max(0, curLandLevel - erosion * batchSteps));
        
            } 
            
            
        
        }
        
    }
    
    
    public String bruteForce(InputData in) {
        
        Grid<Long> land = new Grid<Long>(in.grid);
        
        for(int iter = 0; iter < 600; ++iter) {
            
            log.debug("Grid day {} land {} ", iter, land);
            
            Set<Integer> zeros = land.getIndexesOf(0l);
            
            if (zeros.size() == land.getSize()) {
                return String.format("Case #%d: %d", in.testCase, iter);
            }
            
            Grid<Long> waterLevel = Grid.buildEmptyGrid(land.getRows(),land.getCols(), 0l);
            
            determineWaterLevel(land, waterLevel);
            
            
          //  log.debug("Water level {}", waterLevel);
            
            Grid<Long> nextLand = new Grid<Long>(land);
            
            doErosion(land,waterLevel,nextLand, in.M);
            
           // log.debug("After erosion {}", nextLand);
            
            land = nextLand;
            
            
            
        }
        
        return "g";
    }

    public String handleCase(InputData in) {

        //bruteForce(in);
        
        Grid<Long> land = new Grid<Long>(in.grid);

        long days = 0;
        while(true) {

           // log.debug("Grid batch day {} land {}", days, land);
           //

            Set<Integer> zeros = land.getIndexesOf(0l);

            if (zeros.size() == land.getSize()) {
                return String.format("Case #%d: %d", in.testCase, days);
            }

            Grid<Long> waterLevel = Grid.buildEmptyGrid(land.getRows(), land.getCols(), 0l);

            determineWaterLevel(land, waterLevel);
            //log.debug("Water level {}", waterLevel);

            Grid<Long> nextLand = new Grid<Long>(land);

            RetInfo r = doErosion(land, waterLevel, nextLand, in.M);
            
            log.debug("Grid batch day {} case {}.  Min erosion height {} M {}", days, in.testCase, r.minErosionAmount, in.M);
            
            //Determine if we can do a multistep
            if (!r.existSubmergedFields && r.minErosionAmount == in.M) {
                days += LongMath.divide(r.maxLandHeight, in.M, RoundingMode.UP);
                return String.format("Case #%d: %d", in.testCase, days);
            } else
            if (r.minErosionAmount == (long) in.M) {                
                long batchSteps = r.minWaterOnTop / in.M;
                
                if (batchSteps == 0) {
                    //Then just use the regular one
                    ++days;
                    land = nextLand;
                } else {
                
                nextLand = new Grid<Long>(land);
                doErosionBatch(land, waterLevel, nextLand, batchSteps, in.M);
                days += batchSteps;
                land = nextLand;
                }
            } else {
                ++days;
                land = nextLand;
            }

            //log.debug("After erosion {}", nextLand);

            

        }


    }

}
