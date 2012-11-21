package com.eric.codejam;

import java.util.List;

import com.eric.codejam.main.AbstractInputData;

public class InputData extends AbstractInputData {
    //Données
    int dirExisting;
    int dirToCreate;
    List<String> dirsExisting;
    List<String> dirsToCreate;
    
    InputData(int testCase) {
        super(testCase);
    }
}
