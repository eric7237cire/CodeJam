#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Round3
{
    class MainNS
    {
        static void Main(string[] args)
        {
#if !mono
            bool tests = true;

            if (tests) NUnit.ConsoleRunner.Runner.Main(new string[]
   {
       @"/include=current",
     //  @"C:\codejam\CodeJam\2013\Solution\UnitTest\bin\x64\Debug\UnitTest.dll"
     //  @"/run=UnitTest.TestModMath",
       @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\UnitTest\bin\Debug\UnitTest.dll"
     // System.Reflection.Assembly.GetExecutingAssembly().Location
   });
            //if (tests)
                //return;
#endif
            Cheaters main = new Cheaters();

            List<string> list = new List<string>();

            list.Add("sample.in");
            list.Add("A-small-practice.in");
           // list.Add("A-large-practice.in");

            //list.Add("B-small-practice.in");
            //   list.Add("B-large-practice.in");

            //list.Add("C-small-practice.in");
            //list.Add("C-large-practice.in");

           // list.Add("D-small-practice.in");
          //  list.Add("D-large-practice.in");

            string dir = @"C:\codejam\CodeJam\2013\Solution\Round3\";
            dir = @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\Round3\";
            //dir = @"/home/ent/mono/CodeJam/2013/Solution/Round2/";

            list = list.ConvertAll(s => dir + s);

            CjUtils.RunMain(list, main, main.createInput, null); 
        }
    }
}
