package codejam.y2010.round_1C.chess_boards;

import codejam.utils.main.AbstractInputData;
import codejam.utils.utils.Grid;

public class InputData extends AbstractInputData {
    //Données
    int M, N;
    
    Grid<Integer> grid;
    
    InputData(int testCase) {
        super(testCase);
    }
}
