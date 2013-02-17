package codejam.y2012.round_3.lost_password;

import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.graph.BridgeDirectedGraph;
import codejam.utils.datastructures.graph.DirectedGraphInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>
{

    public Main() {
          super("D", 0,0);
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);

        in.k = scanner.nextInt();
        in.S = scanner.next();

        return in;
    }

    public String handleSmall(InputData in)
    {
        Map<Character, Character> l33t = Maps.newHashMap();
        l33t.put('o', '0');
        l33t.put('i', '1');
        l33t.put('e', '3');
        l33t.put('a', '4');
        l33t.put('s', '5');
        l33t.put('t', '7');
        l33t.put('b', '8');
        l33t.put('g', '9');
        
        BiMap<Character, Integer> charToIntMap = HashBiMap.create(36);
        for(int i = 0; i < 26; ++i)
        {
            charToIntMap.put((char) ( i+'a'), i);
        }
        for(int i = 0; i <= 9; ++i)
        {
            charToIntMap.put((char) ( i+'0'), i+26);
        }
        
        DirectedGraphInt graph = new DirectedGraphInt();
        
        for(int cIdx  = 0; cIdx < in.S.length()-1; ++cIdx)
        {
            char curLetter = in.S.charAt(cIdx);
            char altLetter = curLetter;
            if (l33t.containsKey(curLetter)) {
                altLetter = l33t.get(curLetter);
            }
            
            char nextLetter = in.S.charAt(cIdx+1);
            char nextAltLetter = nextLetter;
            if (l33t.containsKey(nextLetter)) {
                nextAltLetter = l33t.get(nextLetter);
            }
            
            log.debug("Ch  Connecting {} {} to {} {}", curLetter, altLetter, nextLetter, nextAltLetter);
            int curLetterInt = charToIntMap.get(curLetter);
            int altLetterInt = charToIntMap.get(altLetter);
            int nextLetterInt = charToIntMap.get(nextLetter);
            int nextAltLetterInt = charToIntMap.get(nextAltLetter);
            
            log.debug("Int Connecting {} {} to {} {}", curLetterInt, altLetterInt, nextLetterInt, nextAltLetterInt);
            
            //If no alt letter it will end up being a no-op
            graph.addOneWayConnection(curLetterInt,nextLetterInt);
            graph.addOneWayConnection(altLetterInt,nextLetterInt);
            graph.addOneWayConnection(curLetterInt,nextAltLetterInt);
            graph.addOneWayConnection(altLetterInt,nextAltLetterInt);
            
        }
        
        LinkedList<Pair<Integer,Integer>> needInodeDegreeDiffList = new LinkedList<>();
        LinkedList<Pair<Integer,Integer>> nodeDegreeDiffList = new LinkedList<>();
                
        int nodes = 26 + 10;
        int startNode = -1;
        
        for(int n = 0; n < nodes; ++n)
        {
            if (!graph.nodeExists(n))
                continue;
            
            int outDegree = graph.getOutboundDegree(n);
            int inDegree = graph.getInboundDegree(n);
            
            Preconditions.checkState(outDegree > 0 || inDegree > 0);
            
            int diff = outDegree - inDegree;
        
            if (outDegree > 0)
                startNode = n;
            
            if (diff == 0)
                continue;
            
            if (diff < 0)
                nodeDegreeDiffList.offerFirst(new MutablePair<>(n, diff));
            else
                nodeDegreeDiffList.offerLast(new MutablePair<>(n, diff));
        }
        
        log.debug("Graph {}", graph);
        
        
        while(nodeDegreeDiffList.size() > 2)
        {
            log.debug("Degree out-in list size {} : {}", nodeDegreeDiffList.size(), nodeDegreeDiffList);
            Pair<Integer,Integer> needOut = nodeDegreeDiffList.peekFirst();
            Preconditions.checkState(needOut.getRight() < 0);
            
            //Walk down the end of the list, adding connections where we can
            for(int j = nodeDegreeDiffList.size() - 1; j >= 1; --j)
            {
                Pair<Integer,Integer> needIn = nodeDegreeDiffList.get(j);
            
                Preconditions.checkState(needIn.getRight() > 0);
                
                
                
                if (graph.isConnected(needOut.getLeft(), needIn.getLeft()))
                    continue;
                
                graph.addOneWayConnection(needOut.getLeft(), needIn.getLeft());
                
            
                needOut.setValue(needOut.getValue()+1);
                needIn.setValue(needIn.getValue()-1);
                
                if (needIn.getRight() == 0) {
                    nodeDegreeDiffList.remove(j);
                }
                
                if (needOut.getValue()==0) {
                    break;
                }
            }
            
            
            Preconditions.checkState(graph.getInboundDegree(needOut.getLeft()) ==
                    graph.getOutboundDegree(needOut.getLeft()));
            
            nodeDegreeDiffList.removeFirst();
            
            
        }
        
        log.debug("Graph with euler path {}", graph);
        
        
        if (nodeDegreeDiffList.size() > 0) {
            startNode = nodeDegreeDiffList.get(1).getLeft();
            Preconditions.checkState(nodeDegreeDiffList.get(1).getRight().equals(1));
        }
        
        
        
        StringBuffer path = new StringBuffer();
        
        int curNode = startNode;
        path.append(charToIntMap.inverse().get(curNode));
        while(graph.getOutbound(curNode).size() > 0)        
        {
           // log.debug("Calculate path Graph {}", graph);
            //Must avoid bridges unless we have no choice
            BridgeDirectedGraph b = new BridgeDirectedGraph(graph);
            b.go();
            
            Set<Integer> outBound = graph.getOutbound(curNode);
            
            int nextNode = -1;
            for(Integer adjNode : outBound) {
                nextNode = adjNode;
                
                //Same component, we are good
                if (b.compNum(curNode) == b.compNum(nextNode))
                    break;
            }
            
            Preconditions.checkState(nextNode != -1);
                        
            graph.removeConnection(curNode,nextNode);
            
            curNode = nextNode;
            path.append(charToIntMap.inverse().get(curNode));
        }
        
        
        log.debug("Path {} len {}", path.toString(), path.toString().length());
        return String.format("Case #%d: ", in.testCase);
    }

    @Override
    public String handleCase(InputData in)
    {
        if (in != null)
            handleSmall(in);

        return String.format("Case #%d: ", in.testCase);
    }
}