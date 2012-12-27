package codejam.y2012.round_1B.tide;

import codejam.utils.main.AbstractInputData;
import codejam.utils.utils.Grid;

public class InputData extends AbstractInputData {

    int H;
    int N; // rows
    int M;
    Grid<Integer> ceiling;
    Grid<Integer> floor;

    public InputData(int testCase) {
        super(testCase);
    }
}
