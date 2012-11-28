package com.eric.codejam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import mod.GCD;

import org.junit.Test;

import com.eric.codejam.geometry.Line;
import com.eric.codejam.geometry.Point;
import com.eric.codejam.geometry.Polygon;
import com.eric.codejam.geometry.Rectangle;
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
    public void testGCD() {
        int[] gcdXY = GCD.gcdExtended(120, 23);
        assertEquals(1, gcdXY[0]);
        assertEquals(-9, gcdXY[1]);
        assertEquals(47, gcdXY[2]);
    }
    
    @Test
    public void testPolygon() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(3,4));
        points.add(new Point(5,6));
        points.add(new Point(9,5));
        points.add(new Point(12,8));
        points.add(new Point(5,11));
        
        
        double area = Polygon.area(points);
        
        assertEquals(30, area, 0.00001d);
    }
    
    @Test
    public void isBetween() {
        Line l = new Line(2, 1);
        Point a = l.getPointGivenX(3);
        Point b = l.getPointGivenX(-3);
        Point c = l.getPointGivenX(2);
        
        assertTrue(Line.isBetween(b,a,c));
        assertFalse(Line.isBetween(a,c,b));
        assertTrue(Line.isBetween(a,b,c));
        assertFalse(Line.isBetween(c,b,a));
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
    

}
