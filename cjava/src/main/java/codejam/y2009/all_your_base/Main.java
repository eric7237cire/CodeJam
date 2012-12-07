package codejam.y2009.all_your_base;

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
	
	

    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
       InputData input = new InputData(testCase);
       input.codedNum = scanner.next();
       return input;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData input) {
        String codedNum = input.codedNum;

        log.info("Case {} {}", input.testCase, codedNum);
        
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

        StringBuilder transNum = new StringBuilder(codedNum);
        for(int i = 0; i < transNum.length(); ++i) {
            transNum.setCharAt(i, trans.get(transNum.charAt(i)));
        }
        String numVal = new BigInteger(transNum.toString(), Math.max(2, trans.size())).toString(10);
        
        return("Case #" + input.testCase + ": " + numVal);
    }
}