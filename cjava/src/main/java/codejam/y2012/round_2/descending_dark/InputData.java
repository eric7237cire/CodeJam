package codejam.y2012.round_2.descending_dark;

import codejam.utils.main.AbstractInputData;
import codejam.utils.utils.GridChar;

public class InputData 
    extends AbstractInputData {
        
    /*
     * The first line of the input gives the number of test cases, T. T test cases follow, beginning with a line containing integers R and C, representing the number of rows and columns in the mountain layout.

This is followed by R lines, each containing C characters,
 describing a mountain layout. As in the example above, 
 a '#' character represents an impassable square, a '.' 
 character represents a passable square, 
 and the digits '0'-'9' represent caves 
 (which are also passable squares).
     */
  int R; int C; 
        
  GridChar grid;
  
        public InputData(int testCase) {
            super(testCase);
        }
}
