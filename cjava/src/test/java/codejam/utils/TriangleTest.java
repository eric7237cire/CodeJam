package codejam.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.Polygon;
import codejam.utils.geometry.Triangle;


public class TriangleTest
{

    final static Logger log = LoggerFactory.getLogger(TriangleTest.class);
    
    @Test
    public void testClockwise() {
        
        //Points are given in clockwise order
        Point pointTest[][] = new Point[][] {
                {
                    new Point(3, 1),
                    new Point(2, 5),
                    new Point(1, 2)
                },
                {
                    new Point(-5, 7),
                    new Point(6, 2),
                    new Point(3, 6)                    
                },
                {
                    new Point(5, 0),
                    new Point(-6, 0),
                    new Point(-7, -3)                    
                },
                {
                    new Point(-2, -3),
                    new Point(4, -3),
                    new Point(1, 1)                    
                }
        };
        
        for (Point[] pts : pointTest) {
        Point p1 = pts[0];
        Point p2 = pts[1];
        Point p3 = pts[2];
        
        //Counter clockwise
        Triangle t1 = new Triangle(p1,p2,p3);
        Triangle t2 = new Triangle(p3,p1,p2);
        Triangle t3 = new Triangle(p2,p3,p1);
        
        assertTrue(t1.pointsCounterClockwise());
        assertTrue(t2.pointsCounterClockwise());
        assertTrue(t3.pointsCounterClockwise());
        
        //Clockwise
        Triangle t4 = new Triangle(p3,p2,p1);
        Triangle t5 = new Triangle(p1,p3,p2);
        Triangle t6 = new Triangle(p2,p1,p3);
         
        assertFalse(t4.pointsCounterClockwise());
        assertFalse(t5.pointsCounterClockwise());
        assertFalse(t6.pointsCounterClockwise());
        }
    }
    
    @Test
    public void triangleInt() {
        Triangle t = new Triangle(
                new Point(-1, -3),
                new Point(-4, 4),
                new Point(1, 5));
        
        Triangle t2 = new Triangle(
                new Point(-2, 5),
                new Point(3, 1),
                new Point(-2, -2)
                );
        
        
        List<Point> poly = t.getTriangleIntersection(t2);
        
        for(Point p : poly) {
            log.debug("P {}", p);
        }
        
        double a = Polygon.area(poly);
        Collections.reverse(poly);
        double a2 = Polygon.area(poly);
        
        assertEquals(a, a2, 0.0000001);
    }
    
    @Test
    public void testOverlapIntersection() {
        //Larger triangle
        Triangle t = new Triangle(
                new Point(0, 4),
                new Point(4, 0),
                new Point(0, 0));
        
        Triangle t2 = new Triangle(
                new Point(0, 4),
                new Point(3, 1),
                new Point(0, 1)
                );
        
        List<Point> p = t.getTriangleIntersection(t2);
        
        assertEquals(3, p.size());
        
        assertTrue(p.contains(t2.p1));
        assertTrue(p.contains(t2.p2));
        assertTrue(p.contains(t2.p3));
        
        
        Triangle t3 = new Triangle(
                new Point(0, 4),
                new Point(1, 1),
                new Point(0, 1)
                );
        
        p = t.getTriangleIntersection(t3);
        
        assertEquals(3, p.size());
        
        assertTrue(p.contains(t3.p1));
        assertTrue(p.contains(t3.p2));
        assertTrue(p.contains(t3.p3));
    }

    @Test
    public void testTriangeLineIntersection() {
        Triangle t = new Triangle(
                new Point(1, 2),
                new Point(1, 100),
                new Point(0, 100));
        
      
        
        Line l = new Line(new Point(1,1),new Point(1,100));
        
        List<Point> inter = t.getIntersectionWithLine(l);
        
        assertEquals(2, inter.size());
    }
    
    @Test
    public void testTPar() {
        Line l = new Line(new Point(1, 1),
        new Point(1, 100));
        
        Line l2 = new Line(new Point(1, 2),
                new Point(1, 100));
        
        Line l3 = new Line(new Point(1.001, 2),
                new Point(1, 100));
        
        
        assertTrue(l.isParallel(l2));
        assertTrue(l2.isParallel(l));
        
        assertFalse(l.isParallel(l3));
        assertFalse(l3.isParallel(l));
    }

}
