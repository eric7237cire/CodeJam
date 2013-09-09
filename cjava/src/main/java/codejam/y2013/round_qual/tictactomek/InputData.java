package codejam.y2013.round_qual.tictactomek;

import codejam.utils.datastructures.BitSetInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {
    public InputData(int testCase) {
        super(testCase);
        x = new BitSetInt();
        o = new BitSetInt();
    }
    
    
    BitSetInt x;
    BitSetInt o;
    
}
