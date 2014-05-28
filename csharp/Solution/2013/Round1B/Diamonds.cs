using CodeJamUtils;
using Osmos;
using PascalsTriangle;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Utils.geom;
using Utils;

//Pascal's triangle, probablity getting exactly N heads in X coin flips

namespace Round1B_2013.Problem2
{

    public class Input
    {
        public int NumberFallingDiamonds { get; internal set; }
        public Tuple<int, int> Coords { get; internal set; }
    }

    public class DiamondInfo
    {
        //Total diamonds 
        public int N { get; set; }

        //At certain points The probability becomes 100%, basically a pyramid of base size 1, 6, 15 etc        
        public int baseDiamondSize { get; set; }

        //not counting top
        public int sideLength { get; set; }

        //Grid distance from origin to outer layer
        public int outerLayerDistance { get; set; }

        private static List<int> pyramidSizes = new List<int>();

        static DiamondInfo()
        {
            pyramidSizes.Add(1);
        }

        //Get the pyramid that will definitely be 100% filled given nDiamonds
        public static DiamondInfo getDiamondInfo(int nDiamonds)
        {
            //See if pyramid sizes is big enough to contain nDiamonds
            while (pyramidSizes[pyramidSizes.Count - 1] < nDiamonds)
            {
            	//last index
                int index = pyramidSizes.Count - 1;
                //3, 5, 7, etc
                int nextOdd = 2 * (index + 1) + 1;
                
                //How much to create a pyramid of the next size
                int toAdd = 2 * nextOdd - 1;
                pyramidSizes.Add(pyramidSizes[index] + toAdd);
            }

            int idx = CodeJamUtils.CjUtils.binarySearch<int>(0, pyramidSizes.Count - 1, pyramidSizes, nDiamonds);



            DiamondInfo di = new DiamondInfo();
            di.baseDiamondSize = pyramidSizes[idx];
            di.sideLength = 2 * idx + 2;
            di.outerLayerDistance = di.sideLength;
            di.N = nDiamonds;
            Preconditions.checkState(di.N >= di.baseDiamondSize);
            
            return di;
        }
    }

    public class Node : Point<int>
    {
        public int Left
        {
            get {return X;}      
        }

        public int Right
        {
            get {return Y;}
        }

        public Node(int sideLength) : base(0,0)
        {
            
        }

        public Node(int left, int right) : base(left, right)
        {
            
        }

        
    }

    public class Diamond : InputFileConsumer<Input, string>
    {
        

        private PascalsTriangle<double> triangle;

        public void processProb(DiamondInfo di, out IDictionary<Node, double> nodes)
        {
            nodes = new Dictionary<Node, double>();

            int loopCount = di.N - di.baseDiamondSize;
            
            for (int i = 0; i <= loopCount; ++i)
            {
                int left = i;
                int right = loopCount - i;

                if (left > di.sideLength)
                {
                    right += (left - di.sideLength);
                    left = di.sideLength;
                }
                if (right > di.sideLength)
                {
                    left += (right - di.sideLength);
                    right = di.sideLength;
                }

                double prob = triangle[loopCount, i];

                addProb(nodes, new Node(left, right), prob);

            }

        }

        //Still fast enough for large
        public static void processProbSlow(DiamondInfo di, out IDictionary<Node, double> nodes)
        {
            nodes = new Dictionary<Node, double>();
            
            IDictionary<Node, double> nodesToProcess = new Dictionary<Node,double>();
            IDictionary<Node, double> newNodes = new Dictionary<Node, double>();

            //Start with a 100% probability of having 0 diamonds to left, 0 on right side
            nodesToProcess.Add(new Node(0, 0), 1);

            int loopCount = di.N - di.baseDiamondSize;

            for(int i = 0; i < loopCount; ++i)
            {
                newNodes.Clear();

                foreach(KeyValuePair<Node, double> nodeProb in nodesToProcess)
                {
                    Node node = nodeProb.Key;
                    //left side full, so all of parent probability is used on 1 possibility
                    if (node.Left == di.sideLength)
                    {
                        addProb(newNodes, new Node(node.Left, node.Right+1), nodeProb.Value);
                        continue;
                    }
                    if (node.Right == di.sideLength)
                    {
                        addProb(newNodes, new Node(node.Left+1, node.Right), nodeProb.Value);
                        continue;
                    }

                    //50% of parent probability on each side
                    addProb(newNodes, new Node(node.Left+1, node.Right), nodeProb.Value / 2);
                    addProb(newNodes, new Node(node.Left, node.Right+1), nodeProb.Value / 2);
                }

                Ext.swap(ref nodesToProcess, ref newNodes);
            }

            nodes = nodesToProcess;
        }

        private static void addProb(IDictionary<Node, double> nodes, Node node, double prob)
        {
            if (nodes.ContainsKey(node))
            {
                double existingProb = nodes[node];
                nodes[node] = existingProb + prob;
            }
            else
            {
                nodes[node] = prob;
            }
        }

        public static Boolean isCenter(Point<int> p)
        {
            int dist = Math.Abs(p.X) + Math.Abs(p.Y);

            return dist % 2 == 0;
        }

        public Diamond()
        {
            triangle = PascalTriangleCreator.createProb(2500);
        }
        
        //processProbSlow(DiamondInfo di, out IDictionary<Node, double> nodes)

        public string processInputSlow(Input input)
        {
        	return processInputGeneral(input, processProbSlow);
        }
        
        public string processInput(Input input)
        {
        	return processInputGeneral(input, processProb);
        }
        
        public delegate void ProcessProbFuncDelegate(DiamondInfo di, out IDictionary<Node, double> dict);
        
        public string processInputGeneral(Input input, ProcessProbFuncDelegate processProbFunc)
        {
            DiamondInfo di = DiamondInfo.getDiamondInfo(input.NumberFallingDiamonds);

            Point<int> p = new Point<int>(input.Coords.Item1, input.Coords.Item2);

            //Find manhattan distance to see if a diamond could be centered there
            int dist = Math.Abs(p.X) + Math.Abs(p.Y);

            if (dist % 2 != 0)
            {
                return "0";
            }

            //Use final diamond form to see if point could be in 
            if (dist > di.outerLayerDistance)
            {
                return "0";
            }

            //Since only the last layer wont be filled in, if dist is strictly less, 
            //there will always be a diamond there 
            if (di.outerLayerDistance > dist)
            {
                return "1";
            }

            IDictionary<Node, double> nodeProb;
            processProbFunc(di, out nodeProb);

            Debug.Assert(di.outerLayerDistance == dist, String.Format("Dist is {0}, outer layer is {1}", dist, di.outerLayerDistance));

            double probTotal = 0;

            foreach(KeyValuePair<Node,double> kv in nodeProb)
            {
                if (kv.Key.Left > p.Y)
                {
                    probTotal += kv.Value;
                }
            }

            return probTotal.ToString( "0.######", new CultureInfo("en-US") );
        }

        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.NumberFallingDiamonds = scanner.nextInt();
            input.Coords = new Tuple<int, int>(scanner.nextInt(), scanner.nextInt());

            return input;
        }

    }


}
