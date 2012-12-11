package codejam.y2009.round_2.digging_problem;

import codejam.utils.main.AbstractInputData;
import codejam.utils.utils.Grid;

public class InputData extends AbstractInputData {

    Grid<Boolean> grid; //false #  true .
    
    int rows;
    int cols;
    int fallingDistance;
    
    public InputData(int testCase) {
        super(testCase);
    }

}
