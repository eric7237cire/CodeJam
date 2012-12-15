package codejam.y2008.round_amer.king;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import com.google.common.collect.Sets;

import codejam.utils.datastructures.TreeInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.GridChar;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    @Override
    public String[] getDefaultInputFiles() {
       // return new String[] { "sample.in"};
        return new String[] { "D-small-practice.in" };
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

    @Override
    public String handleCase(InputData input) {
        Set<Integer> kingLocs = input.grid.getIndexesOf('K');
        int kingLoc = kingLocs.iterator().next();
        
        TreeInt<Boolean> tree = new TreeInt<Boolean>(kingLoc);
        //tree.setStats(false);
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
        

        return String.format("Case #%d: %s", input.testCase, tree.getRoot().getData() ? "A" : "B");
    }

}
