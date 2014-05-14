using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

//Dynamic programming, could improve by doing a linear solution.
namespace Round1B
{
    
    public class Input
    {
        public int InitialMoteSize { get; private set; }
        public int numberMotes { get; internal set; }
        public int[] otherMotes { get; protected internal set; }

        
        public Input(Scanner scanner)
        {
            InitialMoteSize = scanner.nextInt();
            numberMotes = scanner.nextInt();

            otherMotes = new int[numberMotes];
            for (int i = 0; i < numberMotes; ++i)
            {
                otherMotes[i] = scanner.nextInt();
            }

        }
    }
   

    

    public class Osmos : InputFileConsumer<Input, int>, InputFileProducer<Input>
    {
        static void Main2(string[] args)
        {

            Osmos osmos = new Osmos();

            Runner<Input, int> runner = new Runner<Input, int>(osmos, osmos);

            List<string> list = new List<string>();
            list.Add("sample.txt");
            list.Add("A-small-practice.in");
            list.Add("A-large-practice.in");

            runner.run(list);
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

             const byte ONE = 1;
            
            public byte minSteps(int progress, int currentMoteSize)
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

                //0 value is used to mean non initialized
                if (memoize[progress, currentMoteSize] > 0)
                {
                    return (byte) ( memoize[progress, currentMoteSize] - 1 );
                }

                //Current mote can be absorbed for free
                if (sortedMotes[progress] < currentMoteSize)
                {
                    return minSteps( progress + 1, currentMoteSize + sortedMotes[progress]);
                }

                //The cost of not absorbding the next mote
                byte skipMote = (byte) (minSteps(progress + 1, currentMoteSize) + 1);

                //No choice, can't grow
                if (currentMoteSize <= 1)
                {                    
                    return (byte) ((memoize[progress, currentMoteSize] = (byte) (skipMote + 1)) - 1);
                }
                else
                {
                    int addMoteSteps = minSteps(progress, currentMoteSize + currentMoteSize - 1) + 1;
                    memoize[progress, currentMoteSize] = (byte) (Math.Min(addMoteSteps, skipMote) + 1);
                    return (byte) (memoize[progress, currentMoteSize]  - 1);
                }

                
            }
        }
        
        public int processInput(Input input)
        {
            int[] motes = input.otherMotes;
            Array.Sort(motes);

            int currentMote = input.InitialMoteSize;

            Solver solver = new Solver(motes);

            return solver.minSteps(0, input.InitialMoteSize);
        }

        public Input createInput(Scanner scanner)
        {
            return new Input(scanner);
        }
    }

    
}
