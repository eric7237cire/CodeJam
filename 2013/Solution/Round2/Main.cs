using CodeJamUtils;
using Round2.ErdosNS;
using Round2.Pong;
using Round2.TicketSwapping;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Round2
{
    class MainAAA
    {
        static void Main(string[] args)
        {
            //TicketSwap main = new TicketSwap();

            //ManyPrizes main = new ManyPrizes();
           // Erdos main = new Erdos();
            PongMain main = new PongMain();
            List<string> list = new List<string>();

            list.Add("sample.in");
            //list.Add("A-small-practice.in");
            //list.Add("A-large-practice.in");

          //list.Add("B-small-practice.in");
         //   list.Add("B-large-practice.in");

            //list.Add("C-small-practice.in");
            //list.Add("C-large-practice.in");

           // list.Add("D-small-practice.in");

            string dir = @"C:\codejam\CodeJam\2013\Solution\Round2\";
            dir = @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\Round2\";

            list = list.ConvertAll(s => dir + s);

            CjUtils.RunMainMulti(list, main, main.createInput, null); //Round1C.Properties.Resources.ResourceManager
        }
    }
}
