using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeJamUtils
{
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
        }

        public T Y
        {
            get
            {
                return y;
            }
        }

        public Point(T left, T right)
        {
            this.x = left;
            this.y = right;
        }

        public override bool Equals(object obj)
        {
            return this.Equals(obj as Point<T>);

        }
        public override int GetHashCode()
        {
            return Tuple.Create<T,T>(x, y).GetHashCode();
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
    }
}
