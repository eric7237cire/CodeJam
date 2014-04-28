#define LOGGING

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;


using Logger = Utils.LoggerFile;

namespace Round2
{
    public class Team
    {
        public int teamNumber { get; internal set;}
        public int winlossRank { get; internal set; }
    }

    public class ManyPrizes : InputFileConsumer<Input, string>
    {


        

        public static long LowRankCanWin(int N, long P)
        {
            //Preconditions.checkState(P > 0);
            if (P == 0)
                return -1;
        
            int matches_won = 0;
            long size_of_group = 1L << N;
            while (size_of_group > P)
            {
                matches_won += 1;
                size_of_group /= 2;
            }

            return (1L << N) - (1L << matches_won);
        }
  

    
        
        public string processInput(Input input)
        {
            //2 ** N - LowRankCanWin(N, 2 ** N - P) - 2

            /*
             *  To find largest that must win, similar to finding largest
             *  that fits in the rest.
             */
            long canGetIntoLosingHalf = LowRankCanWin(input.nRounds, (1L << input.nRounds) - input.nPrizes);

            //Adding 1 means can no longer get into the losing half
            long mustGetIntoWinningHalf = canGetIntoLosingHalf + 1;
            long largestGaraunteed = (1L << input.nRounds) - 1 - mustGetIntoWinningHalf;
            

            long largestCouldWin = LowRankCanWin(input.nRounds, input.nPrizes);

            return "{0} {1}".FormatThis(largestGaraunteed, largestCouldWin);
        }

        
        public static List<Team> getRanks(List<int> posList)
        {
            int rounds = 0;
            int count = posList.Count;
            while( count > 1)
            {
                ++rounds;
                count >>= 1;
            }

            Preconditions.checkState(1 << rounds == posList.Count);

            List<Team> teams = new List<Team>();

            for(int posListIndex = 0; posListIndex < posList.Count; ++posListIndex)
            {
                teams.Add(new Team { teamNumber = posList[posListIndex], winlossRank = 0 });
            }

            for(int round = 0; round < rounds; ++round)
            {
                List<Team> pairings = new List<Team>();
                int taken = 0;
                for (int i = 0; i < posList.Count / 2; ++i)
                {
                    Team unUsedTeam = teams.Find(t => (1 << t.teamNumber & taken) == 0);

                    pairings.Add(unUsedTeam);
                    taken |= 1 << unUsedTeam.teamNumber;

                    Team matchTeam = teams.Find(t => (1 << t.teamNumber & taken) == 0 && t.winlossRank == unUsedTeam.winlossRank);

                    pairings.Add(matchTeam);
                    taken |= 1 << matchTeam.teamNumber;

                    unUsedTeam.winlossRank <<= 1;
                    matchTeam.winlossRank <<= 1;

                    if (unUsedTeam.teamNumber < matchTeam.teamNumber)
                        unUsedTeam.winlossRank += 1;
                    else
                        matchTeam.winlossRank += 1;
                }
            }

            teams.ForEach(t => t.winlossRank = teams.Count - 1 - t.winlossRank);

            teams.Sort((lhs, rhs) => lhs.teamNumber.CompareTo(rhs.teamNumber));
            return teams;

        }

       
    }

    
    public class Input
    {

        internal int nRounds { get; private set; }
        internal long nPrizes { get; private set; }

        
        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.nRounds = scanner.nextInt();
            input.nPrizes = scanner.nextLong();

            
            return input;
        }


    }
}
