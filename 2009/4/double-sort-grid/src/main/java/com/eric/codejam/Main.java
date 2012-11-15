package com.eric.codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.eric.codejam.utils.Direction;
import com.eric.codejam.utils.Grid;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Ordering;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    
    public int[][] solve1d() {
        
        //int [Dimension][Letter]
        /*
         * counts[2][24-1] means
         * 
         * w..
         * 
         * abcde
         * fghij
         * klmno
         * pqrst
         * uvwxy
         * z
         */
        int[][] counts = new int[10][26];
        
        for(int let = 1; let <= 26; ++let) {
            counts[0][26-let] = let;
        }
        
        for(int dim = 1; dim < 4; ++dim) {
            int runningSum = 0;
            for(int let = 26; let >= 1; --let) {
                runningSum += counts[dim-1][let-1];
                
                counts[dim][let-1] = runningSum;
                
        //        log.info("Running counts {}", counts[dim]);
            }
        }
        
        return counts;
    }
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        solve1d();
        
        
        int countBF = count(input.grid, new Grid<Integer>(input.grid));
        
        //int[][] count1d = solve1d();
        
        //log.info("count 1 d {}", count1d[input.grid.getCols()-2][input.grid.getEntry(0)-1]);
        
        //SingleRowSolver ss = new SingleRowSolver(input.grid);
        
        int count = SingleRowSolver.solveGrid(input.grid);
        //log.info("Count DP {}.  ans {}", caseNumber, countDP);

        log.info("Done calculating answer case # {}.  ans [ {} ] BF [ {} ]", caseNumber, count, countBF);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " );
    }
    
    //Bruteforce
    public int count(Grid<Integer> grid, Grid<Integer> gridOrig) {
        //log.info("Count grid {}", grid);
        Ordering<Integer> o = Ordering.natural().nullsFirst();
        int count = 0;
        for(int i = 0; i < grid.getSize(); ++i) {
            
            Integer current = grid.getEntry(i);
            Integer top = grid.getEntry(i, Direction.NORTH);
            Integer left = grid.getEntry(i, Direction.WEST);
            
            //A flexible spot, compute all possibilities
            if (current == 0) {
                for(int j = 26; j >= 1; --j) {
                    //Must be non decreasing
                    if (o.compare(j, top) < 0) {
                        break;
                    }
                    
                    if (o.compare(j, left) < 0) {
                        break;
                    }
                    Grid<Integer> copy = new Grid<Integer>(grid);
                    copy.setEntry(i, j);
                    count += count(copy, gridOrig);
                }
                return count;
            }
            
            //Can not decrease
            if (o.compare(current, top) < 0) {
                return 0;
            }
            
            if (o.compare(current, left) < 0) {
                return 0;
            }
        }
        
        //No variable squares
        return 1;
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
        for(int i = 1; i <= 26; ++i) {
            chMap.put( (char) ((int) 'a' + i - 1), i);
        }
        
        chMap.put('.', 0);
        
        input.grid = Grid.buildFromBufferedReader(br, input.R, input.C, chMap, null);
        //log.info("Reading data...Test case # {} ", testCase);
        log.info("Grid {}", input.grid);
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
           args = new String[] { "sample.txt" };
           //args = new String[] { "smallInput.txt" };
           //args = new String[] { "largeInput.txt" };
        }
        log.info("Input file {}", args[0]);

        Main m = new Main();
        //Runner.go(args[0], m, m, new InputData(-1));
        Runner.goSingleThread(args[0], m, m);
        
       
    }

    
}