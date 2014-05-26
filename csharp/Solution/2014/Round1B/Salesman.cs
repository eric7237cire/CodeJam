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

namespace CodeJam.Round1B_2014
{

    using IL = System.Tuple<int, long>;

    public class SalesmanInput
    {
        public int nodes;
        public int edges;

        public int[] zipCodes;
        public Graph graph;
    }


    public class Salesman : InputFileProducer<SalesmanInput>, InputFileConsumer<SalesmanInput, string>
    {
        public SalesmanInput createInput(Scanner scanner)
        {
            scanner.enablePlayBack();
            SalesmanInput input = new SalesmanInput();
            input.nodes = scanner.nextInt();
            input.edges = scanner.nextInt();

            input.zipCodes = new int[input.nodes];
            input.graph = new Graph(input.nodes);

            for (int i = 0; i < input.nodes; ++i)
            {
                input.zipCodes[i] = scanner.nextInt();
            }

            for (int i = 0; i < input.edges; ++i)
            {
                int u = scanner.nextInt() - 1;
                int v = scanner.nextInt() - 1;

                input.graph.addConnection(u, v);
                input.graph.addConnection(v, u);
            }
            Logger.LogTrace(scanner.finishPlayBack());
            return input;
        }

        public static long getVisitable(long visited, int curStartCity, SalesmanInput input)
        {
            /*
            IEnumerable<int> connected = 
                input.graph.getOutboundConnected(curStartCity)
                .OrderBy( (city) => input.zipCodes[city] )
                .Reverse();*/

            visited = visited.SetBit(curStartCity);

           // Logger.LogTrace("Visited {}= {} Cur city {}", visited, visited.ToBinaryString(8), curStartCity);
            if (visited == (1L << input.nodes) - 1)
            {
                return visited;
            }

            foreach (int nextCity in input.graph.getOutboundConnected(curStartCity))
            {
                if (visited.GetBit(nextCity) != 0)
                    continue;

                visited |= getVisitable(visited, nextCity, input);
            }

            return visited;
        }

        public string processInput(SalesmanInput input)
        {
            List<int> nodes = new List<int>();
            for (int n = 0; n < input.nodes; ++n)
            {
                nodes.Add(n);
            }

            nodes.Sort((n1, n2) => input.zipCodes[n1].CompareTo(input.zipCodes[n2]));

            string test = "103484446018001308523241538557587202375846203401614935915377197775231458456670966435727663404744063473473503738324375522898918869471378631187580588522508913499391872716707156294518975968655491508";
            for (int i = 0; i < test.Length; i+=5 )
            {
                string zip = test.Substring(i, 5);
                int node = input.zipCodes.ToList().FindIndex(z => int.Parse(zip) == z);
                Logger.LogTrace("Order {}", node + 1);
            }

                Logger.LogTrace("Nodes in order {}", nodes.Select(n => "\nNode: {0}.  Connections: {1}".FormatThis(n + 1,
                    input.graph.getOutboundConnected(n).Select(n2 => n2 + 1).ToCommaString())
                    ).ToCommaString());
            long allVisited = (1L << input.nodes) - 1;

            /*
             * Store node-> visitable (not being able to traverse anything in answer)
             */
            List<IL> ans = new List<IL>();
            ans.Add(new IL(nodes[0], allVisited));
            nodes.RemoveAt(0);
            long inAnswer = 0;
            inAnswer = inAnswer.SetBit(ans[0].Item1);

            long usedReturn = 0;
            int lastFromNode = -1;

            int loopCh = 0;
            while (ans.Count < input.nodes)
            {
                ++loopCh;
                if (loopCh > input.nodes+3)
                {
                    return "err";
                }

                Logger.LogTrace("Finding another node for ans. inAnswer {} Ans is {}",
                    inAnswer.ToBinaryString(50),
                    ans.Select( (a) => "{0}: visited {1}".FormatThis(a.Item1+1, a.Item2.ToBinaryString(50))).ToCommaString()
                    );
                bool found = false;

                for (int nodeIdx = 0; nodeIdx < nodes.Count && !found; ++nodeIdx)
                {

                    int node = nodes[nodeIdx];
                    Logger.LogTrace("Trying {}.  Used Return {}", node+1, usedReturn.ToBinaryString(50));
                    for (int jumpIdx = ans.Count - 1; jumpIdx >= 0; --jumpIdx)
                    {
                        int fromNode = ans[jumpIdx].Item1;
                        Logger.LogTrace("From node {} Trying {}.  ", fromNode+1, node+1);

                        if (!input.graph.isConnected(node, fromNode))
                        {
                            Logger.LogTrace("Node {} not connected to {}", node, fromNode);
                            continue;
                        }

                        if (/*lastFromNode != fromNode &&*/ usedReturn.GetBit(fromNode) != 0)
                        {
                            Logger.LogTrace("From Node {} already been used in return flight", fromNode);
                            continue;
                        }

                        //Check if 0 to jumpIdx visit's all nodes
                        long visitTally = 0;
                        for(int tIdx = 0; tIdx <= jumpIdx; ++tIdx)
                        {
                            visitTally |= ans[tIdx].Item2;
                        }

                        if (visitTally != allVisited)
                        {
                            Logger.LogTrace("Visit tally {} != allVisited {}", visitTally.ToBinaryString(50), allVisited.ToBinaryString(50));
                            continue;
                        }

                        //long canVisit = getVisitable(inAnswer, ans[jumpIdx], input);

                        //if ( (canVisit | inAnswer) == (1 << input.nodes) - 1 )

                        Logger.LogTrace("Found next best ans: {} based off of {}.  ", node+1, fromNode+1);

                        int fromNodeIndexInAns = ans.FindIndex((il) => (il.Item1 == fromNode));
                        for (int idx = fromNodeIndexInAns + 1; idx < ans.Count; ++idx )
                        //if (fromNode != lastFromNode && lastFromNode != -1)
                        {
                            Logger.LogTrace("Setting {} as return used", ans[idx].Item1+1);
                            usedReturn = usedReturn.SetBit(ans[idx].Item1);
                        }

                        lastFromNode = fromNode;
                        inAnswer = inAnswer.SetBit(node);
                        ans.Add(new IL(node, 0));

                        //Recalculate answer array
                        for (int a = 0; a < ans.Count; ++a)
                        {
                            ans[a] = new IL(ans[a].Item1,
                                getVisitable(inAnswer, ans[a].Item1, input));
                            Logger.LogTrace("Recalculate ans using inAns {} idx {} node {} visitable {}", 
                                inAnswer.ToBinaryString(50),
                                a, ans[a].Item1+1, ans[a].Item2.ToBinaryString(50));
                        }

                        nodes.RemoveAt(nodeIdx);
                        found = true;
                        break;

                    }
                }
            }

            return ans.Aggregate("", (str, il) => str + input.zipCodes[il.Item1].ToString());
        }
    }



    [TestFixture]
    public class TestSalesman
    {
        [Test]
        public void testHasSolution()
        {
            SalesmanInput input = new SalesmanInput();
            input.nodes = 6;
            Graph g = new Graph(6);
            input.graph = g;
            g.addUndirectedConnection(0, 5);
            g.addUndirectedConnection(0, 1);
            g.addUndirectedConnection(1, 3);
            g.addUndirectedConnection(1, 2);
            g.addUndirectedConnection(2, 4);
            g.addUndirectedConnection(3, 4);

            long all = (1 << 6) - 1;
            Assert.AreEqual(all, Salesman.getVisitable(0, 0, input));

            Logger.LogTrace("Starting test");
            //Node 2 already visited
            long ans = Salesman.getVisitable(visited: "000010".FromBinaryString(),
                curStartCity: 0, input: input);

            Assert.AreEqual("00100011".FromBinaryString(), ans, ans.ToBinaryString(8));
        }
        [Test]
        public void TestVisitable()
        {
            SalesmanInput input = new SalesmanInput();
            input.nodes = 4;
            Graph g = new Graph(input.nodes);
            input.graph = g;
            g.addUndirectedConnection(0, 1);
            g.addUndirectedConnection(0, 2);
            g.addUndirectedConnection(1, 3);

            
            Logger.LogTrace("Starting test");
            //Node 2 already visited
            long ans = Salesman.getVisitable(visited: "000011".FromBinaryString(),
                curStartCity: 0, input: input);

            Assert.AreEqual("00000111".FromBinaryString(), ans, ans.ToBinaryString(8));
        }
    }
}