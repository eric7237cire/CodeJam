package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.math.IntMath;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    /**
     * 10 ^ exp  to num*10^exp
     * exp = 3
     * 1000 to 9999
     * @param num
     * @param exp
     * @return
     */
    static Interval getNumRanges(int num, int exp) {
        if (exp < 2) {
            return exp == 1 ?
                    BruteForce.createInterval(10, 10*num+9)
                    : BruteForce.createInterval(1, num);
        }
        int totalPalinExp = exp / 2; // 9 * 10 ^ totalPalin
        
        int totalPerNum = IntMath.pow(10, totalPalinExp);
        
        int numBetween = exp % 2 == 0 ? Integer.parseInt(StringUtils.repeat('9', exp / 2), 10)
                : Integer.parseInt("10" +  StringUtils.repeat('9', exp / 2));
        
        int numBetweenExcep = Integer.parseInt("10" + StringUtils.repeat('9', exp / 2 - 1));
        
        Interval beforeFirstPalin = new Interval(10);
        beforeFirstPalin.left = IntMath.pow(10, exp);
        beforeFirstPalin.right = beforeFirstPalin.left;
        
        Interval palin = new Interval(1);
        
        Interval empties = Interval.createEmpty(numBetween);
        Interval emptiesTenth = Interval.createEmpty(numBetweenExcep);
        
        Interval total = beforeFirstPalin;
        
        for(int n = 1; n < num; ++n) {
            //Get to first palin  1001 / 3003 / 9009 etc
            total = Interval.combin(total, Interval.createEmpty(n-1));
            
            //Add first palin
            total = Interval.combin(total, palin);
            
            int t = 1; //already added 1
            while(t < totalPerNum ) {
                if ( t>10 && (t-1)%10 == 0) { //t = 11 / 21 / 31 put the 10th empties
                    total = Interval.combin(total, emptiesTenth);
                } else {
                    total = Interval.combin(total, empties);
                }
                ++t;
                total = Interval.combin(total, palin);
                Preconditions.checkState(BruteForce.isPalin(total.right));
            }
            
            //Add the empty space to get to next round #
            
            BigInteger spaceNeeded = BigInteger.valueOf(n).add(BigInteger.ONE).multiply(BigInteger.TEN.pow(exp));
            spaceNeeded = spaceNeeded.subtract( total.right );
            
            total = Interval.combin(total, Interval.createEmpty(spaceNeeded.intValue()));
            
            
        }
        
        
        return total;
        
    }

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

        Main m = Main.buildMain(scanner);

        log.info("Starting case {}", caseNumber);

        int r = BruteForce.countTotal(m.L,  m.R, true);
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