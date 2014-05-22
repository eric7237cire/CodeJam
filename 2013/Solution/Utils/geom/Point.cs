//#define LOGGING_TRACE

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.math;

using Logger = Utils.LoggerFile;

namespace Utils.geom
{
    public static class PointExt
    {
        public static Point<double> Add(this Point<double> lhs, Point<double> rhs)
        {
            return new Point<double>(lhs.X + rhs.X, lhs.Y + rhs.Y);
        }

        public static Point<T> Add<T>(this Point<T> lhs, Point<T> rhs) where T : INumeric<T>, IComparable<T>
        {
            return new Point<T>(lhs.X.Add(rhs.X), lhs.Y.Add(rhs.Y));
        }

        public static int dist2(this Point<int> lhs, Point<int> rhs)
        {
            return (lhs.X - rhs.X) * (lhs.X - rhs.X) + (lhs.Y - rhs.Y) * (lhs.Y - rhs.Y);
        }

        //IN counter clockwise order
        private static int polarOrder(Point<double> pt, Point<double> q1, Point<double> q2)
        {
            double dx1 = q1.X - pt.X;
            double dy1 = q1.Y - pt.Y;
            double dx2 = q2.X - pt.X;
            double dy2 = q2.Y - pt.Y;

            if (dy1 >= 0 && dy2 < 0) return -1;    // q1 above; q2 below
            else if (dy2 >= 0 && dy1 < 0) return +1;    // q1 below; q2 above
            else if (dy1 == 0 && dy2 == 0)
            {            // 3-collinear and horizontal
                if (dx1 >= 0 && dx2 < 0) return -1;
                else if (dx2 >= 0 && dx1 < 0) return +1;
                else return 0;
            }
            else return -ccw(pt, q1, q2);     // both above or below

        }

        public static int polarOrder(Point<int> pt, Point<int> q1, Point<int> q2)
        {
            int dx1 = q1.X - pt.X;
            int dy1 = q1.Y - pt.Y;
            int dx2 = q2.X - pt.X;
            int dy2 = q2.Y - pt.Y;

            if (dy1 >= 0 && dy2 < 0) return -1;    // q1 above; q2 below
            else if (dy2 >= 0 && dy1 < 0) return +1;    // q1 below; q2 above
            else if (dy1 == 0 && dy2 == 0)
            {            // 3-collinear and horizontal
                if (dx1 >= 0 && dx2 < 0) return -1;
                else if (dx2 >= 0 && dx1 < 0) return +1;
                else
                {
                    return pt.dist2(q1).CompareTo(pt.dist2(q2));
                }
            }
            else
            {
                int ret = -ccw(pt, q1, q2);     // both above or below
                if (ret != 0)
                    return ret;

                //break ties with distance
                return pt.dist2(q1).CompareTo(pt.dist2(q2));
            }

        }

        // is a->b a->c a counter-clockwise turn?
        // -1 if clockwise, +1 if counter-clockwise, 0 if collinear
        public static int ccw(Point<double> a, Point<double> b, Point<double> c)
        {
            //The cross product, gives direction of vector.  Positive means
            //counter clockwise see  wikipedia page
            double area2 = (b.X - a.X) * (c.Y - a.Y) - (b.Y - a.Y) * (c.X - a.X);
            if (area2 < 0) return -1;
            else if (area2 > 0) return +1;
            else return 0;
        }

        public static int ccw(Point<int> a, Point<int> b, Point<int> c)
        {
            //The cross product, gives direction of vector.  Positive means
            //counter clockwise see  wikipedia page
            long area2 = (long)(b.X - a.X) * (c.Y - a.Y) - (long)(b.Y - a.Y) * (c.X - a.X);
            if (area2 < 0) return -1;
            else if (area2 > 0) return +1;
            else return 0;
        }

        public static List<Point<double>> ConvexHull(this IList<Point<double>> pts)
        {
            int mpIdx;
            return ConvexHullChains(pts, ccw, out mpIdx);
        }
        public static List<Point<int>> ConvexHull(this IList<Point<int>> pts, bool includeColinear = true)
        {
           // return ConvexHull(pts, polarOrder, ccw, includeColinear);
            int mpIdx;
            return ConvexHullChains(pts, ccw, out mpIdx, includeColinear);
        }

#if mono
        public class Comparer<T> : IComparer<T>
		{
			private Comparison<T> cmpFunc;
			
			public Comparer(Comparison<T> compare)				
			{
				cmpFunc = compare;
			}
	
			public int Compare(T x, T y)
			{
				return cmpFunc(x, y);
			}
	
			public static implicit operator Comparer<T>(Comparison<T> comparison)
			{
				return comparison == null ? null : new Comparer<T>(comparison);
			}
	/*
			public static implicit operator Comparer<T>(Func<T, T, int> comparison)
			{
				return comparison == null
						   ? null
						   : new Comparer<T>(comparison);
			}*/
	
			public static implicit operator Func<T, T, int>(Comparer<T> comparer)
			{
				return ReferenceEquals(comparer, null) ? default(Func<T, T, int>) : comparer.Compare;
			}
		}
#endif

        //GrahamScan, returns them in clockwise order, or counter-clockwise if poped off 1 by 1 off the stack
        //Includes co-linear points in hull
        public static List<Point<T>> ConvexHull<T>(this IList<Point<T>> pts,
            Func<Point<T>, Point<T>, Point<T>, int> polarOrderComp,
            Func<Point<T>, Point<T>, Point<T>, int> ccwFunc,
            bool includeColinear = true
            ) where T : IComparable<T>, IEquatable<T>
        {
            // defensive copy
            int N = pts.Count;
            Point<T>[] points = new Point<T>[N];
            for (int i = 0; i < N; i++)
                points[i] = pts[i];

            // preprocess so that points[0] has lowest y-coordinate; break ties by x-coordinate
            // points[0] is an extreme point of the convex hull
            // (alternatively, could do easily in linear time)
            Array.Sort(points, (lhs, rhs) =>
            {
                if (!lhs.Y.Equals(rhs.Y)) return lhs.Y.CompareTo(rhs.Y);

                return lhs.X.CompareTo(rhs.X);
            });

            // sort by polar angle with respect to base point points[0],
            // breaking ties by distance to points[0].
            //Counter clockwise
#if mono
            	Array.Sort(points, 1, N - 1, new Comparer<Point<T>>((lhs, rhs) =>
                { return polarOrderComp(points[0], lhs, rhs); }
                ));
#else
            Array.Sort(points, 1, N - 1, Comparer<Point<T>>.Create((lhs, rhs) =>
                { return polarOrderComp(points[0], lhs, rhs); }
                ));
#endif

            Logger.LogTrace("\nCreating hull from points {}", points.ToCommaString());

            Stack<Point<T>> hull = new Stack<Point<T>>();
            hull.Push(points[0]);       // p[0] is first extreme point

            // find index k1 of first point not equal to points[0]
            int k1;
            for (k1 = 1; k1 < N; k1++)
                if (!points[0].Equals(points[k1])) break;
            if (k1 == N) return null;        // all points equal

            // find index k2 of first point not collinear with points[0] and points[k1]
            int k2;
            if (includeColinear)
            {
                k2 = k1 + 1;
            }
            else
            {
                for (k2 = k1 + 1; k2 < N; k2++)
                    if (ccwFunc(points[0], points[k1], points[k2]) != 0) break;
            }
            hull.Push(points[k2 - 1]);    // points[k2-1] is second extreme point

            Logger.LogTrace("Starting scan with hull (top->bottom stack) {}", hull.ToCommaString());

            // Graham scan; note that points[N-1] is extreme point different from points[0]
            for (int i = k2; i < N; i++)
            {
                Point<T> top = hull.Pop();
                Logger.LogTrace("Top {} Cur point {}", top, points[i]);

                if (includeColinear)
                {
                    while (ccwFunc(hull.Peek(), top, points[i]) < 0)
                    {
                        top = hull.Pop();
                    }
                }
                else
                {
                    while (ccwFunc(hull.Peek(), top, points[i]) <= 0)
                    {
                        Logger.LogTrace("Vector {} {} to {} {} is not a counter clockwise turn, popping {}",
                            hull.Peek(), top, hull.Peek(), points[i], hull.Peek());
                        top = hull.Pop();
                    }
                }
                hull.Push(top);
                hull.Push(points[i]);

                Logger.LogTrace("Hull is now (top->bottom) {}", hull.ToCommaString());
            }

            List<Point<T>> ret = new List<Point<T>>(hull);
            ret.Reverse();
            return ret;
        }

        /*
        Input: a list P of points in the plane.

    Points in the result will be listed in counter-clockwise order starting with
         * extreme left point, then lower hull increasing X, then upper hull going decreasing X.
         * midpoint and 0 are shared between upper and lower hull
         */
        public static List<Point<T>> ConvexHullChains<T>(this IList<Point<T>> ptsOrig,
                Func<Point<T>, Point<T>, Point<T>, int> ccwFunc, //+1 ccw, -1 cw, 0 colinear
            out int midPointIdx,
                bool includeColinear = true
                ) where T : IComparable<T>, IEquatable<T>
        {
            List<Point<T>> points = new List<Point<T>>(ptsOrig);

            //Sort the points of P by x-coordinate (in case of a tie, sort by y-coordinate).
            points.Sort((lhs, rhs) =>
            {
                int xCmp = lhs.X.CompareTo(rhs.X);
                if (xCmp != 0)
                    return xCmp;
                return lhs.Y.CompareTo(rhs.Y);
            });

            
            List<Point<T>> hull = new List<Point<T>>();

            /*
            for i = 1, 2, ..., n:
    while L contains at least two points and the sequence of last two points
            of L and the point P[i] does not make a counter-clockwise turn:
        remove the last point from L
    append P[i] to L
            */
            // Build lower hull

            for (int i = 0; i < points.Count; ++i)
            {
                if (includeColinear)
                {
                    //Discard only clockwise
                    while (hull.Count >= 2 && ccwFunc(hull[hull.Count - 2], hull[hull.Count - 1], points[i]) < 0)
                        hull.RemoveAt(hull.Count - 1);
                }
                else
                {
                    while (hull.Count >= 2 && ccwFunc(hull[hull.Count - 2], hull[hull.Count - 1], points[i]) <= 0)
                        hull.RemoveAt(hull.Count - 1);
                }
                
                hull.Add(points[i]);
            }

            //TODO see python code below, can combine loops by changing sign of upper hull <= to >=

            midPointIdx = hull.Count - 1;

            //Remove last element to avoid repetition
            hull.RemoveAt(hull.Count - 1);
            
            int upperHullMinSize = hull.Count + 2;
            // Build upper hull
            for (int i = points.Count - 1; i >= 0; i--)
            {
                if (includeColinear)
                {
                    while (hull.Count >= upperHullMinSize && ccwFunc(hull[hull.Count - 2], hull[hull.Count - 1], points[i]) < 0)
                        hull.RemoveAt(hull.Count - 1);
                } else
                {
                    while (hull.Count >= upperHullMinSize && ccwFunc(hull[hull.Count - 2], hull[hull.Count - 1], points[i]) <= 0)
                        hull.RemoveAt(hull.Count - 1);
                }
                
                hull.Add(points[i]);
            }

            hull.RemoveAt(hull.Count - 1);

            return hull;
        }

        /*
         * # convex hull (Graham scan by x-coordinate) and diameter of a set of points
# David Eppstein, UC Irvine, 7 Mar 2002

from __future__ import generators

def orientation(p,q,r):
    '''Return positive if p-q-r are clockwise, neg if ccw, zero if colinear.'''
    return (q[1]-p[1])*(r[0]-p[0]) - (q[0]-p[0])*(r[1]-p[1])

def hulls(Points):
    '''Graham scan to find upper and lower convex hulls of a set of 2d points.'''
    U = []
    L = []
    Points.sort()
    for p in Points:
        while len(U) > 1 and orientation(U[-2],U[-1],p) <= 0: U.pop()
        while len(L) > 1 and orientation(L[-2],L[-1],p) >= 0: L.pop()
        U.append(p)
        L.append(p)
    return U,L

def rotatingCalipers(Points):
    '''Given a list of 2d points, finds all ways of sandwiching the points
between two parallel lines that touch one point each, and yields the sequence
of pairs of points touched by each pair of lines.'''
    U,L = hulls(Points)
    i = 0
    j = len(L) - 1
    while i < len(U) - 1 or j > 0:
        yield U[i],L[j]
        
        # if all the way through one side of hull, advance the other side
        if i == len(U) - 1: j -= 1
        elif j == 0: i += 1
        
        # still points left on both lists, compare slopes of next hull edges
        # being careful to avoid divide-by-zero in slope calculation
        elif (U[i+1][1]-U[i][1])*(L[j][0]-L[j-1][0]) > \
                (L[j][1]-L[j-1][1])*(U[i+1][0]-U[i][0]):
            i += 1
        else: j -= 1

def diameter(Points):
    '''Given a list of 2d points, returns the pair that's farthest apart.'''
    diam,pair = max([((p[0]-q[0])**2 + (p[1]-q[1])**2, (p,q))
                     for p,q in rotatingCalipers(Points)])
    return pair
         * */

        public static double PolygonArea(this IList<Point<int>> points)
        {
            double sum = 0;
            for (int i = 0; i < points.Count - 1; ++i)
            {

                sum += (long)points[i].X * points[i + 1].Y;
            }

            sum += (long)points.GetLastValue().X * points[0].Y;

            for (int i = 0; i < points.Count - 1; ++i)
            {
                sum -= (long)points[i + 1].X * points[i].Y;
            }

            sum -= (long)points[0].X * points.GetLastValue().Y;

            return Math.Abs(sum) / 2;
        }

        public static double PolygonArea(this IList<Point<double>> points)
        {
            double sum = 0;
            for (int i = 0; i < points.Count - 1; ++i)
            {

                sum += points[i].X * points[i + 1].Y;
            }

            sum += points.GetLastValue().X * points[0].Y;

            for (int i = 0; i < points.Count - 1; ++i)
            {
                sum -= points[i + 1].X * points[i].Y;
            }

            sum -= points[0].X * points.GetLastValue().Y;

            return Math.Abs(sum) / 2;
        }
        /*
        public static T PolygonArea<T>(this IList<Point<T>> points) where T : INumeric<T>, new()
        {
            T sum = new T();
            for (int i = 0; i < points.Count - 1; ++i)
            {

                sum = sum.Add(points[i].X.Multiply( points[i + 1].Y) );
            }

            sum = sum.Add(points.GetLastValue().X.Multiply ( points[0].Y ) );

            for (int i = 0; i < points.Count - 1; ++i)
            {

                sum = sum.Subtract (points[i + 1].X.Multiply ( points[i].Y ) );
            }

            sum = sum.Subtract (points[0].X.Multiply( points.GetLastValue().Y ));

            return sum.Abs().Divide(2);
        }*/
    }



    public class Point<T> : IEquatable<Point<T>>, IComparable<Point<T>> where T : IComparable<T>
    {
        //private readonly int sideLength;
        private T x;
        private T y;

        public T X
        {
            get
            {
                return x;
            }
            set
            {
                x = value;
            }
        }

        public T Y
        {
            get
            {
                return y;
            }
            set
            {
                y = value;
            }
        }

        public Point(T x, T y)
        {
            this.x = x;
            this.y = y;
        }

        private const int prime1 = 17;
        private const int prime2 = 23;

        public override bool Equals(object obj)
        {
            return this.Equals(obj as Point<T>);

        }
        public override int GetHashCode()
        {
            unchecked
            {
                int hash = 17;
                hash = hash * 23 + x.GetHashCode();
                hash = hash * 23 + y.GetHashCode();
                return hash;
            }
        }


        public bool Equals(Point<T> other)
        {
            if (ReferenceEquals(null, other)) return false;
            return (this.x.Equals(other.x))
                && (this.y.Equals(other.y));
        }

        public static bool operator ==(Point<T> leftOperand, Point<T> rightOperand)
        {
            if (ReferenceEquals(null, leftOperand)) return ReferenceEquals(null, rightOperand);
            return leftOperand.Equals(rightOperand);
        }

        public static bool operator !=(Point<T> leftOperand, Point<T> rightOperand)
        {
            return !(leftOperand == rightOperand);
        }

        public override string ToString()
        {
            return "({0}, {1})".FormatThis(x.ToString(), y.ToString());
        }

        public int CompareTo(Point<T> other)
        {
            int yCmp = Y.CompareTo(other.Y);
            if (yCmp != 0)
                return yCmp;

            return X.CompareTo(other.X);
        }
        
        public static Point<T> operator -(Point<T> lhs, Point<T> rhs)
        {
            return new Point<T>( (dynamic)lhs.X - (dynamic)rhs.X, (dynamic)lhs.Y - (dynamic)rhs.Y );
        }
        public static Point<T> operator /(Point<T> lhs, long rhs)
        {
            return new Point<T>( (T) ((dynamic)lhs.X / rhs), (T) ( (dynamic)lhs.Y / rhs) );
        }
    }
}
