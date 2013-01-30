package codejam.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.math.DoubleMath;

import codejam.utils.geometry.Angle;
import codejam.utils.geometry.Circle;
import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.utils.DoubleComparator;
import codejam.utils.visual.Display;

public class CircleTest {
    private final static double DOUBLE_THRESHOLD = 0.000001d;

    final static Logger log = LoggerFactory.getLogger(CircleTest.class);

    @Test(expected = IllegalArgumentException.class)
    public void testPolar() {
        Angle.comparePolar(1.57, 2.5, 1.8, 2.6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPolar2() {
        Angle.comparePolar(Math.PI, -Math.PI / 2, Math.PI + .35, -Math.PI / 4);
    }

    public void testPolar3() {
        int c = Angle.comparePolar(3, -3, -Math.PI - .03, Math.PI - .02);
        assertEquals(-1, c);

        c = Angle.comparePolar(3, -3, -Math.PI - .03, Math.PI - .04);
        assertEquals(1, c);
    }
    
    @Test
    public void testTangent() {
        Circle c = new Circle(10, 6, 3);
        
        Point C = c.getCenter();
        Point P = new Point(6,8);
        
        Point[] tangentPoints = c.getPointsTangentToLine(P);
        Point T = tangentPoints[0];
        Point T2 = tangentPoints[1];
        
        Point vecP = P.translate(T).normalize();
        Point vecC = C.translate(T).normalize();
        
        //Tangent TP is perpendicular to CT
        double dot = Point.dotProduct(vecP, vecC);
        
        assertEquals(0, dot, 0.0000001);
        
        assertTrue( c.onCircle(T) );
        assertTrue( c.onCircle(T2) );
                
        assertEquals( P.distance(T), P.distance(T2), 0.0001);
        assertFalse(T.equals(T2));
        //Intersection should be the same
        /*
        Point[] pts = c.getPointsIntersectingLine(new Line(P,T));
        Point[] pts2 = c.getPointsIntersectingLine(new Line(P,T2));
       
        assertEquals(pts[0], pts[1]);
        assertEquals(pts2[0], pts2[1]);
        
        assertEquals(T, pts[0]);
        assertEquals(T2, pts2[0]);
        */
    }

    @Test
    public void testIntersectionCircles() {
        Circle circle1 = new Circle(2, 2, 4);
        Circle circle2 = new Circle(5, -5, 5);

        Point[] points = circle1.getIntersection(circle2);

        assertTrue(0 == DoubleComparator.compareStatic(points[0].getX(),
                5.452190844));
        assertTrue(0 == DoubleComparator.compareStatic(points[0].getY(),
                -0.020489638));
        assertTrue(0 == DoubleComparator.compareStatic(points[1].getX(),
                1.082291915));
        assertTrue(0 == DoubleComparator.compareStatic(points[1].getY(),
                -1.893303465));

    }

    @Test
    public void testSlopeYInt() {
        Point p = new Point(2, 7);
        Point p2 = new Point(4, 11);
        Point p3 = new Point(-2, -1);
        double[] mb = p.getSlopeAndYIntercept(p2);

        assertEquals(2d, mb[0], DOUBLE_THRESHOLD);
        assertEquals(3d, mb[1], DOUBLE_THRESHOLD);

        mb = p2.getSlopeAndYIntercept(p);

        assertEquals(2d, mb[0], DOUBLE_THRESHOLD);
        assertEquals(3d, mb[1], DOUBLE_THRESHOLD);

        mb = p3.getSlopeAndYIntercept(p);

        assertEquals(2d, mb[0], DOUBLE_THRESHOLD);
        assertEquals(3d, mb[1], DOUBLE_THRESHOLD);
    }

    @Test
    public void testIntersectingPoint() {
        Circle c1 = null;
        Point p = null;

        c1 = new Circle(3, 3, 1);
        p = new Point(0, 0);

        Line l = new Line(p, c1.getCenter());

        Point[] pts1 = c1.getPointsIntersectingLineOriginatingAtP(p);

        log.debug("{} {}", pts1[0], pts1[1]);

        assertTrue(l.onLine(pts1[0]));
        assertTrue(l.onLine(pts1[1]));
        assertTrue(c1.onCircle(pts1[0]));
        assertTrue(c1.onCircle(pts1[0]));
        assertFalse(pts1[0].equals(pts1[1]));

        Point[] pts2 = c1.getPointsIntersectingLineOriginatingAtP_second(p);

        log.debug("{} {}", pts2[0], pts2[1]);

        assertTrue(l.onLine(pts2[0]));
        assertTrue(l.onLine(pts2[1]));
        assertTrue(c1.onCircle(pts2[0]));
        assertTrue(c1.onCircle(pts2[0]));
        assertFalse(pts1[0].equals(pts2[1]));

    }

    @Test
    public void testIntersectingPoint2() {
        Circle c1 = null;
        Point p = null;

        c1 = new Circle(7, 2, 5);
        p = new Point(-3, 4);

        Line l = new Line(p, c1.getCenter());

        Point[] pts1 = c1.getPointsIntersectingLineOriginatingAtP(p);

        log.debug("{} {}", pts1[0], pts1[1]);

        Point[] pts2 = c1.getPointsIntersectingLineOriginatingAtP_second(p);

        log.debug("{} {}", pts2[0], pts2[1]);

        assertTrue(l.onLine(pts1[0]));
        assertTrue(l.onLine(pts1[1]));
        assertTrue(c1.onCircle(pts1[0]));
        assertTrue(c1.onCircle(pts1[0]));
        assertFalse(pts1[0].equals(pts1[1]));

        assertTrue(l.onLine(pts2[0]));
        assertTrue(l.onLine(pts2[1]));
        assertTrue(c1.onCircle(pts2[0]));
        assertTrue(c1.onCircle(pts2[0]));
        assertFalse(pts1[0].equals(pts2[1]));

    }

    @Test
    public void testIntersectingPointVertical() {
        Circle c1 = null;
        Point p = null;

        c1 = new Circle(-3, -3, 7);
        p = new Point(-3, -14);

        Line l = new Line(p, c1.getCenter());

        Point closePoint = new Point(-3, -10);
        Point farPoint = new Point(-3, 4);

        Point[] pts1 = c1.getPointsIntersectingLineOriginatingAtP(p);

        assertEquals(pts1[0], closePoint);
        assertEquals(pts1[1], farPoint);

        log.debug("{} {}", pts1[0], pts1[1]);

        assertTrue(l.onLine(pts1[0]));
        assertTrue(l.onLine(pts1[1]));
        assertTrue(c1.onCircle(pts1[0]));
        assertTrue(c1.onCircle(pts1[0]));
        assertFalse(pts1[0].equals(pts1[1]));

        Point[] pts2 = c1.getPointsIntersectingLineOriginatingAtP_second(p);

        log.debug("{} {}", pts2[0], pts2[1]);

        assertTrue(l.onLine(pts2[0]));
        assertTrue(l.onLine(pts2[1]));
        assertTrue(c1.onCircle(pts2[0]));
        assertTrue(c1.onCircle(pts2[0]));
        assertFalse(pts1[0].equals(pts2[1]));

        assertEquals(pts1[0], pts2[0]);
        assertEquals(pts1[1], pts2[1]);

    }

    @Test
    public void testIntersectingPointHorizontal() {
        Circle c1 = null;
        Point p = null;

        c1 = new Circle(5, -3, 2);
        p = new Point(15, -3);

        Line l = new Line(p, c1.getCenter());

        Point closePoint = new Point(7, -3);
        Point farPoint = new Point(3, -3);

        Point[] pts1 = c1.getPointsIntersectingLineOriginatingAtP(p);

        assertEquals(pts1[0], closePoint);
        assertEquals(pts1[1], farPoint);

        log.debug("{} {}", pts1[0], pts1[1]);

        assertTrue(l.onLine(pts1[0]));
        assertTrue(l.onLine(pts1[1]));
        assertTrue(c1.onCircle(pts1[0]));
        assertTrue(c1.onCircle(pts1[0]));
        assertFalse(pts1[0].equals(pts1[1]));

        Point[] pts2 = c1.getPointsIntersectingLineOriginatingAtP_second(p);

        log.debug("{} {}", pts2[0], pts2[1]);

        assertTrue(l.onLine(pts2[0]));
        assertTrue(l.onLine(pts2[1]));
        assertTrue(c1.onCircle(pts2[0]));
        assertTrue(c1.onCircle(pts2[0]));
        assertFalse(pts1[0].equals(pts2[1]));

        assertEquals(pts1[0], pts2[0]);
        assertEquals(pts1[1], pts2[1]);
    }

    @Test
    public void testCircle3Circles() {
        Circle a = new Circle(2, 3, 3);

        Circle b = new Circle(4.4142135623731, 1.5857864376269, 2);

        Circle c = new Circle(3, 6, 1);

        Circle ans = Circle.getCircleContaining(a, b, c);

        assertEquals(3, ans.getX(), DoubleComparator.TOLERANCE);
        assertEquals(3, ans.getY(), DoubleComparator.TOLERANCE);
        assertEquals(4, ans.getR(), DoubleComparator.TOLERANCE);

        b = new Circle(5, 3, 2);
        // Circle b = new Circle(4.4142135623731, 1.5857864376269, 2);

        ans = Circle.getCircleContaining(a, b, c);

        Display d = new Display(600, 600);
        d.addCircle(a);
        d.addCircle(b);
        d.addCircle(c);
        d.addCircle(ans);

        // d.setVisible(true);

        assertEquals(3, ans.getX(), DoubleComparator.TOLERANCE);
        assertEquals(3, ans.getY(), DoubleComparator.TOLERANCE);
        assertEquals(4, ans.getR(), DoubleComparator.TOLERANCE);

    }

    

    @Test
    public void testCircle2Points() {
        Circle a = new Circle(10, 7, 5);

        Circle b = new Circle(90, 7, 15);

        Circle ans = Circle.getCircleContaining(a, b);

        assertEquals(55, ans.getX(), DoubleComparator.TOLERANCE);
        assertEquals(7, ans.getY(), DoubleComparator.TOLERANCE);
        assertEquals(50, ans.getR(), DoubleComparator.TOLERANCE);

        a = new Circle(430, 1000, 84);

        b = new Circle(288, 572, 21);

        ans = Circle.getCircleContaining(a, b);

        assertTrue(ans.contains(a));
        assertTrue(ans.contains(b));
    }

    @Test
    public void testCircle2PointsVertical() {

        Circle a = new Circle(200, 100, 1);

        Circle b = new Circle(200, 103, 1);

        Circle ans = Circle.getCircleContaining(a, b);

        assertEquals(200, ans.getX(), DoubleComparator.TOLERANCE);
        assertEquals(101.5, ans.getY(), DoubleComparator.TOLERANCE);
        assertEquals(2.5, ans.getR(), DoubleComparator.TOLERANCE);
    }

   

    @Test
    public void testInside() {
        Circle p1 = new Circle(100.0, 100.0, 1.0);
        Circle p2 = new Circle(200.0, 100.0, 1.0);
        Circle s = new Circle(150.0, 100.0, 51.0);

        assertTrue(s.contains(p1));
        assertTrue(s.contains(p2));

        p1 = new Circle(100.0, 130.0, 1.0);
        p2 = new Circle(150.0, 500.0, 1.0);
        s = Circle.getCircleContaining(p1, p2);

        assertTrue(s.contains(p1));
        assertTrue(s.contains(p2));
    }
}
