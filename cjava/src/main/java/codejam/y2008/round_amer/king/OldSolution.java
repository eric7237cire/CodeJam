package codejam.y2008.round_amer.king;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.ArticulationPoint;
import codejam.utils.datastructures.GraphInt;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

import com.google.common.collect.Sets;

public class OldSolution {


    
    static public Integer getOpenNodeNextToKing(GridChar grid, int loc) {
        for(Direction dir : Direction.values()) {
            Integer childIdx = grid.getIndex(loc,dir);
            if (childIdx == null)
                continue;
            
            char sq = grid.getEntry(childIdx);
            
            if (sq == '.') {
                return childIdx;
            }
        }
        
        return null;
    }
    
    static public boolean reduceGrid(GridChar grid, final int kingLoc) {
        //Create a graph corresponding to grid
        GraphInt graph = new GraphInt();
        
        int startingLoc = kingLoc; //getOpenNodeNextToKing(grid, kingLoc);
        
        Set<Integer> visitedNodes = Sets.newHashSet();
        
        LinkedList<Integer> toVisit = new LinkedList<>();
        toVisit.add(startingLoc);
        
        while(!toVisit.isEmpty()) {
            
            Integer loc = toVisit.poll();
            
            if (visitedNodes.contains(loc))
                continue;
            
            visitedNodes.add(loc);
                        
            for(Direction dir : Direction.values()) {
                Integer childIdx = grid.getIndex(loc,dir);
                if (childIdx == null)
                    continue;
                
                char sq = grid.getEntry(childIdx);
                
                if (sq == '#' || sq == 'K' || sq == 'V' || sq == 'T')
                    continue;
                
                if (loc == kingLoc)
                    //graph.addOneWayConnection(loc,childIdx);
                    graph.addConnection(loc, childIdx);
                else
                    graph.addConnection(loc, childIdx);
                
                toVisit.add(childIdx);
            }
        }
        
        ArticulationPoint ap = new ArticulationPoint(graph);
        //Find bridges
        List<Integer> artPoints = ap.getArticulationPoints();
        
        
        
        for(Integer aPoint : artPoints) {

            if (aPoint == kingLoc)
                continue;
            Set<Integer> adjNodes = graph.getNeighbors(aPoint);
            
            Set<Integer> isolatedNodes = Sets.newHashSet();
            Set<Integer> nonIsolatedNodes = Sets.newHashSet();
            
            adjNodeLoop:
            for(Integer adjNode : adjNodes) {
                if (isolatedNodes.contains(adjNode) || nonIsolatedNodes.contains(adjNode))
                    continue;
                
                Set<Integer> nodes = graph.getConnectedNodesWithoutNode(adjNode, aPoint);
                
                //Isolated set must not contain other articulation points
                for(Integer aPointToTest : artPoints) {
                    if (nodes.contains(aPointToTest)) {
                        nonIsolatedNodes.addAll(nodes);
                        continue adjNodeLoop;
                    }
                }
                
                if (!nodes.contains(startingLoc)) {
                    isolatedNodes = nodes;
                    break;
                }
            }
            
            //No suitable isolated set found, continue to next articulation point
            if (isolatedNodes == null || isolatedNodes.isEmpty())
                continue;
            
            if (isolatedNodes.size() % 2 == 0) {
                //All the isolated nodes are traps
                for(Integer isoNode : isolatedNodes) {
                    grid.setEntry(isoNode, 'T');
                }
                return true;
            } else {
                //The articulation point itself is a trap, as moving to it means
                //the other player can move into an odd numbered field, which is always
                //losing
                grid.setEntry(aPoint, 'T');
                return true;
            }
        }
        
        
        
        return false;

    }
    
    static public int getConnectedSquareCountOld(GridChar grid, int startingLoc) {

        Set<Integer> visitedNodes = Sets.newHashSet();
        
        LinkedList<Integer> toVisit = new LinkedList<>();
        toVisit.add(startingLoc);
        
        while(!toVisit.isEmpty()) {
            
            Integer loc = toVisit.poll();
            
            if (visitedNodes.contains(loc))
                continue;
            
            visitedNodes.add(loc);
                        
            for(Direction dir : Direction.values()) {
                Integer childIdx = grid.getIndex(loc,dir);
                if (childIdx == null)
                    continue;
                
                char sq = grid.getEntry(childIdx);
                
                if (sq == '#' || sq == 'K' || sq == 'T')
                    continue;
                
                toVisit.add(childIdx);
            }
        }

        return visitedNodes.size();
    }
    /**
     * Returns the count of squares reachable by the first player
     * @param grid
     * @param startingLoc
     * @return
     */
    static public boolean getConnectedSquareCount(GridChar grid, int startingLoc) {

       // int oldCount = (1+getConnectedSquareCountOld(grid,startingLoc) );
        
        //The boolean means it is player 1, the connected square co
        Set<Pair<Integer, Boolean>> visitedNodes = Sets.newHashSet();
        
        LinkedList<Pair<Integer, Boolean>> toVisit = new LinkedList<>();
        toVisit.add(new ImmutablePair<>(startingLoc, true));
        
        while(!toVisit.isEmpty()) {
            
            Pair<Integer, Boolean> loc = toVisit.poll();
            
            if (visitedNodes.contains(loc))
                continue;
            
            visitedNodes.add(loc);
                        
            for(Direction dir : Direction.values()) {
                Integer childIdx = grid.getIndex(loc.getLeft(),dir);
                if (childIdx == null)
                    continue;
                
                char sq = grid.getEntry(childIdx);
                
                if (sq == '#' || sq == 'K' || sq == 'T')
                    continue;
                
                toVisit.add(new ImmutablePair<>(childIdx, !loc.getRight()));
            }
        }

        Set<Integer> fpPoints = Sets.newHashSet();
        Set<Integer> spPoints = Sets.newHashSet();
        for(Pair<Integer, Boolean> p : visitedNodes) {
            if (p.getRight())
                fpPoints.add(p.getLeft());
            else 
                spPoints.add(p.getLeft());
        }
        
        Set<Integer> shared = Sets.intersection(fpPoints,spPoints);
        Set<Integer> union = Sets.union(fpPoints, spPoints);
        
        if (!shared.isEmpty()) {
            return (union.size() + 1 ) % 2 == 0;
        } else {
           //log.debug("size {} size {}", fpPoints.size(), spPoints.size());
            return spPoints.size() < fpPoints.size();
        }
        //return (countFP + countSP + 1) % 2 == 0;
    }
    
    //Returns true if B wins
    static public boolean tryFirstMove(GridChar grid, int aKingLoc, int bKingLoc) {
        
        grid.setEntry(aKingLoc,  '#');
        grid.setEntry(bKingLoc, 'K');
        
        boolean r = true;
        while(r) {
            //r = findLoserSquares(input.grid);
            r = reduceGrid(grid, bKingLoc);
        }
        
        for(Direction dir : Direction.values()) {
            Integer childIdx = grid.getIndex(bKingLoc,dir);
            if (childIdx == null)
                continue;
            
            char sq = grid.getEntry(childIdx);
            
            if (sq == 'V') {
                return true;
            }
            
            if (sq == '#' || sq == 'T') {
                continue;
            }
            
            //int size = 1 + getConnectedSquareCount(grid, childIdx);
            if (getConnectedSquareCount(grid, childIdx))
                return true;
//            if (size % 2 == 0) {
//                return true;
//            }
    
        }
        
        
        return false;

    }
    public static String awinsIfEven(InputData input) {
        Set<Integer> kingLocs = input.grid.getIndexesOf('K');
        int kingLoc = kingLocs.iterator().next();
        
        boolean r = true;
        
        GridChar gridOrig = new GridChar(input.grid);
        
      //Further reduce the grid in case B can win after A's first move
        for(Direction dir : Direction.values()) {
            Integer childIdx = input.grid.getIndex(kingLoc,dir);
            if (childIdx == null)
                continue;
            
            char sq = input.grid.getEntry(childIdx);
            
            if (sq != '.') 
                continue;
        
            GridChar gridTry = new GridChar(gridOrig);
            
            if (tryFirstMove(gridTry, kingLoc, childIdx)) {
                input.grid.setEntry(childIdx, 'T');
            } else {
                return String.format("Case #%d: %s", input.testCase, "A" );
            }
            
        }
        
        while(r) {
            //r = findLoserSquares(input.grid);
            r = reduceGrid(input.grid, kingLoc);
        }
        
        
        
                        
        for(Direction dir : Direction.values()) {
            Integer childIdx = input.grid.getIndex(kingLoc,dir);
            if (childIdx == null)
                continue;
            
            char sq = input.grid.getEntry(childIdx);
            
            if (sq == 'V') {
                return String.format("Case #%d: %s", input.testCase, "A" );
            }
            
            if (sq == '#' || sq == 'T') {
                continue;
            }
            
//            int size = 1 + getConnectedSquareCount(input.grid, childIdx);
//            
//            if (size % 2 == 0) {
//                return String.format("Case #%d: %s", input.testCase, "A" );
//            }
            
            if (getConnectedSquareCount(input.grid, childIdx))
                return String.format("Case #%d: %s", input.testCase, "A" );
    
        }
        
        
        return String.format("Case #%d: %s", input.testCase, "B");
        
    }


}
