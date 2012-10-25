package com.eric.codejam;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Circle {
	
	final static Logger log = LoggerFactory.getLogger(Circle.class);
	
	double x;
	double y;
	double r;
	public Circle(double x, double y, double r) {
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
	
	public static Circle getCircleContaining(Circle circleA, Circle circleB) {
	    //da and db, distance from center to edge of circle a and b
	    
	    //D distance between center of a and b
	    
	    //D = r_a + d_a + d_b + r_b
	    //2r_a + d_a = 2r_b + d_b
	    
	    Line lineAB = new Line(circleA.getCenter(), circleB.getCenter());
	    
	    double m = lineAB.getM();
	    double b = lineAB.getB();
	    
	    double r_a = circleA.getR();
	    double r_b = circleB.getR();
	    double x_a = circleA.getX();
	    double y_a = circleA.getY();
	    
	    double D = circleA.getCenter().distance(circleB.getCenter());
	    double d_b = (D + r_a - 3 * r_b) / 2;
	    double d_a = 2 * r_b + d_b - 2 * r_a;
	    
	    double A = 1 + m*m;
	    double B = -2*x_a - 2*m*(y_a-b);
	    double C = x_a * x_a + (y_a-b)*(y_a-b) - d_a * d_a - 2 * d_a * r_a - r_a*r_a;
	    
	    double x1_c = (-B + Math.sqrt(B*B - 4 *A * C)) / (2*A);
	    double x2_c = (-B - Math.sqrt(B*B - 4 *A * C)) / (2*A);
	    
	    double x_c = x1_c >= Math.min(circleA.getX(), circleB.getX()) && x1_c <= Math.max(circleA.getX(), circleB.getX()) ?
	    		x1_c : x2_c;
	    
	    double y_c = m * x1_c + b;
	    double r_c = d_a+2*r_a;
	    return new Circle(x_c, y_c, r_c);
	}
	
	
	/**
     *
     * 
     * (x-x.p)2 + (y-y.p)2 = r.p^2
     * 
     * x2 -2x*x.p +x.p^2 + (mx + b - y.p)2 = r.p2
     * c = b - y.p
     * 
     * x2 - 2x*x.p + x.p^2 + m2x2 + 2mcx + c^2 - r.p^2 = 0
     * (1+m2) x2 + (2mc - 2x.p)x + (x.p^2 + c^2 - r.p^2) = 0;
     * 
     * -B +/- sqrt(B^2 - 4 A C) /  2 A
     */
	public Point[] getPointsIntersectingLineOriginatingAtP(Point p) {
	    
Point[] ret = handleBaseCasesPointsIntersectingLineOriginatingAtP(p);
        
        if (ret != null) {
            return ret;
        }
        
	    Point s = new Point(x, y);
	    Line l = new Line(s, p);
	    
	    double m = l.getM();
	    double b = l.getB();
	    double c = b - y;
	    
	    double A = 1 + m*m;
	    double B = 2 * m * c - 2 * x;
	    double C = x *x + c * c - r * r;
	    
	    double x1 = (-B + Math.sqrt(B*B - 4 * A * C)) / (2 * A);
	    double x2 = (-B - Math.sqrt(B*B - 4 * A * C)) / (2 * A);
	    
	    return buildCloseFar(l.getPointGivenX(x1), l.getPointGivenX(x2), p);
	    
	}
	
	private Point[] buildCloseFar(Point a, Point b, Point ref) {
	    double distA = a.distance(ref);
	    double distB = b.distance(ref);
	    
	    if (distA <= distB) {
	        return new Point[] { a, b };
	    }
	    
	    return new Point[] { b, a };
	    
	}
	
	/*
     * x = cx + r * cos(a)
y = cy + r * sin(a)

angle = tan-1 (m)
     */
	public Point[] getPointsIntersectingLineOriginatingAtP_second(Point p) {
	    Point[] ret = handleBaseCasesPointsIntersectingLineOriginatingAtP(p);
	    
	    if (ret != null) {
	        return ret;
	    }
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

        return buildCloseFar(new Point(x1+p.getX(), y1+p.getY()), new Point(x2+p.getX(),y2+p.getY()), p);
        
	}
	
	private Point[] handleBaseCasesPointsIntersectingLineOriginatingAtP(Point p) {
	    if (this.getCenter().equals(p)) {
	        return new Point[] {p, p};
	    }
	    if (DoubleComparator.compareStatic(p.getX(), x) == 0) {
            return buildCloseFar( new Point(x, y - r), new Point(x, y + r), p);
        }
        if (DoubleComparator.compareStatic(p.getY(), y) == 0) {
            return buildCloseFar( new Point(x - r, y), new Point(x + r, y), p);
        }
        return null;
	}
	
	private final static double DOUBLE_TOLERANCE = 0.000001d;
	public boolean onCircle(Point p) {
	    double r2 = (x - p.getX()) * (x - p.getX()) +
	            (y - p.getY()) * (y - p.getY());
	    
	    double rCalc = Math.sqrt(r2);
	    
	    return Math.abs(rCalc - r) < DOUBLE_TOLERANCE;
	}
	
	public static Circle getCircleContaining(Circle circle1, Circle circle2, Circle circle3) {
		 
		
		
		
		RealMatrix circ1_2 = new Array2DRowRealMatrix( getABCD(circle1, circle2) );
		RealMatrix circ1_3 = new Array2DRowRealMatrix( getABCD(circle1, circle3) );
		RealMatrix circ2_3 = new Array2DRowRealMatrix( getABCD(circle2, circle3) );
		
		
		RealMatrix ry = circ1_2.scalarMultiply( 1d / circ1_2.getEntry(0, 0))
				.add(circ1_3.scalarMultiply( -1d / circ1_3.getEntry(0, 0)));
		
		ry = ry.scalarMultiply(1d / ry.getEntry(1,  0));
		log.debug("{} {}", ry.getRowDimension(), ry.getColumnDimension());
		
		
		RealMatrix rx = circ1_3.scalarMultiply( 1d / circ1_3.getEntry(1, 0))
				.add(circ2_3.scalarMultiply( -1d / circ2_3.getEntry(1, 0)));
		
		rx = rx.scalarMultiply(1d / rx.getEntry(0,  0));
		
		double M = rx.getEntry(3, 0);
		double N = -1 * rx.getEntry(2, 0);
		
		double P = ry.getEntry(3, 0);
		double Q = -1 * ry.getEntry(2, 0);
		
		double x1 = circle1.getX();
		double y1 = circle1.getY();
		double r1 = circle1.getR();
		//double s1 = -1;
		double a = N*N + Q*Q - 1;
		double b = 2*M*N - 2*N*x1 + 2*P*Q - 2*Q*y1 + 2*r1;
		double c = x1*x1 + M*M - 2*M*x1 + P*P + y1*y1 - 2*P*y1 - r1*r1;
		
		double rs = (-b + Math.sqrt(b*b - 4 *a*c)) / (2*a);
		double rs2 = (-b - Math.sqrt(b*b - 4 *a*c)) / (2*a);
		
		//take positive one
		rs = Math.max(rs2,  rs);
		double xs = M+N*rs;
		double ys = P+Q*rs;
		return new Circle(xs,ys,rs);
		//return null;
		
	}
	
	/**
	 * 
	 * @param circle1
	 * @param circle2
	 * @return
	 */
	private static double[] getABCD(Circle circle1, Circle circle2) {
		//Ax + By + Cr = d
		return new double[] {
				2*circle2.getX() - 2 *circle1.getX(),
				2*circle2.getY() - 2 *circle1.getY(),
				2*circle1.getR() - 2 *circle2.getR(),
				circle1.getR()*circle1.getR() 
				- circle2.getR()*circle2.getR()
				- circle1.getX()*circle1.getX()
				+ circle2.getX()*circle2.getX()
				- circle1.getY()*circle1.getY()
				+ circle2.getY()*circle2.getY()
		};
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

  
	
    public double getR() {
		return r;
	}



	public void setR(double r) {
		this.r = r;
	}



	public Point getCenter() {
        return new Point(x, y);
    }



	@Override
	public String toString() {
		return "Circle [x=" + x + ", y=" + y + ", r=" + r + "]";
	}
	
}
