package codejam.y2012.round_final.upstairs_downstairs;

import java.util.List;

import org.apache.commons.lang3.math.Fraction;

import codejam.utils.main.AbstractInputData;
import codejam.y2012.round_final.upstairs_downstairs.Main.Activity;

public class InputData extends AbstractInputData {

    //# of activities
    int N;
    
    //Minimum activities
    int K;
    
    List<Activity> activityList;
    Fraction[] prob;
    int[] limit;

    public InputData(int testCase) {
        super(testCase);
    }

}
