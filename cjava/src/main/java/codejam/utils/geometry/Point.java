package codejam.utils.geometry;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.utils.DoubleComparator;

import com.google.common.base.Objects;

public class Point {
    final private double x;
    final private double y;
    
    public Point(PointInt p) {
        x = p.getX();
        y = p.getY();
    }
    /**
     * @return the x
     */
    public double getX() {
        return x;
    }
    
    /**
     * @return the y
     */
    public double getY() {
        return y;
    }
    
    public Point(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }
    public static Point getMidPoint(Point a, Point b) {
        return new Point((a.x+b.x)/2,(a.y+b.y)/2);
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }
    
    public Point translate(Point newOrigin) {
        return new Point(x - newOrigin.getX(), y - newOrigin.getY());
    }
    
    public Point rotate(double ang) {
        double cosTh = Math.cos(ang);
        double sinTh = Math.sin(ang);
        return new Point(getX() * cosTh - getY() * sinTh,
                getX() * sinTh + getY() * cosTh);
    }
    
    public Point scale(double factor) {
        return new Point(getX() * factor, getY() * factor);
    }
    /*
     *  y - y.s = (y.p - y.s) / (x.p - x.s)  (x - x.s)
     * m = (y.p - y.s) / (x.p - x.s)
     * b = y.s-m*x.s
     */
    public double[] getSlopeAndYIntercept(Point other) {
        if (this.x == other.x) {
            throw new IllegalArgumentException("Vertical");
        }
        
        double m = (other.y - y ) / (other.x - x);
        double b = other.y - m * other.x;
        
        return new double[] { m, b };
    }
    
    public double distance(Point other) {
        return Math.sqrt( (x-other.x)*(x-other.x)+(y-other.y)*(y-other.y));
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
       return Objects.hashCode(x, y);
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        if (0 != new DoubleComparator().compare(x, other.x))
            return false;
        if (0 != new DoubleComparator().compare(y, other.y))
            return false;
        return true;
    }
    
    public boolean equalsPointInt(PointInt obj) {
        
         double tolerance = .000002d;
        if (0 != new DoubleComparator(tolerance).compare(x, (double)obj.getX()))
            return false;
        if (0 != new DoubleComparator(tolerance).compare(y, (double)obj.getY()))
            return false;
        return true;
    }
    
    //Treating points as vectors from 0,0.  Return value is the z component of a 3d vector
    static public double crossProduct(Point u, Point v) {
        return u.getX() * v.getY() - u.getY() * v.getX();
    }
}
