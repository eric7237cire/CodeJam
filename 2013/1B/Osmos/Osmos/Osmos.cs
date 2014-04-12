using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Boxing
{
    class Boxing
    {
        static void test1()
        {
            int i = 3;
            Console.WriteLine("Test {0} {1} {2}", i, i, i);
        }

        static void test2()
        {
            int i = 3;
            object o = i;
            Console.WriteLine("Test {0} {1} {2}", o, o, o);
        }
    }
}

namespace Osmos
{
    
    public class Input
    {
        private Scanner scanner;

        public int InitialMoteSize { get; private set; }
        public int numberMotes { get; internal set; }
        public int[] otherMotes { get; protected internal set; }

        
        public Input(Scanner scanner)
        {
            this.scanner = scanner;

            InitialMoteSize = scanner.nextInt();
            numberMotes = scanner.nextInt();

            otherMotes = new int[numberMotes];
            for (int i = 0; i < numberMotes; ++i)
            {
                otherMotes[i] = scanner.nextInt();
            }

        }
    }
   

    

    public class Osmos
    {
        static void Main2(string[] args)
        {
           

            Scanner scanner = null;

            string inputFileName = @"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\sample.txt";
            //inputFileName = @"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\A-small-practice.in";
            inputFileName = @"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\A-large-practice.in";

            using (scanner = new Scanner(File.OpenText(inputFileName)))
            using (StreamWriter writer = new StreamWriter(@"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\ans.txt", false))
            {
                int testCases = scanner.nextInt();

                for (int tc = 1; tc <= testCases; ++tc)
                {
                    Input input = new Input(scanner);
                    Logger.Log("Input initial {0} n {1} array {2}", input.InitialMoteSize, input.numberMotes, string.Join(", ", input.otherMotes));

                    int ans = Solve(input);

                    writer.WriteLine(String.Format("Case #{0}: {1}", tc, ans));
                }

            }

            scanner = new Scanner(File.OpenText(@"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\sample.txt"));

        }

        private class Solver
        {
             private int[] sortedMotes;
             private byte[,] memoize;

            const int maxMoteSize = 1000001;

            public Solver(int[] sortedMotes)
             {
                 this.sortedMotes = sortedMotes;
                 this.memoize = new byte[sortedMotes.Length+1, maxMoteSize+1];
             }

            private static byte safeAdd(byte n1, byte n2)
            {
                return safeAdd(n1, (int)n2);
            }
            private static byte safeAdd(byte n1, int n2)
            {
                checked
                {
                    int ans = n1 + n2;
                    return (byte)ans;
                }
            }
            public byte minSteps(byte progress, int currentMoteSize)
            {
                //base case, quand nous sommes finis
                if (progress >= sortedMotes.Length)
                {
                    return 0;
                }

                if (currentMoteSize > maxMoteSize)
                {
                    currentMoteSize = maxMoteSize;
                }

                if (memoize[progress, currentMoteSize] > 0)
                {
                    return safeAdd(memoize[progress, currentMoteSize], -1);
                }


                if (sortedMotes[progress] < currentMoteSize)
                {
                    return minSteps( safeAdd(progress, 1), currentMoteSize + sortedMotes[progress]);
                }

                byte skipMote = safeAdd(minSteps(safeAdd(progress, 1), currentMoteSize), 1);

                //No choice, can't grow
                if (currentMoteSize <= 1)
                {
                    memoize[progress, currentMoteSize] = safeAdd(skipMote,1);
                    //return(safeAdd( , -1));
                    return safeAdd(memoize[progress, currentMoteSize], -1);
                }
                else
                {
                    byte addMoteSteps = safeAdd(minSteps(progress, currentMoteSize + currentMoteSize - 1), 1);
                    memoize[progress, currentMoteSize] = safeAdd(Math.Min(addMoteSteps, skipMote), 1);
                    return safeAdd(memoize[progress, currentMoteSize], -1);
                }

                


                
                
            }
        }

        public static int Solve(Input input)
        {
            int[] motes = input.otherMotes;
            Array.Sort(motes);

            int currentMote = input.InitialMoteSize;

            Solver solver = new Solver(motes);

            return solver.minSteps(0, input.InitialMoteSize);
        }

        
    }

    
}
