using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils.tree
{
    public class FenwickTree
    {
        private int[] ft;

        private static int SubtractLeastSignificantBit(int S)
        {
            return (S & (-S));
        }

        public FenwickTree(int n)
        {
            ft = new int[n + 1];
        }


        /// <summary>
        /// Given a binary integer value x, the most significant 1 bit 
        /// (highest numbered element of a bit set) can be computed using 
        /// a SWAR algorithm that recursively "folds" the upper bits into 
        /// the lower bits. This process yields a bit vector with the same 
        /// most significant 1 as x, but all 1's below it. Bitwise AND of 
        /// the original value with the complement of the "folded" value 
        /// shifted down by one yields the most significant bit. For a 32-bit value:
        /// </summary>
        /// <param name="x"></param>
        /// <returns></returns>
        static int msb32(int x)
        {
            x |= (x >> 1);
            x |= (x >> 2);
            x |= (x >> 4);
            x |= (x >> 8);
            x |= (x >> 16);
            return (x & ~(x >> 1));
        }

        public int findLowestIndexWithFreq(int cumFre)
        {
            int idx = 0; // this var is result of function

            int maxVal = ft.Length - 1;

            int bitMask = msb32(maxVal);

            int lowestFound = ft.Length + 10;

            while ((bitMask != 0) && (idx < maxVal))
            { // nobody likes overflow :)
                int tIdx = idx + bitMask; // we make midpoint of interval

                //In case array size is not a power of 2
                if (tIdx >= ft.Length)
                {
                    bitMask >>= 1;
                    continue;
                }

                if (cumFre == ft[tIdx]) // if it is equal, we just return idx
                {
                    lowestFound = Math.Min(lowestFound, tIdx);
                }

                if (cumFre > ft[tIdx])
                {
                    // if tree frequency "can fit" into cumFre,
                    // then include it
                    idx = tIdx; // update index 
                    cumFre -= ft[tIdx]; // set frequency for next loop 
                }
                bitMask >>= 1; // half current interval
            }

            if (lowestFound <= maxVal)
                return lowestFound;


            return -1;

        }


        public int findAnyIndexWithFreq(int cumFre)
        {
            int idx = 0; // this var is result of function

            int maxVal = ft.Length;

            int bitMask = msb32(maxVal);

            while ((bitMask != 0) && (idx < maxVal))
            { // nobody likes overflow :)
                int tIdx = idx + bitMask; // we make midpoint of interval
                if (cumFre == ft[tIdx]) // if it is equal, we just return idx
                    return tIdx;
                else if (cumFre > ft[tIdx])
                {
                    // if tree frequency "can fit" into cumFre,
                    // then include it
                    idx = tIdx; // update index 
                    cumFre -= ft[tIdx]; // set frequency for next loop 
                }
                bitMask >>= 1; // half current interval
            }
            if (cumFre != 0) // maybe given cumulative frequency doesn't exist
                return -1;
            else
                return idx;
        }



        /// <summary>
        /// Returns cumulative sum from [1, right]
        /// </summary>
        /// <param name="ft"></param>
        /// <param name="right"></param>
        /// <returns></returns>
        public static int SumTo(int[] ft, int right, int mod = int.MaxValue)
        {
            /*
             * Each index that is a power of 2 holds the sum from
             * [1, index]. (1, 2, 4, 8, 16, 32, etc)  To handle an index like 19,
             * subtract the highest power of 2, 16, and you get a recursive type structure.
             */
            int sum = 0;
            for (; right > 0; right -= SubtractLeastSignificantBit(right))
            {
                sum += ft[right];
                sum %= mod;
                Preconditions.checkState(sum >= 0);
            }
            return sum;
        }

        /* Range sum query */
        public int SumFromTo(int left, int right, int mod = int.MaxValue)
        { // returns RSQ(a, b)
            Preconditions.checkState(left <= right);
            Preconditions.checkState(left > 0);
            Preconditions.checkState(right < ft.Length);
            if (left == 1)
                return SumTo(ft, right, mod);

            int ans = SumTo(ft, right, mod) - SumTo(ft, left - 1, mod);
            if (ans < 0)
                ans += mod;

            Preconditions.checkState(0 <= ans && ans < mod);
            //range beginning to b - range to a - 1
            return ans;
        }

        // adjusts value of the k-th element by v (v can be +ve/inc or -ve/dec)
        public void AdjustIndexBy(int index, int valueToAdd, int mod = int.MaxValue)
        {
            //Indexes go from 1 to N
            Preconditions.checkState(index > 0);

            Preconditions.checkState(index <= ft.Length);

            // note: n = ft.size() - 1
            for (; index < ft.Length; index += SubtractLeastSignificantBit(index))
            {
                ft[index] += valueToAdd;
                ft[index] %= mod;

                Preconditions.checkState(ft[index] >= 0 && ft[index] < mod);
            }
        }
    }


}
