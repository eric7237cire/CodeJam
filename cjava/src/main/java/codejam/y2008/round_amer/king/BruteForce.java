package codejam.y2008.round_amer.king;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import codejam.utils.datastructures.TreeInt;
import codejam.utils.datastructures.TreeInt.Node;
import codejam.utils.utils.Direction;

import com.google.common.collect.Sets;

public class BruteForce {

    public BruteForce() {
        // TODO Auto-generated constructor stub
    }
    
    public static String bruteForce(InputData input) {
        Set<Integer> kingLocs = input.grid.getIndexesOf('K');
        int kingLoc = kingLocs.iterator().next();
        
        TreeInt<Boolean> tree = new TreeInt<Boolean>(kingLoc);
        tree.setStats(true);
        tree.setUniqueNodeIds(false);
        
        PriorityQueue<TreeInt<Boolean>.Node> toVisit = new PriorityQueue<>(1000, new Comparator<TreeInt<Boolean>.Node>() {

            @SuppressWarnings("rawtypes")
            @Override
            public int compare(Node o1, Node o2) {
                return Integer.compare(o2.getDepth(), o1.getDepth());
            }
            
        });
        
        toVisit.add(tree.getRoot());
        
        int maxDepth = 0;
        
        //Build list of possibilities
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
                if (child.getDepth() > maxDepth) {
                    //log.debug("Child height {}", child.getDepth() );
                    maxDepth = child.getDepth();
                }
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
                //false == victory node
                //data is trap node
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
            
            boolean isSurroundedByTrapNode = child.getData();
            while(childIt.hasNext()) {
                child = childIt.next();
                isSurroundedByTrapNode = isSurroundedByTrapNode && child.getData();
            }
            
            //If this node is completely booby trapped, then it is not a trap node
            node.setData(!isSurroundedByTrapNode);
            
            //If there is at least 1 adjacent node that is not a trap (ie a victory node), then
            //this node is not a trap node
            visited.add(node);
            toVisitStack.pop();
        }
        

        //String resp = awinsIfEven(input);
        
        return String.format("Case #%d: %s", input.testCase, tree.getRoot().getData() ? "A" : "B");
    }

}
