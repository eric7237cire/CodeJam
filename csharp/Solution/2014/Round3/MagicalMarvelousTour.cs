//#define LOGGING_DEBUG
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

            //running sums is sum given index elements
            List<long> runningSums = new List<long>();
            runningSums.Add(0);

            for (int n = 0; n < input.N; ++n)
            {
                values.Add((int)((1L * n * input.p + input.q) % input.r + input.s));

                runningSums.Add(runningSums[n] + values[n]);
            }


            


            long minZ = 0;
            long maxZ = runningSums.GetLastValue();

            while(minZ < maxZ)
            {
                long z = minZ + (maxZ-minZ) / 2;

                int i = findAtMost(runningSums, 0, input.N , z);

                int j = findAtMost(runningSums, i, input.N , z + runningSums[i]);

                Preconditions.checkState(runningSums[i] <= z);
                Preconditions.checkState(i == input.N  || runningSums[i+1] > z);

                Preconditions.checkState(sumInc(runningSums, i+1, j) <= z);
                Preconditions.checkState(j == input.N  || sumInc(runningSums, i + 1, j + 1) > z);
                
                Preconditions.checkState(j >= i);

                long rightSectionSum = sumInc(runningSums, j + 1, input.N );

                if (rightSectionSum > z)
                {
                    minZ = z + 1;
                }
                else
                {
                    maxZ = z;
                }
            }

            Preconditions.checkState(minZ == maxZ);

            return (1d * (runningSums.GetLastValue() - minZ) / runningSums.GetLastValue()).ToString(CultureInfo.GetCultureInfo("en-US"));
        }

        public static long sumInc(List<long> runningSums, int left, int right) 
        {
            if (right >= runningSums.Count)
                return 0;

            if (right < 0)
                return 0;

            return runningSums[right] - (left > 0 ? runningSums[left - 1] : 0);
        }


        public static int findAtMost(List<long> runningSums, int minToTake, int maxToTake, long z)
        {
            while(minToTake < maxToTake)
            {
                //Check invariant
                Preconditions.checkState(runningSums[minToTake] <= z);

                //round up
                int index = minToTake + (maxToTake-minToTake+1) / 2;

                if (runningSums[index] > z)
                {
                    //too far, maximum must be before index
                    maxToTake = index - 1; 
                }
                else
                {
                    minToTake = index;
                }
            }

            Preconditions.checkState(minToTake == maxToTake);

            return minToTake;
        }

        public String processInputOrig(MagicalMarvelousTourInput input)
        {
            List<int> values = new List<int>();
            
            for (long n = 0; n < input.N; ++n )
            {
                values.Add( (int) (  (n * input.p + input.q) % input.r + input.s));
            }

            List<long> runningSums = new List<long>();
            runningSums.Add(values[0]);

            for (int n = 1; n < input.N; ++n)
            {
                runningSums.Add(runningSums[n - 1] + values[n]);
            }

            long lowestMax = long.MaxValue;

            Func<int, int, long> sumInc = (__left, __right) =>
                {
                    if (__right >= runningSums.Count)
                        return 0;

                    if (__right < 0)
                        return 0;

                    return runningSums[__right] - ( __left > 0 ? runningSums[__left - 1] : 0);
                };

            for (int start = 0; start < input.N; ++start )
            {
                long leftSum = -1;

                if (start > 0)
                {
                    leftSum = runningSums[start - 1];
                }

                //Binary search for stop
                int minStop = start;
                int maxStop = input.N - 1;

                while(maxStop > minStop)
                {
                    int midPoint = (maxStop + minStop) / 2;

                    long middle = sumInc(start, midPoint);

                    long right = sumInc(midPoint + 1, input.N - 1);

                    Logger.LogDebug("Finding midpoint.  Range [{}-{}] middle {} [{}-{}] right {}  min/max {} {}", 
                        start, midPoint, 
                        middle,
                        midPoint+1, input.N - 1,
                        right, minStop, maxStop);

                    long diff = right - middle;

                    int nextVal = values[midPoint + 1];
                    Logger.LogDebug("Difference {}.  Value mid+1 {}", diff, nextVal);

                    if (right > middle && diff < nextVal)
                    {
                        Logger.LogDebug("Right sum is larger, but if switch mid+1, the right sum will stay less, and |diff| > current difference {} < {}", diff, nextVal);
                        minStop = maxStop = midPoint;
                        break;
                    }
                                      

                    if (middle >= right)
                    {
                        maxStop = midPoint;
                    }
                    else
                    {
                        minStop = midPoint+1;
                    }
                }

                Preconditions.checkState(minStop == maxStop);

                Logger.LogDebug("Binary search yielded range [{}-{}]  Sums {} {} {}", start, minStop,
                    sumInc(0, start - 1), sumInc(start, minStop), sumInc(minStop + 1, input.N - 1));

                long[] sums = new long[] { sumInc(0, start - 1), sumInc(start, minStop), sumInc(minStop + 1, input.N - 1) };
                int stop = minStop;
                long localLow = sums.Max();

               // long oldLowestMax = lowestMax;

                lowestMax = Math.Min(lowestMax, localLow);

                //if (lowestMax != oldLowestMax)
                {
                  //  Logger.LogDebug("Choosing range [{} to {}] with sums {} {} {}", start, stop, leftSum, middleSum, rightSum);
                }
                
            }


            return (1d * (runningSums.GetLastValue() - lowestMax) / runningSums.GetLastValue()).ToString(CultureInfo.GetCultureInfo("en-US"));
        }
    }

}