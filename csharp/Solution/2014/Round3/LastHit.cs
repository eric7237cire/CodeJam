#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJam.Utils.math;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;
using Logger = Utils.LoggerFile;



namespace Year2014.Round3.Problem2
{

    public class LastHitInput
    {
        public int P;
        public int Q;
        public int N;

        public List<int> H;
        public List<int> G;
    }

    public class LastHit : InputFileProducer<LastHitInput>, InputFileConsumer<LastHitInput, int>
    {
        public LastHitInput createInput(Scanner scanner)
        {
            LastHitInput input = new LastHitInput();

            input.P = scanner.nextInt();
            input.Q = scanner.nextInt();
            input.N = scanner.nextInt();

            input.H = new List<int>();
            input.G = new List<int>();

            for (int i = 0; i < input.N; ++i)
            {
                input.H.Add(scanner.nextInt());
                input.G.Add(scanner.nextInt());
            }

            //Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }

        public int search(LastHitInput input, int depth)
        {
            Logger.LogDebug("{}Search {}", 
                String.Concat(Enumerable.Repeat("    ", depth)), String.Join(", ", input.H));

            if (depth > 1000)
                throw new Exception("uh");

            var remainingHitPoints = input.H;
            if (remainingHitPoints.Sum(h => Math.Max(0, h)) <= 0)
            {
                return 0;
            }

            int maxGold = 0;

            for(int m = 0; m <= remainingHitPoints.Count; ++m)
            {
                int gold = 0;

                if (m < remainingHitPoints.Count)
                {
                    if (remainingHitPoints[m] <= 0)
                    {
                        continue;
                    }

                    //hit it
                    remainingHitPoints[m] -= input.P;

                    if (remainingHitPoints[m] <= 0)
                    {
                        gold += input.G[m];
                    }
                }

                int index = remainingHitPoints.FindIndex(h => h > 0);

                if (index >= 0)
                {
                    remainingHitPoints[index] -= input.Q;
                }
                

                gold += search(input, 1+depth);

                maxGold = Math.Max(gold, maxGold);

                if (index >= 0)
                {
                    remainingHitPoints[index] += input.Q;
                }
                if (m < remainingHitPoints.Count)
                {
                    remainingHitPoints[m] += input.P;
                }
            }

            Logger.LogDebug("{}Search {}  Return {}",
                String.Concat(Enumerable.Repeat("    ", depth)), String.Join(", ", input.H), maxGold);
            return maxGold;
        }

        public int processInputBruteForce(LastHitInput input)
        {
            int maxTurns = input.H.Sum(h => h + input.Q - 1);

            return search(input, 0);
        }

        public int processInput(LastHitInput input)
        {
            //Dynamic program, state being [next][free turns]
            List<int> towerTurnsNeeded = new List<int>(input.H.Select( h => (h + input.Q - 1) / input.Q));

            int maxTowerTurnsRemaining = 1 + towerTurnsNeeded.Sum();

            //If at monster m at full health with t extra turns
            int[][] maxGold;

            Ext.createArray(out maxGold, input.N, maxTowerTurnsRemaining, 0);

            maxTowerTurnsRemaining -= towerTurnsNeeded[input.N - 1];
            
            for(int turns = maxTowerTurnsRemaining; turns >= 0; --turns)
            {
                int hitPoints = input.H[input.N - 1] % input.Q;
                if (hitPoints == 0) {
                    hitPoints += input.Q;
                }

                if (input.P * turns >= hitPoints)
                {
                    maxGold[input.N - 1][turns] = input.G[input.N - 1];
                }
                else
                {
                    maxGold[input.N - 1][turns] = 0;
                }

                Logger.LogDebug("At monster {} with {} free turns. gold = {}",
                           input.N - 1, turns, maxGold[input.N-1][turns]);
            }

            
            

            for (int monsIndex = input.N - 2; monsIndex >= 0; --monsIndex)
            {
                maxTowerTurnsRemaining -= towerTurnsNeeded[monsIndex];
                
                int hitPoints = input.H[monsIndex] % input.Q;
                if (hitPoints == 0) {
                    hitPoints += input.Q;
                }

                int bonusTurnsThisRound = towerTurnsNeeded[monsIndex] - 1;

                for (int turns = maxTowerTurnsRemaining; turns >= 0; --turns)
                {
                    int turnsNeeded = (hitPoints + input.P - 1) / input.P;

                    //int nextRoundBonusTurns = towerTurnsNeeded[monsIndex + 1] - 1;

                    if (turns + bonusTurnsThisRound >= turnsNeeded)
                    {
                        int goldIfKill = input.G[monsIndex] + maxGold[monsIndex + 1][turns - turnsNeeded + bonusTurnsThisRound];
                        Logger.LogDebug("At monster {} with {} free turns. if kill gold= {}  will have {}+{} turns in reserve",
                            monsIndex, turns, goldIfKill, turns - turnsNeeded, bonusTurnsThisRound);
                        maxGold[monsIndex][turns] = Math.Max(maxGold[monsIndex][turns], goldIfKill);
                    }

                    int goldIfSkip = maxGold[monsIndex + 1][turns + 1 + bonusTurnsThisRound];

                    Logger.LogDebug("At monster {} with {} free turns. if not kill gold= {}  will have {}+{} turns in reserve",
                            monsIndex, turns, goldIfSkip, turns + 1, bonusTurnsThisRound);

                    maxGold[monsIndex][turns] = Math.Max(maxGold[monsIndex][turns], goldIfSkip);
                    
                }
            }

            return maxGold[0][1];
        }
    }

}