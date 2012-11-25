package com.eric.codejam.geometry;

import com.eric.codejam.utils.DoubleComparator;
import com.google.common.base.Objects;

public class Point {
    private double x;
    private double y;
    /**
     * @return the x
     */
    public double getX() {
        return x;
    }
    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }
    /**
     * @return the y
     */
    public double getY() {
        return y;
    }
    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
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
}
