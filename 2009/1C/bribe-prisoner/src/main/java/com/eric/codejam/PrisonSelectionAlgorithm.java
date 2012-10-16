package com.eric.codejam;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface PrisonSelectionAlgorithm {

	final static Logger log = LoggerFactory.getLogger(Main.class);
	
	public int findMinCost(final int segStart, final int segEnd, List<Integer> toBeFreed); 
		
}