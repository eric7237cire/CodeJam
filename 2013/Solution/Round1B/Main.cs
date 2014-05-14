using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Round1B
{
    class MainClass
    {
        public static void Main(string[] args)
        {
            NUnit.ConsoleRunner.Runner.Main(new string[]
   {
       //@"/include=current",
       @"/run=UnitTest.TestRunner",
       @"C:\codejam\CodeJam\2013\Solution\UnitTest\bin\x64\Release\UnitTest.dll"
     //  @"/run=UnitTest.TestModMath",
     //  @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\UnitTest\bin\Debug\UnitTest.dll"
     // System.Reflection.Assembly.GetExecutingAssembly().Location
   });

            return;

            Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\2013\Solution\Round1B");
            Osmos osmos = new Osmos();

            Runner<Input, int> runner = new Runner<Input, int>(osmos, osmos);

            List<string> list = new List<string>();
            //  list.Add("sample.txt");
            // list.Add("A-small-practice.in");
            list.Add("A-large-practice.in");

            runner.run(list);

        }
    }
}
