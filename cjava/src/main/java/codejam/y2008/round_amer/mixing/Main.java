package codejam.y2008.round_amer.mixing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.graph.GraphInt;
import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    public Main() {
        super("A", true, true);
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        int N = scanner.nextInt();
        InputData input = new InputData(testCase);
        input.ingredients = new LinkedHashMap<>();
        
        for(int i = 0; i < N; ++i) {
            String mixture = scanner.next();
            int M = scanner.nextInt();
            List<String> ingredients = new ArrayList<>(M);
            for(int m = 0; m < M; ++m) {
                ingredients.add(scanner.next());
            }
            input.ingredients.put(mixture,ingredients);
        }
        return input;
    }
    
    private int getNum(String mixtureName, Map<String, Integer> strIndex) {
        Integer idx = strIndex.get(mixtureName);
        if (idx == null) {
            idx = strIndex.size();
            strIndex.put(mixtureName, idx);
        }
        return idx;
    }

    @Override
    public String handleCase(InputData input) {
        Map<String, Integer> strIndex = new HashMap<>();
        
        GraphInt graph = new GraphInt();
        
        for(String mixture : input.ingredients.keySet()) {
            List<String> ingredients = input.ingredients.get(mixture);
            
            /**
             * Assign a number to each mixture if necesary
             * and return it
             */
            int mixNum = getNum(mixture, strIndex);
            
            for(String ingredient : ingredients) {
                //Not a mixture
                if (ingredient.matches("[a-z]*")) {
                    continue;
                }
                
                int ingNum = getNum(ingredient, strIndex);
                /**
                 * Create a connection in the graph
                 */
                graph.addConnection(mixNum,ingNum);
            }
            
        }
        
        final int ROOT = 0;
        final int nNodes = strIndex.size();
        final int ROOT_PARENT = nNodes;
        
        //The first node is the mixture we are making
        if (graph.getNeighbors(ROOT) == null || graph.getNeighbors(ROOT).isEmpty()) {
            return String.format("Case #%d: %d", input.testCase, 1);
        }
        
        
        int[] parentNode = new int[strIndex.size()];
        Arrays.fill(parentNode, -1);
        
        Queue<Integer> toVisit = new LinkedList<>();
        LinkedList<Integer> vertexOrdering = new LinkedList<>();
        
        parentNode[ROOT] = ROOT_PARENT;
        
        toVisit.add(ROOT);        
        
        //BFS
        while(!toVisit.isEmpty()) {
            Integer nodeId = toVisit.poll();

            vertexOrdering.add(nodeId);
            Preconditions.checkState(parentNode[nodeId] >= 0);
            
            Set<Integer> connections = graph.getNeighbors(nodeId);
            //Get all new children from old tree
            for(Integer childNode : connections) {
                if (parentNode[childNode] == -1) {
                    toVisit.add(childNode);
                    parentNode[childNode] = nodeId;
                }
            }
        }
        
        //Reverse the BFS to make it children first
        Collections.reverse(vertexOrdering);
        
                
        int[] bowlsNeededPerNode = new int[nNodes]; 
        
        for(Integer nodeId : vertexOrdering)
        {
        
            Set<Integer> connections = graph.getNeighbors(nodeId);            
            Integer parentNodeId = parentNode[nodeId];
            
           
            
            List<Integer> bowlsNeeded = new ArrayList<>();
            
            for(Integer childNode : connections) {
                if (childNode.equals(parentNodeId)) {
                    //not visited
                    Preconditions.checkState(bowlsNeededPerNode[childNode] == 0);
                    continue;
                }
            
                //All children should be visited
                Preconditions.checkState(bowlsNeededPerNode[childNode] > 0);
                
                bowlsNeeded.add(bowlsNeededPerNode[childNode]);
            }
            
            //Mix most expensive ingredient first
            Collections.sort(bowlsNeeded, Ordering.natural().reverse());
            
            //Minimum is all the pre-req mixtures + 1 to add this ingredient            
            int bowlsUsed = 1+bowlsNeeded.size();
            //Mix most demanding ingredient first
            for(int ing = 0; ing < bowlsNeeded.size(); ++ing) {
                Preconditions.checkState(bowlsNeeded.get(ing) > 0);
                //To mix this ingredient, we need ing bowls to stock prereqs ingredients already mixed + 
                //the temporary need
                int usedBowls = ing + bowlsNeeded.get(ing);
                bowlsUsed = Math.max(usedBowls, bowlsUsed);
            }
            
            
            Preconditions.checkState(bowlsNeededPerNode[nodeId] == 0);
            bowlsNeededPerNode[nodeId] = bowlsUsed;
            
        }
        
        
        return String.format("Case #%d: %d", input.testCase, bowlsNeededPerNode[0]);
    }

    final static Logger log = LoggerFactory.getLogger(Main.class);

}
