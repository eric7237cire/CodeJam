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
using Utils.geom;

namespace Round3
{
    class MainNS
    {
        static void Main(string[] args)
        {
            //
            ShowProjections.go();
            return;

            Drawing d = new Drawing();
            d.MaximalVisibleX = 55;
            d.MinimalVisibleX = -5;
            d.MaximalVisibleY = 55;
            d.MinimalVisibleY = -5;

            List<Point<double>> points = new List<Point<double>>();
            points.Add(new Point<double>(16, 27));
            points.Add(new Point<double>(36, 18));
            points.Add(new Point<double>(39, 9));

            points.Add(new Point<double>(29, 4));
            points.Add(new Point<double>(17, 7));
            points.Add(new Point<double>(11, 12));

            points.Add(new Point<double>(7, 15));
            points.Add(new Point<double>(2, 14));
            points.Add(new Point<double>(2, 16));

            points.Add(new Point<double>(4, 23));
            points.Add(new Point<double>(26, 11));
            points.Add(new Point<double>(31, 16));

            d.Polygons.Add(points);

            GeomXmlWriter.Save(d, @"C:\Users\thresh\Documents\e.lgf");


            return;

#if !mono
            bool tests = true;

            if (tests) NUnit.ConsoleRunner.Runner.Main(new string[]
   {
       @"/include=current",
       @"C:\codejam\CodeJam\2013\Solution\UnitTest\bin\x64\Debug\UnitTest.dll"
     //  @"/run=UnitTest.TestModMath",
     //  @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\UnitTest\bin\Debug\UnitTest.dll"
     // System.Reflection.Assembly.GetExecutingAssembly().Location
   });
            //if (tests)
                //return;
#endif
            Cheaters main = new Cheaters();

            List<string> list = new List<string>();

            list.Add("sample.in");

            //list.Add("A-small-practice.in");
           // list.Add("A-large-practice.in");


            //list.Add("B-small-practice.in");
            //   list.Add("B-large-practice.in");

            //list.Add("C-small-practice.in");
            //list.Add("C-large-practice.in");

           // list.Add("D-small-practice.in");
          //  list.Add("D-large-practice.in");

            string dir = @"C:\codejam\CodeJam\2013\Solution\Round3\";
           // dir = @"C:\Users\epeg\Documents\GitHub\CodeJam\2013\Solution\Round3\";
            //dir = @"/home/ent/mono/CodeJam/2013/Solution/Round2/";

            list = list.ConvertAll(s => dir + s);

            CjUtils.RunMain(list, main, main.createInput, null); 
        }
    }
}
