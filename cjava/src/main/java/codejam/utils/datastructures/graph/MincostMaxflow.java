package codejam.utils.datastructures.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import codejam.utils.math.Numeric;

import com.google.common.collect.Lists;

public class MincostMaxflow<FlowType, CostType>
{
    

    class Edge
    {
        public int source, destination;
        Numeric<FlowType> capacity, residue;
        Numeric<CostType> cost;

        public Edge(int source, int destination, Numeric<FlowType> capacity, Numeric<FlowType> residue, Numeric<CostType> cost) {
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

    public void reset()
    {
        V.clear();
        E.clear();
    }

    void resizeIfNeeded(int nodeId)
    {
        while (nodeId >= V.size())
            V.add(new ArrayList<Integer>());
    }

    public void addArc(int source, int destination, Numeric<FlowType> capacity, Numeric<CostType> cost)
    {

        int e = E.size();

        resizeIfNeeded(source);
        resizeIfNeeded(destination);

        V.get(source).add(e);
        V.get(destination).add(e + 1);
        E.add(new Edge(source, destination, capacity, capacity, cost));
        E.add(new Edge(destination, source, capacity, flowType.fromInt(0), cost.negate()));
    }

    public MincostMaxflow(Numeric<CostType> cc, Numeric<FlowType> ff) {
        V = Lists.newArrayList();
        E = Lists.newArrayList();

        this.costType = cc;
        this.flowType = ff;
    }

    /**
     * 
     * @param source
     * @param sink
     * @return  Flow / cost
     */
    public Pair<FlowType, CostType> getFlow(int source, int sink)
    {
        resizeIfNeeded(source);
        resizeIfNeeded(sink);

        int N = V.size(), M = E.size();
        Numeric<FlowType> flowSize = flowType.fromInt(0);
        Numeric<CostType> flowCost = costType.fromInt(0);

        Numeric<FlowType> flowInfinity = flowType.getMax();
        Numeric<CostType> costInfinity = costType.getMax();
        //Numeric<CostType> Uepsilon = 1; for (int i=0; i<30; i++) Uepsilon /= 2;

        List<Numeric<FlowType>> flow = Lists.newArrayList();

        for (int i = 0; i < M; ++i)
        {
            flow.add(flowType.fromInt(0));
        }

        List<Numeric<CostType>> potential = Lists.newArrayList();

        for (int i = 0; i < N; ++i)
        {
            potential.add(costType.fromInt(0));
        }

        while (true)
        {
            // use dijkstra to find an augmenting path
            int[] from = new int[N];
            Arrays.fill(from, -1);

            List<Numeric<CostType>> dist = Lists.newArrayList();
            for (int i = 0; i < N; ++i)
            {
                dist.add(costInfinity);
            }

            Queue<Pair<Numeric<CostType>, Integer>> Q = new PriorityQueue<>(10, new Comparator<Pair<Numeric<CostType>, Integer>>() {

                @Override
                public int compare(Pair<Numeric<CostType>, Integer> o1, Pair<Numeric<CostType>, Integer> o2)
                {
                    int cmp1 = o1.getLeft().compareTo(o2.getLeft());
                    if (cmp1 != 0)
                        return -cmp1; //greater first

                    return o1.getRight().compareTo(o2.getRight());

                }

            });
            //priority_queue< pair<U,int>, vector<pair<U,int> >, greater<pair<U,int> > > Q;
            Q.add(new ImmutablePair<Numeric<CostType>, Integer>(costType.fromInt(0), source));

            from[source] = -2;
            dist.set(source, costType.fromInt(0));

            while (!Q.isEmpty())
            {
                Pair<Numeric<CostType>, Integer> top = Q.poll();
                Numeric<CostType> howFar = top.getLeft();
                int where = top.getRight();

                if (dist.get(where).compareTo(howFar) < 0)
                    continue;

                //loop through edges of where
                for (int i = 0; i < V.get(where).size(); i++)
                {
                    Edge edge = E.get(V.get(where).get(i));
                    if (edge.residue.equals(flowType.fromInt(0)))
                        continue;

                    int dest = edge.destination;
                    Numeric<CostType> cost = edge.cost;

                    Numeric<CostType> rhs = dist.get(where).add(potential.get(where)).subtract(potential.get(dest)).add(cost);

                    if (dist.get(dest).compareTo(rhs) > 0)
                    {
                        dist.set(dest, rhs);
                        from[dest] = V.get(where).get(i);
                        Q.add(new ImmutablePair<Numeric<CostType>, Integer>(dist.get(dest), dest));
                    }

                }
            }

            // update vertex potentials
            for (int i = 0; i < N; i++)
            {
                if (dist.get(i).equals(costInfinity))
                {
                    potential.set(i, costInfinity);
                } else if (potential.get(i).compareTo(costInfinity) < 0)
                {
                    potential.set(i, potential.get(i).add(dist.get(i)));
                }
            }

            // if there is no path, we are done
            if (from[sink] == -1)
                return new ImmutablePair<FlowType, CostType>(flowSize.getVal(), flowCost.getVal());

            // construct an augmenting path
            Numeric<FlowType> canPush = flowInfinity;
            int where = sink;

            while (true)
            {
                if (from[where] == -2)
                    break;
                canPush = canPush.min(E.get(from[where]).residue);
                where = E.get(from[where]).source;
            }

            // update along the path
            where = sink;
            while (true)
            {
                if (from[where] == -2)
                    break;
                E.get(from[where]).residue = E.get(from[where]).residue.subtract(canPush);

                //Because we added the edges in pairs xor will either add one or subtract one
                E.get(from[where] ^ 1).residue = E.get(from[where] ^ 1).residue.add(canPush);
                
                //Each unit of flow costs cost 
                flowCost = flowCost.add(E.get(from[where]).cost.multiplyNumeric(canPush));
                where = E.get(from[where]).source;
            }
            flowSize = flowSize.add(canPush);

        }

        //return null;
    }

    Numeric<CostType> costType;
    Numeric<FlowType> flowType;

}
