package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rits.cloning.Cloner;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    String polynomial;
    int k;
    int d;
    List<String> dictWords;
    Polynomial polyObj;

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        Main m = Main.buildMain(scanner);

        List<Integer> total = m.doPerms();
        
        List<Integer> total2 = m.usePoly();

        log.info("Starting case {}\n total {}\n total poly {}", caseNumber, total, total2);

        os.println("Case #" + caseNumber + ": " + StringUtils.join(total, " "));

    }

    public List<Integer> usePoly() {
        List<Integer> totals = new ArrayList<>();
        polyObj = new Polynomial();
        Cloner c = new Cloner();
        Polynomial orig = new Polynomial(polynomial);
        
        Map<String, Integer> values = new HashMap<>();
        for(int i = 0; i < d; ++i) {
            Polynomial p = c.deepClone(orig);
            for(int ch = 'a'; ch <= 'z'; ++ch) {
                String varName = "" + (char) ch + "_" + i;
                p.substitute(new VariableTerm("" + (char) ch), new VariableTerm(varName));
                values.put(varName, StringUtils.countMatches(dictWords.get(i), "" + (char) ch));
            }
            polyObj.add(p);
        }
        
        totals.add(polyObj.evaluate(values));
        
        polyObj.simplify();
        
        for (int eachK = 2; eachK <= k; ++eachK) {
            orig = c.deepClone(polyObj);
            polyObj = new Polynomial();
            
            for(int i = 0; i < d; ++i) {
                Map<VariableTerm, Term> subs = new HashMap<>();
                Polynomial p = c.deepClone(orig);
            
                for(String varName : values.keySet()) {
                    subs.put(new VariableTerm(varName), new BinomialTerm(new VariableTerm(varName), new VariableTerm(varName.charAt(0) + "_" + i)));
                }
                
                log.info("Computing k {} before sub {}", eachK, p);
                
                
                p.substitute(subs);
                log.info("Computing k {} after sub {}", eachK, p);                
                polyObj.add(p);
                log.info("Computing k {} Polyobj {}", eachK, polyObj);
            }
            
            polyObj.doSimplify();
            
            log.info("Poly obj {} k {}", polyObj, eachK);
            
            totals.add(polyObj.evaluate(values));
        }
        
        return totals;
    }
    public List<Integer> doPerms() {
        List<Integer> totals = new ArrayList<>();

        for (int eachK = 1; eachK <= k; ++eachK) {
            Integer[] dictArray = new Integer[d];
            Integer[] combin = new Integer[eachK];
            int total = 0;
            for (int i = 0; i < d; ++i) {
                dictArray[i] = i;
            }
            
            for (int i = 0; i < eachK; ++i) {
                combin[i] = 0;
            }

            /*Permutations<Integer> perm = Permutations.create(dictArray, combin,
                    eachK);
*/
            
            while (true) {
                
                String word = "";
                for(Integer comIndex : combin) {
                    word += dictWords.get(comIndex) + " ";
                }
                
                final int wordEval = evalP(word);
                total += wordEval;

                total %= 10009;
                
                List<Integer> p_counts = new ArrayList<>();
                p_counts.add(StringUtils.countMatches(word,  "a"));
                //p_counts.add(StringUtils.countMatches(word,  "b"));
                
                log.info("Perm {} p({}) {} total {}", (Object) combin, p_counts, wordEval, total);
                
                boolean fullLoop = true;
                for(int pos=0; pos<eachK; ++pos) {
                    combin[pos] ++;
                    if (combin[pos] < d) {
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

    public int evalP(String word) {
        int total = 0;
        int term = 1;
        for (int c = 0; c < polynomial.length(); ++c) {
            char ch = polynomial.charAt(c);
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

    private static Main buildMain(Scanner scanner) {
        Main m = new Main();

        m.polynomial = scanner.next();
        m.k = scanner.nextInt();
        m.d = scanner.nextInt();
        m.dictWords = new ArrayList<>();
        for (int i = 0; i < m.d; ++i) {
            m.dictWords.add(scanner.next());
        }
        log.info("k {} d {} poly {}", m.k, m.d, m.polynomial);

        return m;
    }

    public Main() {

        // TODO Add test case vars
        super();
    }

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
        }
        log.info("Input file {}", args[0]);

        Scanner scanner = new Scanner(new File(args[0]));

        int t = scanner.nextInt();

        for (int i = 1; i <= t; ++i) {

            handleCase(i, scanner, System.out);

        }

        scanner.close();
    }
}