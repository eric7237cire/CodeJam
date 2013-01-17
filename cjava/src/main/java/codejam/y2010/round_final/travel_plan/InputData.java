package codejam.y2010.round_final.travel_plan;

import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    /*
     *  Each test case description starts with a line containing the number of planets N.
     *   The next line contains N numbers Xi, the coordinates of the planets.
     *    The next line contains the amount of fuel F that you have.
     */
        
    int N;
    long[] X;
    long F;
    
    public InputData(int testCase) {
        super(testCase);
    }

}