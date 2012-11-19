package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    private static class Node implements Comparable<Node> {
        int cost;
        List<Integer> pixels;
        boolean fastTrack;
        Node prev;
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + cost;
            result = prime * result
                    + ((pixels == null) ? 0 : pixels.hashCode());
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;
            if (cost != other.cost)
                return false;
            if (pixels == null) {
                if (other.pixels != null)
                    return false;
            } else if (!pixels.equals(other.pixels))
                return false;
            return true;
        }
        @Override
        public String toString() {
            return "Node [cost=" + cost + ", pixels=" + pixels + "]";
        }
        @Override
        public int compareTo(Node o) {
            
            return ComparisonChain.start().compareTrueFirst(fastTrack, o.fastTrack).compare(cost, o.cost).compare(pixels.size(), o.pixels.size()).compare(pixels.hashCode(), o.pixels.hashCode()).result();
        }
        public Node(int cost, List<Integer> pixels) {
            super();
            this.cost = cost;
            this.pixels = pixels;
        }
    }
    
    public void checkNode(Node node, InputData input) {
        if (node.prev == null)
            return;
        
        int unsmoothCount = 0;
        for(int i = 0; i < node.pixels.size() - 1; ++i) {
            if (Math.abs(node.pixels.get(i) - node.pixels.get(i+1)) > input.minimumDist) {
                unsmoothCount++;
            }
        }
        
        int prevCount = 0;
        for(int i = 0; i < node.prev.pixels.size() - 1; ++i) {
            if (Math.abs(node.prev.pixels.get(i) - node.prev.pixels.get(i+1)) > input.minimumDist) {
                prevCount++;
            }
        }
        
       // Preconditions.checkState(unsmoothCount < prevCount);
    }
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}.  Input {} Delete {} Min dist {}. \n Pixels {}", caseNumber, input.insertCost, input.deleteCost, input.minimumDist, input.pixels);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);
        
        SortedSet<Node> nodes = new TreeSet<>();
        Set<List<Integer>> seen = new HashSet<>();

        nodes.add(new Node(0, input.pixels));
        
        int count  = 0;
        while(!nodes.isEmpty()) {
            Node node = nodes.first();
            nodes.remove(node);
            ++count;
            checkNode(node, input);
            if (seen.contains(node.pixels)) {
                continue;
            }
            seen.add(node.pixels);
            
            if (count % 10 == 0) {
           log.debug("Processing node {} count {}", node, count);
            }
            
            boolean valid = true;
            for(int i = 0; i < node.pixels.size() - 1; ++i) {
                if (Math.abs(node.pixels.get(i) - node.pixels.get(i + 1)) > input.minimumDist) {
                    valid = false;
                    
                    for(int n = i; n <= i + 1; ++n) {
                    Node del = new Node(input.deleteCost + node.cost, new ArrayList<Integer>(node.pixels));
                    del.pixels.remove(n);
                    nodes.add(del);
                    del.prev = node;
                    }
                    
                    for(int v = 0; v <= 255; ++v) {
                        for(int n = i; n <= i + 1; ++n) {
                            int curVal = node.pixels.get(n);
                            int otherVal = node.pixels.get(i==n ? i+1 : i);
                            if (Math.abs(v - otherVal) > input.minimumDist) {
                                continue;
                            }
                            Node changeValue = new Node(node.cost + Math.abs(v - curVal), new ArrayList<Integer>(node.pixels));
                            changeValue.pixels.set(n, v);
                            changeValue.prev = node;
                            nodes.add(changeValue);
                        }
                        
                        
                        
                    }
                    
                    int curVal = node.pixels.get(i);
                    int nextVal = node.pixels.get(i+1);
                 
                    int min = Math.min(curVal, nextVal);
                    int max = Math.max(curVal, nextVal);

                    if (input.minimumDist > 0) {
                        Node insertNode = new Node(node.cost,
                                new ArrayList<Integer>(node.pixels));
                        int insertCount = 0;
                        if (curVal < nextVal) {
                            for (int v = min + input.minimumDist; max > v; v += input.minimumDist) {
                                insertNode.pixels.add(i + (++insertCount), v);
                                insertNode.cost += input.insertCost;
                                
                            }
                        } else {
                            for (int v = max - input.minimumDist; min < v; v -= input.minimumDist) {
                                insertNode.pixels.add(i + (++insertCount), v);
                                insertNode.cost += input.insertCost;
                                
                            }
                        }
                        
                        if (insertCount > 1 && insertNode.pixels.size() > i + insertCount) {
                        Node insertNode2 = new Node(insertNode.cost,
                                new ArrayList<Integer>(insertNode.pixels));
                        insertNode2.pixels.remove(i + insertCount);
                        insertNode2.cost -= input.insertCost;
                        
                        int cost = insertNode2.pixels.get(i+insertCount);
                        int newCost = insertNode2.pixels.get(i+insertCount-1) + input.minimumDist;
                        if (curVal > nextVal) 
                            newCost = insertNode2.pixels.get(i+insertCount-1) - input.minimumDist;
                        insertNode2.pixels.set(i+insertCount, newCost);
                        insertNode2.cost += Math.abs(newCost - cost);
                        insertNode2.prev = node;
                        checkNode(insertNode2, input);
                        //insertNode2.fastTrack=true;
                        nodes.add(insertNode2);
                        }
                        // insert node
                        
                    insertNode.prev = node;
                    nodes.add(insertNode);
                    }
                
                }
            }
            
            if (valid) {
                Node pn = node;
                while(pn != null) {
                    log.debug("Step {}", pn);
                    pn = pn.prev;
                }
                return ("Case #" + caseNumber + ": " + node.cost);
            }
            
        }
        
        log.info("Done calculating answer case {}", caseNumber);
        
        
        
        return ("Case #" + caseNumber + ": " );
    }
    
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        
        InputData  input = new InputData(testCase);
        
        input.deleteCost = Integer.parseInt(line[0]);
        input.insertCost = Integer.parseInt(line[1]);
        input.minimumDist = Integer.parseInt(line[2]);
        input.num = Integer.parseInt(line[3]);
        
        input.pixels = new ArrayList<Integer>();
        
        line = br.readLine().split(" ");
        for(int i = 0; i < input.num; ++i) {
            input.pixels.add(Integer.parseInt(line[i]));
        }
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           args = new String[] { "sample.txt" };
           args = new String[] { "B-small-practice.in" };
           //args = new String[] { "largeInput.txt" };
        }
        log.info("Input file {}", args[0]);

        Main m = new Main();
        Runner.goSingleThread(args[0], m, m);
        
       
    }

    
}