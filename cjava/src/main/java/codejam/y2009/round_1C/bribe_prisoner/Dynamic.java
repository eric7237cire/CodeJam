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
	//private TreeSet<Integer> toBeFreed;
	
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
		
		Pair<Integer,Integer> segment = new ImmutablePair<>(segStart,segEnd);
				
		if (dp.containsKey(segment)) {
			return dp.get(segment);
		}
		
		//Start at zero in case no prisoners are freed in this segment ; thus
		//costing nothing
		int ret = 0;
				
		//Find the prisoner that costs the least to free ;
		//does not really help if it is a tree set
		for(int i = 0; i < toBeFreed.size(); ++i) {
		    int position = toBeFreed.get(i);
			
			//This prisoner was already freed		    
			if (position < segStart || position > segEnd) {
				continue;
			}
			
			//Freeing this prisoner will always cost the length of the segment he's in
			int minCost = segEnd - segStart;
			
			//Add costs to free prisoners to the left
			if (position > segStart) {
				minCost += findMinCost(segStart, position - 1) ;
			}
			
			//And to the right
			if (position < segEnd) {
				minCost	+= findMinCost(position + 1, segEnd);
			}
			
			if (ret == 0 || minCost < ret) {
				ret = minCost;
			}
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