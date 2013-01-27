package codejam.y2009.round_final.lights;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.fraction.BigFraction;
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
     //   return new String[] {"A-small-practice.in"};
        return new String[] {"A-small-practice.in", "A-large-practice.in"};
    }
    
    public static class Tournament {
        int rounds;
        int[] roundDays;
    }
    

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData input = new InputData(testCase);
       
        return input;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData in) {
       

        return String.format("Case #%d: ", in.testCase);
    }
}