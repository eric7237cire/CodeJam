#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
using CodeJamUtils;
using Round1C_P2;
using Round3;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils.geom;

namespace MainNS
{
    class MainClass
    {
        static void Main(string[] args)
        {
            //


#if !mono
            bool tests = false;

            if (tests) NUnit.ConsoleRunner.Runner.Main(new string[]
   {
       //@"/include=current",
       @"/run=UnitTest.TestLost",
       //"/nothread",
       @"C:\codejam\CodeJam\2013\Solution\UnitTest\bin\x64\Debug\UnitTest.dll"
     //  @"/run=UnitTest.TestModMath",
     //  @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\UnitTest\bin\Debug\UnitTest.dll"
     // System.Reflection.Assembly.GetExecutingAssembly().Location
   });
            //if (tests)
            //return;
#endif
            //Cheaters main = new Cheaters();
            //  Rural main = new Rural();
            //Pogo main = new Pogo();
            Wheel main = new Wheel();

            // main.processInput(null);
            // return;

            List<string> list = new List<string>();

             list.Add("sample.in");

            //list.Add("A-small-practice.in");
            // list.Add("A-large-practice.in");


            //list.Add("B-small-practice.in");
            // list.Add("B-large-practice.in");

            //list.Add("C-small-practice.in");
            //list.Add("C-large-practice.in");

            // list.Add("D-small-practice.in");
            //  list.Add("D-large-practice.in");

            //string dir = @"C:\codejam\CodeJam\2013\Solution\Round3\";
            //Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\2013\Solution\Round1C\");
            Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\2013\Solution\Round3\");

            // dir = @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\Round3\";
#if mono
            Directory.SetCurrentDirectory( @"/home/ent/mono/CodeJam/2013/Solution/Round3/" );
#endif
            // list = list.ConvertAll(s => dir + s);

             CjUtils.RunMain(list, main, main.createInput, null); 
            //CjUtils.RunMainMulti(list, main, main.createInput, null);
        }
    }
}
