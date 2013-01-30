package codejam.y2011.round_2.expensive_dinner;

import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Prime;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
      //   return new String[] {"sample.in"};
        return new String[] { "C-small-practice.in", "C-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {



        InputData in = new InputData(testCase);
        in.N = scanner.nextLong();
        return in;
    }


    static TreeSet<Long> primePows;
    
    static void buildPrimePows() {
        List<Integer> primes = Prime.generatePrimes(1000000);
        
        long max = 1000000000000L;
        
        primePows = new TreeSet<>();
        
        for(Integer prime : primes) {
            
            long primeToPower = (long) prime * prime;
            
            while(primeToPower <= max ) {
                Preconditions.checkState(!primePows.contains(primeToPower));
                primePows.add(primeToPower);
                
                primeToPower *= prime;
            }
            
        }
        
    }
    

    public String handleCase(InputData in) {

        if (primePows == null) {
            buildPrimePows();
        }
        
        /**
         * The maximum # is going to be the prime factorization, ie
         * 
         * 2 * 2 * 2 * 3 * 3 * 5 * 5 * 5 * 5 etc
         * 
         * To minimize this #, we only count each unique factor only once, ie
         * 
         * 2^a * 3^b * 5^c
         * 
         * So the difference in spead is how many prime factors = p^i with i >= 2
         */
        
        int spread = 0;
        if (in.N == 1) {
            spread = 0;
        } else {
            spread = 1 + primePows.headSet(in.N, true).size();
        }
    
        return String.format("Case #%d: %d", in.testCase, spread);
        
    }

}
