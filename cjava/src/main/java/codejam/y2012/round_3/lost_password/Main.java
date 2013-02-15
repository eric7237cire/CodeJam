package codejam.y2012.round_3.lost_password;

import java.util.Scanner;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Main()
    {
      //  super("E");
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
       
        return in;
    }

  
    @Override
    public String handleCase(InputData in) {
       

        return String.format("Case #%d: ", in.testCase);
    }
}