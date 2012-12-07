package codejam.y2009.sokoban;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Direction;
import codejam.utils.utils.Grid;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Main implements TestCaseInputScanner<InputData>, TestCaseHandler<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    final static Logger perfLog = LoggerFactory.getLogger("perf");
    

    public int getMinSteps(Set<Node> seen, Queue<Node> nodesToProcess) {
        int iterations = 0;
        seen = new HashSet<>();

        while (!nodesToProcess.isEmpty()) {
            Node n = nodesToProcess.iterator().next();

            nodesToProcess.remove(n);

            if (seen.contains(n)) {
                continue;
            }
            seen.add(n);
            ++iterations;
            if (iterations % 1000 == 0) {
                perfLog.debug("Iterations {}", iterations);
            }
            log.debug("Processing {}", n);

            if (n.grid.getIndexesOf(SquareType.Box).isEmpty()) {
                return n.steps;
            }

            generateNodes(n, seen, nodesToProcess);

        }

        return -1;
    }
    
    private boolean isAllConnected( Node n) {
        
        if (n.boxes.size() == 1) {
            return true;
        }
        
        Set<Integer> seen = new HashSet<Integer>();
        List<Integer> toSee = new ArrayList<>();
        
        toSee.add(n.boxes.iterator().next());
        
        while(!toSee.isEmpty()) {
            int box = toSee.remove(0);
            if (seen.contains(box)) {
                continue;
            }
            seen.add(box);
            for(int diag = 0; diag <= 3; ++diag) {
                Direction dir = Direction.NORTH.turn(diag * 2);
                Integer idx = n.grid.getIndex(box, dir);
                SquareType sq = n.grid.getEntry(box,  dir);
                if (sq.isBox()) {
                    toSee.add(idx);
                }
            }
        }
        
        if (seen.equals(n.boxes)) {
            return true;
        }
        
        return false;
    }
    
    public void generateNodes(Node n, Set<Node> seen,
    Queue<Node> nodesToProcess) {
        //Diagonal
        //...
        //No.
        //FoF
        //F..
        
        log.debug("Node generateNodes {}", n);
        /*
        Set<Integer> stop = new HashSet<>();
        stop.addAll( Arrays.asList(new Integer[] {
        4, 13, 22, 23, 33 } ));
        
        if (n.boxes.equals(stop)) {
            log.info("Stop");
        }*/
        
        for(int box : n.boxes) {
            
            for(int diag = 0; diag <= 3; ++diag) {
                Direction dir = Direction.NORTH.turn(diag * 2);
                
                final Integer targetSquareIndex = n.grid.getIndex(box,dir);
                SquareType targetSquare = n.grid.getEntry(box, dir);
                SquareType[] neededEmpty = 
                        new SquareType[] {targetSquare, n.grid.getEntry(box, dir.turn(4))
                 };
                
                
                boolean valid = true;
                
                for(SquareType needToBeEmpty : neededEmpty) {
                    if(!needToBeEmpty.isEmpty()) {
                        valid = false;
                        break;
                    }                        
                }
                
                if (!valid) {
                    continue;
                }
                
                Set<Integer> newBoxes = new HashSet<>(n.boxes);
                Grid<SquareType> newGrid = new Grid<>(n.grid);
                newGrid.setEntry(box, newGrid.getEntry(box).removeBox());
                newGrid.setEntry(targetSquareIndex, newGrid.getEntry(targetSquareIndex).addBox());
                
                newBoxes.remove(box);
                newBoxes.add(targetSquareIndex);
                
                Node newSteps = new Node(newBoxes, 1+n.steps, newGrid);
                if (seen.contains(newSteps)) {
                    continue;
                }
                newSteps.isConnected =  isAllConnected(newSteps);
                if (!n.isConnected && !newSteps.isConnected) {
                    continue;
                }
                                
                log.debug("Adding node {}", newSteps);
                nodesToProcess.add(newSteps);
                
            }
        }
    }


    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData input) {
        

        Set<Node> seen = new HashSet<Node>();
        Queue<Node> nodesToProcess;
        
       // nodesToProcess = new PriorityQueue<>(1, new Node.PriorityCompare());
        nodesToProcess = new LinkedList<>();
        
        Set<Integer> listBoxes = input.grid.getIndexesOf(SquareType.Box);
        listBoxes.addAll(input.grid.getIndexesOf(SquareType.BoxOnGoal));
        nodesToProcess.add(new Node(listBoxes,0,input.grid));
        
        int min = getMinSteps(seen,nodesToProcess);

        log.info("Starting case {}", input.testCase);

        return ("Case #" + input.testCase + ": " + min);
    }

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        
        
        
        BiMap<Character, SquareType> mapping = HashBiMap.create(5);
        mapping.put('#', SquareType.Wall);
        mapping.put('.', SquareType.Empty);
        mapping.put('x', SquareType.Goal);
        mapping.put('o', SquareType.Box);
        mapping.put('w', SquareType.BoxOnGoal);
        
        Grid<SquareType> grid = Grid.buildFromScanner(scanner, row,col,  mapping, SquareType.Invalid);
        
        input.grid = grid;
        input.row = row;
        input.col = col;

        return input;
    }
}