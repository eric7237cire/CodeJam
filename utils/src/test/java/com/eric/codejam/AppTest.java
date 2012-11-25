package com.eric.codejam;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.eric.codejam.geometry.Circle;
import com.eric.codejam.geometry.Point;
import com.eric.codejam.geometry.Rectangle;
import com.eric.codejam.utils.DoubleComparator;
import com.eric.codejam.utils.GraphAdjList;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(  )
    {
        super(  );
    }

    @Test
    public void testRectTouching()
    {
        Rectangle r1 = new Rectangle(5, 5, 7, 7);
        Rectangle r2 = new Rectangle(3, 3, 9, 9);
        
        assertTrue(r1.touchesHorVer(r2));
        
        r1 = new Rectangle(2, 5, 2, 7);
        r2 = new Rectangle(3, 1, 3, 5);
        
        assertTrue(r1.touchesHorVer(r2));
        
        r1 = new Rectangle(2, 6, 2, 7);
        r2 = new Rectangle(3, 1, 3, 5);
        
        assertFalse(r1.touchesHorVer(r2));
        
        r1 = new Rectangle(2, 6, 2, 7);
        r2 = new Rectangle(2, 1, 2, 5);
        
        assertTrue(r1.touchesHorVer(r2));
        
        r1 = new Rectangle(7, 14, 7, 17);
        r2 = new Rectangle(3, 1, 7, 14);
        
        assertTrue(r1.touchesHorVer(r2));
        
        r1 = new Rectangle(92, 3, 93, 98);
        r2 = new Rectangle(63, 57, 92, 58);
        
        assertTrue(r1.touchesHorVer(r2));
        assertTrue(r2.touchesHorVer(r1));
    }
    
    @Test
    public void testAdjGraph() {
        GraphAdjList graph = new GraphAdjList(9);
        graph.addConnection(1, 8);
        graph.addConnection(8, 6);
        
        graph.addConnection(4, 2);
        graph.addConnection(7, 4);
        graph.addConnection(2, 7);
        
        graph.addConnection(3, 3);
        
        graph.addConnection(5, 5);
        
        List<List<Integer>> cc = graph.getConnectedComponents();
        assertEquals(4, cc.size());
        
        for(int i = 0; i < 4; ++i)
            Collections.sort(cc.get(i));
        
        assertEquals(Arrays.asList(1, 6, 8), cc.get(0));
        assertEquals(Arrays.asList(2, 4, 7), cc.get(1));
        assertEquals(Arrays.asList(3), cc.get(2));
        assertEquals(Arrays.asList(5), cc.get(3));
    }
    
    @Test
    public void testIntersectionCircles() {
        Circle circle1 = new Circle(2,2,4);
        Circle circle2 = new Circle(5,-5,5);
        
        Point[] points = circle1.getIntersection(circle2);
        
        assertTrue( 0 == DoubleComparator.compareStatic(points[0].getX(), 5.452190844));
        assertTrue( 0 == DoubleComparator.compareStatic(points[0].getY(), -0.020489638));
        assertTrue( 0 == DoubleComparator.compareStatic(points[1].getX(), 1.082291915));
        assertTrue( 0 == DoubleComparator.compareStatic(points[1].getY(), -1.893303465));
        
        
    }
}
