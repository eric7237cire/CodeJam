package codejam.y2010.round_2.goats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;

import codejam.utils.geometry.Circle;
import codejam.utils.geometry.Line;
import codejam.utils.geometry.Point;
import codejam.utils.geometry.draw.StdDraw;

public class TestInversion
{
    final protected static Logger log = LoggerFactory.getLogger("main");
    
    /**
     * Shows the intersection of the inverted circles coincides with
     * the intersection of the circles.
     * c
     * @param args
     */
    public static void main( String[] args) {
        //Centers 0 20
        //        20 0
        //-20 10
        //40 20
        
        StdDraw.setCanvasSize(800, 800);
        int dim = 70;
        int minX = -dim;
        int maxX = dim;
        int minY = -dim;
        int maxY = dim;
        
        /*
         minX = -23;
         maxX = -17;
         minY = 7;
         maxY = 13;
        */
        
        StdDraw.setXscale(minX, maxX);
        StdDraw.setYscale(minY, maxY);
        StdDraw.line(minX, 0, maxX, 0);
        StdDraw.line(0, minY, 0, maxY);
        
        
        Point q = new Point(-20, 10);
        Point c1Center = new Point(0, 20);
        Point c2Center = new Point(20, 0);
        Circle c1 = new Circle(c1Center, c1Center.distance(q) );
        Circle c2 = new Circle(c2Center, c2Center.distance(q) );
        
        StdDraw.point(q.x(), q.y());
        StdDraw.text(q.x(), q.y(), "Q");
        
        StdDraw.circle(c1.getX(), c1.getY(), c1.r());
        StdDraw.circle(c2.getX(), c2.getY(), c2.r());
        
        StdDraw.show();
        
        Line l1 = getInversion(c1, q);
        Line l2 = getInversion(c2, q);
        
        Point invIntersection = l1.getIntersection(l2);
        
        Point invIntersectionInverted = invertPoint(invIntersection, q, 10);
        
        log.debug("inversion intersection {}, {}", invIntersection, invIntersectionInverted);
        
        StdDraw.text(invIntersectionInverted.x(), invIntersectionInverted.y(), "???");
    }
    
    static Point invertPoint(Point p, Point invCenter, double invRadius) {
        double pDist = p.distance(invCenter);
        double pInvDist = invRadius * invRadius / pDist;
        
        log.debug("dist {}", pDist);
        Point p1_inv = p.translate(invCenter).scale(pInvDist / pDist).translate(invCenter.scale(-1));
        
        double check1 = p.distance(invCenter);
        double check2 = p1_inv.distance(invCenter);
        
        Preconditions.checkState(DoubleMath.fuzzyEquals(invRadius*invRadius, check1 * check2, 0.0001));
        return p1_inv;
    }

    
    static Line getInversion(Circle c, Point q) {
        Preconditions.checkState(c.onCircle(q));
        
        Line pCent = new Line(q, c.getCenter());
        
        Point dir1 = c.getCenter().rotateAbout(q,-Math.PI / 7.1);
        Point dir2 = c.getCenter().rotateAbout(q, Math.PI / 4.1);
        
        Line l1 = new Line(q, dir1);
        Line l2 = new Line(q, dir2);
        
        Point[] pts = c.getPointsIntersectingLine(l1);
        Point[] pts2 = c.getPointsIntersectingLine(l2);
        
        Point p1 = pts[0].equals(q) ? pts[1] : pts[0];
        Point p2 = pts2[0].equals(q) ? pts2[1] : pts2[0];
        
        log.debug("Circle {}\n l1 {} l2 {}\n pts {} pts2 {}",c,l1,l2,pts,pts2);
        
       // StdDraw.text(p1.x(), p1.y(), "pts");
       // StdDraw.text(p2.x(), p2.y(), "pts2");
        
        double d1 = p1.distance(q);
        double d2 = p2.distance(q);
        
        Point p1_inv = invertPoint(p1, q, 10);
        Point p2_inv = invertPoint(p2, q, 10);
        
        
       
       // StdDraw.text(p1_inv.x(), p1_inv.y(), "inv pts");
      //  StdDraw.text(p2_inv.x(), p2_inv.y(), "inv_pts2");
        
        Line inversion = new Line(p1_inv, p2_inv);
        
        log.debug("Inversion {}, slope {}", inversion, inversion.getM());
        StdDraw.line(-50d, inversion.getPointGivenX(-50).y(),
                50d, inversion.getPointGivenX(50).y());
        
        return inversion;
    }

}
