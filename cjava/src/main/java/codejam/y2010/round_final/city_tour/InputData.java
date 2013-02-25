package codejam.y2010.round_final.city_tour;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    int N;
    
    boolean[][] connections;
    
    List<Pair<Integer,Integer>> edges;
        
    public InputData(int testCase) {
        super(testCase);
    }

}