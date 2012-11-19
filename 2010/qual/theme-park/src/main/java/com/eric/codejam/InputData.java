package com.eric.codejam;

import java.util.List;

import com.eric.codejam.main.AbstractInputData;

public class InputData extends AbstractInputData {
    //Données
    int rides;
    List<Integer> groupSizes;
    int capacity;
    
    InputData(int testCase) {
        super(testCase);
    }
}
