package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.genetics.PermutationChromosome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.utils.Permutations;

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

        log.info("Starting case {}", caseNumber);

        os.println("Case #" + caseNumber + ": " + StringUtils.join(total, " "));

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
                
                total += evalP(word);

                total %= 10009;
                
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
                log.info("Perm {} word {} total {}", (Object) combin, word, total);
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