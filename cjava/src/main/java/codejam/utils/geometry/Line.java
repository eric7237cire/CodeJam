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
        this(m, a.y() - m * a.x());
    }
    public Line(Point a, Point b) {
        this.p1 = a;
        this.p2 = b;
        
        if (dc.compare(a.x(), b.x()) == 0) {
            type = Type.VERTICAL;
            this.b = a.x();
            this.m = Double.POSITIVE_INFINITY;
            return;
        }

        if (dc.compare(a.y(), b.y()) == 0) {
            type = Type.HORIZONTAL;
            this.b = a.y();
            this.m = 0;
            return;
        }
        type = Type.NORMAL;
        m = (a.y() - b.y()) / (a.x() - b.x());
        this.b = a.y() - m * a.x();
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

        double dotproduct = (pointToTest.x() - a.x()) * (b.x() - a.x()) + (pointToTest.y() - a.y())*(b.y() - a.y());
        
        if (dotproduct < 0) {
            return false;
        }

        double squaredlengthba = (b.x() - a.x())*(b.x() - a.x()) + (b.y() - a.y())*(b.y() - a.y());
        
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
            DoubleMath.fuzzyCompare(Math.min(p1.x(), p2.x()), pointToTest.x(), tolerance) <= 0 &&
            DoubleMath.fuzzyCompare(Math.max(p1.x(), p2.x()), pointToTest.x(), tolerance) >= 0 &&
            DoubleMath.fuzzyCompare(Math.min(p1.y(), p2.y()), pointToTest.y(), tolerance) <= 0 && 
            DoubleMath.fuzzyCompare(Math.max(p1.y(), p2.y()), pointToTest.y(), tolerance) >= 0) ;

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
            return new Point(p1.x(),y);
        } else {
            return null;
        }
    }

    public boolean onLine(Point a) {
        if (type == Type.NORMAL) {
            Point b = getPointGivenY(a.y());
            return a.equals(b);
        } else if (type == Type.HORIZONTAL) {
            return DoubleComparator.compareStatic(a.y(), b) == 0;
        } else if (type == Type.VERTICAL) {
            return DoubleComparator.compareStatic(a.x(), b) == 0;
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
        
        if (vec.y() == 0) {
            return new Line(p, new Point(p.x(), p.y()+1));
        }
        
        double mPerp = -vec.x() / vec.y();
        return new Line(p, mPerp);
        
    }
    
    public Point getIntersection(Line line2) {
        
        
        if (this.type != Line.Type.NORMAL || line2.type != Line.Type.NORMAL) {
            Point p3 = line2.p1;
            Point p4 = line2.p2;
            double numX = (p1.x()*p2.y() - p1.y()*p2.x()) * (p3.x()-p4.x()) - 
                    (p1.x() - p2.x()) * (p3.x()*p4.y() - p3.y()*p4.x());
            
            double numY =(p1.x()*p2.y() - p1.y()*p2.x()) * (p3.y()-p4.y()) - 
                    (p1.y() - p2.y()) * (p3.x()*p4.y() - p3.y()*p4.x());
            
            double denom = (p1.x()-p2.x())*(p3.y()-p4.y()) - (p1.y()-p2.y())*(p3.x()-p4.x());
            
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
    
    /*
     * Segment to segment intersection
     * 
     * 
bool getIntersection( const SegmentD& seg1, const SegmentD& seg2, PointD& inter)
{
    PointD p = seg1.first;
    PointD r = seg1.second - seg1.first;
    
    PointD q = seg2.first;
    PointD s = seg2.second - seg2.first;
    
    //P + t*r intersects q + u*s
    
    double rCrossS = cross(r, s);
    
    if ( abs( rCrossS ) < tolerance )
        return false;
    
    double t = cross(q-p, s / rCrossS);
    
    double u = cross(q-p, r / rCrossS);
    
    inter = p + t*r;
    return true;
}

http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect

t between 0 and 1
     */
    
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
