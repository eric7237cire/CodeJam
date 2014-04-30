using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Numerics;

namespace Utils.math
{
    public interface INumeric<T>
    {
        T Add(T rhs);
        T Subtract(T rhs);
        T Multiply(T rhs);
        T Divide(T rhs);

        bool IsZero();
    }

    public struct DoubleNumeric : INumeric<DoubleNumeric>
    {
        double value;
        public DoubleNumeric Add(DoubleNumeric rhs)
        {
            return new DoubleNumeric { value = value + rhs.value };
        }

        public DoubleNumeric Subtract(DoubleNumeric rhs)
        {
            return new DoubleNumeric { value = value - rhs.value };
        }

        public DoubleNumeric Multiply(DoubleNumeric rhs)
        {
            return new DoubleNumeric { value = value * rhs.value };
        }

        public DoubleNumeric Divide(DoubleNumeric rhs)
        {
            return new DoubleNumeric { value = value / rhs.value };
        }

        public bool IsZero()
        {
            return value == 0;
        }

        public static implicit operator DoubleNumeric(int i)
        {
            return new DoubleNumeric { value = i };
        }
        public static implicit operator DoubleNumeric(long i)
        {
            return new DoubleNumeric { value = i };
        }
        public static implicit operator DoubleNumeric(double d)
        {
            return new DoubleNumeric { value = d };
        }

        public static explicit operator long(DoubleNumeric d)
        {
            return (long)d.value;
        }
        public static implicit operator double(DoubleNumeric d)
        {
            return d.value;
        }
    }

    public struct BigIntegerNumeric : INumeric<BigIntegerNumeric>
    {
        BigInteger value;

        public BigIntegerNumeric Add(BigIntegerNumeric rhs)
        {
            return new BigIntegerNumeric{ value = BigInteger.Add(value, rhs.value) };
        }


        public BigIntegerNumeric Subtract(BigIntegerNumeric rhs)
        {
            return new BigIntegerNumeric { value = BigInteger.Subtract(value, rhs.value) };
        }


        public BigIntegerNumeric Multiply(BigIntegerNumeric rhs)
        {
            throw new NotImplementedException();
        }

        public BigIntegerNumeric Divide(BigIntegerNumeric rhs)
        {
            throw new NotImplementedException();
        }


        public bool IsZero()
        {
            throw new NotImplementedException();
        }
    }
}
