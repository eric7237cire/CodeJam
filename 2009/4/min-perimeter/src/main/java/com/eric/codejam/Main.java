package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.geometry.PointLong;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        double ans = Main.buildMain(scanner);

        log.info("Starting case {}", caseNumber);

        DecimalFormat decim = new DecimalFormat("0.00000000000");
        decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        os.println("Case #" + caseNumber + ": " + decim.format(ans));

    }

    private static double buildMain(Scanner scanner) {
        
        int n = scanner.nextInt();
        
        List<PointLong> points = new ArrayList<>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            points.add(new PointLong(x,y));
        }

        return BruteForce.minPerimUsingSort(points);
        //return BruteForce.minPerimUsingDiff(points);
        //return BruteForce.minPerim(points);
    }

    public Main() {

        // TODO Add test case vars
        super();
    }

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           //args = new String[] { "sample.txt" };
           args = new String[] { "smallInput.txt" };
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