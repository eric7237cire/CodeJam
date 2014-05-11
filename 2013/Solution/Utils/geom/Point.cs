using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.math;

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
                else return 0;
            }
            else return -ccw(pt, q1, q2);     // both above or below

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
            double area2 = (b.X - a.X) * (c.Y - a.Y) - (b.Y - a.Y) * (c.X - a.X);
            if (area2 < 0) return -1;
            else if (area2 > 0) return +1;
            else return 0;
        }

        public static Stack<Point<double>> ConvexHull(this IList<Point<double>> pts) 
        {
            return ConvexHull(pts, polarOrder, ccw);
        }
        public static Stack<Point<int>> ConvexHull(this IList<Point<int>> pts)
        {
            return ConvexHull(pts, polarOrder, ccw);
        }

        //GrahamScan, returns them in clockwise order, or counter-clockwise if poped off 1 by 1 off the stack
        public static Stack<Point<T>> ConvexHull<T>(this IList<Point<T>> pts, 
            Func<Point<T>, Point<T>, Point<T>, int> polarOrderComp,
            Func<Point<T>, Point<T>, Point<T>, int> ccwFunc
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
            Array.Sort(points, 1, N - 1, Comparer<Point<T>>.Create((lhs, rhs) =>
                { return polarOrderComp(points[0], lhs, rhs); }
                ));
            
            

            Stack<Point<T>> hull = new Stack<Point<T>>();
            hull.Push(points[0]);       // p[0] is first extreme point

            // find index k1 of first point not equal to points[0]
            int k1;
            for (k1 = 1; k1 < N; k1++)
                if (!points[0].Equals(points[k1])) break;
            if (k1 == N) return hull;        // all points equal

            // find index k2 of first point not collinear with points[0] and points[k1]
            int k2;
            for (k2 = k1 + 1; k2 < N; k2++)
                if (ccwFunc(points[0], points[k1], points[k2]) != 0) break;
            hull.Push(points[k2 - 1]);    // points[k2-1] is second extreme point

            // Graham scan; note that points[N-1] is extreme point different from points[0]
            for (int i = k2; i < N; i++)
            {
                Point<T> top = hull.Pop();
                //IMPORTANT: TO INCLUDE COLINEAR POINTS IN HULL, USE < OTHERWISE <=
                while (ccwFunc(hull.Peek(), top, points[i]) < 0)
                {
                    top = hull.Pop();
                }
                hull.Push(top);
                hull.Push(points[i]);
            }

            return hull;
        }

        public static double PolygonArea(this IList<Point<int>> points)
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



    public class Point<T> : IEquatable<Point<T>>, IComparable<Point<T>> where T:IComparable<T>
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
            int hash = 17;
            hash = hash * 23 + x.GetHashCode();
            hash = hash * 23 + y.GetHashCode();
            return hash;
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
    }
}
