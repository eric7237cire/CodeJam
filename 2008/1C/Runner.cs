using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Diagnostics;

namespace LogicProblems
{
    interface TestCaseHandler
    {
        string doTestCase();
    }

    class Runner
    {
        static void Main(string[] args) {

            TestCaseHandler tch = new UglyNumbers();

            StreamReader testFile = null;


            /*
             * [a/b/c/d] [s/l/i] [test file]
             */

            if (args.Count() == 1) {
                Console.SetIn(new StreamReader(@"C:\Work\cs\LogicProblems\LogicProblems\input.txt"));
                TextWriterTraceListener writer = new TextWriterTraceListener(System.Console.Out);
                Trace.Listeners.Add(writer);
            } else if (args.Count() > 1) {
                if (args[1][0] == 's') {
                    Console.SetIn(new StreamReader(args[0].ToUpper() + "-small-practice.in"));
                }
                else if (args[1][0] == 'l') {
                    Console.SetIn(new StreamReader(@"C:\Documents and Settings\egroning\workspace\google\" +  args[0].ToUpper() + "-large-practice.in"));
                }

                if (args.Count() >= 3) {
                    testFile = new StreamReader(new FileStream(args[2], FileMode.Open, FileAccess.Read));
                }
            }

            int testCases = int.Parse(Console.In.ReadLine());

            for (int testCase = 1; testCase <= testCases; ++testCase) {
                string a = "Case #" + testCase + ": " + tch.doTestCase();
                Console.WriteLine(a);
                if (testFile != null) {
                    string t = testFile.ReadLine();
                    if (t != a) {
                        throw new Exception(t + " and " + a + " did not match");
                    }
                }
            }

        }
    }
}
