//#define LOGGING_TRACE
#define LOGGING

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Logger = Utils.LoggerFile;

namespace Utils
{
    public struct ModdedLong
    {
        private readonly long value;
        public long Value
        {
            get
            {
                return value;
            }
        }
        public static  int mod = 1000002013;

        /*
        public static implicit operator long(ModdedLong m)
        {
            Logger.Log("Implicit cast moddedlong to long to {0}", m.value);
            return m.value;
        }*/

        public static implicit operator ModdedLong(int val)
        {
            Logger.LogTrace("Implicit cast int to moddedlong {0}", val);
            return new ModdedLong(val);
        }

        public ModdedLong(long v)
        {

            value = v % mod;
            Logger.LogTrace("Construct {0} = {1} ", v, value);
            Preconditions.checkState(value >= 0);
        }

        public static ModdedLong operator +(ModdedLong c1, ModdedLong c2)
        {
            Logger.LogTrace("Adding {0} and {1}", c1, c2);
            return new ModdedLong(c1.value + c2.value );
        }
        public static ModdedLong operator -(ModdedLong c1, ModdedLong c2)
        {
            Logger.LogTrace("Subtract {0} and {1}", c1, c2);
            return new ModdedLong(c1.value - c2.value + mod);
        }
        public static ModdedLong operator *(ModdedLong c1, ModdedLong c2)
        {
            Logger.LogTrace("Multiply {0} and {1}", c1, c2);
            return new ModdedLong(c1.value * c2.value);
        }
        public static ModdedLong operator /(ModdedLong c1, int rhs)
        {

            int div = modInverse(rhs, mod);
            
            Logger.LogTrace("Divide int {0} and {1}.  mult. inv: {2}", c1, rhs, div);
            return new ModdedLong(c1.value * div);
        }
        

        public override string ToString()
        {
            return "{0} mod long".FormatThis(value);
        }

        
        public static int pow(int a, int b)
        {
            return pow(a, b, ModdedLong.mod);
        }
        /*
         * a ^ b % MOD
         */
        public static int pow(int a, int b, int MOD)
        {
            long x = 1;
            long y = a;

            while (b > 0)
            {
                if (b % 2 == 1)
                {
                    x = (x * y);
                    if (x > MOD) x %= MOD;
                }
                y = (y * y);
                if (y > MOD) y %= MOD;
                b /= 2;
            }
            return (int)x;
        }


        /*  Modular Multiplicative Inverse
    
            Using Euler's Theorem.  ***ONly works if mod is prime!!!!***
    
            a^(phi(m)) = 1 (mod m)
    
            a^(-1) = a^(m-2) (mod m) */

        private static long InverseEulerIfModIsPrime(int n)
        {

            long ret = pow(n, (int)(mod - 2), (int)mod);
            Logger.LogTrace("InverseEuler n={0} mod={1}  n^mod-2 = {2}", n, mod, ret);
            return ret;
        }

        /* This function return the gcd of a and b followed by
    the pair x and y of equation ax + by = gcd(a,b)*/
        public static int[] extendedEuclid(int a, int b)
        {
            int x = 1, y = 0;
            int xLast = 0, yLast = 1;
            int q, r, m, n;
            while (a != 0)
            {
                q = b / a;
                r = b % a;
                m = xLast - q * x;
                n = yLast - q * y;
                xLast = x;
                yLast = y;
                x = m;
                y = n;
                b = a;
                a = r;
            }
            return new int[] { b, xLast, yLast };
        }

        /**
         *  returns x such that ax = 1 mod m
         */
        public static int modInverse(int a, int m)
        {
            int[] gcdXY = extendedEuclid(a, m);
            return ( gcdXY[1] + m) % m;
        }
      
    }
}
