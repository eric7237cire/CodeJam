package codejam.y2010.round_2.world_cup;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main() {
        super("B",true,true);
    }

    @Override
    public String handleCase(InputData input) {

        int caseNumber = input.testCase;
        int minCost = minCost(input.P, 0, 0, input);

        return ("Case #" + caseNumber + ": " + minCost);
    }
    
    static int MAX_PRICE = 100000 * 2048;
    
    /**
     * MinCost starts at the top of the tournament tree
     * roundNum = P (Pth round) is the top
     * round 0 
     * 
     * 
     * @param roundNum 1 based round number with # of matches = 2 ^ (P - roundNum)
     * @param matchNum which match  0 to 2 ^ (P - roundNum) - 1 in the round
     * @param matchesMissed how many not purchased above in the tree
     */
    static int minCost(int roundNum, int matchNum, int matchesMissed, final InputData input) {
        //Next match numbers = matchNum * 2 and matchNum * 2 + 1
        
        //Base case, here we know how many matches could have been missed
        if (roundNum == 0) {
            //here the matchNum is really the team number
            if (matchesMissed > input.M[matchNum] ) {
                return MAX_PRICE;
            }
            return 0;
        }
        
        int matchNumLeft = matchNum * 2;
        int matchNumRight = matchNum * 2 + 1;
        
        //Go down the tree in 2 ways, either we buy it or not
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

    



    
}