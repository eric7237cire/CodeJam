package codejam.y2010.round_1B.chicks;

import java.util.List;

import codejam.utils.main.AbstractInputData;

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
