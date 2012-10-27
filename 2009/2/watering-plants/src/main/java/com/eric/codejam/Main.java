package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


public class Main {

	Display d;
	
	public void display() {
		d = new Display(600, 600);
		
		for(Circle p : plants) {
			d.addCircle(p);
		}
		
		for(Circle s : sprinklers) {
			d.addCircle(s);
		}
		
		d.setVisible(true);
	}

	
	final static int ITERATION_PRINT = 500000;
	
	int n;
	List<Circle> plants;
	
	Circle[] sprinklers;
	
	
	public double bruteForce() {

		int iterations = 0;

		if (n == 1) {
			sprinklers = new Circle[] { plants.get(0) };
			return plants.get(0).getR();
		}

		if (n == 2) {
			sprinklers = new Circle[] { plants.get(0), plants.get(1) };
			return Math.max(plants.get(0).getR(), plants.get(1).getR());
		}

		Map<Circle, List<Integer>> plantsCovered = new HashMap<>();
		List<List<Sprinkler>> sizeToSprinkler = new ArrayList<>();
		for (int i = 0; i < n; ++i) {
			sizeToSprinkler.add(new ArrayList<Sprinkler>());
		}
		
		Set<Long> ignoreK3 = new HashSet<>();

		// all pairs
		for (int k = 2; k <= 3; ++k) {

			Iterator<Long> it = new CombinationsIterator(n, k);

			while (it.hasNext()) {
				final long combin = it.next();

				if (k==3 && ignoreK3.contains(combin)) {
					continue;
				}
				List<Circle> chosenCircles = new ArrayList<>();
				
				for (int chosen = 0; chosen < n; ++chosen) {
					if ((1L << chosen & combin) != 0) {
						chosenCircles.add(plants.get(chosen));
					}
				}

				Preconditions.checkState(chosenCircles.size() == k);

				Circle sprinkler = k == 2 ? Circle.getCircleContaining(
						chosenCircles.get(0), chosenCircles.get(1)) : Circle
						.getCircleContaining(chosenCircles.get(0),
								chosenCircles.get(1), chosenCircles.get(2));

				List<Integer> plantsInside = new ArrayList<>();
				long plantsInsideBitSet = 0L;
				int plantsCount = 0;

				for (int plant = 0; plant < n; ++plant) {
					if (sprinkler.contains(plants.get(plant))) {
						plantsInside.add(plant);
						plantsInsideBitSet |= (1L << plant);
						plantsCount++;
						
						if (k==2 && (1L << plant & combin) == 0) {
							ignoreK3.add(1L << plant | combin);
						}
					}
				}

				++iterations;
				if (iterations % ITERATION_PRINT == 0) {
					log.debug("Iterations {} c k {}", iterations, k);
				}

				Preconditions.checkState(plantsInside.size() >= k);
				plantsCovered.put(sprinkler, plantsInside);
				sizeToSprinkler.get(plantsCount - 1).add(
						new Sprinkler(sprinkler.getR(), plantsInsideBitSet, sprinkler));

			}

		}

		for (int i = 0; i < n; ++i) {
			Collections.sort(sizeToSprinkler.get(i));
		}

		double minRadius = Double.MAX_VALUE;

		long goal = (1L << n) - 1;

		for (int sizeOuter = 1; sizeOuter <= n; ++sizeOuter) {

			for (Sprinkler outer : sizeToSprinkler.get(sizeOuter-1)) {

				if (sizeOuter >= n - 1 && outer.r < minRadius) {
					minRadius = outer.r;
					sprinklers = new Circle[] { outer.circle, outer.circle };
					continue;
				}

				if (outer.r >= minRadius) {
					continue;
				}

				int sizeNeeded = n - sizeOuter;

				for (int size = sizeNeeded; size <= n; ++size) {
					for (Sprinkler inner : sizeToSprinkler.get(size-1)) {
						++iterations;
						if (iterations % ITERATION_PRINT == 0) {
							log.debug("Iterations {} choose plants", iterations);
						}

						if (inner.r > outer.r) {
							break;
						}

						long combined = outer.plantsCovered
					| inner.plantsCovered;

						if (combined != goal) {
							continue;
						}

						Preconditions.checkState(outer.r <= minRadius);
						minRadius = outer.r;
						sprinklers = new Circle[] { outer.circle, inner.circle };
						break;

					}
				}
			}
		}

		return minRadius;
	}
	
	

	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {

		Main m = Main.buildMain(scanner);

		double min = m.bruteForce();

		log.info("Starting case {}", caseNumber);
		
		DecimalFormat df = new DecimalFormat("0.######");
		df.setRoundingMode(RoundingMode.HALF_UP);
		df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

		os.println("Case #" + caseNumber + ": " + df.format(min));
		
	
		/*
		m.display();
		m.d.toFront();

		log.info("Finished Starting case {}.  ", caseNumber
				);
		
		m.d.setVisible(false);
		m.d.dispose();*/
	}

	private static Main buildMain(Scanner scanner) {
		Main m = new Main(scanner.nextInt());


		for(int i = 0; i < m.n; ++i) {
			m.plants.add(new Circle(scanner.nextInt(),scanner.nextInt(),scanner.nextInt()));
		}

		return m;
	}

	public Main(int n) {
		super();

		this.n = n;
		
		this.plants = new ArrayList<>();

	}

	final static Logger log = LoggerFactory.getLogger(Main.class);

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