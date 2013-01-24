package codejam.y2008.round_emea.painting_fence;

import java.util.BitSet;
import java.util.Collection;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    @Override
    public String[] getDefaultInputFiles() {
     //   return new String[] {"sample.in"};
        //return new String[] {};
        return new String[] {"B-small-practice.in", "B-large-practice.in"};
     //   return new String[] {"A-small-practice.in", "A-large-practice.in"};
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData i = new InputData(testCase);
        int N = scanner.nextInt();
        i.A = new Integer[N];
        i.B = new Integer[N];
        i.colors = new String[N];
        for(int ii = 0; ii < N; ++ii) {
            i.colors[ii] = scanner.next();
            i.A[ii] = scanner.nextInt();
            i.B[ii] = scanner.nextInt();
        }
        return i;
    }
   
    private static class Node implements Comparable<Node> {
        int cost;
        BitSet colorsUsed;
        int fencePainted;
        @Override
        public int compareTo(Node o) {
            return ComparisonChain.start().compare(cost,o.cost).compare(fencePainted, o.fencePainted).result();
        }
        public Node(int cost, BitSet colorsUsed, int fencePainted) {
            super();
            this.cost = cost;
            this.colorsUsed = colorsUsed;
            this.fencePainted = fencePainted;
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((colorsUsed == null) ? 0 : colorsUsed.hashCode());
            result = prime * result + cost;
            result = prime * result + fencePainted;
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
            if (colorsUsed == null) {
                if (other.colorsUsed != null)
                    return false;
            } else if (!colorsUsed.equals(other.colorsUsed))
                return false;
            if (cost != other.cost)
                return false;
            if (fencePainted != other.fencePainted)
                return false;
            return true;
        }
    }
    
    private static class Offer {
        final int start;
        final int finish;
        final int color;
        public Offer(int start, int finish, String colorStr, Map<String, Integer> colorNumbers) {
            super();
            this.start = start;
            this.finish = finish;
            
            Integer color = colorNumbers.get(colorStr);
            if (color == null) {
                color = colorNumbers.size();
                colorNumbers.put(colorStr, color);
            }
            this.color = color;
        }
    }
  
    @Override
    public String handleCase(InputData input) {
        
        Map<String, Integer> colorNumbers = Maps.newHashMap();
        Multimap<Integer, Offer> offerMap = HashMultimap.create();
        
        for(int i = 0; i < input.colors.length; ++i) {
            Offer off = new Offer(input.A[i], input.B[i], input.colors[i], colorNumbers);
            
            for(int j = off.start; j <= off.finish; ++j) {
                offerMap.put(j, off);
            }
        }
        
        PriorityQueue<Node> toVisit = new PriorityQueue<>();
        Set<Node> visited = Sets.newHashSet();
        
        toVisit.add(new Node(0,new BitSet(),0));
        
        while(!toVisit.isEmpty()) {
            Node node = toVisit.poll();
            
            if (visited.contains(node)) {
                continue;
            }
            
            if (node.fencePainted >= 10000) {
                return String.format("Case #%d: %d", input.testCase, node.cost);
            }
            
            visited.add(node);
            
            //Need an offer that covers the very next square
            Collection<Offer> offers = offerMap.get(node.fencePainted+1);
            
            for(Offer offer : offers) {
                BitSet bs =  BitSet.valueOf(node.colorsUsed.toLongArray());
                bs.set(offer.color);
                                
                //Limited to 3 colors
                if (bs.cardinality() > 3)
                    continue;
                

                toVisit.add(new Node(node.cost+1,bs, offer.finish));
            }
        }
        
        return String.format("Case #%d: IMPOSSIBLE", input.testCase);
    }

}
