package codejam.y2011.round_final.ace_in_hole;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Primitives;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        //return new String[] { "sample.in" };
         return new String[] { "D-small-practice.in" };
      //   return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        in.N = scanner.nextInt();

        in.cardsExamined = Lists.newArrayList();

        for (int i = 0; i < in.N; ++i) {

            in.cardsExamined.add(scanner.nextInt());
        }
        return in;
    }

    
    public String handleCase(InputData in) {

        List<Integer> unexaminedPositions = Lists.newArrayList();
        List<Integer> unexaminedCards = Lists.newArrayList();
        
        for(int i = 0; i < in.N; ++i) {
            unexaminedCards.add(i+1);
            unexaminedPositions.add(i+1);
        }
        
        //cards remaining
        int m = in.N;
        
        int[] assignments = new int[in.N];
        Arrays.fill(assignments, -1);
        
        for(int posExaminedIdx = 0; posExaminedIdx < in.N; ++posExaminedIdx) {
            
            m = unexaminedCards.size();
            Preconditions.checkState(unexaminedCards.size() == unexaminedPositions.size());
            
            int positionExaminedInput = in.cardsExamined.get(posExaminedIdx);
            
            int pIdx = unexaminedPositions.indexOf(positionExaminedInput);
            
            if (pIdx+1 < m) {
                //Get value k+1
                assignments[positionExaminedInput-1]
                        = unexaminedCards.get(pIdx+1); //+1 - 1
                unexaminedCards.remove(pIdx+1);
                unexaminedPositions.remove(pIdx);
                continue;
            }
            
            Preconditions.checkState(pIdx+1 == m);
            
            if (m <= 2) {
                assignments[positionExaminedInput-1] = unexaminedCards.get(m-1);
                
                unexaminedCards.remove(m-1);
                unexaminedPositions.remove(m-1);
                
                continue;
            }
            
            boolean foundPrevCard = false;
            
            int vMPrev = unexaminedCards.get(m-2);
            int vM = unexaminedCards.get(m-1);
            
            for(int prevPos = 0; prevPos < positionExaminedInput-1; ++prevPos) {
                if (assignments[prevPos] >= 0 && isBetween(assignments[prevPos], vMPrev, vM)) 
                {
                    foundPrevCard = true;
                    break;
                }
            }
            
            if (foundPrevCard) {
                //Assign vM
                assignments[positionExaminedInput-1] = vM;
                
                unexaminedCards.remove(m-1);
                unexaminedPositions.remove(m-1);
                
                continue;
            }
            
            //Assign vM-1
            assignments[positionExaminedInput-1] = vMPrev;
            
            unexaminedCards.remove(m-2);
            unexaminedPositions.remove(m-1);
            
        }
        
        String ans = Ints.join(" ", assignments);
        return String.format("Case #%d: ", in.testCase) + ans;
        
    }
    
    boolean isBetween(int n, int a, int b) {
        int min = Math.min(a,b);
        int max = Math.max(a,b);
        return (a < n && n < b);
    }

}
