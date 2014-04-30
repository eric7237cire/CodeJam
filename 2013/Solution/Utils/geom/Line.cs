using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils.geom
{

    using Utils.math;
    using NumType = System.Double;
    using Point = Point<System.Double>;

    //Ax + By = C
    public class Line
    {
        private NumType A;
        private NumType B;
        private NumType C;

        private Line(NumType A, NumType B, NumType C)
        {
            this.A = A; this.B = B; this.C = C;
        }

        public static Line createStandard(NumType A, NumType B, NumType C) 
        {
            return new Line(A, B, C);
        }

        public static Line createFromPoints(Point p1, Point p2)
        {
            NumType A = p2.Y - p1.Y;
            NumType B =  p1.X - p2.X;
            NumType C = A*p1.X + B * p1.Y;
            return new Line(A, B, C);
        }

        public static Line createFromCoords(NumType x1, NumType y1, NumType x2, NumType y2)
        {
            NumType A = y2 - y1;
            NumType B = x1 - x2;
            NumType C = A * x1 + B * y1;
            return new Line(A, B, C);
        }

        public Point intersection(Line rhs)
        {
            NumType det = A * rhs.B - rhs.A * B;
            if (det == 0)
            {
                //parrallel
                return null;
            }

            return new Point((rhs.B * C - B * rhs.C) / det,
                (A * rhs.C - rhs.A * C) / det);
        }
    }

    public class LineNumeric<T> where T : INumeric<T>
    {
        private T A;
        private T B;
        private T C;

        private LineNumeric(T A, T B, T C)
        {
            this.A = A; this.B = B; this.C = C;
        }

        public static LineNumeric<T> createStandard(T A, T B, T C)
        {
            return new LineNumeric<T>(A, B, C);
        }

        public static LineNumeric<T> createFromPoints(Point<T> p1, Point<T> p2)
        {
            T A = p2.Y.Subtract(p1.Y);
            T B = p1.X.Subtract(p2.X);
            T C = A.Multiply(p1.X).Add ( B.Multiply(p1.Y) );
            return new LineNumeric<T>(A, B, C);
        }

        public static LineNumeric<T> createFromCoords(T x1, T y1, T x2, T y2)
        {
            T A = y2.Subtract(y1);
            T B = x1.Subtract(x2);
            T C = A.Multiply(x1).Add(B.Multiply(y1));
            return new LineNumeric<T>(A, B, C);
        }

        public Point<T> intersection(LineNumeric<T> rhs)
        {
            T det = A.Multiply(rhs.B).Subtract( rhs.A.Multiply(B) );
            if (det.IsZero())
            {
                //parrallel
                return null;
            }

            return new Point<T>((rhs.B.Multiply(C).Subtract( B.Multiply(rhs.C) ) ).Divide(det),
                (A.Multiply(rhs.C).Subtract( rhs.A.Multiply(C) )).Divide(det));
        }
    }
}
