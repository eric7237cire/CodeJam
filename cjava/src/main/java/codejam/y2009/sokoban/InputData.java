package codejam.y2009.sokoban;

import codejam.utils.main.AbstractInputData;
import codejam.utils.utils.Grid;

public class InputData extends AbstractInputData {
    Grid<SquareType> grid;
    int row;
    int col;
    public InputData(int testCase) {
        super(testCase);
    }
}
