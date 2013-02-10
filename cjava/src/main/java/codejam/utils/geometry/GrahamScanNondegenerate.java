package codejam.utils.geometry;

/*************************************************************************

 *  Compilation:  javac GrahamaScanNondegenerate.java
 *  Execution:    java GrahamNondegenerate < input.txt
 *  Dependencies: Point2D.java Stack.java
 * 
 *  Read points from standard input and compute their convex hull
 *  using Graham's algorithm.
 *
 *  Returns the extreme points of the convex hull in counterclockwise
 *  order (starting with the point with smallest y-coordinate).
 *
 *  Non-degeneracy assumption
 *  -------------------------
 *   -  at least 3 points
 *   -  no coincident points
 *   -  no 3 collinear points
 *
 *  GrahamScan.java removes these degeneracy assumptions.
 *
 *************************************************************************/

import java.util.Arrays;

import codejam.utils.datastructures.Stack;
import codejam.utils.datastructures.StdIn;
import codejam.utils.datastructures.StdOut;

public class GrahamScanNondegenerate {
    private Stack<Point2D> hull = new Stack<Point2D>();

    public GrahamScanNondegenerate(Point2D[] points) {
        // defensive copy
        int N = points.length;
        if (N <= 2) throw new RuntimeException("Requires at least 3 points");
        Point2D[] p = new Point2D[N];
        for (int i = 0; i < N; i++)
            p[i] = points[i];

        // preprocess so that p[0] has lowest y-coordinate; break ties by x-coordinate
        // p[0] is an extreme point of the convex hull
        // (could do easily in linear time)
        Arrays.sort(p, Point2D.Y_ORDER);

        // sort by polar angle with respect to base point p[0].
        // (no ties because of general position assumption)
        Arrays.sort(p, 1, N, p[0].POLAR_ORDER);

        // p[0] and p[1] are extreme points (p[1] because of general position)
        hull.push(p[0]);
        hull.push(p[1]);

        // Graham scan
        for (int i = 2; i < N; i++) {
            Point2D top = hull.pop();
            // could replace >= with > since no three collinear
            // could replace unnecessary popping/pushing with peekpeek()
            while (Point2D.ccw(hull.peek(), top, p[i]) <= 0) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(p[i]);
        }

        assert isConvex();
    }


    // return extreme points on convex hull in counterclockwise order as an Iterable
    // (no need to reverse if we want to return in clockwise order)
    public Iterable<Point2D> hull() {
        Stack<Point2D> s = new Stack<Point2D>();
        for (Point2D p : hull) s.push(p);
        return s;
    }

    // check that boundary of hull is strictly convex
    private boolean isConvex() {
        int N = hull.size();
        Point2D[] points = new Point2D[N];
        int n = 0;
        for (Point2D p : hull()) {
            points[n++] = p;
        }

        // needs to check N = 1 and N = 2 cases if not in general position

        for (int i = 0; i < N; i++) {
            if (Point2D.ccw(points[i], points[(i+1) % N], points[(i+2) % N]) <= 0) {
                return false;
            }
        }
        return true;
    }

    // test client
    public static void main(String[] args) {
        int N = StdIn.readInt();
        Point2D[] points = new Point2D[N];
        for (int i = 0; i < N; i++) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[i] = new Point2D(x, y);
        }
        GrahamScanNondegenerate graham = new GrahamScanNondegenerate(points);
        for (Point2D p : graham.hull())
            StdOut.println(p);
    }

}
