﻿#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE

using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Numerics;
using System.Reflection;
using System.Resources;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Collections.Concurrent;

using Logger = Utils.LoggerFile;
using Utils;
using System.Globalization;
using CodeJam.Main;

namespace CodeJamUtils
{

    public interface InputFileProducer<InputClass>
    {
        InputClass createInput(Scanner scanner);
    }

    public interface InputFileConsumer<InputClass, AnswerClass>
    {
        AnswerClass processInput(InputClass input);
    }

    public interface InputFileConsumer2<InputClass>
    {
        void processInput(InputClass input, IAnswerAcceptor answerAcceptor, int testCase);
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

        public void runMultiThread(List<string> fileNames)
        {
            Logger.LogTrace("runMultithead enter");
            producerRun(fileNames);
        }

        [Obsolete]
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
                Logger.Log("Take:{0}", nextItem.Item2);
                AnswerClass ans = inputFileConsumer.processInput(nextItem.Item1);
                Logger.Log("Done:{0}", nextItem.Item2);
                timer.Stop();
                TimeSpan timespan = timer.Elapsed;

                Logger.Log(String.Format("Input done : {4}  in {0:00}:{1:00}:{2:00} ({3:00} ticks)", timespan.Minutes, timespan.Seconds, timespan.Milliseconds, timespan.Ticks, nextItem.Item2));
                answers[nextItem.Item2 - 1] = ans;

            }

            Logger.Log("Exiting producerConsume");
        }

        private void consumeSingle(InputClass input, int testCase, AnswerClass[] answers)
        {
            
            Stopwatch timer = Stopwatch.StartNew();
            Logger.LogTrace("Entering consume single Take:{0}", testCase);
                
            AnswerClass ans = inputFileConsumer.processInput(input);
                
            
            timer.Stop();
            TimeSpan timespan = timer.Elapsed;
            
            Logger.LogInfo(String.Format("\n\nTest case done : {4} ans: {5}  in {0:00}:{1:00}:{2:00} ({3:00} ticks\n\n)", 
                timespan.Minutes, timespan.Seconds, timespan.Milliseconds, timespan.Ticks, testCase, ans));
            answers[testCase - 1] = ans;

            Logger.LogTrace("Exiting consume single");
        }

        [Obsolete]
        private void producerRun2(List<string> fileNames, int nConsumerThreads, ResourceManager resourceManager)
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
                        Preconditions.checkState(tc <= 20);
                        
                        
                    }
                    queue.CompleteAdding();

                   
                    for (int i = 0; i < nConsumerThreads; ++i)
                    {
                        Logger.Log("Building thread " + i);
                       // consumeTasks[i] = Task.Run(() => producerConsume(queue, answers));
                    }

                    //This thread can also now help conuming
                    producerConsume(queue, answers);


                    timer.Stop();
                    TimeSpan timespan = timer.Elapsed;

                    Logger.Log(String.Format("Input done {0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));


                }

                Task.WaitAll(consumeTasks);

                Regex regex = new Regex(@"(\..*)?$");
                string outputFileName = regex.Replace(inputFileName, ".out", 1);

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


       

        private void producerRun(List<string> fileNames)
        {
            Scanner scanner = null;
            
            Logger.LogTrace("producerRun enter");
            foreach (string fn in fileNames)
            {
                Logger.LogInfo("File {}", fn);
                
                string inputFileName = fn;

                TextReader inputReader = File.OpenText(fn);

                Stopwatch timer = Stopwatch.StartNew();

                Task[] consumeTasks = null;
                AnswerClass[] answers;
                Action[] actions;

                using (scanner = new Scanner(inputReader))
                {
                    int testCases = scanner.nextInt();

                    answers = new AnswerClass[testCases];
                    consumeTasks = new Task[testCases];
                    
                    actions = new Action[testCases];

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
                        Logger.LogTrace("Read input for tc {0} : {1}", tc, input.ToString());
                       
                        
                        //Avoid binding to tc which gets incremented beyond the limit
                        
                        int tcLocal = tc;
                        //consumeTasks[tc-1] = Task.Run(() => );
                        actions[tc-1]=() =>
                        {
                            consumeSingle(input, tcLocal, answers);
                        };
                        
                    }

                    
                                        
                    timer.Stop();
                    TimeSpan timespan = timer.Elapsed;

                    Logger.LogInfo(String.Format("Input done {0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));


                }

                var po = new ParallelOptions();
                po.MaxDegreeOfParallelism = 2;
                System.Threading.Tasks.Parallel.Invoke(po, actions);
               // Task.WaitAll(consumeTasks);

                Regex regex = new Regex(@"(\..*)?$");
                string outputFileName = regex.Replace(inputFileName, ".out", 1);

                using (StreamWriter writer = new StreamWriter(outputFileName, false))
                {
                    for (int tc = 1; tc <= answers.Length; ++tc)
                    {
                        AnswerClass ans = answers[tc - 1];
                        string line = String.Format("Case #{0}: {1}", tc, ans);
                        Logger.LogTrace("Writing to {0}, {1}", outputFileName, line);
                        writer.WriteLine(line);
                    }
                }
            }
        }

        public void run(List<string> fileNames, ResourceManager resourceManager = null)
        {

            Scanner scanner = null;
            Regex regex = new Regex(@"(\..*)?$");
            foreach (string fn in fileNames)
            {
                string inputFileName = fn;

               // string checkFileName = regex.Replace(inputFileName, ".check", 1);
                string outputFileName = regex.Replace(inputFileName, ".out", 1);

                TextReader inputReader = null;
                byte[] obj = resourceManager == null ? null : (byte[])resourceManager.GetObject(fn);
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
                        Logger.LogInfo("Testcase# " + tc);
                        if (inputFileProducer != null)
                        {
                            input = inputFileProducer.createInput(scanner);
                        }
                        else
                        {
                            input = inputFileProducerDelegate.Invoke(scanner);
                        }

                        AnswerClass ans = inputFileConsumer.processInput(input);
                        string ansStr = String.Format("Case #{0}: {1}", tc, ans);
                        writer.WriteLine(ansStr);
                        writer.Flush();
                        Logger.Log(ansStr);
                    }

                    timer.Stop();
                    TimeSpan timespan = timer.Elapsed;

                    Logger.LogInfo(String.Format("{0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));


                }



            }




        }
    }

    
    public class Scanner : IDisposable
    {
        private TextReader reader;
        string currentWord;
        int playbackCount = 0;

        private StringBuilder playBack;

        public Scanner(TextReader reader)
        {
            this.reader = reader;
        }

        public void enablePlayBack()
        {
            playBack = new StringBuilder();
            ++playbackCount;
            
        }

        public string finishPlayBack()
        {
            string ret = "Playback #" + playbackCount + "\n" + playBack.ToString();
            playBack = null;
            return ret;
        }

        private void readNextWord()
        {
            System.Text.StringBuilder sb = new StringBuilder();
            char nextChar;
            int next;

            while ((reader.Peek() >= 0) && (char.IsWhiteSpace((char)reader.Peek())))
            {
                next = reader.Read();
                if (playBack != null)
                    playBack.Append((char)next);
            }
            
            do
            {
                next = reader.Read();
                if (next < 0)
                    break;
                nextChar = (char)next;
                
                if (playBack != null)
                    playBack.Append(nextChar);

                if (char.IsWhiteSpace(nextChar))
                    break;
                sb.Append(nextChar);
            } while (true);
            while ((reader.Peek() >= 0) && (char.IsWhiteSpace((char)reader.Peek())))
            {
                next = reader.Read();
                if (playBack != null)
                    playBack.Append((char)next);
            }
            if (sb.Length > 0)
                currentWord = sb.ToString();
            else
                currentWord = null;
        }

        

        public int nextInt()
        {
            readNextWord();
            
            Logger.LogTrace("Parsing [{}]", currentWord);
            return int.Parse(currentWord);
            
        }
        
        public BigInteger nextBigInteger()
        {
            readNextWord();
            
            return BigInteger.Parse(currentWord);
            
        }

        public long nextLong()
        {
            readNextWord();
            
            return long.Parse(currentWord);
            
        }

        public double nextDouble()
        {
            readNextWord(); 

            return double.Parse(currentWord, new CultureInfo("en-US"));
            
        }

        public string nextWord()
        {
            readNextWord(); 

            return currentWord;
        }

        public void buildStringArray( out string[] array, int size) 
        {
            array = new string[size];
            for(int i = 0; i < size; ++i)
            {
                array[i] = nextWord();
            }
            
        }

        public int[] nextIntArray(int size)
        {
            int[] array = new int[size];
            for (int i = 0; i < size; ++i)
            {
                array[i] = nextInt();
            }
            return array;
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
            //Logger.Log(String.Format("Dispose {0}", disposing));
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
            //Logger.Log("~Scanner");
            // Do not re-create Dispose clean-up code here.
            // Calling Dispose(false) is optimal in terms of
            // readability and maintainability.
            Dispose(false);
        }
    }

    public static class CjUtils
    {
        public static void RunMain<InputClass, AnswerClass>(
            List<string> list, 
            InputFileConsumer<InputClass, AnswerClass> main, 
            Runner<InputClass, AnswerClass>.InputFileProducerDelegate inputDelegate,
            ResourceManager rm
            )
        {
            var runner = new Runner<InputClass, AnswerClass>(main, inputDelegate);

            Stopwatch timer = Stopwatch.StartNew();
            runner.run(list, rm);

            timer.Stop();
            TimeSpan timespan = timer.Elapsed;

            Logger.Log(String.Format("Total {0:00}:{1:00}:{2:00}:{3:00}", timespan.Hours, timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));

        }

        public static void RunMainMulti<InputClass, AnswerClass>(List<string> list,
           InputFileConsumer<InputClass, AnswerClass> main,
           Runner<InputClass, AnswerClass>.InputFileProducerDelegate inputDelegate,
           ResourceManager rm = null
           )
        {
            var runner = new Runner<InputClass, AnswerClass>(main, inputDelegate);

            Stopwatch timer = Stopwatch.StartNew();
            runner.runMultiThread(list);

            timer.Stop();
            TimeSpan timespan = timer.Elapsed;

            Logger.LogInfo(String.Format("Total {0:00}:{1:00}:{2:00}", timespan.Minutes, timespan.Seconds, timespan.Milliseconds / 10));

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
