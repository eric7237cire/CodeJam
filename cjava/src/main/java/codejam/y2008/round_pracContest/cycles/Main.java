package codejam.y2008.round_pracContest.cycles;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.y2008.round_pracContest.old_magician.InputData;

import com.google.common.base.Preconditions;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
       //super();
        super("A", 0,0);
    }
    
    
    @Override
    public InputData readInput(Scanner scanner, int testCase)
    {

        InputData in = new InputData(testCase);
        
       // in.W = scanner.nextInt();
        //in.B = scanner.nextInt();
        
        return in;
    }
    
    
    @Override
    public String handleCase(InputData in)
    {
        
        return  String.format("Case #%d: ", in.testCase);
        
    }
    

}