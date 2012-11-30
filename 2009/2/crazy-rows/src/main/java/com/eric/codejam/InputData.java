package com.eric.codejam;

import java.util.ArrayList;
import java.util.List;

import com.eric.codejam.Main.Row;
import com.eric.codejam.main.AbstractInputData;


public class InputData extends AbstractInputData {
    //Données

    int dimension ;

    List<Row> liste = new ArrayList<>();
    
    InputData(int testCase) {
        super(testCase);
    }
}

