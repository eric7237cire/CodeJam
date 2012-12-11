package codejam.y2011.round_1A.dominion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.DAG;
import codejam.utils.datastructures.DAG.Edge;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.N = scanner.nextInt();
        for(int i = 0; i < input.N; ++i) {
            input.hand.add(new Card(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), i));
        }
        input.M = scanner.nextInt();
        for(int i = 0; i < input.M; ++i) {
            input.deck.add(new Card(scanner.nextInt(), scanner.nextInt(), scanner.nextInt(), i + input.N));
        }
        return input;
    }

    @Override
    public String handleCase(InputData input) {
        List<Card> c0_cards = new ArrayList<>();
        List<Card> c1_cards = new ArrayList<>();
        List<Card> c2_cards = new ArrayList<>();
        List<Card> t_cards = new ArrayList<>();
        
        for(Card card : input.hand) {
            if (card.T >= 1) {
                t_cards.add(card);
            } else if (card.C == 0) {
                c0_cards.add(card);
            } else if (card.C == 1) {
                c1_cards.add(card);
            } else if (card.C == 2) {
                c2_cards.add(card);
            } else {
                log.error("c2 invalid");
            }
        }
        for(Card card : input.deck) {
            if (card.T >= 1) {
                t_cards.add(card);
            } else if (card.C == 0) {
                c0_cards.add(card);
            } else if (card.C == 1) {
                c1_cards.add(card);
            } else if (card.C == 2) {
                c2_cards.add(card);
            } else {
                log.error("c2 invalid");
            }
        }
        
        Node.TOTAL_CARDS = input.N + input.M;
        
        //Generate all nodes to DAG
        Node startingNode = new Node(input.N, 1, 0,0,0);
        
        DAG<Node> graph = new DAG<>();
        
        graph.addNode(startingNode);
        LinkedList<Node> nodesToProcess = new LinkedList<>();
        nodesToProcess.add(startingNode);
        
        Set<Node> visited = Sets.newHashSet();
        
        
        while(!nodesToProcess.isEmpty()) {
            Node node = nodesToProcess.poll();
            
            if (visited.contains(node)) {
                continue;
            }
            
            visited.add(node);
            
            if (node.turns == 0)
                continue;
            
            //TODO can improve by removing t from Node definition
            Node newTNode = new Node(node.hand ,
                    node.turns,
                    node.t,
                    node.c1,
                    node.c2
                    );
            int t_weight = 0;
            
            //t nodes
            for(int t = node.t; t < t_cards.size(); ++t) {
                Card card = t_cards.get(t);
                if ( card.index >= newTNode.hand ) 
                    break;
                
                newTNode.hand += card.C;
                newTNode.turns += card.T - 1;
                newTNode.t = t+1;
                t_weight += card.S;
            }
            
            if (newTNode.t > node.t) {
                newTNode.limit();
                graph.addNode(newTNode);
                graph.addEdge(node,newTNode,t_weight);
                nodesToProcess.add(newTNode);
                continue;
            }
            
           
            
            //c1. play it 
            if (node.c1 < c1_cards.size() && c1_cards.get(node.c1).index < node.hand ) {
                Node newNode = new Node(node.hand+1,node.turns-1, node.t, 1+node.c1, node.c2);
                newNode.limit();
                graph.addNode(newNode);
                graph.addEdge(node, newNode, c1_cards.get(node.c1).S);
                nodesToProcess.add(newNode);
            
            
                //skip it            
                Node newSkipNode = new Node(node.hand,node.turns, node.t, 1+node.c1, node.c2);
                newSkipNode.limit();
                graph.addNode(newSkipNode);
                graph.addEdge(node, newSkipNode, 0);
                nodesToProcess.add(newSkipNode);
            }
            
            //play c2
            if (node.c2 < c2_cards.size() && c2_cards.get(node.c2).index < node.hand) {
                Node newNode = new Node(node.hand+2,node.turns-1, node.t, node.c1, node.c2+1);
                newNode.limit();
                graph.addNode(newNode);
                graph.addEdge(node, newNode, c2_cards.get(node.c2).S);
                nodesToProcess.add(newNode);
            
                Node newSkipNode = new Node(node.hand,node.turns, node.t, node.c1, node.c2+1);
                newSkipNode.limit();
                graph.addNode(newSkipNode);
                graph.addEdge(node, newSkipNode, 0);
                nodesToProcess.add(newSkipNode);
            }
            
            //play c0s
            
            //TODO precomute like max[hand][index]
            
            //Will be first index to large
            int max_c0_index = 0;
            while(max_c0_index < c0_cards.size() && c0_cards.get(max_c0_index).index < node.hand) {
                ++max_c0_index;
            }
            List<Card> canPick = new ArrayList<>(c0_cards.subList(0, max_c0_index));
            
            int weight = 0;
            for(int i = 0; i < node.turns; ++i) {
                if (canPick.isEmpty())
                    break;
                
                Card max = Collections.max(canPick, new Comparator<Card>() {

                    @Override
                    public int compare(Card o1, Card o2) {
                        return Integer.compare(o1.S, o2.S);
                    }                    
                });
                
                weight += max.S;
                canPick.remove(max);
                
            }
            
            Node newNode = new Node(-1, 0, -1,-1,-1);
            graph.addNode(newNode);
            graph.addEdge(node,newNode,weight);
            
        }
        
        
        log.info("Nodes: " + graph.getNumNodes());
        memoize = new int[graph.getNumNodes()];
        Arrays.fill(memoize,-1);
        
        int maxWeight = getMaxDistance(graph,0);
        return "Case #" + input.testCase + ": " + maxWeight;
    }
    
    /**
     * Doing a bottom up would require a topological sort
     * @param graph
     * @param nodeNum
     * @return
     */
    public int getMaxDistance(DAG<Node> graph, int nodeNum) {
        
        if (memoize[nodeNum] >= 0) 
            return memoize[nodeNum];
        
        Node node = graph.nodes.inverse().get(nodeNum);
        
        log.debug("getMaxDistance. Num {} Node {}", nodeNum, node);
        
        List<Edge> edges = graph.connections.get(nodeNum);
        
        int maxWeight = 0;
        
        if (edges != null) {
            for (Edge edge : edges) {
                int weight = graph.edges.get(edge);
                Preconditions.checkState(nodeNum == edge.from);
                weight += getMaxDistance(graph, edge.to);
                maxWeight = Math.max(weight, maxWeight);
                
                Node toNode = graph.nodes.inverse().get(edge.to);
                log.debug("Weight from node {} = weight {}.  To Node {}", edge.from, weight, toNode );
            }
        }

        memoize[nodeNum] = maxWeight;
        return maxWeight;
    }

    int[] memoize;
}
