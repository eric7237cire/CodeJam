package com.eric.codejam;

import java.util.List;

import com.eric.codejam.main.AbstractInputData;

public class InputData extends AbstractInputData {
    //Données

    int deleteCost;
    int insertCost;
    int num;
    int minimumDist;
    List<Integer> pixels;
    
    InputData(int testCase) {
        super(testCase);
    }
}
