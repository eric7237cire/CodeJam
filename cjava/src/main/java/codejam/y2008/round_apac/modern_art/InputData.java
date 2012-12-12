package codejam.y2008.round_apac.modern_art;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    public InputData(int testCase) {
        super(testCase);
    }
    int N;
    List<Pair<Integer, Integer>> largeConnections;
    
    int M;
    List<Pair<Integer, Integer>> smallConnections;
}
