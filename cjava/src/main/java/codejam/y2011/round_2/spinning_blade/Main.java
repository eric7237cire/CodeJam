package codejam.y2011.round_2.spinning_blade;

import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Grid;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
         return new String[] {"sample.in"};
      //  return new String[] { "A-small-practice.in", "A-large-practice.in" };
    }

    @Override
    public InputData readInput(Scanner scanner, int testCase) {



        /*
         * The first line of the input gives the number of test cases, T. 
         * T test cases follow. Each one starts with a line containing 
         * 3 integers: R, C and D — the dimensions of the grid and the 
         * mass you expected each cell to have.
         *  The next R lines each contain C digits wij each,
         *   giving the differences between the actual and 
         *   the expected mass of the grid cells. Each cell
         *    has a uniform density, but could have an integer
         *     mass between D + 0 and D + 9, inclusive.
         */
        InputData in = new InputData(testCase);
        in.R = scanner.nextInt();        
        in.C = scanner.nextInt();
        in.D = scanner.nextInt();
        
        Pattern delim = scanner.delimiter();
        scanner.useDelimiter("");
        
        in.cells = Grid.buildFromScanner(scanner, in.R, in.C,
                new Grid.FromScanner<Integer>() {
                    @Override
                    public Integer getFromScanner(Scanner scanner) {
                        String s = null;
                        while(StringUtils.trimToNull(s) == null) {
                            s = scanner.next();
                        }
                        return Integer.parseInt(s);
                    }

                }, -1);
        
        scanner.useDelimiter(delim);

        return in;
    }

    public String handleCase(InputData in) {

        
    
        return String.format("Case #%d: %s", in.testCase);
    }

}
