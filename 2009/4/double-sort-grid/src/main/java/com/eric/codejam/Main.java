package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.eric.codejam.utils.Grid;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    
    
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

       
        
        log.info("Starting calculating case {}", caseNumber);
        
        int count = 0;
       
         count = DynamicProgrammingLarge.solveGrid(input.grid);
        
       // count = DynamicProgrammingLargeNonOptimized.solveGrid(input.grid);
        //log.info("Count DP {}.  ans {}", caseNumber, countDP);

       // log.info("Done dp ");
        
        log.info("Done calculating answer case # {}.  ans [ {} ] ", caseNumber, count);
        
        
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + count);
    }
    
  
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        
        InputData  input = new InputData(testCase);
        
        Scanner ls = new Scanner(br.readLine());
       // ls.useDelimiter("");
        
        
        input.R = ls.nextInt();
        input.C = ls.nextInt();
                ls.close();
        
        BiMap<Character, Integer> chMap = HashBiMap.create();
        for(int i = 0; i < DynamicProgrammingLarge.LETTER_MAX; ++i) {
            chMap.put( (char) ((int) 'a' + i ), i);
        }
        
        chMap.put('.', DynamicProgrammingLarge.LETTER_MAX);
        
        input.grid = Grid.buildFromBufferedReader(br, input.R, input.C, chMap, null);
        //log.info("Reading data...Test case # {} ", testCase);
     //   log.info("Grid {}", input.grid);
        //log.info("Done Reading data...Test case # {} ", testCase);
        
     //   String line = br.readLine();
        
       // ls.close();
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
        //   args = new String[] { "sample.txt" };
           args = new String[] { "C-small-practice.in" };
          // args = new String[] { "C-large-practice.in" };
           //args = new String[] { "largeInput.txt" };
        }
        log.info("Input file {}", args[0]);

        Main m = new Main();
        Runner.go(args[0], m, m, new InputData(-1));
        //Runner.goSingleThread(args[0], m, m);
        
       
    }

    
}