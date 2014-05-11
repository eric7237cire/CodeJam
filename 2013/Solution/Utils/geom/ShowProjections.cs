using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Utils.geom
{
    public class ShowProjections
    {
        public static void go()
        {
            LineSegment<int> line = LineExt.createSegmentFromCoords(3, 3, 16, 7);

            Random r = new Random(5);
            Drawing d = new Drawing();

            for(int i = 0; i < 5; ++i)
            {
                Point<int> p = new Point<int>(r.Next(1, 50), r.Next(1, 50));
                d.AddPoint(p);
            }

            
            d.AddAsLine(line);

            GeomXmlWriter.Save(d, @"C:\Users\thresh\Documents\e.lgf");
        }
    }
}
