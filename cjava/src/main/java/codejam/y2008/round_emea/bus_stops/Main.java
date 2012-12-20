package codejam.y2008.round_emea.bus_stops;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.ArrayUtils;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.PermutationWithRepetition;

public class Main implements TestCaseHandler<InputData>,
        TestCaseInputScanner<InputData>, DefaultInputFiles {

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] {"sample.in"};
        //return new String[] {"B-small-practice.in"};
        //return new String[] {"B-large-practice.in"};
     //   return new String[] {"A-small-practice.in", "A-large-practice.in"};
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData i = new InputData(testCase);
        
        i.N = scanner.nextInt();
        i.K = scanner.nextInt();
        i.P = scanner.nextInt();
        return i;
    }
   
    public static int sumOfProductAllPermutations(List<Integer> numbers, int permLength) {
        int[] lastRound = new int[numbers.size()];
        for(int n = 0; n < numbers.size(); ++n) {
            lastRound[n] = numbers.get(n);
        }
        
        int lastSum = 1;
        
        for(int i = 0; i < permLength; ++i) {
            int sum = 0;
            for(int n = 0; n < numbers.size(); ++n) {
                sum += numbers.get(n) * lastSum;
            }
            lastSum = sum;            
        }
        
        return lastSum;
    }
    
    public static int sumOfProductAllPermutationsBruteForce(List<Integer> numbers, int permLength) {
        Integer[] numbersArray =  numbers.toArray(new Integer[] {}) ;
        
        Integer[] perm = new Integer[permLength];
        
        PermutationWithRepetition pr = PermutationWithRepetition.create(numbersArray, perm);
        
        int sum = 0;
        do {
            int product = 1;
            for(int i = 0; i < perm.length; ++i) {
                product *= perm[i];
            }
            sum+=product;
            
            pr.next();
            
        } while(pr.hasNext());
        
        return sum;
    }
    
    @Override
    public String handleCase(InputData input) {
        
        
        
        return String.format("Case #%d: IMPOSSIBLE", input.testCase);
        //        return String.format("Case #%d: %s %s", input.testCase, df.format(p.getX()), df.format(p.getY()));
    }

}
