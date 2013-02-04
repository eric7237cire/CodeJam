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
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.MinMaxPriorityQueue;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main() {
        super("C", 1, 1);
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
    public String handleCase(InputData in) {
        
        Solution s = new Solution();
        if(1==1) {
            return s.handleCase(in);
        }
            
       // int[] currentQ = new int[input.Q];
        
        Node[][] bestPaths = new Node[in.Q][4];
        
        /**
         * Initialize first 4 nodes
         */
        for(int a = 0; a < 4; ++a) {
            bestPaths[0][a] = new Node(in.prob[0][a], a, in.Q);
        }
        
        for (int q = 1; q < in.Q; ++q) {
            for (int ans = 0; ans < 4; ++ans) {
                //Build a list of best paths
                //List<Path> paths = new ArrayList<>();
                MinMaxPriorityQueue<Path> mm = MinMaxPriorityQueue.maximumSize(in.M).expectedSize(in.M).create();
                
                for (int prevA = 0; prevA < 4; ++prevA) {
                    Node node = bestPaths[q-1][prevA];
                    for(Path prevPath : node.bestPaths) {
                        Path newPath = new Path(Arrays.copyOf(prevPath.pathList, q+1));
                        newPath.pathList[q] = ans;
                        newPath.weight = prevPath.weight * in.prob[q][ans];
                        if (mm.size() == in.M  && newPath.weight < mm.peekLast().weight) {
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
        MinMaxPriorityQueue<Path> paths = MinMaxPriorityQueue.maximumSize(in.M).expectedSize(in.M).create();
        
        for (int ans = 0; ans < 4; ++ans) {
            paths.addAll(bestPaths[in.Q-1][ans].bestPaths);            
        }
        
        double ev = 0;
        for(Path path : paths) {            
            ev+=path.weight;
        }
        
        
        
        
        DecimalFormat df = new DecimalFormat("0.######");
        df.setRoundingMode(RoundingMode.HALF_UP);
        
        return String.format("Case #%d: %s", in.testCase, df.format(ev));
    }
    

}
