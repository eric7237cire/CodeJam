package codejam.y2012.round_3.lost_password;

import java.util.List;

import codejam.utils.geometry.Circle;
import codejam.utils.geometry.PointInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    int N;
    
    PointInt redLight;
    PointInt greenLight;
    
    List<Circle> pillars;
    /*
     * One line containing the coordinates x, y of the 
     * red light source.
One line containing the coordinates x, y of the green light 
source.
One line containing the number of pillars n.
n lines describing the pillars. 
Each contains 3 numbers x, y, r. 
The pillar is a disk with the center (x, y) and radius r.

     */
    public InputData(int testCase) {
        super(testCase);
    }

}