using CodeJamUtils;
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

            ManyPrizes main = new ManyPrizes();
            List<string> list = new List<string>();

            list.Add("sample.in");
            //list.Add("A-small-practice.in");
            //list.Add("A-large-practice.in");

          list.Add("B-small-practice.in");
            list.Add("B-large-practice.in");

            string dir = @"C:\codejam\CodeJam\2013\Solution\Round2\";

            list = list.ConvertAll(s => dir + s);

            CjUtils.RunMain(list, main, Input.createInput, null); //Round1C.Properties.Resources.ResourceManager
        }
    }
}
