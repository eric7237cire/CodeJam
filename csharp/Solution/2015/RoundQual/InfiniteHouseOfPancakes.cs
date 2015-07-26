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



namespace Year2015.RoundQual.Problem2
{

    public class InfiniteHouseOfPancakesInput
    {

        public int D;

        public int[] P;
    }

    public class InfiniteHouseOfPancakes : InputFileProducer<InfiniteHouseOfPancakesInput>, InputFileConsumer<InfiniteHouseOfPancakesInput, int>
    {
        public InfiniteHouseOfPancakesInput createInput(Scanner scanner)
        {
            scanner.enablePlayBack();
            InfiniteHouseOfPancakesInput input = new InfiniteHouseOfPancakesInput();

            input.D = scanner.nextInt();

            Ext.createArrayWithFunc(out input.P, input.D, i => scanner.nextInt());
            
            Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }

        public int cost(int[] p, int currentIndex, int minimum, int[][] memo)
        {
            if (currentIndex >= p.Length)
                return minimum;

            if (memo[currentIndex][minimum] != -1)
            {
                return memo[currentIndex][minimum];
            }
            

            int val = p[currentIndex];
            int costDoNothing = val;

            if (val <= minimum)
                return memo[currentIndex][minimum] = minimum;


            int costSwitching = int.MaxValue;

            if (p[currentIndex] > minimum)
            {
                for (int div = 2; div < 32 && div < val; ++div )
                {
                    int c = div-1;
                    
                    int min = val / div;
                    if (val % div != 0)
                        min ++;

                    c += cost(p, currentIndex+1, Math.Max(minimum, min), memo);

                    costSwitching = Math.Min(costSwitching, c);
                }
                    
            }
            return memo[currentIndex][minimum] = Math.Min(costDoNothing, costSwitching);

        }
        public int processInput(InfiniteHouseOfPancakesInput input)
        {
            int[][] memo;
            Ext.createArray(out memo, input.P.Length, 1001, -1);
            Array.Sort(input.P);
            Array.Reverse(input.P);

            return cost(input.P,0, 1, memo);
        }
    }

}