#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;

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

        public int processInput(StoryInput input)
        {
            return 37;
        }

        public int countNonIncSubsequences()
        {
            return 3;
        }

        public static void createArray<T>( out T[][] array, int d1, int d2, T defValue)
        {
            array = new T[d1][];
            for(int i = 0; i < d1; ++i)
            {
                array[i] = new T[d2];
                for(int j = 0; j < d2; ++j)
                {
                    array[i][j] = defValue;
                }
            }
        }

        public static int countNonIncSubsequencesBruteForce(IList<int> values, out int[][] dp)
        {
            createArray(out dp, values.Count + 1, values.Count, 0);

            int sum = 0;
            for(int combin = (1 << values.Count) - 1; combin > 0; --combin)
            {
                List<int> subSeq = new List<int>();

                bool ok = true;
                for(int i = 0; i < values.Count; ++i)
                {
                    if (combin.GetBit(i))
                    {
                        subSeq.Add(values[i]);
                        if (subSeq.Count > 1 && subSeq[subSeq.Count-2] < subSeq[subSeq.Count-1])
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
            createArray(out dp, values.Length + 1, values.Length, 0);

            for (int idx = 0; idx < values.Length; ++idx )
            {
                int value = values[idx];

                dp[1][value] = 1;
                Logger.LogTrace("Summing idx {} value {}", idx, value);

                for(int len = 2; len <= idx+1; ++len)
                {
                    //take sum of all previous lengths for all other elements
                    int sum = 0;
                    for(int prevElem = value+1; prevElem < values.Length; ++prevElem)
                    {
                        sum += dp[len - 1][prevElem];
                        if (dp[len - 1][prevElem] > 0)
                        {
                            Logger.LogTrace("Adding {} to sum now {}", dp[len - 1][prevElem], sum);
                        }
                    }

                    Logger.LogTrace("Len {} Elem {} = {}", len, value, sum);
                    dp[len][value] = sum;
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
            for(int i = 0; i < input.N; ++i)
            {
                order.Add(i);
            }

            order.Sort((lhs, rhs) => {
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

            for(int i = 0; i < order.Count; ++i)
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
