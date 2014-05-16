#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE

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
        ///  so first let’s look at P(i, j), the probability that j-th gondola will stay empty while we fill up 
        ///  all gondolas from the interval [i, j) assuming each coming person approaches some gondola in inteval [i, j] (note that j is included here)
        /// </summary>
        /// <param name="gondalas"></param>
        /// <param name="i"></param>
        /// <param name="j"></param>
        /// <returns></returns>
        public static BigFraction P(bool[] gondalas, int i, int j)
        {
            Preconditions.checkState(gondalas[i] == false);
            Preconditions.checkState(gondalas[j] == false);
            Preconditions.checkState(gondalas.Length <= 7);

            //Create a list of all the holes
            List<int> holePositions = new List<int>();

            ModdedLong pos = new ModdedLong(i, gondalas.Length);
            ModdedLong stop = new ModdedLong(j, gondalas.Length);
            
            while (!pos.Equals(stop))
            {
                if (!gondalas[pos])
                {
                    holePositions.Add(pos);
                }

                pos += 1;
            }

            //Now cycle through all permutations
            int numerator = 0;
            int denominator = 0;

            int counter = 0;
            foreach (List<int> list in Combinations.nextPermutationWithRepetition(holePositions.Count, gondalas.Length))
            {
                

                ++counter;
            }

            return new BigFraction(numerator, denominator);
        }

        //
        /// <summary>
        /// The probability that gondola j stays empty while we fill interval [i, j) and that gondola at position (i+k) is filled last is P(i, j, k) and can be computed as:
        /// </summary>
        /// <param name="gondalas">True if filled, false if empty</param>
        /// <param name="i"></param>
        /// <param name="j"></param>
        /// <param name="k"></param>
        /// <returns></returns>
        public static BigFraction P(bool[] gondalas, int i, int j, int k)
        {
            Preconditions.checkState(gondalas[i] == false);
            Preconditions.checkState(gondalas[j] == false);
            Preconditions.checkState(gondalas[i + k] == false);
            Preconditions.checkState(ModdedLong.isStrictlyBetween(i, j - 1, i + k));

            //Create a list of all the holes
            List<int> holePositions = new List<int>();

            ModdedLong pos = new ModdedLong(i, gondalas.Length);
            ModdedLong stop = new ModdedLong(j, gondalas.Length);
            ModdedLong mid = new ModdedLong(i + k, gondalas.Length);
            //stop -= 1;

            int check = 0;
            while (!pos.Equals(stop))
            {
                if (!gondalas[pos])
                {
                    holePositions.Add(pos);
                }

                pos += 1;

                ++check;
                Preconditions.checkState(check < gondalas.Length);
            }

            //Now cycle through all permutations
            int numerator = 0;
            int denominator = 0;
           
            foreach (List<int> perm in Combinations.nextPermutation<int, List<int>>(holePositions))
            {
                Logger.LogTrace("Perm {} stop {} mid - 1 {}", perm.ToCommaString(), stop-1, mid);
                if ((perm[perm.Count - 1] == mid) && perm[perm.Count - 2] == stop-1)
                    ++numerator;
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
    }
}
