package codejam.utils.geometry;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


public class Triangle {

    final static Logger log = LoggerFactory.getLogger(Triangle.class);
    
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
        Set<Point> ret = Sets.newHashSet();
        
        List<Line> linesT1 = Lists.newArrayList();
        
        linesT1.add(new Line(p1,p2));
        linesT1.add(new Line(p2,p3));
        linesT1.add(new Line(p1,p3));
        
        List<Line> linesT2 = Lists.newArrayList();
        
        linesT2.add(new Line(other.p1,other.p2));
        linesT2.add(new Line(other.p2,other.p3));
        linesT2.add(new Line(other.p1,other.p3));
        
        for(Line l1 : linesT1) {
            for(Line l2 : linesT2) {
                
                //log.debug("Ret {}", ret);
               // log.debug("Line 1 / 2 {} {}", l1, l2);
                
                if (l1.isParallel(l2)) {
                    //Only accept points that are between the other line 
                    //ret.clear();
                    if (l1.onLineSegment(l2.getP1())) {
                        ret.add(l2.getP1());
                    }
                    if (l1.onLineSegment(l2.getP2())) {
                        ret.add(l2.getP2());
                    }
                    if (l2.onLineSegment(l1.getP1()) ) {
                        ret.add(l1.getP1());
                    }
                    if (l2.onLineSegment(l1.getP2()) ) {
                        ret.add(l1.getP2());
                    }
                    continue;
                }
                
                Point p = l1.getIntersection(l2);
                if (l1.onLineSegment(p) && l2.onLineSegment(p))
                    ret.add(p);
                
                if (pointInTriangle(l2.getP1())) {
                    ret.add(l2.getP1());
                }
                if (pointInTriangle(l2.getP2())) {
                    ret.add(l2.getP2());
                }
                if (other.pointInTriangle(l1.getP1())) {
                    ret.add(l1.getP1());
                }
                if (other.pointInTriangle(l1.getP2())) {
                    ret.add(l1.getP2());
                }
            }
        }
        
        double cx=0, cy=0;
        
        if (ret.size() < 3)
            return null;
        
        for(Point p : ret) {
            cx += p.getX();
            cy += p.getY();
        }
        
        cx /= ret.size();
        cy /= ret.size();
        
        final Point center = new Point(cx,cy);
        
       List<Point> retList = Lists.newArrayList(ret);
        
       //Order by angle with center
       Collections.sort(retList,new Comparator<Point>(){

        @Override
        public int compare(Point o1, Point o2)
        {
            return Double.compare(o1.translate(center).polarAngle(),
                    o2.translate(center).polarAngle());
        }
           
       });
       
          
        return retList;
        
    }
    
   
    
    public List<Point> getIntersectionWithLine(Line line) {
        
        //Preconditions.checkState(pointsCounterClockwise());
        
        Line[] triLines = new Line[] { new Line(p1,p2),
         new Line(p2,p3),
         new Line(p3,p1) };
        
        List<Point> ret = Lists.newArrayList();
        
        for(Line triangleSegment : triLines) {
            
            if (line.isParallel(triangleSegment)) {
                //Only accept points that are between the other line 
                ret.clear();
                if (line.onLineSegment(triangleSegment.getP1())) {
                    ret.add(triangleSegment.getP1());
                }
                if (line.onLineSegment(triangleSegment.getP2())) {
                    ret.add(triangleSegment.getP2());
                }
                if (triangleSegment.onLineSegment(line.getP1()) && !ret.contains(line.getP1())) {
                    ret.add(line.getP1());
                }
                if (triangleSegment.onLineSegment(line.getP2()) && !ret.contains(line.getP2())) {
                    ret.add(line.getP2());
                }
                return ret;
            }
            
            Point p = line.getIntersection(triangleSegment);
            
            if (triangleSegment.onLineSegment(p) && !ret.contains(p)) {
                ret.add(p);
            }
        }
        
        Preconditions.checkState(ret.size() <= 2);
        
        return ret;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Triangle [p1=" + p1 + ", p2=" + p2 + ", p3=" + p3 + "]";
    }
    
}
