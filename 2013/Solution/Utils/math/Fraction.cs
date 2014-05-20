//#define LOGGING
//#define LOGGING_DEBUG
//#define LOGGING_INFO
//#define LOGGING_TRACE

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;



using System.Globalization;
using System.Numerics;

using Utils;

using Logger = Utils.LoggerFile;

namespace Utils.math
{
    public struct Fraction : INumeric<Fraction>, IComparable<Fraction>, IEquatable<Fraction>
    {
        private readonly long numerator;
        private readonly long denominator;

        public long Numerator { get { return numerator; } }
        public long Denominator { get { return denominator; } }

        #region constructor
       

        public static long LeastCommonDenominator(params Fraction[] fracList)
        {
            long lcd = fracList[0].Denominator;

            for (int i = 1; i < fracList.Count(); ++i)
            {
                long gcd = ModdedLong.gcd_recursive(lcd, fracList[i].Denominator);
                lcd *= fracList[i].Denominator / gcd;
            }

            return lcd;
        }

        private Fraction(bool a, long num, long den)
        {
            numerator = num;
            denominator = den;
        }
        public Fraction(long num, long den, bool reduce = true)
        {
            Logger.LogTrace("Fraction cons {} / {}", num, den);
            if (den == 0)
            {
                throw new ArgumentException("zero");
            }
            if (num == 0)
            {
                numerator = 0;
                denominator = 1;
            }
            else
            {
                if (reduce)
                {
                    // reduce numerator and denominator by greatest common denominator
                    long gcd = ModdedLong.gcd_recursive(num, den);
                    if (1 <  gcd)
                    {
                        num /= gcd;
                        den /= gcd;
                    }


                }

                // move sign to numerator
                if (den < 0)
                {
                    num = -num;
                    den = -den;
                }

                // store the values in the final fields
                numerator = num;
                denominator = den;


            }
        }
        #endregion

        #region methods

        public Fraction reduce()
        {
            return new Fraction(numerator, denominator, true);
        }

        /// <summary>
        /// First integer >= to the fraction
        /// </summary>
        /// <returns></returns>
        public long ceil()
        {
        	long div = numerator / denominator;
        	
            if (numerator % denominator == 0)
                return div;

            if (numerator < 0)
            	return div;
            
            return div + 1;
            
        }

        /// <summary>
        /// First integer <= to the fraction
        /// </summary>
        /// <returns></returns>
        public long floor()
        {
            return numerator / denominator;
        }


        #endregion

        #region operators to Fraction
        public static implicit operator Fraction(int i)
        {
            return new Fraction(i, 1);
        }
        public static implicit operator Fraction(long i)
        {
            return new Fraction(i, 1);
        }
        
        #endregion

        #region operators from Fraction
        public static explicit operator double(Fraction f)
        {
            return (double)f.numerator / (double)f.denominator;
        }
        public static explicit operator long(Fraction f)
        {
            return f.floor();
        }
        #endregion

        #region operators
        public static Fraction operator +(int i, Fraction x)
        {
            return x.Add(i);
        }
        public static Fraction operator +(Fraction lhs, Fraction rhs)
        {
            return lhs.Add(rhs);
        }
        public static Fraction operator /(Fraction lhs, Fraction rhs)
        {
            return lhs.Divide(rhs);
        }
        public static Fraction operator -(Fraction lhs, Fraction rhs)
        {
            return lhs.Subtract(rhs);
        }
        public static Fraction operator -(Fraction lhs)
        {
            return new Fraction(-lhs.numerator, lhs.denominator);
        }
        public static Fraction operator *(long i, Fraction x)
        {
            return x.Multiply(i);
        }
        public static Fraction operator %(Fraction lhs, long i)
        {
            return lhs - lhs.Divide(i).floor() * i;
        }
        
        public static Fraction operator *(Fraction x, long num)
        {
            return x.Multiply(num);
        }
        public static Fraction operator *(Fraction lhs, Fraction rhs)
        {
            return lhs.Multiply(rhs);
        }
        public static bool operator <(Fraction lhs, Fraction rhs)
        {
            return lhs.CompareTo(rhs) < 0;
        }
        public static bool operator >(Fraction lhs, Fraction rhs)
        {
            return lhs.CompareTo(rhs) > 0;
        }
        public static bool operator <=(Fraction lhs, Fraction rhs)
        {
            return lhs.CompareTo(rhs) <= 0;
        }
        public static bool operator >=(Fraction lhs, Fraction rhs)
        {
            return lhs.CompareTo(rhs) >= 0;
        }
        public static bool operator !=(Fraction lhs, Fraction rhs)
        {
            return lhs.CompareTo(rhs) != 0;
        }
        public static bool operator ==(Fraction lhs, Fraction rhs)
        {
            return lhs.CompareTo(rhs) == 0;
        }
        #endregion

        #region INumeric<Fraction>
        public Fraction Add(Fraction fraction)
        {

            if (fraction.IsZero())
            {
                return this;
            }

            if (denominator == fraction.denominator)
            {
                return new Fraction(numerator + fraction.numerator,
                    denominator);
            }
            else
            {
                long num;
                long den;
                num = (numerator * fraction.denominator) + 
                    (fraction.numerator * denominator);
                den = denominator * fraction.denominator;
                return new Fraction(num, den);
            }

        }

        public Fraction Subtract(Fraction rhs)
        {
            return Add(new Fraction(-rhs.numerator, rhs.denominator));
        }

        public Fraction Multiply(Fraction rhs)
        {
            return new Fraction(numerator * rhs.numerator, 
                denominator * rhs.denominator);
        }

        public Fraction Divide(Fraction rhs)
        {
            return new Fraction(numerator * rhs.denominator,
                denominator * rhs.numerator);
        }

        public Fraction Abs()
        {
            if (numerator< 0) 
            {
                return new Fraction(-numerator, denominator);
            }
            return this;
        }

        public bool IsZero()
        {
            return numerator == 0;
        }
        #endregion


        #region interface IComparable<Fraction>

        public int CompareTo(Fraction other)
        {
            long nOd = (numerator * other.denominator);
            long dOn = (denominator * other.numerator);
            return nOd.CompareTo(dOn);
        }

        #endregion

        #region Object overrides
        public override string ToString()
        {
            if (denominator == 1)
                return numerator.ToString();

#if mono
return "{0} / {1}".FormatThis(numerator.ToString(), denominator.ToString() );
#else
            return "{0} / {1}".FormatThis(numerator.ToString("0,0", new CultureInfo("en-US")), denominator.ToString("0,0", new CultureInfo("en-US")));
#endif
        }

        public override bool Equals(object obj)
        {
            if (!(obj is Fraction))
                return false;
            return this.Equals((Fraction)obj);
        }

        public override int GetHashCode()
        {
            int hash = 17;
            hash = hash * 23 + numerator.GetHashCode();
            hash = hash * 23 + denominator.GetHashCode();
            return hash;
        }

        #endregion

        #region interface IEquatable
        public bool Equals(Fraction other)
        {
            //TODO inconsistent with reduced == unreduced fraction
            return CompareTo(other) == 0;
        }
        #endregion
    }

}
