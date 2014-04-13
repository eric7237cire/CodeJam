using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

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

    public class Runner<InputClass, AnswerClass>
    {

        private string fileDir;
        private InputFileProducer<InputClass> inputFileProducer;
        private InputFileConsumer<InputClass, AnswerClass> inputFileConsumer;

        public Runner(string fileDir, InputFileConsumer<InputClass, AnswerClass> inputFileConsumer, InputFileProducer<InputClass> inputFileProducer)
        {
            this.fileDir = fileDir;
            this.inputFileConsumer = inputFileConsumer;
            this.inputFileProducer = inputFileProducer;
        }

        public void run(List<string> fileNames)
        {

            Scanner scanner = null;


            foreach(string fn in fileNames)
            {
                string inputFileName = fileDir + fn;
                string checkFileName = Regex.Replace(inputFileName, @"\..*$", ".check");

                string outputFileName = Regex.Replace(inputFileName, @"\..*$", ".out");

                using (scanner = new Scanner(File.OpenText(inputFileName)))
                using (StreamWriter writer = new StreamWriter(outputFileName, false))
                {
                    int testCases = scanner.nextInt();

                    for (int tc = 1; tc <= testCases; ++tc)
                    {
                        InputClass input = inputFileProducer.createInput(scanner);
                        
                        AnswerClass ans = inputFileConsumer.processInput(input);

                        writer.WriteLine(String.Format("Case #{0}: {1}", tc, ans));
                    }
                }

            }
            
            
            

        }
    }

    class Logger
    {
        public static void Log(String msg, params object[] args)
        {
            Console.WriteLine(String.Format(msg, args));
        }

        public static void Log(String msg)
        {
            Console.WriteLine(msg);
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
}
