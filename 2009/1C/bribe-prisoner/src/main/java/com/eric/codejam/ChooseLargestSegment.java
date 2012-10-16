package com.eric.codejam;

import java.util.ArrayList;
import java.util.List;

public class ChooseLargestSegment implements PrisonSelectionAlgorithm {

	@Override
	public int findMinCost(int segStart, int segEnd, List<Integer> toBeFreed) {
		
		int numFreed = 0;
		int cost  = 0;
		
		List<PrisonCell> listeCells = new ArrayList<>();
		
		for(int i = 0; i < toBeFreed.size(); ++i) {
			int index = toBeFreed.get(i);
		listeCells.add(new PrisonCell(i, toBeFreed.size() - i - 1, index,
				segEnd - segStart - 1 - index, index));
		}
		
		
		
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
		
		return cost;
	}

}
