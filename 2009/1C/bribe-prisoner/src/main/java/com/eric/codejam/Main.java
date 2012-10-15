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

	private static class PrisonCell {
		private int prisonersLeftToBeReleased;
		private int prisonersRightToBeReleased;

		private int occupiedCellsLeft;
		private int occupiedCellsRight;

		private int index;

		private boolean freed;

		

		public PrisonCell(int prisonersLeftToBeReleased,
				int prisonersRightToBeReleased, int occupiedCellsLeft,
				int occupiedCellsRight, int index) {
			super();
			this.prisonersLeftToBeReleased = prisonersLeftToBeReleased;
			this.prisonersRightToBeReleased = prisonersRightToBeReleased;
			this.occupiedCellsLeft = occupiedCellsLeft;
			this.occupiedCellsRight = occupiedCellsRight;
			this.index = index;
			this.freed = false;
		}



		@Override
		public String toString() {
			return "PrisonCell [prisonersLeft=" + prisonersLeftToBeReleased
					+ ", prisonersRight=" + prisonersRightToBeReleased + ", cellsLeft="
					+ occupiedCellsLeft + ", cellsRight=" + occupiedCellsRight + ", index="
					+ index + ", freed=" + freed + "]";
		}
		
		

	}
	
	private static int findMinCost(final int segStart, final int segEnd, List<Integer> toBeFreed) {
		int minCost = Integer.MAX_VALUE;
		
		log.debug("findMinCost {} {} {}", segStart, segEnd, toBeFreed);
		if (segEnd <= segStart) {
			return 0;
		}
		
		if (toBeFreed.size() == 0) {
			return 0;
		}
		
		if (toBeFreed.size() == 1) {
			return segEnd - segStart ;
		}
		
		for(int i = 0; i < toBeFreed.size(); ++i) {
			int prisIdx = toBeFreed.get(i);
			
			Preconditions.checkState(! (prisIdx > segEnd || prisIdx < segStart));
				
			int cost = segEnd - segStart;
			
			if (i > 0) {
			List<Integer> cpy = new ArrayList<>(toBeFreed);
			cpy = cpy.subList(0, i);			
			cost += findMinCost(segStart, prisIdx - 1, cpy);
			}
			
			if (i < toBeFreed.size() - 1) {
				List<Integer>  cpy = new ArrayList<>(toBeFreed);
			cpy = cpy.subList(i+1,  toBeFreed.size());
			cost += findMinCost(prisIdx + 1, segEnd, cpy);
			}
			
			
			minCost = Math.min(minCost,  cost);
			
		}
		
		
		log.debug("findMinCost return {} params {} {} {}", minCost, segStart, segEnd, toBeFreed);
		return minCost;
	}

	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {
		final int numCells = scanner.nextInt();
		final int numToBeFreed = scanner.nextInt();

		

		List<PrisonCell> listeCells = new ArrayList<>();

		for (int i = 0; i < numToBeFreed; ++i) {
			int index = scanner.nextInt() - 1;

			listeCells.add(new PrisonCell(i, numToBeFreed - i - 1, index,
					numCells - 1 - index, index));
		}

		int numFreed = 0;
		int cost  = 0;
		
		while (numFreed < listeCells.size()) {
			int minBenefit = Integer.MAX_VALUE;
			PrisonCell nextCell = null;
			int maxIndex = -1;

			for (int i = 0; i < listeCells.size(); ++i) {
				PrisonCell pc = listeCells.get(i);
				
				if (pc.freed) {
					continue;
				}
				
				int benefit = Math.max(pc.occupiedCellsLeft, pc.occupiedCellsRight);
						
				log.debug("Iterating {} benefit {}", pc, benefit);
				
				if (benefit < minBenefit) {
					minBenefit = benefit;
					nextCell = pc;
					maxIndex = i;
				}
			}
			
			log.info("Freeing {}", nextCell);

			// take care of rhs
			for (int i = maxIndex + 1; i < listeCells.size(); ++i) {
				PrisonCell pc = listeCells.get(i);
				if (pc.freed) {
					break;
				}
				pc.occupiedCellsLeft -= (1 + nextCell.occupiedCellsLeft);
				pc.prisonersLeftToBeReleased -= 1;
			}

			for (int i = maxIndex - 1; i >= 0; --i) {
				PrisonCell pc = listeCells.get(i);
				if (pc.freed) {
					break;
				}
				pc.occupiedCellsRight -= (1 + nextCell.occupiedCellsRight);
				pc.prisonersRightToBeReleased -= 1;
			}
			
			nextCell.freed = true;
			cost += nextCell.occupiedCellsLeft + nextCell.occupiedCellsRight;
			
			++numFreed;
		}

		log.info("Case #" + caseNumber + ": " + cost);
		//os.println("Case #" + caseNumber + ": " + cost);
		
		List<Integer> listToBeFree = new ArrayList<>();
		for (PrisonCell pc : listeCells) {
			listToBeFree.add(pc.index);
		}
		cost = findMinCost(0, numCells - 1, listToBeFree);
		
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