package codejam.y2009.round_final.double_sort_grid;

import java.util.Scanner;

import ch.qos.logback.classic.Level;
import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.utils.Grid;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class DoubleSortGrid extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

   
    public DoubleSortGrid()
    {
        super("C", 1, 1);
        (( ch.qos.logback.classic.Logger) log).setLevel(Level.INFO);
    }
    
    
    @Override
    public String handleCase(InputData input) {

       
        
        log.info("Starting calculating case {}", input.testCase);
        
        int count = 0;
       
         count = DynamicProgrammingLarge.solveGrid(input.grid);
        
        log.info("Done calculating answer case # {}.  ans [ {} ] ", input.testCase, count);
        
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

    
    
}