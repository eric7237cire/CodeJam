#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE
#endif

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.tree;
using Logger = Utils.LoggerFile;

namespace RoundFinal
{
    public class Story : InputFileProducer<StoryInput>, InputFileConsumer<StoryInput, int>
    {
        public StoryInput createInput(Scanner scanner)
        {
            StoryInput input = new StoryInput();
            input.N = scanner.nextInt();

            input.salaries = new List<int>();
            for (int i = 0; i < input.N; ++i)
            {
                input.salaries.Add(scanner.nextInt());
            }

            return input;
        }

        const int mod = 10007;

        public int processInput(StoryInput input)
        {
            int[][] dp;
            int[] values;
            transformInput(input, out values);

            Story.dynProg(values, out dp);

            int[] modFact = ModdedLong.generateModFactorial(values.Length + 1, mod);

            int N = values.Length;

            int[] totalNonDecSubSeqOfLength = new int[values.Length + 1];
            for (int len = 1; len <= N; ++len)
            {
                totalNonDecSubSeqOfLength[len] = dp[len].Sum();
                totalNonDecSubSeqOfLength[len] %= mod;
            }

            int ans = 0;

            if (totalNonDecSubSeqOfLength[N] == 1)
            {
                return 1;
            }

            Preconditions.checkState(totalNonDecSubSeqOfLength[N] == 0);

            for (int K = 1; K <= N; ++K)
            {
                Logger.LogTrace("K is {}", K);
                int numNonDecLenK = totalNonDecSubSeqOfLength[K];
                int totalWaysPickSubSeqLenK = modFact[N - K];
                int total = numNonDecLenK * totalWaysPickSubSeqLenK;
                Logger.LogTrace("Total {} * {} = {}", numNonDecLenK, totalWaysPickSubSeqLenK, total);
                total %= mod;

                if (K < N)
                {
                    //But we must discount how many ways we could have made a non decreasing subsequence of length K + 1
                    int numNonDecLenKp1 = totalNonDecSubSeqOfLength[K + 1];
                    int totalWaysPickSubSeqLenKp1 = modFact[N - (K + 1)];

                    total -= (int) ( ( (long)numNonDecLenKp1 * totalWaysPickSubSeqLenKp1 * (K + 1)) % mod );

                    Logger.LogTrace("Total -= {} * {} * {} = {}", numNonDecLenKp1, totalWaysPickSubSeqLenKp1, (K + 1), total);

                    if (total < 0)
                        total += mod;
                }

                ans += total;
                ans %= mod;
            }

            return ans;
        }

        public int countNonIncSubsequences()
        {
            return 3;
        }

        

        public static int countNonIncSubsequencesBruteForce(IList<int> values, out int[][] dp)
        {
            Ext.createArray(out dp, values.Count + 1, values.Count, 0);

            int sum = 0;
            for (int combin = (1 << values.Count) - 1; combin > 0; --combin)
            {
                List<int> subSeq = new List<int>();

                bool ok = true;
                for (int i = 0; i < values.Count; ++i)
                {
                    if (combin.GetBit(i))
                    {
                        subSeq.Add(values[i]);
                        if (subSeq.Count > 1 && subSeq[subSeq.Count - 2] < subSeq[subSeq.Count - 1])
                        {
                            ok = false;
                            break;
                        }
                    }
                }

                if (ok)
                {
                    Logger.LogTrace("Counting sub sequence {}", subSeq.ToCommaString());
                    ++sum;

                    dp[subSeq.Count][subSeq.GetLastValue()]++;
                }
            }

            return sum;
        }

        //what is the number of non-increasing subsequences of length P that end with number Q? 
        public static void dynProg(int[] values, out int[][] dp)
        {
            Ext.createArray(out dp, values.Length + 1, values.Length, 0);

            FenwickTree[] ftrees = new FenwickTree[values.Length + 1];
            for (int i = 0; i < ftrees.Length; ++i)
            {
                ftrees[i] = new FenwickTree(values.Length);
            }


            for (int idx = 0; idx < values.Length; ++idx)
            {
                int value = values[idx];

                dp[1][value] = 1;
                ftrees[1].AdjustIndexBy(value+1, 1, mod);

                Logger.LogTrace("Summing idx {} value {}", idx, value);

                for (int len = 2; len <= idx + 1; ++len)
                {
                    //take sum of all previous lengths for all other elements
                    int sum = 0;

                    /*
                    for (int prevElem = value + 1; prevElem < values.Length; ++prevElem)
                    {
                        sum += dp[len - 1][prevElem];
                        sum %= mod;
                    }*/
                    if (value + 2 <= values.Length)
                        sum = ftrees[len - 1].SumFromTo(value + 2, values.Length, mod);

                    Logger.LogTrace("Len {} Elem {} = {}", len, value, sum);
                    dp[len][value] = sum;
                    ftrees[len].AdjustIndexBy(value+1, sum, mod);
                }
            }


        }

        /// <summary>
        /// Create array from 0 to N - 1
        /// </summary>
        /// <param name="input"></param>
        /// <param name="newValues"></param>
        public static void transformInput(StoryInput input, out int[] normalizedValues)
        {
            List<int> order = new List<int>();
            for (int i = 0; i < input.N; ++i)
            {
                order.Add(i);
            }

            order.Sort((lhs, rhs) =>
            {
                //Logger.LogTrace("Compare {} and {}", lhs, rhs);
                int cmp = input.salaries[lhs].CompareTo(input.salaries[rhs]);

                if (cmp != 0)
                {
                    //Logger.LogTrace("Return {} ", cmp);
                    return cmp;
                }

                //Logger.LogTrace("Return2: {} ", -lhs.CompareTo(rhs));
                return -lhs.CompareTo(rhs);
            });

            normalizedValues = new int[order.Count];

            for (int i = 0; i < order.Count; ++i)
            {
                normalizedValues[order[i]] = i;
            }
        }
    }

    public class StoryInput
    {
        public int N;
        public List<int> salaries;
    }
}
