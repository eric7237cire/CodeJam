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

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        Main m = Main.buildMain(scanner);

        List<Integer> total = m.doPerms();

        List<Integer> total2 = m.usePoly();

        log.info("Starting case {}\n total {}\n total poly {}", caseNumber,
                total, total2);

        os.println("Case #" + caseNumber + ": " + StringUtils.join(total, " "));

    }

    public List<Integer> usePoly() {
        List<Integer> totals = new ArrayList<>();

        Cloner c = new Cloner();

        Map<String, Integer> values = new HashMap<>();

        Polynomial totalPoly = new Polynomial();
        for (int i = 0; i < d; ++i) {
            Polynomial p = new Polynomial(polynomial);
            for (int chInt = 'a'; chInt <= 'z'; ++chInt) {
                char ch = (char) chInt;
                String varName = "" + ch + "_" + i;
                String xVarName = "" + ch + "_x" ;
                
                p.substitute(new VariableTerm("" + ch), new BinomialTerm(
                        new VariableTerm(varName), new VariableTerm(xVarName)));
                values.put(
                        varName,
                        StringUtils.countMatches(dictWords.get(i), ""
                                + (char) chInt));
                values.put(xVarName, 0);
            }

            p.doSimplify();

            totalPoly.add(p);
        }

        totals.add(totalPoly.evaluate(values));

        Polynomial orig = c.deepClone(totalPoly);
        for (int eachK = 2; eachK <= k; ++eachK) {

            totalPoly = new Polynomial();

            for (int i = 0; i < d; ++i) {

                Polynomial p = c.deepClone(orig);

                Map<VariableTerm, Term> subs = new HashMap<>();
                for (int chInt = 'a'; chInt <= 'z'; ++chInt) {
                    char ch = (char) chInt;
                    String xVarName = "" + ch + "_x" ;
                    subs.put(new VariableTerm(xVarName), new BinomialTerm(
                            new VariableTerm(xVarName), new VariableTerm(ch
                                    + "_" + i)));

                }
                log.info("Computing k {} before sub {}", eachK, p);
                p.substitute(subs);
                log.info("Computing k {} after sub {}", eachK, p);
                totalPoly.add(p);
                
                log.info("Computing k {} after add {}", eachK, totalPoly);
                totalPoly.doSimplify();

                log.info("Computing k {} after simplify {}", eachK, totalPoly);
                
            }

            totalPoly.doSimplify();

            log.info("Poly obj {} k {}", totalPoly, eachK);
            
            orig = c.deepClone(totalPoly);

            totals.add(totalPoly.evaluate(values));
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

            /*
             * Permutations<Integer> perm = Permutations.create(dictArray,
             * combin, eachK);
             */

            while (true) {

                String word = "";
                for (Integer comIndex : combin) {
                    word += dictWords.get(comIndex) + " ";
                }

                final int wordEval = evalP(word);
                total += wordEval;

                total %= 10009;

                List<Integer> p_counts = new ArrayList<>();
                p_counts.add(StringUtils.countMatches(word, "a"));
                // p_counts.add(StringUtils.countMatches(word, "b"));

                log.info("Perm {} p({}) {} total {}", (Object) combin,
                        p_counts, wordEval, total);

                boolean fullLoop = true;
                for (int pos = 0; pos < eachK; ++pos) {
                    combin[pos]++;
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