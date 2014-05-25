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
    public class MineSweeperInput
    {
        public int N;
        public int[] perm;
    }
    public class MineSweeper : InputFileProducer<MineSweeperInput>, InputFileConsumer<MineSweeperInput, string>
    {
        public MineSweeperInput createInput(Scanner scanner)
        {
            MineSweeperInput input = new MineSweeperInput();
            input.N = scanner.nextInt();
            input.perm = new int[input.N];

            for (int i = 0; i < input.N; ++i)
            {
                input.perm[i] = scanner.nextInt();
            }

            return input;
        }

        public string processInput(MineSweeperInput input)
        {
            return "buh";
        }
    }
}