package codejam.y2011.round_1C.space_emergency;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles{

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String[] getDefaultInputFiles() {
    //   return new String[] {"sample.in"};
        return new String[] {"B-small-practice.in", "B-large-practice.in"};
    }
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        /*
         *  Each contains integers, L, t, N and C, followed by C integers ai,
         *   all separated by spaces. ai is the number of parsecs between star
         *    k*C+i and star k*C+i+1, for all integer values of k.
         */
        
        InputData in = new InputData(testCase);
        in.L = scanner.nextInt();
        in.t = scanner.nextLong();
        in.N = scanner.nextInt();
        in.C = scanner.nextInt();
        in.a = new int[in.C];
        for(int i = 0; i < in.C; ++i) {
            in.a[i] = scanner.nextInt();
        }
        
        return in;
    }
    
    
    @Override
    public String handleCase(InputData input) {
     
        int[] distances = new int[input.N];
        for(int d = 0; d < distances.length; ++d) {
            int a = d % input.C;
            
            //Set the time it takes
            distances[d] = 2 * input.a[a];
        }
        
        long distanceBeforeBoostersComplete = input.t;
        int current = 0;
        while(distanceBeforeBoostersComplete > 0 && current < distances.length) {
            if (distances[current] >= distanceBeforeBoostersComplete) {
                distances[current] -= distanceBeforeBoostersComplete;
                distanceBeforeBoostersComplete = 0;
                break;
            } else {
                distanceBeforeBoostersComplete -= distances[current];
                
                distances[current] = 0;
                current++; 
            }
        }
      
        
        Arrays.sort(distances);
                        
        for(int boosted = 0; boosted < input.L; ++boosted) {
            int d = distances.length - 1 - boosted;
            distances[d] /= 2;
        }
        
        long time = input.t - distanceBeforeBoostersComplete;
        for(int d = 0; d < distances.length; ++d) {
            time += distances[d];
        }
        
        return String.format("Case #%d: %d", input.testCase, time);
        
    }

}
