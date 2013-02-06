package codejam.utils.geometry;

import java.util.List;

import com.google.common.collect.Lists;



public class TriangleInt {
    //x, y
    final public PointInt p1;

    final public PointInt p2;
    final public PointInt p3;
    
    
    public TriangleInt(int x1, int y1, int x2, int y2, int x3, int y3) {
        super();
        this.p1 = new PointInt(x1,y1);
        this.p2 = new PointInt(x2,y2);
        this.p3 = new PointInt(x3,y3);
    }
    
    public List<Double> getSides() {
        double s1 = p1.distance(p2);
        double s2 = p2.distance(p3);
        double s3 = p1.distance(p3);
        
        List<Double> ret = Lists.newArrayList();
        ret.add(s1);
        ret.add(s2);
        ret.add(s3);
        
        
        return ret;
    }
    
    public static double lawCosines(double a, double b, double c)
    {
        double l = (c * c - a * a - b * b) / (-2 * a * b);
        
        return Math.acos(l);
    }
    
    
    public boolean pointInTriangle(Point p) {
        if (Line.sameSide(p, p3.toPoint(), p1.toPoint(), p2.toPoint())
                && Line.sameSide(p, p2.toPoint(), p1.toPoint(), p3.toPoint())
                && Line.sameSide(p, p1.toPoint(), p2.toPoint(), p3.toPoint()))
            return true;
        else
            return false;
    }
}
