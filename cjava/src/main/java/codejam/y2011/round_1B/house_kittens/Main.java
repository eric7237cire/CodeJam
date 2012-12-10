package codejam.y2011.round_1B.house_kittens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2009.round_3.football_team.Graph;

import com.google.common.base.Preconditions;
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
            
            vertexSets.remove(setIndex);
            
            
            vertexSets.add(set1);
            vertexSets.add(set2);
            
        }
        
        int minVertexCount = Integer.MAX_VALUE;
        
        
        for(int i = 0; i < vertexSets.size(); ++i) {
            List<Integer> set = vertexSets.get(i);
            minVertexCount = Math.min(minVertexCount, set.size());
        }
        
        for(int colors = minVertexCount; colors >= 0; --colors) {
            
            int[] assignment = new int[input.N];
            
            count = 0;
            boolean chk = backtrack(assignment, 0, vertexSets, colors);
            Preconditions.checkState(isValid(vertexSets,assignment,colors));
            
            if (chk) {
                return "Case #" + input.testCase + ": " + colors + "\n" + Ints.join(" ",assignment);
            } else {
                log.info("Invalid {}", colors);
            }
        }
        
        return null;
        
    }
    
    private int count = 0;
    
    private boolean backtrack(int[] solution, int verticesColored,
            List<List<Integer>> vertexSets, final int colors) {

        ++count;
        if (count % 10 == 0) {
            log.info("Backtrack. count {}  vc {}", count, verticesColored);
        }
        if (!isValidPartial(vertexSets, solution, colors)) {
            //solution[verticesColored+1] = 0;
            return false;
        }
        if (verticesColored == solution.length && isValid(vertexSets,solution,colors)) {
            return true;
        }
        for (int i = 1; i <= colors; ++i) {
            solution[verticesColored] = i;
            boolean r = backtrack(solution, verticesColored+1, vertexSets, colors);
            if (r) {
                return true;
            }
        }
        solution[verticesColored] = 0;
        
        return false;
    }
        
    int[] canAssign(List<List<Integer>> vertexSets, final int colors, int N) {
        int[] assignment = new int[N];
        Arrays.fill(assignment, 0);
        /*
        Collections.sort(vertexSets, new Comparator<List<Integer>>() {

            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                return Integer.compare(o2.size(), o1.size());
            }
            
        });
        */
        
        for(List<Integer> set : vertexSets) {
            
            BitSet usedColors = new BitSet(colors); 
                        
            //Find unassigned colors
            for(Integer vertex : set) {
                int color = assignment[vertex-1];
                if (color > 0) {
                    usedColors.set(color-1);                    
                }
            }
            
            for(int i = 0; i < set.size(); ++i) {
                int vertex = set.get(i);
                int prevVertex = set.get( i == 0 ? set.size() - 1 : i - 1);
                int nextVertex = set.get( i == set.size() - 1 ? 0 : i + 1);
                
                int color = assignment[vertex-1];
                int prevColor = assignment[prevVertex - 1];
                int nextColor = assignment[nextVertex - 1];
                
                //Color already assigned
                if (color != 0) {
                    continue;
                }
                
                //Use an unused color if it exists
                int unusedColor = usedColors.nextClearBit(0);
                
                if (unusedColor < colors) {
                    // usedColors |= unusedColorBit;
                    color = unusedColor + 1;
                    Preconditions.checkState(1 <= color && color <= colors);
                    assignment[vertex - 1] = color;
                    
                    usedColors.set(unusedColor);
                } else {
                    //Use a color that is not the next or previous color
                    for(int tryColor = 1; tryColor <= colors; ++tryColor) {
                        if (prevColor != tryColor && nextColor != tryColor) {
                            assignment[vertex-1]=tryColor;
                            break;
                        }
                    }
                }
                
            }
            
            Preconditions.checkState(isValidPartial(vertexSets,assignment,colors));
        }
        
       return assignment;
        
    }
    
    boolean isValidPartial(List<List<Integer>> vertexSets, int[] assignment, int colors) {
        BitSet hasAllColors = new BitSet(colors);
        hasAllColors.set(0, colors, true);
        
        for(List<Integer> set : vertexSets) {
            BitSet colorCheck = new BitSet(colors);
            int blanks = 0;
            int usedColors = 0;
            for(Integer v : set) {
                int color = assignment[v-1];
                if (color == 0)
                    blanks++;
                else if (!colorCheck.get(color-1)) {
                    colorCheck.set(color-1);
                    usedColors++;
                }
            }
            
            if (usedColors + blanks < colors) 
                return false;
        }
        
        return true;
    }
    
    boolean isValid(List<List<Integer>> vertexSets, int[] assignment, int colors) {
        BitSet hasAllColors = new BitSet(colors);
        hasAllColors.set(0, colors, true);
        
        for(List<Integer> set : vertexSets) {
            BitSet colorCheck = new BitSet(colors);
            for(Integer v : set) {
                int color = assignment[v-1];
                colorCheck.set(color-1);
            }
            if (!colorCheck.equals(hasAllColors)) 
                return false;
        }
        
        return true;
    }
    
    int[] canAssign2(List<List<Integer>> vertexSets, int colors, int N) {
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
    

}
