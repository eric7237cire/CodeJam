package codejam.y2009.stock_charts;

import java.util.List;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    List<List<Integer>> stocks; // false # true .
    int n;
    int k;
    
    public InputData(int testCase) {
        super(testCase);
    }

}
