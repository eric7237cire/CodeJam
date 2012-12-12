package codejam.y2008.round_amer.mixing;

import java.util.LinkedHashMap;
import java.util.List;

import codejam.utils.main.AbstractInputData;

public class InputData  extends AbstractInputData {

    int N;
    public InputData(int testCase) {
        super(testCase);
    }
    LinkedHashMap<String,List<String>> ingredients;
}
