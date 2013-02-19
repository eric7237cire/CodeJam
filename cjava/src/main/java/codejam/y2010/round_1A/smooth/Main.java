package codejam.y2010.round_1A.smooth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    public Main() {
        super("B", 1, 1);
    }
    
    int[][] memoize = new int[100][257];
    
    public final int NO_PREVIOUS_VALUE = 256;
    
    int getMinCost(int currentIndex, int previousValue, InputData input) {
        
        if (currentIndex > input.pixels.size() - 1) {
            return 0;
        }
        
        if (memoize[currentIndex][previousValue] >= 0) {
            return memoize[currentIndex][previousValue];
        }
        
        int currentValue = input.pixels.get(currentIndex);
      
        int minCost = Integer.MAX_VALUE;
        
        //del current; always valid
        int cost = input.deleteCost + getMinCost(currentIndex + 1, previousValue, input);
        minCost = Math.min(cost, minCost);
        
        //Change value current to something close enough to the previous value
        int min = Math.max(0, previousValue - input.minimumDist);
        int max = Math.min(255, previousValue + input.minimumDist);
        min = (previousValue == NO_PREVIOUS_VALUE) ? 0 : min;
        max = (previousValue == NO_PREVIOUS_VALUE) ? 255 : max;
        for(int v = min; v <= max; ++v) {            
            int diff = Math.abs(currentValue - v);
            cost = diff + getMinCost(currentIndex + 1, v, input);
            minCost = Math.min(cost, minCost);            
        }
        

        /**
         * If previous value is more than the min distance
         * away from the currently value, then add a node to
         * make it valid.
         * 
         * The checks avoid an infinite loop as what was invalid now is valid
         */
        if (previousValue != NO_PREVIOUS_VALUE && input.minimumDist > 0) {

            
            if (previousValue + input.minimumDist < currentValue) {
                // add a node = prevVal + minDist to reduce distance
                cost = input.insertCost
                        + getMinCost(currentIndex, previousValue
                                + input.minimumDist, input);
            } else if (previousValue - input.minimumDist > currentValue) {
                cost = input.insertCost
                        + getMinCost(currentIndex, previousValue
                                - input.minimumDist, input);
            }
            minCost = Math.min(cost, minCost);
        }
            
              
        
       // log.debug("Prev {} Cur idx {} cur val {} min cost {}", previousValue, currentIndex, currentValue, minCost);
        memoize[currentIndex][previousValue] = minCost;
        return minCost;
    }
    
    @Override
    public String handleCase(InputData input) {

        int caseNumber = input.testCase;
        
        log.debug(
                "Starting calculating case {}.  Input {} Delete {} Min dist {}. \n Pixels {}",
                caseNumber, input.insertCost, input.deleteCost,
                input.minimumDist, input.pixels);

        Main m = new Main();
        for (int i = 0; i < m.memoize.length; ++i) {
            Arrays.fill(m.memoize[i], -1);            
        }
        int cost = m.getMinCost(0, NO_PREVIOUS_VALUE, input);
        return ("Case #" + caseNumber + ": " + cost);

    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        
    
        InputData  input = new InputData(testCase);
        
        input.deleteCost = scanner.nextInt();
        input.insertCost = scanner.nextInt();
        input.minimumDist = scanner.nextInt();
        input.num = scanner.nextInt();
        
        input.pixels = new ArrayList<Integer>();
        
        
        for(int i = 0; i < input.num; ++i) {
            input.pixels.add(scanner.nextInt());
        }
        return input;
        
    }


    
}