package codejam.y2011.round_3.mystery_square;

import java.math.RoundingMode;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.math.LongMath;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public Main()
    {
       super("D", 0, 0, 1);
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData input = new InputData(testCase);
       
        return input;
    }

    public void testTopDown()
    {
        for(long i = 2; i < 500; ++i) {
            long ii = i * i;
            log.debug("\nSquare root of {} ({}): {} ({})", 
                    Long.toBinaryString(ii), ii, Long.toBinaryString(i), i);
            
            
            String binaryStr = Long.toBinaryString(ii);
            
            int len = binaryStr.length();
            
            int mysStartIndex = len / 2;
            //int halfIndex = (len+1) / 2 - 1;
            String mystery = binaryStr.substring(0, mysStartIndex) +
                    StringUtils.repeat('?', binaryStr.length() - mysStartIndex);
            
            log.debug("Mystery {}", mystery);
            
            String withOnesStr = mystery.replace('?', '1');
            long withOnes = Long.parseLong(withOnesStr, 2);
            long sqrt = LongMath.sqrt(withOnes, RoundingMode.DOWN);
            log.debug("Replace with ones {} ({}) take sqrt rounded down = {} ", 
                    withOnesStr, withOnes, sqrt );
        }
        
        
    }
  
    @Override
    public String handleCase(InputData in) {
       
        testTopDown();

        return String.format("Case #%d: ", in.testCase);
    }
}