package codejam.utils.geometry;

import codejam.utils.utils.DoubleComparator;

import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;

public class Line {
    
    public static double tolerance = 0.000002;
    public final static DoubleComparator dc = new DoubleComparator(0.000002);
    private double m;
    private double b;
    
    private Point p1;
    private Point p2;

    public enum Type {
        NORMAL, VERTICAL, HORIZONTAL
    }

    private Type type;

    public Type getType() {
        return type;
    }

    public Line(double m, double b) {
        super();
        this.m = m;
        this.b = b;
        type = Type.NORMAL;
        
        //Just make up 2 points
        p1 = getPointGivenX(0);
        p2 = getPointGivenX(1);
    }

    public Line(Point a, double m) {
        this(m, a.getY() - m * a.getX());
    }
    public Line(Point a, Point b) {
        this.p1 = a;
        this.p2 = b;
        
        if (dc.compare(a.getX(), b.getX()) == 0) {
            type = Type.VERTICAL;
            this.b = a.getX();
            this.m = Double.POSITIVE_INFINITY;
            return;
        }

        if (dc.compare(a.getY(), b.getY()) == 0) {
            type = Type.HORIZONTAL;
            this.b = a.getY();
            this.m = 0;
            return;
        }
        type = Type.NORMAL;
        m = (a.getY() - b.getY()) / (a.getX() - b.getX());
        this.b = a.getY() - m * a.getX();
    }
    
    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public static boolean isBetween(Point a, Point b, Point pointToTest) {
        
        if (pointToTest == null)
            return false;
        //Assume all are on the line
     
//        crossproduct = (c.y - a.y) * (b.x - a.x) - (c.x - a.x) * (b.y - a.y)
  //  if abs(crossproduct) > epsilon : return False   # (or != 0 if using integers)

        //crossproduct = (c.y - a.y) * (b.x - a.x) - (c.x - a.x) * (b.y - a.y)
        //if abs(crossproduct) > epsilon : return False   # (or != 0 if using integers)

        double dotproduct = (pointToTest.getX() - a.getX()) * (b.getX() - a.getX()) + (pointToTest.getY() - a.getY())*(b.getY() - a.getY());
        
        if (dotproduct < 0) {
            return false;
        }

        double squaredlengthba = (b.getX() - a.getX())*(b.getX() - a.getX()) + (b.getY() - a.getY())*(b.getY() - a.getY());
        
        if (dotproduct > squaredlengthba) {
            return false;
        }

        return true;

    }
    
    public boolean isParallel(Line other) {
        Point vec1 = p2.translate(p1);
        Point vec2 = other.getP2().translate(other.getP1());
        
        double cp = Point.crossProduct(vec1,vec2);
        
        return DoubleMath.fuzzyEquals(0, cp, 0.00001);
    }
    
    
    
    /**
     * Is point on the line segment between p1 and p2.
     * 
     * @param pointToTest
     * @return
     */
    public boolean onLineSegment(Point pointToTest) {
        
        if (pointToTest == null)
            return false;
        
        if (!onLine(pointToTest)) 
            return false;
        
        return (
            DoubleMath.fuzzyCompare(Math.min(p1.getX(), p2.getX()), pointToTest.getX(), tolerance) <= 0 &&
            DoubleMath.fuzzyCompare(Math.max(p1.getX(), p2.getX()), pointToTest.getX(), tolerance) >= 0 &&
            DoubleMath.fuzzyCompare(Math.min(p1.getY(), p2.getY()), pointToTest.getY(), tolerance) <= 0 && 
            DoubleMath.fuzzyCompare(Math.max(p1.getY(), p2.getY()), pointToTest.getY(), tolerance) >= 0) ;

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
        if (type == Type.NORMAL)
            return new Point(x, m * x + b);
        else if (type == Type.HORIZONTAL) {
            return new Point(x, b);
        } else
            return null;

    }

    public Point getPointGivenY(double y) {
        if (type == Type.NORMAL)
            return new Point((y - b) / m, y);
        else if (type == Type.VERTICAL) {
            return new Point(p1.getX(),y);
        } else {
            return null;
        }
    }

    public boolean onLine(Point a) {
        if (type == Type.NORMAL) {
            Point b = getPointGivenY(a.getY());
            return a.equals(b);
        } else if (type == Type.HORIZONTAL) {
            return DoubleComparator.compareStatic(a.getY(), b) == 0;
        } else if (type == Type.VERTICAL) {
            return DoubleComparator.compareStatic(a.getX(), b) == 0;
        }

        throw new IllegalStateException("huh");

    }
    
    /**
     * 
     * Are p1 and p2 on the same side of line definde by a and b ?
     * @param p1
     * @param p2
     * @param a
     * @param b
     * @return
     */
    public static boolean sameSide(Point p1, Point p2, Point a, Point b) {
        double cp = Point.crossProduct(b.translate(a), p1.translate(a));
        double cp2 = Point.crossProduct(b.translate(a), p2.translate(a));
        if (cp * cp2 >= 0)
            return true;
        else
            return false;
    }
    
    /**
     * 
     * @param a segment start
     * @param b segment end
     * @return 
     */
    public Point intersectsSegment(Point a, Point b) {
        Point intersection = getIntersection(new Line(a,b));
        
        if ( isBetween(a, b, intersection) )
            return intersection;
        
        return null;
    }
    
    public Line getLinePerpendicular(Point p) {
        //Vector current line
        Point vec = p2.translate(p1);
        
        if (vec.getY() == 0) {
            return new Line(p, new Point(p.getX(), p.getY()+1));
        }
        
        double mPerp = -vec.getX() / vec.getY();
        return new Line(p, mPerp);
        
    }
    
    public Point getIntersection(Line line2) {
        
        
        if (this.type != Line.Type.NORMAL || line2.type != Line.Type.NORMAL) {
            Point p3 = line2.p1;
            Point p4 = line2.p2;
            double numX = (p1.getX()*p2.getY() - p1.getY()*p2.getX()) * (p3.getX()-p4.getX()) - 
                    (p1.getX() - p2.getX()) * (p3.getX()*p4.getY() - p3.getY()*p4.getX());
            
            double numY =(p1.getX()*p2.getY() - p1.getY()*p2.getX()) * (p3.getY()-p4.getY()) - 
                    (p1.getY() - p2.getY()) * (p3.getX()*p4.getY() - p3.getY()*p4.getX());
            
            double denom = (p1.getX()-p2.getX())*(p3.getY()-p4.getY()) - (p1.getY()-p2.getY())*(p3.getX()-p4.getX());
            
            if (denom == 0) {
                return null;
            }
            
            return new Point(numX / denom, numY / denom);
        }
        
        double x = (line2.b - b) / (m - line2.m);
        Point p = getPointGivenX(x);
        Point p2 = line2.getPointGivenX(x);
        
        Preconditions.checkState(p.equals(p2));
        
        return p;
    }
    
    public double distanceToPoint(Point P) {
        Point vecP = P.translate(p1);
        Point vecB = p2.translate(p1);
        
        double cross = Point.crossProduct(vecP,vecB);
        
        double d = p2.distance(p1);
        
        return Math.abs(cross / d);
    }
    
    public Point getVector() {
        return p2.translate(p1);
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(b);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(m);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Line other = (Line) obj;
		if (dc.compare(b, other.b) != 0)
			return false;
		if (dc.compare(m, other.m) != 0)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		//return "Line [m=" + m + ", b=" + b + ", type=" + type + "]";
	    return "[ " + p1.toString() + ", " + p2.toString() + " ]";
	}
    
	
}
