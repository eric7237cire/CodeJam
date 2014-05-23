#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using CodeJamUtils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Utils.geom;
using Utils.math;
using Utils;

using Logger = Utils.LoggerFile;

namespace RoundFinal
{
    using Point = Utils.geom.Point<int>;
    using PointD = Utils.geom.Point<double>;
    using IP_Pair = Tuple<double, int>;
    using Frac = Utils.math.BigFraction;
    using System.Globalization;

    public class XSpot : InputFileProducer<XSpotInput>, InputFileConsumer<XSpotInput, string>
    {
        public XSpotInput createInput(Scanner scanner)
        {
            XSpotInput input = new XSpotInput();
            input.N = scanner.nextInt();
            
            input.points = new List<Point>();
            for(int i = 0; i < 4 * input.N; ++i)
            {
            	input.points.Add( new Point(scanner.nextInt(), scanner.nextInt()) );	
            }
            
            return input;            	
        }

        public string processInput(XSpotInput input)
        {
            LineSegment<double> splitLine1;
        	LineSegment<double> splitLine2;
        	
    		XSpot.calcAns(input.points, out splitLine1, out splitLine2);
    		
    		PointD inter = splitLine1.line.intersection(splitLine2.line);
    		
    		//Since the limit is 10^9, we can get more precision if we intersect the line with 10^9 - 1 let's say

            Line<double> right = LineExt.createFromCoords(1e9 - 1, 0, 1e9 - 1, 1);
            Line<double> top = LineExt.createFromCoords(0, 1e9 - 1, 1, 1e9 - 1);

            PointD pointOnX = right.intersection(splitLine1.line);

            if (pointOnX.Y < -1e9 + 1 || pointOnX.Y > 1e9 - 1)
            {
                pointOnX = top.intersection(splitLine1.line);
            }

            Preconditions.checkState(pointOnX.Y >= -1e9 + 1 && pointOnX.Y <= 1e9 + 1);
            Preconditions.checkState(pointOnX.X >= -1e9 + 1 && pointOnX.X <= 1e9 + 1);

            string ixStr = inter.X.ToUsString(9);
            double ix = Double.Parse(ixStr, new CultureInfo("en-US"));

            string iyStr = inter.Y.ToUsString(9);
            double iy = Double.Parse(iyStr, new CultureInfo("en-US"));

            string pxStr = pointOnX.X.ToUsString(9);
            double px = Double.Parse(pxStr, new CultureInfo("en-US"));

            string pyStr = pointOnX.Y.ToUsString(9);
            double py = Double.Parse(pyStr, new CultureInfo("en-US"));

            Preconditions.checkState(checkAnswer(input.points, new PointD(ix,iy), new PointD(px, py)));

    		return "{0} {1} {2} {3}".FormatThis(ixStr, iyStr, pxStr, pyStr);
        }

        static PointD getDirectionVector(double angle, double distance = 1)
        {
            return new PointD(distance * Math.Cos(angle), distance * Math.Sin(angle));
        }
        
        public static void calcAns(List<Point> points, 
        	out LineSegment<double> splitLine1, out LineSegment<double> splitLine2)
        {
        	Preconditions.checkState(points.Count % 4 == 0);
        	
        	int N = points.Count / 4;
        	
        	//No colinear
        	#if DEBUG
        	List<int> listIdxColinear;
        	List<int> duplicates;
        	FindColinear(points, out listIdxColinear, out duplicates);
        	Preconditions.checkState(listIdxColinear.Count == 0);
        	Preconditions.checkState(duplicates.Count == 0);
        	#endif
        	
        	Random r = new Random(1);
        	double ang = r.NextDouble() * Math.PI;
        	
        	//LineSegment<double> splitLine1;
        	//LineSegment<double> splitLine2;
        	//double ang2 = ang + Math.PI / 2;
        	
        	int X = FindX(points, ang, out splitLine1, out splitLine2);
        	
        	Logger.LogTrace("starting X {} N {}", X, N);
        	
        	if (X > N)
        	{
        		ang += Math.PI / 2;
        		int newX = FindX(points, ang, out splitLine1, out splitLine2);
        		
        		Preconditions.checkState(newX < N);
        		
        		X = newX;
        		Logger.LogTrace("starting X now {}", X);
        	} else if (X == N) {
        		Logger.LogDebug("Done immediately lines {} {}", splitLine1, splitLine2);
        		return;
        	}
        	
        	int loopCheck = 0;
        	
        	double lowerBound = ang;
        	double upperBound = ang + Math.PI / 2;
        	
        	int checkX = FindX(points, upperBound, out splitLine1, out splitLine2);
        	
        	Logger.LogTrace("Check X {}", checkX);
        	Preconditions.checkState(X + checkX == 2 * N, "{0} check X".FormatThis(checkX));
        	
        	//Preconditions.checkState(lowerBound >= 0 && lowerBound < 2* Math.PI);
        	//Preconditions.checkState(upperBound >= 0 && upperBound < 2 *Math.PI);
        	
        	while(X < N)
        	{
        		Logger.LogTrace("Angles {} {}.  loop {}", lowerBound * 180d/Math.PI, upperBound * 180d/Math.PI,
        			loopCheck);
        		
        		++loopCheck;
        		if (loopCheck >= 50)
        		{
        			Logger.LogDebug("Loop check passed");
        			return;
        		}
        		Preconditions.checkState(X <= N && X >= 0, "X larger than N");
        		Preconditions.checkState(lowerBound <= upperBound);
        		
        		double medianAngle = (lowerBound + upperBound) / 2d;
        		
        		int medianX = FindX(points, medianAngle, out splitLine1, out splitLine2);
        		
        		Logger.LogTrace("X= {} median X= {} for angle {}", X, medianX, medianAngle * 180d/Math.PI);
        		
        		
        		//Preconditions.checkState(medianX >= X, "Median= {0}. X= {1}".FormatThis(medianX, X));
        		
        		
        		if (medianX == N)
        		{
        			Logger.LogDebug("Found {} {}", splitLine1, splitLine2);
        			return;
        		}
        		
        		if (medianX < N)
        		{
        			lowerBound = medianAngle;
        		} else {
        			upperBound = medianAngle;	
        		}
        		
        	}
        	
        	return;
        }
        
        public static int FindX(List<Point> points, double ang, out LineSegment<double> splitLine1, out LineSegment<double> splitLine2)
        {
        
        	PointD slope1 = getDirectionVector(ang);
        	PointD slope2 = getDirectionVector(ang + Math.PI / 2);
    		
    		bool[] inFirstHalf;
            bool[] inFirstHalf2;

            //LineSegment<double> splitLine1;
            //LineSegment<double> splitLine2;

            XSpot.splitPoints(slope1, points, out inFirstHalf, out splitLine1);
            XSpot.splitPoints(slope2, points, out inFirstHalf2, out splitLine2);

            int X = 0;
            for(int i = 0; i < points.Count; ++i)
            {
                if (inFirstHalf[i] && inFirstHalf2[i])
                	++X;                
            }
        	
            return X;
        }
        
        

        public static void splitPoints(PointD directionUnitVector,
        	List<Point> points, out bool[] inFirstHalf, out LineSegment<double> splitingLine)
        {
            List<IP_Pair> withCP = new List<IP_Pair>();

            /*
             *  A Cross product is the area of a parrallelogram with sides
             *  v1 and v2.  Because v1 is constant, the area is affected by 
             *  the distance to this line v1, as the area is BxH, B is v1 and H
             *  is the distance to the line!
             */
            for(int ptIdx = 0; ptIdx < points.Count; ++ptIdx)
            {
                Point pt = points[ptIdx];
                double cp = PointExt.CrossProduct2(directionUnitVector.X, directionUnitVector.Y, pt.X, pt.Y);

                //object cp = PointExt.CrossProduct(pt, testLine);

                //int cp = PointExt.CrossProduct2(pt.X, pt.Y, testLine.X, testLine.Y);

                //Logger.LogTrace("Pt {}.  cp {} type {}", pt, cp, cp.GetType());
                withCP.Add(new IP_Pair(cp, ptIdx));
            }

            withCP.Sort((lhs, rhs) => lhs.Item1.CompareTo(rhs.Item1));

            //Logger.LogTrace("Sorted {}",  withCP.ToCommaString());

            int secondHalfStart = points.Count / 2;

            PointD avg = (points[withCP[secondHalfStart - 1].Item2] + points[withCP[secondHalfStart].Item2]) / 2d;
            splitingLine = LineExt.createSegmentFromCoords(avg.X, avg.Y, avg.X + directionUnitVector.X, avg.Y + directionUnitVector.Y);
            inFirstHalf = new bool[points.Count];

            for(int i = 0; i < secondHalfStart; ++i)
            {
                inFirstHalf[withCP[i].Item2] = true;
            }
        }
        
        public static bool checkAnswer(List<Point> points, PointD ptIntersection, PointD ptOnX)
        {
        	
        	Func<double, Frac> convertFunc = (dbl) => BigFraction.createFromDouble(dbl, 9);
        	
        	Func<Frac, double> convertFuncToDouble = (f) => (double) f;
        	
        	Point<Frac> ptInt = ptIntersection.Convert(convertFunc);
        	Point<Frac> secPt = ptOnX.Convert(convertFunc);
        	
        	Logger.LogTrace("ptIntersection {}.  ptInt {}", ptIntersection, ptInt.Convert(convertFuncToDouble));
        	Logger.LogTrace("ptOnX {}.  secPt {}", ptOnX, secPt.Convert(convertFuncToDouble));
        	
        	Preconditions.checkState(points.Count % 4 == 0);
        	int N = points.Count / 4;
        	
        	LineSegment<Frac> preciceLine1 = LineExt.createSegmentFromCoordsB( (BigFraction) ptInt.X, ptInt.Y, secPt.X, secPt.Y );
        	
        	//Perp slop is neg reciprocal of -A/B so B/A
    		LineSegment<Frac> preciceLine2 = LineExt.createSegmentFromCoordsB( (BigFraction)ptInt.X, ptInt.Y, ptInt.X+preciceLine1.line.A,
    			ptInt.Y + preciceLine1.line.B);
    		
    		int[] quadCounts = new int[] {0,0,0,0};
    		
    		Point<Frac> vec1 = preciceLine1.p2 - preciceLine1.p1;
			Point<Frac> vec2 = preciceLine2.p2 - preciceLine2.p1;
			/*
			Logger.LogTrace("Vec1 {}. Slope={} Vec2 {} Slope {}.\nLine 1\n{} Line 2\n{}\nSlope 1= {} slope 2= {}", 
				vec1, 
				(double) (vec1.Y / vec1.X),
				vec2, 
				(double) (vec2.Y / vec2.X),
				preciceLine1, preciceLine2,
				-preciceLine1.line.A/preciceLine1.line.B,
				-preciceLine2.line.A/preciceLine2.line.B
				);*/
			
			Logger.LogTrace("Vec1 {} Vec2 {}.\nLine 1\n{} Line 2\n{}", vec1.Convert(convertFuncToDouble), 
				vec2.Convert(convertFuncToDouble), preciceLine1.Convert(convertFuncToDouble), preciceLine2.Convert(convertFuncToDouble));
			
			Drawing d = new Drawing();
			string[] colors = new string[] { "#FF7D2BA2", "#FF12CBC9", "#FFC5CB12", "#FFE31212" };
			
			d.AddAsLine(preciceLine1.Convert( (frac) => (double) frac));
            d.AddAsLine(preciceLine2.Convert( (frac) => (double) frac));
            
            Func<Line<Frac>, Point<int>, double> distToLineFunc =
            (line, pt) =>
            {
            	return (double) (line.A * pt.X + line.B * pt.Y - line.C).Abs() / Math.Sqrt( (double) (line.A*line.A + line.B*line.B));
            	
            };
			
    		for(int ptIdx = 0; ptIdx < points.Count; ++ptIdx)
    		{
    			
    			
    			Frac cp1 = PointExt.CrossProduct2( vec1.X, vec1.Y, points[ptIdx].X - preciceLine1.p1.X,
    				points[ptIdx].Y - preciceLine1.p1.Y );
    			Frac cp2 = PointExt.CrossProduct2( vec2.X, vec2.Y, points[ptIdx].X - preciceLine2.p1.X,
    				points[ptIdx].Y - preciceLine2.p1.Y );
    			
    			if (cp1 == 0)
    			{
    				Logger.LogTrace("Point {} on line {}", points[ptIdx], preciceLine1);
					return false;    				
    			}
    			if (cp2 == 0)
    			{
    				Logger.LogTrace("Point {} on line {}", points[ptIdx], preciceLine2);
					return false;    				
    			}
    			
    			Logger.LogTrace("Point {} Dist to line 1 {} Dist to line 2 {}",
    				points[ptIdx],
    				distToLineFunc(preciceLine1.line, points[ptIdx]),
    				distToLineFunc(preciceLine2.line, points[ptIdx]));
    			
    			Preconditions.checkState( points[ptIdx].X * preciceLine1.line.A +
    				points[ptIdx].Y * preciceLine1.line.B != preciceLine1.line.C );
    			
    			Preconditions.checkState( points[ptIdx].X * preciceLine2.line.A +
    				points[ptIdx].Y * preciceLine2.line.B != preciceLine2.line.C );
    			
    			
    			//Assert.IsTrue(cp1 != 0 && cp2 != 0);
    			
    			int quad = (cp1 < 0 ? 0 : 1) + (cp2 < 0 ? 0 : 2);
    			quadCounts[quad] ++;
    			
    			d.AddPoint(points[ptIdx], colors[quad]);

    		}
    		
    		//GeomXmlWriter.Save(d, @"/home/ent/e.lgf");
    		
    		for(int i = 0; i < 4; ++i)
    		{
    			if (N != quadCounts[i])
    			{
    				Logger.LogTrace("Count not ok");
    				
    				/*
    				double a = Math.Atan2(2000000-1, 1);
    				Logger.LogTrace("Angle {} {}", a, a * 180d / Math.PI);
    				a = Math.Atan2(2000000d, 1);
    				Logger.LogTrace("high Angle {} {}", a, a * 180d / Math.PI);
    				a = Math.Atan2(2000000-2, 1);
    				Logger.LogTrace("low Angle {} {}", a, a * 180d / Math.PI);
    				*/
    				return false;
    			}
    		}

            GeomXmlWriter.Save(d, @"C:\Users\thresh\Documents\e.lgf");
    		return true;
        }

        //Where no 3 lines are colinear, attempts N then removes colinear
        public static List<Point> generateTestSet(int N, int min = 0, int max = 100)
        {
        	 Random r = new Random(3);
        	 
        	 List<Point> points = new List<Point>();
        	 
        	 for(int i = 0; i < N; ++i)
        	 {
        	 	points.Add(new Point(r.Next(min, max+1), r.Next(min, max+1)) );
        	 }
        	 
        	 List<int> listIdxColinear;
        	 List<int> duplicates;
        	 
        	 FindColinear(points, out listIdxColinear, out duplicates);
        	 
        	 for(int dupIdx = 1; dupIdx < duplicates.Count; dupIdx += 2)
        	 {
        	 	points[duplicates[dupIdx]] = null;
        	 }
        	 
        	 for(int coLinIdx = 2; coLinIdx < listIdxColinear.Count; coLinIdx += 3)
        	 {
        	 	points[listIdxColinear[coLinIdx]] = null;
        	 }
        	 
        	 points.RemoveAll(p => p == null);
        	 
        	 #if DEBUG
        	 FindColinear(points, out listIdxColinear, out duplicates);
        	 Preconditions.checkState(listIdxColinear.Count == 0);
        	 Preconditions.checkState(duplicates.Count == 0);
        	 #endif
        	 
        	 return points;
        }

        //Returns list of colinear points in O(N), so for(idx=0; idx+=3) idx idx+1 idx+2 are colinear.  dups are not included in colinear list
        public static void FindColinear(List<Point> points, out List<int> listIdxColinear, out List<int> duplicates)
        {
        	listIdxColinear = new List<int>();
        	duplicates = new List<int>();
        	
        	bool[] isDup = new bool[points.Count];
        	
        	for(int p0Idx = 0; p0Idx < points.Count; ++p0Idx)
        	{
        		Dictionary<Point, int> slopePointMap = new Dictionary<Point, int>();
        		
        		Logger.LogTrace("Starting {} {}", p0Idx, points[p0Idx]);
        		
        		//Find all slopes with other points
        		for(int pIdx = p0Idx + 1; pIdx < points.Count; ++pIdx)
        		{
        			if (isDup[pIdx])
        				continue;
        		
        			Point dxdy = points[pIdx] - points[p0Idx];
        			
        			if (dxdy.Y == 0 && dxdy.X == 0)
        			{
        				duplicates.Add(p0Idx);
        				duplicates.Add(pIdx);
        				isDup[pIdx] = true;
        				continue;
        			}
        			
        			int gcd = (int) ModdedLong.gcd_recursive(dxdy.X, dxdy.Y);
        			if (gcd != 0) dxdy /= gcd;
        			
        			//Logger.LogTrace("\u0394x\u0394y between [idx : point] {} {} and {} {} is {}",
        			//	p0Idx, points[p0Idx], pIdx, points[pIdx], dxdy); 
        			
        			if (slopePointMap.ContainsKey(dxdy))
        			{
        				int pPrevIdx = slopePointMap[dxdy];
        				
        				if (points[pPrevIdx].Equals(points[pIdx]))
        				{
        					//Check 2 other points are not the exact same
        					isDup[pIdx] = true;
        					duplicates.Add(pPrevIdx);
        					duplicates.Add(pIdx);
        					continue;
        				}
        				listIdxColinear.Add(p0Idx);
        				
        				listIdxColinear.Add(pPrevIdx);
        				listIdxColinear.Add(pIdx);
        				continue;
        			}
        			
        			slopePointMap[dxdy] = pIdx;
        		}
        			
        	}
        	
        	//return listIdxColinear;
        }
    }



    public class XSpotInput
    {
        public int N;
        public List<Point<int>> points;
    }
}
