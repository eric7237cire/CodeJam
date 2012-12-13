package codejam.y2009.round_1C.bribe_prisoner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Preconditions;

public class Dynamic implements PrisonSelectionAlgorithm {

	
	public Dynamic(int maxLoc) {
	    dp = new HashMap<>(maxLoc*20);
	}
	
	private Map<Pair<Integer,Integer>, Integer> dp;
	
	
	private List<Integer> toBeFreed ;
	
	/**
	 * segStart to segEnd are filled with prisoners
	 * 
	 * @param segStart 1 indexed
	 * @param segEnd
	 * @return
	 */
	public int findMinCost(int segStart, int segEnd) {
		Preconditions.checkArgument(segStart >= 0);
		Preconditions.checkArgument(segEnd >= segStart);
		
		//int len = segEnd - segStart;
		Pair<Integer,Integer> segment = new ImmutablePair<>(segStart,segEnd);
		
		
		if (dp.containsKey(segment)) {
			return dp.get(segment);
		}
		
		Integer ret = null;
		//Find the prisoner that costs the least to free
		for(int i = 0; i < toBeFreed.size(); ++i) {
			int position = toBeFreed.get(i);
			
			//This prisoner was already freed 
			if (position < segStart || position > segEnd) {
				continue;
			}
			
			int minCost = segEnd - segStart;
			
			//Add costs to free prisoners to the left
			if (position > segStart) {
				minCost += findMinCost(segStart, position - 1) ;
			}
			
			//And to the right
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
		
		dp.put(segment, ret);
		return ret;
	}
	
	@Override
	public int findMinCost(int segStart, int segEnd, List<Integer> toBeFreed) {
		this.toBeFreed = toBeFreed;
		
		return findMinCost(segStart, segEnd);
	}

	

}