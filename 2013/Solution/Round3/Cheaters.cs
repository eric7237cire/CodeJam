#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE

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

namespace Round3
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

            Logger.LogDebug("Budget {} other bets {}\n{}", input.budget, bets.ToCommaString(), betInfo.ToCommaString());

            long budget = input.budget;

            double best = 0;

            
            //These are the bet values that are chosen to be the lowest
            for (int lowestBetInfoIdx = 0; lowestBetInfoIdx < betInfo.Count; ++lowestBetInfoIdx )
            {
                Logger.LogTrace("\n\nMake indexes 0 to {} the least total money bets ", lowestBetInfoIdx);

                
                long baseCost = betInfo.TakeWhile((bi, idx) => { return idx < lowestBetInfoIdx; })
                    .Aggregate((long)0, (curCost, bi) => { return curCost + bi.count * (betInfo[lowestBetInfoIdx].betAmt - bi.betAmt); });

                if (baseCost > budget)
                {
                    Logger.LogTrace("Stopping at index {}.  baseCost {} budget {}", lowestBetInfoIdx, baseCost, budget);
                    break;
                }

                Logger.LogTrace("Base cost to make all previous spots at least as much as the current {}.  Bet must be at least {}", baseCost, betInfo[lowestBetInfoIdx].betAmt);
   
                //Increase surrounding bets to allow us to allow a higher minimum
                for(int maxBetDeterminedByIdx = lowestBetInfoIdx + 1; maxBetDeterminedByIdx <= betInfo.Count; ++maxBetDeterminedByIdx)
                {
                    long betUpperLimit = (maxBetDeterminedByIdx == betInfo.Count ) ? input.budget : betInfo[maxBetDeterminedByIdx].betAmt - 1;

                    //Cost to increase eveything between the set of possible winning numbers (lowest sum of money bet) and the lowest maximum
                    long baseCostToIncreaseMaximum = 0;
                    long grp2Count = 0; // betInfo[maxBetDeterminedByIdx].count;

                    Logger.LogTrace("\nMaximum det by index {} = {}.  ", maxBetDeterminedByIdx, betUpperLimit);

                    long minGrp1 = betInfo[maxBetDeterminedByIdx-1].betAmt;
                    long grp1Count = 0;
                    for (int idx = 0; idx <= lowestBetInfoIdx; ++idx)
                    {
                        Preconditions.checkState(betInfo[idx].betAmt <= minGrp1);
                        baseCostToIncreaseMaximum += (minGrp1 - betInfo[idx].betAmt) * betInfo[idx].count;
                        grp1Count += betInfo[idx].count;

                    }

                    long minGrp2 = betInfo[maxBetDeterminedByIdx - 1].betAmt + 1;
                    for (int idx = lowestBetInfoIdx + 1; idx < maxBetDeterminedByIdx; ++idx )
                    {
                        baseCostToIncreaseMaximum += ( minGrp2 - betInfo[idx].betAmt) * betInfo[idx].count;
                        grp2Count += betInfo[idx].count;
                        Logger.LogTrace("Looping idx {} {} {}", idx, betInfo[maxBetDeterminedByIdx - 1].betAmt, betInfo[idx]);
                    }

                    /*
                     * 3 groupes, 1 - 2 - 3
                     *  ..[.]mm[m][M]MM
                     *  [.] = lowestBetInfoIdx
                     *  [m] = maxDetByIdx - 1
                     *  [M] = maxDetByIdx
                     *  
                     * m may not exist 
                     *  
                     * baseCost assures that all . are at least [.]
                     * 
                     * To increase ..[.] must be equal to [m]
                     * mm[m] must be equal to [m+1]
                     */

                    Logger.LogTrace("base cost {} to make bet at least {} count {}", baseCostToIncreaseMaximum, betInfo[maxBetDeterminedByIdx - 1].betAmt, grp2Count);

                    if (baseCostToIncreaseMaximum > budget)
                    {
                        Logger.LogTrace("Stopping at maxDetBy {}  baseToIncMax {}", maxBetDeterminedByIdx, baseCostToIncreaseMaximum);
                        break;
                    }

                    long budgetLeftForInc = budget - baseCostToIncreaseMaximum;

                    //cost increasing up to idx to a bet + 1

                    long maxBetWithIncMax = minGrp1 + (budgetLeftForInc) / (grp2Count + grp1Count);

                    Logger.LogTrace("max bet {}", maxBetWithIncMax);

                    long realBetAmt = Math.Min(betUpperLimit, maxBetWithIncMax);

                    double expectedGain = betInfo.TakeWhile((bi, idx) => { return idx <= lowestBetInfoIdx; })
                    .Aggregate(0d, (curCost, bi) => { return curCost + 1d / grp1Count * 36 * bi.count * (realBetAmt - bi.betAmt); });

                    long actualCost = baseCostToIncreaseMaximum + (realBetAmt - minGrp1) * (grp2Count + grp1Count);

                    Logger.LogTrace("Expected gain {} actual cost {} real bet amount {}", expectedGain, actualCost, realBetAmt);

                    expectedGain -= actualCost;

                    best = Math.Max(best, expectedGain);
                    Logger.LogTrace("Best is now {}", best);
                }
            }
            #region old
            /*
                while (position < 37)
                {
                    long curBetAmount = bets[position];
                    int nextPosition = bets.FindIndex(position + 1, (bet) => bet > curBetAmount);
                    long nextBetAmount = bets[nextPosition];

                    int count = nextPosition;
                    //First put as much in the current value without going past the bet amount in the next position
                    long betAmount = Math.Min(nextBetAmount - 1, curBetAmount + budget / count);

                    budget -= (betAmount - curBetAmount) * count;
                    double expectedGain = 0;

                    if (betAmount > curBetAmount)
                    {
                        for (int i = 0; i < nextPosition; ++i)
                        {
                            expectedGain += 1d / count * (betAmount - bets[i]) * 36;
                        }
                        expectedGain -= (input.budget - budget);
                        best = Math.Max(expectedGain, best);
                    }
                    Logger.LogTrace("position {} curBet {} betAmount {} count {} budget {} expected gain {}", position,
                        curBetAmount, betAmount, count, budget, expectedGain);

                    //Now to go to the next step, we have to fill in all the current positions up to the next
                    budget -= count * (nextBetAmount - betAmount);

                    if (budget < 0)
                        break;

                    long countAtNextBetAmount = bets.Count((bet) => bet == nextBetAmount);

                    long maxNumToBetOnNextAmt = Math.Min(countAtNextBetAmount, budget);

                    for (long numToBetOnNextAmt = 0; numToBetOnNextAmt <= maxNumToBetOnNextAmt; ++numToBetOnNextAmt)
                    {
                        expectedGain = 0;

                        for (int i = 0; i < nextPosition; ++i)
                        {
                            expectedGain += 1d / (count + countAtNextBetAmount - numToBetOnNextAmt) * (nextBetAmount - bets[i]) * 36;
                            //Logger.LogTrace(" 1 / {} * {} * 36", count + adjCountAtNextBetAmount, (nextBetAmount - bets[i]));
                        }
                        Logger.LogTrace("Next pos {}  count @ next {} elim  {} expected gain before cost {} budget {}", nextPosition, countAtNextBetAmount, numToBetOnNextAmt, expectedGain, budget);
                        expectedGain -= (input.budget - budget);
                        expectedGain -= numToBetOnNextAmt;
                        best = Math.Max(expectedGain, best);
                    }
                    Logger.LogTrace("Filling up count at next {} expected gain {}  {}", countAtNextBetAmount, expectedGain, (input.budget - budget));



                    position = nextPosition;
                }
            */
            #endregion
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
