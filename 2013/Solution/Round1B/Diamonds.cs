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

//Pascal's triangle, probablity getting exactly N heads in X coin flips

namespace Diamonds
{

    public class Input
    {
        public int NumberFallingDiamonds { get; private set; }
        public Tuple<int, int> Coords { get; internal set; }


        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.NumberFallingDiamonds = scanner.nextInt();
            input.Coords = new Tuple<int, int>(scanner.nextInt(), scanner.nextInt());

            return input;
        }
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

        public static DiamondInfo getDiamondInfo(int nDiamonds)
        {
            //See if pyramid sizes is big enough
            while (pyramidSizes[pyramidSizes.Count - 1] < nDiamonds)
            {
                int index = pyramidSizes.Count - 1;
                int nextOdd = 2 * (index + 1) + 1;
                int toAdd = 2 * nextOdd - 1;
                pyramidSizes.Add(pyramidSizes[index] + toAdd);
            }

            int idx = CodeJamUtils.CjUtils.binarySearch<int>(0, pyramidSizes.Count - 1, pyramidSizes, nDiamonds);



            DiamondInfo di = new DiamondInfo();
            di.baseDiamondSize = pyramidSizes[idx];
            di.sideLength = 2 * idx + 2;
            di.outerLayerDistance = di.sideLength;
            di.N = nDiamonds;
            return di;
        }
    }

    public class Node : IEquatable<Node>
    {
        //private readonly int sideLength;
        private int left;
        private int right;

        public int Left
        {
            get
            {
                return left;
            }            
        }

        public int Right
        {
            get
            {
                return right;
            }
        }

        public Node(int sideLength)
        {
            //this.sideLength = sideLength;
            left = right = 0;
        }

        public Node(int left, int right)
        {
            this.left = left;
            this.right = right;
        }

        public override bool Equals(object obj)
        {
            return this.Equals(obj as Node);

        }
        public override int GetHashCode()
        {
            return Tuple.Create<int, int>(left, right).GetHashCode();
        }


        public bool Equals(Node other)
        {
            if (ReferenceEquals(null, other)) return false;
            return (this.left.Equals(other.left))
                && (this.right.Equals(other.right));
        }

        public static bool operator ==(Node leftOperand, Node rightOperand)
        {
            if (ReferenceEquals(null, leftOperand)) return ReferenceEquals(null, rightOperand);
            return leftOperand.Equals(rightOperand);
        }

        public static bool operator !=(Node leftOperand, Node rightOperand)
        {
            return !(leftOperand == rightOperand);
        }
    }

    public class Diamond : InputFileConsumer<Input, double>
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

        public static void processProbSlow(DiamondInfo di, out IDictionary<Node, double> nodes)
        {
            nodes = new Dictionary<Node, double>();

            IDictionary<Node, double> nodesToProcess = new Dictionary<Node,double>();
            IDictionary<Node, double> newNodes = new Dictionary<Node, double>();

            nodesToProcess.Add(new Node(0, 0), 1);

            int loopCount = di.N - di.baseDiamondSize;

            for(int i = 0; i < loopCount; ++i)
            {
                newNodes.Clear();

                foreach(KeyValuePair<Node, double> nodeProb in nodesToProcess)
                {
                    Node node = nodeProb.Key;
                    //left side full
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

                    addProb(newNodes, new Node(node.Left+1, node.Right), nodeProb.Value / 2);
                    addProb(newNodes, new Node(node.Left, node.Right+1), nodeProb.Value / 2);
                }

                CjUtils.swap(ref nodesToProcess, ref newNodes);
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

        static void Main2(string[] args)
        {
            // Put the following code before InitializeComponent()
            String culture = "en-US";
            // Sets the culture to French (France)
            Thread.CurrentThread.CurrentCulture = new CultureInfo(culture);
            // Sets the UI culture to French (France)
            Thread.CurrentThread.CurrentUICulture = new CultureInfo(culture);

            /*
            DiamondInfo di = DiamondInfo.getDiamondInfo(21);
            IDictionary<Node,double> nodeProb;
            processProb(di, out nodeProb);
            foreach(var kv in nodeProb)
            {
                Logger.Log("Left: {0} Right: {1} Prob : {2}", kv.Key.Left, kv.Key.Right, kv.Value);
            }

            Logger.Log("Done");
            return;
            */
            Diamond diamond = new Diamond();

            Runner<Input, double> runner = new Runner<Input, double>(diamond, Input.createInput);

            List<string> list = new List<string>();
            //list.Add("sample.txt");
            list.Add("B-small-practice.in");
            list.Add("B-large-practice.in");

            runner.run(list, Round1B.Properties.Resources.ResourceManager);
        }


        

        public Diamond()
        {
            triangle = PascalTriangleCreator.createProb(2500);
        }

        public double processInput(Input input)
        {
            DiamondInfo di = DiamondInfo.getDiamondInfo(input.NumberFallingDiamonds);

            Point<int> p = new Point<int>(input.Coords.Item1, input.Coords.Item2);

            int dist = Math.Abs(p.X) + Math.Abs(p.Y);

            if (dist % 2 != 0)
            {
                return 0;
            }

            if (dist > di.outerLayerDistance)
            {
                return 0;
            }

            if (di.outerLayerDistance > dist)
            {
                return 1;
            }

            IDictionary<Node, double> nodeProb;
            processProb(di, out nodeProb);

            Debug.Assert(di.outerLayerDistance == dist, String.Format("Dist is {0}, outer layer is {1}", dist, di.outerLayerDistance));

            double probTotal = 0;

            foreach(KeyValuePair<Node,double> kv in nodeProb)
            {
                if (kv.Key.Left > p.Y)
                {
                    probTotal += kv.Value;
                }
            }

            return probTotal;
        }


    }


}
