
//#define LOGGING_DEBUG
//#define LOGGING_INFO
#if DEBUG
//#define LOGGING_TRACE
#endif

using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

using Utils;

using Logger = Utils.LoggerFile;

namespace Utils.math
{
    public struct BigFraction : INumeric<BigFraction>, IComparable<BigFraction>, IEquatable<BigFraction>
    {
        private readonly BigInteger numerator;
        private readonly BigInteger denominator;

        public BigInteger Numerator { get { return numerator; } }
        public BigInteger Denominator { get { return denominator; } }

        #region constructor
        public BigFraction(long num, long den)
            : this(new BigInteger(num), new BigInteger(den))
        {

        }
        
        public static BigInteger LeastCommonDenominator(params BigFraction[] fracList)
        {
			BigInteger lcd = fracList[0].Denominator;
			
			for( int i = 1; i < fracList.Count(); ++i)
			{
				BigInteger gcd = BigInteger.GreatestCommonDivisor(lcd, fracList[i].Denominator);
				lcd *= fracList[i].Denominator / gcd;
			}
				
			return lcd;
        }
        
        public static BigFraction createFromDouble(double num, int useDecimals = 9)
        {
        	bool isNeg = num < 0;
        	if (isNeg)
        		num *= -1;
        	
        	Logger.LogTrace("Num {}. is neg {}", num, isNeg);
        	
        	BigInteger intPart = (BigInteger) Math.Floor(num);
        	num -= Math.Floor(num);
        	
        	long mult = 1;
        	for(int i = 0; i < useDecimals; ++i)
        		mult *= 10;
        	
        	Preconditions.checkState(num < 1 && num >= 0);
        	
        	num *= mult;
        	
        	Logger.LogTrace("Num {}. mult {} is neg {}", num, mult, isNeg);
        	
        	BigInteger decPart = (BigInteger) Math.Floor(num);
        	
        	Preconditions.checkState(decPart < mult);
        	
        	Logger.LogTrace(" intPart {} mult {} + dec part {}", intPart, mult, decPart);
        	
        	return new BigFraction( (isNeg ? -1 : 1) * (intPart * mult + decPart), mult);
        	
        }
        
        private BigFraction(bool a, BigInteger num, BigInteger den )
        {
            numerator = num;
            denominator = den;
        }
        public BigFraction(BigInteger num, BigInteger den, bool reduce = true )
        {
            Logger.LogTrace("BigFraction cons {} / {}", num, den);
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
                if (reduce)
                {
                    // reduce numerator and denominator by greatest common denominator
                    BigInteger gcd = BigInteger.GreatestCommonDivisor(num, den);
                    if (BigInteger.One.CompareTo(gcd) < 0)
                    {
                        num = BigInteger.Divide(num, gcd);
                        den = BigInteger.Divide(den, gcd);
                    }

                    
                }

                // move sign to numerator
                if (den.Sign < 0)
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

        #region methods

        public BigFraction reduce()
        {
            return new BigFraction(numerator, denominator, true);
        }

        /// <summary>
        /// First integer >= to the fraction
        /// </summary>
        /// <returns></returns>
        public BigInteger ceil()
        {
            BigInteger remainder;
            BigInteger ans = BigInteger.DivRem(numerator, denominator, out remainder);

            if (remainder.IsZero)
            {
                return ans;
            }
            else
            {
                return ans + 1;
            }
        }

        /// <summary>
        /// First integer <= to the fraction
        /// </summary>
        /// <returns></returns>
        public BigInteger floor()
        {
            BigInteger remainder;
            BigInteger ans = BigInteger.DivRem(numerator, denominator, out remainder);

            return ans;
        }


        #endregion

        #region operators to BigFraction
        public static implicit operator BigFraction(int i)
        {
            return new BigFraction(false, new BigInteger(i), BigInteger.One);
        }
        public static implicit operator BigFraction(long i)
        {
            return new BigFraction(new BigInteger(i), BigInteger.One);
        }
        public static implicit operator BigFraction(BigInteger i)
        {
            return new BigFraction(i, BigInteger.One);
        }
        public static explicit operator BigFraction(Fraction f)
        {
            return new BigFraction(f.Numerator, f.Denominator);
        }
        #endregion

        #region operators from BigFraction
        public static explicit operator double(BigFraction f)
        {
            BigInteger remainder;
            BigInteger ans = BigInteger.DivRem(f.numerator, f.denominator, out remainder);

            int nDigitsToExtract = 20;
            BigInteger mult = BigInteger.Pow(10, nDigitsToExtract);

            BigInteger decimals = BigInteger.Divide(remainder * mult, f.denominator);

            return (double)ans + (double) decimals / 1e20;
        }
        public static explicit operator BigInteger(BigFraction f)
        {
            return BigInteger.Divide(f.numerator,f.denominator);
        }
        #endregion

        #region operators
        public static BigFraction operator +(int i, BigFraction x)
        {
            return x.Add(i);
        }
        public static BigFraction operator +(BigFraction lhs, BigFraction rhs)
        {
            return lhs.Add(rhs);
        }
        public static BigFraction operator /(BigFraction lhs, BigFraction rhs)
        {
            return lhs.Divide(rhs);
        }
        public static BigFraction operator -(BigFraction lhs, BigFraction rhs)
        {
            return lhs.Subtract(rhs);
        }
        public static BigFraction operator -(BigFraction lhs)
        {
            return new BigFraction(-lhs.numerator, lhs.denominator);
        }
        public static BigFraction operator *(long i, BigFraction x)
        {
            return x.Multiply(i);
        }
        public static BigFraction operator %(BigFraction lhs, long i)
        {
            return lhs - lhs.Divide(i).floor() * i;
        }
        public static BigFraction operator %(BigFraction lhs, BigInteger i)
        {
            return lhs - lhs.Divide(i).floor() * i;
        }
        public static BigFraction operator *(BigFraction x, long num)
        {
            return x.Multiply(num);
        }
        public static BigFraction operator *(BigFraction lhs, BigFraction rhs)
        {
            return lhs.Multiply(rhs);
        }
        public static bool operator< (BigFraction lhs, BigFraction rhs)
        {
            return lhs.CompareTo(rhs) < 0;
        }
        public static bool operator >(BigFraction lhs, BigFraction rhs)
        {
            return lhs.CompareTo(rhs) > 0;
        }
        public static bool operator <=(BigFraction lhs, BigFraction rhs)
        {
            return lhs.CompareTo(rhs) <= 0;
        }
        public static bool operator >=(BigFraction lhs, BigFraction rhs)
        {
            return lhs.CompareTo(rhs) >= 0;
        }
        public static bool operator !=(BigFraction lhs, BigFraction rhs)
        {
            return lhs.CompareTo(rhs) != 0;
        }
        public static bool operator ==(BigFraction lhs, BigFraction rhs)
        {
            return lhs.CompareTo(rhs) == 0;
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

#if mono
return "{0} / {1}".FormatThis(numerator.ToString(), denominator.ToString() );
#else
            return "{0} / {1}".FormatThis(numerator.ToString("0,0", new CultureInfo("en-US")), denominator.ToString("0,0", new CultureInfo("en-US")));
#endif
        }
        
        public override bool Equals(object obj)
        {
        	if (! (obj is BigFraction) )
        		return false;
            return this.Equals( (BigFraction)obj );
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
        public bool Equals(BigFraction other)
        {
        	//TODO inconsistent with reduced == unreduced fraction
            return CompareTo(other) == 0;
        }
        
        #endregion
    }

}
