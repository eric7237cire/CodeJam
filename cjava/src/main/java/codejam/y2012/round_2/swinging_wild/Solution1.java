package codejam.y2012.round_2.swinging_wild;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import codejam.utils.datastructures.graph.GraphInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Implement the first N^3 solution
 * 
 *
 */
public class Solution1 extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles 
 {

    public Solution1() 
    {
        super("A", 1, 0, 0);
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
    in.ropeLength[in.nRopes-1] = 0;
    in.ropePosition[in.nRopes-1] = in.goalDistance;
    
    return in;
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

    @Override
    public String toString()
    {
        return "Node [ropeIndex=" + ropeIndex + ", swingLength=" + swingLength + "]";
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
    
    
    
    Map<Node, Integer> nodeToIndex = Maps.newHashMap();
    
    GraphInt graph = new GraphInt();
    //Set<Node> nodes = Sets.newHashSet();
    
    List<List<Node>> nodesPerRope = Lists.newArrayList();
    for(int r1 = 0; r1 < in.nRopes; ++r1)
    {
        nodesPerRope.add(Lists.<Node>newArrayList());
    }
    
    Preconditions.checkState(in.ropeLength[0] >= in.ropePosition[0]);
    
    //Holding rope at ledge so cannot use full length
    Node startNode = new Node(0, in.ropePosition[0]);
    Node endNode = new Node(in.nRopes-1, 0);
    
    final int startNodeId = getNodeId(startNode, nodeToIndex, nodesPerRope);
    final int endNodeId = getNodeId(endNode, nodeToIndex, nodesPerRope);
    
    for(int r1 = 0; r1 < in.nRopes; ++r1) 
    {
        for(int r2 = r1+1; r2 < in.nRopes; ++r2) 
        {
            int pos1 = in.ropePosition[r1];
            int pos2 = in.ropePosition[r2];
            
            int len1 = in.ropeLength[r1];
            int len2 = in.ropeLength[r2];
            
            int disBetween = Math.abs(pos1 - pos2);
            
            log.debug("Pos {}, {} dis bet {}", pos1, pos2, disBetween);
            
            Node node1 = new Node(r1, Math.min(len1, disBetween));
            Node node2 = new Node(r2, Math.min(len2, disBetween));
            
            int nodeId1 = getNodeId(node1, nodeToIndex, nodesPerRope);
            int nodeId2 = getNodeId(node2, nodeToIndex, nodesPerRope);
            
            //From rope1, we can swing to node2
            if (len1 >= disBetween) {
                log.debug("Connecting node {} id {} with node {} id {}", node1, nodeId1, node2, nodeId2);
                graph.addOneWayConnection(nodeId1, nodeId2);
            }
            
            //From rope2, we can swing to node1
            if (len2 >= disBetween) {
                log.debug("Connecting node {} id {} with node {} id {}", node2, nodeId2, node1, nodeId1);
                graph.addOneWayConnection(nodeId2, nodeId1);
            }
            
        }
    }
    
    //Now for each rope, add edges to lower rope swing lengths.  This corresponds to climbing up the rope
    
    for (int r = 0; r < in.nRopes; ++r) {
        List<Node> ropeNodeList = nodesPerRope.get(r);
        
        Collections.sort(ropeNodeList, new Comparator<Node>(){

            @Override
            public int compare(Node o1, Node o2) {
                return Integer.compare(o1.swingLength, o2.swingLength);
            }
            
        });
        
        for(int pLonger = 0; pLonger < ropeNodeList.size(); ++pLonger) {
            Node nodeLonger = ropeNodeList.get(pLonger);
            int nodeIdLonger = nodeToIndex.get(nodeLonger);
            
            for(int pShorter = 0; pShorter < pLonger; ++pShorter) {
                Node nodeShorter = ropeNodeList.get(pShorter);
                int nodeIdShorter = nodeToIndex.get(nodeShorter);
                
                log.debug("Connecting longer node {} id {} with shorter node {} id {}", nodeLonger,
                        nodeIdLonger, nodeShorter, nodeIdShorter);
                graph.addOneWayConnection(nodeIdLonger, nodeIdShorter);
            }
        }
    }
    
    log.debug("Number of nodes {}", nodeToIndex.size());
    
    int nNodes = nodeToIndex.size();
    
    Queue<Integer> toVisit = new LinkedList<>();
    toVisit.add(startNodeId);
    
    boolean[] visited = new boolean[nNodes];

    boolean found = false;
    
    while(!toVisit.isEmpty()) {
        Integer curNode = toVisit.poll();
        
        log.debug("Visiting node id {}", curNode);
        if (curNode == endNodeId) {
            found = true;
            break;
        }
        
        if (visited[curNode]) {
            continue;
        }
        
        visited[curNode] = true;
        
        Set<Integer> adjNodes = graph.getNeighbors(curNode);
        
        for(Integer adjNode : adjNodes) 
        {
            toVisit.add(adjNode);
        }
    }
    

    return String.format("Case #%d: %s", in.testCase, found ? "YES" : "NO");
} 

}
