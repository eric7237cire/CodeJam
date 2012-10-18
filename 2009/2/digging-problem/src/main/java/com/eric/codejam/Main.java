package com.eric.codejam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ComparisonChain;

public class Main {
    
    boolean [][] grid;
    
    static class GridState implements Comparable<GridState>{
        final int depth;
        List<Boolean> currentRow;
        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
        public int compareTo(GridState o) {
            return ComparisonChain.start()
                    .compare(leftOne, o.leftOne)
                    .compare(rightOne, o.rightOne).result();
        }
        
        public Row(String s) {
            boolean hasOne = -1 != s.indexOf('1');
            
            leftOne = hasOne ? s.indexOf('1') : 0;
            rightOne = hasOne ? s.lastIndexOf('1') : 0;
            
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "(" + leftOne + ", " + rightOne + ")";
        }
        
        
    }

    static int findMin(int listeProg, List<Row> liste) {
        Integer bottomRowIndex = null;
        Row  bottomRow = null;
        
       
        log.debug("findMin {}", liste);

        for (int i = 0; i < liste.size(); ++i) {
            Row r = liste.get(i);

            if (bottomRow == null) {
                
                if (r.rightOne > i + listeProg ) {
                    bottomRow = r;
                    bottomRowIndex = i;
                    continue;
                }
            } else {

                if (r.rightOne <= bottomRowIndex + listeProg) {
                    List<Row> copyListe = new ArrayList<>(liste);
                    //Collections.swap(copyListe, bottomRowIndex, i);
                    Row rem = copyListe.remove(i);
                    //copyListe.add(0,  rem);

                    log.debug("findMin {} new liste {}", liste, copyListe);

                    return  i - bottomRowIndex + findMin(listeProg + 1, copyListe);

                    //minCost = Math.min(minCost, cost);
                }
            }

        }

        if (bottomRow == null) {
            return 0;
        }

        log.debug("findMin {} returns {}", liste, 0);
        
        return 0;
    }

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        int dimension = scanner.nextInt();

        List<Row> liste = new ArrayList<>();

        for (int i = 0; i < dimension; ++i) {
            liste.add(new Row(scanner.next()));
        }

        int cost = findMin(0, liste);
        log.info("Real Case #" + caseNumber + ": " + cost);
        os.println("Case #" + caseNumber + ": " + cost);
    }

    Main() {

    }

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
        }
        log.info("Input file {}", args[0]);

        Scanner scanner = new Scanner(new File(args[0]));

        OutputStream os = new FileOutputStream("output.txt");

        PrintStream pos = new PrintStream(os);

        int t = scanner.nextInt();

        for (int i = 1; i <= t; ++i) {

            handleCase(i, scanner, pos);

        }

        scanner.close();
    }
}