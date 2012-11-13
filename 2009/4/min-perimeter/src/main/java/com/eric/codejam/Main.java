package com.eric.codejam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.geometry.PointLong;

public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static String handleCase(int caseNumber, InputData input) {

        
        
        log.info("Starting case {}", caseNumber);
        

       
        
        log.info("Done processing input case {}", caseNumber);
        
        double ans = DivideConq.findMinPerimTriangle(input.points);

        log.info("Done calculating answer case {}", caseNumber);
        
        DecimalFormat decim = new DecimalFormat("0.00000000000");
        decim.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        
        return ("Case #" + caseNumber + ": " + decim.format(ans));
                

    }
    
    static class InputData {
        List<PointInt> points;
    }
    static InputData readInput(Scanner scanner) {
        List<PointInt> points = new ArrayList<>();
        
    
        int n = scanner.nextInt();
        log.info("Reading data...");
        
        for (int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            points.add(new PointInt(x, y));
        }
        
        InputData  i = new InputData();
        i.points = points;
        return i;
        
    }
    
    static InputData readInput(BufferedReader br) throws IOException {
        List<PointInt> points = new ArrayList<>();
    
        String line = br.readLine();
        int n = Integer.parseInt(line);
        
        /*
        Scanner scanner = new Scanner(input.rawPointData);
        for(int i = 0; i < input.numPoints; ++i) {
        int x = scanner.nextInt();
        int y = scanner.nextInt();
        input.points.add(new PointLong(x, y));
        }*/
        
        log.info("Reading data...");
        for (int i = 0; i < n; ++i) {
            line = br.readLine();
            StringTokenizer st = new StringTokenizer(line);
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            points.add(new PointInt(x,y));
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

        //
        
        InputStreamReader isr = new InputStreamReader(new FileInputStream(new File(args[0])));
        final BufferedReader br = new BufferedReader(isr);
        //final Scanner scanner = new Scanner(br);

        String line = br.readLine();
        
        final int t = Integer.parseInt(line);
        
        OutputStream os = new FileOutputStream(args[0] + ".out");
        PrintStream pos = new PrintStream(os);

        final String[] answers = new String[t];
        final InputData[] input = new InputData[t];
        
        for (int i = 0; i < t; ++i) {  
            
           // input[i] = readInput(scanner);
        }
        //scanner.close();
        
        final int THREADS = 2;
        test = 0;
        Thread[] threads = new Thread[THREADS];
        
        for (int i = 0; i < threads.length; i++) {
            // final B inst = new B();
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        int ltest = 0;
                        synchronized (answers) {
                            ltest = test;
                            test++;
                            if (ltest >= t) {
                                return;
                            }
                            try {
                            input[ltest] = readInput(br);
                            } catch (IOException ex) {
                                log.error("IO", ex);
                            }
                        }
                        long t = System.currentTimeMillis();
                        String ans = null;
                        try {
                            ans = handleCase(ltest + 1, input[ltest]);
                            input[ltest] = null;
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        synchronized (answers) {
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
        //scanner.close();
        br.close();
    }
}