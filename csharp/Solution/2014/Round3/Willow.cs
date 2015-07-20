#define LOGGING_DEBUG
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

        public int processInput(WillowInput input)
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