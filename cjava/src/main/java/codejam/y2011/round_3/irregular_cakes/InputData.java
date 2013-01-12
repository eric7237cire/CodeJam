package codejam.y2011.round_3.irregular_cakes;

import java.util.List;

import codejam.utils.geometry.Point;
import codejam.utils.main.AbstractInputData;

public class InputData 
    extends AbstractInputData {

   /*
    * W (the cake's width), L (the number of points on the lower boundary),
    *  U (the number of points on the upper boundary) 
    *  and G (the number of guests at the party).

This is followed by L lines specifying the lower boundary. 
The i-th line contains two integers xi and yi, representing the coordinates 
of the i-th point on the lower boundary. This is followed by U more lines 
specifying the upper boundary. The j-th line here contains two integers
 xj and yj, representing the coordinates of the j-th point on the upper boundary.
    */
        int W;
        int L;
        int U;
        int G;
        
        List<Point> lower;
        List<Point> upper;
        
        
        public InputData(int testCase) {
            super(testCase);
        }
        
}
