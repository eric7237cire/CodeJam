package com.eric.codejam;

import java.util.List;

import com.eric.codejam.main.AbstractInputData;

public class InputData extends AbstractInputData {

    //Données
    
    int P;
    int[] M;
    List<List<Integer>> costs;
    
    InputData(int testCase) {
        super(testCase);
    }
}
