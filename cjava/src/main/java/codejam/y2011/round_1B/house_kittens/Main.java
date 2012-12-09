package codejam.y2011.round_1B.house_kittens;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.DoubleComparator;

import com.google.common.base.Joiner;
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
        
        List<Set<Integer>> vertexSets = new ArrayList<>();
        
        //generate first set
        Set<Integer> allVertices = Sets.newHashSet();
        for(int n = 1; n <= input.N; ++n) {
            allVertices.add(n);
        }
        vertexSets.add(allVertices);
        
        for(Pair<Integer,Integer> pair : input.interiorWalls) {
            //find the set that contains both vertexes
            int setIndex = 0;
            Set<Integer> set = vertexSets.get(setIndex);
            
            while(!set.contains(pair.getLeft()) || !set.contains(pair.getRight())) {
                ++setIndex;
                set = vertexSets.get(setIndex);
            }
            
            vertexSets.remove(setIndex);
            //Now divide the set into 2
            
            Set<Integer> set1  = Sets.newHashSet();
            Set<Integer> set2 = Sets.newHashSet();
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
        
        for(Set<Integer> set : vertexSets) {
            minVertexCount = Math.min(minVertexCount, set.size());
        }
        
        for(int colors = minVertexCount; colors >= 0; --colors) {
            
            int[] assignment = canAssign(vertexSets, colors, input.N);
            
            if (assignment != null) {
                return "Case #" + input.testCase + ": " + colors + "\n" + Ints.join(" ",assignment);
            }
        }
        
        return null;
        
    }
    
    int[] canAssign(List<Set<Integer>> vertexSets, int colors, int N) {
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
