package com.eric.codejam;

import com.eric.codejam.main.AbstractInputData;
import com.eric.codejam.utils.Grid;

public class InputData extends AbstractInputData {

    Grid<Boolean> grid; //false #  true .
    
    int rows;
    int cols;
    int fallingDistance;
    
    public InputData(int testCase) {
        super(testCase);
    }

}
