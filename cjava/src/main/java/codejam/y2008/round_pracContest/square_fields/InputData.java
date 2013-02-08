package codejam.y2008.round_pracContest.square_fields;

import java.util.List;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    
    int N; // number of points
    int K; // number of squares
    
    List<PointInt> points;
    
    public InputData(int testCase) {
        super(testCase);
    }

}