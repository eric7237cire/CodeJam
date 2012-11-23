package com.eric.codejam;

import java.util.List;

import com.eric.codejam.main.AbstractInputData;

public class InputData extends AbstractInputData {
    //Données
    
    int L; //load
    int P; //unacceptable load
    int C; //factor L + C(L) >= P
    InputData(int testCase) {
        super(testCase);
    }
}
