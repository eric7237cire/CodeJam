package codejam.y2009.round_2.watering_plants;

import java.util.ArrayList;
import java.util.List;

import codejam.utils.geometry.Circle;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {


    int n;
    List<Circle> plants;
    
    public InputData(int testCase) {
        super(testCase);
        plants = new ArrayList<>();
    }

}
