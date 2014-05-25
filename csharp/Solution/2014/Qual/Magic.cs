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

namespace CodeJam.RoundQual_2014
{
    public class MagicInput
    {
        public int N;
        public int[] perm;
    }
    public class Magic : InputFileProducer<MagicInput>, InputFileConsumer<MagicInput, string>
    {
        public MagicInput createInput(Scanner scanner)
        {
            MagicInput input = new MagicInput();
            input.N = scanner.nextInt();
            input.perm = new int[input.N];

            for (int i = 0; i < input.N; ++i)
            {
                input.perm[i] = scanner.nextInt();
            }

            return input;
        }

        public string processInput(MagicInput input)
        {
            return "buh";
        }
    }
}