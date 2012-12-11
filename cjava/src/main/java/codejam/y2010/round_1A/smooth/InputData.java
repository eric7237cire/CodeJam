package codejam.y2010.round_1A.smooth;

import java.util.List;

import codejam.utils.main.AbstractInputData;

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
