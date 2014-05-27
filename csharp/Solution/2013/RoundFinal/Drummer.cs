#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using Utils.math;
using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils;
using Utils.geom;
using Logger = Utils.LoggerFile;


namespace RoundFinal
{
    using LineSeg = LineSegment<double>;

    public class Drummer : InputFileProducer<DrummerInput>, InputFileConsumer<DrummerInput, string>
    {

        public DrummerInput createInput(Scanner scanner)
        {
            DrummerInput input = new DrummerInput();

            input.N = scanner.nextInt();
            input.T = new int[input.N];
            for (int i = 0; i < input.N; ++i)
            {
                input.T[i] = scanner.nextInt();
            }

            return input;
        }

        public string processInput(DrummerInput input)
        {

            double minDistance = Double.MaxValue;

            List<Point<int>> points = new List<Point<int>>();

            int maxY = int.MinValue;
            int minY = int.MaxValue;

            for (int i = 0; i < input.T.Length; ++i)
            {
                points.Add(new Point<int>(i, input.T[i]));

                if (input.T[i] > maxY)
                    maxY = input.T[i];

                if (input.T[i] < minY)
                    minY = input.T[i];
            }

            int midPointIdx;
            List<Point<int>> hull = points.ConvexHullChains(PointExt.ccw, out midPointIdx, true);

            Drawing d = new Drawing();
            d.MinimalVisibleY = minY - 2;
            d.MaximalVisibleY = maxY + 2;
            d.MinimalVisibleX = 0;
            d.MaximalVisibleX = input.T.Length + 2;

            Logger.LogDebug("start {} midpoint {}", hull[0], hull[midPointIdx]);
            d.AddPolygon(hull, "#FF00FF00");
            //

            List<Point<int>> lowerHull = new List<Point<int>>();
            List<Point<int>> upperHull = new List<Point<int>>();

            for (int i = 0; i <= midPointIdx; ++i)
                lowerHull.Add(hull[i]);

            for (int i = midPointIdx; i < hull.Count; ++i)
                upperHull.Add(hull[i]);

            upperHull.Add(hull[0]);

            Logger.LogDebug("Upper hull {}\nLower hull{}\n", upperHull.ToCommaString(), lowerHull.ToCommaString());

            //def rotatingCalipers(Points):
            /*Given a list of 2d points, finds all ways of sandwiching the points
        between two parallel lines that touch one point each, and yields the sequence
        of pairs of points touched by each pair of lines.*/
            
            int UH = 0;
            int LH = 0;
            while (UH < upperHull.Count - 1 || LH < lowerHull.Count - 1)
            {
                Logger.LogDebug("Caliper points upper {}, lower {}", upperHull[UH], lowerHull[LH]);
                
                bool incLowerHull = false;

                //if all the way through one side of hull, advance the other side
                if (UH == upperHull.Count - 1)
                {
                    LH ++;
                    incLowerHull = true;
                }
                else if (LH == lowerHull.Count - 1)
                {
                    UH ++;                        
                }
                else if ((upperHull[UH].Y - upperHull[UH+1].Y) * (lowerHull[LH+1].X - lowerHull[LH].X) >
                    (lowerHull[LH+1].Y - lowerHull[LH].Y) * (upperHull[UH].X - upperHull[UH+1].X))
                {
                    //dy_u / dx_u > dy_l / dx_u, whichever has the most negative slope can be incremented

                    //lower hull is more negative
                    ++LH;
                    incLowerHull = true;
                }
                else
                {
                    ++UH;
                }

                LineSegment<long> seg;
                LineSegment<long> segPar;

                if (incLowerHull)
                {
                     seg = LineExt.createSegmentFromCoords((long)lowerHull[LH - 1].X, lowerHull[LH - 1].Y,
                        lowerHull[LH].X, lowerHull[LH].Y);

                    segPar = LineExt.createSegmentFromCoords(upperHull[UH].X,upperHull[UH].Y,  upperHull[UH].X + seg.line.B,
                        upperHull[UH].Y - seg.line.A);

                   // d.AddAsLine(seg);
                  //  d.AddAsLine(segPar);

                }
                else
                {
                     seg = LineExt.createSegmentFromCoords((long)upperHull[UH - 1].X, upperHull[UH - 1].Y,
                        upperHull[UH].X, upperHull[UH].Y);

                     segPar = LineExt.createSegmentFromCoords(lowerHull[LH].X, lowerHull[LH].Y, lowerHull[LH].X + seg.line.B,
                        lowerHull[LH].Y - seg.line.A);

                   // d.AddAsLine(seg);
                   // d.AddAsLine(segPar);
                }

                Line<double> line = LineExt.createFromCoords(0, 1d, 0, 2);

                double y1 = line.intersection(seg.line.ToDouble()).Y;
                double y2 = line.intersection(segPar.line.ToDouble()).Y;

                double dist = Math.Abs(y1-y2) / 2;

                if (dist < minDistance)
                {
                    minDistance = dist;
                }

            }

            //TODO create last line between last point inc and first

            //GeomXmlWriter.Save(d, @"C:\Users\thresh\Documents\e.lgf");
            return (minDistance).ToUsString(8);
        }

        public string processInputSmall(DrummerInput input)
        {
            if (input.N <= 2)
            {
                return "0";
            }
            double smallestE = Double.MaxValue;

            //O(N^4) solution

            for (int i = 0; i < input.N; ++i)
            {
                for (int j = i + 1; j < input.N; ++j)
                {
                    for (int k = 0; k < input.N; ++k)
                    {
                        if (k == i || k == j)
                            continue;

                        Line<double> line;
                        double E;

                        findE(input, i, j, k, out E, out line);

                        bool ok = true;

                        E = Math.Abs(E);

                        for (int idx = 0; idx < input.N; ++idx)
                        {
                            double y = (line.C - line.A * idx) / line.B;
                            double diff = Math.Abs(y - input.T[idx]);
                            if (diff > E + 1e-6)
                            {
                                ok = false;
                                break;
                            }
                        }

                        if (ok && E < smallestE)
                        {
                            smallestE = E;
                            Logger.LogDebug("Found best line y = {} + {}x with E {}", line.C / line.B,
                                -line.A / line.B, E);
                        }
                    }
                }
            }

            return smallestE.ToUsString(8);
        }

        //X + Y * index
        public bool IsEPossible(double E, DrummerInput input, out double X, out double Y)
        {
            X = 0;
            Y = 0;
            List<LineSeg> segments = new List<LineSeg>();

            for (int i = 0; i < input.T.Length; ++i)
            {
                segments.Add(LineExt.createSegmentFromCoords(i, input.T[i] - E, i, input.T[i] + E));
            }

            return true;
        }

        public void findE(DrummerInput input, int p1, int p2, int p3, out double E, out Line<double> line)
        {
            /*
             * Assume p1 and p2 both add or subtract E and p3 does opposite
             * 
             * delta (p2 - p1) == delta (p3 - p1)
             */

            double deltaY_p1p2 = input.T[p2] - input.T[p1];  // E's cancel out
            double deltaX_p1p2 = p2 - p1;

            double deltaY_p1p3 = input.T[p3] - input.T[p1];  // + 2E or - 2E
            double deltaX_p1p3 = p3 - p1;

            double lhs = deltaY_p1p2 / deltaX_p1p2 * deltaX_p1p3 - (deltaY_p1p3);

            E = lhs / 2;
            E *= -1;

            //E is positive means p1 - E, p2 - E, p3 + E is the line
            line = LineExt.createFromCoords(p1, input.T[p1] + E,
                p2, input.T[p2] + E);

            Preconditions.checkState(line.onLine(p3, input.T[p3] - E));


        }
    }

    public class DrummerInput
    {
        public int N;
        public int[] T;
    }
}
