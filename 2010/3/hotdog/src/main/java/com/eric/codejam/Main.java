package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.InputData.Corner;
import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);

        int cost = doBreadthFirstSearch(input.vendors);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + cost);
    }
    
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        
        
        InputData  input = new InputData(testCase);
        
        input.C = Integer.parseInt(br.readLine());
        
        input.vendors = new ArrayList<>(input.C);
        
        for(int i = 0; i < input.C; ++i) {
            String[] line = br.readLine().split(" ");
            input.vendors.add(new Corner(Integer.parseInt(line[0]), Integer.parseInt(line[1])));
        }
        
        return input;
        
    }

    private static class Node {
        Multiset<Integer> vendors;
        Node prevNode;
        int cost;
        public Node(Multiset<Integer> vendors, int distance, Node prevNode) {
            super();
            this.vendors = vendors;
            this.cost = distance;
            this.prevNode = prevNode;
        }
        
    }
    
    String printStreet(Multiset<Integer> vendors, int min, int max) {
        int len = max - min + 1;
        StringBuffer sb = new StringBuffer();
        for(int i = min; i <= max; ++i) {
            int count = vendors.count(i);
            if (count == 0) {
            sb.append(" - ");
            } else {
                sb.append(" ").append(StringUtils.rightPad(Integer.toString(count), 2));
            }
        }
        
        return sb.toString();
    }
    /*
     * Finds shortest distance to a target leftover.
     * 
     * 
     */
    public Integer doBreadthFirstSearch(List<Corner> vendors) {
        
        
        
        
        
        
        Queue<Node> toProcess = new PriorityQueue<>(1, new Comparator<Node>() {
            public int compare(Node a, Node b) {
                return Integer.compare(a.cost, b.cost);
            }
        });
        
        Set<Multiset<Integer>> visited  = new HashSet<>();
         
        Multiset<Integer> initialNode = TreeMultiset.create();
        for(int i = 0; i < vendors.size(); ++i) {
            initialNode.add(vendors.get(i).location, vendors.get(i).count);
        }
        
        toProcess.add(new Node(initialNode,0, null));
                
        while(!toProcess.isEmpty()) {
            Node currentNode = toProcess.poll();
            Multiset<Integer> currentVendors = currentNode.vendors;
                        
            //log.debug("Current node {}  cost {}", currentVendors, currentNode.cost);
            if (visited.contains(currentVendors)) {
                //already visited.  Visited nodes have the minimum cost recorded
                continue;
            }
            
            visited.add(currentVendors);
            
            List<Integer> possibleMoves = new ArrayList<>();
            for(int corner : currentVendors.elementSet() ) {
                if (currentVendors.count(corner) > 1) {
                    possibleMoves.add(corner);
                }
            }
            if (possibleMoves.isEmpty()) {
                StringBuffer sb = new StringBuffer();
                int min = Collections.min(currentNode.vendors.elementSet());
                int max = Collections.max(currentNode.vendors.elementSet());
                
                
                sb.append("\nSolution \n ")
                .append(printStreet(currentNode.vendors,min,max));
                Node node = currentNode;
                while(node.prevNode != null) {
                    node = node.prevNode;
                    sb.append("\n ").append(printStreet(node.vendors,min,max));
                }
                log.debug(sb.toString());
                return currentNode.cost;
            }
            
            for(int i = 0; i < possibleMoves.size(); ++i) {
                int corner = possibleMoves.get(i);
                Multiset<Integer> copy = TreeMultiset.create(currentVendors);
                
                Preconditions.checkState(currentVendors.count(corner) >= 2);
                copy.setCount(corner, copy.count(corner) - 2);
                copy.add(corner-1);
                copy.add(corner+1);
                
                toProcess.add(new Node(copy, currentNode.cost + 1, currentNode));
                               
                                
            }
            
            
        }
        
        return null;
        
    }


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
           // args = new String[] { "B-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}