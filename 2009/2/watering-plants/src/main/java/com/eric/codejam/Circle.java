package com.eric.codejam;

public class Circle {
	double x;
	double y;
	int r;
	public Circle(int x, int y, int r) {
		super();
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	public Circle(Circle c) {
	    this.x = c.x;
	    this.y = c.y;
	    this.r = c.r;
	}
	/**
     *
     * 
     * (x-x.p)2 + (y-y.p)2 = r.p^2
     * 
     * x2 -2x*x.p +x.p^2 + (mx + b - y.p)2 = r.p2
     * c = b - y.p
     * 
     * x2 - 2x*x.p + x.p^2 + m2x2 + 2mcx + y.p^2 - r.p^2 = 0
     * (1+m2) x2 + (2mc - 2x.p)x + (x.p^2 + y.p^2 - r.p^2) = 0;
     * 
     * -B +/- sqrt(B^2 - 4 A C) /  2 A
     */
	public Point[] getPointsIntersectingLineOriginatingAtP(Point p) {
	    
	    Point s = new Point(x, y);
	    Line l = new Line(s, p);
	    
	    double m = l.getM();
	    double b = l.getB();
	    double c = b - y;
	    
	    double A = 1 + m*m;
	    double B = 2 * m * c - 2 * x;
	    double C = x *x + y * y - r * r;
	    
	    double y1 = -B + Math.sqrt(B*B - 4 * A * C) / (2 * A);
	    double y2 = -B - Math.sqrt(B*B - 4 * A * C) / (2 * A);
	    
	    return new Point[] { l.getPointGivenY(y1), l.getPointGivenY(y2) };
	    
	}
	
	/*
     * x = cx + r * cos(a)
y = cy + r * sin(a)

angle = tan-1 (m)
     */
	public Point[] getPointsIntersectingLineOriginatingAtP_second(Point p) {
	    Circle transCircle = new Circle(this);
	    //move point to origin 
	    transCircle.y = this.y - p.getY();
	    transCircle.x = this.x - p.getX();
	    Line l = new Line(new Point(x,y), p);
	    double angle = Math.atan(l.getM());
	    
	    double x1 = transCircle.x + transCircle.r * Math.cos(angle);
	    double y1 = transCircle.y + transCircle.r * Math.sin(angle);
	    
	    double x2 = transCircle.x + transCircle.r * Math.cos(angle+Math.PI);
        double y2 = transCircle.y + transCircle.r * Math.sin(angle+Math.PI);

        return new Point[] {new Point(x1, y1), new Point(x2,y2) };
        
	}
	
	private final static double DOUBLE_TOLERANCE = 0.000001d;
	public boolean onCircle(Point p) {
	    double r2 = (x - p.getX()) * (x - p.getX()) +
	            (y - p.getY()) * (y - p.getY());
	    
	    double rCalc = Math.sqrt(r2);
	    
	    return Math.abs(rCalc - r) < DOUBLE_TOLERANCE;
	}

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

    /**
     * @return the r
     */
    public int getR() {
        return r;
    }

    /**
     * @param r the r to set
     */
    public void setR(int r) {
        this.r = r;
    }
	
    public Point getCenter() {
        return new Point(x, y);
    }
	
}
