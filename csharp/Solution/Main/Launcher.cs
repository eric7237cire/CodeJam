#define LOGGING
#define LOGGING_DEBUG
#define LOGGING_INFO
using CodeJam.Round1A_2014;
using CodeJam.Round1B_2014;
using CodeJam.RoundQual_2014;
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
using Utils.geom;

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
           Directory.SetCurrentDirectory(@"/home/ent/mono/CodeJam/2013/Solution/RoundFinal/");
           //Directory.SetCurrentDirectory(@"/home/ent/mono/CodeJam/2013/Solution/Round3/");
#else
            // Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\2013\Solution\RoundFinal\");
            //Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\csharp\Solution\2014\Round1A\");
            //Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\csharp\Solution\2014\Qual\");
            Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\csharp\Solution\2014\Round1B\");
#endif


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
            Salesman main = new Salesman();

            // Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\2013\Solution\Round2\");
            //TicketSwap main = new TicketSwap();

            // main.processInput(null);
            // return;

            List<string> list = new List<string>();

            list.Add("sample.in");
            // list.Add("E-small-practice.in");
            //  list.Add("E-large-practice.in");

            //  list.Add("A-small-practice.in");

            //  list.Add("A-large-practice.in");


           // list.Add("B-small-practice.in");
            //list.Add("B-large-practice.in");

            list.Add("C-small-practice.in");
            list.Add("C-large-practice.in");

           // list.Add("D-small-practice.in");
           // list.Add("D-large-practice.in");

            //string dir = @"C:\codejam\CodeJam\2013\Solution\Round3\";
            //Directory.SetCurrentDirectory(@"C:\codejam\CodeJam\2013\Solution\Round1C\");


            // dir = @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\Round3\";

            // list = list.ConvertAll(s => dir + s);

            CjUtils.RunMain(list, main, main.createInput, null);
            // CjUtils.RunMainMulti(list, main, main.createInput, null);
        }
    }
}
