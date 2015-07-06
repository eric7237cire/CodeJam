using NUnit.Framework;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Logger = Utils.LoggerFile;

namespace CodeJam.Utils.graph
{
    public class MaxFlowPreflowN3
    {

        int[][] cap;

        public void init(int nodes)
        {
            cap = new int[nodes][];
            for (int i = 0; i < nodes; ++i)
            {
                cap[i] = new int[nodes];
            }
        }

        public void addEdge(int s, int t, int capacity)
        {
            cap[s][t] = capacity;
        }
        public void addBidirectionsalEdge(int s, int t, int capacity)
        {
            addEdge(s, t, capacity);
            addEdge(t, s, capacity);
        }

        public int maxFlow(int s, int t)
        {
            int n = cap.Length;

            int[] h = new int[n];
            h[s] = n - 1;

            int[] maxh = new int[n];

            int[][] f = new int[n][];
            for (int initarr = 0; initarr < n; ++initarr)
            {
                f[initarr] = new int[n];
            }

            int[] e = new int[n];

            for (int i = 0; i < n; ++i)
            {
                f[s][i] = cap[s][i];
                f[i][s] = -f[s][i];
                e[i] = cap[s][i];
            }

            for (int sz = 0; ; )
            {
                if (sz == 0)
                {
                    for (int i = 0; i < n; ++i)
                        if (i != s && i != t && e[i] > 0)
                        {
                            if (sz != 0 && h[i] > h[maxh[0]])
                                sz = 0;
                            maxh[sz++] = i;
                        }
                }
                if (sz == 0)
                    break;
                while (sz != 0)
                {
                    int i = maxh[sz - 1];
                    bool pushed = false;
                    for (int j = 0; j < n && e[i] != 0; ++j)
                    {
                        if (h[i] == h[j] + 1 && cap[i][j] - f[i][j] > 0)
                        {
                            int df = Math.Min(cap[i][j] - f[i][j], e[i]);
                            f[i][j] += df;
                            f[j][i] -= df;
                            e[i] -= df;
                            e[j] += df;
                            if (e[i] == 0)
                                --sz;
                            pushed = true;
                        }
                    }
                    if (!pushed)
                    {
                        h[i] = int.MaxValue;
                        for (int j = 0; j < n; ++j)
                            if (h[i] > h[j] + 1 && cap[i][j] - f[i][j] > 0)
                                h[i] = h[j] + 1;
                        if (h[i] > h[maxh[0]])
                        {
                            sz = 0;
                            break;
                        }
                    }
                }
            }

            int flow = 0;
            for (int i = 0; i < n; i++)
                flow += f[s][i];

            return flow;
        }

        [TestFixture]
        public class Tests
        {
            // Usage example
            [Test]
            public void TestUsage()
            {
                int[][] capacity = new int[3][];
                capacity[0] = new int[] { 0, 3, 2 };
                capacity[1] = new int[] { 0, 0, 2 };
                capacity[2] = new int[] { 0, 0, 0 };
                int n = capacity.Length;
                MaxFlowPreflowN3 flow = new MaxFlowPreflowN3();
                flow.init(n);
                for (int i = 0; i < n; i++)
                    for (int j = 0; j < n; j++)
                        if (capacity[i][j] != 0)
                            flow.addEdge(i, j, capacity[i][j]);
                Assert.AreEqual(5, flow.maxFlow(0, 2));
            }
        }
    }

}
