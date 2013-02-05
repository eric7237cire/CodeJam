package codejam.y2010.round_final.ying_yang;

import java.util.Set;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    int nRows; //rows
    int nCols; //columns
    
    PointInt[] corners;
    Set<PointInt> cornerSet;
    
    public InputData(int testCase) {
        super(testCase);
    }

}