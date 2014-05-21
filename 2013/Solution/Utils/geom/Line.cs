using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils.geom
{

    using Utils.math;
    //using NumType = System.Double;
    //using Point = Point<System.Double>;
    using PP = Tuple<Point<int>, Point<int>>;

    public static class LineExt
    {

        public static Line<double> createFromPoints(Point<double> p1, Point<double> p2)
        {
            double A = p2.Y - p1.Y;
            double B = p1.X - p2.X;
            double C = A * p1.X + B * p1.Y;
            return Line<double>.createStandard(A, B, C);
        }

        public static Line<T> createFromPoints<T>(Point<T> p1, Point<T> p2) where T: INumeric<T>, IComparable<T>
        {
            T A = p2.Y.Subtract(p1.Y);
            T B = p1.X.Subtract(p2.X);
            T C = A.Multiply(p1.X).Add(B.Multiply(p1.Y));
            return Line<T>.createStandard(A, B, C);
        }

        public static Line<double> createFromCoords(double x1, double y1, double x2, double y2)
        {
            double A = y2 - y1;
            double B = x1 - x2;
            double C = A * x1 + B * y1;
            return Line<double>.createStandard(A, B, C);
        }

        public static Line<int> createFromCoords(int x1, int y1, int x2, int y2)
        {
            int A = y2 - y1;
            int B = x1 - x2;
            int C = A * x1 + B * y1;
            return Line<int>.createStandard(A, B, C);
        }

        public static LineSegment<int> createSegmentFromCoords(int x1, int y1, int x2, int y2)
        {
            int A = y2 - y1;
            int B = x1 - x2;
            int C = A * x1 + B * y1;
            LineSegment<int> ls = new LineSegment<int>();
            ls.line = Line<int>.createStandard(A, B, C);
            ls.p1 = new Point<int>(x1, y1);
            ls.p2 = new Point<int>(x2, y2);
            return ls;
        }

        public static LineSegment<long> createSegmentFromCoords(long x1, long y1, long x2, long y2)
        {
            long A = y2 - y1;
            long B = x1 - x2;
            long C = A * x1 + B * y1;
            LineSegment<long> ls = new LineSegment<long>();
            ls.line = Line<long>.createStandard(A, B, C);
            ls.p1 = new Point<long>(x1, y1);
            ls.p2 = new Point<long>(x2, y2);
            return ls;
        }
        
        public static LineSegment<double> createSegmentFromCoords(double x1, double y1, double x2, double y2)
        {
            double A = y2 - y1;
            double B = x1 - x2;
            double C = A * x1 + B * y1;
            LineSegment<double> ls = new LineSegment<double>();
            ls.line = Line<double>.createStandard(A, B, C);
            ls.p1 = new Point<double>(x1, y1);
            ls.p2 = new Point<double>(x2, y2);
            return ls;
        }

        public static Line<T> createFromCoords<T>(T x1, T y1, T x2, T y2) where T : INumeric<T>
        {
            T A = y2.Subtract(y1);
            T B = x1.Subtract(x2);
            T C = A.Multiply(x1).Add(B.Multiply(y1));
            return Line<T>.createStandard(A, B, C);
        }

        public static Point<double> intersection(this Line<double> lhs, Line<double> rhs)
        {
            //Using idea to solve the 2 equations Ax + By = C and A'x + B'y = C' for x and y

            double det = lhs.A * rhs.B - rhs.A * lhs.B;
            if (det == 0)
            {
                //parrallel
                return null;
            }

            return new Point<double>((rhs.B * lhs.C - lhs.B * rhs.C) / det,
                (lhs.A * rhs.C - rhs.A * lhs.C) / det);
        }

        public static Point<T> intersection<T>(this Line<T> lhs, Line<T> rhs) where T : INumeric<T>, IComparable<T>
        {
            T det = lhs.A.Multiply(rhs.B).Subtract(rhs.A.Multiply(lhs.B));
            if (det.IsZero())
            {
                //parrallel
                return null;
            }

            return new Point<T>((rhs.B.Multiply(lhs.C).Subtract(lhs.B.Multiply(rhs.C))).Divide(det),
                (lhs.A.Multiply(rhs.C).Subtract(rhs.A.Multiply(lhs.C))).Divide(det));
        }

        //Returns a line perpendicular to line XY going through pointP
        public static LineSegment<int> getPerpendicularLineSeg(this LineSegment<int> lineXY, Point<int> pointP)
        {
            int A = -lineXY.line.B;
            int B = lineXY.line.A;
            int C = A * pointP.X + B * pointP.Y;

            LineSegment<int> line = new LineSegment<int>();

            line.line = Line<int>.createStandard(A, B, C);

            //-A / B is slope
            Point<int> p2 = new Point<int>(pointP.X + B, pointP.Y - A);
            line.p1 = pointP;
            line.p2 = p2;

            return line;

        }

        public static Line<double> ToDouble(this Line<int> line)
        {
            return Line<double>.createStandard(line.A, line.B, line.C);
        }
        public static Point<double> ToDouble(this Point<int> point)
        {
            return new Point<double>(point.X, point.Y);
        }
        /// <summary>
        /// Returns the line segment p1 p2 where p2 is also on lineXY
        /// where line seg p1 p2 is perpendicular to line x y.
        /// 
        /// p2 is the intersection
        /// </summary>
        /// <param name="lineXY"></param>
        /// <param name="pointP"></param>
        /// <returns></returns>
        public static LineSegment<double> getPerpendicularLineSegWithIntersection(this LineSegment<int> lineXY, Point<int> pointP)
        {
            long A = -lineXY.line.B;
            long B = lineXY.line.A;
            long C = A * pointP.X + B * pointP.Y;

            LineSegment<double> lineSeg = new LineSegment<double>();

            lineSeg.line = Line<double>.createStandard(A, B, C);

            //-A / B is slope
            Point<double> p2 = intersection(lineXY.line.ToDouble(), lineSeg.line);

            lineSeg.p1 = pointP.ToDouble();
            lineSeg.p2 = p2;
            return lineSeg;

        }

        public static int crossProduct(this Point<int> v1, Point<int> v2)
        {
            return (v1.X * v2.Y) - (v1.Y * v2.X);
        }

        public static Point<int> translate(this Point<int> a, Point<int> newOrigin)
        {
            return new Point<int>(a.X - newOrigin.X, a.Y - newOrigin.Y);
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
        public static bool sameSide(this LineSegment<int> lineSeg, Point<int> a, Point<int> b)
        {
            int cp = b.translate(a).crossProduct( lineSeg.p1.translate(a)) ;
            int cp2 = b.translate(a).crossProduct( lineSeg.p2.translate(a));
            if (cp * cp2 >= 0)
                return true;
            else
                return false;
        }
        
        public static bool sameSide(Point<int> x, Point<int> y, Point<int> a, Point<int> b)
        {
            int cp = b.translate(a).crossProduct( x.translate(a)) ;
            int cp2 = b.translate(a).crossProduct( y.translate(a));
            if (cp * cp2 >= 0)
                return true;
            else
                return false;
        }

        // Given three colinear points p, q, r, the function checks if
        // point q lies on line segment 'pr'
        static bool onSegment(Point<int> p, Point<int> q, Point<int> r)
        {
            if (q.X <= Math.Max(p.X, r.X) && q.X >= Math.Min(p.X, r.X) &&
                q.Y <= Math.Max(p.Y, r.Y) && q.Y >= Math.Min(p.Y, r.Y))
                return true;

            return false;
        }

        public static bool onLine(this Line<double> line, double x, double y)
        {
            double C = line.A * x + line.B * y;
            return isEqual(C, line.C);
        }

        private static bool isEqual(double v1, double v2, double tolerance = 1e-6)
        {
            return Math.Abs(v1 - v2) <= tolerance;
        }
        

        // The main function that returns true if line segment 'p1q1'
        // and 'p2q2' intersect.
        static bool doIntersect(Point<int> p1, Point<int> q1, Point<int> p2, Point<int> q2)
        {
            // Find the four orientations needed for general and
            // special cases
            int o1 = PointExt.ccw(p1, q1, p2);
            int o2 = PointExt.ccw(p1, q1, q2);
            int o3 = PointExt.ccw(p2, q2, p1);
            int o4 = PointExt.ccw(p2, q2, q1);

            // General case
            if (o1 != o2 && o3 != o4)
                return true;

            // Special Cases
            // p1, q1 and p2 are colinear and p2 lies on segment p1q1
            if (o1 == 0 && onSegment(p1, p2, q1)) return true;

            // p1, q1 and p2 are colinear and q2 lies on segment p1q1
            if (o2 == 0 && onSegment(p1, q2, q1)) return true;

            // p2, q2 and p1 are colinear and p1 lies on segment p2q2
            if (o3 == 0 && onSegment(p2, p1, q2)) return true;

            // p2, q2 and q1 are colinear and q1 lies on segment p2q2
            if (o4 == 0 && onSegment(p2, q1, q2)) return true;

            return false; // Doesn't fall in any of the above cases
        }

        
        public static bool checkIsPolygon(this IList<Point<int>> polygon)
        {
            List<PP> segs = new List<PP>();
            for(int i = 0; i < polygon.Count; ++i)
            {
                int nextPointIdx = (i+1) % polygon.Count;
                segs.Add(new PP(polygon[i], polygon[nextPointIdx]));
            }

            if (segs.Count < 3)
            {
                return false;
            }
            for(int s1 = 0; s1 < segs.Count; ++s1)
            {
                int iSecCount = 0;
                for(int s2 = 0; s2 < segs.Count; ++s2)
                {
                    if (s2 == s1)
                        continue;

                    if (doIntersect(segs[s1].Item1, segs[s1].Item2, segs[s2].Item1, segs[s2].Item2))
                        ++iSecCount;
                }

                if (iSecCount != 2)
                {
                    return false;
                }
            }


            return true;
        }
    }

    public class LineSegment<T> where T : IComparable<T>
    {
        public Point<T> p1 { get; internal set; }
        public Point<T> p2 { get; internal set; }

        //The line going through p1 and p2
        public Line<T> line { get; internal set; } 
    }
    public class Line<T>
    {
        //Ax + By = C
        public T A;
        public T B;
        public T C;

        public static Line<T> createStandard(T A, T B, T C) 
        {
            return new Line<T>{A = A, B = B, C = C};
        }

    }

    
}
