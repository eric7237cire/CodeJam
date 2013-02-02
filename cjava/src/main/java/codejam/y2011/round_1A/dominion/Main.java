package codejam.y2011.round_1A.dominion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.graph.WeightedGraphInt;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

    static int large = 1;
    static int small = 0;
    public Main() {        
        super("C", small==1, large==1);
    }
    
    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData in = new InputData(testCase);
        in.N = scanner.nextInt();
        
        in.c0_cards = new ArrayList<>();
        in.c1_cards = new ArrayList<>();
        in.c2_cards = new ArrayList<>();
        in.t_cards = new ArrayList<>();
        
        for(int i = 0; i < in.N; ++i) {
            Card card = new Card(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), i);
            classifyCard(card, in);
            in.hand.add(card);
        }
        in.M = scanner.nextInt();
        for(int i = 0; i < in.M; ++i) {
            Card card = new Card(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), i + in.N);
            classifyCard(card, in);
            in.deck.add(card);
        }
        return in;
    }
    
    private static void classifyCard(Card card, InputData input) {
        if (card.T >= 1) {
            input.t_cards.add(card);
        } else if (card.C == 0) {
            input.c0_cards.add(card);
        } else if (card.C == 1) {
            input.c1_cards.add(card);
        } else if (card.C == 2) {
            input.c2_cards.add(card);
        } else {
            log.error("c2 invalid");
        }
    }
    
    /**
     * Because we end the algorithm by choosing best C0 cards;
     * build an array int[index][cards left]
     * @param input
     * @return
     */
    private static int[][] buildC0(InputData input) {
        
        Ordering<Card> maxS = Ordering.natural().reverse();
        
        Set<Card> maxScoreC0cards = new TreeSet<>(maxS);
        
        int numCards = input.M + input.N;
        
        int[][] maxC0 = new int[numCards][numCards+1];
        
        for(int c0CardIndex = 0; c0CardIndex < input.c0_cards.size(); ++c0CardIndex) {
            Card card = input.c0_cards.get(c0CardIndex);
            
            int index = card.index;
            int nextIndex = c0CardIndex < input.c0_cards.size() - 1 ? input.c0_cards.get(c0CardIndex+1).index
                    : numCards;
            
            maxScoreC0cards.add(card);
            
            for(int curIndex = index; curIndex < nextIndex; ++curIndex) {
                int cardsUsed = 0;
                for(Card maxCard : maxScoreC0cards) {
                    ++cardsUsed;
                    maxC0[curIndex][cardsUsed] = maxC0[curIndex][cardsUsed-1] + maxCard.S; 
                }
                ++cardsUsed;
                //Fill in rest of array
                for(; cardsUsed <= numCards; ++cardsUsed) {
                    maxC0[curIndex][cardsUsed] = maxC0[curIndex][cardsUsed-1];
                }
            }
        }
        
        return maxC0;
    }
    
    
    public static int getNodeIndex(Node node, Map<Node,Integer> nodeIndexes) {
        Integer index = nodeIndexes.get(node);
        
        if (index == null) {
            index = nodeIndexes.size();
            nodeIndexes.put(node,index);
        }
        
        return index;
    }
    
    public static void addConnection(Integer nodeIndex, Node newNode, int weight, WeightedGraphInt graph, Map<Node,Integer> nodeIndexes) {
        Integer newNodeIndex = getNodeIndex(newNode, nodeIndexes);
        graph.addOneWayConnection(nodeIndex,newNodeIndex,weight );
    }
    
public String handleCase(InputData in) {
        
        
        List<Card> c1_cards = in.c1_cards;
        List<Card> c2_cards = in.c2_cards;
        List<Card> t_cards = in.t_cards;
        
        int TOTAL_CARDS = in.N + in.M;
        
        /**
         * best[Index][cards left]
         */
        int[][] bestC0Cards = buildC0(in);
        
        //Generate all nodes to DAG
        Node startingNode = new Node(in.N, 1, 0,0,0,TOTAL_CARDS);
        
        //DAG<Node> graph = new DAG<>();
        WeightedGraphInt graph = new WeightedGraphInt();
        
        //graph.addNode(startingNode);
        LinkedList<Node> nodesToProcess = new LinkedList<>();
        nodesToProcess.add(startingNode);
        
        Set<Integer> visited = Sets.newHashSet();
        
        Map<Node,Integer> nodeIndexes = Maps.newHashMap();
                
        while(!nodesToProcess.isEmpty()) {
            Node node = nodesToProcess.poll();
            Integer nodeIndex = getNodeIndex(node, nodeIndexes);
            
            if (visited.contains(nodeIndex)) {
                continue;
            }
            
            visited.add(nodeIndex);
            
            if (node.turns == 0)
                continue;

            int t_weight = 0;

            int tHand = node.hand;
            int tT = node.t;
            int tTurns = node.turns;

            //t nodes ; play all at once
            for (int t = node.t; t < t_cards.size(); ++t)
            {
                Card card = t_cards.get(t);
                if (card.index >= tHand)
                    break;

                tHand += card.C;
                tTurns += card.T - 1;
                tT = t + 1;
                t_weight += card.S;
            }

            if (tT > node.t)
            {

                Node newTNode = new Node(tHand, tTurns, tT, node.c1, node.c2, TOTAL_CARDS);

                addConnection(nodeIndex, newTNode, t_weight, graph, nodeIndexes);
                nodesToProcess.add(newTNode);
                continue;
            }
            
           
            
            //c1. play it 
            if (node.c1 < c1_cards.size() && c1_cards.get(node.c1).index < node.hand ) {
                Node newNode = new Node(node.hand+1,node.turns-1, node.t, 1+node.c1, node.c2,TOTAL_CARDS);
                
                addConnection(nodeIndex, newNode, c1_cards.get(node.c1).S, graph, nodeIndexes);                
                nodesToProcess.add(newNode);
            
                //skip it            
                Node newSkipNode = new Node(node.hand,node.turns, node.t, 1+node.c1, node.c2, TOTAL_CARDS);
                
                addConnection(nodeIndex, newSkipNode, 0, graph, nodeIndexes);                
                nodesToProcess.add(newSkipNode);
            }
            
            //play c2
            if (node.c2 < c2_cards.size() && c2_cards.get(node.c2).index < node.hand) {
                Node newNode = new Node(node.hand+2,node.turns-1, node.t, node.c1, node.c2+1,TOTAL_CARDS);
                
                addConnection(nodeIndex, newNode, c2_cards.get(node.c2).S, graph, nodeIndexes);
                nodesToProcess.add(newNode);
            
                Node newSkipNode = new Node(node.hand,node.turns, node.t, node.c1, node.c2+1,TOTAL_CARDS);

                addConnection(nodeIndex, newSkipNode, 0, graph, nodeIndexes);                
                nodesToProcess.add(newSkipNode);
            }
            
            //play c0s
            
            int weight = bestC0Cards[node.hand-1][node.turns];
            
            //Final node
            Node newNode = new Node(-1, 0, -1,-1,-1,TOTAL_CARDS);
            
            addConnection(nodeIndex, newNode, weight, graph, nodeIndexes);
            
            
        }
        
        
        //log.info("Nodes: " + graph..getNumNodes());

        int[] memoize = new int[nodeIndexes.size()];
        Arrays.fill(memoize,-1);
        
        int maxWeight = getMaxDistance(graph,0,memoize);
        return "Case #" + in.testCase + ": " + maxWeight;
    }

    /**
     * Doing a bottom up would require a topological sort.
     * 
     * Finds longest weighted path in the graph
     * @param graph
     * @param nodeNum
     * @return
     */
    public static int getMaxDistance(WeightedGraphInt graph, int nodeNum, int[] memoize)
    {

        if (memoize[nodeNum] >= 0)
            return memoize[nodeNum];

        Set<WeightedGraphInt.Edge> edges = graph.getEdges(nodeNum);

        int maxWeight = 0;

        if (edges == null) {
            memoize[nodeNum] = maxWeight;
            return maxWeight;
        }
        
        for (WeightedGraphInt.Edge edge : edges)
        {
            int weight = edge.weight;

            /**
             * We can do this because it is acyclic
             */
            weight += getMaxDistance(graph, edge.to, memoize);
            maxWeight = Math.max(weight, maxWeight);
        }
    

        memoize[nodeNum] = maxWeight;
        return maxWeight;
    }
   

}
