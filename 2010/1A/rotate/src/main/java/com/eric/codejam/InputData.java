package com.eric.codejam;

import com.eric.codejam.main.AbstractInputData;
import com.eric.codejam.utils.Grid;

public class InputData extends AbstractInputData {
    //Données
    
    int gridSize;
    int k;
    Grid<Character> grid;
    InputData(int testCase) {
        super(testCase);
    }
}
