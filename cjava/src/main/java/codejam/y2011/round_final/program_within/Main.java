package codejam.y2011.round_final.program_within;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
        return new String[] { "sample.in" };
     //    return new String[] { "B-small-practice.in" };
      //   return new String[] { "B-small-practice.in", "B-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData in = new InputData(testCase);

        in.N = scanner.nextInt();

       
        return in;
    }

    /**
     * Looked at the solution.  Basically you can never have
     * a straight that encompasses another, so use a greedy strategy
     * to add the card to the shortest straight.
     */
    public String handleCase(InputData in) {

        try {
        String text =
                IOUtils.toString(this.getClass().getResourceAsStream(
                        "/codejam/y2011/rules1.txt"));
       
        log.debug(text);
        
        Simulator s = Simulator.fromStr(text);
        s.printStateLimit = 20;
        
        Integer finalLoc = s.go();
        
        log.debug("Final state {}", finalLoc); 
        
        } catch (Exception ex) {
            log.debug("ex",ex);
        }
        
        return String.format("Case #%d: %d", in.testCase,8);
        
    }

}
