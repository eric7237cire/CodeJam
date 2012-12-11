package codejam.y2009.round_4.double_sort_grid;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Grid;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    
    
    
    @Override
    public String handleCase(InputData input) {

       
        
        log.info("Starting calculating case {}", input.testCase);
        
        int count = 0;
       
         count = DynamicProgrammingLarge.solveGrid(input.grid);
        
       // count = DynamicProgrammingLargeNonOptimized.solveGrid(input.grid);
        //log.info("Count DP {}.  ans {}", caseNumber, countDP);

       // log.info("Done dp ");
        
        log.info("Done calculating answer case # {}.  ans [ {} ] ", input.testCase, count);
        
        
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + input.testCase + ": " + count);
    }
    
  
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)  {
        
    
        
        InputData  input = new InputData(testCase);
        
        
        
        
        input.R = scanner.nextInt();
        input.C = scanner.nextInt();
               
        
        BiMap<Character, Integer> chMap = HashBiMap.create();
        for(int i = 0; i < DynamicProgrammingLarge.LETTER_MAX; ++i) {
            chMap.put( (char) ((int) 'a' + i ), i);
        }
        
        chMap.put('.', DynamicProgrammingLarge.LETTER_MAX);
        
        input.grid = Grid.buildFromScanner(scanner, input.R, input.C, chMap, null);
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
        Runner.go(args[0], m, m, new InputData(-1),4);
      //  Runner.goSingleThread(args[0], m, m);
        
       
    }

    
}