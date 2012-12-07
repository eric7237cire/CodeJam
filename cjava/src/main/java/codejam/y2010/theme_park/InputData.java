package codejam.y2010.theme_park;

import java.util.List;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {
    //Données
    int rides;
    List<Integer> groupSizes;
    int capacity;
    
    InputData(int testCase) {
        super(testCase);
    }
}
