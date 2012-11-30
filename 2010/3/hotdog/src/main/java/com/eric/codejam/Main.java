package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.InputData.Corner;
import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    static CircularFifoBuffer lastElems = new CircularFifoBuffer(3);
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);

        long cost = 0;
        //cost = doBreadthFirstSearch(input.vendors);
        
        
        //int cost2 = takeFirstMove(input.vendors);
        
        cost = takeAllMove(input.vendors);
    
        /*lastElems.add(cost2);
        
        int lastEl = 0;
        Iterator<Integer> it = lastElems.iterator();
       // while(it.hasNext()) {
            lastEl = (Integer) it.next();
        //}*/
        
        //log.debug("Case {} cost {} diff {}",  caseNumber, cost2, cost2 - lastEl);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " +  cost);
    }
    
    @Override
    public InputData readInput(BufferedReader br, int testCase)
            throws IOException {

        InputData input = new InputData(testCase);

        input.C = Integer.parseInt(br.readLine());

        input.vendors = new ArrayList<>(input.C);

        for (int i = 0; i < input.C; ++i) {
            String[] line = br.readLine().split(" ");
            input.vendors.add(new Corner(Integer.parseInt(line[0]), Integer
                    .parseInt(line[1])));
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
            
            for(int i = 0; i < 1; ++i) {
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

 public long takeAllMove(List<Corner> vendors) {
                
        
        Multiset<Integer> currentVendors = TreeMultiset.create();
        LinkedList<Integer> toProcess = new LinkedList<Integer>();
        
        for(int i = 0; i < vendors.size(); ++i) {
            currentVendors.add(vendors.get(i).location, vendors.get(i).count);
            if (vendors.get(i).count > 1) {
                toProcess.add(vendors.get(i).location);
            }
        }
        long cost = 0;
        
        int iterations = 0;
        
        while(!toProcess.isEmpty()) {
            
            
           
           
            Integer corner = toProcess.pollFirst();
            
            
            int count = currentVendors.count(corner);
            if (count <= 1)
                continue;
            
            ++iterations;
            if (iterations % 1000000 == 0) 
                log.debug("Iterations {}.  Size {}  corner count {}", iterations/100000, currentVendors.elementSet().size(), count);
            
            int half = count / 2;
            long costAdd = (long)half*(half+1)*(2*half+1)/6;
            cost+=costAdd;
            
            for(int leftSide = corner - half; leftSide < corner; ++leftSide) {                
                int beforeCount = currentVendors.count(leftSide);
                beforeCount ++;
                if (beforeCount > 1)
                    toProcess.add(leftSide);
                currentVendors.setCount(leftSide,beforeCount);
            }
            for(int rightSide = corner + half; rightSide > corner; --rightSide) {                
                int beforeCount = currentVendors.count(rightSide);
                beforeCount ++;
                if (beforeCount > 1)
                    toProcess.add(rightSide);
                currentVendors.setCount(rightSide,beforeCount);
            }
            
            if (count % 2 == 0) {
                currentVendors.setCount(corner, 0);
            } else {
                currentVendors.setCount(corner, 1);
            }
            
            if (cost % 1000000 == 0) {
                log.debug("Cost is {}", cost);
            }
            
            
        }
        
        return cost;
    }

    /*
     * Finds shortest distance to a target leftover.
     * 
     * 
     */
    public Integer takeFirstMove(List<Corner> vendors) {
                
        
        Multiset<Integer> vendorCounts = TreeMultiset.create();
        for(int i = 0; i < vendors.size(); ++i) {
            vendorCounts.add(vendors.get(i).location, vendors.get(i).count);
        }
        int cost = 0;
        
        while(true) {
            
            Multiset<Integer> currentVendors = vendorCounts;
                        
            Integer possibleMove = null;
            for(int corner : currentVendors.elementSet() ) {
                if (currentVendors.count(corner) > 1) {
                    possibleMove = corner;
                }
            }
            if (possibleMove == null) {
                /*StringBuffer sb = new StringBuffer();
                int min = Collections.min(currentVendors.elementSet());
                int max = Collections.max(currentVendors.elementSet());
                
                
                sb.append("\nSolution \n ")
                .append(printStreet(currentVendors,min,max));
                Node node = currentNode;
                while(node.prevNode != null) {
                    node = node.prevNode;
                    sb.append("\n ").append(printStreet(node.vendors,min,max));
                }
                log.debug(sb.toString());*/
                return cost;
            }
        
            ++cost;
            if (cost % 100000 == 0) {
                log.debug("Cost is {}", cost);
            }
            currentVendors.setCount(possibleMove, currentVendors.count(possibleMove) - 2);
            currentVendors.add(possibleMove-1);
            currentVendors.add(possibleMove+1);
                
                               
                                
            
            
            
        }
        
        
    }
    
    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
           // args = new String[] { "C-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}