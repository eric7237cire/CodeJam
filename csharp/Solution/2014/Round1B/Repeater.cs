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
using Utils.math;

namespace Round1B_2014.Problem1
{
    public class RepeaterInput
    {
        public int N;
        public string[] S;
    }

    public class Repeater : InputFileProducer<RepeaterInput>, InputFileConsumer<RepeaterInput, string>
    {
        public RepeaterInput createInput(Scanner scanner)
        {
            RepeaterInput input = new RepeaterInput();
            input.N = scanner.nextInt();
            input.S = new string[input.N];
            for (int i = 0; i < input.N; ++i)
            {
                input.S[i] = scanner.nextWord() + ' ';
            }

            return input;
        }

        public string processInput(RepeaterInput input)
        {
            List<Char> letterGroupes = new List<char>();
            List<int>[] tallies;
            Ext.createArray(out tallies, input.N);

            for (int i = 0; i < input.S.Length; ++i)
            {
                int idx = 0;
                int count = 0;
                char curChar = input.S[i][0];
                int grpNum = 0;
                while (idx < input.S[i].Length)
                {
                    if (input.S[i][idx] == curChar)
                    {
                        ++count;
                    }
                    else
                    {
                        tallies[i].Add(count);

                        if (i == 0)
                        {
                            letterGroupes.Add(curChar);
                        }
                        else
                        {
                            if (grpNum >= letterGroupes.Count || letterGroupes[grpNum] != curChar)
                            {
                                return "Fegla Won";
                            }
                        }
                        count = 1;
                        curChar = input.S[i][idx];
                        ++grpNum;
                    }
                    ++idx;
                }

                if (i > 0 && tallies[i].Count != tallies[0].Count)
                {
                    return "Fegla Won";
                }

            }

            int overallSumDiffs = 0;

            for (int grp = 0; grp < tallies[0].Count; ++grp )
            {
                //It is actually the median that works, not the average.  THink of the trees

                //Need to select same element in each of the strings tally list, then sort to find median.
                int median = tallies.Select((t1) => t1[grp]).OrderBy((e) => e).ElementAt(input.N / 2);

                int totalDiff = tallies.Aggregate(0, 
                    (runningTotal, grpTallyList) => runningTotal + Math.Abs(median - grpTallyList[grp]));
                
                overallSumDiffs += totalDiff;
                Logger.LogTrace("Group {} total diff {}", grp,  totalDiff);
            }

                
            Logger.LogTrace("Letter groupes {} counts {}", letterGroupes.ToCommaString(), tallies.Select((t) => " [" + t.ToCommaString() + "] ").ToCommaString());
            return overallSumDiffs.ToString();
        }
    }

    [TestFixture]
    public class TestRepeater
    {
        [Test]
        public void testRound()
        {
            Fraction f = new Fraction(2, 3);
            long r = f.round();
            Assert.AreEqual(1, r);

            f = new Fraction(450, 300);
            Assert.AreEqual(2, f.round());

            f = new Fraction(449, 300);
            Assert.AreEqual(1, f.round());
        }
    }
}