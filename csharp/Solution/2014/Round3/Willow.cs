//#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam._2014.Round2;
using CodeJam.Utils.geom;
using CodeJam.Utils.graph;
using CodeJam.Utils.math;
using CodeJam.Utils.tree;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Wintellect.PowerCollections;
using Logger = Utils.LoggerFile;



namespace Year2014.Round3.Problem4
{

    public class WillowInput
    {
        
        public int N;

        public List<int> C;

        public List<Pair<int, int>> Roads;
    }

    public class Willow : InputFileProducer<WillowInput>, InputFileConsumer<WillowInput, int>
    {
        public WillowInput createInput(Scanner scanner)
        {
            WillowInput input = new WillowInput();

            input.N = scanner.nextInt();

            input.C = new List<int>();
            input.Roads = new List<Pair<int, int>>();

            for (int i = 0; i < input.N; ++i)
            {
                input.C.Add(scanner.nextInt());
            }

            for (int i = 1; i < input.N; ++i)
            {
                input.Roads.Add(new Pair<int, int>(i, scanner.nextInt()));
            }
            //Logger.LogInfo("Input {}", scanner.finishPlayBack());
            return input;
        }

        
       
        /// <summary>
        /// 
        /// </summary>
        /// <param name="i">last vertex picked by current player</param>
        /// <param name="j">last vertex of other player</param>
        /// <param name="turn">who is making the turn.0 for first player, 1 second player</param>
        /// <returns></returns>
        public int rec(int i, int j, int turn, bool[][][] visited, WillowInput input, 
            GraphUndirectedLinkedList g, GraphUndirected gu) 
        {
            if (visited[i][j][turn])
                return 0;
  
            visited[i][j][turn] = true;

            int ci = input.C[i];
            input.C[i] = 0; //  # Remove the coins at vertex i

            int ret = int.MinValue;

            
            //foreach(var ni in neighbors)
            foreach(int ni in gu.getOutboundConnected(i))
            {
                if (g.GetNode(i, ni) == null)
                    continue;
                
                g.removeConnection(i, ni);

                ret = Math.Max(ret, -rec(j, ni, 1 - turn, visited, input, g, gu));

                g.addConnection(i, ni);                
            }
  
            if (ret == int.MinValue) 
            {
                ret = -rec(j, i, 1 - turn, visited, input, g, gu); //  # See note 5
            }
    
            input.C[i] = ci;  //# Restore the coins at vertex i

            return ret + ci;
      }

        public int processInput(WillowInput input)
        {
            //TreeInt<int> tree = new TreeInt<int>(0);
            GraphUndirectedLinkedList g = new GraphUndirectedLinkedList(input.N);
            GraphUndirected gu = new GraphUndirected(input.N);
            foreach(var road in input.Roads)
            {
                g.addConnection(road.First - 1, road.Second - 1);
                gu.addConnection(road.First - 1, road.Second - 1);
            }

            int max = int.MinValue;

            bool[][][] visited;
            Ext.createArray(out visited, input.N, input.N, 2, false);

            for(int i = 0; i < input.N; ++i)
            {
                Logger.LogInfo("Looking at i {}", i);
                int minResponse = int.MaxValue;

                for(int j = 0; j < input.N; ++j)
                {
                    Ext.resetArray(visited, input.N, input.N, 2, false);
                   // Logger.LogInfo("Looking at j {}", j);
                    
                    /*
                     TreeInt<int> tree = g.GetTree<int>(i);
                     tree.PostOrderTraversal(tree.getRoot(), node =>
                     {
                         node.setData(input.C[node.getId()]);
                     });
                    */
                    int bestScore = rec(i,j, 0, visited, input, g, gu);

                    //Logger.LogDebug("For tree {} score is {}", tree, bestScore);
                    minResponse = Math.Min(bestScore, minResponse);
                }

                max = Math.Max(max, minResponse);
            }

            return max;
        }
        public int processInputFail(WillowInput input)
        {
            //TreeInt<int> tree = new TreeInt<int>(0);
            Graph g = new Graph(input.N);
            foreach(var road in input.Roads)
            {
                g.addUndirectedConnection(road.First - 1, road.Second - 1);
            }


            int maxScore = -1;

            for (int hannaRootNode = 0; hannaRootNode < input.N; ++hannaRootNode )
            {
                TreeInt<int> tree = g.GetTree<int>(hannaRootNode);

                int[] sums = new int[input.N];
                tree.PostOrderTraversal(tree.getRoot(), node =>
                {
                    int childSums = node.getChildren().Count == 0 ? 0 :  node.getChildren().Max(cn => cn.getData());
                    node.setData(childSums + input.C[node.getId()]);
                });

                List<int> subTreeTotals = new List<int>();
                foreach (var cn in tree.getRoot().getChildren())
                {
                    subTreeTotals.Add(cn.getData());
                }

                subTreeTotals.Sort();
                int otherScore = 0;

                if (subTreeTotals.Count > 0)
                {
                    otherScore = subTreeTotals.GetLastValue();
                    subTreeTotals.RemoveAt(subTreeTotals.Count - 1);
                }

                int myScore = input.C[hannaRootNode] + (subTreeTotals.Count == 0 ? 0 : subTreeTotals.GetLastValue()) - otherScore;

                maxScore = Math.Max(maxScore, myScore);

                tree.getRoot().setData(input.C[hannaRootNode]);
                Logger.LogDebug("Tree is {}, my score {}", tree, myScore);
            }
                
            return maxScore;
        }
    }

}