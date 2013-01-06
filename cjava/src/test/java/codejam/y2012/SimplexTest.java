package codejam.y2012;

import java.util.Arrays;

import org.junit.Test;

import codejam.y2012.round_2.mountain_view.Simplex;

public class SimplexTest {

    @Test
    public void simplexTest() {
       /*
        * x1 + x2 > 1
2x1 − x2 > 1
3x2 6 2
        */
        
        Simplex s = new Simplex(2);
        
        s.addConstraintGTE(Arrays.asList(1d, 1d), 1d);
        s.addConstraintGTE(Arrays.asList(2d, -1d), 1d);
        s.addConstraintLTE(Arrays.asList(0d, 3d), 2d);
        
        s.doPhase1();
        
    }

}
