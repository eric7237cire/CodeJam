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
using Utils;
using Utils.geom;

namespace Round3
{
    using TP = Tuple<double, Point<int>>;
    //using Tuple<double, Point<int>> = TP;
    public class Rural : InputFileConsumer<RuralInput, string>, InputFileProducer<RuralInput>
    {
        

        public string processInput(RuralInput input)
        {
            Drawing d = new Drawing();

            /*
            List<Point<int>> points = new List<Point<int>>();

            

            Random r = new Random(15);
            for (int i = 0; i < 95; ++i)
            {
                Point<int> p = new Point<int>(r.Next(1, 50), r.Next(1, 50));
                
                points.Add(p);

            }*/

            List<Point<int>> points = new List<Point<int>>(input.points);

            foreach (Point<int> p in points)
                d.AddPoint(p);

            int midPointIdx;
            List<Point<int>> ch = points.ConvexHullChains( PointExt.ccw, out midPointIdx, true);

            List<Point<int>> hull = new List<Point<int>>(ch);

            if (hull.Count == points.Count)
            {
                IEnumerable<int> l = hull.Select((p) => points.IndexOf(p));
                return string.Join(" ", l);
            }

            List<Point<int>> lowerHull = new List<Point<int>>();
            List<Point<int>> upperHull = new List<Point<int>>();

            for (int i = 0; i <= midPointIdx; ++i)
                lowerHull.Add(hull[i]);

            for (int i = midPointIdx; i < hull.Count; ++i)
                upperHull.Add(hull[i]);

            upperHull.Add(hull[0]);
            

            Preconditions.checkState( lowerHull.Count + upperHull.Count == ch.Count + 2);
            
                

           // d.AddPolygon(upperHull, "EB3D49");
           // d.AddPolygon(lowerHull, "59EE8D");

            //Create polyline

            HashSet<Point<int>> set = new HashSet<Point<int>>(ch);

            LineSegment<int> horLine = LineExt.createSegmentFromCoords(0,0, 200000,1);
                       
            List<TP> middlePoints = new List<TP>();

            foreach(Point<int> p in points)
            {
                if (set.Contains(p))
                    continue;

                LineSegment<double> intSeg = horLine.getPerpendicularLineSegWithIntersection(p);

                middlePoints.Add(new TP(intSeg.p2.X, p));
            }

            

            middlePoints.Sort();
            

            //Create upper
            List<TP> addInUpper = new List<TP>(middlePoints);
            List<TP> addInLower = new List<TP>(middlePoints);

            Action< List<TP> , List<Point<int>> > addFunc = (addIn, hullHalfToAdd) =>
                {
                    //Not adding hull endpoints (shared between the 2 hulls)
                    foreach(Point<int> p in hullHalfToAdd.Skip(1).Take(hullHalfToAdd.Count-2))
                    {
                        LineSegment<double> intSeg = horLine.getPerpendicularLineSegWithIntersection(p);

                        addIn.Add(new TP(intSeg.p2.X, p));
                    }
                    addIn.Sort();
                };

            addFunc(addInUpper, upperHull);
            addFunc(addInLower, lowerHull);


           // d.AddPolygon(addInUpper.Select( (tp) => tp.Item2 ), "FF0000");
            
            List<Point<int>> lowerPoly = new List<Point<int>> ( addInUpper.Select((tp) => tp.Item2) );

            lowerPoly.InsertRange(0, lowerHull.Reverse<Point<int>>());
            
            List<Point<int>> upperPoly = new List<Point<int>>(addInLower.Select((tp) => tp.Item2));

            upperPoly.InsertRange(0, upperHull);

           // d.AddPolygon(lowerPoly, "FF0000");
           // d.AddPolygon(upperPoly, "00FF00");

            d.MaximalVisibleX = 5 + points.Max( (p) => p.X );
            d.MinimalVisibleX = -5 + points.Min((p) => p.X);
            d.MaximalVisibleY = 5 + points.Max((p) => p.Y); ;
            d.MinimalVisibleY = -5 + points.Min((p) => p.Y); ;
               
            //GeomXmlWriter.Save(d, @"C:\Users\thresh\Documents\e.lgf");

            double totalArea = hull.PolygonArea();
            double area = lowerPoly.PolygonArea();
            double area2 = upperPoly.PolygonArea();

            Preconditions.checkState(lowerPoly.Count == points.Count);
            Preconditions.checkState(upperPoly.Count == points.Count);

            List<Point<int>> biggerPolygon;
            
                Preconditions.checkState(lowerPoly.checkIsPolygon());
                Preconditions.checkState(upperPoly.checkIsPolygon());
                biggerPolygon = area > area2 ? lowerPoly : upperPoly;
            

            
            IEnumerable<int> l2 = biggerPolygon.Select((p) => points.IndexOf(p));

            Preconditions.checkState(totalArea / 2 <= biggerPolygon.PolygonArea());

            return string.Join(" ", l2);
        }

        

        public RuralInput createInput(Scanner scanner)
        {
            RuralInput input = new RuralInput();
            input.nPoints = scanner.nextInt();
            input.points = new Point<int>[input.nPoints];

            for (int i = 0; i < input.nPoints; ++i)
            {
                input.points[i] = new Point<int>(scanner.nextInt(), scanner.nextInt());
            }

            return input;
        }
    }

    public class RuralInput
    {
        
        internal int nPoints { get; set; }

        internal Point<int>[] points;

    }
}
