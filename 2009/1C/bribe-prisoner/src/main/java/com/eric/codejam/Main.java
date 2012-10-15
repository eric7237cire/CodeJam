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

public class Main {
	
	private static class PrisonCell {
		private int prisonersLeft;
		private int prisonersRight;
		
		private int cellsLeft;
		private int cellsRight;
		
		private int index;
		private int maxIndex;
		private int minIndex;
		
		private boolean freed;

		public PrisonCell(int prisonersLeft, int prisonersRight, int cellsLeft,
				int cellsRight, int index 
				) {
			super();
			this.prisonersLeft = prisonersLeft;
			this.prisonersRight = prisonersRight;
			this.cellsLeft = cellsLeft;
			this.cellsRight = cellsRight;
			this.index = index;
			this.freed = false;
		}

		
		
	}

	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {
		int numPrisoners = scanner.nextInt();
		final int numToBeFreed = scanner.nextInt();

		int prisonersLeft = 0;
		int prisonersRight = numPrisoners - 1;
		
		List<PrisonCell> listeCells = new ArrayList<>();
		
		for(int i = 0; i < numToBeFreed; ++i) {
			int index = scanner.nextInt() - 1;
			
			listeCells.add(new PrisonCell(prisonersLeft, prisonersRight, index, numToBeFreed - 1 - index, index));
		}
		
		for(int i = 0; i < listeCells.size(); ++i) {
			
		}
		
		
		os.println("Case #" + caseNumber + ": " );
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