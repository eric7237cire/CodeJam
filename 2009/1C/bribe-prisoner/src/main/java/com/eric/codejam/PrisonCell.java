package com.eric.codejam;

public class PrisonCell {

	int prisonersLeftToBeReleased;
	int prisonersRightToBeReleased;

	int occupiedCellsLeft;
	int occupiedCellsRight;

	int index;

	boolean freed;

	public PrisonCell(int prisonersLeftToBeReleased,
			int prisonersRightToBeReleased, int occupiedCellsLeft,
			int occupiedCellsRight, int index) {
		super();
		this.prisonersLeftToBeReleased = prisonersLeftToBeReleased;
		this.prisonersRightToBeReleased = prisonersRightToBeReleased;
		this.occupiedCellsLeft = occupiedCellsLeft;
		this.occupiedCellsRight = occupiedCellsRight;
		this.index = index;
		this.freed = false;
	}

	@Override
	public String toString() {
		return "PrisonCell [prisonersLeft=" + prisonersLeftToBeReleased
				+ ", prisonersRight=" + prisonersRightToBeReleased
				+ ", cellsLeft=" + occupiedCellsLeft + ", cellsRight="
				+ occupiedCellsRight + ", index=" + index + ", freed=" + freed
				+ "]";
	}

}
