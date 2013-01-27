package codejam.y2009.round_final.double_sort_grid;

import codejam.utils.main.AbstractInputData;
import codejam.utils.utils.Grid;

public class InputData extends AbstractInputData {
    //Données
    
    public InputData(int testCase) {
        super(testCase);
    }
    
    int R;
    int C;
    
    Grid<Integer> grid;

    @Override
    public String toString() {
        return "InputData [grid=" + grid + "]";
    }
    
    
}
