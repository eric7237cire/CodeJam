package codejam.y2012.round_3.lost_password;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.graph.BridgeDirectedGraph;
import codejam.utils.datastructures.graph.DirectedGraphInt;
import codejam.utils.datastructures.graph.FlowEdge;
import codejam.utils.datastructures.graph.FlowNetwork;
import codejam.utils.datastructures.graph.FordFulkerson;
import codejam.utils.datastructures.graph.MincostMaxflow;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.math.NumericLong;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.IntegerPair;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>
{

    public Main() {
          super("D", 0, 0);
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);

        in.k = scanner.nextInt();
        in.S = scanner.next();

        return in;
    }
    
    
    
    /**
     * Generates all l33t variants
     * 
     *
     */
    public class GeneratePerms implements Iterator<String>
    {
        StringBuffer permutedString;
        
        List<Integer> questionMarkPositions;
        int maxPerm;
        int perm;
                
        String originalString;
        BiMap<Character,Character> replacements;
        
        GeneratePerms(String mysteryString, BiMap<Character,Character> replacements)        
        {
            this.replacements = replacements;
            this.permutedString = new StringBuffer(mysteryString);
            this.originalString = mysteryString;
            //log.info("Mystery str len {}", mysteryString.length());
            questionMarkPositions = Lists.newArrayList();
            
            
            for(int c = 0; c < mysteryString.length(); ++c) {
                char ch = mysteryString.charAt(c);
                
                
                
                if (replacements.containsKey(ch)) {
                    questionMarkPositions.add(c);
                }
                
                //0 means use original character, 1 means use alternate
                                
            }
            
            
            maxPerm = (1 << questionMarkPositions.size()) - 1;
        }

        @Override
        public boolean hasNext()
        {
            return perm <= maxPerm;
        }

        @Override
        public String next()
        {
            String r = permutedString.toString();
            
            ++perm;
            for(int i = 0; i < questionMarkPositions.size(); ++i) {
                int pos = questionMarkPositions.get(i);
                char orig = originalString.charAt(pos);
                
                if ((perm & 1 << i) != 0) {
                    permutedString.setCharAt(pos, replacements.get(orig));
                } else {
                    permutedString.setCharAt(pos, originalString.charAt(pos));
                }
            }

            return r;
        }

        @Override
        public void remove()
        {
           
            
        }
        
    }

    /**
     * Matching prefix 
     * A cost of 1 is 
     * ?xxx
     * xxx?
     * 
     * 2 is 
     * 
     * ??xx
     *   xx??
     * 
     */
    int transition_cost(String suffix, String prefix) 
    {
        Preconditions.checkState(suffix.length() == prefix.length());
        
        for (int n=1; n<=suffix.length(); ++n) 
        {
            if (suffix.substring(n, suffix.length()).equals(
                    prefix.substring(0, prefix.length()-n)))
                return n;
        }
        
        throw new RuntimeException("huh");
    }
    
    public String showMaxFlow(InputData in)
    {
        BiMap<Character, Character> l33t = HashBiMap.create(9);
        l33t.put('o', '0');
        l33t.put('i', '1');
        l33t.put('e', '3');
        l33t.put('a', '4');
        l33t.put('s', '5');
        l33t.put('t', '7');
        l33t.put('b', '8');
        l33t.put('g', '9');
        
        
        //Find all k length candidates  of S 

        Set<String> candidates = Sets.newHashSet();

        for(int startIdx = 0; startIdx <= in.S.length() - in.k; ++startIdx)
        {
            int endIdx = startIdx + in.k - 1;
            String subStr = in.S.substring(startIdx,endIdx+1);
            
            log.debug("Sub string {}", subStr);
            
            GeneratePerms gp = new GeneratePerms(subStr, l33t);
            
            while(gp.hasNext())
            {
                String nextPerm = gp.next();
                log.debug("Next perm {}", nextPerm);
                candidates.add(nextPerm);
            }
        }
        
        //Calculate weight for each prefix
        Map<String, Integer> phraseWeight = Maps.newHashMap();
        
        for(String candidate : candidates)
        {
            String prefix = candidate.substring(0, candidate.length()-1);
            String suffix = candidate.substring(1, candidate.length());
            
            log.debug("Candidate {} Prefix {} suffix {}", candidate, prefix, suffix);
            
            int curPrefixWeight = phraseWeight.containsKey(prefix) ? phraseWeight.get(prefix) : 0;                        
            phraseWeight.put(prefix, curPrefixWeight-1);
            
            int curSuffixWeight = phraseWeight.containsKey(suffix) ? phraseWeight.get(suffix) : 0;
            phraseWeight.put(suffix, curSuffixWeight+1);
        }

        List<String> allPhrases = Lists.newArrayList(phraseWeight.keySet());
        
        int s = allPhrases.size();
        int t = allPhrases.size()+1;
        
        int inf = 1000000;
        MincostMaxflow<Long,Long> fn = new MincostMaxflow<>(new NumericLong(0), new NumericLong(0));
        FlowNetwork fn2 = new FlowNetwork(t+1);
        
        for(int i = 0; i < allPhrases.size(); ++i)
        {
            for(int j = 0; j < allPhrases.size(); ++j)
            {
            
                String suffix = allPhrases.get(i);
                String prefix = allPhrases.get(j);
                
                //  A = x????
                //  B = ????y
                
                //check ? are the same
                int transitionCost = transition_cost(suffix, prefix);
                
                
                
                log.debug("Connecting {}, {} to {}, {}", i,suffix, j,prefix);
                fn.addArc(i, j, new NumericLong(inf), new NumericLong(transitionCost));
                fn2.addEdge(new FlowEdge(i,j,inf));
                 
                
                
            }
        }
        
        for(int i = 0; i < allPhrases.size(); ++i)
        {
            String phrase = allPhrases.get(i);
            int weight = phraseWeight.get(phrase);
            
            log.debug("Phrase {} weight {}", phrase, weight);
            
            if (weight > 0) {
                fn.addArc(s, i, new NumericLong(weight), new NumericLong(0));
                fn2.addEdge(new FlowEdge(s,i, weight));
            } 
            
            if (weight <= 0)
            {
                fn.addArc(i, t, new NumericLong(-weight), new NumericLong(0));
                fn2.addEdge(new FlowEdge(i,t,-weight));
            }
        }
        
        
        Pair<Long,Long> flowCost = fn.getFlow(s, t);
        
        FordFulkerson fff = new FordFulkerson(fn2, s,t);
        double flowCheck = fff.value(); 
        
        log.debug("Flow network {}", fn2.toString());
        
        long flow = flowCost.getRight();
        if (flow >= 2)
            flow -= 2;
        log.debug("Flow {} cost {} flow check {}", flowCost.getLeft(), flowCost.getRight(), flowCheck);
        long ans = flow + candidates.size() + in.k - 1;
        
        return String.format("Case #%d: %d", in.testCase, ans);
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
            graph.setEdgesBetween(curLetterInt,nextLetterInt, 1);
            graph.setEdgesBetween(altLetterInt,nextLetterInt, 1);
            graph.setEdgesBetween(curLetterInt,nextAltLetterInt, 1);
            graph.setEdgesBetween(altLetterInt,nextAltLetterInt, 1);
            
        }
        
        int nodes = 26 + 10;
        
        int addedEdges = 0;
        
        for(int n = 0; n < nodes; ++n)
        {
            if (!graph.nodeExists(n))
                continue;
            
            int outDegree = graph.getOutboundDegree(n);
            int inDegree = graph.getInboundDegree(n);
            
            Preconditions.checkState(outDegree > 0 || inDegree > 0);
            
            int diff = outDegree - inDegree;
            
            if (diff > 0)
            {
                addedEdges += diff;
            }
        }
        
        //really +1 -1 because we want the verticies and we can have an odd degree
        int eulerCircuitLength = graph.edgeCount() + addedEdges ;
        
        if (addedEdges == 0)
            eulerCircuitLength ++;
        

        return String.format("Case #%d: %d", in.testCase, eulerCircuitLength);
    }
    
    public void tryToCreateEulerCircuitOnMultiEdgedGraph(DirectedGraphInt graph, BiMap<Character, Integer> charToIntMap)
    {
        LinkedList<IntegerPair> needInDegreeList = new LinkedList<>();
        LinkedList<IntegerPair> needOutDegreeList = new LinkedList<>();
                
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
                needOutDegreeList.offerFirst(new IntegerPair(n, diff));
            else
                needInDegreeList.offerLast(new IntegerPair(n, diff));
        }
        
        log.debug("Graph {}", graph);
        
        int currentNeedOutIdx  = 0;
        while(needOutDegreeList.size() > 1)
        {
            log.debug("Current edge count {}", graph.edgeCount());
            log.debug("Cur out idx {}\nNeed out list sz {},{}\n Need in list sz {}, {}\n",
                    currentNeedOutIdx,
                    needOutDegreeList.size(), needOutDegreeList, needInDegreeList.size(),
                    needInDegreeList);
            
            IntegerPair needOut = needOutDegreeList.get(currentNeedOutIdx);
            
            Preconditions.checkState(needOut.second() < 0);
            
            ListIterator<IntegerPair> needInIt = needInDegreeList.listIterator();
            //Walk down the end of the list, adding connections where we can
            while(needInIt.hasNext())
            {
                IntegerPair needIn = needInIt.next();
            
                Preconditions.checkState(needIn.second() > 0);
                
                
                /*
                if (graph.isConnected(needOut.first(), needIn.first()))
                    continue;
                */
                graph.addOneWayConnection(needOut.first(), needIn.first());
                
            
                needOut.addSecond(1);
                needIn.addSecond(-1);
                
                if (needIn.second() == 0) {
                    needInIt.remove();                    
                }
                
                if (needOut.second()==0) {
                    break;
                }
            }
            
            
            if (needOut.second() == 0) {
                Preconditions.checkState(graph.getInboundDegree(needOut.first()) ==
                        graph.getOutboundDegree(needOut.first()));
                
                needOutDegreeList.remove(currentNeedOutIdx);
            } else {
                //currentNeedOutIdx++;
            }            
        }
        
        
        
        log.debug("Graph with euler path {}", graph);
        
        
        if (needInDegreeList.size() > 0) {
            
            IntegerPair start = needInDegreeList.get(0);
            IntegerPair stop = needOutDegreeList.get(0);
            
            while(start.second() > 1)
            {
                graph.addOneWayConnection(start.first(),stop.first());
                start.addSecond(-1);
                
                stop.addSecond(+1);
            }
            
            startNode = needInDegreeList.get(0).first();
            Preconditions.checkState(needInDegreeList.get(0).second() == 1);
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
            
            if (graph.getOutboundDegree(curNode) == 0) 
            {
                graph.removeNode(curNode);
            }
            
            curNode = nextNode;
            path.append(charToIntMap.inverse().get(curNode));
        }
        
        log.debug("Graph edges {}", graph.edgeCount());
        log.debug("Path {} len {}", path.toString().length(), path.toString());
    }

    @Override
    public String handleCase(InputData in)
    {
        if (in != null)
        //    return handleSmall(in);
            return showMaxFlow(in);
        
        

        return String.format("Case #%d: ", in.testCase);
    }
}