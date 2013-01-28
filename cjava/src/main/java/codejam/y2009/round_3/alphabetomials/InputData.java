package codejam.y2009.round_3.alphabetomials;

import java.util.List;

import codejam.utils.main.AbstractInputData;


public class InputData extends AbstractInputData {

    public String polynomial;
    public int k;
    public int d;
    public List<String> dictWords;
    
    public int[][] wordLetterCount;
    
    public InputData(int testCase) {
        super(testCase);
    }
}


