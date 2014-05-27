
#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE
#endif

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using Logger = Utils.LoggerFile;
using Utils;

namespace Round3_2013.Problem1
{
    public class Cheaters : InputFileConsumer<CheatersInput, string>, InputFileProducer<CheatersInput>
    {
        struct BetInfo
        {
            internal long betAmt;
            internal int count;

            public override string ToString()
            {
                return "Bet: {0} Count: {1}".FormatThis(betAmt, count);
            }
        }
        public string processInput(CheatersInput input)
        {

            Logger.LogTrace("\nSTART\n");
            
            List<long> bets = new List<long>(input.bets);

            for(int i = input.nBets; i < 37; ++i)
            {
                bets.Add(0);
            }

            bets.Sort();

            List<BetInfo> betInfo = new List<BetInfo>();
            
            for (int pos = 0; pos < 37;  )
            {
                int next = bets.FindIndex(pos, (bet) => bet > bets[pos]);
                if (next == -1)
                    next = 37;

                betInfo.Add(new BetInfo { betAmt = bets[pos], count = next - pos });

                pos = next;
            }

            Logger.LogTrace("Budget {} other bets {}\n{}", input.budget, bets.ToCommaString(), betInfo.ToCommaString());

            long budget = input.budget;

            double best = 0;

            //These are the bet values that are chosen to be the lowest
            for (int lowestBetInfoIdx = 0; lowestBetInfoIdx < betInfo.Count; ++lowestBetInfoIdx )
            {
                /*
                 * 3 groupes, 1 - 2 - 3
                 * 
                 * ... are the lowest total bets
                 * 
                 *  ..[.]mm[m][M]MM
                 *  [.] = lowestBetInfoIdx
                 *  [m] = maxDetByIdx - 1
                 *  [M] = maxDetByIdx
                 *  
                 * m may not exist 
                 *  
                 * 
                 * To increase ..[.] must be equal to [m]
                 * mm[m] must be equal to [m+1]
                 */

                //Increase surrounding bets to allow us to allow a higher minimum
                for(int maxBetDeterminedByIdx = lowestBetInfoIdx + 1; maxBetDeterminedByIdx <= betInfo.Count; ++maxBetDeterminedByIdx)
                {
                    long betUpperLimit = (maxBetDeterminedByIdx == betInfo.Count ) ? input.budget : betInfo[maxBetDeterminedByIdx].betAmt - 1;

                    //Cost to increase eveything between the set of possible winning numbers (lowest sum of money bet) and the lowest maximum
                    long baseCost = 0;
                    long grp2Count = 0; // betInfo[maxBetDeterminedByIdx].count;

                    Logger.LogTrace("\nMaximum det by index {} = {}.  ", maxBetDeterminedByIdx, betUpperLimit);

                    long minGrp1 = betInfo[maxBetDeterminedByIdx-1].betAmt;
                    long grp1Count = 0;
                    for (int idx = 0; idx <= lowestBetInfoIdx; ++idx)
                    {
                        Preconditions.checkState(betInfo[idx].betAmt <= minGrp1);
                        baseCost += (minGrp1 - betInfo[idx].betAmt) * betInfo[idx].count;
                        grp1Count += betInfo[idx].count;

                    }

                    long minGrp2 = betInfo[maxBetDeterminedByIdx - 1].betAmt + 1;
                    for (int idx = lowestBetInfoIdx + 1; idx < maxBetDeterminedByIdx; ++idx )
                    {
                        baseCost += ( minGrp2 - betInfo[idx].betAmt) * betInfo[idx].count;
                        grp2Count += betInfo[idx].count;
                        Logger.LogTrace("Looping idx {} {} {}", idx, betInfo[maxBetDeterminedByIdx - 1].betAmt, betInfo[idx]);
                    }

                    Logger.LogTrace("base cost {} to make bet at least {} count {}", baseCost, betInfo[maxBetDeterminedByIdx - 1].betAmt, grp2Count);

                    if (baseCost > budget)
                    {
                        Logger.LogTrace("Stopping at maxDetBy {}  baseToIncMax {}", maxBetDeterminedByIdx, baseCost);
                        break;
                    }

                    long maxBetWithIncMax = minGrp1 + (budget - baseCost) / (grp2Count + grp1Count);

                    Logger.LogTrace("max bet {}", maxBetWithIncMax);

                    long realBetAmt = Math.Min(betUpperLimit, maxBetWithIncMax);

                    double expectedGain = betInfo.TakeWhile((bi, idx) => { return idx <= lowestBetInfoIdx; })
                    .Aggregate(0d, (curCost, bi) => { return curCost + 1d / grp1Count * 36 * bi.count * (realBetAmt - bi.betAmt); });

                    long actualCost = baseCost + (realBetAmt - minGrp1) * (grp2Count + grp1Count);

                    Logger.LogTrace("Expected gain {} actual cost {} real bet amount {}", expectedGain, actualCost, realBetAmt);

                    expectedGain -= actualCost;

                    best = Math.Max(best, expectedGain);
                    Logger.LogTrace("Best is now {}", best);
                }
            }
           
            Logger.LogDebug("Best {}", best);
            return "" + best.ToString("0.#######", new CultureInfo("en-US"));
        }

        public CheatersInput createInput(Scanner scanner)
        {
            CheatersInput input = new CheatersInput();
            input.budget = scanner.nextLong();
            input.nBets = scanner.nextInt();

            input.bets = new long[input.nBets];
            for(int i = 0; i < input.nBets; ++i)
            {
                input.bets[i] = scanner.nextLong();
            }

            return input;
        }
    }

    public class CheatersInput
    {
        internal long budget { get; set; }
        internal int nBets { get; set; }

        internal long[] bets;

    }
}
