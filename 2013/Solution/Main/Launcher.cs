#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
//#define LOGGING_TRACE
using CodeJamUtils;
using Round1C_P2;
using Round2_P1;
using Round3;
using RoundFinal;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UnitTest;
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

           // Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\2013\Solution\Round3\");
            Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\2013\Solution\RoundFinal\");

            if (tests) NUnit.ConsoleRunner.Runner.Main(new string[]
   {
       //@"/include=current",
       @"/run=UnitTest.TestRunner",
      // @"/run=UnitTest.TestUtils",
       //"/nothread",
       @"C:\codejam\CodeJam\2013\Solution\UnitTest\bin\x64\Debug\UnitTest.dll"
     //  @"/run=UnitTest.TestModMath",
     //  @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\UnitTest\bin\Debug\UnitTest.dll"
     // System.Reflection.Assembly.GetExecutingAssembly().Location
   });
            //if (tests)
            //return;
#endif

            TestMath tm = new TestMath();
            tm.TestModdedLongArithmetic();

          //  TestUtils tu = new TestUtils();
            //tu.testEquality();
          //  TestWheel tw = new TestWheel();
           // tw.TestRBruteForce();
          //  tw.TestLargeFractionToDouble();
           // tw.TestEI_bruteForce();
            //tw.TestPermutationWithRep();
            //tw.TestPij_bruteforce();
            //tw.TestPijk_bruteForce();
            //tw.TestPijk_bruteForce2();
           // tw.TestP();
            //tw.TestCompare();
           // return;

            //Cheaters main = new Cheaters();
            //  Rural main = new Rural();
            //Pogo main = new Pogo();
            
           // Wheel main = new Wheel();
            Graduation main = new Graduation();

           // Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\2013\Solution\Round2\");
            //TicketSwap main = new TicketSwap();

            // main.processInput(null);
            // return;

            List<string> list = new List<string>();

            list.Add("sample.in");

#if !DEBUG
            list.Add("A-small-practice.in");
#endif
            // list.Add("A-large-practice.in");


            //list.Add("B-small-practice.in");
            // list.Add("B-large-practice.in");

            //list.Add("C-small-practice.in");
            //list.Add("C-large-practice.in");

            // list.Add("D-small-practice.in");
            // list.Add("D-large-practice.in");

            //string dir = @"C:\codejam\CodeJam\2013\Solution\Round3\";
            //Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\2013\Solution\Round1C\");
            

            // dir = @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\Round3\";
#if mono
            Directory.SetCurrentDirectory( @"/home/ent/mono/CodeJam/2013/Solution/Round3/" );
#endif
            // list = list.ConvertAll(s => dir + s);

             CjUtils.RunMain(list, main, main.createInput, null); 
           // CjUtils.RunMainMulti(list, main, main.createInput, null);
        }
    }
}
