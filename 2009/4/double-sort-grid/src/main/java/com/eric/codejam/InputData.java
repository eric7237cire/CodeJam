package com.eric.codejam;

import com.eric.codejam.main.AbstractInputData;
import com.eric.codejam.utils.Grid;

public class InputData extends AbstractInputData {
    //Données
    
    InputData(int testCase) {
        super(testCase);
    }
    
    int R;
    int C;
    
    Grid<Integer> grid;

    @Override
    public String toString() {
        return "InputData [grid=" + grid + "]";
    }
    
    
}
