using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Logger = Utils.LoggerFile;

namespace CodeJam.Utils.graph
{
    //http://codeforces.com/blog/entry/14378
    //http://web.stanford.edu/~liszt90/acm/notebook.html#file3
    partial class Maxflow2Int32
    {

        int n;
        List<List<EdgeInt32>> adj;

        List<int> dist, count;
        List<bool> active;
        List<List<int>> B;
        int b;
        List<int> Q;

        public Maxflow2Int32(int n)
        {
            this.n = n;
            this.adj = new List<List<EdgeInt32>>();

            for (int i = 0; i < n; ++i)
            {
                adj.Add(new List<EdgeInt32>());
            }

        }

        public void addBidirectionsalEdge(int from, int to, int cap)
        {
            AddEdge(from, to, cap);
            AddEdge(to, from, cap);
        }
        public void AddEdge(int from, int to, int cap)
        {
            
            adj[from].Add(new EdgeInt32(from, to, cap, 0, adj[to].Count));
            if (from == to)
            {
                adj[from][adj.Count - 1].index++;
            }
            adj[to].Add(new EdgeInt32(to, from, 0, 0, adj[from].Count - 1));

        }

        void Enqueue(int v)
        {
            if (!active[v] && excess[v] > 0 && dist[v] < n)
            {
                active[v] = true;
                B[dist[v]].Add(v);
                b = Math.Max(b, dist[v]);
            }
        }

        void Push(EdgeInt32 e)
        {
            var amt = Math.Min(excess[e.from], e.cap - e.flow);
            if (dist[e.from] == dist[e.to] + 1 && amt > 0)
            {
                e.flow += amt;
                adj[e.to][e.index].flow -= amt;
                excess[e.to] += amt;
                excess[e.from] -= amt;
                Enqueue(e.to);
            }
        }

        void Gap(int k)
        {
            for (int v = 0; v < n; v++) if (dist[v] >= k)
                {
                    count[dist[v]]--;
                    dist[v] = Math.Max(dist[v], n);
                    count[dist[v]]++;
                    Enqueue(v);
                }
        }

        void Relabel(int v)
        {
            count[dist[v]]--;
            dist[v] = n;
            foreach (var e in adj[v])
            {
                if (e.cap - e.flow > 0)
                {
                    dist[v] = Math.Min(dist[v], dist[e.to] + 1);
                }
            }
            count[dist[v]]++;
            Enqueue(v);
        }

        void Discharge(int v)
        {
            foreach (var e in adj[v])
            {
                if (excess[v] > 0)
                {
                    Push(e);
                }
                else
                {
                    break;
                }
            }

            if (excess[v] > 0)
            {
                if (count[dist[v]] == 1)
                {
                    Gap(dist[v]);
                }
                else
                {
                    Relabel(v);
                }
            }
        }



        public int GetMaxFlow(int s, int t)
        {
            Ext.initList(out dist, n, 0);
            Ext.initList(out excess, n, 0);
            Ext.initList(out count, n + 1, 0);
            Ext.initList(out active, n, false);

            B = new List<List<int>>();
            for (int i = 0; i < n; ++i)
            {
                //List<int> subList;
                B.Add(new List<int>());
            }
            b = 0;

            foreach (var e in adj[s])
            {
                excess[s] += e.cap;
            }

            count[0] = n;
            Enqueue(s);
            active[t] = true;

            while (b >= 0)
            {
                if (B[b].Count > 0)
                {
                    int v = B[b].GetLastValue();
                    B[b].pop_back();
                    active[v] = false;
                    Discharge(v);
                }
                else
                {
                    b--;
                }
            }
            return excess[t];
        }

        //    T GetMinCut (int s, int t, vector <int> &cut);

    }
}
