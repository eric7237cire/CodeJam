package codejam.y2009.round_3.alphabetomials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;

import com.google.common.collect.Lists;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {

      //  return new String[] { "sample.in" };
        
        return new String[] {"B-small-practice.in", "B-large-practice.in"};
    }

    
    /* (non-Javadoc)
     * @see codejam.utils.main.Runner.TestCaseInputScanner#readInput(java.util.Scanner, int)
     */
    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);

        input.polynomial = scanner.next();
        input.k = scanner.nextInt();
        input.d = scanner.nextInt();
        input.dictWords = new ArrayList<>();
        for (int i = 0; i < input.d; ++i) {
            input.dictWords.add(scanner.next());
        }
        
        input.wordLetterCount = new int[input.d][26];
        
        for (int i = 0; i < input.d; ++i) {
            for (int chInt = 'a'; chInt <= 'z'; ++chInt) {
                char ch = (char) chInt;
            
                //Count of letter in the dictionary
                int count = StringUtils.countMatches(input.dictWords.get(i), ""+ch);
                
                input.wordLetterCount[i][chInt-'a'] = count;
            }
        }


        return input;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    
    public String handleCase(InputData input)
    {
        List<Map<String, Integer>> memo = Lists.newArrayList();
        for (int k = 1; k <= input.k; ++k)
        {
            memo.add(new HashMap<String, Integer>());
        }

        List<Integer> totals = Lists.newArrayList();

        for (int k = 1; k <= input.k; ++k)
        {
            int total = FastSolution.combineTerms(input, k, memo);
            totals.add(total);
        }

        return ("Case #" + input.testCase + ": " + StringUtils.join(totals, " "));

    }
    
    public String oldHandleCase(InputData input) {

        OrigSolution os = new OrigSolution();

        List<Integer> total2 = os.usePoly(input);

        log.info("Starting case {}\n total {}\n total poly {}", input.testCase,
                 total2);

//        os.println("Case #" + caseNumber + ": " + StringUtils.join(total, " "));
        return ("Case #" + input.testCase + ": " + StringUtils.join(total2, " "));

    }
}