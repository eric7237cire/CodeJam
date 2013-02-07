package codejam.y2011.round_2.spinning_blade;

import codejam.utils.main.AbstractInputData;
import codejam.utils.utils.Grid;

public class InputData 
    extends AbstractInputData {

   
        int R;
        int C;
        
        //Density
        int D;
        
        Grid<Integer> cells;
        
        public InputData(int testCase) {
            super(testCase);
        }
        
}
