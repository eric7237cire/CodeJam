package codejam.utils.geometry;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.utils.DoubleComparator;

import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;



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
	
	public Circle(Point center, double r) {
        super();
        this.x = center.x();
        this.y = center.y();
        this.r = r;
    }

	
	
	public Circle(Circle c) {
	    this.x = c.x;
	    this.y = c.y;
	    this.r = c.r;
	}
	
	/**
	 * 
	 * @param pWinBettingRound
	 * @return both points tangent to circle and forming a line with p
	 */
	public Point[] getPointsTangentToLine(Point P) {
	    Point C = getCenter();
	    double disPC = getCenter().distance(P);
	    
	    // Line CT and CP are perpendicular
	    double disTC = r;
	    
	    double disPT = Math.sqrt(disPC*disPC - disTC*disTC);
	    
	    
	    
	    double a = Math.asin(disTC / disPC);
	    
	    /*
	     * These angles are equal, we have all sides so we can choose any
	     */
	    //double a2 = Math.acos(disPT / disPC);	    
	    //double a3 = Math.atan(disTC / disPT);

	    //Take vector PC, make it the correct length, rotate it, then
	    //move it back to P
	    Point T = C.translate(P).scale(disPT / disPC).
	            rotate(-a).translate( P.scale(-1));
	    Point T2 = C.translate(P).scale(disPT / disPC).
                rotate(a).translate( P.scale(-1));
	    
	    
	    return new Point[] {T, T2};
	    
	}

	/**
	 * The 2 points where the circles touch
	 * @param circle2
	 * @return
	 */
    public Point[] getIntersection(Circle circle2) {
        Circle circle1 = this;

        double d = circle1.getCenter().distance(circle2.getCenter());

        double a = (circle1.r() * circle1.r() - circle2.r()
                * circle2.r() + d * d)
                / (2 * d);

        double h = Math.sqrt(circle1.r() * circle1.r() - a * a);

        double dx = circle2.x - circle1.x;
        double dy = circle2.y - circle1.y;
        Point intPoint1 = new Point(circle1.x + (a * dx - h * dy) / d,
                circle1.y + (h * dx + a * dy) / d);
        Point intPoint2 = new Point(circle1.x + (a * dx + h * dy) / d,
                circle1.y + (-h * dx + a * dy) / d);

        return new Point[] { intPoint1, intPoint2 };

    }
    
    /**
     * Given an intersection point of a circle, find the other one
     * @param circle2
     * @param intersectionPoint
     * @return
     */
    public Point getOtherIntersection(Circle circle2, Point intersectionPoint) {
        Circle circle1 = this;
        
        Line lineBetweenCenters = new Line(circle1.getCenter(), circle2.getCenter());
        
        Point midPoint = null;
        
        if (lineBetweenCenters.getType() == Line.Type.HORIZONTAL) {
            //perp line is a vertical line going through intersectionPoint
            midPoint = lineBetweenCenters.getPointGivenX(intersectionPoint.x());
        } else if (lineBetweenCenters.getType() == Line.Type.VERTICAL) {
            Preconditions.checkState(circle1.getCenter().x() == circle2.getCenter().x());
            //Perp line is a horizontal line going through intersectionPoint
            midPoint = new Point(circle1.getCenter().x(), intersectionPoint.y());
            
        } else {
        //find line perpendicular to this line containing the first intersection point
        
            Line perpLine = new Line(intersectionPoint, -1d / lineBetweenCenters.getM());

            midPoint = lineBetweenCenters.getIntersection(perpLine);
        }
        
        //This point is the midpoint between the 2 intersections.  Since the
        //midpoint = (x1 + x2) / 2, (y1+y2) / 2
        
        Point otherIntersection = new Point(2 * midPoint.x() - intersectionPoint.x(),
                2 * midPoint.y() - intersectionPoint.y());
        return otherIntersection;
    }
    
    //http://en.wikipedia.org/wiki/Circular_segment
    /**
     * Will find the area that is <= half or the area of the circle
     * @param segmentLength
     * @return
     */
    public double findSegmentArea(double segmentLength) {

        double arg = (2 * r * r - segmentLength * segmentLength)
                / (2 * r * r);
        
        //Correct if arg = -1.0000000002 for ex
        if (arg < -1) {
            Preconditions.checkState( arg >= -1.000001d);
            arg = -1;
        }
        
        double ang = Math.acos(arg);

        double area = (r * r / 2d) * (ang - Math.sin(ang));

        return area;
    }

    public double findAreaIntersection(Circle circle2) {
        Circle circle1 = this;
        
        Point[] intPoints = getIntersection(circle2);
        
        //find mid point
        Point midPoint = Point.getMidPoint(intPoints[0],intPoints[1]);
        
        //find height of arc.  Height is between the midpoint of the
        //intersections and the point on the line between the centers
        //in the other circle
        
        double c = intPoints[0].distance(intPoints[1]);
        double area1 = circle1.findSegmentArea(c);
        double area2 = circle2.findSegmentArea(c);
        
        /*
         * If the center of this circle is between the midpoint of the intersections and
         * the center of the other circle, then we want  
         */
        if (Line.isBetween(circle2.getCenter(), midPoint, circle1.getCenter())) {       
            area1 = circle1.getArea() - area1;
        }
        
        if (Line.isBetween(circle1.getCenter(), midPoint, circle2.getCenter())) {
            area2 = circle2.getArea() - area2;
        }
        
        return area1 + area2;
        
    }
    
    public double getArea() {
        return Math.PI * r * r;
    }
    

	private static Circle getCircleContaining_vertical(Circle circleA, Circle circleB) {
		Circle top = circleA.getY() >= circleB.getY() ? circleA : circleB;
		Circle bottom = circleA.getY() < circleB.getY() ? circleA : circleB;
		
		double yTop = top.getY() + top.r();
		double yBottom = bottom.getY() - bottom.r();
		
		double y = (yTop + yBottom) / 2;
		
		return new Circle(circleA.getX(), y, (yTop - yBottom) / 2);
		
		
	}
	
	
	public static Circle getCircleContaining(Circle circleA, Circle circleB) {
		if (DoubleComparator.compareStatic(circleA.getX(), circleB.getX()) == 0) {
			return getCircleContaining_vertical(circleA, circleB);
		}
	    //da and db, distance from center to edge of circle a and b
	    
	    //D distance between center of a and b
	    
	    //D = r_a + d_a + d_b + r_b
	    //2r_a + d_a = 2r_b + d_b
	    
	    Line lineAB = new Line(circleA.getCenter(), circleB.getCenter());
	    
	    double m = lineAB.getM();
	    double b = lineAB.getB();
	    
	    double r_a = circleA.r();
	    double r_b = circleB.r();
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
	    
	    double y_c = m * x_c + b;
	    double r_c = d_a+2*r_a;
	    return new Circle(x_c, y_c, r_c);
	}
	
	
	/**
     * Line from to center looks like.
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

    public Point[] getPointsIntersectingLineOld(Line l) {

        double m = l.getM();
        double b = l.getB();
        double c = b - y;

        double A = 1 + m * m;
        double B = 2 * m * c - 2 * x;
        double C = x * x + c * c - r * r;

        double x1 = (-B + Math.sqrt(B * B - 4 * A * C)) / (2 * A);
        double x2 = (-B - Math.sqrt(B * B - 4 * A * C)) / (2 * A);

        return new Point[] { l.getPointGivenX(x1), l.getPointGivenX(x2) };

    }
    
    public Point[] getPointsIntersectingLine(Line l) {
        //Move circle to origin
        double x2 = l.getP2().x() - x;
        double y2 = l.getP2().y() - y;
        double x1 =  l.getP1().x() - x;
        double y1 =  l.getP1().y() - y;
        double dx = x2-x1;
        double dy = y2-y1;
        double dr = Math.sqrt(dx*dx+dy*dy);
        double D = x1*y2-x2*y1;
        
       
        double disc =r*r*dr*dr-D*D;
        if (disc < 0)
            return null;
        
        double discSqRt = Math.sqrt(disc);
        double intX1 = x+ (D*dy+sgn(dy)*dx*discSqRt) / (dr*dr);
        double intY1 = y+ (-D*dx+Math.abs(dy) * discSqRt) / (dr*dr);
        
        double intX2 = x+ (D*dy-sgn(dy)*dx*discSqRt) / (dr*dr);
        double intY2 = y+ (-D*dx-Math.abs(dy) * discSqRt) / (dr*dr);
        
        return new Point[] { new Point(intX1, intY1), new Point(intX2, intY2) };
    }
    
    public Point getClosestPointIntersectingLine(Line l) {
        Point[] intPoints = getPointsIntersectingLine(l);
        
        if (intPoints == null)
            return null;
        
        Point p = l.getP1();
        
        double d1 = p.distance(intPoints[0]);
        double d2 = p.distance(intPoints[1]);
        
        //only care about the closer one
        Point intersection = d1 < d2 ? intPoints[0] : intPoints[1];
        
        return intersection;
    }
    
    
    private static int sgn(double x) {
        if (x < 0)
            return -1;
        return 1;
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
	    transCircle.y = this.y - p.y();
	    transCircle.x = this.x - p.x();
	    Line l = new Line(new Point(x,y), p);
	    double angle = Math.atan(l.getM());
	    
	    double x1 = transCircle.x + transCircle.r * Math.cos(angle);
	    double y1 = transCircle.y + transCircle.r * Math.sin(angle);
	    
	    double x2 = transCircle.x + transCircle.r * Math.cos(angle+Math.PI);
        double y2 = transCircle.y + transCircle.r * Math.sin(angle+Math.PI);

        return buildCloseFar(new Point(x1+p.x(), y1+p.y()), new Point(x2+p.x(),y2+p.y()), p);
        
	}
	
	private Point[] handleBaseCasesPointsIntersectingLineOriginatingAtP(Point p) {
	    if (this.getCenter().equals(p)) {
	        return new Point[] {p, p};
	    }
	    if (DoubleComparator.compareStatic(p.x(), x) == 0) {
            return buildCloseFar( new Point(x, y - r), new Point(x, y + r), p);
        }
        if (DoubleComparator.compareStatic(p.y(), y) == 0) {
            return buildCloseFar( new Point(x - r, y), new Point(x + r, y), p);
        }
        return null;
	}
	
	private final static double DOUBLE_TOLERANCE = 0.000001d;
	public boolean onCircle(Point p) {
	    double r2 = (x - p.x()) * (x - p.x()) +
	            (y - p.y()) * (y - p.y());
	    
	    double rCalc = Math.sqrt(r2);
	    
	    return DoubleMath.fuzzyEquals( rCalc - r, 0, DOUBLE_TOLERANCE);
	}
	
	public boolean contains(Circle c) {
		//Radius must be >= c
		if (r < c.r()) {
			return false;
		}
		
		double d = c.getCenter().distance(getCenter());
		
		return DoubleComparator.compareStatic(d, r - c.r) <= 0;
	}
	
	public boolean containsPoint(Point p) {
	    double d = p.distance(getCenter());
	    
	    return DoubleComparator.compareStatic(d, r) <= 0;
	}
	
	/*
	 * See apolonious in 2009 round 2 watering plants.
	 * 
	 * Algebraic solutions.  Tangent internally
	 */
	public static Circle getCircleContaining(Circle circle1, Circle circle2, Circle circle3) {
		 
		Line line12 = new Line(circle1.getCenter(), circle2.getCenter());
		Line line23 = new Line(circle2.getCenter(), circle3.getCenter());
		
		if (line12.equals(line23)) {
			//since circles do not intersect
			double diff_12 = circle1.getCenter().distance(circle2.getCenter());
			double diff_13 = circle1.getCenter().distance(circle3.getCenter());
			double diff_23 = circle2.getCenter().distance(circle3.getCenter());
			
			if (diff_12 >= diff_13 && diff_12 >= diff_23) {
				return getCircleContaining(circle1, circle2);
			}
			if (diff_13 >= diff_12 && diff_13 >= diff_23) {
				return getCircleContaining(circle1, circle3);
			}
			if (diff_23 >= diff_13 && diff_23 >= diff_12) {
				return getCircleContaining(circle2, circle3);
			}
			
			throw new IllegalStateException("h");
		}
		
		
		
		RealMatrix circ1_2 = new Array2DRowRealMatrix( getABCD(circle1, circle2) );
		RealMatrix circ1_3 = new Array2DRowRealMatrix( getABCD(circle1, circle3) );
		RealMatrix circ2_3 = new Array2DRowRealMatrix( getABCD(circle2, circle3) );
		
		/*
		RealMatrix rm = new Array2DRowRealMatrix( new double[][] { circ1_2.getColumn(0), circ1_3.getColumn(0), circ2_3.getColumn(0) });
		double[][] rmDat = new double[3][3];
		rm.copySubMatrix(0, 2, 0, 2, rmDat);
		rm = new Array2DRowRealMatrix(rmDat);
		RealMatrix d = new Array2DRowRealMatrix( new double[] { circ1_2.getEntry(3, 0), circ1_3.getEntry(3, 0),circ2_3.getEntry(3, 0) });
		double det = new LUDecomposition(rm).getDeterminant();
		*/
		
		RealMatrix ry = null;
		RealMatrix rx = null;
		
		//Special case where cant eliminate x
		if (  circ1_2.getEntry(1, 0) != 0
				&& circ1_3.getEntry(1, 0) != 0
				) {
			//eliminate y
			rx = circ1_2.scalarMultiply(1d / circ1_2.getEntry(1, 0)).add(
					circ1_3.scalarMultiply(-1d / circ1_3.getEntry(1, 0)));
		} else if (circ1_2.getEntry(1, 0) == 0) {
			rx = circ1_2;
		} else {
			rx = circ1_3;
		}
		
		//eliminate x
		if (  circ1_3.getEntry(0, 0) != 0
				&& circ2_3.getEntry(0, 0) != 0
				) {
		
			ry = circ1_3.scalarMultiply(1d / circ1_3.getEntry(0, 0)).add(
					circ2_3.scalarMultiply(-1d / circ2_3.getEntry(0, 0)));
		} else if (  circ1_3.getEntry(0, 0) == 0 ) {
			//x is already gone
			ry = circ1_3;
		} else {
			ry = circ2_3;
		}
					
		ry = ry.scalarMultiply(1d / ry.getEntry(1,  0));
		//log.debug("{} {}", ry.getRowDimension(), ry.getColumnDimension());
		
		rx = rx.scalarMultiply(1d / rx.getEntry(0,  0));
		
		Preconditions.checkState(DoubleComparator.compareStatic(ry.getEntry(0, 0),0d) == 0);
		ry.setEntry(0, 0, 0);
		Preconditions.checkState(DoubleComparator.compareStatic(rx.getEntry(1, 0),0d) == 0);
		rx.setEntry(1, 0, 0);
		//Check if xs or ys can be solved for directly instead of in terms of Rs
		/*
		if (0 == rx.getEntry(2, 0)) {
			
			double xs = rx.getEntry(3, 0);
			double ys = ry.getEntry(3, 0) - ry.getEntry(0, 0) * xs ;
			double rs = Math.sqrt((xs-circle1.getX()) * (xs-circle1.getX()) + (ys-circle1.getY()) * (ys-circle1.getY())) + circle1.getR();
			return new Circle(xs,ys,rs);
		} else if (0 == ry.getEntry(2, 0)) {
			
			double ys = ry.getEntry(3, 0) ;
			double xs = rx.getEntry(3, 0) - rx.getEntry(1, 0) * ys ;
			
			double rs = Math.sqrt((xs-circle1.getX()) * (xs-circle1.getX()) + (ys-circle1.getY()) * (ys-circle1.getY())) + circle1.getR();
			return new Circle(xs,ys,rs);
		}*/
		
		
		double M = rx.getEntry(3, 0);
		double N = -1 * rx.getEntry(2, 0);
		
		double P = ry.getEntry(3, 0);
		double Q = -1 * ry.getEntry(2, 0);
		
		
		double x1 = circle1.getX();
		double y1 = circle1.getY();
		double r1 = circle1.r();
		//double s1 = -1;
		
		//Substitute xs = M+N*rs, ys = P + Q*rs
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
				2*circle1.r() - 2 *circle2.r(),
				circle1.r()*circle1.r() 
				- circle2.r()*circle2.r()
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

  
	
    public double r() {
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



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(r);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Circle other = (Circle) obj;
		if (Double.doubleToLongBits(r) != Double.doubleToLongBits(other.r))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
	
}
