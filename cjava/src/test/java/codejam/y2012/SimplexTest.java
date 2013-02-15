package codejam.y2012;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import codejam.utils.linear.Simplex;

import com.google.common.collect.Lists;

public class SimplexTest {

 
    
    
    @Test
    public void testExampleChapter4()
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
        
        assertEquals(3, solutions.size());
        assertEquals(2d, solutions.get(0), 1e-5);
        assertEquals(2d, solutions.get(1), 1e-5);
        
        assertEquals(50d, solutions.get(2), 1e-5);
    }
    
    @Test
    public void testExampleChapter5()
    {
        Simplex s = new Simplex(2);
        
        /**
         * Maximize Z=15x_1 + 10x_2
         * 
         * x1 <= 2
         * x2 <= 3
         * x1 + x2 == 4
         */
        s.addObjectiveFunction(Arrays.asList(15d, 10d));
        
        s.addConstraintLTE(Arrays.asList(1d, 0d), 2d);
        
        s.addConstraintLTE(Arrays.asList(0d, 1d), 3d);
        
        s.addConstraintEquals(Arrays.asList(1d, 1d), 4d);
        
        List<Double> solutions = Lists.newArrayList();
        
        s.solve(solutions);
        
        assertEquals(3, solutions.size());
        assertEquals(2d, solutions.get(0), 1e-5);
        assertEquals(2d, solutions.get(1), 1e-5);
        
        assertEquals(50d, solutions.get(2), 1e-5);
    }
    
    @Test
    public void testExampleChap04_page134()
    {
        Simplex s = new Simplex(2);
        
        /**
         * Maximize Z=3x_1 + 5x_2
         * 
         * x1 <= 4
         * 2x2 <= 12
         * 3x1 + 2x2 == 18
         */
        s.addObjectiveFunction(Arrays.asList(3d, 5d));
        
        s.addConstraintLTE(Arrays.asList(1d, 0d), 4d);
        
        s.addConstraintLTE(Arrays.asList(0d, 2d), 12d);
        
        s.addConstraintEquals(Arrays.asList(3d, 2d), 18d);
        
        List<Double> solutions = Lists.newArrayList();
        
        s.solve(solutions);
        
        assertEquals(3, solutions.size());
        assertEquals(2d, solutions.get(0), 1e-5);
        assertEquals(6d, solutions.get(1), 1e-5);
        
        //Z
        assertEquals(36d, solutions.get(2), 1e-5);
    }
    
    @Test
    public void testExampleChap04_page137()
    {
        Simplex s = new Simplex(2);
        
        /**
         * Minimize Z=.4x_1 + .5x_2
         * 
         * .3x1 + .1x2 <= 2.7
         * .5x1 + .5x2 = 6
         * .6x1 + .4x2 >= 6
         */
        s.addObjectiveFunctionToMinimize(Arrays.asList(.4, .5));
        
        s.addConstraintLTE(Arrays.asList(.3, .1), 2.7);
        
        s.addConstraintEquals(Arrays.asList(.5, .5), 6d);
        
        s.addConstraintGTE(Arrays.asList(.6, .4), 6d);
        
        List<Double> solutions = Lists.newArrayList();
        
        s.solve(solutions);
        
        assertEquals(3, solutions.size());
        assertEquals(7.5d, solutions.get(0), 1e-5);
        assertEquals(4.5d, solutions.get(1), 1e-5);
        
        //Z
        assertEquals(5.25d, solutions.get(2), 1e-5);
    }

}
