package codejam.y2008.round_amer.king;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.Bridge;
import codejam.utils.datastructures.GraphInt;
import codejam.utils.datastructures.TreeInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] { "sample.in"};
       // return new String[] { "D-small-practice.in" };
        //return new String[] { "B-large-practice.in" };
        //return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.row = scanner.nextInt();
        input.col = scanner.nextInt();
        GridChar grid = GridChar.buildFromScanner(scanner,input.row,input.col, '#');
        input.grid = grid;
        
        return input;
    }
    
    public boolean findLoserSquares(GridChar grid) {
        int maxIndex = grid.getRows() * grid.getCols() - 1;
        boolean r = false;
        for (int sq = 0; sq <= maxIndex; ++sq) {

            char ch = grid.getEntry(sq);

            if (ch == '.') {

                int count = 0;
                for (Direction dir : Direction.values()) {
                    Integer childIdx = grid.getIndex(sq, dir);
                    if (childIdx == null)
                        continue;

                    char adjSq = grid.getEntry(childIdx);

                    if (adjSq == 'K' || adjSq == '.' || adjSq == 'L' ) {
                        ++count;
                    }
                }

                if (count == 1) {
                    grid.setEntry(sq, 'L');
                    r = true;
                }
            }
            
            if (ch == 'L') {
                for (Direction dir : Direction.values()) {
                    Integer childIdx = grid.getIndex(sq, dir);
                    if (childIdx == null)
                        continue;

                    char Adjsq = grid.getEntry(childIdx);
                    
                    if (Adjsq == '.') {
                        grid.setEntry(childIdx, '#');
                        r = true;
                    }
                }
            }

        }
        
        return r;
    }
    
    public boolean reduceGrid(GridChar grid, int startingLoc) {
        //Create a graph corresponding to grid
        GraphInt graph = new GraphInt();
        
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
                
                if (sq == '#' || sq == 'K' || sq == 'L')
                    continue;
                
                graph.addConnection(loc, childIdx);
                toVisit.add(childIdx);
            }
        }
        
        Bridge bridge = new Bridge(graph);
        //Find bridges
        List<Pair<Integer,Integer>> bridges = bridge.getBridges(); 

        
        //Must find a bridge that is isolated 
        Set<Integer> bridgeEdgeNodes = Sets.newHashSet();
        
        for(Pair<Integer,Integer> bEdge : bridges) {
            bridgeEdgeNodes.add(bEdge.getLeft());
            bridgeEdgeNodes.add(bEdge.getRight());
        }
        
        
        for(Pair<Integer,Integer> bEdge : bridges) {
            
            Set<Integer> leftNodes = graph.getConnectedNodesWithoutEdge(bEdge.getLeft(), bEdge.getLeft(), bEdge.getRight());
            
            Set<Integer> rightNodes = graph.getConnectedNodesWithoutEdge(bEdge.getRight(), bEdge.getLeft(), bEdge.getRight());
            
            //looking for the set that only contains 1 bridge node, that means it is isolated.  We want
            //to reduce grid using only that bridge
            
            Set<Integer> isolatedSet = null;
            
            //First node is in the isolated set
            Pair<Integer,Integer> isoBridge = null;
            if (Sets.intersection(bridgeEdgeNodes,leftNodes).size() == 1 && !leftNodes.contains(startingLoc)) {
                isolatedSet = leftNodes;                
            }
            if (Sets.intersection(bridgeEdgeNodes,rightNodes).size() == 1 && !rightNodes.contains(startingLoc)) {
                isolatedSet = rightNodes;                
            }
            
            if (isolatedSet == null)
                continue;
            
            if (isolatedSet.contains(bEdge.getLeft())) {
                isoBridge = new ImmutablePair<Integer,Integer>(bEdge.getLeft(), bEdge.getRight());
                Preconditions.checkState(!isolatedSet.contains(bEdge.getRight()));
            }
            if (isolatedSet.contains(bEdge.getRight())) {
                isoBridge = new ImmutablePair<Integer,Integer>(bEdge.getRight(), bEdge.getLeft());
                Preconditions.checkState(!isolatedSet.contains(bEdge.getLeft()));
            }
            
            if (isolatedSet.size() % 2 == 0) {
                //block the node in the isolated set as even numbered # of nodes wins for whose move it is
                //thus, we can never move to the square
                grid.setEntry(isoBridge.getLeft(), '#');
                return true;
            } else {
                //We cannot move to the entrance as entering into to the isolated set is a winning move
                
                //..#
                //.##
                //E#
                
                //ie, E is now blocked because whoever's move it is in an odd squared field loses
                
                if (grid.getEntry(isoBridge.getRight()) == 'K') {
                    grid.setEntry(isoBridge.getLeft(), 'L');
                    //auto win
                    return false;
                }
                else
                    grid.setEntry(isoBridge.getRight(), '#');
                return true;
            }
        }
        
        
        
        return false;

    }
    
    public int getConnectedSquareCount(GridChar grid, int startingLoc) {

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
                
                if (sq == '#' || sq == 'K')
                    continue;
                
                toVisit.add(childIdx);
            }
        }

        return visitedNodes.size();
    }
    
    public String awinsIfEven(InputData input) {
        Set<Integer> kingLocs = input.grid.getIndexesOf('K');
        int kingLoc = kingLocs.iterator().next();
        
        boolean r = true;
        
        while(r) {
            //r = findLoserSquares(input.grid);
            r = reduceGrid(input.grid, kingLoc);
        }
        
                        
        for(Direction dir : Direction.values()) {
            Integer childIdx = input.grid.getIndex(kingLoc,dir);
            if (childIdx == null)
                continue;
            
            char sq = input.grid.getEntry(childIdx);
            
            if (sq == 'L') {
                return String.format("Case #%d: %s", input.testCase, "A" );
            }
            
            if (sq == '#') {
                continue;
            }
            
            int size = 1 + getConnectedSquareCount(input.grid, childIdx);
            
            if (size % 2 == 0) {
                return String.format("Case #%d: %s", input.testCase, "A" );
            }
    
        }
        
        
        return String.format("Case #%d: %s", input.testCase, "B");
        
    }

    @Override
    public String handleCase(InputData input) {
        return awinsIfEven(input);
    }
    public String bruteForce(InputData input) {
        Set<Integer> kingLocs = input.grid.getIndexesOf('K');
        int kingLoc = kingLocs.iterator().next();
        
        TreeInt<Boolean> tree = new TreeInt<Boolean>(kingLoc);
        tree.setStats(true);
        tree.setUniqueNodeIds(false);
        
        LinkedList<TreeInt<Boolean>.Node> toVisit = new LinkedList<>();
        
        toVisit.add(tree.getRoot());
        
        while(!toVisit.isEmpty()) {
            
            TreeInt<Boolean>.Node thisNode = toVisit.poll();
            final int loc = thisNode.getId();
                        
            for(Direction dir : Direction.values()) {
                Integer childIdx = input.grid.getIndex(loc,dir);
                if (childIdx == null)
                    continue;
                
                char sq = input.grid.getEntry(childIdx);
                
                if (sq == '#')
                    continue;
                
                TreeInt<Boolean>.Node node = thisNode;
                boolean alreadyHitSq = false;
                
                while(node.getParent() != null) {
                    node = node.getParent();
                    if(node.getId() == childIdx) {
                        alreadyHitSq = true;
                        break;
                    }
                }
                
                if (alreadyHitSq)
                    continue;
                
                TreeInt<Boolean>.Node child = thisNode.addChild(childIdx);
                toVisit.add(child);
            }
        }
        
        //Now traverse
        Stack<TreeInt<Boolean>.Node> toVisitStack = new Stack<>();
        
        Set<TreeInt<Boolean>.Node> visited = Sets.newHashSet();
        
        toVisitStack.add(tree.getRoot());
        
        while(!toVisitStack.empty()) {
            TreeInt<Boolean>.Node node = toVisitStack.peek();
            
            if (node.getChildren().isEmpty()) {
                node.setData(false);
                visited.add(node);
                toVisitStack.pop();
                continue;
            }
            
            Iterator<TreeInt<Boolean>.Node> childIt = node.getChildren().iterator(); 
            TreeInt<Boolean>.Node child = childIt.next(); 
            if (!visited.contains(child)) {
                //Add all children to stack
                toVisitStack.add(child);
                while(childIt.hasNext()) {
                    child = childIt.next();
                    toVisitStack.add(child);
                }
                continue;
            }
            
            boolean isLoserNode = child.getData();
            while(childIt.hasNext()) {
                child = childIt.next();
                isLoserNode = isLoserNode && child.getData();
            }
            
            node.setData(!isLoserNode);
            visited.add(node);
            toVisitStack.pop();
        }
        

        //String resp = awinsIfEven(input);
        
        return String.format("Case #%d: %s", input.testCase, tree.getRoot().getData() ? "A" : "B");
    }

}
