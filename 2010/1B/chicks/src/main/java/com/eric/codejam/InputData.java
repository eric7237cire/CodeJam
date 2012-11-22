package com.eric.codejam;

import java.util.List;

import com.eric.codejam.main.AbstractInputData;

public class InputData extends AbstractInputData {
    //Données
    int numberChicks;
    int chicksToPass;
    
    int barnLocation;
    int minimumTime;
    
    List<Integer> chickLocations;
    List<Integer> chickSpeeds;
    
    InputData(int testCase) {
        super(testCase);
    }
}
