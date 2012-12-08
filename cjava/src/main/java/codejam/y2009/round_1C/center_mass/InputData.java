package codejam.y2009.round_1C.center_mass;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    public InputData(int testCase) {
        super(testCase);
    }

    int numFireflies;

    double[] coordsAvg = new double[3];
    int[] coords = new int[3];

    double[] velocityAvg = new double[3];
    int[] velocity = new int[3];
}