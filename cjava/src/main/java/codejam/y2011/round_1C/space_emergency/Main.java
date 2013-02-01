package codejam.y2011.round_1C.space_emergency;

import java.util.Arrays;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

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
     
        int[] timeBetStars = new int[input.N];
        for(int d = 0; d < timeBetStars.length; ++d) {
            /**
             * a_i is the distance between
             * star k*C+i and k*C+i+1, so
             * we calculate i
             */
            int i = d % input.C;
            
            //Set the time it takes
            timeBetStars[d] = 2 * input.a[i];
        }
        
        long timeRemainingUntilBoostersComplete = input.t;
        int current = 0;
        /**
         * After this loop, we travel the time it takes to
         * finish the boosters.  Any distance travelled
         * during that time is subtracted.
         */
        while(timeRemainingUntilBoostersComplete > 0 && current < timeBetStars.length) {
            if (timeBetStars[current] >= timeRemainingUntilBoostersComplete) {
                timeBetStars[current] -= timeRemainingUntilBoostersComplete;
                timeRemainingUntilBoostersComplete = 0;
                break;
            } else {
                timeRemainingUntilBoostersComplete -= timeBetStars[current];
                
                timeBetStars[current] = 0;
                current++; 
            }
        }
      
        /**
         * Here, we take the greatest distances and divide
         * the time in half as it is boosted.
         */        
        long time = -1;
        
        if (timeRemainingUntilBoostersComplete == 0) {
            Arrays.sort(timeBetStars);

            for (int boosted = 0; boosted < input.L; ++boosted)
            {
                int d = timeBetStars.length - 1 - boosted;
                timeBetStars[d] /= 2;
            }

            time = input.t;
            for (int d = 0; d < timeBetStars.length; ++d)
            {
                time += timeBetStars[d];
            }
        } else
        {
            /**
             * Boosters finished too late to do any good
             */
            time = input.t - timeRemainingUntilBoostersComplete;            
        }
        
        return String.format("Case #%d: %d", input.testCase, time);
        
    }

}
