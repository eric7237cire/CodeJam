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

    public static class LineExt
    {

        public static Line<double> createFromPoints(Point<double> p1, Point<double> p2)
        {
            double A = p2.Y - p1.Y;
            double B = p1.X - p2.X;
            double C = A * p1.X + B * p1.Y;
            return Line<double>.createStandard(A, B, C);
        }

        public static Line<T> createFromPoints<T>(Point<T> p1, Point<T> p2) where T: INumeric<T>
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

        public static Point<T> intersection<T>(this Line<T> lhs, Line<T> rhs) where T : INumeric<T>
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
    }

    public class LineSegment<T>
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
