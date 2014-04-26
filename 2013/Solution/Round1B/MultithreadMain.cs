using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CodeJamUtils;
using System.Diagnostics;
using System.Globalization;


using Logger = Utils.LoggerFile;

namespace Multithread
{


    class MultithreadMain : InputFileConsumer<Input, int>
    {

        public int processInput(Input input)
        {
            Random rnd = new Random();
            int iterations = input.iters * 3000000;

            double total = 0;
            for (int i = 0; i < iterations; ++i)
            {
                total += rnd.NextDouble() * rnd.NextDouble();
            }

            return (int)total;
        }

        static void Main2(string[] args)
        {



            MultithreadMain main = new MultithreadMain();

            //string baseDir = @"C:\codejam\CodeJam\2013\1B\Osmos\Osmos\";
            Runner<Input, int> runner = new Runner<Input, int>(main, Input.createInput);

            List<string> list = new List<string>();

            list.Add("iters.txt");

            Stopwatch timer = Stopwatch.StartNew();
            runner.run(list, Round1B.Properties.Resources.ResourceManager);
            // runner.runMultiThread(list);

            timer.Stop();
            TimeSpan timespan = timer.Elapsed;

            Console.WriteLine(String.Format("Total {0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));

        }
    }




    public class Input
    {
        public int iters { get; internal set; }


        public static Input createInput(Scanner scanner)
        {
            Input input = new Input();
            input.iters = scanner.nextInt();

            return input;
        }


    }
}
