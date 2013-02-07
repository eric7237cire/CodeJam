package codejam.y2008.round_beta.random_route;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.datastructures.graph.WeightedGraphInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    
    int nRoads;
    WeightedGraphInt graph;
    
    List<Pair<Integer,Integer>> roads;
    
    public InputData(int testCase) {
        super(testCase);
    }

}