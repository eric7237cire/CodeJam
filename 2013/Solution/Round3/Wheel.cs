#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE

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

namespace Round3
{

    public class WheelInput
    {
        public string wheel;
    }

    class DynamicProgramming
    {
        public string FormatGondolas(int state)
        {
            return string.Join("", state.ToBinaryString(nTotal).Replace('1', 'X').Replace('0', '.').Reverse());
        }
        readonly int endPosition;
        BigInteger[] expectedValue;
        int nTotal;

        public DynamicProgramming(int nTotal)
        {

            this.nTotal = nTotal;
            //nTotal bits == 1
            endPosition = (1 << nTotal) - 1;
            expectedValue = new BigInteger[1 << 20];

            Logger.LogTrace("total {} end {} = {}.", nTotal, endPosition.ToBinaryString(8), FormatGondolas(endPosition));
            for (int i = 0; i < expectedValue.Length; ++i)
            {
                expectedValue[i] = -1;
            }
        }

        public BigInteger calc(int curState, BigInteger pMult)
        {
            if (curState == endPosition)
            {
                return 0;
            }

            if (expectedValue[curState] != -1)
            {
                return expectedValue[curState];
            }

            Logger.LogTrace("calc {} pMult {}", string.Join("", curState.ToBinaryString(nTotal).Replace('1', 'X').Replace('0', '.').Reverse()), pMult);

            BigInteger totalReturn = 0;

            //Choose each possibility
            for (int i = 0; i < nTotal; ++i)
            {
                int pos = i;
                int price = nTotal;

                BitSet csBitSet = new BitSet(curState);
                while (true)
                {
                    if (!csBitSet[pos])
                    {
                        Logger.ChangeIndent(4);
                        BigInteger toAdd = calc(curState | 1 << pos, pMult / nTotal);
                        totalReturn += price * pMult + toAdd;
                        Logger.LogTrace("Calculating spot {} next free {} Add [ price {} * mult {} + rest {} = {} ].  Total {}", i, pos, price, pMult, toAdd, price * pMult + toAdd, totalReturn);
                        Logger.ChangeIndent(-4);
                        break;
                    }

                    ++pos;
                    if (pos == nTotal)
                        pos = 0;
                    --price;
                    Preconditions.checkState(price >= 1);
                }
            }

            //Here rotate the binary nTotal times as X..XX is the same as ..XXX or .XXX.
            int equivalentState = curState;
            //Logger.LogInfo("1 eq state {}", equivalentState.ToBinaryString(nTotal));
            for (int i = 0; i < nTotal; ++i)
            {
                bool firstBit = equivalentState.GetBit(0);
                equivalentState >>= 1;

                if (firstBit)
                    equivalentState = equivalentState.SetBit(nTotal - 1);
                else
                    equivalentState = equivalentState.ClearBit(nTotal - 1);
                // Logger.LogInfo("eq state {}", equivalentState.ToBinaryString(nTotal));
                expectedValue[equivalentState] = totalReturn;
            }
            Preconditions.checkState(equivalentState == curState);

            return expectedValue[curState] = totalReturn;
        }

    }

    public class Wheel : InputFileConsumer<WheelInput, string>, InputFileProducer<WheelInput>
    {

        public string processInput(WheelInput input)
        {
            int nTotal = input.wheel.Length;

            bool[] initialState = new bool[nTotal];
            
            for (int i = 0; i < nTotal; ++i)
            {
                if (input.wheel[i] == 'X') initialState[i] = true;
                if (input.wheel[i] == '.') initialState[i] = false;

            }

            BigFraction ans = computeAnswer(initialState);

            return ((double) ans).ToUsString(9);
        }

        public string processInputSmall(WheelInput input)
        {
            int nTotal = input.wheel.Length;

            int initialState = 0;
            int holeCount = 0;
            for (int i = 0; i < nTotal; ++i)
            {
                if (input.wheel[i] == 'X') initialState = initialState.SetBit(i);
                if (input.wheel[i] == '.') holeCount++;

            }

            BigInteger denom = 1;

            for (int i = 0; i < holeCount; ++i)
                denom *= nTotal;

            DynamicProgramming dp = new DynamicProgramming(nTotal);

            BigInteger num = dp.calc(initialState, denom / nTotal);

            Logger.LogDebug(" {} / {} ", num, denom);

            return ((double)new BigFraction(num, denom)).ToUsString(9);
        }

        /// <summary>
        /// Start can be > stop, then wraps around.  Copies [start, stop] to source
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="source"></param>
        /// <param name="start"></param>
        /// <param name="stop"></param>
        /// <returns></returns>
        public static T[] copyArray<T>(T[] source, int start, int stop)
        {
            int len = 1+ ModdedLong.diff(start, stop, source.Length);

            T[] ret = new T[len];
            for(int i = 0; i < len; ++i)
            {
                ret[i] = source[(start + i) % source.Length];
            }

            return ret;
        }

        //Returns true if the last index is filled in last
        /// <summary>
        /// 
        /// </summary>
        /// <param name="gondolas"></param>
        /// <param name="permOrder"></param>
        /// <param name="secondToLastToFill"></param>
        /// <returns>Value given permutation. 0 if conditions that gondolas[last] must be filled last and optionally secondToLast </returns>
        public static int simulatePermutation(bool[] gondolas, IList<int> permOrder, int secondToLastToFill = -1, int N = 300)
        {
            //Defensive copy
            bool[] sim = new bool[gondolas.Length];
            Array.Copy(gondolas, sim, gondolas.Length);

            //end must be empty
            Preconditions.checkState(!sim.GetLastValue());
            //Filling in false values
            Preconditions.checkState(sim.Count((b) => !b) == permOrder.Count);

            int valueTotal = 0;

            //Go through each one but the last
            for (int idx = 0; idx < permOrder.Count - 1; ++idx)
            {
                int chosenPos = permOrder[idx];

                while (chosenPos < sim.Length - 1 && sim[chosenPos])
                    ++chosenPos;

                if (chosenPos == sim.Length - 1)
                {
                    return 0;
                }

                sim[chosenPos] = true;

                valueTotal += N - (chosenPos - permOrder[idx]);

                //Enforce 2nd to last
                if (secondToLastToFill != -1 && idx == permOrder.Count - 2 && chosenPos != secondToLastToFill)
                {
                    return 0;
                }
            }

            valueTotal += N - (sim.Length - 1 - permOrder.GetLastValue());

            Preconditions.checkState(valueTotal != 0);
            return valueTotal;
        }

        /// <summary>
        ///  so first let’s look at P(i, j), the probability that j-th gondola will stay empty while we fill up 
        ///  all gondolas from the interval [i, j) assuming each coming person approaches some gondola in inteval [i, j] (note that j is included here)
        /// </summary>
        /// <param name="gondalas"></param>
        /// <param name="i"></param>
        /// <param name="j"></param>
        /// <returns></returns>
        public static BigFraction P_bruteForce(bool[] gondalas, int i, int j)
        {
            Preconditions.checkState(gondalas[j] == false);
                        
            ModdedLong pos = new ModdedLong(i, gondalas.Length);
            ModdedLong stop = new ModdedLong(j, gondalas.Length);

            bool[] ij = Wheel.copyArray(gondalas, i, j);
            //Otherwise too slow
            Preconditions.checkState(ij.Length <= 7);
            
            int holeCount = ij.Count((b) => !b );
            
            //Now cycle through all permutations
            int numerator = 0;
            int denominator = 0;
                        
            foreach (List<int> list in Combinations.nextPermutationWithRepetition(holeCount, ij.Length))
            {
                Logger.LogTrace("Perm {} for ij {}", list.ToCommaString(), ij.ToCommaString());
                //Simulate 
                if (0 != simulatePermutation(ij, list))
                {
                    Logger.LogTrace("last element filled last");
                    ++numerator;
                }
                
                ++denominator;
            }

            return new BigFraction(numerator, denominator);
        }

        //
        /// <summary>
        /// The probability that gondola j stays empty while we fill interval [i, j) and that 
        /// gondola at position (i+k) is filled last is P(i, j, k) and can be computed as:
        /// </summary>
        /// <param name="gondalas">True if filled, false if empty</param>
        /// <param name="i"></param>
        /// <param name="j"></param>
        /// <param name="k"></param>
        /// <returns></returns>
        public static BigFraction P_bruteForce(bool[] gondalas, int i, int j, int k)
        {
            Preconditions.checkState(gondalas[j] == false);

            Logger.LogDebug("P_bruteForce {} [{} to {}] k={}", gondalas.ToCommaString(), i, j, k);

            bool[] ij = Wheel.copyArray(gondalas, i, j);
            //Otherwise too slow
            Preconditions.checkState(ij.Length <= 7);

            int holeCount = ij.Count((b) => !b);

            ModdedLong pos = new ModdedLong(i, gondalas.Length);
            ModdedLong stop = new ModdedLong(j, gondalas.Length);
            ModdedLong mid = new ModdedLong(i + k, gondalas.Length);

            Preconditions.checkState(ModdedLong.isStrictlyBetween(pos-1, stop, mid));

            if (gondalas[mid])
                return 0;

            //Now cycle through all permutations
            int numerator = 0;
            int denominator = 0;
           
            foreach (List<int> perm in Combinations.nextPermutationWithRepetition(holeCount, ij.Length))            
            {
                Logger.LogTrace("Perm {} for ij {}.  ", perm.ToCommaString(), ij.ToCommaString());
                    
                if (0 != simulatePermutation(ij, perm, k))
                {
                    Logger.LogTrace("Perm {} for ij {}.  mid filled 2nd to last {}", perm.ToCommaString(), ij.ToCommaString(), mid.Value);
                    ++numerator;
                }
                ++denominator;
            }

            return new BigFraction(numerator, denominator);
        }

        
        

        

        public static BigFraction computeAnswer(bool[] gondalas)
        {
            BigFraction sum = 0;
            for(int i = 0; i < gondalas.Length; ++i)
            {
                if (gondalas[i])
                    continue;

                int nextI = (i + 1) % gondalas.Length;
                BigFraction probIFilledLast = P(gondalas, nextI, i);
                BigFraction expValue = E(gondalas, nextI, i, gondalas.Length);
                expValue += new BigFraction(gondalas.Length + 1, gondalas.Length);
                sum += probIFilledLast * expValue;
            }

            return sum;
        }

        /// <summary>
        /// The expected money we get while filling out the interval [i, j) 
        /// so that the last filled gondola is at position (i+k) is:
        /// </summary>
        /// <param name="gondalas"></param>
        /// <param name="i"></param>
        /// <param name="j"></param>
        /// <param name="k"></param>
        /// <param name="N"></param>
        /// <returns></returns>
        public static BigFraction E_bruteForce(bool[] gondalas, int i, int j, int k, int N)
        {
            Preconditions.checkState(gondalas[j] == false);

            Logger.LogDebug("P_bruteForce {} [{} to {}] k={}", gondalas.ToCommaString(), i, j, k);

            bool[] ij = Wheel.copyArray(gondalas, i, j);
            //Otherwise too slow
            Preconditions.checkState(ij.Length <= 7);

            int holeCount = ij.Count((b) => !b);

            ModdedLong pos = new ModdedLong(i, gondalas.Length);
            ModdedLong stop = new ModdedLong(j, gondalas.Length);
            ModdedLong mid = new ModdedLong(i + k, gondalas.Length);

            Preconditions.checkState(ModdedLong.isStrictlyBetween(pos - 1, stop, mid));

            if (gondalas[mid])
                return 0;

            //Now cycle through all permutations
            int numerator = 0;
            int denominator = 0;

            foreach (List<int> perm in Combinations.nextPermutationWithRepetition(holeCount, ij.Length))
            {
                
                int value =  simulatePermutation(ij, perm, k, N);
                Logger.LogTrace("Perm {} for ij {} Value {}", perm.ToCommaString(), ij.ToCommaString(), value);

                numerator += value;
                ++denominator;
            }

            return new BigFraction(numerator, denominator);
        }
        public WheelInput createInput(Scanner scanner)
        {
            WheelInput input = new WheelInput();
            input.wheel = scanner.nextWord();
            return input;
        }

        public class DynamicProgrammingLarge
        {
            bool[] gondalas;
            int N;
            BigFraction[][][] Pijk_memoize;
            BigFraction[][] Pij_memoize;
            BigFraction[][][] Eijk_memoize;
            BigFraction[][] Eij_memoize;

            public DynamicProgrammingLarge(bool[] gond)
            {
                gondalas = gond;
                N = gondalas.Length;

                int d = gond.Length;
                Pijk_memoize = new BigFraction[d][][];
                Eijk_memoize = new BigFraction[d][][];
                Pij_memoize = new BigFraction[d][];
                Eij_memoize = new BigFraction[d][];

                for(int i = 0; i < d; ++i)
                {
                    Pijk_memoize[i] = new BigFraction[d][];
                    Eijk_memoize[i] = new BigFraction[d][];

                    Pij_memoize[i] = new BigFraction[d];
                    Eij_memoize[i] = new BigFraction[d];

                    for(int j = 0; j < d; ++j)
                    {
                        Pijk_memoize[i][j] = new BigFraction[d];
                        Eijk_memoize[i][j] = new BigFraction[d];

                        Eij_memoize[i][j] = -1;
                        Pij_memoize[i][j] = -1;

                        for(int k = 0; k < d; ++k)
                        {
                            Eijk_memoize[i][j][k] = -1;
                            Pijk_memoize[i][j][k] = -1;
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
            public BigFraction P(int i, int j, int k)
            {
                Preconditions.checkState(gondalas[j] == false);



                ModdedLong start = new ModdedLong(i, gondalas.Length);
                ModdedLong stop = new ModdedLong(j, gondalas.Length);
                ModdedLong mid = new ModdedLong(i + k, gondalas.Length);

                Preconditions.checkState(ModdedLong.isStrictlyBetween(start - 1, stop, mid));

                if (gondalas[mid])
                    return 0;

                int totalLen = ModdedLong.diff(i, j, gondalas.Length) + 1;
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

                int firstHalfLen = ModdedLong.diff(start, mid, N);
                int secondHalfLen = ModdedLong.diff(mid+1, stop, N);

                Preconditions.checkState(firstHalfLen + secondHalfLen + 2 == totalLen);

                //Choose people to go to first half -or- we can choose which ones go to second half.  k and j are predetermined
                //int choose = Combinations.combin(freeBeforeK + freeAfterK, freeBeforeK);
                int choose = Combinations.combin(freeBeforeK + freeAfterK, freeAfterK);

                //Probability that a + 1 people have a gondola on the left side
                BigFraction f = 1;
                for (int t = 0; t <= freeBeforeK; ++t)
                    f *= new BigFraction(firstHalfLen, totalLen);

                for (int t = 0; t < freeAfterK; ++t)
                    f *= new BigFraction(secondHalfLen, totalLen);

                BigFraction probLeft = P( start, mid);
                BigFraction probRight = P( mid+1, stop );

                BigFraction ans = choose * f * probLeft * probRight;

                //BigFraction check = P_bruteForce(gondalas, i, j, k);
                //Preconditions.checkState(ans.Equals(check));
                return ans;
            }

            public BigFraction P(int i, int j)
            {
                Preconditions.checkState(gondalas[j] == false);
                if (i == j)
                    return 1;

                ModdedLong start = new ModdedLong(i, gondalas.Length);
                ModdedLong stop = new ModdedLong(j, gondalas.Length);

                int ijLength;
                int holeCount = GetHoleCount(i, j, out ijLength);
                
                //Base case
                if (holeCount == 0)
                    return 1;

                BigFraction sum = 0;
                for (int k = 0; k < ijLength - 1; ++k)
                {
                    sum += P(i, j, k);
                }

                return sum;
            }

            public static BigFraction E(bool[] gondalas, int i, int j, int k, int N)
            {
                Logger.LogTrace("E {} {} {} {} N {}", gondalas.ToCommaString(), i, j, k, N);
                ModdedLong start = new ModdedLong(i, gondalas.Length);
                ModdedLong stop = new ModdedLong(j, gondalas.Length);
                ModdedLong mid = new ModdedLong(i + k, gondalas.Length);

                bool[] ij = Wheel.copyArray(gondalas, i, j);
                // [i, i+k]
                bool[] firstHalf = Wheel.copyArray(gondalas, start, mid);
                // [i+k+1, j]
                bool[] secondHalf = Wheel.copyArray(gondalas, mid + 1, stop);

                Preconditions.checkState(!gondalas[stop]);

                if (gondalas[mid])
                {
                    return 0;
                }

                return E(firstHalf, 0, k, N) + E(secondHalf, 0, secondHalf.Length - 1, N) + N - new BigFraction(k, 2);
            }

            public BigFraction E(int i, int j)
            {
                Preconditions.checkState(gondalas[j] == false);
                if (i == j)
                    return 1;

                int ijLength;
                int holeCount = GetHoleCount(i, j, out ijLength);

                //Base case
                if (holeCount == 1)
                    return 1;

                BigFraction sum = 0;
                for (int k = 0; k < ijLength - 1; ++k)
                {
                    sum += P(i, j, k) * E(gondalas, i, j, k, N);
                }

                return sum / P(i, j);
            }

            private int GetHoleCount(int i, int j, out int ijLength)
            {
                int holeCount = 0;
                ModdedLong stop = new ModdedLong(j, N);
                for (ModdedLong idx = new ModdedLong(i, N); idx != stop; ++idx)
                    if (!gondalas[idx])
                        holeCount++;

                ijLength = ModdedLong.diff(i, j, gondalas.Length) + 1;

                return holeCount;
            }
        }
    }
}
