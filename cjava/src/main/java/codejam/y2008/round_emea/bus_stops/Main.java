package codejam.y2008.round_emea.bus_stops;

import java.util.List;
import java.util.Scanner;

import com.google.common.primitives.Ints;

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
   
    public static int sumOfProductAllPermutations(Integer[] numbers, int permLength, int mod) {
        int[] lastRound = new int[numbers.length];
        for(int n = 0; n < numbers.length; ++n) {
            lastRound[n] = numbers[n];
        }
        
        long lastSum = 1;
        
        for(int i = 0; i < permLength; ++i) {
            long sum = 0;
            for(int n = 0; n < numbers.length; ++n) {
                sum += numbers[n] * lastSum;
                sum %= mod;
            }
            lastSum = sum;            
        }
        
        return Ints.checkedCast(lastSum);
    }
    
    public static int sumOfProductAllPermutationsBruteForce(Integer[] numbersArray, int permLength, int mod) {
        Integer[] perm = new Integer[permLength];
        
        PermutationWithRepetition<Integer> pr = PermutationWithRepetition.create(numbersArray, perm);
        
        long sum = 0;
        while(pr.hasNext())
         {
            pr.next();
            long product = 1;
            for(int i = 0; i < perm.length; ++i) {
                product *= perm[i];
                product %= mod;
            }
            sum+=product;
            sum%=mod;
        } 
        
        return Ints.checkedCast(sum);
    }
    
    @Override
    public String handleCase(InputData input) {
        
        
        
        return String.format("Case #%d: IMPOSSIBLE", input.testCase);
        //        return String.format("Case #%d: %s %s", input.testCase, df.format(p.getX()), df.format(p.getY()));
    }

}
