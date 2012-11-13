package com.eric.codejam;

import java.util.List;

import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.main.AbstractInputData;

public class InputData extends AbstractInputData {
    List<PointInt> points;
    
    InputData(int testCase) {
        super(testCase);
    }
}
