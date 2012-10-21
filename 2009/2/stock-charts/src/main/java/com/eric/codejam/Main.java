package com.eric.codejam;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.Sets;

public class Main {

	final List<List<Integer>> stocks; // false # true .
	final int n;
	final int k;

	Boolean[][] canMatch;

	List<Set<Integer>> sets;
	int disjointCount;

	Set<Integer> targetSet;

	int iterations;
	
	
	
	private int findMinRecursive() {
		buildVertexToSubsetMap();
		
		log.debug("vertex to set map {}", nToSetMap);
		
		findIntersections();
		
		return findMinRecursiveFunction(0,  new HashSet<Integer>());
	}
	
	
	BiMap<Integer, Integer> matchesMap;
	boolean[] seen;
	
	public int findAllAugmentingPaths() {
		
	}
	
	private boolean findAugmentingPath(int vertex) {
		
	}
	
	private void findIntersections() {
		
		
		for(int i = 0; i < n; ++i) {
			Set<Integer> interSet = new HashSet<>(targetSet);
			
			List<Set<Integer>> list = nToSetMap.get(i);
			
			if (list == null) {
				continue;
			}
			
			for(Set<Integer> set : list) {
				interSet = Sets.intersection(interSet, set);
			}
			
			for(Integer nInSet : interSet) {
				if (nInSet != i) {
					log.debug("Removing {}", nInSet);
					nToSetMap.remove(nInSet);
				}
			}
		}
	}
	
	private int findMinRecursiveFunction(int i, Set<Integer> unionSoFar) {
		List<Set<Integer>> list = nToSetMap.get(i);
		
		
		if (i == n) {
			return 0;
		}
		
		if (list == null) {
			return findMinRecursiveFunction(i+1, unionSoFar);
		}
		
		if (unionSoFar.contains(i)) {
			return findMinRecursiveFunction(i+1, unionSoFar);
		}
		
		if (i <= 10) {
			log.debug("findMinRecursiveFunction n={} {} {}", n, i, unionSoFar);
		}
		
		//Try all in list
		int allMin = Integer.MAX_VALUE;
		
		for(Set<Integer> set : list) {
			Set<Integer> newUnion = new HashSet<>(unionSoFar);
			newUnion.addAll(set);
			int min = 1 + findMinRecursiveFunction(i+1, newUnion);
			allMin = Math.min(allMin,  min);
		}
		
		return allMin;
	}
	
	Map<Integer, List<Set<Integer>>> nToSetMap;
	
	private void buildVertexToSubsetMap() {
		nToSetMap = new HashMap<>();
		for(int i =0; i < n; ++i) {
			List<Set<Integer>> list = new ArrayList<>();
			for(Set<Integer> set : sets) {
				if (set.contains(i)) {
					list.add(set);
				}
			}
			nToSetMap.put(i,  list);
		}
	}

	private int findMinCombination() {

		if (targetSet.isEmpty()) {
			return 0;
		}
		
		for (int k = 1; k < sets.size(); ++k) {
			
			Iterator<Long> ci = new CombinationsIterator(sets.size(), k);
			
			// log.debug("Trying bits {}", Integer.toBinaryString(bits));
			
			boolean isUnion = false;
			
			while(ci.hasNext()) {
				long setBits = ci.next();
				
				Set<Integer> union = new HashSet<>();
				
				for (int i = 1; i <= sets.size(); ++i) {
					if ((1 << i - 1 & setBits) > 0) {
						union = Sets.union(union, sets.get(i - 1));
					}
				}
			
				if (union.containsAll(targetSet)) {
					//log.debug("Union found {} subSetCount {}", union, subSetCount);
					isUnion = true;
					break;
				}
				 
			}
			
			if (isUnion) {
				return k ;
			}
		}

			// log.debug("Union is {}", union);

			
		log.error("Problem");
		return sets.size();
	}

	
	private int findMinCombinationAll() {

		if (targetSet.isEmpty()) {
			return 0;
		}
		int minCom = Integer.MAX_VALUE;

		int end = 1 << (sets.size());

		for (int bits = 1; bits < end; ++bits) {
			// log.debug("Trying bits {}", Integer.toBinaryString(bits));
			Set<Integer> union = new HashSet<>();
			int subSetCount = 0;
			for (int i = 1; i <= sets.size(); ++i) {
				if ((1 << i - 1 & bits) > 0) {
					subSetCount++;
					union = Sets.union(union, sets.get(i - 1));
				}
			}

			// log.debug("Union is {}", union);

			if (union.containsAll(targetSet)) {
				//log.debug("Union found {} subSetCount {}", union, subSetCount);
				minCom = Math.min(subSetCount, minCom);
			}

		}
		return minCom;
	}

	private void findDisjointSets() {
		disjointCount = 0;
		

		for (Iterator<Set<Integer>> it = sets.iterator(); it.hasNext();) {
			Set<Integer> set = it.next();
			int itersectionCount = 0;
			for (Set<Integer> set2 : sets) {
				if (!Sets.intersection(set, set2).isEmpty()) {
					itersectionCount++;
				}
			}
			if (itersectionCount == 1) {
				targetSet.removeAll(set);
				it.remove();
				++disjointCount;
			}

		}
	}

	private void buildSets() {

		sets = new ArrayList<>();

		Set<Set<Integer>> k = new HashSet<>();

		//Build k == 1
		for (int i = 0; i < n; ++i) {
			Set<Integer> set = new HashSet<>();
			set.add(i);
			k.add(set);
		}

		while (!k.isEmpty()) {

			Set<Set<Integer>> kPlusOne = new HashSet<>();

			for (Iterator<Set<Integer>> kSetIterator = k.iterator(); kSetIterator
					.hasNext();) {
				Set<Integer> kSet = kSetIterator.next();
				boolean hasAdded = false;

				//find another vertex not in the set
				for (int i = 0; i < n; ++i) {
					if (kSet.contains(i)) {
						continue;
					}

					boolean matchesAll = true;
					for (int setMember : kSet) {
						if (!canMatch[setMember][i]) {
							matchesAll = false;
							break;
						}
					}

					if (matchesAll) {
						Set<Integer> kPlusOneSet = new HashSet<>(kSet);
						kPlusOneSet.add(i);
						kPlusOne.add(kPlusOneSet);
						hasAdded = true;
					}
				}

				if (!hasAdded) {
					sets.add(kSet);
				}
			}

			k = kPlusOne;

		}

		log.info("Sets {}", sets);
	}

	private void buildCanMatch() {
		canMatch = new Boolean[n][n];

		for (int i = 0; i < n; ++i) {
			canMatch[i][i] = true;
			for (int j = i + 1; j < n; ++j) {
				boolean canMatchB = intersect(stocks.get(i), stocks.get(j));
				canMatch[i][j] = canMatchB;
				canMatch[j][i] = canMatchB;
			}
		}
	}

	private boolean intersect(List<Integer> a, List<Integer> b) {
		boolean strictlyGreater = a.get(0) > b.get(0);
		Preconditions.checkArgument(a.size() == k && b.size() == k);
		for (int i = 0; i < a.size(); ++i) {
			if (strictlyGreater && a.get(i) <= b.get(i)) {
				return false;
			}
			if (!strictlyGreater && a.get(i) >= b.get(i)) {
				return false;
			}
		}

		return true;
	}

	public static void handleCase(int caseNumber, Scanner scanner,
			PrintStream os) {

		Main m = Main.buildMain(scanner);

		int min = m.findMinRecursive();

		log.info("Starting case {}", caseNumber);

		os.println("Case #" + caseNumber + ": " + min);

		log.info("Finished Starting case {}.  Iterations {}", caseNumber,
				m.iterations);
	}

	private static Main buildMain(Scanner scanner) {
		Main m = new Main(scanner.nextInt(), scanner.nextInt());

		m.targetSet = new HashSet<>();

		for (int n = 0; n < m.n; ++n) {
			m.targetSet.add(n);
			List<Integer> values = new ArrayList<>(m.k);
			for (int k = 0; k < m.k; ++k) {
				int value = scanner.nextInt();
				values.add(value);
			}
			m.stocks.add(values);
		}

		m.buildCanMatch();

		m.buildSets();
		//m.findDisjointSets();
		return m;
	}

	public Main(int n, int k) {
		super();
		this.n = n;
		this.k = k;
		this.disjointCount = 0;
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