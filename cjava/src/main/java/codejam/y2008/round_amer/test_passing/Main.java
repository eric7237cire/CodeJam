package codejam.y2008.round_amer.test_passing;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.MinMaxPriorityQueue;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
      //  return new String[] { "sample.in"};
        //return new String[] { "C-small-practice.in" };
        return new String[] { "C-large-practice.in" };
        //return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.M = scanner.nextInt();
        input.Q = scanner.nextInt();
        
        input.prob = new double[input.Q][4];
        for(int i = 0; i < input.Q; ++i) {
            input.prob[i] = new double[] {scanner.nextDouble(),scanner.nextDouble(),scanner.nextDouble(),scanner.nextDouble() };
        }
        return input;
    }

    
    private static class Node {
        List<Path> bestPaths;       
        
        Node(double prob, int ans, int maxQ) {
            Path p = new Path();
            p.pathList[0] = ans;
            p.weight = prob;
            bestPaths = new ArrayList<>();
            bestPaths.add(p);
        }
        
        Node(List<Path> paths) {
            bestPaths = paths;
        }

        @Override
        public String toString() {
            return "Node [bestPaths=" + bestPaths + "]";
        }
    }
    
    private static class Path implements Comparable<Path> {
        //pair is question [0-29], response[0-3]
        int[] pathList;
        double weight;
        
        Path() {
            pathList = new int[1];
        }
        
        Path(int[] pathList) {
            this.pathList = pathList;
        }

        @Override
        public int compareTo(Path o) {
            return Double.compare(o.weight, weight);
        }

        @Override
        public String toString() {
            return "Path [pathList=" + Arrays.toString(pathList) + ", weight="
                    + weight + "]";
        }
    }
    
    @Override
    public String handleCase(InputData input) {
        
       // int[] currentQ = new int[input.Q];
        
        Node[][] bestPaths = new Node[input.Q][4];
        
        for(int a = 0; a < 4; ++a) {
            bestPaths[0][a] = new Node(input.prob[0][a], a, input.Q);
        }
        
        for (int q = 1; q < input.Q; ++q) {
            for (int ans = 0; ans < 4; ++ans) {
                //Build a list of best paths
                //List<Path> paths = new ArrayList<>();
                MinMaxPriorityQueue<Path> mm = MinMaxPriorityQueue.maximumSize(input.M).expectedSize(input.M).create();
                
                for (int prevA = 0; prevA < 4; ++prevA) {
                    Node node = bestPaths[q-1][prevA];
                    for(Path prevPath : node.bestPaths) {
                        Path newPath = new Path(Arrays.copyOf(prevPath.pathList, q+1));
                        newPath.pathList[q] = ans;
                        newPath.weight = prevPath.weight * input.prob[q][ans];
                        if (mm.size() == input.M  && newPath.weight < mm.peekLast().weight) {
                            break;
                        }
                        mm.add(newPath);
                        //paths.add(newPath);
                    }
                }
                
                List<Path> paths = new ArrayList<>(mm);
                
                Collections.sort(paths);                
                
                bestPaths[q][ans] = new Node(paths);
            }
        }
        
        //Combine last questions paths
        //Build a list of best paths
        MinMaxPriorityQueue<Path> paths = MinMaxPriorityQueue.maximumSize(input.M).expectedSize(input.M).create();
        
        for (int ans = 0; ans < 4; ++ans) {
            paths.addAll(bestPaths[input.Q-1][ans].bestPaths);            
        }
        
        double ev = 0;
        for(Path path : paths) {            
            ev+=path.weight;
        }
        
        /*
        int perms = IntMath.pow(4,input.Q);
        List<Double> evList = new ArrayList<>();
        
        for(int i = 0; i < perms; ++i) {
            double p = 1;
            
            for(int q = 0; q < input.Q; ++q) {
                p *= input.prob[q][currentQ[q]];
            }
            evList.add(p);
            
            //Increment
            for(int q = 0; q < input.Q; ++q) {
                currentQ[q]++;
                if (currentQ[q] > 3) {
                    currentQ[q] = 0;
                } else {
                    break;
                }
            }
        }
        
        Collections.sort(evList, Ordering.natural().reverse());
        
        double ev = 0;
        for(int m = 0; m < input.M; ++m) {
            
            ev += evList.get(m);
            
            if (ev + 0.0000001d >= 1)
                break;
            
            
        }*/
        
        /*
       Arrays.sort(input.prob, new Comparator<double[]>() {

        @Override
        public int compare(double[] o1, double[] o2) {
            ComparisonChain cc = ComparisonChain.start();
            for(int i = 0; i < 4; ++i) {
                cc = cc.compare(o1[i], o2[i]);
                if (cc.result() != 0)
                    return cc.result();
            }
            
            return cc.result();
        }
           
       });*/
        
        
        DecimalFormat df = new DecimalFormat("0.######");
        df.setRoundingMode(RoundingMode.HALF_UP);
        
        return String.format("Case #%d: %s", input.testCase, df.format(ev));
    }
    

}
