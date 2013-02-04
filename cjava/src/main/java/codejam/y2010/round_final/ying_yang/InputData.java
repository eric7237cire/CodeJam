package codejam.y2010.round_final.ying_yang;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    int nRows; //rows
    int nCols; //columns
    
    PointInt[] corners;
    
    public InputData(int testCase) {
        super(testCase);
    }

}