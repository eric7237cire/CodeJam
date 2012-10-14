package com.eric.codejam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
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
			
			log.info("Dimension {} coord {} velocity {}", new Object[] { j,
					coordsAvg[j], velocityAvg[j] });
		}

		os.println("Case #" + caseNumber + ": ");
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