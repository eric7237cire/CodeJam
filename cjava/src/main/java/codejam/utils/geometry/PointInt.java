package codejam.utils.geometry;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.math.LongMath;

public class PointInt implements Comparable<PointInt>{
    private int x;
    private int y;
    /**
     * @return the x
     */
    public int x() {
        return x;
    }
    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }
    /**
     * @return the y
     */
    public int y() {
        return y;
    }
    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }
    public PointInt(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }
    
    public static PointInt add(PointInt a, PointInt b) {
        return new PointInt(a.x() + b.x(), a.y() + b.y());   
    }
    
    public PointInt add(int[] xy) {
        return new PointInt(x() + xy[0], y() + xy[1]);
    }
    
    /*
     *  y - y.s = (y.p - y.s) / (x.p - x.s)  (x - x.s)
     * m = (y.p - y.s) / (x.p - x.s)
     * b = y.s-m*x.s
     */
    int[] getSlopeAndYIntercept(PointInt other) {
        if (this.x == other.x) {
            throw new IllegalArgumentException("Vertical");
        }
        
        int m = (other.y - y ) / (other.x - x);
        int b = other.y - m * other.x;
        
        return new int[] { m, b };
    }
    
    public double distance(PointInt other) {
        
        long l = LongMath.checkedAdd( LongMath.checkedPow(x-other.x, 2), LongMath.checkedPow(y-other.y, 2) );
        double r = Math.sqrt( l );
        Preconditions.checkArgument(!Double.isNaN(r));
        
        return r;
    }
    
    public long distanceSq(PointInt other) {
        
        long l = LongMath.checkedAdd( LongMath.checkedPow(x-other.x, 2), LongMath.checkedPow(y-other.y, 2) );
        return l;
    }
    
    public PointInt translate(PointInt newOrigin) {
        return new PointInt(x - newOrigin.x(), y - newOrigin.y());
    }
    
    public Point toPoint() {
        return new Point(x, y);
    }
    
    public int getManhattanDistance()
    {
        return Math.abs(x()) + Math.abs(y());
    }
    
    public int getKingDistance(PointInt rhs) {
        return Math.max( Math.abs(x()-rhs.x()) ,
                Math.abs(y()-rhs.y()) );
    }
    
    static public int crossProduct(PointInt u, PointInt v) {
        return u.x() * v.y() - u.y() * v.x();
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
        PointInt other = (PointInt) obj;
        return x == other.x && y == other.y;
        
    }
    @Override
    public int compareTo(PointInt o) {
        return ComparisonChain.start().compare(x, o.x).compare(y, o.y).result();
    }
}
