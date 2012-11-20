package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
        
    int[][] memoize = new int[100][257];
    int getMinCost(int currentIndex, int previousValue, InputData input) {
        
        if (currentIndex > input.pixels.size() - 1) {
            return 0;
        }
        
        if (memoize[currentIndex][previousValue] >= 0) {
            return memoize[currentIndex][previousValue];
        }
        
        int currentValue = input.pixels.get(currentIndex);
      
        int minCost = Integer.MAX_VALUE;
        
        //del current
        int cost = input.deleteCost + getMinCost(currentIndex + 1, previousValue, input);
        minCost = Math.min(cost, minCost);
        
        //Change value current
        int min = Math.max(0, previousValue - input.minimumDist);
        int max = Math.min(255, previousValue + input.minimumDist);
        min = (previousValue == 256) ? 0 : min;
        max = (previousValue == 256) ? 255 : max;
        for(int v = min; v <= max; ++v) {            
            int diff = Math.abs(currentValue - v);
            cost = diff + getMinCost(currentIndex + 1, v, input);
            minCost = Math.min(cost, minCost);            
        }
        
        //previous not valid
        if (previousValue != 256 && input.minimumDist > 0) {

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
    public String handleCase(int caseNumber, InputData input) {

        log.info(
                "Starting calculating case {}.  Input {} Delete {} Min dist {}. \n Pixels {}",
                caseNumber, input.insertCost, input.deleteCost,
                input.minimumDist, input.pixels);

        Main m = new Main();
        for (int i = 0; i < m.memoize.length; ++i) {
            Arrays.fill(m.memoize[i], -1);            
        }
        int cost = m.getMinCost(0, 256, input);
        return ("Case #" + caseNumber + ": " + cost);

    }
    
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        
        InputData  input = new InputData(testCase);
        
        input.deleteCost = Integer.parseInt(line[0]);
        input.insertCost = Integer.parseInt(line[1]);
        input.minimumDist = Integer.parseInt(line[2]);
        input.num = Integer.parseInt(line[3]);
        
        input.pixels = new ArrayList<Integer>();
        
        line = br.readLine().split(" ");
        for(int i = 0; i < input.num; ++i) {
            input.pixels.add(Integer.parseInt(line[i]));
        }
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           //args = new String[] { "sample.txt" };
          // args = new String[] { "B-small-practice.in" };
           args = new String[] { "B-large-practice.in" };
        }
        log.info("Input file {}", args[0]);

        Main m = new Main();
        //Runner.goSingleThread(args[0], m, m);
        Runner.go(args[0], m, m, new InputData(-1));
        
       
    }

    
}