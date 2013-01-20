package codejam.y2012.round_final.zombie_smash;

import codejam.utils.geometry.PointInt;
import codejam.utils.main.AbstractInputData;

public class InputData extends AbstractInputData {

    /*
    each starting with a line containing a single integer Z, the number of zombies in the level.

    The next Z lines contain 3 space-separated integers each, representing the location and time at which a given zombie will appear and disappear.
The ith line will contain the integers Xi, Yi and Mi, where:

    Xi is the X coordinate of the cell at which zombie i appears,
    Yi is the Y coordinate of the cell at which zombie i appears,
    Mi is the time at which zombie i appears, in milliseconds after the beginning of the game. 
    The time interval during which the zombie can smashed is inclusive: if you reach the cell 
    at any time in the range [Mi, Mi + 1000] with a charged Zombie Smasher, you can smash the 
    zombie in that cell.
    */
    
    int Z;
    
    PointInt[] zombieLoc;
    
    int[] zombieAppearance;

    public InputData(int testCase) {
        super(testCase);
    }

}
