package codejam.y2010.round_3.boards;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.ArrayUtils;

import ch.qos.logback.classic.Level;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

public class Boards extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    final static int MAX_BOARD_LENGTH = 100000;
    final static int MAX_N = 100;
    
    public Boards()
    {
        super("B", 1, 1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
    
    
    private static class Node {
        Integer residue;
        int cost;
        public Node(Integer residue, int distance) {
            super();
            this.residue = residue;
            this.cost = distance;
        }
        
    }
    /*
     * Finds shortest distance to a target leftover.
     * 
     * 
     */
    public Integer doBreadthFirstSearch(int boardLengths[], int largestBoardLength, int targetResidue) {
        int[] distance = new int[largestBoardLength];
        int[] previous = new int[largestBoardLength];
        
        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(previous, -1);
        
        Queue<Node> toProcess = new PriorityQueue<>(boardLengths.length, new Comparator<Node>() {
            public int compare(Node a, Node b) {
                return Integer.compare(a.cost, b.cost);
            }
        });
                
        toProcess.add(new Node(0,0));
        distance[0] = 0;
        
        while(!toProcess.isEmpty()) {
            Node currentNode = toProcess.poll();
            int currentResidue = currentNode.residue;
                        
            //log.debug("Current residue {}  cost {}", currentResidue, currentNode.cost);
            if (distance[currentResidue] < currentNode.cost) {
                //already visited.  Visited nodes have the minimum cost recorded
                continue;
            }
            
            if (currentResidue == targetResidue) {
                return currentNode.cost;
            }
            
            for(int i = 0; i < boardLengths.length - 1; ++i) {
                Integer newResidue = currentResidue + boardLengths[i];
                //Costs 1 to add a new board
                int cost = 1;
                if (newResidue >= largestBoardLength) {
                    //take away a larger board
                    --cost;
                    newResidue -= largestBoardLength; 
                }
                
                Preconditions.checkState(distance[currentResidue] < Integer.MAX_VALUE);
                int newCost = distance[currentResidue] + cost;
                
                if (newCost < distance[newResidue]) {
                    distance[newResidue] = newCost;
                    previous[newResidue] = currentResidue;
                    toProcess.add(new Node(newResidue, newCost));
                }               
                                
            }
            
            Preconditions.checkState(distance[currentResidue] >= currentNode.cost);
            distance[currentResidue] = currentNode.cost;
        }
        
        return null;
        
    }
    
    
    
    @Override
    public String handleCase(InputData input) {
        int caseNumber = input.testCase;

        log.info("Starting calculating case {}", caseNumber);

        // We are forced to use the max index, see solution for explanation.

        int maxBoardIndex = input.N - 1;

        log.debug("Case number {} max board index {}", caseNumber,
                maxBoardIndex);

        final long sum = input.L / input.boardLens[maxBoardIndex];
        int rest = Ints.checkedCast(input.L % input.boardLens[maxBoardIndex]);

        Integer cost = doBreadthFirstSearch(input.boardLens,
                input.boardLens[maxBoardIndex], rest);

        if (cost == null) {
            return ("Case #" + caseNumber + ": IMPOSSIBLE")                                     ;
        }
        long total = sum + cost;

        return ("Case #" + caseNumber + ": " + total);

    }
    
    
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
    
        
        InputData  input = new InputData(testCase);

        input.L = scanner.nextLong();
        input.N = scanner.nextInt();
        
        input.boardLens = new int[input.N];
        
        
        for(int i = 0; i < input.N; ++i) {
            input.boardLens[i] = scanner.nextInt();
           
        }
        
        List<Integer> ii = Arrays.asList(ArrayUtils.toObject(input.boardLens));
        SortedSet<Integer> ss = new TreeSet<Integer>(ii);
        
        Integer[] array = new Integer[ss.size()];
        ss.toArray(array);
        input.boardLens = ArrayUtils.toPrimitive(array);
        
        input.N = input.boardLens.length;
        
        return input;
        
    }

    
}