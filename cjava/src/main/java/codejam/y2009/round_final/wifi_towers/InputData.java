package codejam.y2009.round_final.wifi_towers;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    int N;
    
    PointInt towerLoc[];
    int towerRange[];
    int towerScore[];
    
    
    /*
     * Each test case starts with the number of towers, n. 
     * The following n lines each contain 4 integers: x, y, r, s. 
     * They describe a tower at coordinates x, y having a range of r
     *  and a score (value of updating to the new protocol) of s
     */
    
    public InputData(int testCase) {
        super(testCase);
    }

}