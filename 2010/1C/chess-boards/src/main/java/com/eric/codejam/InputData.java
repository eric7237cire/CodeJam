package com.eric.codejam;

import com.eric.codejam.main.AbstractInputData;
import com.eric.codejam.utils.Grid;

public class InputData extends AbstractInputData {
    //Données
    int M, N;
    
    Grid<Integer> grid;
    
    InputData(int testCase) {
        super(testCase);
    }
}
