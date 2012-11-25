package com.eric.codejam;

import com.eric.codejam.geometry.PointInt;
import com.eric.codejam.main.AbstractInputData;

public class InputData extends AbstractInputData {
    //Données
    
    int N, M;
    
    PointInt[] goatPolePositions;
    PointInt[] bucketPositions;
    
    InputData(int testCase) {
        super(testCase);
    }
}
