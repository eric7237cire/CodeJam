package codejam.y2010.round_3.hotdog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.datastructures.Gaps;
import codejam.utils.datastructures.Gaps.Gap;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2010.round_3.hotdog.InputData.Corner;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    static CircularFifoBuffer lastElems = new CircularFifoBuffer(3);
    
    @Override
    public String handleCase( InputData input) {
        int caseNumber = input.testCase;
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
    public InputData readInput(Scanner scanner, int testCase) {

        InputData input = new InputData(testCase);

        input.C = scanner.nextInt();

        input.vendors = new ArrayList<>(input.C);

        for (int i = 0; i < input.C; ++i) {
            
            input.vendors.add(new Corner(scanner.nextInt(), scanner.nextInt()));
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
        //int len = max - min + 1;
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

    /**
     * The main idea is that any move is an optimal move.  So we solve in 2 steps.
     * 
     * Exploding all the large corners from the input into 1's
     * and then sweeping up to move any 2's created from the explosion
     * to the edges.
     * 
     * @param vendors
     * @return
     */
    public long takeAllMove(List<Corner> vendors) {

        Multiset<Integer> currentVendors = HashMultiset.create();

        Gaps gaps = new Gaps();

        for (int i = 0; i < vendors.size(); ++i) {
            currentVendors.add(vendors.get(i).location, vendors.get(i).count);
            gaps.mergeGap(vendors.get(i).location, vendors.get(i).location);            
        }
        
        long cost = 0;

        for (int vi = 0; vi < vendors.size(); ++vi) {

            Integer corner = vendors.get(vi).location;

            int count = currentVendors.count(corner);
            if (count <= 1)
                continue;


            int half = count / 2;
            //Found by observation   9 becomes 1111 1 1111 with a cost of the sum of the squares
            long costAdd = (long) half * (half + 1) * (2 * half + 1) / 6;
            cost += costAdd;

            //Add the 1's
            for (int leftSide = corner - half; leftSide < corner; ++leftSide) {
                int beforeCount = currentVendors.count(leftSide);
                beforeCount++;
                currentVendors.setCount(leftSide, beforeCount);
            }
            gaps.mergeGap(corner-half, corner-1);
            for (int rightSide = corner + half; rightSide > corner; --rightSide) {
                int beforeCount = currentVendors.count(rightSide);
                beforeCount++;
                currentVendors.setCount(rightSide, beforeCount);
            }
            gaps.mergeGap(corner+1, corner+half);

            //Center is either 0 or 1
            if (count % 2 == 0) {
                currentVendors.setCount(corner, 0);
                gaps.removeGap(corner,corner);
            } else {
                currentVendors.setCount(corner, 1);
                gaps.mergeGap(corner,corner);
            }
            
            //Now do a sweep, removing any 2's created
            for(int sweep = corner - half; sweep <= corner + half; ++sweep) {
                int sweepCount = currentVendors.count(sweep);
                if (sweepCount <= 1)
                    continue;
                
                //Will be taken into account by the loop as if the count > 2, then it
                //came forcibly from the input vendor set
                if (sweepCount > 2) {
                   // Preconditions.checkState(toProcess.contains(sweep));
                    continue;
                }
                
                Gap gap = gaps.getGap(sweep);
                Preconditions.checkState(gap != null);
                Preconditions.checkState(!currentVendors.contains(gap.lb-1));
                Preconditions.checkState(!currentVendors.contains(gap.ub+1));
                
                /*Say we have      112111
                  it will become 1 110111 1   offset -- 2
                                 1 111011 1
                  */
                
                int offset = sweep - gap.lb;
                
                //offset from left and right get decreased by 1, they may be the same number ie
                //   121
                // 1 101 1
                // 1 101 1
                
                int cornerToDecrease = gap.lb + offset;
                boolean r = currentVendors.remove(cornerToDecrease);
                Preconditions.checkState(r);
                if (!currentVendors.contains(cornerToDecrease)) {
                    gaps.removeGap(cornerToDecrease, cornerToDecrease);
                }
                cornerToDecrease = gap.ub - offset;
                
                r = currentVendors.remove(cornerToDecrease);
                Preconditions.checkState(r);
                if (!currentVendors.contains(cornerToDecrease)) {
                    gaps.removeGap(cornerToDecrease, cornerToDecrease);
                }
                
                //Add 1's to left and right
                Preconditions.checkState(!currentVendors.contains(gap.lb-1));
                Preconditions.checkState(!currentVendors.contains(gap.ub+1));
                
                currentVendors.add(gap.lb-1);
                gaps.mergeGap(gap.lb-1,gap.lb-1);
                currentVendors.add(gap.ub+1);
                gaps.mergeGap(gap.ub+1,gap.ub+1);
                
                
                //Length left side (including sweep) * length right side (including sweep)
                //Found by observation
                long costAdded = (long)(sweep - gap.lb + 1) * (gap.ub - sweep + 1);
                cost += costAdded;
                
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
    
    
}