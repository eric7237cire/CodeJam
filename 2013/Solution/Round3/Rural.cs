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

            Stack<Point<int>> ch = points.ConvexHull();

            List<Point<int>> hull = new List<Point<int>>(ch);

            if (hull.Count == points.Count)
            {
                IEnumerable<int> l = hull.Select((p) => points.IndexOf(p));
                return string.Join(" ", l);
            }

            double avgY = hull.Average( (p) => (double) p.Y);

            int lowToHighIdx = -1;
            int highToLowIdx = -1;

            if (hull.Count <= 4)
            {
                Point<int> highest = hull.Max();

                int idx = hull.IndexOf(highest);
                highToLowIdx = (idx + 1) % hull.Count;
                lowToHighIdx = (hull.Count + idx - 1) % hull.Count;

            }
            else
            {

                for (int i = 0; i < hull.Count; ++i)
                {
                    int y = hull[i].Y;
                    int nextIndex = i == hull.Count - 1 ? 0 : i + 1;
                    int y2 = hull[nextIndex].Y;

                    if (y == y2)
                        continue;

                    if (y2 >= avgY && y <= avgY)
                    {
                        Preconditions.checkState(lowToHighIdx == -1);
                        lowToHighIdx = nextIndex;
                    }
                    if (y2 <= avgY && y >= avgY)
                    {
                        Preconditions.checkState(highToLowIdx == -1);
                        highToLowIdx = i;
                    }
                }
            }
            Preconditions.checkState(lowToHighIdx != -1);
            Preconditions.checkState(highToLowIdx != -1);

            List<Point<int>> lowerHull = new List<Point<int>>();
            List<Point<int>> upperHull = new List<Point<int>>();

            
            
            int index = lowToHighIdx;
            while (true)
            {
                upperHull.Add(hull[index]);
                if (index == highToLowIdx && upperHull.Count > 1)
                    break;
                ++index;
                if (index == hull.Count)
                    index = 0;
            }

            index = highToLowIdx;
            while (true)
            {
                lowerHull.Add(hull[index]);
                if (index == lowToHighIdx && lowerHull.Count > 1)
                    break;
                ++index;
                if (index == hull.Count)
                    index = 0;
            }
            

            Preconditions.checkState( lowerHull.Count + upperHull.Count == ch.Count + 2);
            
                //ch.Average((p) => p.Y);

                //ch.

           // d.AddPolygon(upperHull, "EB3D49");
           // d.AddPolygon(lowerHull, "59EE8D");

            //Create polyline

            HashSet<Point<int>> set = new HashSet<Point<int>>(ch);

            LineSegment<int> horLine = LineExt.createSegmentFromCoords(0,0, 200000,1);

           
            List<TP> middlePoints = new List<TP>();

            foreach(Point<int> p in points)
            {
                //if (p != lowerHull[0] && p != lowerHull.GetLastValue() && set.Contains(p))
                if (set.Contains(p))
                    continue;

                LineSegment<double> intSeg = horLine.getPerpendicularLineSegWithIntersection(p);

                middlePoints.Add(new TP(intSeg.p2.X, p));
            }

            

            middlePoints.Sort();

            for (int i = 0; i < middlePoints.Count - 1; ++i )
            {
                //d.AddAsSeg(middlePoints[i].Item2, middlePoints[i + 1].Item2, "111111");
            }


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


            d.AddPolygon(addInUpper.Select( (tp) => tp.Item2 ), "FF0000");
            // d.AddPolygon(upperPoly, "00FF00");

                // d.AddPolygon(ch);
            List<Point<int>> lowerPoly = new List<Point<int>> ( addInUpper.Select((tp) => tp.Item2) );

            //lowerPoly.InsertRange(0, lowerHull.Skip(1).Take(lowerHull.Count - 2));
            lowerPoly.InsertRange(0, lowerHull);
            
            List<Point<int>> upperPoly = new List<Point<int>>(addInLower.Select((tp) => tp.Item2));

            //upperPoly.InsertRange(0, upperHull.Skip(1).Take(upperHull.Count - 2).Reverse());
            upperPoly.InsertRange(0, upperHull.Reverse<Point<int>>());

           // d.AddPolygon(lowerPoly, "FF0000");
           // d.AddPolygon(upperPoly, "00FF00");

            d.MaximalVisibleX = 5 + points.Max( (p) => p.X );
            d.MinimalVisibleX = -5 + points.Min((p) => p.X);
            d.MaximalVisibleY = 5 + points.Max((p) => p.Y); ;
            d.MinimalVisibleY = -5 + points.Min((p) => p.Y); ;
               
            GeomXmlWriter.Save(d, @"C:\Users\thresh\Documents\e.lgf");

            double totalArea = hull.PolygonArea();
            double area = lowerPoly.PolygonArea();
            double area2 = upperPoly.PolygonArea();

            Preconditions.checkState(lowerPoly.Count == points.Count);
            Preconditions.checkState(upperPoly.Count == points.Count);

            List<Point<int>> biggerPolygon;
            if (upperHull.Count == 3)
            {
                Preconditions.checkState(lowerPoly.checkIsPolygon());
                biggerPolygon = lowerPoly;

                //upper polygon may not be an actual polygon.  with only 3 points in the hull,
                //once the end points are removed, only 1 point, not garuanteed to not touch the other points
            }
            else
            {
                Preconditions.checkState(lowerPoly.checkIsPolygon());
                Preconditions.checkState(upperPoly.checkIsPolygon());
                biggerPolygon = area > area2 ? lowerPoly : upperPoly;
            }

            
            //Preconditions.checkState(Math.Abs(totalArea - area - area2) <= 0.01);
            
            IEnumerable<int> l2 = biggerPolygon.Select((p) => points.IndexOf(p));

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
