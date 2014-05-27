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

            for(int i = 0; i < 35; ++i)
            {
                Point<int> p = new Point<int>(r.Next(1, 50), r.Next(1, 50));
                //d.AddPoint(p);

                LineSegment<double> per = line.getPerpendicularLineSegWithIntersection(p);
                d.AddAsSeg(per);
            }

            
            d.AddAsLine(line);

           // GeomXmlWriter.Save(d, @"C:\Users\thresh\Documents\e.lgf");
        }

        public static void showPolygon()
        {
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

           // GeomXmlWriter.Save(d, @"C:\Users\thresh\Documents\e.lgf");
        }
    }
}
