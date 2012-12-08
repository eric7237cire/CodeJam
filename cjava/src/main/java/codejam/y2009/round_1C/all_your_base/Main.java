package codejam.y2009.round_1C.all_your_base;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

	final static Logger log = LoggerFactory.getLogger(Main.class);
	
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       InputData input = new InputData(testCase);
       input.codedNum = scanner.next();
       return input;
    }

    @Override
    public String handleCase(InputData input) {
        String codedNum = input.codedNum;

        log.info("Case {} {}", input.testCase, codedNum);
    
        /*to minimize the codednum, we read the symbols left to right
         * assigning 1, 0, 2, 3, 4, etc
         *  
         */
        Map<Character, Character> trans = new HashMap<>();

        for (int i = 0; i < codedNum.length(); ++i) {
            Character c = codedNum.charAt(i);
            if (trans.containsKey(c)) {
                continue;
            }
            
            Character tarC = trans.size() >= 2 ?
                    Character.forDigit(trans.size(), 36) :
                        (trans.size() == 0 ? '1' : '0')  ;
            
            trans.put(c, tarC);

        }

        //Build up the new number
        StringBuilder transNum = new StringBuilder(codedNum);
        for(int i = 0; i < transNum.length(); ++i) {
            transNum.setCharAt(i, trans.get(transNum.charAt(i)));
        }
        //Evaluate it in the minimal base possible
        String numVal = new BigInteger(transNum.toString(), Math.max(2, trans.size())).toString(10);
        
        return("Case #" + input.testCase + ": " + numVal);
    }
}