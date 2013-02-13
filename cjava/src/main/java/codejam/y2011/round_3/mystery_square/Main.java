package codejam.y2011.round_3.mystery_square;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.InputFilesHandler;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.math.BigIntegerMath;
import com.google.common.math.LongMath;

public class Main extends InputFilesHandler implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    
    public Main()
    {
       super("D", 0, 1, 0);
    }
    
    
   
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       
        InputData in = new InputData(testCase);
        in.S = scanner.next();
        return in;
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
            
            String topDown = getNeededTopDownString(binaryStr);
            
            BigInteger sqRoot = findSquareRootTopDown(new BigInteger(topDown, 2), binaryStr.length() - topDown.length());
            
            log.debug("Mystery {} ({}) topDown {} square root {} ({})", mystery, ii, topDown, sqRoot.toString(2), sqRoot);
            
            Preconditions.checkState(sqRoot.multiply(sqRoot).longValue() == ii);
            
        }
        
        
    }
    
    /**
     * 
     * if we have 
     * 101???      topPart = 101, lenRest = 3
     * or
     * 11???  then topPart = 11, lenRest = 3
     * 
     */
    BigInteger findSquareRootTopDown(BigInteger topPart, int lenRest) {
        BigInteger top = topPart.shiftLeft(lenRest);
        log.debug("len rest {}  1 << len {}  - 1 = {}", lenRest, 
                BigInteger.ONE.shiftLeft(lenRest).toString(2),
                BigInteger.ONE.shiftLeft(lenRest).subtract(BigInteger.ONE).toString(2));
        top = top.add(BigInteger.ONE.shiftLeft(lenRest).subtract(BigInteger.ONE));
        
        
        BigInteger sqRoot = BigIntegerMath.sqrt(top, RoundingMode.DOWN);
        log.debug("Find square root top down.  top {} ({}) ; sq root {}", top.toString(2), top, sqRoot);
        
        return sqRoot;
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
            
            String neededBottomUp = getNeededBottomUpString(binaryStr);
            
            int sqRootLen = (len+1) / 2;
            
            Preconditions.checkState(neededBottomUp.length() == sqRootLen + 1);
            
            if (i % 4 == 3 || i % 4 == 1) {
            
                BigInteger sqRoot = findSquareRootBottomUp( new BigInteger(neededBottomUp, 2), neededBottomUp.length(), (int) (i % 4) );
                BigInteger sq = sqRoot.multiply(sqRoot);
                
                log.debug("Calculated square : {} ({})", sq.toString(2), sq );
                Preconditions.checkState(ii == sq.longValue());
                
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
                //An invalid state, bottom up failed
                if (!aIsZero.equals(mustEqual)) {
                    return null;
                }
                //it's a zero
                
            }
            
            log.debug("Current square root guess {} ({})", currentSquareRootGuess.toString(2), currentSquareRootGuess);
        }
        
        return currentSquareRootGuess;
    }
  
    
    public String getNeededTopDownString(String mystery)
    {
        int len = mystery.length();
        
        int sqRootLen = (len+1) / 2;
        
        int lenNeededTopDown = len - sqRootLen;
        
        return mystery.substring(0, lenNeededTopDown);
    }
    
    public String getNeededBottomUpString(String mystery) {
        int len = mystery.length();
        
        int sqRootLen = (len+1) / 2;
        
        int lenNeededBottomUp = sqRootLen + 1;
        
        // len - start + 1 = lenBu
        int startBottomUp = len  - lenNeededBottomUp;
        
        return mystery.substring(startBottomUp, mystery.length() );

    }
    
    public BigInteger solve(String binaryStringAns)
    {
        
        String binaryString = binaryStringAns;
        
        int iter = 0;
        int divisionShifts = 0;
        
        while(iter < 50)
        {
            ++iter;
        
            int len = binaryString.length();
            
            log.debug("Binary string {} length {} div shifts {}", binaryString, len, divisionShifts);
            
            if (len == 0) {
                return BigInteger.ZERO;
            }
            
            int sqRootLen = (len+1) / 2;
            
            int lenNeededTopDown = len - sqRootLen;
            
            String top = getNeededTopDownString(binaryString);
            
            String bottom = getNeededBottomUpString(binaryString);
            
            log.debug("Binary str {} top {} bot {}", binaryString, top, bottom);
            
            int countTop = StringUtils.countMatches(top, "?");
            int countBottom = StringUtils.countMatches(bottom, "?");
            
            if (countTop <= countBottom) 
            {
                GeneratePerms topPerms = new GeneratePerms(top);
                
                while(topPerms.hasNext()) {
                    BigInteger topPerm = topPerms.next();
                    
                    BigInteger sqRoot = findSquareRootTopDown(topPerm, len - lenNeededTopDown);
                    
                    if (matchBinaryStr(sqRoot, binaryString)) {
                        return sqRoot.multiply(sqRoot).shiftLeft(divisionShifts);
                    }
                }
            } else {
                GeneratePerms botPerms = new GeneratePerms(bottom);
                log.debug("Generating perms for {}", bottom);
                    
                while(botPerms.hasNext()) {
                    BigInteger botPerm = botPerms.next();
                    
                    log.debug("Bottom perm {} ({})", botPerm.toString(2), botPerm);
                    
                    if (botPerm.toString(2).equals("111111100101010010100010001")) {
                       log.debug("Found");
                    }
                    
                    BigInteger sqRoot = findSquareRootBottomUp(botPerm, bottom.length(), 1);
                    
                    if (matchBinaryStr(sqRoot, binaryString)) {
                        return sqRoot.multiply(sqRoot).shiftLeft(divisionShifts);
                    }
                    
                    sqRoot = findSquareRootBottomUp(botPerm, bottom.length(), 3);
                    
                    if (matchBinaryStr(sqRoot, binaryString)) {
                        return sqRoot.multiply(sqRoot).shiftLeft(divisionShifts);
                    }
                }
            }
            
            divisionShifts += 2;
            binaryString = binaryString.substring(0, binaryString.length() - 2);
        
        }
        
        return null;
    }
    
    public boolean matchBinaryStr(BigInteger sqRoot, String binaryStringAns) 
    {
        if (sqRoot == null)
            return false;
        
        String binary = sqRoot.multiply(sqRoot).toString(2);
        
        if (binary.length() != binaryStringAns.length())
            return false;
        
        for(int c = 0; c < binary.length(); ++c) {
            char ch = binary.charAt(c);
            char ch2 = binaryStringAns.charAt(c);
        
            if (ch2 == '?')
                continue;
            
            if (ch != ch2)
                return false;
        }
        
        return true;
    }
    
    public void testGenerate() {
        String test = "111?11?0010101001?100010?01";
        
        String goal = "111111100101010010100010001";
        
        GeneratePerms g = new GeneratePerms(test);
        
        int count = 0;
        boolean found = false;
        
        while(g.hasNext()) {
            String str = g.next().toString(2);
            log.debug("{}", test);
            log.debug("{}", str);
            if (str.equals(goal)) {
                found = true;
            }
            ++count;
        }
        
        Preconditions.checkState(count == 1 << 4);
        Preconditions.checkState(found);
    }
    
    public class GeneratePerms implements Iterator<BigInteger>
    {
        String mysteryString;
        
        List<Integer> questionMarkPositions;
        int maxPerm;
        int perm;
        
        BigInteger curPerm;
        
        GeneratePerms(String mysteryString)
        {
            this.mysteryString = mysteryString;
            questionMarkPositions = Lists.newArrayList();
            
            curPerm = BigInteger.valueOf(0);
            
            for(int c = 0; c < mysteryString.length(); ++c) {
                char ch = mysteryString.charAt(c);
                
                //In a string, MSB is 0
                int bitPos = mysteryString.length() - 1 - c;
                
                if (ch == '?') {
                    questionMarkPositions.add(bitPos);
                }
                
                if (ch == '1') {
                    curPerm = curPerm.setBit(bitPos);
                }
            }
            
            
            maxPerm = (1 << questionMarkPositions.size()) - 1;
        }

        @Override
        public boolean hasNext()
        {
            return perm <= maxPerm;
        }

        @Override
        public BigInteger next()
        {
            BigInteger r = curPerm;
            
            ++perm;
            for(int i = 0; i < questionMarkPositions.size(); ++i) {
                if ((perm & 1 << i) != 0) {
                    curPerm = curPerm.setBit(questionMarkPositions.get(i));
                } else {
                    curPerm = curPerm.clearBit(questionMarkPositions.get(i));
                }
            }

            return r;
        }

        @Override
        public void remove()
        {
           
            
        }
        
    }
    
    @Override
    public String handleCase(InputData in) {
       
        //testTopDown();
        //testBottomUp();
        
        testGenerate();
        
        if (in.S.length() == 1) {
            return String.format("Case #%d: 1", in.testCase);
        }
        
        BigInteger square = solve(in.S);
        //solve("11001", 1);
        
       // testBottomUp();
        

        return String.format("Case #%d: %s", in.testCase, square.toString(2));
    }
}