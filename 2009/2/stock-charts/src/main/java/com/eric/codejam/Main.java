package com.eric.codejam;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eric.codejam.main.Runner;
import com.eric.codejam.main.Runner.TestCaseInputScanner;
import com.eric.codejam.multithread.Consumer.TestCaseHandler;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Main implements TestCaseHandler<InputData>, TestCaseInputScanner<InputData>{

	

	//Left to right
	boolean[][] canMatch;

	int iterations;
	
	
	
	//Right to Left
	BiMap<Integer, Integer> matchesMap;
	//Right hand side of the bipartite graph
	boolean[] seen;
	
	public int findAllAugmentingPaths(InputData input) {
		matchesMap = HashBiMap.create();
		int count = 0;
		for(int i = 0; i < input.n; ++i) {
			seen = new boolean[input.n];
			/*Is there an augmenting path?
			 * If so, then the node belongs to a combined graph.
			 * Otherwise, it is the lowest stock chart on a new combined graph
			 */
			if (!findAugmentingPath(i, input)) {
				++count;
			}
			
			printAugmentingPaths(input);
		}
		
		return count;
	}
	
	private void printAugmentingPaths(InputData input) {
		Map<Integer, Integer> leftToRightMatches = matchesMap.inverse();
		log.debug("Print all paths\n\n");
		for(int lhsVertex = 0; lhsVertex < input.n; ++lhsVertex) {
			//Only looking for unmatched vertices
			if (leftToRightMatches.containsKey(lhsVertex)) {
				continue;
			}
			log.debug("Path Start");
			//Check if another lhsVertex has a match ending on the vertex,
			//which is why the right->left map is used
			Integer nextLhsVertex = lhsVertex;
			do {
				log.debug("Path {}", nextLhsVertex);
				nextLhsVertex = matchesMap.get(nextLhsVertex);
				
			} while (nextLhsVertex != null);
			
		}
	}
	
	private boolean findAugmentingPath(int lhsVertex, InputData input) {
		/* 
		 * The vertex is part of the left hand side
		 */
		
		//Loop through all right hand side vertices
		for(int rhsVertex = 0; rhsVertex < input.n; ++rhsVertex) {
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
			if (!matchesMap.containsKey(rhsVertex) || findAugmentingPath(matchesMap.get(rhsVertex),input)) {
				
				//If it exists, free the existing match edge
				Integer removed = matchesMap.inverse().remove(lhsVertex);
				
				matchesMap.put(rhsVertex,  lhsVertex);
				return true;
			}
		}
		
		return false;
	}
	
	private void buildCanMatch(InputData input) {
		canMatch = new boolean[input.n][input.n];

		for (int i = 0; i < input.n; ++i) {
			
			for (int j = 0; j < input.n; ++j) {
				boolean canMatchB = isStrictlyGreater(input.stocks.get(i), input.stocks.get(j));
				canMatch[i][j] = canMatchB;
				
			}
		}
	}
	
	

	

	

	private boolean isStrictlyGreater(List<Integer> a, List<Integer> b) {
		
		//Preconditions.checkArgument(a.size() == k && b.size() == k);
		for (int i = 0; i < a.size(); ++i) {
			if (a.get(i) <= b.get(i)) {
				return false;
			}			
		}

		return true;
	}

	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {

		
	}

	

	public Main() {
		super();	

	}

	final static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String args[]) throws Exception {

		if (args.length < 1) {
			args = new String[] { "sample.txt" };
		}
		log.info("Input file {}", args[0]);

		Main m = new Main();
		Runner.goScanner(args[0],m,m);
	}

    @Override
    public InputData readInput(Scanner scanner, int testCase) {
        InputData input = new InputData(testCase);
        input.n = scanner.nextInt();
        input.k = scanner.nextInt();

        input.stocks = new ArrayList<>();
        for (int n = 0; n < input.n; ++n) {
            List<Integer> values = new ArrayList<>(input.k);
            for (int k = 0; k < input.k; ++k) {
                int value = scanner.nextInt();
                values.add(value);
            }
            input.stocks.add(values);
        }

        return input;
    }

    /* (non-Javadoc)
     * @see com.eric.codejam.multithread.Consumer.TestCaseHandler#handleCase(int, java.lang.Object)
     */
    @Override
    public String handleCase(int caseNumber, InputData data) {

        iterations=0;
        buildCanMatch(data);
        

        int min = findAllAugmentingPaths(data);



        log.info("Finished Starting case {}.  Iterations {}", caseNumber,
                iterations);
        
        return ("Case #" + caseNumber + ": " + min);
    }
}