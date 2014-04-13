using CodeJamUtils;
using Osmos;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

//Dynamic programming, could improve by doing a linear solution.
//Uses checked addition

namespace Diamonds
{

    public class Input
    {
        public int NumberFallingDiamonds { get; private set; }
        public Tuple<int,int> Coords  { get; internal set; }
        

        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.NumberFallingDiamonds = scanner.nextInt();
            input.Coords = new Tuple<int,int>(scanner.nextInt(), scanner.nextInt());

            return input;
        }
    }

    public class DiamondInfo
    {
        //Total diamonds 
        public int N  {get; set;}

        //At certain points The probability becomes 100%, basically a pyramid of base size 1, 3, 5 etc        
        public int baseDiamondSize  {get; set;}

        //not counting top
        public int sideLength {get; set;}

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

            int idx = CodeJamUtils.Utils.binarySearch<int>(0, pyramidSizes.Count-1, pyramidSizes, nDiamonds);



            DiamondInfo di = new DiamondInfo();
            di.baseDiamondSize = pyramidSizes[idx];
            di.sideLength = 2 * idx  ;
            di.N = nDiamonds;
            return di;
        }
    }

    public class Diamond : InputFileConsumer<Input, string>
    {
        static void Main(string[] args)
        {

            Diamond diamond = new Diamond();

            string baseDir = @"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\";
            Runner<Input, string> runner = new Runner<Input, string>(baseDir, diamond, Input.createInput);

            List<string> list = new List<string>();
            list.Add("sample.txt");
            //list.Add("A-small-practice.in");
            //list.Add("A-large-practice.in");

            runner.run(list);
        }

        public string processInput(Input input)
        {
            return input.NumberFallingDiamonds.ToString();
        }

        
    }


}
