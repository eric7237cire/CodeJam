package com.eric.codejam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.geometry.PointLong;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static String handleCase(int caseNumber, InputData input) {

        double ans = DivideConq.findMinPerimTriangle(input.points);
        
        log.info("Starting case {}", caseNumber);

        DecimalFormat decim = new DecimalFormat("0.00000000000");
        decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + decim.format(ans));
                

    }
    
    static class InputData {
        List<PointLong> points;
    }
    static InputData readInput(Scanner scanner) {
        List<PointLong> points = new ArrayList<>();
        
    
        int n = scanner.nextInt();

        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            points.add(new PointLong(x, y));
        }
        
        InputData  i = new InputData();
        i.points = points;
        return i;
        
    }



    public Main() {

        // TODO Add test case vars
        super();
    }

    static int test = 0;
    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
           //args = new String[] { "sample.txt" };
           //args = new String[] { "smallInput.txt" };
           args = new String[] { "largeInput.txt" };
        }
        log.info("Input file {}", args[0]);

        final Scanner scanner = new Scanner(new File(args[0]));

        final int t = scanner.nextInt();
        
        OutputStream os = new FileOutputStream(args[0] + ".out");
        PrintStream pos = new PrintStream(os);

        final String[] answers = new String[t];
        final InputData[] input = new InputData[t];
        
        for (int i = 0; i < t; ++i) {  
            input[i] = readInput(scanner);
        }
        scanner.close();
        
        final int THREADS = 2;
        test = 0;
        Thread[] threads = new Thread[THREADS];
        
        for (int i = 0; i < threads.length; i++)
        {
            //final B inst = new B();
            threads[i] = new Thread(new Runnable()
                {
                public void run()
                                        {
                    while (true)
                                            {
                        int ltest = 0;
                        synchronized (scanner)
                                                {
                            ltest = test;
                            test++;
                            if (ltest >= t)
                                                    {
                                return;
                            }
                        }
                        long t = System.currentTimeMillis();
                        String ans = null;
                        try
                        {
                            ans = handleCase(ltest+1, input[ltest]);
                        } catch (Throwable e)
                                                {
                            e.printStackTrace();
                        }
                        synchronized (answers)
                                                {
                            System.err.println(ltest + " "
                                + (System.currentTimeMillis() - t));
                            answers[ltest] = ans;
                        }
                    }
                }
            });
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++)
        {
            threads[i].join();
        }
        for (int test = 1; test <= t; test++)
        {
            pos.println(answers[test - 1]);
        }
        /*
        for (int i = 1; i <= t; ++i) {  
            
            handleCase(i, scanner, pos);
        }*/

        log.info("Finished");
        
        os.close();
        scanner.close();
    }
}