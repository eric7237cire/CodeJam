package com.eric.codejam;

public class Line {
    private double m;
    private double b;
    public Line(double m, double b) {
        super();
        this.m = m;
        this.b = b;
    }
    
    public Line(Point a, Point b) {
        m = (a.getY() - b.getY() ) / (a.getX() - b.getX());
        this.b = a.getY() - m * a.getX();
    }
    /**
     * @return the m
     */
    public double getM() {
        return m;
    }
    /**
     * @param m the m to set
     */
    public void setM(double m) {
        this.m = m;
    }
    /**
     * @return the b
     */
    public double getB() {
        return b;
    }
    /**
     * @param b the b to set
     */
    public void setB(double b) {
        this.b = b;
    }
    
    public Point getPointGivenX(double x) {
        return new Point(x, m * x + b);
    }
    
    public Point getPointGivenY(double y) {
        return new Point( (y-b) / m, y);
    }
    
    public boolean onLine(Point a) {
        Point b = getPointGivenY(a.getY());
        return a.equals(b);
    }
}
