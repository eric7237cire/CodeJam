package com.eric.codejam;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class Main {

	
	
	

	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {
		final int numCells = scanner.nextInt();
		final int numToBeFreed = scanner.nextInt();

		


		List<Integer> listToBeFree = new ArrayList<>();
		for (int i = 0; i < numToBeFreed; ++i) {
			int index = scanner.nextInt() - 1;

			listToBeFree.add(index);
			
		}

		PrisonSelectionAlgorithm alg = new ChooseLargestSegment();
		int cost = alg.findMinCost(0, numCells - 1, listToBeFree);

		log.info("Case #" + caseNumber + ": " + cost);
		//os.println("Case #" + caseNumber + ": " + cost);
		
		alg = new BruteForce();
		cost = alg.findMinCost(0, numCells - 1, listToBeFree);
		
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