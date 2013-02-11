package codejam.y2012.round_2.swinging_wild;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import codejam.utils.datastructures.graph.GraphInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Implement the first N^3 solution
 * 
 *
 */
public class Solution1 extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles 
 {



@Override
public InputData readInput(Scanner scanner, int testCase) {
    return Main.readInputStatic(scanner, testCase);
}

private static class Node
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
    
}

public int getNodeId(Node node, Map<Node, Integer> nodeToIndex)
{
    Integer nodeId = nodeToIndex.get(node);
    
    if (nodeId == null) {
        nodeId = nodeToIndex.size();
        nodeToIndex.put(node, nodeId);
    }
    
    return nodeId;
    
}

@Override
public String handleCase(InputData in) {
    
    Map<Node, Integer> nodeToIndex = Maps.newHashMap();
    
    GraphInt graph = new GraphInt();
    //Set<Node> nodes = Sets.newHashSet();
    
    List<List<Node>> nodesPerRope = Lists.newArrayList();
    for(int r1 = 0; r1 < in.nRopes; ++r1)
    {
        nodesPerRope.add(Lists.<Node>newArrayList());
    }
    
    for(int r1 = 0; r1 < in.nRopes; ++r1) 
    {
        for(int r2 = r1+1; r2 < in.nRopes; ++r2) 
        {
            int pos1 = in.ropePosition[r1];
            int pos2 = in.ropePosition[r2];
            
            int len1 = in.ropeLength[r1];
            int len2 = in.ropeLength[r2];
            
            int disBetween = Math.abs(pos1 - pos2);
            
            Node node1 = new Node(r1, Math.min(len1, disBetween));
            Node node2 = new Node(r2, Math.min(len2, disBetween));
            
            int nodeId1 = getNodeId(node1, nodeToIndex);
            int nodeId2 = getNodeId(node2, nodeToIndex);
            
            //From rope1, we can swing to node2
            if (len1 >= disBetween) {
                graph.addConnection(nodeId1, nodeId2);
            }
            
            //From rope2, we can swing to node1
            if (len2 >= disBetween) {
                graph.addConnection(nodeId2, nodeId1);
            }
            
            nodesPerRope.get(r1).add(node1);
            nodesPerRope.get(r2).add(node2);
        }
    }
    
    //Now for each rope, add edges to lower rope lengths
    
    for (int r = 0; r < in.nRopes; ++r) {
        List<Node> ropeNodeList = nodesPerRope.get(r);
    }

    return null;
}

}
