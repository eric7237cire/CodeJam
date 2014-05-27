#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.tree;
using Logger = Utils.LoggerFile;

using CodeJam.Utils.graph;

namespace Round1A_2014.Problem2
{
    public class FullTreeInput
    {
        public int N;
        public Graph graph;
    }
    public class FullTree : InputFileProducer<FullTreeInput>, InputFileConsumer<FullTreeInput, int>
    {
        public FullTreeInput createInput(Scanner scanner)
        {
            FullTreeInput input = new FullTreeInput();
            input.N = scanner.nextInt();
            input.graph = new Graph(input.N);

            for(int i = 0; i < input.N - 1; ++i)
            {
                int u = scanner.nextInt() - 1;
                int v = scanner.nextInt() - 1;
                input.graph.addConnection(u, v);
                input.graph.addConnection(v, u);
            }

            return input;
        }

        public int processInput(FullTreeInput input)
        {
            int minCost = Int32.MaxValue;
            int[][] dp;
            Ext.createArray(out dp, input.N, input.N, -1);

            for (int root = 0; root < input.N; ++root)
            {
                Logger.LogTrace("\nTrying root {}", root);
                int c = cost(root, root, input, dp);
                if (c < minCost)
                {
                    Logger.LogTrace("Found better root at {}.  Cost {}", root, c);
                    minCost = c;
                }
            }

            return minCost;
        }

        int cost(int parentNode, int node, FullTreeInput input, int[][] dp)
        {

            if (dp[parentNode][node] >= 0)
                return dp[parentNode][node];

            //Loop through all children
            List<int> conNodes = input.graph.getOutboundConnected(node);

            bool isRoot = parentNode == node;

            Preconditions.checkState(conNodes.Count >= 1);

            Logger.LogTrace("Cost parent node {} node {}", parentNode, node);

            //Base case, leaf node
            if (conNodes.Count == 1 && !isRoot)
            {
                return dp[parentNode][node] = 0;
            }

            int minCost = int.MaxValue;

            int costToDelAllChildren = 0;
            int[] costToDel = new int[conNodes.Count];
            for (int i = 0; i < conNodes.Count; ++i)
            {
                if (conNodes[i] == parentNode)
                    continue;

                costToDel[i] = input.graph.floodFill(conNodes[i], node);
                Logger.LogTrace("Child {} cost to del {}", conNodes[i], costToDel[i]);
                costToDelAllChildren += costToDel[i];
            }

            Logger.LogTrace("Init cost to delete all children {}", costToDelAllChildren);
            minCost = costToDelAllChildren;


            for(int i = 0; i < conNodes.Count; ++i)
            {
                if (conNodes[i] == parentNode)
                    continue;

                for(int j = i + 1; j < conNodes.Count; ++j)
                {
                    if (conNodes[j] == parentNode)
                        continue;

                    //Pick the 2 stems for the tree and remove the rest
                    int totalCost = 0;

                    Logger.ChangeIndent(2);
                    totalCost += cost(node, conNodes[i], input, dp);
                    Logger.ChangeIndent(-2);
                    Logger.LogTrace("Cost rhs");
                    Logger.ChangeIndent(2);
                    totalCost += cost(node, conNodes[j], input, dp);
                    Logger.ChangeIndent(-2);

                    //Add the costs to delete the rest except the 2 chosen branches
                    totalCost += costToDelAllChildren;
                    totalCost -= costToDel[i];
                    totalCost -= costToDel[j];

                    if (totalCost < minCost)
                    {
                        minCost = totalCost;
                    }
                }
            }

            Logger.LogTrace("Returning {} for parent node {} node {}", minCost, parentNode, node);
            return dp[parentNode][node] = minCost;
        }

        
    }
}
