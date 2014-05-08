#define DEBUG
#define TRACE
#define LOGGING
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace Utils
{
    

    public sealed class LoggerFile
    {
        private static readonly Lazy<LoggerFile> lazy =
        new Lazy<LoggerFile>(() => new LoggerFile());

        public static LoggerFile Instance { get { return lazy.Value; } }

        private LoggerFile()
        {
            writer = new StreamWriter(@"log.txt", false);
            AppDomain.CurrentDomain.ProcessExit += new EventHandler(CurrentDomain_ProcessExit);

            Console.WriteLine("Current directory " + Directory.GetCurrentDirectory());
        }

        public static void CurrentDomain_ProcessExit(object sender, EventArgs e)
        {
            Console.WriteLine("exit");
            LoggerFile.Instance.writer.Close();
        }

        [Conditional("LOGGING"), Conditional("LOGGING_TRACE")]
        public static void Log(String msg, params object[] args)
        {
            Log(String.Format(msg, args));
        }

        private static string replacePlaceholders(string msg, params object[] args)
        {
            Regex rgx = new Regex(@"\{\}");

            if (rgx.IsMatch(msg))
            {
                for (int i = 0; i < args.Count(); ++i)
                {
                    msg = rgx.Replace(msg, args[i].ToString(), 1);
                }
            }

            return msg;
        }

        private static int indent = 0;

        [Conditional("LOGGING_TRACE")]
        public static void LogTrace(String msg, params object[] args)
        {
            msg = replacePlaceholders(msg, args);
            //string msgf = String.Format(msg, args);


            Log(new String(' ', indent) + msg);
        }

        [Conditional("LOGGING_TRACE")]
        public static void ChangeIndent(int val)
        {
            indent += val;

            if (indent > 200)
            {
                throw new ArgumentOutOfRangeException("ex");
            }
        }

        [Conditional("LOGGING_INFO")]
        public static void LogInfo(String msg, params object[] args)
        {
            msg = replacePlaceholders(msg, args);

            Debug.WriteLine(msg);
            Console.WriteLine(msg);
            
            Log(String.Format(msg, args));
        }

        [Conditional("LOGGING_DEBUG")]
        public static void LogDebug(String msg, params object[] args)
        {
            msg = replacePlaceholders(msg, args);
            Log(String.Format(msg, args));
        }

        private StreamWriter writer;

        [Conditional("LOGGING"), Conditional("LOGGING_TRACE")]
        public static void Log(String msg)
        {
            //msg = "Thread id {0} -- ".FormatThis(System.Threading.Thread.CurrentThread.ManagedThreadId) + msg;
            
           LoggerFile.Instance.writer.WriteLine(msg);


            LoggerFile.Instance.writer.Flush();
        }

    }
}
