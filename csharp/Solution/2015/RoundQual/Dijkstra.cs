#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJam.Utils.math;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;
using Logger = Utils.LoggerFile;



namespace Year2015.RoundQual.Problem3
{

    public class DijkstraInput
    {

        public int L;
        public int X;

        public String S;

        public int[][] mult;
        public Dictionary<char, int> d = new Dictionary<char, int>();

    }

    public class Dijkstra : InputFileProducer<DijkstraInput>, InputFileConsumer<DijkstraInput, String>
    {
        public DijkstraInput createInput(Scanner scanner)
        {
            //scanner.enablePlayback();
            DijkstraInput input = new DijkstraInput();

            input.L = scanner.nextInt();
            input.X = scanner.nextInt();

            input.S = scanner.nextWord();

            input.mult = new int[4][];
            
            //1 2 3 4

            input.mult[0] = new int[] { 1, 2, 3, 4 };
            input.mult[1] = new int[] { 2, -1, 4, -3 };
            input.mult[2] = new int[] { 3, -4, -1, 2 };
            input.mult[3] = new int[] { 4, 3, -2, -1 };

            input.d['i'] = 2;
            input.d['j'] = 3;
            input.d['k'] = 4;

            //Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }

        public String processInput(DijkstraInput input)
        {
             

            //build x matrix

            String s = "i";

            int[] one = getResults(s, input);
            Logger.LogDebug("Starting {}", String.Join(", ", one));
            int[] last = one;
            for (int i = 0; i < 20; ++i )
            {
                int[] next = getResults(last, one, input);
                Logger.LogDebug("Starting {}", String.Join(", ", next));
                last = next;
            }


                return "-1";
        }

        public int[] getResults(int[] inputs, int[] xForm, DijkstraInput input)
        {
            int[] ret = new int[4];
            for (int i=0; i < 4 ; ++i)
            {
                bool n = inputs[i] < 0;
                ret[i] = xForm[Math.Abs(inputs[i]) - 1];
                if (n)
                    ret[i] *= -1;
            }
            return ret;
        }

        public int[] getResults(String s, DijkstraInput input)
        {
            int[] ret = new int[4];
            for (int startVal = 1; startVal <= 4; ++startVal)
            {
                ret[startVal-1] = multL(startVal, s, 0, s.Length - 1, input);
            }
            return ret;
        }

        public int multL(int startVal, String s, 
            int startIndex,
            int endIndex,
            DijkstraInput input)
        {

            int cur = startVal;

            for (int sIdx = startIndex; sIdx <= endIndex; ++sIdx)
            {
                int next = input.d[s[sIdx]];
                bool neg = cur < 0 ? true : false;
                cur = Math.Abs(cur);

                int nextVal = input.mult[cur-1][next-1];
                if (neg)
                    nextVal *= -1;

                cur = nextVal;
            }

            return cur;
        }
    }

}