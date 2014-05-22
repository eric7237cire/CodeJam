#if DEBUG
#define LOGGING_DEBUG
#define LOGGING_INFO
#define LOGGING_TRACE
#endif

using System;
using NUnit;
using NUnit.Framework;

using RoundFinal;
using Utils.geom;
using System.Collections.Generic;
using Utils;

using Logger = Utils.LoggerFile;

namespace UnitTest
{
	using Point = Utils.geom.Point<int>;
	using IP_Pair = Tuple<int, Utils.geom.Point<int>>;
	
    [TestFixture]
    public class TestXSpot
    {
        
        public List<Point> getPts()
        {
            

            List<Point> points = new List<Point>();
            for (int i = 0; i < 5; ++i)
            {
                points.Add(new Point(3 + 2 * i, -17 + 9 * i));
            }

            for (int i = 0; i < 5; ++i)
            {
                points.Add(new Point(5 + 2 * i, -18 + 9 * i));
            }
            return points;
        }
    	[Test]
    	public void TestGenTestSet()
    	{
            int min = -10;
            int max = 50;

            int numPoints = 4 * 80;

    		List<Point> points = XSpot.generateTestSet(numPoints, min, max);
           // List<Point> points = getPts();
    		
    		//Logger.LogTrace("Test set:\n{}", points.ToCommaString());
    		
    		Point testLine = new Point(2, 9);
    		
    		List<IP_Pair> withCP = new List<IP_Pair>();
    		
            /*
             *  A Cross product is the area of a parrallelogram with sides
             *  v1 and v2.  Because v1 is constant, the area is affected by 
             *  the distance to this line v1, as the area is BxH, B is v1 and H
             *  is the distance to the line!
             */
    		foreach(Point pt in points)
    		{
    			int cp = pt.CrossProduct(testLine);
    			
    			//object cp = PointExt.CrossProduct(pt, testLine);
    			
    			//int cp = PointExt.CrossProduct2(pt.X, pt.Y, testLine.X, testLine.Y);
    			
    			//Logger.LogTrace("Pt {}.  cp {} type {}", pt, cp, cp.GetType());
    			withCP.Add( new IP_Pair( cp, pt ) );	
    		}
    		
    		withCP.Sort( (lhs, rhs) => lhs.Item1.CompareTo(rhs.Item1));
    		
    		//Logger.LogTrace("Sorted {}",  withCP.ToCommaString());

            int side2Idx = points.Count / 2;
            int side1Idx = side2Idx - 1;

            double distDir = Math.Sqrt(testLine.dist2(new Point(0,0)));
            Point p1 = withCP[side1Idx].Item2;
            Point p2 = withCP[side2Idx].Item2;
            int cp1 = withCP[side1Idx].Item1;
            int cp2 = withCP[side2Idx].Item1;
            double h1 = cp1  / distDir;
            double h2 = cp2 / distDir;
            Logger.LogTrace("Points cp {} {} and {} {}.  h {} {}", withCP[side1Idx].Item1, withCP[side1Idx].Item2, withCP[side2Idx].Item1, withCP[side2Idx].Item2, h1, h2);
            

            Drawing d = new Drawing();

            double ang = Math.PI / 2 -  Math.Atan2(testLine.Y, testLine.X);
            h1 = h1 * Math.Cos(ang);
            h2 = h2 * Math.Cos(ang);

          //  LineSegment<double> line1 = LineExt.createSegmentFromCoords(h1, 0, h1 + testLine.X, 0 + testLine.Y);
           // LineSegment<double> line2 = LineExt.createSegmentFromCoords(h2, 0, h2 + testLine.X, testLine.Y);

            LineSegment<int> line3 = LineExt.createSegmentFromCoords(p1.X, p1.Y, p1.X + testLine.X, p1.Y + testLine.Y);
            LineSegment<int> line4 = LineExt.createSegmentFromCoords(p2.X, p2.Y, p2.X + testLine.X, p2.Y + testLine.Y);


          //  d.AddAsLine(line1, "#FF7D2BA2"); //purple
          //  d.AddAsLine(line2, "#FF12CBC9"); //teal
            d.AddAsLine(line3, "#FFC5CB12"); //yellow
            d.AddAsLine(line4, "#FFE31212"); //red
            
            d.MaximalVisibleY = max + 1;
            d.MaximalVisibleX = max + 1;
            d.MinimalVisibleX = min - 1;
            d.MinimalVisibleY = min - 1;

            for(int i = 0; i < withCP.Count; ++i)
            {
                d.AddPoint(withCP[i].Item2, i <= side1Idx ? "#FFFF0000" : "#FF00FF00");
            }

            d.AddAsLine(LineExt.createSegmentFromCoords(0, 0, testLine.X, testLine.Y));

            GeomXmlWriter.Save(d, @"C:\Users\thresh\Documents\e.lgf");
    		/*
    		points = XSpot.generateTestSet(50, 10, 10);
    		
    		Logger.LogTrace("Test set:\n{}", points.ToCommaString());
    		
    		points = XSpot.generateTestSet(50, 0, 4);
    		
    		Logger.LogTrace("Test set:\n{}", points.ToCommaString());
    		*/
    	}
    	
        [Test]
        public void TestColinear()
        {
         	List<Point> points = new List<Point>();
         	points.Add(new Point(-17, 5));
         	points.Add(new Point(-17, 0));
         	points.Add(new Point(3 - 100*17, -93833 + 987*17));
         	
         	//3 , 4 , 5
         	points.Add(new Point(-17, -138383385));
         	points.Add(new Point(383838374, 0));         	
         	points.Add(new Point(3, -93833));
         	
         	//6 , 7 , 8
         	points.Add(new Point(-17, 0));
         	points.Add(new Point(383838373, 0));
         	points.Add(new Point(3 - 100*-45, -93833 + 987*-45));
         	
         	List<int> colinearIndexes;
         	List<int> dups;
         	
         	XSpot.FindColinear(points, out colinearIndexes, out dups);
         	
         	Logger.LogTrace("Point indexs {}", colinearIndexes.ToCommaString());
         	
         	int idx = 0;
         	Assert.AreEqual(0, colinearIndexes[idx++]);
         	Assert.AreEqual(1, colinearIndexes[idx++]);
         	Assert.AreEqual(3, colinearIndexes[idx++]);
         	
         	Assert.AreEqual(1, colinearIndexes[idx++]);
         	Assert.AreEqual(4, colinearIndexes[idx++]);
         	Assert.AreEqual(7, colinearIndexes[idx++]);
         	
         	Assert.AreEqual(2, colinearIndexes[idx++]);
         	Assert.AreEqual(5, colinearIndexes[idx++]);
         	Assert.AreEqual(8, colinearIndexes[idx++]);
         	
         	Assert.AreEqual(2, dups.Count);
         	Assert.AreEqual(1, dups[0]);
         	Assert.AreEqual(6, dups[1]);
         	
        }
        
    }
}
