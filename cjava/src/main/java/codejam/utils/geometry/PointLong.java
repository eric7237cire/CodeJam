package codejam.utils.geometry;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.math.LongMath;

public class PointLong implements Comparable<PointLong> {
    private long x;
    private long y;

    /**
     * @return the x
     */
    public long getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(long x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public long getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(long y) {
        this.y = y;
    }

    public PointLong(long x, long y) {
        this.x = x;
        this.y = y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + "]";
    }

    /*
     * y - y.s = (y.p - y.s) / (x.p - x.s) (x - x.s) m = (y.p - y.s) / (x.p -
     * x.s) b = y.s-m*x.s
     */
    long[] getSlopeAndYIntercept(PointLong other) {
        if (this.x == other.x) {
            throw new IllegalArgumentException("Vertical");
        }

        long m = (other.y - y) / (other.x - x);
        long b = other.y - m * other.x;

        return new long[] { m, b };
    }

    public double distance(PointLong other) {

        long l = LongMath.checkedAdd(LongMath.checkedPow(x - other.x, 2),
                LongMath.checkedPow(y - other.y, 2));
        double r = Math.sqrt(l);
        Preconditions.checkArgument(!Double.isNaN(r));

        return r;
    }
    
    public static PointLong midPoint(PointLong a, PointLong b) {
        return new PointLong( (a.x+b.x)/2, (a.y+b.y)/2 );
    }

    public long distanceSq(PointLong other) {

        long l = LongMath.checkedAdd(LongMath.checkedPow(x - other.x, 2),
                LongMath.checkedPow(y - other.y, 2));
        return l;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(x, y);
    }

    /*
     * (non-Javadoc)
     * 
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
        PointLong other = (PointLong) obj;
        return x == other.x && y == other.y;

    }

    @Override
    public int compareTo(PointLong o) {
        return ComparisonChain.start().compare(x, o.x).compare(y, o.y).result();
    }
}
