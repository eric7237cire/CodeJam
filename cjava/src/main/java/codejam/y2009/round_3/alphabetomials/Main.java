package codejam.y2009.round_3.alphabetomials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import codejam.utils.main.DefaultInputFiles;
import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.polynomial.AddTerms;
import codejam.utils.polynomial.CoefficientTerm;
import codejam.utils.polynomial.Polynomial;
import codejam.utils.polynomial.Term;
import codejam.utils.polynomial.VariableTerm;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>, DefaultInputFiles {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    @Override
    public String[] getDefaultInputFiles() {
      //  return new String[] { "sample.in" };
         //return new String[] { "B-small-practice.in" };
         return new String[] { "B-large-practice.in" };
    }

    
    public List<Integer> usePoly(InputData input) {
        List<Integer> totals = new ArrayList<>();


        Map<String, Integer> values = new HashMap<>();

       

        List<Map<VariableTerm, Term>> subsList = new ArrayList<>();
        
        for (int i = 0; i < input.d; ++i) {
            Map<VariableTerm, Term> subs = new HashMap<>();
            for (int chInt = 'a'; chInt <= 'z'; ++chInt) {
                char ch = (char) chInt;
                
                String varName = "" + ch;
                
                //Count of letter in the dictionary
                int count = StringUtils.countMatches(input.dictWords.get(i), varName);
                
                //Letter/variable will be substituted by letter + count 
                subs.put(new VariableTerm(varName), new AddTerms(
                        new CoefficientTerm(count),
                        new VariableTerm(varName)));
                
                values.put(varName, 0);
            }
            
            subsList.add(subs);
        }

        Polynomial orig = new Polynomial(input.polynomial);
        orig.doSimplify();
        
        Polynomial totalPoly = new Polynomial();

        for (int eachK = 1; eachK <= input.k; ++eachK) {
            //System.out.println("k " + eachK);
            totalPoly = new Polynomial();

            for (int i = 0; i < input.d; ++i) {
                //System.out.println("i " + i);
                Polynomial p = new Polynomial(orig);

                //log.info("Computing k {} before sub {}", eachK, p);
                p.substitute(subsList.get(i));
                //log.debug("Computing k {} after sub {}", eachK, p);
                totalPoly.addSelf(p);
                
                //log.info("Computing k {} after add {}", eachK, totalPoly);
                totalPoly.doSimplify();

                //log.info("Computing k {} after simplify {}", eachK, totalPoly);
                
            }

            totalPoly.doSimplify();

            //log.debug("Poly obj {} k {}", totalPoly, eachK);
            
            /**
             * Save the polynomial representing the total p(S).  The
             * sum of all permutations of the words in the dictionary (basically a=3,b=2,...)
             * using k words.
             * 
             * To compute k+1 the next round, it is as if we take all the words again and
             * add them.  
             * 
             * The trick is we keep the last round in polynomial form, ready to make another subsitition.
             * 
             * ie, the polynomial is a^2
             * 
             * for k = 1, and d= a=2; a=3 we do (a+2)^2 + (a+3)^2.
             * 
             * The total for k = 1 is the value of the polynomial with all vars = 0.
             * 
             * The variables is to be ready to substitute for the next round.
             * 
             * k=2 will be (a+2+2)^2 + (a+2+3)^2 + (a+3+2)^2 + (a+3+3)^2 
             */
            orig = new Polynomial(totalPoly);
            
            totals.add(totalPoly.evaluate(values) % 10009);
            //System.out.println(totals);
        }

        return totals;
    }

    public List<Integer> doPerms(InputData input) {
        List<Integer> totals = new ArrayList<>();

        /**
         * k is how many words from the dictionary to combine into a new word.
         * Every permutation is calculated and counted.
         */
        for (int eachK = 1; eachK <= input.k; ++eachK) {
            
            Integer[] combin = new Integer[eachK];
            int total = 0;
            

            for (int i = 0; i < eachK; ++i) {
                combin[i] = 0;
            }

            /*
             * Permutations<Integer> perm = Permutations.create(dictArray,
             * combin, eachK);
             */

            while (true) {

                String word = "";
                for (Integer comIndex : combin) {
                    word += input.dictWords.get(comIndex) + " ";
                }

                final int wordEval = evalP(word,input);
                total += wordEval;

                total %= 10009;

                List<Integer> p_counts = new ArrayList<>();
                p_counts.add(StringUtils.countMatches(word, "a"));
                // p_counts.add(StringUtils.countMatches(word, "b"));

                log.debug("Perm {} p({}) {} total {}", new Object[] {(Object) combin,
                        p_counts, wordEval, total});

                boolean fullLoop = true;
                //Try all permutations.  This loop finds the first position that can be incremented
                for (int pos = 0; pos < eachK; ++pos) {
                    combin[pos]++;
                    
                    //Incremented, we are done
                    if (combin[pos] < input.d) {
                        fullLoop = false;
                        break;
                    }
                    
                    //This position is now zero, look to increment combin
                    combin[pos] = 0;
                }

                if (fullLoop) {
                    break;
                }

            }

            totals.add(total);
        }

        return totals;
    }

    public int evalP(String word, InputData input) {
        int total = 0;
        int term = 1;
        for (int c = 0; c < input.polynomial.length(); ++c) {
            char ch = input.polynomial.charAt(c);
            if (ch == '+') {
                total += term;
                term = 1;
                continue;
            }
            int count = StringUtils.countMatches(word, "" + ch);
            term *= count;
        }

        total += term;
        return total;
    }


    public Main() {

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
    
    public String handleCase(InputData input) {
    List<Map<String, Integer>> memo = Lists.newArrayList();
    for(int k = 1; k <= input.k; ++k) {
        memo.add(new HashMap<String,Integer>());
    }
    
    List<Integer> totals = Lists.newArrayList();
    
    for(int k = 1; k <= input.k; ++k) {
        int total = FastSolution.combineTerms(input, k, memo);
        totals.add(total);
    }
    
    return ("Case #" + input.testCase + ": " + StringUtils.join(totals, " "));
    
    }
    
    public String rhandleCase(InputData input) {

        

        List<Integer> total2 = usePoly(input);

        log.info("Starting case {}\n total {}\n total poly {}", input.testCase,
                 total2);

//        os.println("Case #" + caseNumber + ": " + StringUtils.join(total, " "));
        return ("Case #" + input.testCase + ": " + StringUtils.join(total2, " "));

    }
}