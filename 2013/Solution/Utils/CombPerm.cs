using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

namespace CombPerm
{
    public static class Combinations
    {
        public static long factorial(int n)
        {
            checked
            {
                long r = 1;
                for (int i = 2; i <= n; ++i)
                {
                    r *= i;
                }
                return r;
            }
        }
        public static long combin(int n, int k)
        {
            checked { 
            long num = factorial(n);
            long den = factorial(k) * factorial(n - k);

            return num / den;
            }
        }

        /// <summary>
        /// Generates array C[n][k]  n choose k
        /// </summary>
        /// <param name="max"></param>
        /// <returns></returns>
        public static BigInteger[][] generateCombin(int max) 
        {

            BigInteger[][] combinations = new BigInteger[max + 1][];
            for(int n = 0; n <= max; ++n)
            {
                combinations[n] = new BigInteger[max + 1];
                for(int k = 0; k <= max; ++k)
                {
                    if (n<k)
                        combinations[n][k] =0;
                    else if (n==k || k==0 )
                        combinations[n][k] = 1;
                    else
                        combinations[n][k] = (combinations[n-1][k] + combinations[n-1][k-1]);
                }
            }
        
            return combinations;
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

        /// <summary>
        /// 
        /// </summary>
        /// <param name="permLength">Length of permutation</param>
        /// <param name="numElementValues">Values in returned list vary from [0, numElementValues)</param>
        /// <returns></returns>
        public static IEnumerable<List<int>> nextPermutationWithRepetition(int permLength, int numElementValues)
        {
            List<int> list = new List<int>();
            for (int i = 0; i < permLength; ++i)
            {
                list.Add(0);
            }

            while (true)
            {
                bool fullLoop = true;

                yield return list;

                //Try all permutations.  This loop finds the first position that can be incremented
                for (int pos = 0; pos < list.Count; ++pos)
                {
                    list[pos]++;

                    //Incremented, we are done
                    if (list[pos] < numElementValues)
                    {
                        fullLoop = false;
                        break;
                    }

                    //This position is now zero, look to increment combin
                    list[pos] = 0;
                }

                if (fullLoop)
                    break;

            }
        }

        /// <summary>
        /// Cycles through all permutations of an array
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <typeparam name="L"></typeparam>
        /// <param name="array"></param>
        /// <returns></returns>
        public static IEnumerable<L> nextPermutation<T, L>(L array)
            where T : IComparable<T>
            where L : IList<T>
        {
            while (true)
            {
                yield return array;

                // Find longest non-increasing suffix
                int i = array.Count - 1;
                while (i > 0 && array[i - 1].CompareTo(array[i]) >= 0)
                    i--;
                // Now i is the head index of the suffix

                // Are we at the last permutation already?
                if (i == 0)
                    yield break;

                // Let array[i - 1] be the pivot
                // Find rightmost element that exceeds the pivot
                int j = array.Count - 1;
                while (array[j].CompareTo(array[i - 1]) <= 0)
                    j--;
                // Now the value array[j] will become the new pivot
                // Assertion: j >= i

                // Swap the pivot with j
                T temp = array[i - 1];
                array[i - 1] = array[j];
                array[j] = temp;

                // Reverse the suffix
                j = array.Count - 1;
                while (i < j)
                {
                    temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                    i++;
                    j--;
                }
                
            }
        }
    }
}
