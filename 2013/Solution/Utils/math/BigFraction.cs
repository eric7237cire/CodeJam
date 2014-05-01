using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

namespace Utils.math
{
    public struct BigFraction : INumeric<BigFraction>, IComparable<BigFraction>
    {
        private BigInteger numerator;
        private BigInteger denominator;

        #region constructor
        public BigFraction(long num, long den)
            : this(new BigInteger(num), new BigInteger(den))
        {

        }
        public BigFraction(BigInteger num, BigInteger den)
        {
            Preconditions.checkState(num != null);
            Preconditions.checkState(den != null);

            if (den.IsZero)
            {
                throw new ArgumentException("zero");
            }
            if (num.IsZero)
            {
                numerator = BigInteger.Zero;
                denominator = BigInteger.One;
            }
            else
            {

                // reduce numerator and denominator by greatest common denominator
                BigInteger gcd = BigInteger.GreatestCommonDivisor(num, den);
                if (BigInteger.One.CompareTo(gcd) < 0)
                {
                    num = BigInteger.Divide(num, gcd);
                    den = BigInteger.Divide(den, gcd);
                }

                // move sign to numerator
                if (BigInteger.Zero.CompareTo(den) > 0)
                {
                    num = BigInteger.Negate(num);
                    den = BigInteger.Negate(den);
                }

                // store the values in the final fields
                numerator = num;
                denominator = den;

            }
        }
        #endregion

        #region operators to BigFraction
        public static implicit operator BigFraction(int i)
        {
            return new BigFraction(new BigInteger(i), BigInteger.One);
        }
        public static implicit operator BigFraction(long i)
        {
            return new BigFraction(new BigInteger(i), BigInteger.One);
        }
        #endregion

        #region operators from BigFraction
        public static explicit operator double(BigFraction f)
        {
            return (double)f.numerator / (double)f.denominator;
        }
        #endregion

        #region INumeric<BigFraction>
        public BigFraction Add(BigFraction fraction)
        {

            if (fraction.IsZero())
            {
                return this;
            }



            if (denominator.Equals(fraction.denominator))
            {
                return new BigFraction(BigInteger.Add(numerator, fraction.numerator), denominator);
            }
            else
            {
                BigInteger num;
                BigInteger den;
                num = BigInteger.Add(BigInteger.Multiply(numerator, fraction.denominator),
                    BigInteger.Multiply(fraction.numerator, denominator));
                den = BigInteger.Multiply(denominator, fraction.denominator);
                return new BigFraction(num, den);
            }

        }

        public BigFraction Subtract(BigFraction rhs)
        {
            return Add(new BigFraction(BigInteger.Negate(rhs.numerator), rhs.denominator));
        }

        public BigFraction Multiply(BigFraction rhs)
        {
            return new BigFraction(BigInteger.Multiply(numerator, rhs.numerator), BigInteger.Multiply(denominator, rhs.denominator));
        }

        public BigFraction Divide(BigFraction rhs)
        {
            return new BigFraction(BigInteger.Multiply(numerator, rhs.denominator), BigInteger.Multiply(denominator, rhs.numerator));
        }

        public BigFraction Abs()
        {
            if (numerator.CompareTo(BigInteger.Zero) < 0)
            {
                return new BigFraction(BigInteger.Negate(numerator), denominator);
            }
            return this;
        }

        public bool IsZero()
        {
            return numerator.Equals(BigInteger.Zero);
        }
        #endregion


        #region interface IComparable<BigFraction>

        public int CompareTo(BigFraction other)
        {
            BigInteger nOd = BigInteger.Multiply(numerator, other.denominator);
            BigInteger dOn = BigInteger.Multiply(denominator, other.numerator);
            return nOd.CompareTo(dOn);
        }

        #endregion

        #region Object overrides
        public override string ToString()
        {
            if (denominator.Equals(BigInteger.One))
                return numerator.ToString();

            return "{0} / {1}".FormatThis(numerator.ToString(), denominator.ToString());
        }
        #endregion
    }

}