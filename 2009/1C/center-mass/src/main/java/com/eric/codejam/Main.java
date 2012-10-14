package com.eric.codejam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {
		int numFireflies = scanner.nextInt();

		double[] coordsAvg = new double[3];
		int[] coords = new int[3];

		double[] velocityAvg = new double[3];
		int[] velocity = new int[3];

		for (int i = 0; i < numFireflies; ++i) {
			for (int j = 0; j < 3; ++j) {
				coords[j] = scanner.nextInt();				
				coordsAvg[j] += coords[j];
				log.debug("ff {} coords {} cord avg {}", new Object[] {i, coords[j], coordsAvg[j] } );
			}
			for (int j = 0; j < 3; ++j) {
				velocity[j] = scanner.nextInt();
				velocityAvg[j] += velocity[j];
			}
		}

		for (int j = 0; j < 3; ++j) {
			coordsAvg[j] = coordsAvg[j] / (double) numFireflies;
			velocityAvg[j] = velocityAvg[j] / (double) numFireflies;
			
			log.debug("Dimension {} coord {} velocity {}",  j,coordsAvg[j], velocityAvg[j] );
		}
		
		//find where derivative of distance formula is 0
		
		double a = 0;
		double b = 0;

		double vSum = 0;
		for (int j = 0; j < 3; ++j) { 
			a += 2 * coordsAvg[j] * velocityAvg[j];
			b += 2 * velocityAvg[j] * velocityAvg[j] ;
			vSum += velocityAvg[j];
		}
		double t = 0;
		
		if (b != 0) {
			t = -a / b;
		}
		
		log.info("caseNumber {} is {} a {} b {}", caseNumber, t, a, b);
		if (t < 0) {
			//t = 0;
			for (int j = 0; j < 3; ++j) {
				log.info("CN {} Dimension {} coord {} velocity {}", caseNumber, j,coordsAvg[j], velocityAvg[j] );
			}
			t = 0;
		}
		
		double d = 0;
		
		for (int j = 0; j < 3; ++j) {
			d += (coordsAvg[j] + velocityAvg[j] * t)* (coordsAvg[j] + velocityAvg[j] * t);
		}
		
		d = Math.sqrt(d);
		
		log.info("D is " + d);
		
		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(Locale.US);
		NumberFormat df = new DecimalFormat("#.######", dfs);
		os.println("Case #" + caseNumber + ": " + df.format(d) + " " + df.format(t));
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