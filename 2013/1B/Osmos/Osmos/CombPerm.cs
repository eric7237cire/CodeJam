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
    }
}
