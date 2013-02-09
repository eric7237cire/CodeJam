package codejam.y2008.round_pracContest.cycles;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    //Number of verticies
    int N;
    
    //Forbidden edges
    int K;
    
    List<Pair<Integer,Integer>> forbiddenEdges;
    
    public InputData(int testCase) {
        super(testCase);
    }

}