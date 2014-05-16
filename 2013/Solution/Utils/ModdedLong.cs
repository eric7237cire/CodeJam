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
    public struct ModdedLong : IEquatable<ModdedLong>
    {
        private readonly int value;
        public readonly int mod;

        public int Value
        {
            get
            {
                return value;
            }
        }
        
        /*
        public static implicit operator long(ModdedLong m)
        {
            Logger.Log("Implicit cast moddedlong to long to {0}", m.value);
            return m.value;
        }*/

        /*public static implicit operator ModdedLong(int val)
        {
            Logger.LogTrace("Implicit cast int to moddedlong {0}", val);
            return new ModdedLong(val);
        }*/

        public ModdedLong(long v, int mod ) //1000002013)
        {
            this.mod = mod;
            long test = v % mod;
            value = (int) (v % mod);
            Logger.LogTrace("Construct {0} = {1} ", v, value);
            Preconditions.checkState(value >= 0);
        }

        public static ModdedLong operator +(ModdedLong c1, ModdedLong c2)
        {
            Preconditions.checkState(c1.mod == c2.mod);
            Logger.LogTrace("Adding {0} and {1}", c1, c2);
            return new ModdedLong(c1.value + c2.value, c1.mod);
        }
        public static ModdedLong operator -(ModdedLong c1, ModdedLong c2)
        {
            Preconditions.checkState(c1.mod == c2.mod);
            Logger.LogTrace("Subtract {0} and {1}", c1, c2);
            return new ModdedLong(c1.value - c2.value + c1.mod, c1.mod);
        }
        public static ModdedLong operator -(ModdedLong c1, int c2)
        {
            Logger.LogTrace("Subtract {0} and {1}", c1, c2);
            return new ModdedLong(c1.value - c2 + c1.mod, c1.mod);
        }
        public static ModdedLong operator +(ModdedLong c1, int c2)
        {
            Logger.LogTrace("Subtract {0} and {1}", c1, c2);
            return new ModdedLong(c1.value + c2, c1.mod);
        }
        public static ModdedLong operator *(ModdedLong c1, ModdedLong c2)
        {
            Preconditions.checkState(c1.mod == c2.mod);
            Logger.LogTrace("Multiply {0} and {1}", c1, c2);
            return new ModdedLong((long)c1.value * c2.value, c1.mod);
        }
        public static ModdedLong operator *(ModdedLong c1, int c2)
        {
            Logger.LogTrace("Multiply {0} and {1}", c1, c2);
            return new ModdedLong((long)c1.value * c2, c1.mod);
        }
        public static ModdedLong operator *(int c2, ModdedLong c1)
        {
            return c1 * c2;
        }
        public static ModdedLong operator /(ModdedLong c1, int rhs)
        {

            int div = modInverse(rhs, c1.mod);

            Logger.LogTrace("Divide int {0} and {1}.  mult. inv: {2}", c1, rhs, div);
            return new ModdedLong( (long)c1.value * div, c1.mod);
        }

        public static implicit operator int(ModdedLong f)
        {
            return f.value;
        }


        public override string ToString()
        {
            return "{0} mod long".FormatThis(value);
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

        private static long InverseEulerIfModIsPrime(int n, int mod)
        {

            long ret = pow(n, (int)(mod - 2), (int)mod);
            Logger.LogTrace("InverseEuler n={0} mod={1}  n^mod-2 = {2}", n, mod, ret);
            return ret;
        }

        /* This function return the gcd of a and b followed by
    the pair x and y of equation ax + by = gcd(a,b)*/
        public static int[] extendedEuclid(int a, int b)
        {
            Logger.LogTrace("Start {} {}", a, b);
            int s_last2 = 1, t_last2 = 0;
            int s_last = 0, t_last = 1;
            int q, r, s=0, t=0;
            while (b != 0)
            {
                q = a / b;
                r = a % b;
                s = s_last2 - q * s_last;
                t = t_last2 - q * t_last;
                Logger.LogTrace("q {} r {} s {} t {}", q, r, s, t);

                s_last2 = s_last;
                t_last2 = t_last;

                s_last = s;
                t_last = t;
                
                a = b;
                b = r;
                Logger.LogTrace("x {} y {} a {} b {}", s_last2, t_last2, a, b);
            }
            return new int[] { a, s_last2, t_last2 };
        }

       

        /**
         *  returns x such that ax = 1 mod m
         */
        public static int modInverse(int a, int modulus)
        {
            int[] gcdXY = extendedEuclid(a, modulus);
            //a and m must be coprime
            Preconditions.checkState(gcdXY[0] == 1);
            //the +m %m is to prevent it from being negative

            return gcdXY[1] < 0 ? gcdXY[1] + modulus : gcdXY[1];
            //return (gcdXY[1] + modulus) % modulus;
        }

        public static long gcd_recursive(long a, long b)
        {
            if (b != 0)
                return gcd_recursive(b, a % b);
            else
                return a;
        }

        /*
         *  mir(a, m) = x
         *  ax === 1  (mod m)
         *  ax + qm = 1
         *  (q is just an integer, negative if a is positive)
         *  
         * x = (1 - qm) / a
         * and 
         * q = (1 - ax) / m
         * which equals mir(m, a) !
         */
        public static long mod_inverse_recursive(long a, long m)
        {
            Logger.LogTrace("mod inverse a {} m {}", a, m);
            a %= m;
            Preconditions.checkState( a != 0 );
            if (a == 1)
            {
                Logger.LogTrace("return 1");
                return 1;
            }
            else
            {
                Logger.ChangeIndent(4);
                long mir = mod_inverse_recursive(m, a);
                Logger.ChangeIndent(-4);
                Logger.LogTrace("return [ (1 - {} * {}) / {} ] % {} = {}",
                    m, mir, a, m, ((1 - m * mir) / a) % m);
                return ((1 - m * mir) / a) % m;
            }
        }

        /// <summary>
        ///  Returns c, such that (a + c) % m == b 
        /// </summary>
        /// <param name="a"></param>
        /// <param name="b"></param>
        /// <param name="m"></param>
        /// <returns></returns>
        public static long diff(long a, long b, long m)
        {
            Preconditions.checkState(0 <= a && a < m);
            Preconditions.checkState(0 <= b && b < m);
            if (b >= a)
            {
                return b - a;
            }

            return (b + m - a);
        }

        /// <summary>
        /// is p between left and right
        /// </summary>
        /// <param name="a"></param>
        /// <param name="b"></param>
        /// <param name="c"></param>
        /// <param name="m"></param>
        /// <returns></returns>
        public static bool isStrictlyBetween(long left, long right, long p)
        {
            if (left < right)
            {
                return p > left && p < right;
            }

            return p > left || p < right;
        }

        public bool Equals(ModdedLong other)
        {
            Preconditions.checkState(mod == other.mod);
            return value == other.value;
        }
    }
}
