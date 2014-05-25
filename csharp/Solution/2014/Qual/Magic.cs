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
        public int firstRow;
        public int[][] first;
        public int secondRow;
        public int[][] second;
    }
    public class Magic : InputFileProducer<MagicInput>, InputFileConsumer<MagicInput, string>
    {
        public MagicInput createInput(Scanner scanner)
        {
            MagicInput input = new MagicInput();
            input.firstRow = scanner.nextInt() - 1;

            Ext.createArray(out input.first, 4, 4, 0);
            Ext.createArray(out input.second, 4, 4, 0);

            for (int r = 0; r < 4; ++r)
            {
                for (int c = 0; c < 4; ++c)
                {
                    input.first[r][c] = scanner.nextInt();
                }                
            }

            input.secondRow = scanner.nextInt() - 1;
            for (int r = 0; r < 4; ++r)
            {
                for (int c = 0; c < 4; ++c)
                {
                    input.second[r][c] = scanner.nextInt();
                }
            }
            return input;
        }

        public string processInput(MagicInput input)
        {
            HashSet<int> firstRow = new HashSet<int>();
            for (int i = 0; i < 4; ++i )
            {
                firstRow.Add(input.first[input.firstRow][i]);
            }
            HashSet<int> secondRow = new HashSet<int>();
            for (int i = 0; i < 4; ++i)
            {
                secondRow.Add(input.second[input.secondRow][i]);
            }

            var inter = firstRow.Intersect(secondRow);

            if (inter.Count() == 1)
            {
                return inter.First().ToString();
            } else if (inter.Count() == 0)
            {
                return "Volunteer cheated!";
            }
            return "Bad magician!";
        }
    }
}