package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {


	int iterations ;
	
	int n;
	List<Circle> plants;
	
	
	private void pickSplinkerLocation() {
		
	}
	
	/*
	 * Line from center through plant circle.  Find furthest point(s).
	 * 
	 * If 1, move center closer to that point
	 * If 2 or 3, if 180 then done, otherwise move towards them both (line between 2 more extreme points, halfway)
	 * 
	 * Make a triangle, if centre circle inside, then the points are not on the same side
	 */
	private void findFurthestIntersectionPoints() {
		
	}
	
	/*
	 * Take center, find most extreme point away from this center
	 */
	private void intersectionPointCircle() {
		
	}

	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {

		Main m = Main.buildMain(scanner);

		int min = 5;

		log.info("Starting case {}", caseNumber);

		os.println("Case #" + caseNumber + ": " + min);

		log.info("Finished Starting case {}.  Iterations {}", caseNumber,
				m.iterations);
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

		// OutputStream os = new FileOutputStream("output.txt");

		// PrintStream pos = new PrintStream(os);

		int t = scanner.nextInt();

		for (int i = 1; i <= t; ++i) {

			handleCase(i, scanner, System.out);

		}

		scanner.close();
	}
}