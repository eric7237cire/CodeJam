package codejam.utils.geometry;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


public class Triangle {

    final public Point p1;

    final public Point p2;
    final public Point p3;
    
    boolean intersection(Point a) {
        return false;
    }
    
    public Triangle(Point p1, Point p2, Point p3) {
        super();
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public boolean pointInTriangle(Point p) {
        if (Line.sameSide(p, p3, p1, p2)
                && Line.sameSide(p, p2, p1, p3)
                && Line.sameSide(p, p1, p2, p3))
            return true;
        else
            return false;
    }
    
    public boolean pointsCounterClockwise() {
        
        Point vecP3P1 = p1.translate(p3);
        Point vecP1P2 = p2.translate(p1);
        Point vecP2P3 = p3.translate(p2);
        
        double c1 = Point.crossProduct(vecP3P1, vecP1P2);
        double c2 = Point.crossProduct(vecP1P2, vecP2P3);
        double c3 = Point.crossProduct(vecP2P3, vecP3P1);
        
        if (c1 > 0 && c2 > 0 && c3 > 0) {
            return true;
        }
        
        if (c1 < 0 && c2 < 0 && c3 < 0) {
            return false;
        }
        
        //Not a triangle?
        Preconditions.checkState(false);
        
        return true;
    }
    
    public List<Point> getTriangleIntersection(Triangle other)
    {
        List<Point> ret = Lists.newArrayList();
        
        if (other.pointInTriangle(p1)) {
            ret.add(p1);
        }
        
        List<Point> p1p2 = other.getIntersectionWithLine(new Line(p1,p2));
        
        addPoints(p1p2, ret, p1);
        
        if (other.pointInTriangle(p2)) {
            ret.add(p2);
        }
        
        List<Point> p2p3 = other.getIntersectionWithLine(new Line(p2,p3));
        
        addPoints(p2p3, ret, p2);
        
        if (other.pointInTriangle(p3)) {
            ret.add(p3);
        }
        
        List<Point> p3p1 = other.getIntersectionWithLine(new Line(p3, p1));
        
        addPoints(p3p1, ret, p3);
        
        return ret;
        
    }
    
    /**
     * Want to add in counter clockwise order, so if there are 2 int points
     * add the one closest to refPoint first
     * @param intPoints
     * @param polygon
     * @param refPoint
     */
    private void addPoints(List<Point> intPoints, List<Point> polygon, Point refPoint) {
        if (intPoints.size() == 0)
            return;
        
        if (intPoints.size() == 1) {
            polygon.add(intPoints.get(0));
            return;
        }
        
        Preconditions.checkState((intPoints.size() == 2));
        
        double dist1 = intPoints.get(0).distance2(refPoint);
        double dist2 = intPoints.get(1).distance2(refPoint);
        
        if (dist1 < dist2) {
            polygon.add(intPoints.get(0));
            polygon.add(intPoints.get(1));
        } else {
            polygon.add(intPoints.get(1));
            polygon.add(intPoints.get(0));
        }
        
        
    }
    
    public List<Point> getIntersectionWithLine(Line line) {
        
        //Preconditions.checkState(pointsCounterClockwise());
        
        Line[] triLines = new Line[] { new Line(p1,p2),
         new Line(p2,p3),
         new Line(p3,p1) };
        
        List<Point> ret = Lists.newArrayList();
        
        for(Line triangleSegment : triLines) {
            Point p = line.getIntersection(triangleSegment);
            
            if (triangleSegment.onLineSegment(p)) {
                ret.add(p);
            }
        }
        
        Preconditions.checkState(ret.size() <= 2);
        
        return ret;
    }
    
}
