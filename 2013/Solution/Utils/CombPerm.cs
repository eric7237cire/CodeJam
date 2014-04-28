using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CombPerm
{
    static class Combinations
    {
        public static int factorial(int n)
        {
            checked
            {
                int r = 1;
                for (int i = 2; i <= n; ++i)
                {
                    r *= i;
                }
                return r;
            }
        }
        public static int combin(int n, int k)
        {
            checked { 
            int num = factorial(n);
            int den = factorial(k) * factorial(n - k);

            return num / den;
            }
        }

        public static IEnumerable<long> iterateCombin(int n, int k)
        {
            long next = (1L << k) - 1;

            while ((next & (1L << n)) == 0)
            {
                // Gosper's hack, described by Knuth, referenced in
                // http://en.wikipedia.org/wiki/Combinatorial_number_system#Applications
                long result = next;
                long x = next;
                long u = x & -x;
                long v = u + x;
                x = v + (((v ^ x) / u) >> 2);
                next = x;
                yield return result;
            }
        }
    }
}
