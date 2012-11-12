package com.eric.codejam.geometry;

import com.google.common.base.Objects;

public class PointInt {
    private int x;
    private int y;
    /**
     * @return the x
     */
    public int getX() {
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
    public int getY() {
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
        PointInt other = (PointInt) obj;
        return x == other.x && y == other.y;
        
    }
}
