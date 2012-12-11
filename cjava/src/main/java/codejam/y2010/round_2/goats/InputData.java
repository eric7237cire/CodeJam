package codejam.y2010.round_2.goats;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {
    //Données
    
    int N, M;
    
    PointInt[] goatPolePositions;
    PointInt[] bucketPositions;
    
    InputData(int testCase) {
        super(testCase);
    }
}
