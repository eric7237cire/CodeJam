package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    
    final List<List<Integer> > stocks; //false #  true .
    final int n;
    final int k;
        
    int iterations;
    
   

    public static void handleCase(int caseNumber, Scanner scanner,
            PrintStream os) {

    	Main m = Main.buildMain(scanner);
        
        
        log.info("Starting case {}", caseNumber);
        
        
        os.println("Case #" + caseNumber + ": No");
        
        log.info("Finished Starting case {}.  Iterations {}", caseNumber, m.iterations);
    }


   private static Main buildMain(Scanner scanner) {
	   Main m = new Main(scanner.nextInt(), scanner.nextInt());
	   
	   for(int n = 0; n < m.n; ++n) {
		   List<Integer> values = new ArrayList<>();
		   for(int k = 0; k < m.k; ++k) {
			   int value = scanner.nextInt();
			   values.add(value);
		   }
		   m.stocks.add(values);
	   }
	   
	   return m;
   }

	public Main(int n, int k) {
		super();
		this.n = n;
		this.k = k;
		this.stocks = new ArrayList<>();
	}




	final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String args[]) throws Exception {

        if (args.length < 1) {
            args = new String[] { "sample.txt" };
        }
        log.info("Input file {}", args[0]);

        Scanner scanner = new Scanner(new File(args[0]));

       // OutputStream os = new FileOutputStream("output.txt");

      //  PrintStream pos = new PrintStream(os);

        int t = scanner.nextInt();

        for (int i = 1; i <= t; ++i) {

            handleCase(i, scanner, System.out);

        }

        scanner.close();
    }
}