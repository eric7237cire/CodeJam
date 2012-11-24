package com.eric.codejam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.InputData;
import com.eric.codejam.Main;
import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

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
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        
        
        InputData  input = new InputData(testCase);
        
        input.P = Integer.parseInt(br.readLine());
        
        String[] line = br.readLine().split(" ");
        input.M = new int[1 << input.P];
        for(int i = 0; i < input.M.length; ++i) {
            input.M[i] = Integer.parseInt(line[i]);
        }
        
        input.costs = new ArrayList<>();
        for(int round = 1; round <= input.P; ++round) {
            int matches = 1 << input.P - round;
            List<Integer> matchCosts = new ArrayList<>();
            line = br.readLine().split(" ");
            for(int i = 0; i < matches; ++i) {
                matchCosts.add(Integer.parseInt(line[i]));
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