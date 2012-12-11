package codejam.y2010.round_2.world_cup;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(InputData input) {

        int caseNumber = input.testCase;
        log.info("Starting calculating case {}", caseNumber);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);
        int minCost = minCost(input.P, 0, 0, input);

        log.info("Done calculating answer case {}", caseNumber);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + minCost);
    }
    
    static int MAX_PRICE = 100000 * 2048;
    
    /**
     * @param roundNum 1 based round number with matches = 2 ^ (P - roundNum)
     * @param matchNum which match  0 to 2 ^ (P - roundNum) - 1
     * @param matchesMissed how many not purchased above in the tree
     */
    static int minCost(int roundNum, int matchNum, int matchesMissed, final InputData input) {
        //Next match numbers = matchNum * 2 and matchNum * 2 + 1
        
        //Base case
        if (roundNum == 0) {
            //here the matchNum is really the team number
            if (matchesMissed > input.M[matchNum] ) {
                return MAX_PRICE;
            }
            return 0;
        }
        
        int matchNumLeft = matchNum * 2;
        int matchNumRight = matchNum * 2 + 1;
        
        int ifBuyPrice = input.costs.get(roundNum-1).get(matchNum)
                + minCost(roundNum-1, matchNumLeft, matchesMissed, input)
                + minCost(roundNum-1, matchNumRight, matchesMissed, input);
        
        int ifNotBuyPrice =  minCost(roundNum-1, matchNumLeft, matchesMissed+1, input)
                + minCost(roundNum-1, matchNumRight, matchesMissed+1, input);
        
        int minPrice = Math.min(ifBuyPrice, ifNotBuyPrice);
        
        if (minPrice > MAX_PRICE) {
            minPrice = MAX_PRICE;
        }
        
        return minPrice;
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
    
        
        
        InputData  input = new InputData(testCase);
        
        input.P = scanner.nextInt();
        
        
        input.M = new int[1 << input.P];
        for(int i = 0; i < input.M.length; ++i) {
            input.M[i] = scanner.nextInt();
        }
        
        input.costs = new ArrayList<>();
        for(int round = 1; round <= input.P; ++round) {
            int matches = 1 << input.P - round;
            List<Integer> matchCosts = new ArrayList<>();
            
            for(int i = 0; i < matches; ++i) {
                matchCosts.add(scanner.nextInt());
            }
            input.costs.add(matchCosts);
        }
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
           // args = new String[] { "B-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}