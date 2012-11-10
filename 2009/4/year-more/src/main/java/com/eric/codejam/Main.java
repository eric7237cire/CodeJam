package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        Main.buildMain(scanner);

        log.info("Starting case {}", caseNumber);

        os.println("Case #" + caseNumber + ": ");

    }
    
    public static class Tournament {
        int rounds;
        int[] roundDays;
    }
    
    private class Block {
        
    }

    private static void buildMain(Scanner scanner) {
        
        int N = scanner.nextInt();
        int T = scanner.nextInt();
        
        List<Tournament> tournaments = new ArrayList<>();
        for(int i = 0; i < T; ++i) {
            Tournament t = new Tournament();
            t.rounds = scanner.nextInt();
            t.roundDays = new int[t.rounds];
            t.roundDays[0] = 1;
            for(int j =1; j < t.rounds; ++j) {
                t.roundDays[j] = scanner.nextInt();
            }
            tournaments.add(t);
        }
        
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