#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO

//#define LOGGING_TRACE
using CodeJamUtils;
using Round1C_2014.Problem2;
using Round1C_2014.Problem3;
using Round3_2008.Problem1;
using Round3_2008.Problem2;
using Round1B_2013;
using Round1B_2013.Problem1;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Utils;
using Utils.geom;
using RoundFinal_2013.Problem5;
using Round2_2014.Problem1;
using Round2_2014.Problem2;
using Round2_2014.Problem3;
using Year2014.Round3.Problem1;
using Year2014.Round3.Problem2;
using Year2014.Round3.Problem3;
using Year2014.Round3.Problem4;

using Year2014.Round4.Problem1;
using Year2014.Round4.Problem2;
using Year2014.Round4.Problem3;

namespace MainNS
{
    class MainClass
    {
        static void Main(string[] args)
        {

            //Cheaters main = new Cheaters();
            //  Rural main = new Rural();
            //Pogo main = new Pogo();

            // Wheel main = new Wheel();
#if mono
           Directory.SetCurrentDirectory(@"/home/ent/mono/CodeJam/csharp/Solution/2014/Round1C/");
           //Directory.SetCurrentDirectory(@"/home/ent/mono/CodeJam/2013/Solution/Round3/");
#else
           // Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\csharp\Solution\2013\Round1B\");
          //  Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\csharp\Solution\2013\RoundFinal\");
            //Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\csharp\Solution\2014\Round1A\");
            //Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\csharp\Solution\2014\Qual\");
           // Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\csharp\Solution\2014\Round1B\");
            //Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\csharp\Solution\2014\Round1C\");
            Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\csharp\Solution\2014\Round2\");
#endif
           // Round1B_2013.Problem1.Osmos main = new Round1B_2013.Problem1.Osmos();

            // Graduation main = new Graduation();

            //Rural main = new Rural();


            //Drummer main = new Drummer();
            //XSpot main = new XSpot();
            //CantStop main = new CantStop();
            //Story main = new Story();

            //Chaos main = new Chaos();
            //FullTree main = new FullTree();
            //Shuffle main = new Shuffle();

            // Magic main = new Magic();
            //Cookie main = new Cookie();
            //MineSweeper main = new MineSweeper();
            //War main = new War();

            //Repeater main = new Repeater();
           // Lottery main = new Lottery();
         //   Salesman main = new Salesman();

           // Elf main = new Elf();
           // Train main = new Train();
            //Round1C_2014.Problem3.Enclosure main = new Round1C_2014.Problem3.Enclosure();

            //2014 round 2
            //greedy easy 
            //DataPacking main = new DataPacking();

            //min swaps to get Increasing then decreasing sequence
            //UpAndDown main = new UpAndDown();
            //DontBreakNile main = new DontBreakNile();

            //Counting permutations in a Trie, post order traversal, combinatorics
            //TrieSharding main = new TrieSharding();

            //2014 round3 

            //Binary search, interesting log n * log n
           // MagicalMarvelousTour main = new MagicalMarvelousTour();

            //Bottum up dynamic programming like  a boss
            //LastHit main = new LastHit();
          
            //rules based processing, reasoning.  A binary search too.
            //CrimeHouse main = new CrimeHouse();

            //Tree parsing, TODO slow memoization, redo in bottom up.  Halfway done
            //Willow main = new Willow();

            //2014 final round (4)
           // CheckerboardMatrix main = new CheckerboardMatrix();

            //Sorting in powers 2, generalzie rule from small case to larger case
            //PowerSwapper main = new PowerSwapper();

            //Another one (like Willow) where c# can't handle the recursion depth.  Understanding...
            SymmetricTrees main = new SymmetricTrees();

            string baseDir = @"C:\codejam\CodeJam\csharp\Solution\";

            //Portal main = new Portal();
            //Pockets main = new Pockets();

            Match m = new Regex(@"Year(.*)\.(.*)\.Problem\d+").Match(main.GetType().Namespace);
            Preconditions.checkState(m.Success);

            if (m.Success)
            {
                baseDir = baseDir + m.Groups[1] + @"\" + m.Groups[2] + "\\";
            }
            else { 
                m = new Regex(@"(.*)_(.*)\.Problem\d+").Match(main.GetType().Namespace);
                Preconditions.checkState(m.Success);

                baseDir = baseDir + m.Groups[2] + @"\" + m.Groups[1] + "\\";
            }
            Directory.SetCurrentDirectory(baseDir);
            //TicketSwap main = new TicketSwap();

            // main.processInput(null);
            // return;

            List<string> list = new List<string>();

         list.Add("sample.in");
            // list.Add("E-small-practice.in");
             // list.Add("E-large-practice.in");

           //   list.Add("A-small-practice.in");
            //
            // list.Add("A-large-practice.in");


           // list.Add("B-small-practice.in");
           // list.Add("B-large-practice.in");

            list.Add("C-small-practice.in");
            list.Add("C-large-practice.in");

         //  list.Add("D-small-practice.in");
         //   list.Add("D-large-practice.in");

            //string dir = @"C:\codejam\CodeJam\2013\Solution\Round3\";
            //Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\2013\Solution\Round1C\");


            // dir = @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\Round3\";

            // list = list.ConvertAll(s => dir + s);

            CjUtils.RunMain(list, main, main.createInput, null);
             //CjUtils.RunMainMulti(list, main, main.createInput, null);
        }
    }
}
