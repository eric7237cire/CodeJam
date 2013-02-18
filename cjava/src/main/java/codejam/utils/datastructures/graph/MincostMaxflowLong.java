package codejam.utils.datastructures.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codejam.utils.utils.LongIntPair;

import com.google.common.collect.Lists;

public class MincostMaxflowLong {

    final protected static Logger log = LoggerFactory.getLogger("main");

    class Edge {
        public int source, destination;
        long capacity, residue;
        long cost;

        public Edge(int source, int destination, long capacity, long residue,
                long cost) {
            super();
            this.source = source;
            this.destination = destination;
            this.capacity = capacity;
            this.residue = residue;
            this.cost = cost;
        }

    }

    List<List<Integer>> V;
    List<Edge> E;

    public void reset() {
        V.clear();
        E.clear();
    }

    void resizeIfNeeded(int nodeId) {
        while (nodeId >= V.size())
            V.add(new ArrayList<Integer>());
    }

    public void addArc(int source, int destination, long capacity, long cost) {

        int e = E.size();

        resizeIfNeeded(source);
        resizeIfNeeded(destination);

        V.get(source).add(e);
        V.get(destination).add(e + 1);
        E.add(new Edge(source, destination, capacity, capacity, cost));
        E.add(new Edge(destination, source, capacity, 0L, -cost));
    }

    public MincostMaxflowLong() {
        V = Lists.newArrayList();
        E = Lists.newArrayList();

    }

    /**
     * 
     * @param source
     * @param sink
     * @return Flow / cost
     */
    public Pair<Long, Long> getFlow(int source, int sink) {
        resizeIfNeeded(source);
        resizeIfNeeded(sink);

        int N = V.size();
        long flowSize = 0;
        long flowCost = 0;

        long flowInfinity = Long.MAX_VALUE;
        long costInfinity = Long.MAX_VALUE;

        //Used to avoid negative weight cycles
        long[] potential = new long[N];

        int iterCheck = 0;
        while (true) {
            ++iterCheck;
            if (iterCheck % 20 == 0)
                log.info("Starting getFlow loop {}", iterCheck);
            // use dijkstra to find an augmenting path
            int[] from = new int[N];
            Arrays.fill(from, -1);

            long[] dist = new long[N];
            Arrays.fill(dist, costInfinity);

            Queue<LongIntPair> Q = new PriorityQueue<>(10,
                    new Comparator<LongIntPair>() {

                        @Override
                        public int compare(LongIntPair o1, LongIntPair o2) {
                            if (o1._first != o2._first) {
                                return o1._first < o2._first ? -1 : 1;
                            }

                            return o1._second - o2._second;

                        }

                    });
            // priority_queue< pair<U,int>, vector<pair<U,int> >,
            // greater<pair<U,int> > > Q;
            Q.add(new LongIntPair(0L, source));

            from[source] = -2;
            dist[source] = 0;

            while (!Q.isEmpty()) {
                LongIntPair top = Q.poll();
                long howFar = top._first;
                int where = top._second;

                if (dist[where] < howFar)
                    continue;

                // loop through edges of where
                for (int i = 0; i < V.get(where).size(); i++) {
                    Edge edge = E.get(V.get(where).get(i));
                    if (edge.residue == 0L)
                        continue;

                    int dest = edge.destination;
                    long cost = edge.cost;

                    long rhs = dist[where] + potential[where] - potential[dest]
                            + cost;

                    if (dist[dest] > rhs) {
                        dist[dest] = rhs;
                        from[dest] = V.get(where).get(i);
                        Q.add(new LongIntPair(dist[dest], dest));
                    }

                }
            }

            // update vertex potentials
            for (int i = 0; i < N; i++) {
                if (dist[i] == costInfinity) {
                    potential[i] = costInfinity;
                } else if (potential[i] < costInfinity) {
                    potential[i] += dist[i];
                }
            }

            // if there is no path, we are done
            if (from[sink] == -1)
                return new ImmutablePair<Long, Long>(flowSize, flowCost);

            // construct an augmenting path
            long canPush = flowInfinity;
            int where = sink;

            while (true) {
                if (from[where] == -2)
                    break;
                canPush = Math.min(canPush, E.get(from[where]).residue);
                where = E.get(from[where]).source;
            }

            // update along the path
            where = sink;
            while (true) {
                if (from[where] == -2)
                    break;
                E.get(from[where]).residue = E.get(from[where]).residue
                        - (canPush);

                // Because we added the edges in pairs xor will either add one
                // or subtract one
                E.get(from[where] ^ 1).residue = E.get(from[where] ^ 1).residue
                        + (canPush);

                // Each unit of flow costs cost
                flowCost += E.get(from[where]).cost * (canPush);
                where = E.get(from[where]).source;
            }
            flowSize += canPush;

        }

    }

}
