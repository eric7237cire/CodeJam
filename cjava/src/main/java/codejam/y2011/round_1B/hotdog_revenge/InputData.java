package codejam.y2011.round_1B.hotdog_revenge;

import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {
    int C;
    int D;
    
    List<ImmutablePair<Integer, Integer>> posCount;

    public InputData(int testCase) {
        super(testCase);
    }
}
