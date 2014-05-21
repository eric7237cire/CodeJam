using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.geom;
using Logger = Utils.LoggerFile;

[assembly: System.Runtime.CompilerServices.InternalsVisibleTo("UnitTest")]

//Where proof by induction simplifies greatly the problem
namespace Round1C_P2
{

   
    public class Pogo : InputFileConsumer<Input, string>
    {
        static private List<int> summations;

        public Pogo()
        {

        }
        static Pogo()
        {
            summations = new List<int>();
            int curSum = 0;
            int n = 0;
            summations.Add(0);
            while (curSum < 2000000)
            {
                n++;
                curSum += n;
                summations.Add(curSum);
            }
        }

        public static Point<int> simulate(string dir)
        {
            Point<int> p = new Point<int>(0, 0);
            List<int>[] al = new List<int>[4];
            for (int i = 0; i < 4; ++i)
            {
                al[i] = new List<int>();
            }
            for (int idx = 0; idx < dir.Length; ++idx)
            {
                switch (dir[idx])
                {
                    case 'N':
                        p.Y = p.Y + idx + 1;
                        al[0].Add(idx + 1);
                        break;
                    case 'S':
                        p.Y -= idx + 1;
                        al[1].Add(idx + 1);
                        break;
                    case 'E':
                        p.X += idx + 1;
                        al[2].Add(idx + 1);
                        break;
                    case 'W':
                        p.X -= idx + 1;
                        al[3].Add(idx + 1);
                        break;
                }
            }

            for (int i = 0; i < 4; ++i)
            {
                Logger.LogTrace(string.Join(", ", al[i]) + " sum: " + al[i].Sum());

            }

            return p;

        }

        public string processInput(Input input)
        {
            //Determine minimum N such that 1 + ... + N >= |X| + |Y|

            int sumAbs = Math.Abs(input.X) + Math.Abs(input.Y);

            int lowIdx, hiIdx;
            Tuple<int, int> bestMatch = summations.binarySearch(sumAbs, out lowIdx, out hiIdx);

            int N = bestMatch.Item1 == sumAbs ? lowIdx : hiIdx;

            //Now check parity
            int sumAbsParity = sumAbs % 2;
            int oneToNParity = summations[N] % 2;

            if (sumAbsParity != oneToNParity)
            {
                N += N % 2 == 0 ? 1 : 2; //if N is even, then N+1 will change parity, if odd, then we need N+1 and N+2
            }

            Point<int> curPoint = new Point<int>(input.X, input.Y);
            StringBuilder ans = new StringBuilder();


            for (; N >= 1; --N)
            {
                //Check induction properties
                Preconditions.checkState(Math.Abs(curPoint.X) + Math.Abs(curPoint.Y) <= summations[N]);
                Preconditions.checkState((Math.Abs(curPoint.X) + Math.Abs(curPoint.Y)) % 2 == summations[N] % 2);

                if (Math.Abs(curPoint.X) >= Math.Abs(curPoint.Y))
                {
                    if (curPoint.X > 0)
                    {
                        ans.Insert(0, 'E');
                        curPoint.X -= N;
                    }
                    else
                    {
                        ans.Insert(0, 'W');
                        curPoint.X += N;
                    }
                }
                else
                {
                    if (curPoint.Y > 0)
                    {
                        ans.Insert(0, 'N');
                        curPoint.Y -= N;
                    }
                    else
                    {
                        ans.Insert(0, 'S');
                        curPoint.Y += N;
                    }
                }
            }

            return ans.ToString();
        }

        /**
         * Finds closest sum then adds +1 via N / N+1 to adjust
         */
        public string processInputSmall(Input input)
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

            int bestIdx;
            if ((sum - bestMatch.Item1) < 2 + bestMatch.Item2 - sum)
            {
              //  best = bestMatch.Item1;
                bestIdx = lowIdx;
            }
            else
            {
               // best = bestMatch.Item2;
                bestIdx = hiIdx;
            }

            StringBuilder dirStr = new StringBuilder();

            int greaterCoordAbs = Math.Max(xAbs, yAbs);
            int lowerCoordAbs = Math.Min(xAbs, yAbs);

            char greaterPlus, greaterMinus, lesserPlus; //, lesserMinus;
            if (yAbs > xAbs)
            {
                //Advance towards the absolute value
                greaterPlus = input.Y > 0 ? 'N' : 'S';
                greaterMinus = input.Y > 0 ? 'S' : 'N';
                lesserPlus = input.X > 0 ? 'E' : 'W';
               // lesserMinus = input.X > 0 ? 'W' : 'E';
            }
            else
            {
                lesserPlus = input.Y > 0 ? 'N' : 'S';
               // lesserMinus = input.Y > 0 ? 'S' : 'N';
                greaterPlus = input.X > 0 ? 'E' : 'W';
                greaterMinus = input.X > 0 ? 'W' : 'E';
            }

            int greaterTotal = 0;

            for (int idx = bestIdx; idx >= 0; --idx)
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
          //  int greaterCoord = xAbs > yAbs ? input.X : input.Y;

            for (int i = 0; i < Math.Abs(greaterCoordAbs - greaterTotal); ++i)
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
            string ans = dirStr.ToString();

            Point<int> check = simulate(ans);

            if (check.X != input.X || check.Y != input.Y)
            {
                throw new Exception("gah");
            }

            return ans;
        }

        
        
        public Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.X = scanner.nextInt();
            input.Y = scanner.nextInt();

            return input;
        }
    }




    public class Input
    {
        public int X { get; set; }
        public int Y { get; set; }

        public static Input createInput(int x, int y)
        {
            Input input = new Input();
            input.X = x;
            input.Y = y;

            return input;
        }

        


    }

}
