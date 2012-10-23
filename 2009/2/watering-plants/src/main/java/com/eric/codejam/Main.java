package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Main {

	final List<List<Integer>> stocks; // false # true .
	final int n;
	final int k;

	//Left to right
	boolean[][] canMatch;

	int iterations;
	
	
	
	//Right to Left
	BiMap<Integer, Integer> matchesMap;
	//Right hand side of the bipartite graph
	boolean[] seen;
	
	public int findAllAugmentingPaths() {
		matchesMap = HashBiMap.create();
		int count = 0;
		for(int i = 0; i < n; ++i) {
			seen = new boolean[n];
			/*Is there an augmenting path?
			 * If so, then the node belongs to a combined graph.
			 * Otherwise, it is the lowest stock chart on a new combined graph
			 */
			if (!findAugmentingPath(i)) {
				++count;
			}
			
			printAugmentingPaths();
		}
		
		return count;
	}
	
	private void printAugmentingPaths() {
		Map<Integer, Integer> leftToRightMatches = matchesMap.inverse();
		log.info("Print all paths\n\n");
		for(int lhsVertex = 0; lhsVertex < n; ++lhsVertex) {
			//Only looking for unmatched vertices
			if (leftToRightMatches.containsKey(lhsVertex)) {
				continue;
			}
			log.info("Path Start");
			//Check if another lhsVertex has a match ending on the vertex,
			//which is why the right->left map is used
			Integer nextLhsVertex = lhsVertex;
			do {
				log.info("Path {}", nextLhsVertex);
				nextLhsVertex = matchesMap.get(nextLhsVertex);
				
			} while (nextLhsVertex != null);
			
		}
	}
	
	private boolean findAugmentingPath(int lhsVertex) {
		/* 
		 * The vertex is part of the left hand side
		 */
		
		//Loop through all right hand side vertices
		for(int rhsVertex = 0; rhsVertex < n; ++rhsVertex) {
			if (!canMatch[lhsVertex][rhsVertex]) {
				continue;
			}
			if (seen[rhsVertex]) {
				continue;
			}
			
			seen[rhsVertex] = true;
			
			/*Here the edge is either already in M or not.
			 * If its not we are done, we found a path.  Otherwise
			 * we try to rematch the connected lhsvertex to another greater rhsVertex .
			 */
			if (!matchesMap.containsKey(rhsVertex) || findAugmentingPath(matchesMap.get(rhsVertex))) {
				
				//If it exists, free the existing match edge
				Integer removed = matchesMap.inverse().remove(lhsVertex);
				
				matchesMap.put(rhsVertex,  lhsVertex);
				return true;
			}
		}
		
		return false;
	}
	
	private void buildCanMatch() {
		canMatch = new boolean[n][n];

		for (int i = 0; i < n; ++i) {
			
			for (int j = 0; j < n; ++j) {
				boolean canMatchB = isStrictlyGreater(stocks.get(i), stocks.get(j));
				canMatch[i][j] = canMatchB;
				
			}
		}
	}
	
	

	

	

	private boolean isStrictlyGreater(List<Integer> a, List<Integer> b) {
		
		Preconditions.checkArgument(a.size() == k && b.size() == k);
		for (int i = 0; i < a.size(); ++i) {
			if (a.get(i) <= b.get(i)) {
				return false;
			}			
		}

		return true;
	}

	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {

		Main m = Main.buildMain(scanner);

		int min = m.findAllAugmentingPaths();

		log.info("Starting case {}", caseNumber);

		os.println("Case #" + caseNumber + ": " + min);

		log.info("Finished Starting case {}.  Iterations {}", caseNumber,
				m.iterations);
	}

	private static Main buildMain(Scanner scanner) {
		Main m = new Main(scanner.nextInt(), scanner.nextInt());


		for (int n = 0; n < m.n; ++n) {
			List<Integer> values = new ArrayList<>(m.k);
			for (int k = 0; k < m.k; ++k) {
				int value = scanner.nextInt();
				values.add(value);
			}
			m.stocks.add(values);
		}

		m.buildCanMatch();

	
		//m.findDisjointSets();
		return m;
	}

	public Main(int n, int k) {
		super();
		this.n = n;
		this.k = k;
		
		this.stocks = new ArrayList<>(n);

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