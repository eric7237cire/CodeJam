package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.utils.Direction;
import com.eric.codejam.utils.Grid;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    final static Logger perfLog = LoggerFactory.getLogger("perf");
    
    private Set<Node> seen;
    private PriorityQueue<Node> nodesToProcess;

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        Main m = Main.buildMain(scanner);
        int min = m.getMinSteps();

        log.info("Starting case {}", caseNumber);

        os.println("Case #" + caseNumber + ": " + min);

    }
    
    public int getMinSteps() {
        int iterations = 0;
        seen = new HashSet<>();
        
        while(!nodesToProcess.isEmpty()) {
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
            
            generateNodes(n);
            
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
    
    public void generateNodes(Node n) {
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
                this.nodesToProcess.add(newSteps);
                
            }
        }
    }

    private static Main buildMain(Scanner scanner) {
        
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        
        Main m = new Main();
        
        BiMap<Character, SquareType> mapping = HashBiMap.create(5);
        mapping.put('#', SquareType.Wall);
        mapping.put('.', SquareType.Empty);
        mapping.put('x', SquareType.Goal);
        mapping.put('o', SquareType.Box);
        mapping.put('w', SquareType.BoxOnGoal);
        
        Grid<SquareType> grid = Grid.buildFromScanner(scanner, row,col,  mapping, SquareType.Invalid);
        
        m.nodesToProcess = new PriorityQueue<>(1, new Node.PriorityCompare());
        
        Set<Integer> listBoxes = grid.getIndexesOf(SquareType.Box);
        listBoxes.addAll(grid.getIndexesOf(SquareType.BoxOnGoal));
        m.nodesToProcess.add(new Node(listBoxes,0,grid));

        return m;
    }

    public Main() {

        // TODO Add test case vars
        super();
    }

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
        }
        log.info("Input file {}", args[0]);

        Scanner scanner = new Scanner(new File(args[0]));

        int t = scanner.nextInt();

        for (int i = 1; i <= t; ++i) {

            handleCase(i, scanner, System.out);

        }

        scanner.close();
    }
}