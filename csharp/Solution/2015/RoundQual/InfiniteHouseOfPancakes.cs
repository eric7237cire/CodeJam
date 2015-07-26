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

        public int cost(int[] counts)
        {
            int highest = -1;
            for(int m = counts.Length - 1; m >= 0; --m)
            {
                if (counts[m] > 0)
                {
                    highest = m;
                    break;
                }
            }

            Preconditions.checkState(highest >= 0);

            int costDoNothing = highest;


            int costSwitching = int.MaxValue;

            if (highest > 2)
            {
                costSwitching = counts[highest];
                counts[highest / 2] += counts[highest];
                counts[highest - highest / 2] += counts[highest];
                counts[highest] = 0;
                 costSwitching += cost(counts);
            }
            return Math.Min(costDoNothing, costSwitching);

        }
        public int processInput(InfiniteHouseOfPancakesInput input)
        {
            int[] counts = new int[1001];

            foreach(int p in input.P)
            {
                counts[p]++;
            }
            return cost(counts);
        }
    }

}