package codejam.y2010.round_1C.rope_intranet;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(InputData input) {

        int caseNumber = input.testCase;
        log.info("Starting calculating case {}", caseNumber);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);
        
        int intersect = 0;
        
        for(int i = 0; i < input.numberRopes; ++i) {
            for(int j = i+1; j < input.numberRopes; ++j) {
                if (input.ropeLeft[i] < input.ropeLeft[j] && input.ropeRight[i] < input.ropeRight[j]) {
                    
                } else 
                    if (input.ropeLeft[i] > input.ropeLeft[j] && input.ropeRight[i] > input.ropeRight[j]) { 
                }
                    else {
                        intersect++;
                    }
            }
        }

        log.info("Done calculating answer case {}", caseNumber);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + intersect);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {

        InputData input = new InputData(testCase);

        input.numberRopes = scanner.nextInt();
        input.ropeLeft = new int[input.numberRopes];
        input.ropeRight = new int[input.numberRopes];

        for (int i = 0; i < input.numberRopes; ++i) {

            input.ropeLeft[i] = scanner.nextInt();
            input.ropeRight[i] = scanner.nextInt();
        }

        return input;

    }

    
    
}