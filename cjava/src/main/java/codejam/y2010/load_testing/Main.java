package codejam.y2010.load_testing;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.math.IntMath;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(InputData input) {

        int caseNumber = input.testCase;
        log.info("Starting calculating case {}", caseNumber);
        
        int numSteps = 0;
        long step = (long)input.L * input.C;
        while(step < (long)input.P) {
            step *= input.C;
            ++numSteps;
            //log.debug("num steps {} case {} step {}", numSteps, caseNumber, step);
        }
        
        int testsNeeded = 0;
        
        if (numSteps > 0) {
            testsNeeded = 1;
            while(IntMath.checkedPow(2, testsNeeded) - 1 < numSteps) {
                ++testsNeeded;
                log.debug("Tests needed {} for case {}", testsNeeded, caseNumber);
            }
        }

        log.info("Done calculating answer case {}", caseNumber);
        
        
        return ("Case #" + caseNumber + ": " + testsNeeded);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
    
        InputData  input = new InputData(testCase);
        
        input.L = scanner.nextInt();
        input.P = scanner.nextInt();
        input.C = scanner.nextInt();
        
        return input;
        
    }

    
}