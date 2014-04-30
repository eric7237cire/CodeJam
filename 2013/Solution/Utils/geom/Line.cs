using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils.geom
{

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
}
