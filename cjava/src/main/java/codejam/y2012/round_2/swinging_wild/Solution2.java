package codejam.y2012.round_2.swinging_wild;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

import ch.qos.logback.classic.Level;
import codejam.utils.datastructures.IndexMinPQ;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

/**
 * Implement the alternative solution
 * using Dijkstras
 * 
 *
 */
public class Solution2 extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles 
 {

    public Solution2() 
    {
        super("A", 0, 1, 0);
        
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
        
        //super("A", 0, 0, 1);
    }


@Override
public InputData readInput(Scanner scanner, int testCase) {
    InputData in = new InputData(testCase);
    
    /*
     * The first line of each test case contains the number N of vines.
     *  N lines describing the vines follow, each with a pair of integers
     *   di and li - the distance of the vine from your ledge, and
     *    the length of the vine, respectively. The last line of the 
     *    test case contains the distance D to the ledge with your one 
     *    true love. You start by holding the first vine in hand.
     */
    
    in.nRopes = scanner.nextInt() + 1;
    in.ropePosition = new int[in.nRopes];
    in.ropeLength = new int[in.nRopes];
    
    for(int n = 0; n < in.nRopes - 1; ++n) {
        in.ropePosition[n] = scanner.nextInt();
        in.ropeLength[n] = scanner.nextInt();
    }
    
    
    in.goalDistance = scanner.nextInt();

    //Create a dummy rope at goal
    in.ropeLength[in.nRopes-1] = 1;
    in.ropePosition[in.nRopes-1] = in.goalDistance;
    
    return in;
}

private static class Node implements Comparable<Node>
{
    int ropeIndex;
    int swingLength;
    public Node(int ropeIndex, int swingLength) {
        super();
        this.ropeIndex = ropeIndex;
        this.swingLength = swingLength;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ropeIndex;
        result = prime * result + swingLength;
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (ropeIndex != other.ropeIndex)
            return false;
        if (swingLength != other.swingLength)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        return "Node [ropeIndex=" + ropeIndex + ", swingLength=" + swingLength + "]";
    }

    @Override
    public int compareTo(Node o)
    {
        return ComparisonChain.start().
                compare(o.swingLength, swingLength)
                .compare(ropeIndex, o.ropeIndex).result();
    }
    
    
    
}

public int getNodeId(Node node, 
        Map<Node, Integer> nodeToIndex,
        List<List<Node>> nodesPerRope)
{
    Integer nodeId = nodeToIndex.get(node);
    
    if (nodeId == null) {
        nodeId = nodeToIndex.size();
        nodeToIndex.put(node, nodeId);
        
        nodesPerRope.get(node.ropeIndex).add(node);
    }
    
    return nodeId;
    
}

@Override
public String handleCase(InputData in) {
    
    
    
    
    int[] maxLengthVisited = new int[in.nRopes];
    Arrays.fill(maxLengthVisited, -1);
    
    TreeMap<Integer,Integer> positionToIndex = new TreeMap<>();
    
    IndexMinPQ<Integer> toVisit = new IndexMinPQ<>(in.nRopes);
    
    for(int r = 0; r < in.nRopes; ++r)
    {
        toVisit.insert(r, Integer.MAX_VALUE);
        positionToIndex.put(in.ropePosition[r], r);
    }
    
    
    boolean found = false;
    
    
    
    
    Preconditions.checkState(in.ropeLength[0] >= in.ropePosition[0]);
    
    toVisit.changeKey(0, Integer.MAX_VALUE-in.ropePosition[0]);
    
    int count = 0;
    
    while(!toVisit.isEmpty()) {
        
        int currentSwingLength = Integer.MAX_VALUE-toVisit.minKey();
        int currentRopeIndex = toVisit.delMin();
        
        log.debug("Current rope {} swing length {}", currentRopeIndex, currentSwingLength);
        
        if (currentSwingLength == 0)
            continue;
        
        if (currentRopeIndex == in.nRopes-1) {
            found = true;
            break;
        }
        
        /*
        if (curNode.swingLength <= maxLengthVisited[curNode.ropeIndex]) {
            continue;
        }*/
    
        ++count;
        if (count % 100 == 0)
            log.info("Nodes visited {} of {} ", count, in.nRopes);
        //maxLengthVisited[curNode.ropeIndex] = curNode.swingLength;
        
        positionToIndex.remove(in.ropePosition[currentRopeIndex]);
        
        int curPos = in.ropePosition[currentRopeIndex];
        
        Map<Integer,Integer> reachable = positionToIndex.
                subMap(curPos - currentSwingLength, true, curPos + currentSwingLength, true);
        
        for(int ri : reachable.values())
        {
            int d = Math.abs(curPos - in.ropePosition[ri]);
            
            Preconditions.checkState(d <= currentSwingLength);
            
            int riSwingLength = Integer.MAX_VALUE-Math.min(in.ropeLength[ri], d);
            
            int curSwingLength = toVisit.keyOf(ri);
            
            if (riSwingLength < curSwingLength) {
                toVisit.decreaseKey(ri, riSwingLength);
            }
            
        }
    }
    

    return String.format("Case #%d: %s", in.testCase, found ? "YES" : "NO");
} 

}
