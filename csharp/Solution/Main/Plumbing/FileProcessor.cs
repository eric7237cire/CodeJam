using CodeJamUtils;
using StructureMap;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Utils;
using Logger = Utils.LoggerFile;

namespace CodeJam.Main.Plumbing
{
    public interface IFileProcessor
    {
        void ReadInputFile<InputClass>(InputFileProducer<InputClass> inputProducer, String inputFileName);
    }
    public class FileProcessor : IFileProcessor
    {
        private IContainer container;

        public FileProcessor(IContainer cont)
        {
            container = cont;
        }


        public void ReadInputFile<InputClass>(InputFileProducer<InputClass> inputProducer, String inputFileName)
        {
            Scanner scanner = null;

            //Setting base directory
            string baseDir = @"C:\codejam\CodeJam\csharp\Solution\";

            //Portal main = new Portal();
            //Pockets main = new Pockets();

            Match m = new Regex(@"Year(.*)\.(.*)\.Problem\d+").Match( typeof(InputClass).Namespace);
            Preconditions.checkState(m.Success);

            if (m.Success)
            {
                baseDir = baseDir + m.Groups[1] + @"\" + m.Groups[2] + "\\";
            }
            else {
                m = new Regex(@"(.*)_(.*)\.Problem\d+").Match(typeof(InputClass).Namespace);
                Preconditions.checkState(m.Success);

                baseDir = baseDir + m.Groups[2] + @"\" + m.Groups[1] + "\\";
            }
            Directory.SetCurrentDirectory(baseDir);

            Logger.LogTrace("producerRun enter");


            TextReader inputReader = File.OpenText(inputFileName);

            Stopwatch timer = Stopwatch.StartNew();

            IAnswerAcceptor aa = container.GetInstance<IAnswerAcceptor>();

            InputFileConsumer2<InputClass> fileConsumer = container.GetInstance<InputFileConsumer2<InputClass>>();

            int testCases = 0;

            using (scanner = new Scanner(inputReader))
            {
                testCases = scanner.nextInt();
                                
                for (int tc = 1; tc <= testCases; ++tc)
                {
                    InputClass input = inputProducer.createInput(scanner);

                    Logger.LogTrace("Read input for tc {0} : {1}", tc, input.ToString());

                    fileConsumer.processInput(input, aa, tc);
                                        

                }



                timer.Stop();
                TimeSpan timespan = timer.Elapsed;

                Logger.LogInfo(String.Format("Input done {0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));


            }

            Regex regex = new Regex(@"(\..*)?$");
            string outputFileName = regex.Replace(inputFileName, ".out", 1);

            using (StreamWriter writer = new StreamWriter(outputFileName, false))
            {
                for (int tc = 1; tc <= testCases; ++tc)
                {
                    String line = aa.GetAnswer(tc);
                    Logger.LogTrace("Writing to {0}, {1}", outputFileName, line);
                    writer.WriteLine(line);
                }
            }


        }
    }
}
