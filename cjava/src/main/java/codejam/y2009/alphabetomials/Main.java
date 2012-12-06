package codejam.y2009.alphabetomials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.main.Runner.TestCaseInputScanner;
import codejam.utils.multithread.Consumer.TestCaseHandler;
import codejam.utils.polynomial.AddTerms;
import codejam.utils.polynomial.CoefficientTerm;
import codejam.utils.polynomial.Polynomial;
import codejam.utils.polynomial.Term;
import codejam.utils.polynomial.VariableTerm;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    
    public List<Integer> usePoly(InputData input) {
        List<Integer> totals = new ArrayList<>();

//        Cloner c = new Cloner();

        Map<String, Integer> values = new HashMap<>();

       

        List<Map<VariableTerm, Term>> subsList = new ArrayList<>();
        
        for (int i = 0; i < input.d; ++i) {
            Map<VariableTerm, Term> subs = new HashMap<>();
            for (int chInt = 'a'; chInt <= 'z'; ++chInt) {
                char ch = (char) chInt;
                
                String varName = "" + ch;
                
                int count = StringUtils.countMatches(input.dictWords.get(i), varName);
                
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
                log.debug("Computing k {} after sub {}", eachK, p);
                totalPoly.addSelf(p);
                
                //log.info("Computing k {} after add {}", eachK, totalPoly);
                totalPoly.doSimplify();

                //log.info("Computing k {} after simplify {}", eachK, totalPoly);
                
            }

            totalPoly.doSimplify();

            log.debug("Poly obj {} k {}", totalPoly, eachK);
            
            orig = new Polynomial(totalPoly);
            
            totals.add(totalPoly.evaluate(values) % 10009);
            //System.out.println(totals);
        }

        return totals;
    }

    public List<Integer> doPerms(InputData input) {
        List<Integer> totals = new ArrayList<>();

        for (int eachK = 1; eachK <= input.k; ++eachK) {
            Integer[] dictArray = new Integer[input.d];
            Integer[] combin = new Integer[eachK];
            int total = 0;
            for (int i = 0; i < input.d; ++i) {
                dictArray[i] = i;
            }

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

                log.info("Perm {} p({}) {} total {}", new Object[] {(Object) combin,
                        p_counts, wordEval, total});

                boolean fullLoop = true;
                for (int pos = 0; pos < eachK; ++pos) {
                    combin[pos]++;
                    if (combin[pos] < input.d) {
                        fullLoop = false;
                        break;
                    }
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
       // log.info("k {} d {} poly {}", m.k, m.d, m.polynomial);

        return input;
    }

    /* (non-Javadoc)
     * @see codejam.utils.multithread.Consumer.TestCaseHandler#handleCase(java.lang.Object)
     */
    @Override
    public String handleCase(InputData input) {
        

        List<Integer> total2 = usePoly(input);

        log.info("Starting case {}\n total {}\n total poly {}", input.testCase,
                 total2);

//        os.println("Case #" + caseNumber + ": " + StringUtils.join(total, " "));
        return ("Case #" + input.testCase + ": " + StringUtils.join(total2, " "));

    }
}