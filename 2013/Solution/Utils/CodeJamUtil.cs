using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Resources;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Collections.Concurrent;

namespace CodeJamUtils
{
#if (PERF)
   
    using Logger = CodeJamUtils.LoggerEmpty;
#else
    using Logger = CodeJamUtils.LoggerReal;
#endif

    public interface InputFileProducer<InputClass>
    {
        InputClass createInput(Scanner scanner);
    }

    public interface InputFileConsumer<InputClass, AnswerClass>
    {
        AnswerClass processInput(InputClass input);
    }

    public class Runner<InputClass, AnswerClass>
    {

        public delegate InputClass InputFileProducerDelegate(Scanner scanner);
        private InputFileProducerDelegate inputFileProducerDelegate;
        private InputFileProducer<InputClass> inputFileProducer;
        private InputFileConsumer<InputClass, AnswerClass> inputFileConsumer;

        public Runner(InputFileConsumer<InputClass, AnswerClass> inputFileConsumer, InputFileProducer<InputClass> inputFileProducer)
        {
            this.inputFileConsumer = inputFileConsumer;
            this.inputFileProducer = inputFileProducer;
        }

        public Runner(InputFileConsumer<InputClass, AnswerClass> inputFileConsumer, InputFileProducerDelegate inputFileProducerDelegate)
        {
            this.inputFileConsumer = inputFileConsumer;
            this.inputFileProducerDelegate = inputFileProducerDelegate;
        }

        public void runMultiThread(List<string> fileNames, ResourceManager resourceManager)
        {
            Logger.Log("runMultithead enter");
            Task t = Task.Run(() => producerRun(fileNames, 3, resourceManager));
            t.Wait();
        }

        private void producerConsume(BlockingCollection<Tuple<InputClass, int>> queue, AnswerClass[] answers)
        {
            Logger.Log("Entering producerConsume");

            while (!queue.IsCompleted)
            {
                Tuple<InputClass, int> nextItem = default(Tuple<InputClass, int>);

                if (!queue.TryTake(out nextItem))
                {
                    //Console.WriteLine(" Take Blocked");
                    continue;
                }

                Stopwatch timer = Stopwatch.StartNew();
                Console.WriteLine("Take:{0}", nextItem.Item2);
                AnswerClass ans = inputFileConsumer.processInput(nextItem.Item1);
                Console.WriteLine("Done:{0}", nextItem.Item2);
                timer.Stop();
                TimeSpan timespan = timer.Elapsed;

                Console.WriteLine(String.Format("Input done : {4}  in {0:00}:{1:00}:{2:00} ({3:00} ticks)", timespan.Minutes, timespan.Seconds, timespan.Milliseconds, timespan.Ticks, nextItem.Item2));
                answers[nextItem.Item2 - 1] = ans;

            }

            Logger.Log("Exiting producerConsume");
        }

        private void producerRun(List<string> fileNames, int nConsumerThreads, ResourceManager resourceManager)
        {
            Scanner scanner = null;
            
            Logger.Log("producerRun enter");
            foreach (string fn in fileNames)
            {
                Logger.Log("File {0}", fn);
                BlockingCollection<Tuple<InputClass, int>> queue = new BlockingCollection<Tuple<InputClass, int>>();
                string inputFileName = fn;

                TextReader inputReader = null;
                byte[] obj = (byte[])resourceManager.GetObject(fn);
                if (obj != null)
                {
                    inputReader = new StreamReader(new MemoryStream(obj));
                }
                else
                {
                    inputReader = File.OpenText(fn);
                }

                Stopwatch timer = Stopwatch.StartNew();

                Task[] consumeTasks = null;
                AnswerClass[] answers;

                using (scanner = new Scanner(inputReader))
                {
                    int testCases = scanner.nextInt();

                    answers = new AnswerClass[testCases];
                    consumeTasks = new Task[nConsumerThreads];
                    

                    for (int tc = 1; tc <= testCases; ++tc)
                    {
                        InputClass input;

                        if (inputFileProducer != null)
                        {
                            input = inputFileProducer.createInput(scanner);
                        }
                        else
                        {
                            input = inputFileProducerDelegate.Invoke(scanner);
                        }
                        Logger.Log("Read input for tc {0} : {1}", tc, input.ToString());
                        queue.Add(new Tuple<InputClass, int>(input, tc));
                    }
                    queue.CompleteAdding();

                    for (int i = 0; i < nConsumerThreads; ++i)
                    {
                        Console.WriteLine("Building thread " + i);
                        consumeTasks[i] = Task.Run(() => producerConsume(queue, answers));
                    }

                    //This thread can also now help conuming
                    producerConsume(queue, answers);

                    
                    timer.Stop();
                    TimeSpan timespan = timer.Elapsed;

                    Console.WriteLine(String.Format("Input done {0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));


                }

                Task.WaitAll(consumeTasks);

                string outputFileName = Regex.Replace(inputFileName, @"(\..*)?$", ".out");
                using (StreamWriter writer = new StreamWriter(outputFileName, false))
                {
                    for (int tc = 1; tc <= answers.Length; ++tc)
                    {
                        AnswerClass ans = answers[tc - 1];
                        string line = String.Format("Case #{0}: {1}", tc, ans);
                        Logger.Log("Writing to {0}, {1}", outputFileName, line);
                        writer.WriteLine(line);
                    }
                }
            }
        }

        public void run(List<string> fileNames, ResourceManager resourceManager)
        {

            Scanner scanner = null;
            
            foreach (string fn in fileNames)
            {
                string inputFileName = fn;
                string checkFileName = Regex.Replace(inputFileName, @"(\..*)?$", ".check");
                string outputFileName = Regex.Replace(inputFileName, @"(\..*)?$", ".out");

                TextReader inputReader = null;
                byte[] obj = (byte[])resourceManager.GetObject(fn);
                if (obj != null)
                {
                    inputReader = new StreamReader(new MemoryStream(obj));
                }
                else
                {
                    inputReader = File.OpenText(fn);
                }

                Stopwatch timer = Stopwatch.StartNew();


                using (scanner = new Scanner(inputReader))
                using (StreamWriter writer = new StreamWriter(outputFileName, false))
                {
                    int testCases = scanner.nextInt();

                    for (int tc = 1; tc <= testCases; ++tc)
                    {
                        InputClass input;

                        if (inputFileProducer != null)
                        {
                            input = inputFileProducer.createInput(scanner);
                        }
                        else
                        {
                            input = inputFileProducerDelegate.Invoke(scanner);
                        }

                        AnswerClass ans = inputFileConsumer.processInput(input);

                        writer.WriteLine(String.Format("Case #{0}: {1}", tc, ans));
                    }

                    timer.Stop();
                    TimeSpan timespan = timer.Elapsed;

                    Console.WriteLine(String.Format("{0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));


                }



            }




        }
    }

    public sealed class LoggerEmpty
    {

        public static void Log(String msg, params object[] args)
        {

        }


        public static void Log(String msg)
        {

        }

    }

    public sealed class LoggerReal
    {
        private static readonly Lazy<LoggerReal> lazy =
        new Lazy<LoggerReal>(() => new LoggerReal());

        public static LoggerReal Instance { get { return lazy.Value; } }

        private LoggerReal()
        {
            writer = new StreamWriter(@"log.txt", false);
            AppDomain.CurrentDomain.ProcessExit += new EventHandler(CurrentDomain_ProcessExit);

        }

        public static void CurrentDomain_ProcessExit(object sender, EventArgs e)
        {
            Console.WriteLine("exit");
            LoggerReal.Instance.writer.Close();
        }

        public static void Log(String msg, params object[] args)
        {
            Log(String.Format(msg, args));
        }

        private StreamWriter writer;


        public static void Log(String msg)
        {
            Console.WriteLine(msg);
            LoggerReal.Instance.writer.WriteLine(msg);
            //Logger.Instance.writer.Flush();
        }

    }
    public class Scanner : IDisposable
    {
        private TextReader reader;
        string currentWord;

        public Scanner(TextReader reader)
        {
            this.reader = reader;
            readNextWord();
        }

        private void readNextWord()
        {
            System.Text.StringBuilder sb = new StringBuilder();
            char nextChar;
            int next;
            do
            {
                next = reader.Read();
                if (next < 0)
                    break;
                nextChar = (char)next;
                if (char.IsWhiteSpace(nextChar))
                    break;
                sb.Append(nextChar);
            } while (true);
            while ((reader.Peek() >= 0) && (char.IsWhiteSpace((char)reader.Peek())))
                reader.Read();
            if (sb.Length > 0)
                currentWord = sb.ToString();
            else
                currentWord = null;
        }

        public bool hasNextInt()
        {
            if (currentWord == null)
                return false;
            int dummy;
            return int.TryParse(currentWord, out dummy);
        }

        public int nextInt()
        {
            try
            {
                return int.Parse(currentWord);
            }
            finally
            {
                readNextWord();
            }
        }

        public bool hasNextDouble()
        {
            if (currentWord == null)
                return false;
            double dummy;
            return double.TryParse(currentWord, out dummy);
        }

        public double nextDouble()
        {
            try
            {
                return double.Parse(currentWord);
            }
            finally
            {
                readNextWord();
            }
        }

        public string nextWord()
        {
            try
            {
                return currentWord;
            }
            finally
            {
                readNextWord();
            }
        }

        public bool hasNext()
        {
            return currentWord != null;
        }

        public void Dispose()
        {
            Dispose(true);

            // Take yourself off the Finalization queue 
            // to prevent finalization code for this object
            // from executing a second time.
            GC.SuppressFinalize(this);
        }

        // Track whether Dispose has been called.
        private bool disposed = false;

        // Dispose(bool disposing) executes in two distinct scenarios.
        // If disposing equals true, the method has been called directly
        // or indirectly by a user's code. Managed and unmanaged resources
        // can be disposed.
        // If disposing equals false, the method has been called by the 
        // runtime from inside the finalizer and you should not reference 
        // other objects. Only unmanaged resources can be disposed.
        protected virtual void Dispose(bool disposing)
        {
            Console.WriteLine(String.Format("Dispose {0}", disposing));
            // Check to see if Dispose has already been called.
            if (!this.disposed)
            {
                // If disposing equals true, dispose all managed 
                // and unmanaged resources.
                if (disposing)
                {
                    // Dispose managed resources.
                    reader.Dispose();
                }
                // Release unmanaged resources. If disposing is false, 
                // only the following code is executed.
                // CloseHandle(handle);
                //  handle = IntPtr.Zero;
                // Note that this is not thread safe.
                // Another thread could start disposing the object
                // after the managed resources are disposed,
                // but before the disposed flag is set to true.
                // If thread safety is necessary, it must be
                // implemented by the client.

            }
            disposed = true;
        }

        // Use C# destructor syntax for finalization code.
        // This destructor will run only if the Dispose method 
        // does not get called.
        // It gives your base class the opportunity to finalize.
        // Do not provide destructors in types derived from this class.
        ~Scanner()
        {
            Console.WriteLine("~Scanner");
            // Do not re-create Dispose clean-up code here.
            // Calling Dispose(false) is optimal in terms of
            // readability and maintainability.
            Dispose(false);
        }
    }

    public static class CjUtils
    {
        public static void swap<T>(ref T a, ref T b)
        {
            T temp = a;
            a = b;
            b = temp;
        }

        public static int binarySearch<T>(int loIdx, int hiIdx, List<T> list, T target)
        {
            Comparer<T> comp = Comparer<T>.Default;

            if (comp.Compare(list[hiIdx], target) <= 0)
            {
                //Everything in the range is lower than target
                return hiIdx;
            }
            if (comp.Compare(list[loIdx], target) > 0)
            {
                //Everything is greater
                return loIdx;
            }
            //  invariant list[loIdx] <= target ; list[hiIdx] > target
            while (true)
            {
                int midIdx = loIdx + (hiIdx - loIdx) / 2;

                T value = list[midIdx];

                if (comp.Compare(value, target) <= 0)
                {
                    loIdx = midIdx;
                }
                else
                {
                    hiIdx = midIdx;
                }

                Debug.Assert(loIdx <= hiIdx);

                if (hiIdx - loIdx <= 1)
                    break;
            }

            return loIdx;
        }
    }
}
