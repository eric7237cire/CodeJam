using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils.geom;

namespace Round3
{
    class Rural : InputFileConsumer<RuralInput, string>, InputFileProducer<RuralInput>
    {
        public string processInput(RuralInput input)
        {
            List<Point<int>> points = new List<Point<int>>();

            Drawing d = new Drawing();

            Random r = new Random(15);
            for (int i = 0; i < 95; ++i)
            {
                Point<int> p = new Point<int>(r.Next(1, 50), r.Next(1, 50));
                d.AddPoint(p);
                points.Add(p);

                //LineSegment<double> per = line.getPerpendicularLineSegWithIntersection(p);
                //d.AddAsSeg(per);
            }

            Stack<Point<int>> ch = points.ConvexHull();

            d.AddPolygon(ch);

            GeomXmlWriter.Save(d, @"C:\Users\thresh\Documents\e.lgf");
            return "3";
        }

        public RuralInput createInput(Scanner scanner)
        {
            RuralInput input = new RuralInput();
            input.nPoints = scanner.nextInt();
            input.points = new Point<double>[input.nPoints];

            for (int i = 0; i < input.nPoints; ++i)
            {
                input.points[i] = new Point<double>(scanner.nextInt(), scanner.nextInt());
            }

            return input;
        }
    }

    public class RuralInput
    {
        
        internal int nPoints { get; set; }

        internal Point<double>[] points;

    }
}
