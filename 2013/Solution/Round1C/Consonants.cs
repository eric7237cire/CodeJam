using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Round1C
{
    class Consonants : InputFileConsumer<Input, int>
    {

        public int processInput(Input input)
        {
            return input.n;
        }

        static void Main(string[] args)
        {



            Consonants main = new Consonants();

            Runner<Input, int> runner = new Runner<Input, int>(main, Input.createInput);

            List<string> list = new List<string>();

            list.Add("sample.txt");

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
        public string name { get; private set; }
        public int n { get; private set; }


        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.name = scanner.nextWord();
            input.n = scanner.nextInt();

            return input;
        }


    }

}
