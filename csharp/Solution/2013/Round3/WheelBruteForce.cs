#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE
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
namespace Round3_2013.Problem4
{
    public class WheelBruteForce
    {
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
        public static BruteForceFracClass E_bruteForce(bool[] gondalas, int i, int j, int k, int N)
        {
            Preconditions.checkState(gondalas[j] == false);

            Logger.LogDebug("E_bruteForce {} [{} to {}] k={}", gondalas.ToCommaString(), i, j, k);

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

            foreach (List<int> perm in Combinations.nextPermutationWithRepetition(holeCount - 1, ij.Length - 1))
            {

                int value = simulatePermutationExpectedValue(ij, perm, k, N);
                Logger.LogTrace("Perm {} for ij {} Value {}", perm.ToCommaString(), ij.ToCommaString(), value);

                if (value == -1)
                    continue;

                numerator += value;
                ++denominator;
            }

            Logger.LogTrace(" Return {} / {}", numerator, denominator);
            return new BruteForceFracClass(numerator, denominator);
        }

        //See comment in WheelFast
        public static Fraction getE_wheelFast(bool[] a, int i, int j, int holeCount, int N)
        {
            Logger.LogTrace("i {} j {} hc {} N {}", i, j, holeCount, N);
            Preconditions.checkState(j >= i);
            bool[] gondolas = new bool[j - i + 1];

            for(int idx = 0; idx < gondolas.Length; ++idx)
            {
                gondolas[idx] = a[i + idx] ;
            }

            int totalValue = 0;

            int denom = 0;
            foreach(List<int> perm in Combinations.nextPermutationWithRepetition(holeCount,gondolas.Length))
            {
                int value = simulateForWheelFast(gondolas, perm, N);
                Logger.LogTrace("Perm {} value {}", perm.ToCommaString(), value);

                if (value < 0)
                    continue;

                totalValue += value;
                ++denom;
            }

            Logger.LogDebug("E({}, {}) denom {}", i, j, denom);
            return new Fraction(totalValue,denom);
        }

        public static int simulateForWheelFast(bool[] gondolas, IList<int> permOrder, int N = 300)
        {
            //Defensive copy
            bool[] sim = new bool[gondolas.Length];
            Array.Copy(gondolas, sim, gondolas.Length);

            
            //Filling in false values
            Preconditions.checkState(sim.Count((b) => !b) == permOrder.Count);

            int valueTotal = 0;

            //Go through each one but the last
            for (int idx = 0; idx < permOrder.Count; ++idx)
            {
                int chosenPos = permOrder[idx];

                while (chosenPos < sim.Length && sim[chosenPos])
                    ++chosenPos;

                //Cannot fall off to the right, must stay contained
                if (chosenPos == sim.Length)
                {
                    return -1;
                }
                sim[chosenPos] = true;

                valueTotal += N - (chosenPos - permOrder[idx]);
            }

            return valueTotal;
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

        //Returns true if the last index is filled in last
        /// <summary>
        /// 
        /// </summary>
        /// <param name="gondolas"></param>
        /// <param name="permOrder"></param>
        /// <param name="secondToLastToFill"></param>
        /// <returns>Value given permutation. 0 if conditions that gondolas[last] must be filled last and optionally secondToLast </returns>
        public static int simulatePermutationExpectedValue(bool[] gondolas, IList<int> permOrder,
            int secondToLastToFill, int N)
        {
            //Defensive copy
            bool[] sim = new bool[gondolas.Length];
            Array.Copy(gondolas, sim, gondolas.Length);

            //end must be empty
            Preconditions.checkState(!sim.GetLastValue());
            //Filling in false values
            //Preconditions.checkState(sim.Count((b) => !b) == permOrder.Count);

            int valueTotal = 0;

            //Go through each one but the last
            for (int idx = 0; idx < permOrder.Count; ++idx)
            {
                int chosenPos = permOrder[idx];

                while (chosenPos < sim.Length - 1 && sim[chosenPos])
                    ++chosenPos;

                if (chosenPos == sim.Length - 1)
                {
                    return -1;
                }

                sim[chosenPos] = true;

                valueTotal += N - (chosenPos - permOrder[idx]);

                //Enforce 2nd to last
                if (idx == permOrder.Count - 1 && chosenPos != secondToLastToFill)
                {
                    return -1;
                }
            }



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
        public static BruteForceFracClass P_bruteForce(bool[] gondalas, int i, int j)
        {
            Preconditions.checkState(gondalas[j] == false);

           // ModdedLong pos = new ModdedLong(i, gondalas.Length);
           // ModdedLong stop = new ModdedLong(j, gondalas.Length);

            bool[] ij = Wheel.copyArray(gondalas, i, j);
            //Otherwise too slow
            Preconditions.checkState(ij.Length <= 7);

            int holeCount = ij.Count((b) => !b);

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

            return new BruteForceFracClass(numerator, denominator);
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
        public static BruteForceFracClass P_bruteForce(bool[] gondalas, int i, int j, int k)
        {
            Preconditions.checkState(gondalas[j] == false);

            Logger.LogDebug("P_bruteForce {} [{} to {}] k={}", gondalas.ToCommaString(), i, j, k);

            bool[] ij = Wheel.copyArray(gondalas, i, j);
            //Otherwise too slow
            Preconditions.checkState(ij.Length <= 8);

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
                Logger.LogTrace("Perm {} for ij {}.  ", perm.ToCommaString(), ij.ToCommaString());

                if (0 != simulatePermutation(ij, perm, k))
                {
                    Logger.LogTrace("Perm {} for ij {}.  mid filled 2nd to last {}", perm.ToCommaString(), ij.ToCommaString(), mid.Value);
                    ++numerator;
                }
                ++denominator;
            }

            return new BruteForceFracClass(numerator, denominator);
        }
    }
}
