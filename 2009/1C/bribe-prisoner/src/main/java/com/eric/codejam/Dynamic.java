package com.eric.codejam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public class Dynamic implements PrisonSelectionAlgorithm {

	private static class Segment implements Comparable<Segment> {
		private int start;
		private int stop;
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}

			Segment rhs = (Segment) obj;

			return Objects.equal(rhs.start, start) && Objects.equal(rhs.stop, stop);
		}
		@Override
		public int hashCode() {
			return Objects.hashCode(start, stop);
		}
		@Override
		public int compareTo(Segment arg0) {
			return ComparisonChain.start().compare(start, arg0.start).compare(stop, arg0.stop).result();
		}
		public Segment(int start, int stop) {
			super();
			this.start = start;
			this.stop = stop;
		}
		
		
	}
	
	public Dynamic() {
		dp = new HashMap<>();
	}
	
	private Map<Segment, Integer> dp;
	
	private List<Integer> toBeFreed ;
	
	public int findMinCost(int segStart, int segEnd) {
		Preconditions.checkArgument(segStart >= 0);
		Preconditions.checkArgument(segEnd >= segStart);
		
		Segment seg = new Segment(segStart, segEnd);
		
		if (dp.containsKey(seg)) {
			return dp.get(seg);
		}
		
		Integer ret = null;
		for(int i = 0; i < toBeFreed.size(); ++i) {
			int position = toBeFreed.get(i);
			
			if (position < segStart || position > segEnd) {
				continue;
			}
			
			int minCost = segEnd - segStart;
			
			if (position > segStart) {
				minCost += findMinCost(segStart, position - 1) ;
			}
			
			if (position < segEnd) {
				minCost	+= findMinCost(position + 1, segEnd);
			}
			
			if (ret == null || minCost < ret) {
				ret = minCost;
			}
		}
		
		if (ret == null) {
			ret = 0;
		}
		
		dp.put(seg,  ret);
		return ret;
	}
	
	@Override
	public int findMinCost(int segStart, int segEnd, List<Integer> toBeFreed) {
		this.toBeFreed = toBeFreed;
		
		return findMinCost(segStart, segEnd);
	}

	

}