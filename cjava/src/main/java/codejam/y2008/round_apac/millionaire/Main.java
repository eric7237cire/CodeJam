package codejam.y2008.round_apac.millionaire;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleComparator;

import com.google.common.base.Preconditions;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.M = scanner.nextInt();
        input.P = scanner.nextDouble();
        input.X = scanner.nextInt();
        return input;
    }

    static class Node implements Comparable<Node> {
        double prob;
        double lowerBound;
        public Node(double prob, double lowerBound) {
            super();
            this.prob = prob;
            this.lowerBound = lowerBound;
        }
        @Override
        public String toString() {
            return "Node [prob=" + prob + ", lowerBound=" + lowerBound + "]";
        }
        static DoubleComparator dc = new DoubleComparator(0.000000001);
        @Override
        public int compareTo(Node o) {
            return dc.compare(lowerBound, o.lowerBound);
        }
        
        
        
    }
    @Override
    public String handleCase(InputData input) {
        DoubleComparator dc = new DoubleComparator(0.000000001);
        
        TreeMap<Double,Double> nodes = new TreeMap<>(dc);
        nodes.put(1000000D, 1D);
        nodes.put(0D, 0D);
                
        
        for(int m = 0; m < input.M; ++m) {
            TreeMap<Double,Double> newNodes = new TreeMap<>(nodes);
            
            //Combine all nodes
            for(Map.Entry<Double,Double> highNode : nodes.entrySet()) {
                
                SortedMap<Double,Double> lowerNodes = nodes.headMap(highNode.getKey());
                
                for(Map.Entry<Double,Double> lowerNode : lowerNodes.entrySet()) {
                    
                    //Key is the minimum dollar amount
                    Preconditions.checkState(highNode.getKey() > lowerNode.getKey());
                    
                    double mid = (highNode.getKey()+lowerNode.getKey()) / 2;
                    double prob = input.P * highNode.getValue() + (1D-input.P) * lowerNode.getValue();
                    
                    Double existProb = newNodes.get(mid);
                    
                    if (existProb == null || dc.compare(prob, existProb) > 0) {
                        newNodes.put(mid,prob);
                    }
                    
                }
            }
            
            nodes = newNodes;
            
            
            log.debug("List size.  m {} {}", m, nodes.size());
            
        }
        
        double maxP = 0;
        
        for(Map.Entry<Double,Double> n : nodes.entrySet()) {
            
            if (input.X >= n.getKey()) {
                maxP = Math.max(maxP,n.getValue());
            }
        }
        
        DecimalFormat df = new DecimalFormat("0.######");
        df.setRoundingMode(RoundingMode.HALF_UP);
       // df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        

        return("Case #" + input.testCase + ": " + df.format(maxP));

    }

}
