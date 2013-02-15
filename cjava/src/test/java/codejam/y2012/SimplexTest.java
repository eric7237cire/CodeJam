package codejam.y2012;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import codejam.y2012.round_2.mountain_view.Simplex;

import com.google.common.collect.Lists;

public class SimplexTest {

    
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
        
        List<Double> solutions = Lists.newArrayList();
        
        //boolean f = s.doPhase1(solutions);
        
        //assertEquals(1, solutions.get(0), 1e-5);
       // assertEquals(0, solutions.get(1), 1e-5);
        
        
        s = new Simplex(4);
        s.addConstraintEquals(Arrays.asList(1d, 2d, 0d, 1d), 6d);
        
        s.addConstraintEquals(Arrays.asList(1d, 2d, 1d, 1d), 7d);
        
        s.addConstraintEquals(Arrays.asList(1d, 3d, -1d, 2d), 7d);
        
        s.addConstraintEquals(Arrays.asList(1d, 1d, 1d, 0d), 5d);
        
       // s.doPhase1(solutions);
        
    }
    
    
    @Test
    public void testExampleChapter2()
    {
        Simplex s = new Simplex(2);
        
        /**
         * Maximize Z=15x_1 + 10x_2
         * 
         * x1 <= 2
         * x2 <= 3
         * x1 + x2 <= 4
         */
        s.addObjectiveFunction(Arrays.asList(15d, 10d));
        
        s.addConstraintLTE(Arrays.asList(1d, 0d), 2d);
        
        s.addConstraintLTE(Arrays.asList(0d, 1d), 3d);
        
        s.addConstraintLTE(Arrays.asList(1d, 1d), 4d);
        
        List<Double> solutions = Lists.newArrayList();
        s.solve(solutions);
        
        assertEquals(2, solutions.size());
        assertEquals(2d, solutions.get(0), 1e-5);
        assertEquals(2d, solutions.get(1), 1e-5);
    }

}
