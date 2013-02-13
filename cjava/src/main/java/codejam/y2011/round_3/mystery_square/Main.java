package codejam.y2011.round_3.mystery_square;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;

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
            
            int squareRootDigits = Long.toBinaryString(i).length();
            
            //Need one extra digit in the square
            BigInteger squareDigits = BigInteger.valueOf(ii).mod(BigInteger.ONE.shiftLeft(squareRootDigits+1));
            
            log.debug("Square digits : {}", squareDigits.toString(2));
            
            if (i % 4 == 3 || i % 4 == 1) {
            
                BigInteger sqRoot = findSquareRootBottomUp( squareDigits, squareRootDigits+1, (int) (i % 4) );
                BigInteger sq = sqRoot.multiply(sqRoot);
                
                log.debug("Calculated square : {} ({})", sq.toString(2), sq );
                
                
            }
            
        }
    }
    
    //Guessing last 2 digits are 11
    /**
     * Produces a square root of k - 1 digits
     * @param lowerSquareDigits 
     * @param numLowerSquareDigits k
     * @param initialSquareRootGuess 11 or 01
     */
    public BigInteger findSquareRootBottomUp(BigInteger lowerSquareDigits, int numLowerSquareDigits,
            int initialSquareRootGuess)
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
        BigInteger currentSquareRootGuess = BigInteger.valueOf(initialSquareRootGuess);
        
        log.debug("lower square {} ({})", lowerSquareDigits.toString(2), 
                lowerSquareDigits);
        
        
        
        for(int sqRootDigit = 3; sqRootDigit < numLowerSquareDigits; ++sqRootDigit)
        {
            BigInteger currentSquareRootMod = BigInteger.ONE.shiftLeft(sqRootDigit - 1);
            
            log.debug("Square root = {}*A + {}", currentSquareRootMod, currentSquareRootGuess);
            BigInteger coefA = currentSquareRootMod.shiftLeft(1).multiply( currentSquareRootGuess );
            BigInteger constant = currentSquareRootGuess.multiply(currentSquareRootGuess);
            
            BigInteger currentSquareMod = currentSquareRootMod.shiftLeft(2);
            
            log.debug("(Square root)^2 = {}*A^2 + {}*A + {}", 
                    currentSquareMod, coefA, constant);
            
            // currentSquareRootGuessMod * A^2 + coefA * A + constant = square
            
            
            BigInteger mustEqual = lowerSquareDigits.mod( currentSquareMod );
            
            log.debug("(Square root)^2 % {} = {}", 
                    currentSquareMod, mustEqual);
            
            BigInteger aIsOne = coefA.add(constant).mod( currentSquareMod );
            BigInteger aIsZero =  constant.mod( currentSquareMod );
            
            if (aIsOne.equals( mustEqual)) {
                currentSquareRootGuess = currentSquareRootGuess.setBit(sqRootDigit - 1);
            } else {
                //it's a zero
                Preconditions.checkState(aIsZero.equals(mustEqual));
            }
            
            log.debug("Current square root guess {} ({})", currentSquareRootGuess.toString(2), currentSquareRootGuess);
        }
        
        return currentSquareRootGuess;
    }
  
    public void solve(String binaryString, int currentDivision)
    {
        int len = binaryString.length();
        
        int sqRootLen = (len+1) / 2;
        
        int lenNeededBottomUp = sqRootLen + 1;
        int lenNeededTopDown = len - sqRootLen;
        
        // len - start + 1 = lenBu
        int startBottomUp = len  - lenNeededBottomUp;
        
        String top = binaryString.substring(0, lenNeededTopDown);
        
        String bottom = binaryString.substring(startBottomUp, binaryString.length() );
        
        log.debug("Binary str {} top {} bot {}", binaryString, top, bottom);
    }
    
    @Override
    public String handleCase(InputData in) {
       
        solve("11001", 1);
       // testTopDown();
       // testBottomUp();
        

        return String.format("Case #%d: ", in.testCase);
    }
}