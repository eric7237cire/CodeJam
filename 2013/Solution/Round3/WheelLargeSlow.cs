//
#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#else
#define USE_DOUBLE
#endif

using CodeJamUtils;
using CombPerm;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.math;
using Logger = Utils.LoggerFile;

using BruteForceFracClass = Utils.math.BigFraction;

#if USE_DOUBLE
using FracClass = System.Double; 

#else
using FracClass = Utils.math.BigFraction;
#endif

namespace Round3
{
    public class WheelLargeSlow
    {
        bool[] gondalas;
        int N;
        FracClass[][][] Pijk_memoize;
        FracClass[][] Pij_memoize;
        FracClass[][][] Eijk_memoize;
        FracClass[][] Eij_memoize;

        FracClass init;

        public static FracClass computeAnswer(bool[] gondalas)
        {
            FracClass sum = 0;
            WheelLargeSlow dp = new WheelLargeSlow(gondalas);

            for (int i = 0; i < gondalas.Length; ++i)
            {
                if (gondalas[i])
                    continue;

                int nextI = (i + 1) % gondalas.Length;
                FracClass probIFilledLast = dp.P(nextI, i);
                FracClass expValue = dp.E(nextI, i);
#if USE_DOUBLE
                expValue += (double) (gondalas.Length + 1) / 2d;
#else
                expValue += new FracClass(gondalas.Length + 1, 2);
#endif
                sum += probIFilledLast * expValue;

                Preconditions.checkState(sum >= 0);
            }


            return sum;
        }

        public WheelLargeSlow(bool[] gond)
        {
            gondalas = gond;
            N = gondalas.Length;

            int d = gond.Length;

#if USE_DOUBLE
            init = -1;
#else
            init = new FracClass(-1, 1);
#endif

            Pijk_memoize = new FracClass[d][][];
            Eijk_memoize = new FracClass[d][][];
            Pij_memoize = new FracClass[d][];
            Eij_memoize = new FracClass[d][];

            for (int i = 0; i < d; ++i)
            {
                Pijk_memoize[i] = new FracClass[d][];
                Eijk_memoize[i] = new FracClass[d][];

                Pij_memoize[i] = new FracClass[d];
                Eij_memoize[i] = new FracClass[d];

                for (int j = 0; j < d; ++j)
                {
                    Pijk_memoize[i][j] = new FracClass[d];
                    Eijk_memoize[i][j] = new FracClass[d];

                    Eij_memoize[i][j] = init;
                    Pij_memoize[i][j] = init;

                    for (int k = 0; k < d; ++k)
                    {
                        Eijk_memoize[i][j][k] = init;
                        Pijk_memoize[i][j][k] = init;
                    }
                }
            }
        }

        /// <summary>
        /// The probability that gondola j stays empty while we fill interval [i, j) 
        /// and that gondola at position (i+k) is filled last is P(i, j, k) and can be computed as
        /// </summary>
        /// <param name="i"></param>
        /// <param name="j"></param>
        /// <param name="k"></param>
        /// <returns></returns>
        public FracClass P(int i, int j, int k)
        {
            Preconditions.checkState(gondalas[j] == false);

            if (init != Pijk_memoize[i][j][k])
            {
                //Logger.LogDebug("Memoize P {} {} {}", i, j, k);
                return Pijk_memoize[i][j][k];
            }
            ModdedLong start = new ModdedLong(i, N);
            ModdedLong stop = new ModdedLong(j, N);
            ModdedLong mid = new ModdedLong(i + k, N);

            Preconditions.checkState(ModdedLong.isStrictlyBetween(start - 1, stop, mid));

            if (gondalas[mid])
                return Pijk_memoize[i][j][k] = 0;

            int totalLen = ModdedLong.diff(i, j, N) + 1;
            // [i, i+k]
            //bool[] firstHalf = Wheel.copyArray(gondalas, start, mid);
            // [i+k+1, j]
            //bool[] secondHalf = Wheel.copyArray(gondalas, mid + 1, stop);

            Preconditions.checkState(!gondalas[mid]);

            Preconditions.checkState(!gondalas[stop]);

            //Gondolas free in [i, k)
            int freeBeforeK = 0;
            //Gondolas free is [k+1, j)
            int freeAfterK = 0;

            //Does not count endpoints (mid for 1st half, stop for 2nd)
            for (ModdedLong idx = start; idx != mid; ++idx)
                if (!gondalas[idx])
                    ++freeBeforeK;

            for (ModdedLong idx = mid + 1; idx != stop; ++idx)
                if (!gondalas[idx])
                    ++freeAfterK;

            int firstHalfLen = 1 + ModdedLong.diff(start, mid, N);
            int secondHalfLen = 1 + ModdedLong.diff(mid + 1, stop, N);

            Preconditions.checkState(firstHalfLen + secondHalfLen == totalLen);

            //Choose people to go to first half -or- we can choose which ones go to second half.  k and j are predetermined
            //int choose = Combinations.combin(freeBeforeK + freeAfterK, freeBeforeK);
            //long choose = Combinations.combin(freeBeforeK + freeAfterK, freeAfterK);
            BigInteger choose = CombinArray.Instance.combinArray[freeBeforeK + freeAfterK][freeAfterK];

            //Probability that a + 1 people have a gondola on the left side
            FracClass f = 1;
#if USE_DOUBLE
            for (int t = 0; t <= freeBeforeK; ++t)
                f *= (double)firstHalfLen / totalLen;

            for (int t = 0; t < freeAfterK; ++t)
                f *= (double) secondHalfLen / totalLen;
#else
            for (int t = 0; t <= freeBeforeK; ++t)
                f *= new FracClass(firstHalfLen, totalLen);

            for (int t = 0; t < freeAfterK; ++t)
                f *= new FracClass(secondHalfLen, totalLen);
#endif

            FracClass probLeft = P(start, mid);
            FracClass probRight = P(mid + 1, stop);

#if USE_DOUBLE
            FracClass ans = (double)choose * f * probLeft * probRight;
#else
            FracClass ans = choose * f * probLeft * probRight;
#endif
            Preconditions.checkState(ans >= 0);

            //FracClass check = P_bruteForce(gondalas, i, j, k);
            //Preconditions.checkState(ans.Equals(check));
            return Pijk_memoize[i][j][k] = ans;
        }


        /// <summary>
        /// the probability that gondola j will stay empty 
        /// as interval [i, j) fills up assuming only positions
        /// between [i, j] are chosen
        /// </summary>
        /// <param name="i"></param>
        /// <param name="j"></param>
        /// <returns></returns>
        public FracClass P(int i, int j)
        {
            if (init != Pij_memoize[i][j])
                return Pij_memoize[i][j];

            //Logger.LogInfo("P({}, {})", i, j);

            Preconditions.checkState(gondalas[j] == false);
            if (i == j)
            {
                Logger.LogTrace(" P({}, {}) = {}  (i==j)", i, j, 1);
                return Pij_memoize[i][j] = 1;
            }

            //ModdedLong start = new ModdedLong(i, N);
            //ModdedLong stop = new ModdedLong(j, N);

            int ijLength;
            int holeCount = GetHoleCount(i, j, out ijLength);

            //Base case
            if (holeCount == 0)
            {
                Logger.LogTrace(" P({}, {}) = {}  (no free)", i, j, 1);
                return Pij_memoize[i][j] = 1;
            }

            FracClass sum = 0;
            for (int k = 0; k < ijLength - 1; ++k)
            {
                sum += P(i, j, k);
            }

            Logger.LogTrace(" P({}, {}) = {} = {}", i, j, sum, (double) sum);
            return Pij_memoize[i][j] = sum;
        }

        /// <summary>
        /// The expected money we get while filling out the interval [i, j) 
        /// so that the last filled gondola is at position (i+k).
        /// 
        /// This means that the value for filling j is not counted
        /// </summary>
        /// <param name="i"></param>
        /// <param name="j"></param>
        /// <param name="k"></param>
        /// <returns></returns>
        public FracClass E(int i, int j, int k)
        {
            if (init != Eijk_memoize[i][j][k])
            {
                //Logger.LogDebug("Memoize E {} {} {}", i, j, k);
                return Eijk_memoize[i][j][k];
            }

            //Logger.LogTrace("E {} {} {} {} N {}", gondalas.ToCommaString(), i, j, k, N);
           // ModdedLong start = new ModdedLong(i, gondalas.Length);
            ModdedLong stop = new ModdedLong(j, gondalas.Length);
            ModdedLong mid = new ModdedLong(i + k, N);

            Preconditions.checkState(!gondalas[stop]);

            if (gondalas[mid])
            {
                return Eijk_memoize[i][j][k] = 0;
            }

            FracClass expectedLeft = E(i, mid);
            FracClass expectedRight = E(mid + 1, stop);

#if USE_DOUBLE
            FracClass ans = expectedLeft + expectedRight + N - (double) k /2;
#else
            FracClass ans = expectedLeft + expectedRight + N - new FracClass(k, 2);
#endif
            Logger.LogTrace(" E({},{}) = {} + E({},{}) = {} + {} = {}",
                i,
                mid,
                expectedLeft,
                mid + 1,
                stop,
                expectedRight, N, ans);
            //FracClass check = Wheel.E_bruteForce(gondalas, i, j, k, N);
            // Preconditions.checkState(check.Equals(ans));

            return Eijk_memoize[i][j][k] = ans;
        }

        public FracClass E(int i, int j)
        {
            if (init != Eij_memoize[i][j])
                return Eij_memoize[i][j];

            Preconditions.checkState(gondalas[j] == false);
            if (i == j)
                return Eij_memoize[i][j] = 0;

           // Logger.LogInfo("E({}, {})", i, j);

            int ijLength;
            GetHoleCount(i, j, out ijLength);

           //P(i,j, k) / P(i,j) is like the weighted average, where P(i,j) acts like 100%

            FracClass sum = 0;
            for (int k = 0; k < ijLength - 1; ++k)
            {
                sum += P(i, j, k) * E(i, j, k);
            }
            Preconditions.checkState(sum >= 0);
            Logger.LogTrace(" E({}, {}) = {} or {} divided by {} = {} or {}", i, j, 
                sum, (double)sum, P(i,j), sum / P(i, j), (double)(sum / P(i, j)));
            
            return Eij_memoize[i][j] = sum / P(i, j);
        }

        private int GetHoleCount(int i, int j, out int ijLength)
        {
            int holeCount = 0;
            ModdedLong stop = new ModdedLong(j, N);
            for (ModdedLong idx = new ModdedLong(i, N); idx != stop; ++idx)
                if (!gondalas[idx])
                    holeCount++;

            ijLength = ModdedLong.diff(i, j, N) + 1;

            return holeCount;
        }
    }
   
}
