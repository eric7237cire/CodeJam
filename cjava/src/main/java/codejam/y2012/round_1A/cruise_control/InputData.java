package codejam.y2012.round_1A.cruise_control;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.main.AbstractInputData;

public class InputData 
    extends AbstractInputData {
        
        int N;
        char[] initialLane;
        int [] speed;
        int [] initialPosition;
        
        public InputData(int testCase) {
            super(testCase);
        }
}
