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

namespace CodeJam.Round1A_2014
{
    public class ChaosInput
    {
        public int N;
        public int L;

        public List<string> initialFlow;
        public List<string> targetFlow;
    }

    public class Chaos : InputFileProducer<ChaosInput>, InputFileConsumer<ChaosInput, string>
    {
        public ChaosInput createInput(Scanner scanner)
        {
            ChaosInput input = new ChaosInput();
            input.N = scanner.nextInt();
            input.L = scanner.nextInt();

            input.initialFlow = new List<string>();
            input.targetFlow = new List<string>();
            for (int i = 0; i < input.N; ++i)
            {
                input.initialFlow.Add(scanner.nextWord());
            }
            for (int i = 0; i < input.N; ++i)
            {
                input.targetFlow.Add(scanner.nextWord());
            }
            return input;
        }

        public static byte BitCount(ulong value)
        {
            ulong result = value - ((value >> 1) & 0x5555555555555555UL);
            result = (result & 0x3333333333333333UL) + ((result >> 2) & 0x3333333333333333UL);
            return (byte)(unchecked(((result + (result >> 4)) & 0xF0F0F0F0F0F0F0FUL) * 0x101010101010101UL) >> 56);
        }

        static ulong FromString(string binaryStr)
        {
            ulong ret = 0;
            for(int c = 0; c < binaryStr.Length; ++c)
            {
                if (binaryStr[c] == '0')
                    continue;

                ret |= 1UL << c;
            }
            return ret;
        }

        public string processInput(ChaosInput input)
        {
            //Get inputs into binary
            List<ulong> initial = new List<ulong>(input.initialFlow.Select((str) => FromString(str)));
            List<ulong> target = new List<ulong>(input.targetFlow.Select((str) => FromString(str)));

            Dictionary<ulong, int> counts = new Dictionary<ulong, int>();
            int bestAns = Int32.MaxValue;

            for(int i = 0; i < initial.Count; ++i)
            {
                for(int j = 0; j < target.Count; ++j)
                {
                    ulong switchesNeeded = initial[i] ^ target[j];
                    int count;
                    counts.TryGetValue(switchesNeeded, out count);

                    counts[switchesNeeded] = count + 1;

                    if (count + 1 == input.N)
                    {
                        int popCount = BitCount(switchesNeeded);
                        bestAns = Math.Min(bestAns, popCount);
                    }
                }
            }

            if (bestAns == Int32.MaxValue)
                return "NOT POSSIBLE";

            return "" + bestAns;
        }
    }
}
