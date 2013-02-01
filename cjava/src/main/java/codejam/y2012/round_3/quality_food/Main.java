package codejam.y2012.round_3.quality_food;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
      //  super("E");
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData input = new InputData(testCase);
       
        return input;
    }

  
    @Override
    public String handleCase(InputData in) {
       

        return String.format("Case #%d: ", in.testCase);
    }
}