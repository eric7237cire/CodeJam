package com.eric.codejam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.InputData;
import com.eric.codejam.Main;
import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.main.Runner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.eric.codejam.multithread.Producer.TestCaseInputReader;
import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class Main implements TestCaseHandler<InputData>, TestCaseInputReader<InputData> {

    final static Logger log = LoggerFactory.getLogger(Main.class);
    
    @Override
    public String handleCase(int caseNumber, InputData input) {

        log.info("Starting calculating case {}", caseNumber);
        
        //double ans = DivideConq.findMinPerimTriangle(input.points);
        
        long count = 0;
        int losing = 0;
        int lastWon = 1;
        
        int[] losingLB = new int[1000000];
        int[] losingUP = new int[1000000];
        
        losingLB[0] = 1; losingUP[0] = 1;
        List<Range<Integer>> ranges = new ArrayList<>();
        
        int lastUpperBound = 1;
        int lastLowerBound = 1;
        ranges.add(Ranges.closed(1, 1));
        for(int i = 1; i<1000000; ++i ) {
            int n = i + 1;
            if (losingUP[lastLowerBound-1] < n ) {
                ++lastLowerBound;
            }
            losingLB[i] = lastLowerBound;
            losingUP[i] = lastLowerBound + n - 1;
            ranges.add(Ranges.closed(losingLB[i], losingUP[i]));
            lastUpperBound = lastLowerBound + n - 1;
           // log.debug(" for n {}  losing between {} and {}", n, losingLB[i], losingUP[i]);
        }
        
        Range<Integer> bRange = Ranges.closed(input.B1, input.B2);
        
        for(int a = input.A1; a <= input.A2; ++a) {
            
            count += bRange.upperEndpoint() - bRange.lowerEndpoint() + 1;
            
            Range<Integer> losingRange = ranges.get(a-1);
            Preconditions.checkState(losingRange != null);
            if (losingRange.isConnected(bRange)) {
            Range<Integer> inter = bRange.intersection(losingRange);
            count -= (inter.upperEndpoint() - inter.lowerEndpoint() + 1);
            }
           //for(int b = lastWon; b <= input.B2; ++b) {
            //for(int b = input.B1; b <= input.B2; ++b) {
               //if (b < a) 
                 //  continue;
      //  for(int a = 1; a <= 1000000; ++a) {
       //       for(int b = a; b <= 1000000; ++b) {
                
                /*
                if (isWinning(a,b)) {
                    log.debug("A {} B {} is winning diff {}", a, b, b - lastWon);
                    lastWon = b;
                    ++count;
                    //break;
                } else {
                    log.debug("A {} B {} is losing", a, b);
                    ++losing;
                }*/
            //}
        }

        log.info("Done calculating answer case {}.  w {}/losing {} total {}", caseNumber, count, losing, count+losing);
        
        //DecimalFormat decim = new DecimalFormat("0.00000000000");
        //decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + count );
    }
    
    public boolean isWinning(int A, int B) {
        
        
        
        int G = Math.max(A, B);
        int L = Math.min(A, B);
        
        if (G==L) {
            return false;
        }
        
        if (G % L == 0) 
            return true;
        
        boolean ret = false;
        
        int notFlexCount = 0;
        while(L > 0) {
            boolean flexNode = false;
            if (G - L >= L) {
                flexNode = true;
            }
        
            if (!flexNode) {
                notFlexCount ++;
            } else {
                return notFlexCount % 2 == 0;
            }
            //log.debug("G {} L {} flexible? {}", G, L, flexNode);
            int newG = L;
            L = G % L;
            G = newG;
            
             G = Math.max(G, L);
             L = Math.min(G, L);
        }
        log.debug("\n\n");
        
            return notFlexCount % 2 == 0;
        
        /*
        while(A > 0 && B > 0) {
            ++ count;
            if (A > B) {
                int mult = A / B;
                if (A % B == 0) {
                    mult --;
                }
                A = A - mult * B;
            } else if (B > A) {
                int mult = B / A;
                if (B % A == 0) {
                    mult --;
                }
                B = B - mult * A;
            } else {
                A = 0;
               
            }
            
            log.debug("a {} b {} count {}", A,B,count);
        }
        
        if  (count % 2 == 0) {
            return true;
        }
        return false;*/
    }
    
    
    @Override
    public InputData readInput(BufferedReader br, int testCase) throws IOException {
        
    
        String[] line = br.readLine().split(" ");
        
        InputData  input = new InputData(testCase);
        input.A1 = Integer.parseInt(line[0]);
        input.A2 = Integer.parseInt(line[1]);
        input.B1 = Integer.parseInt(line[2]);
        input.B2 = Integer.parseInt(line[3]);
        
        return input;
        
    }

    


    public Main() {
        super();
    }
    
    
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
         //  args = new String[] { "sample.txt" };
            args = new String[] { "C-small-practice.in" };
//            args = new String[] { "B-large-practice.in" };
         }
         log.info("Input file {}", args[0]);

         Main m = new Main();
         Runner.goSingleThread(args[0], m, m);
         //Runner.go(args[0], m, m, new InputData(-1));

        
       
    }

    
}