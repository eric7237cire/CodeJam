package codejam.y2011.round_1B.house_kittens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.N = scanner.nextInt();
        input.M = scanner.nextInt();
        input.interiorWalls = new ArrayList<>(input.M);
        for(int i = 0; i < input.M; ++i) {
            input.interiorWalls.add(new MutablePair<>(scanner.nextInt(), 0));        
        }
        for(int i = 0; i < input.M; ++i) {
            ((MutablePair<Integer,Integer>) input.interiorWalls.get(i)).setRight(scanner.nextInt());
        }
        return input;
    }
    
    
    @Override
    public String handleCase(InputData input) {
        
        List<List<Integer>> vertexSets = new ArrayList<>();
        
        //generate first set
        List<Integer> allVertices = new ArrayList<>();
        for(int n = 1; n <= input.N; ++n) {
            allVertices.add(n);
        }
        vertexSets.add(allVertices);
        
        for(Pair<Integer,Integer> pair : input.interiorWalls) {
            //find the set that contains both vertexes
            int setIndex = 0;
            List<Integer> set = vertexSets.get(setIndex);
            
            while(!set.contains(pair.getLeft()) || !set.contains(pair.getRight())) {
                ++setIndex;
                set = vertexSets.get(setIndex);
            }
            
            vertexSets.remove(setIndex);
            //Now divide the set into 2
            
            List<Integer> set1  = new ArrayList<>();
            List<Integer> set2 = new ArrayList<>();
            //go from u to v
            for(int n = pair.getLeft(); n <= pair.getRight(); ++n) {
                if (set.contains(n))
                    set1.add(n);
            }
            
            //go from v to u
            for(int i = pair.getRight(); i <= pair.getLeft() + input.N; ++i) {
                int n = i > input.N ? i - input.N : i;
                if (set.contains(n))
                    set2.add(n);
            }
            
            vertexSets.add(set1);
            vertexSets.add(set2);
        }
        
        int minVertexCount = Integer.MAX_VALUE;
        
        for(List<Integer> set : vertexSets) {
            minVertexCount = Math.min(minVertexCount, set.size());
        }
        
        for(int colors = minVertexCount; colors >= 0; --colors) {
            
            int[] assignment = canAssign(vertexSets, colors, input.N);
            
            if (assignment != null) {
                return "Case #" + input.testCase + ": " + colors + "\n" + Ints.join(" ",assignment);
            } else {
                log.info("Invalid {}", colors);
            }
        }
        
        return null;
        
    }
    
    int[] canAssign(List<List<Integer>> vertexSets, int colors, int N) {
        int[] assignment = new int[N];
        Arrays.fill(assignment, 0);
        
        Collections.sort(vertexSets, new Comparator<List<Integer>>() {

            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                return Integer.compare(o2.size(), o1.size());
            }
            
        });
        
        
        for(List<Integer> set : vertexSets) {
            
            int usedColors = 0; //(1 << colors) - 1;
            int colorsLeftToUse = colors;
            
            //Find unassigned colors
            for(Integer vertex : set) {
                int color = assignment[vertex-1];
                if (color > 0) {
                    usedColors |= 1 << (color-1);                    
                }
            }
            
            for(int i = 0; i < set.size(); ++i) {
                int vertex = set.get(i);
                int prevVertex = set.get( i == 0 ? set.size() - 1 : i - 1);
                int nextVertex = set.get( i == set.size() - 1 ? 0 : i + 1);
                
                int color = assignment[vertex-1];
                int prevColor = assignment[prevVertex - 1];
                int nextColor = assignment[nextVertex - 1];
                
                if (color == 0) {
                    //Assign the rest
                    int unusedColorBit = ~usedColors & (usedColors+1);
                    usedColors |= unusedColorBit;
                    color = Integer.numberOfTrailingZeros(unusedColorBit) + 1;
                    Preconditions.checkState(1 <= color && color <= colors);
                    assignment[vertex-1] = color;
                }
            }
        }
        
        boolean incremented = true;
        
        while(incremented) {
            incremented=false;
            
            for(int i = 0; i < N; ++i) {
                assignment[i]++;
                if (assignment[i] > colors) {
                    assignment[i] = 1;
                } else {
                    incremented = true;
                    break;
                }
            }
            
            if (!incremented) {
                break;
            }
            
            if (isValidList(vertexSets, assignment, colors)) {
                return assignment;
            }
        }
        
        return null;
        
    }
    
    boolean isValidList(List<List<Integer>> vertexSets, int[] assignment, int colors) {
        int hasAllColors = (1 << colors) - 1;
        for(List<Integer> set : vertexSets) {
            int colorCheck = 0;
            for(Integer v : set) {
                int color = assignment[v-1];
                colorCheck |= 1 << (color-1);
            }
            if (colorCheck != hasAllColors) 
                return false;
        }
        
        return true;
    }
    
    int[] canAssign2(List<Set<Integer>> vertexSets, int colors, int N) {
        int[] assignment = new int[N];
        Arrays.fill(assignment, 1);
        
        boolean incremented = true;
        
        while(incremented) {
            incremented=false;
            
            for(int i = 0; i < N; ++i) {
                assignment[i]++;
                if (assignment[i] > colors) {
                    assignment[i] = 1;
                } else {
                    incremented = true;
                    break;
                }
            }
            
            if (!incremented) {
                break;
            }
            
            if (isValid(vertexSets, assignment, colors)) {
                return assignment;
            }
        }
        
        return null;
        
    }
    
    boolean isValid(List<Set<Integer>> vertexSets, int[] assignment, int colors) {
        int hasAllColors = (1 << colors) - 1;
        for(Set<Integer> set : vertexSets) {
            int colorCheck = 0;
            for(Integer v : set) {
                int color = assignment[v-1];
                colorCheck |= 1 << (color-1);
            }
            if (colorCheck != hasAllColors) 
                return false;
        }
        
        return true;
    }

}
