package com.eric.codejam;

import java.util.ArrayList;
import java.util.List;

import com.eric.codejam.geometry.Circle;
import com.eric.codejam.main.AbstractInputData;

public class InputData extends AbstractInputData {


    int n;
    List<Circle> plants;
    
    public InputData(int testCase) {
        super(testCase);
        plants = new ArrayList<>();
    }

}
