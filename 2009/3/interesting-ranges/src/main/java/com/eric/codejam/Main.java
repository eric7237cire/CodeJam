package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        Main m = Main.buildMain(scanner);

        log.info("Starting case {}", caseNumber);

        int r = BruteForce.count(m.L,  m.R);
        os.println("Case #" + caseNumber + ": " + r);
        int palin = 10;
        BruteForce.countPalin(palin, palin * 100 - 1);
        /*
        os.println("1-9 " + BruteForce.countPalin(1, 9));
        os.println(BruteForce.countPalin(10, 99));
        os.println("100-999 " + BruteForce.countPalin(100, 999));
        os.println(BruteForce.countPalin(1000, 9999));
        
        os.println(BruteForce.countPalin(100, 9999));
        
        os.println(BruteForce.countPalin(10000, 99999));
        os.println("100,000-999999 " + BruteForce.countPalin(100000, 999999));

        os.println(BruteForce.countPalin(1000000, 9999999));
        */
    }

    int L;
    int R;
    private static Main buildMain(Scanner scanner) {
        // TODO build main from input
        Main m = new Main();
        m.L = scanner.nextInt();
        m.R = scanner.nextInt();

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