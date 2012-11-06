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