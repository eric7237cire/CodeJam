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
        public string processInput(CheatersInput input)
        {

            Logger.LogTrace("\nSTART\n");
            int numPossible;

            List<long> bets = new List<long>(input.bets);

            for(int i = input.nBets; i < 37; ++i)
            {
                bets.Add(0);
            }

            bets.Sort();

            Logger.LogDebug("Budget {} other bets {}", input.budget, bets.ToCommaString());

            int position = 0;
            long budget = input.budget;

            double best = 0;

            while(position < 37)
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
                        expectedGain += 1d / count * (betAmount - bets[i]) * 36 ;
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
