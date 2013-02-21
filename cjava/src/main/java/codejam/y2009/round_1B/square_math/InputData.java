package codejam.y2009.round_1B.square_math;

import java.util.List;

import codejam.utils.main.AbstractInputData;
import codejam.utils.utils.GridChar;

public class InputData extends AbstractInputData {
    int width ;
    int queries ;
    char[][] grid;
    List<Integer> targets;
    
    InputData(int testCase) {
        super(testCase);
    }
}
