using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
namespace Utils.geom
{
    public static class PointExt
    {
        public static Point<double> Add(this Point<double> lhs, Point<double> rhs)
        {
            return new Point<double>(lhs.X + rhs.X, lhs.Y + rhs.Y);
        }
    }
    public class Point<T> : IEquatable<Point<T>>
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

        public Point(T left, T right)
        {
            this.x = left;
            this.y = right;
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
            return "({0}, {1})".FormatThis(x.ToString(),y.ToString());
        }
    }
}
