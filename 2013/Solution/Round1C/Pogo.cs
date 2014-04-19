using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
[assembly: System.Runtime.CompilerServices.InternalsVisibleTo
                          ("UnitTest")]
namespace Round1C.Pogo
{
#if (PERF)
   
    using Logger = CodeJamUtils.LoggerEmpty;
#else
    using Logger = CodeJamUtils.LoggerReal;
#endif

    class Node
    {
        internal Point<int> curLoc;
        internal char lastMove;
        internal int numHops;
        internal Node parent;

        internal static Node create(Point<int> curLoc,
        char lastMove,
        int numHops,
        Node parent)
        {
            Node node = new Node();
            node.curLoc = curLoc;
            node.lastMove = lastMove;
            node.numHops = numHops;
            node.parent = parent;
            return node;
        }
    }

    internal class Pogo : InputFileConsumer<Input, string>
    {
        static private List<int> summations;

        internal Pogo()
        {

        }
        static Pogo()
        {
            summations = new List<int>();
            int curSum = 0;
            int n = 0;
            while(curSum < 2000000)
            {
                n++;
                curSum += n;
                summations.Add(curSum);
            }
        }

        public static Point<int> simulate(string dir)
        {
            Point<int> p = new Point<int>(0,0);
            for(int idx = 0; idx < dir.Length; ++idx)
            {
                switch(dir[idx])
                {
                    case 'N':
                        p.Y = p.Y + idx + 1;
                        break;
                    case 'S':
                        p.Y -=  idx + 1;
                        break;
                    case 'E':
                        p.X += idx + 1;
                        break;
                    case 'W':
                        p.X -= idx + 1;
                        break;
                }
            }

            return p;

        }

        public string processInput(Input input)
        {
            
            int xAbs = Math.Abs(input.X);
            int yAbs = Math.Abs(input.Y);

            if (xAbs == 0 && yAbs == 0)
            {
                return "";
            }

            int sum = xAbs + yAbs;

            int lowIdx, hiIdx;
            Tuple<int, int> bestMatch = summations.binarySearch(sum, out lowIdx, out hiIdx);

            int best, bestIdx;
            if  ( (sum - bestMatch.Item1 ) < 2 + bestMatch.Item2 - sum) {
                best = bestMatch.Item1;
                bestIdx = lowIdx;
            } else {
                best = bestMatch.Item2;
                bestIdx = hiIdx;
            }

            StringBuilder dirStr = new StringBuilder();

            int greaterCoordAbs = Math.Max(xAbs, yAbs);
            int lowerCoordAbs = Math.Min(xAbs, yAbs);

            char greaterPlus, greaterMinus, lesserPlus, lesserMinus;
            if (yAbs > xAbs)
            {
                //Advance towards the absolute value
                greaterPlus = input.Y > 0 ? 'N' : 'S'; 
                greaterMinus = input.Y > 0 ? 'S' : 'N';
                lesserPlus = input.X > 0 ? 'E' : 'W';
                lesserMinus = input.X > 0 ? 'W' : 'E';
            }
            else
            {
                lesserPlus = input.Y > 0 ? 'N' : 'S';
                lesserMinus = input.Y > 0 ? 'S' : 'N';
                greaterPlus = input.X > 0 ? 'E' : 'W';
                greaterMinus = input.X > 0 ? 'W' : 'E';
            }

            int greaterTotal = 0;

            for (int idx = bestIdx; idx >= 0; --idx )
            {
                int hopSize = idx + 1;
                if (hopSize <= lowerCoordAbs)
                {
                    lowerCoordAbs -= hopSize;
                    dirStr.Insert(0, lesserPlus);
                }
                else
                {
                    dirStr.Insert(0, greaterPlus);
                    greaterTotal += hopSize;
                }
            }

            //lower coord should be exact, greater
            int greaterCoord = xAbs > yAbs ? input.X : input.Y;
            
            for(int i = 0; i < Math.Abs(greaterCoordAbs - greaterTotal); ++i)
            {
                if (greaterCoordAbs < greaterTotal)
                {
                    dirStr.Append(greaterPlus);
                    dirStr.Append(greaterMinus);
                }
                else
                {
                    dirStr.Append(greaterMinus);
                    dirStr.Append(greaterPlus);
                }
            }

            //char[] array = dirStr.ToString().ToCharArray();
           // Array.Reverse(array);
            return dirStr.ToString();
        }

        static void Main(string[] args)
        {



            Pogo main = new Pogo();


            var runner = new Runner<Input, string>(main, Input.createInput);

            List<string> list = new List<string>();

           // list.Add("sample");
            list.Add("B_small_practice");
            //list.Add("A-large-practice.in");

            Stopwatch timer = Stopwatch.StartNew();
            runner.run(list, Round1C.Properties.Resources.ResourceManager);
            // runner.runMultiThread(list);

            timer.Stop();
            TimeSpan timespan = timer.Elapsed;

            Console.WriteLine(String.Format("Total {0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));

        }
    }




    public class Input
    {
        public int X { get; private set; }
        public int Y { get; private set; }

        public static Input createInput(int x, int y)
        {
            Input input = new Input();
            input.X = x;
            input.Y = y;

            return input;
        }

        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.X = scanner.nextInt();
            input.Y = scanner.nextInt();

            return input;
        }


    }

}
