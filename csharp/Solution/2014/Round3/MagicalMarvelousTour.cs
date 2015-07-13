#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJam.Utils.math;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;
using Logger = Utils.LoggerFile;



namespace Year2014.Round3.Problem1
{

    public class MagicalMarvelousTourInput
    {
        public int N;

        public int p;
        public int q;
        public int r;
        public int s;

    }

    public class MagicalMarvelousTour : InputFileProducer<MagicalMarvelousTourInput>, InputFileConsumer<MagicalMarvelousTourInput, String>
    {
        public MagicalMarvelousTourInput createInput(Scanner scanner)
        {
            MagicalMarvelousTourInput input = new MagicalMarvelousTourInput();

            
            input.N = scanner.nextInt();

            input.p = scanner.nextInt();
            input.q = scanner.nextInt();
            input.r = scanner.nextInt();
            input.s = scanner.nextInt();

            //Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }

        public String processInput(MagicalMarvelousTourInput input)
        {
            List<int> values = new List<int>();
            

            for (long n = 0; n < input.N; ++n )
            {
                values.Add( (int) (  (n * input.p + input.q) % input.r + input.s));
            }

            List<int> runningSums = new List<int>();
            runningSums.Add(values[0]);

            for (int n = 1; n < input.N; ++n)
            {
                runningSums.Add(runningSums[n - 1] + values[n]);
            }

            int lowestMax = int.MaxValue;

            for (int start = 0; start < input.N; ++start )
            {
                for(int stop = start; stop < input.N; ++stop)
                {
                    int leftSum = -1;

                    if (start > 0)
                    {
                        leftSum = runningSums[start-1];
                    }

                    int rightSum = -1;

                    if (stop < input.N - 1)
                    {
                        rightSum = runningSums[input.N - 1] - runningSums[stop];
                    }

                    int middleSum = runningSums[stop] - ( start > 0 ? runningSums[start - 1] : 0);

                    int max = Math.Max(Math.Max(leftSum, rightSum), middleSum);

                    int oldLowestMax = lowestMax;

                    lowestMax = Math.Min(lowestMax, max);

                    if (lowestMax != oldLowestMax)
                    {
                        Logger.LogDebug("Choosing range [{} to {}] with sums {} {} {}", start, stop, leftSum, middleSum, rightSum);
                    }
                }
            }


            return (1d * (runningSums.GetLastValue() - lowestMax) / runningSums.GetLastValue()).ToString(CultureInfo.GetCultureInfo("en-US"));
        }
    }

}