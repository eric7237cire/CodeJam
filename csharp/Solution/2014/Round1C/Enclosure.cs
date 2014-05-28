
#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using CodeJamUtils;
using Utils;
using Utils.tree;
using Logger = Utils.LoggerFile;

using CodeJam.Utils.graph;
using Utils.geom;
using NUnit.Framework;

namespace Round1C_2014.Problem3
{
    public class EnclosureInput
    {
        public short N;
        public short M;
        public short K;
    }

    public class Enclosure : InputFileProducer<EnclosureInput>, InputFileConsumer<EnclosureInput, int>
    {
        public EnclosureInput createInput(Scanner scanner)
        {
            EnclosureInput input = new EnclosureInput();

            input.N = (short) scanner.nextInt();
            input.M = (short) scanner.nextInt();
            input.K = (short) scanner.nextInt();
            return input;
        }

        public int processInput(EnclosureInput input)
        {
            if (input.N < input.M) //  # If the row size is smaller than the column size
                Ext.swap(ref input.N, ref input.M); //# Transpose the grid

            int res = 1000000;
            short[][][] dp;
            Ext.createArray(out dp, 500, 40, 1000, (short)-1);
            //# Try all possible number of stones for the top row.
            for (short stones = 1; stones <= Math.Min(input.K, input.M); ++stones)
            {
                //# The stones needed to cover the top row + the next rows.
                int _stones = stones + rec(input.N - 1, stones, (short) (input.K - stones), input.M, dp);
                res = Math.Min(res, _stones);
            }

            return res;

        }

        public short rec(int rem_rows, short prev_dist, short rem_points, int M, short[][][] dp)
        {
            if (rem_points <= 0) //  # If the remaining area is non positive,
                return 0; //  # then no stone is needed.

            short ret = short.MaxValue; //  # Infinity.

            if (rem_rows <= 0) //  # No more row but rem_points is still > 0.
                return ret; //  # Return infinity.

            if (M == 1) //  # Special case where each row only has one stone.
                return rem_points; //  #  rem_rows >= rem_points is guaranteed.

            if (dp[rem_rows][prev_dist][rem_points] >= 0)
                return dp[rem_rows][prev_dist][rem_points];
            
            //Limit distance between 1 and M (columns)
            short min_dist = (short) Math.Max(prev_dist - 2, 1);
            short max_dist = (short) Math.Min(prev_dist + 2, M);

            for (short next_dist = min_dist; next_dist <= max_dist; ++next_dist)
            {
                if (next_dist >= rem_points)
                //# Close the enclosure for the last row.
                {
                    ret = Math.Min(ret, next_dist);
                }
                else if (next_dist > 1)
                {
                    //# Cover this row using 2 stones.
                    short next_rem_points = (short) (rem_points - next_dist);
                    ret = (short) Math.Min(ret,
                    2 + rec(rem_rows - 1, next_dist, next_rem_points, M, dp));
                }



            }

            return dp[rem_rows][prev_dist][rem_points] = ret;
        }
    }



    [TestFixture]
    public class EnclosureTest
    {
        [Test]
        public void Test()
        {
            EnclosureInput i = new EnclosureInput();


            Enclosure e = new Enclosure();
            Assert.AreEqual("2", e.processInput(i));
        }
    }
}