
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
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

            string curDir = Directory.GetCurrentDirectory();
            Console.WriteLine("Current directory " + curDir);
        }

        public static void CurrentDomain_ProcessExit(object sender, EventArgs e)
        {
            Console.WriteLine("exit");
            LoggerFile.Instance.writer.Close();
        }

        [Conditional("LOGGING_INFO"), Conditional("LOGGING_DEBUG"), Conditional("LOGGING_TRACE")]
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
                    //
                    String replaceMent = (args[i] ?? "").ToString();

                    if (args[i] is long)
                    {
                        replaceMent = ((long)args[i]).ToString("n0", CultureInfo.InvariantCulture); 
                    }
                    if (args[i] is int)
                    {
                        replaceMent = ((int)args[i]).ToString("n0", CultureInfo.InvariantCulture);
                    }

                    msg = rgx.Replace(msg, replaceMent, 1);
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

        private static void Log(String msg)
        {
            //msg = "Thread id {0} -- ".FormatThis(System.Threading.Thread.CurrentThread.ManagedThreadId) + msg;

            //Console.WriteLine(msg);

            LoggerFile.Instance.writer.WriteLine(msg);

            LoggerFile.Instance.writer.Flush();
        }

    }
}
