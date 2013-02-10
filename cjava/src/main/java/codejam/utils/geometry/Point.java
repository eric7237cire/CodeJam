package codejam.utils.geometry;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.apache.commons.math3.complex.Complex;

import com.google.common.base.Objects;
import com.google.common.math.DoubleMath;

public class Point implements Comparable<Point> {
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
    
    static DecimalFormat df;
    
    static {
        df = new DecimalFormat("0.###");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        
        return String.format("(%s, %s)", df.format(x), df.format(y));
    }
    
    public Complex toComplex() {
        return new Complex(getX(), getY());
    }
    
    public Point translate(Point newOrigin) {
        return new Point(x - newOrigin.getX(), y - newOrigin.getY());
    }
    
    public Point add(Point point) {
        return new Point(x + point.getX(), y + point.getY());   
    }
    
    public Point rotate(double ang) {
        double cosTh = Math.cos(ang);
        double sinTh = Math.sin(ang);
        return new Point(getX() * cosTh - getY() * sinTh,
                getX() * sinTh + getY() * cosTh);
    }
    
    public Point rotateAbout(Point pivot, double ang) {
        return translate(pivot).rotate(ang).translate( pivot.scale(-1));
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
    
    
    public double distance2(Point other) {
        return ( (x-other.x)*(x-other.x)+(y-other.y)*(y-other.y));
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        
       return Objects.hashCode(roundDouble(x), roundDouble(y));
    }
    
    //TODO
    public static double roundDouble(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) 
            return d;
        BigDecimal bd = new BigDecimal(d);
        int decimalPlace = 3;
    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
    return bd.doubleValue();
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
        
        return DoubleMath.fuzzyEquals(x, other.x, tolerance)
        && DoubleMath.fuzzyEquals(y, other.y, tolerance);
    }
    
    @Override
    public int compareTo(Point other) {
        int yc = DoubleMath.fuzzyCompare(x, other.x, tolerance);
        
        if (yc != 0) 
            return yc;
        
        return DoubleMath.fuzzyCompare(y, other.y, tolerance);
    }
    
    public static double tolerance = .000002d;
    
    public boolean equalsPointInt(PointInt obj) {
        
         
        if (!DoubleMath.fuzzyEquals(x, obj.getX(), tolerance))
            return false;
        if (!DoubleMath.fuzzyEquals(y, obj.getY(), tolerance))
            return false;
        return true;
    }
    
    //Treating points as vectors from 0,0.  Return value is the z component of a 3d vector
    static public double crossProduct(Point u, Point v) {
        return u.getX() * v.getY() - u.getY() * v.getX();
    }
    
    static public double dotProduct(Point u, Point v) {
        return u.getX() * v.getX() + u.getY() * v.getY();
    }
    
    public double polarAngle() {
        double ang = Math.atan2(getY(), getX());
        if (ang < 0) {
            ang += 2 * Math.PI;
        }
        return ang;
    }
    
    public Point normalize() {
        double len = new Point(0,0).distance(this);
        return new Point(getX() / len, getY() / len);
    }
}
