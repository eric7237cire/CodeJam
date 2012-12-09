package codejam.y2011.round_1B.house_kittens;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {
    int N;
    int M;
    
    List<Pair<Integer, Integer>> interiorWalls;

    public InputData(int testCase) {
        super(testCase);
    }
}
