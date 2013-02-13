package codejam.y2011.round_3.mystery_square;

import java.math.RoundingMode;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
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
            
            //Half using ? marks
            String mystery = binaryStr.substring(0, mysStartIndex) +
                    StringUtils.repeat('?', binaryStr.length() - mysStartIndex);
            
            log.debug("Mystery {}", mystery);
            
            //Calculate square root rounded down
            String withOnesStr = mystery.replace('?', '1');
            long withOnes = Long.parseLong(withOnesStr, 2);
            long sqrt = LongMath.sqrt(withOnes, RoundingMode.DOWN);
            log.debug("Replace with ones {} ({}) take sqrt rounded down = {} ", 
                    withOnesStr, withOnes, sqrt );
            
            
            
        }
        
        
    }
    
    public void testBottomUp() {
        for(long i = 2; i < 50; ++i) {
            long ii = i * i;
            log.debug("\nSquare root of {} ({}): {} ({})", 
                    Long.toBinaryString(ii), ii, Long.toBinaryString(i), i);
            
            
            String binaryStr = Long.toBinaryString(ii);
            
            int len = binaryStr.length();
            
            int mysStopIndex = (len+1) / 2;
            
            String mystery = StringUtils.repeat('?', mysStopIndex) 
                    + binaryStr.substring(mysStopIndex, binaryStr.length()) ;
            
            log.debug("Mystery {}", mystery);
            
        }
    }
    
    //Guessing last 2 digits are 11
    public void findSquareRootBottomUp(long lowerSquareDigits, int numLowerSquareDigits,
            long initialSquareRootGuess)
    {
        Preconditions.checkState(initialSquareRootGuess == 3 || initialSquareRootGuess == 1);
        /**
         * say we know the square root ends in
         * ----11
         * then the square root must be 4A + 3
         * 
         * ==16A^2 + 3*4*2A + 9 = 24A + 9
         * 
         * then take lowerSquareDigits % 16
         */
        long currentSquareRootGuess = initialSquareRootGuess;
        
        log.debug("lower square {} ({})", Long.toBinaryString(lowerSquareDigits), lowerSquareDigits);
        
        
        
        for(int sqRootDigit = 3; sqRootDigit < numLowerSquareDigits; ++sqRootDigit)
        {
            int currentSquareRootMod = 1 << sqRootDigit - 1;
            
            log.debug("Square root = {}*A + {}", currentSquareRootMod, currentSquareRootGuess);
            long coefA = currentSquareRootMod * 2 * currentSquareRootGuess;
            long constant = currentSquareRootGuess*currentSquareRootGuess;
            
            int currentSquareMod = currentSquareRootMod << 2;
            
            log.debug("(Square root)^2 = {}*A^2 + {}*A + {}", 
                    currentSquareMod, coefA, constant);
            
            // currentSquareRootGuessMod * A^2 + coefA * A + constant = square
            
            
            long mustEqual = lowerSquareDigits % currentSquareMod;
            
            log.debug("(Square root)^2 % {} = {}", 
                    currentSquareMod, mustEqual);
            
            long aIsOne = (coefA * 1 + constant) % currentSquareMod;
            long aIsZero = (coefA * 0 + constant) % currentSquareMod;
            
            if (aIsOne == mustEqual) {
                currentSquareRootGuess |= (1 << sqRootDigit - 1);
            } else {
                //it's a zero
                Preconditions.checkState(aIsZero == mustEqual);
            }
            
            log.debug("Current square root guess {} ({})", Long.toBinaryString(currentSquareRootGuess), currentSquareRootGuess);
        }
        
    }
  
    @Override
    public String handleCase(InputData in) {
       
       // testTopDown();
        //testBottomUp();
        findSquareRootBottomUp(361 % (1 << 6), 6,  3);

        return String.format("Case #%d: ", in.testCase);
    }
}