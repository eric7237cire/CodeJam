package codejam.utils.datastructures.graph;

import java.util.*;

import codejam.utils.utils.LongIntPair;

public class MincostMaxflow2 {

  public static class Edge {
    int to, f, rev;
    
    long cap, cost;

    Edge(int v, long cap, long cost, int rev) {
      this.to = v;
      this.cap = cap;
      this.cost = cost;
      this.rev = rev;
    }

    @Override
    public String toString()
    {
        return "Edge [to=" + to + ", f=" + f + ", cap=" + cap + ", cost=" + cost + ", rev=" + rev + "]";
    }
    
    
  }

  public static List<Edge>[] createGraph(int n) {
    List<Edge>[] graph = new List[n];
    for (int i = 0; i < n; i++)
      graph[i] = new ArrayList<>();
    return graph;
  }

  public static void addEdge(List<Edge>[] graph, int s, int t, long cap, long cost) {
    graph[s].add(new Edge(t, cap, cost, graph[t].size()));
    graph[t].add(new Edge(s, 0, -cost, graph[s].size() - 1));
  }

  static void bellmanFord(List<Edge>[] graph, int s, long[] dist) {
    int n = graph.length;
    Arrays.fill(dist, 0, n, Integer.MAX_VALUE);
    dist[s] = 0;
    boolean[] inqueue = new boolean[n];
    int[] q = new int[n];
    int qt = 0;
    q[qt++] = s;
    for (int qh = 0; (qh - qt) % n != 0; qh++) {
      int u = q[qh % n];
      inqueue[u] = false;
      for (int i = 0; i < graph[u].size(); i++) {
        Edge e = graph[u].get(i);
        if (e.cap <= e.f)
          continue;
        int v = e.to;
        long ndist = dist[u] + e.cost;
        if (dist[v] > ndist) {
          dist[v] = ndist;
          if (!inqueue[v]) {
            inqueue[v] = true;
            q[qt++ % n] = v;
          }
        }
      }
    }
  }

  public static long[] minCostFlow(List<Edge>[] graph, int s, int t, long maxf) {
    int n = graph.length;
    long[] prio = new long[n];
    long[] curflow = new long[n];
    int[] prevedge = new int[n];
    int[] prevnode = new int[n];
    long[] pot = new long[n];

    // bellmanFord can be safely commented if edges costs are non-negative
    bellmanFord(graph, s, pot);
    long flow = 0;
    long flowCost = 0;
    while (flow < maxf) {
      Queue<LongIntPair> q = new PriorityQueue<>(10,
              new Comparator<LongIntPair>() {

                  @Override
                  public int compare(LongIntPair o1, LongIntPair o2) {
                      if (o1._first != o2._first) {
                          return o1._first < o2._first ? -1 : 1;
                      }

                      return o1._second - o2._second;

                  }

              });
      
      q.add(new LongIntPair(0L, s));
      Arrays.fill(prio, 0, n, Long.MAX_VALUE);
      prio[s] = 0;
      curflow[s] = Long.MAX_VALUE;
      while (!q.isEmpty()) {
        LongIntPair cur = q.remove();
        long d = cur.first();
        int u = cur.second();
        if (d != prio[u])
          continue;
        for (int i = 0; i < (int) graph[u].size(); i++) {
          Edge e = graph[u].get(i);
          int v = e.to;
          if (e.cap <= e.f)
            continue;
          long nprio = prio[u] + e.cost + pot[u] - pot[v];
          if (prio[v] > nprio) {
            prio[v] = nprio;
            q.add(new LongIntPair(nprio, v));
            prevnode[v] = u;
            prevedge[v] = i;
            curflow[v] = Math.min(curflow[u], e.cap - e.f);
          }
        }
      }
      if (prio[t] == Long.MAX_VALUE)
        break;
      for (int i = 0; i < n; i++)
        pot[i] += prio[i];
      long df = Math.min(curflow[t], maxf - flow);
      flow += df;
      for (int v = t; v != s; v = prevnode[v]) {
        Edge e = graph[prevnode[v]].get(prevedge[v]);
        e.f += df;
        graph[v].get(e.rev).f -= df;
        flowCost += df * e.cost;
      }
    }
    return new long[] { flow, flowCost };
  }

  // Usage example
  public static void main(String[] args) {
    List<Edge>[] graph = createGraph(3);
    addEdge(graph, 0, 1, 3, 1);
    addEdge(graph, 0, 2, 2, 1);
    addEdge(graph, 1, 2, 2, 1);
    long[] res = minCostFlow(graph, 0, 2, Long.MAX_VALUE);
    long flow = res[0];
    long flowCost = res[1];
    System.out.println(4 == flow);
    System.out.println(6 == flowCost);
  }
}