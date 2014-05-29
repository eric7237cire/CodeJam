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

namespace Round3_2008.Problem1
{
    using ST = Tuple<string, int>;
    using pii = Round3_2008.Problem1.MinMax;
    using pnt = System.Numerics.Complex;
    using System.Numerics;

    class MinMax
    {
        public int min;
        public int max;

        public MinMax()
        {
            min = int.MaxValue;
            max = int.MinValue;
        }

        public void newValue(int val)
        {
            if (val < min)
                min = val;

            if (val > max)
                max = val;
        }
    }

    public class PocketsInput
    {
        public int L;
        public List<Tuple<string,int>> dirsCount;
    }

    public class Pockets : InputFileProducer<PocketsInput>, InputFileConsumer<PocketsInput, int>
    {
        public PocketsInput createInput(Scanner scanner)
        {
            PocketsInput input = new PocketsInput();
            input.L = scanner.nextInt();
            input.dirsCount = new List<ST>();

            for (int i = 0; i < input.L; ++i )
            {
                input.dirsCount.Add(new ST(scanner.nextWord(), scanner.nextInt()));
            }

            return input;
        }

        public int processInput(PocketsInput input)
        {
            int head = 0;
            pnt cur = new pnt(0, 0);

            pnt[] del = new pnt[]
            {
                new pnt(1, 0), //east
                new pnt(0, 1), //north
                new pnt(-1, 0), //west
                new pnt(0, -1) //south
            };

            const int arrayAdj = 3050;
            pii[] row_rng;
            pii[] col_rng;
            Ext.createArrayWithNew(out row_rng, 7000);
            Ext.createArrayWithNew(out col_rng, 7000);

            int area = 0;

            foreach (ST st in input.dirsCount)
            {
                string s = st.Item1;
                int T = st.Item2;
                int len = s.Length;
                

                for (int rep = 0; rep < T; rep++)
                {
                    for (int j = 0; j < len; j++)
                    {
                        switch (s[j])
                        {
                            case 'L':
                                head = (head + 1) & 3;
                                break;
                            case 'R':
                                head = (head - 1) & 3;
                                break;
                            case 'F':
                                
                                    pnt nxt = cur + del[head];
                                    int idx;
                                    switch (head)
                                    {
                                        case 0:
                                        case 2:
                                            //Moving east or west

                                            //Idx is like the line between 2 points, so instead of .5 for
                                            //between 0 and 1, 0 is used
                                            idx = (int) Math.Min(cur.Real, nxt.Real);
                                            Preconditions.checkState(cur.Imaginary == nxt.Imaginary);
                                            col_rng[idx+arrayAdj].newValue((int)cur.Imaginary);                                            
                                            break;
                                        case 1:
                                        case 3:
                                            //Moving north or south
                                            Preconditions.checkState(cur.Real == nxt.Real);
                                            idx = (int) Math.Min(cur.Imaginary, nxt.Imaginary);
                                            row_rng[idx+arrayAdj].newValue((int)cur.Real);
                                            break;
                                    }
                                //This has for effect to add or subtract y if x's are the same or to
                                //add or subtract x if y's are the same;
                                    area += (int) (Complex.Conjugate(cur) * nxt).Imaginary;
                                    cur = nxt;
                                
                                break;
                        }
                    }
                }
            }

            int ans = 0;
            for (int i = -3001; i <= 3001; i++)
                for (int j = -3001; j <= 3001; j++)
                    if ((j >= row_rng[i + arrayAdj].min && j < row_rng[i + arrayAdj].max)
                        || (i >= col_rng[j + arrayAdj].min && i < col_rng[j + arrayAdj].max))
                        ans++;
            ans -= Math.Abs(area / 2);
            return ans;
        }
    }
    [TestFixture]
    public class PocketsTest
    {
        [Test]
        public void Test()
        {
            PocketsInput i = new PocketsInput();
           

            Pockets e = new Pockets();
            Assert.AreEqual("2", e.processInput(i));
        }
    }
}