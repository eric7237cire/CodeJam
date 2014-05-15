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
