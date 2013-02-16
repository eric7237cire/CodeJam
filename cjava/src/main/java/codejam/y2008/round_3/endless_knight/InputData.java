package codejam.y2008.round_3.endless_knight;

import java.util.List;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    int H;
    int W;
    int R;
    
    List<PointInt> rocks;
    PointInt[] rocksOrig;
    
    PointInt finalCorner;
    
    
    public InputData(int testCase) {
        super(testCase);
    }

}